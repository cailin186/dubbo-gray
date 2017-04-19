package com.alibaba.dubbo.gray.data;

import java.util.HashMap;
import java.util.Map;

public class GrayDataCache {

	private Map<String,Application> cache = new HashMap<String,Application>();
	
	private static final GrayDataCache instance = new GrayDataCache();
	
	public static GrayDataCache getInstance(){
		return instance;
	}
	
	public Application getApplication(String applicationId){
		return cache.get(applicationId);
	}
	
	public Application putApplication(Application application){
		return cache.put(application.getApplicationId(),application);
	}
	
	public Application removeApplication(Application application){
		return cache.remove(application.getApplicationId());
	}
}
