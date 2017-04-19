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
package com.alibaba.dubbo.demo.consumer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.alibaba.dubbo.demo.DemoService;
import com.alibaba.dubbo.demo.Person;

public class DemoAction {
    
    private DemoService demoService;
    private String[] persons = {"zhangsan","lisi","wangwu"};
    public void setDemoService(DemoService demoService) {
        this.demoService = demoService;
    }

	public void start() throws Exception {
		new Thread(new Runnable(){
			public void run() {
				Random r=new Random(); 
				for (int i = 0; i < Integer.MAX_VALUE; i ++) {
		            try {
		            	String name = "zhangsan_"+i;
		            	int ri = r.nextInt(6);
		            	if(ri >= 0 && ri < 3){
		            		name = persons[ri];
		            	}
		            	Person person = new Person(name,i,"s"+i);
		            	String hello = demoService.grayTest2(person, "cccc"+i);
		                System.out.println("grayTest2:[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + hello);
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		            try {
						Thread.sleep(5*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		        }
			}
		}).start();
		new Thread(new Runnable(){
			public void run() {
				Random r=new Random(); 
				for (int i = 0; i < Integer.MAX_VALUE; i ++) {
					try {
						String name = "zhangsan_"+i;
		            	int ri = r.nextInt(6);
		            	if(ri >= 0 && ri < 3){
		            		name = persons[ri];
		            	}
		            	Person person = new Person(name,i,"s"+i);
		            	String hello = demoService.sayHello(person);
						System.out.println("sayHello：[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + hello);
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}