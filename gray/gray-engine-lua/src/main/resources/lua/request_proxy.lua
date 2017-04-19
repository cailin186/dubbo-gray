--加载三方库
local whitelistCache = ngx.shared.whitelistCache
local strategyCache = ngx.shared.strategyCache
local applicationId = ngx.var.cur_application_id

local requestUri = ngx.var.uri;
local applicationStatus = strategyCache:get(applicationId .. "_status")

if(not(applicationStatus) or applicationStatus == 0) then
	return ngx.var.cur_proxy_pass
end


local request_method = ngx.var.request_method
local args = nil

if "GET" == request_method then
    args = ngx.req.get_uri_args()
elseif "POST" == request_method then
    ngx.req.read_body()
    args = ngx.req.get_post_args()
end

--1.首先判断service是否存在，lua引擎中service也就是uri
local cacheUriKey = applicationId.. "_service_" .. requestUri
local serviceStatus = strategyCache:get(cacheUriKey .. "_status")
--local applicationUpstream = strategyCache:get(applicationId.. "_upstream")

--新生成的两个stream的名字
local grayUpstream = applicationId .. "_gray"
local newUpstream = applicationId .. "_new"

local function router(prefix,level)
        local serviceStrategyWay = strategyCache:get(prefix .. "_way")
        local serviceParam = strategyCache:get(prefix .. "_param")
        local paramValue = args[serviceParam]
	--获取参数为nil
	if(not(paramValue)) then
		return
	end
        if(serviceStrategyWay == "whitelist") then
                --白名单策略
                local cacheParamValue = whitelistCache:get(prefix .. "_whitelist_" .. serviceParam .. "_" .. paramValue)
                if(cacheParamValue == 1) then
                        --白名单存在，转发到灰度ip
                        return grayUpstream
                end
        elseif(serviceStrategyWay == "flowTatio") then
		--小流量
		local newval,err = strategyCache:incr(prefix .. "_count",1)
		local flowTatio = strategyCache:get(prefix .. "_flowTatio")

                if(newval % 100 <= flowTatio) then
			return grayUpstream
		end
        elseif(serviceStrategyWay == "ip") then
                ngx.say("othrer success")
        elseif(serviceStrategyWay == "weight") then
                ngx.say("othrer success")
        elseif(serviceStrategyWay == "business") then
                ngx.say("othrer success")
        elseif(serviceStrategyWay == "regular") then
		--正则表达式
                local regular =  strategyCache:get(prefix .. "_regular")
                local grayStartIndex,grayEndIndex = string.find(paramValue,regular)
                if(grayStartIndex) then
                        return grayUpstream
                end

        end
end

--1.1如果存在走service策略
if (serviceStatus and serviceStatus ~=0) then
	local routerResult = router(cacheUriKey,2)
        if(routerResult == grayUpstream) then
		return grayUpstream
	end
elseif(applicationStatus ==1) then
	local routerResult = router(applicationId,1)
        if(routerResult == grayUpstream) then
                return grayUpstream
        end
end


return applicationUpstream .. "_new"

