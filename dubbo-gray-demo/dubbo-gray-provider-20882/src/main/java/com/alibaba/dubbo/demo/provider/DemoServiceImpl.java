/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.demo.provider;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.dubbo.demo.DemoService;
import com.alibaba.dubbo.demo.Person;
import com.alibaba.dubbo.rpc.RpcContext;

public class DemoServiceImpl implements DemoService {

    public String sayHello(Person person) {
        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + person.getName() + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return "sayHello:Hello " + person.getName() + ",age = " + person.getAge() + ", response form provider: " + RpcContext.getContext().getLocalAddress();
    }

	public String grayTest2(Person person, String arg2) {
		System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + person.getName() + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return "grayTest2:Hello " + person.getName() + ",age = " + person.getAge() + ", response form provider: " + RpcContext.getContext().getLocalAddress();
	}
    
}