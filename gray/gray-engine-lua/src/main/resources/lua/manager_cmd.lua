--加载三方库
local http = require "resty.http"
local dkjson = require "dkjson"
local dyups = require "ngx.dyups"

local whitelistCache = ngx.shared.whitelistCache
local strategyCache = ngx.shared.strategyCache
local upstreamUrlManager = "http://127.0.0.1:65533/";

local function httpPost(url,inputMethod,bodyParam)
	local httpc = http.new()
	local resStr --响应结果
	local res, err = httpc:request_uri(url, {
    		method = inputMethod,
    		--args = str,
    		body = bodyParam,
    		headers = {
        		["Content-Type"] = "application/json",
    		}
	})

	if not res then
    		ngx.log(ngx.WARN,"failed to request: ", err)
    		return resStr
	end
	--请求之后，状态码
	--ngx.status = res.status
	if res.status ~= 200 then
    		ngx.log(ngx.WARN,"非200状态，ngx.status:"..ngx.status)
    		return resStr
	end
	--header中的信息遍历，只是为了方便看头部信息打的日志，用不到的话，可以不写的
	for key, val in pairs(res.headers) do
    		if type(val) == "table" then
        		ngx.log(ngx.WARN,"table:"..key, ": ", table.concat(val, ", "))
    		else
        		ngx.log(ngx.WARN,"one:"..key, ": ", val)
    		end
	end
	--响应的内容
	return res.body
end

local function setWhiteList(objs,prifix,pstatus)
	if(not(objs)) then
		return
	end
        for index,user in pairs(objs) do
                local param = dkjson.decode(user.param, 1, nil);
                for k,v in pairs(param) do
			if(user.status ==0 or pstatus ~= 1) then
				whitelistCache:delete(prifix  .. "_whitelist_" .. k .. "_" .. v)
			else
                        	whitelistCache:set(prifix  .. "_whitelist_" .. k .. "_" .. v,1)
			end
                end
        end
end

local function split(szFullString, szSeparator)
	local nFindStartIndex = 1
	local nSplitIndex = 1
	local nSplitArray = {}
	while true do
   		local nFindLastIndex = string.find(szFullString, szSeparator, nFindStartIndex)
   	if not nFindLastIndex then
    		nSplitArray[nSplitIndex] = string.sub(szFullString, nFindStartIndex, string.len(szFullString))
    		break
   	end
   	nSplitArray[nSplitIndex] = string.sub(szFullString, nFindStartIndex, nFindLastIndex - 1)
   	nFindStartIndex = nFindLastIndex + string.len(szSeparator)
   	nSplitIndex = nSplitIndex + 1
	end
	return nSplitArray
end

local function setCache(prefix,param,strategy,whiteList,cmd,status)
	if(status == 1 and cmd ~= "reloadApplicationWhitelist" and cmd ~= "reloadServiceWhitelist") then
        	strategyCache:set(prefix .. "_way",strategy.way)
        	strategyCache:set(prefix .. "_param",param)
        	strategyCache:set(prefix .. "_forwardReverse",strategy.forwardReverse)
        	strategyCache:set(prefix .. "_flowTatio",strategy.flowTatio)
        	strategyCache:set(prefix .. "_regular",strategy.regular)
		--初始小流量计数器
		if(strategy.way == 'flowTatio') then
			strategyCache:set(prefix .. "_count",0)
		end
		
	elseif(status ~= 1) then
		strategyCache:delete(prefix .. "_way")
                strategyCache:delete(prefix .. "_param")
                strategyCache:delete(prefix .. "_forwardReverse")
                strategyCache:delete(prefix .. "_flowTatio")
                strategyCache:delete(prefix .. "_regular")
		--小流量特有的参数
		strategyCache:delete(prefix .. "_count")
	end
        setWhiteList(whiteList,prefix,status)
end

local function deleteGrayUpstream(upstream)
        local grayUpstream = upstream .. "_gray"
        local newUpstream = upstream .. "_new"
        local deleteGray = httpPost(upstreamUrlManager .. "upstream/" .. grayUpstream,"DELETE","")
        local deleteNew = httpPost(upstreamUrlManager .. "upstream/" .. newUpstream,"DELETE","")

        if(deleteGray ~= "success" or deleteNew ~= "success") then
                return "关闭灰度异常"
        end

        return "success"
end

--logIsOpen 0未开启，1开启
local function manager(applicationId,logIsOpen,cmd)
	if(cmd == "closeAllGray") then
		--清空所有缓存
		strategyCache:flush_all()
    strategyCache:flush_expired()
    whitelistCache:flush_all()
    whitelistCache:flush_expired()

		--删除灰度用到的upstream
		local upstreams = httpPost(upstreamUrlManager .. "list","GET","")
		ngx.print(upstreams)
		local upstreamArray = split(upstreams,'\n')
		for index,upstream in pairs(upstreamArray) do
			if(upstream ~='') then
				ngx.say(upstream)
				local su = string.sub(upstream,-5)
				if(su == "gray") then
          	--ngx.say(string.sub(upstream,1,-5))
          	local deleteResult = deleteGrayUpstream(upstream);
          	if(logIsOpen == 1) then
                  	ngx.say(upstream .. deleteResult)
          	end
      	end
				ngx.print(su)
			end
		end
		return
	end

	local engineType="nginx"
	
        local url = "${gray.server.url}"
        url = url .. "?applicationId="..applicationId.."&engineType="..engineType
 
        local applicationConfigJson = httpPost(url,"POST","")
        local obj, pos, err = dkjson.decode(applicationConfigJson, 1, nil)
	if(obj.success and obj.code ~="J000000") then
		if(logIsOpen == 1) then
			ngx.print("请求服务端错误")
		end
		return
	end

	--解析json成功，进行进本数据校验和数据缓存
	local application = obj.obj
	if(application.applicationId ~= applicationId) then
		--打印错误日志
		if(logIsOpen == 1) then
			ngx.print("引擎制定的被灰度系统的id错误")
		end
		return 
	end

	local applicationStrategy = application.strategy
	if(tostring(applicationStrategy.type) ~= engineType) then
		--打印错误日志
		if(logIsOpen == 1) then
			ngx.print("引擎类型错误")
		end
		return
	end

	--判断状态是开启还是关闭，如果是关闭清空缓存然后删除灰度的upstream，返回成功
	local grayUpstream = application.applicationId .. "_gray"
	local newUpstream = application.applicationId .. "_new"	

	if(application.status == 0 and logIsOpen == 1) then
		strategyCache:delete(applicationId .. "_status")
		for index,service in pairs(application.services) do
			strategyCache:delete(applicationId .. '_service_' .. service.name)
		end	
		
		--strategyCache:delete(applicationId .. "_upstream")

		local deleteResult = deleteGrayUpstream(application.applicationId);	
		--if(logIsOpen == 1) then
		--	ngx.print(deleteResult)
		--end
		--return
	end

	if(cmd == "reloadApplicationWhitelist" or cmd =="startApplicationGray" or cmd == "reloadApplicationStrategy" or cmd ~= "reloadServiceStrategy") then
		setCache(applicationId,application.param,applicationStrategy,application.whitelists,cmd,application.status)
	end

	for index,service in pairs(application.services) do
		if(tostring(service.strategy.type) == engineType) then
			if(service.status == 1) then
				strategyCache:set(applicationId .. "_service_" .. service.name .. "_status",service.status)
			else
				strategyCache:delete(applicationId .. "_service_" .. service.name .. "_status")
			end
			setCache(applicationId .. "_service_" .. service.name,service.param,service.strategy,service.whiteLists,cmd,service.status)
		end
	end
	
	if((cmd == "startApplicationGray" or cmd == "startServiceGray") and application.status ~= 0) then	
		--基础数据加载成功，下一步需要根据配置创建灰度的upstream(upstream_gray,upstream_new)
		--获取当前upstream对应的配置
		local createGray = httpPost(upstreamUrlManager .. "upstream/" .. grayUpstream,"POST",application.grayIp)
        	local createNew = httpPost(upstreamUrlManager .. "upstream/" .. newUpstream,"POST",application.deployIp)	

		if(createGray ~= "success" or createNew ~= "success") then
			if(logIsOpen == 1) then
				ngx.print("创建灰度所用的upstream失败，请不要放入流量")
			end
			return
		end
	
		strategyCache:set(applicationId .. "_status",application.status)
		--strategyCache:set(applicationId .. "_upstream",application.applicationId)
	end 
	if(logIsOpen == 1) then	
		ngx.print("success")
	end
end

--接受管控平台的操作指令
if(not(applicationIds)) then
	local request_method = ngx.var.request_method
	local args = nil

	if "GET" == request_method then
    		args = ngx.req.get_uri_args()
	elseif "POST" == request_method then
    		ngx.req.read_body()
    		args = ngx.req.get_post_args()
	end

	local cmd = args["cmd"]
	local applicationId = args["applicationId"]
	manager(applicationId,1,cmd)
end

--nginx重启或者reload加载配置，重新按照配置生成upstream，启动灰度
function reloadInit(applicationIds)
	local newval,err = strategyCache:incr("workerTotal",1)
	if(newval ~= 1) then
		return
	end
	local function timerTask()
		for k,v in ipairs(applicationIds) do
                	manager(v,0,"startApplicationGray")
		end
        end
	
        local ok,err = ngx.timer.at(10,timerTask)
end
