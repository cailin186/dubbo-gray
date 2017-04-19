package com.xlz.event;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.gray.service.GrayCmdService;
import com.alibaba.dubbo.registry.NotifyListener;
import com.alibaba.dubbo.registry.RegistryService;

/**
 * RegistryContainer
 * 
 * @author zhangll
 */
public class RegistryContainer  {

    private final Set<String> applications = new ConcurrentHashSet<String>();

    private final Map<String, Set<String>> providerServiceApplications = new ConcurrentHashMap<String, Set<String>>();
    //应用-->接收命令接口
    private final Map<String, GrayCmdService> applicationCmds = new ConcurrentHashMap<String,GrayCmdService>();

    private final Map<String, Set<String>> providerApplicationServices = new ConcurrentHashMap<String, Set<String>>();

    private final Map<String, Set<String>> consumerServiceApplications = new ConcurrentHashMap<String, Set<String>>();

    private final Map<String, Set<String>> consumerApplicationServices = new ConcurrentHashMap<String, Set<String>>();

    private final Set<String> services = new ConcurrentHashSet<String>();

    private final Map<String, List<URL>> serviceProviders = new ConcurrentHashMap<String, List<URL>>();

    private final Map<String, List<URL>> serviceConsumers = new ConcurrentHashMap<String, List<URL>>();

    private RegistryService registryService;
    private ApplicationConfig application;
    private RegistryConfig registry;
    
    public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	public void setApplication(ApplicationConfig application) {
		this.application = application;
	}

	public void setRegistry(RegistryConfig registry) {
		this.registry = registry;
	}

	public Set<String> getApplications() {
        return Collections.unmodifiableSet(applications);
    }
    
    public Set<String> getDependencies(String application, boolean reverse) {
        if (reverse) {
            Set<String> dependencies = new HashSet<String>();
            Set<String> services = providerApplicationServices.get(application);
            if (services != null && services.size() > 0) {
                for (String service : services) {
                    Set<String> applications = consumerServiceApplications.get(service);
                    if (applications != null && applications.size() > 0) {
                        dependencies.addAll(applications);
                    }
                }
            }
            return dependencies;
        } else {
            Set<String> dependencies = new HashSet<String>();
            Set<String> services = consumerApplicationServices.get(application);
            if (services != null && services.size() > 0) {
                for (String service : services) {
                    Set<String> applications = providerServiceApplications.get(service);
                    if (applications != null && applications.size() > 0) {
                        dependencies.addAll(applications);
                    }
                }
            }
            return dependencies;
        }
    }

    public Set<String> getServices() {
        return Collections.unmodifiableSet(services);
    }

    public Map<String, List<URL>> getServiceProviders() {
        return Collections.unmodifiableMap(serviceProviders);
    }

    public List<URL> getProvidersByService(String service) {
        List<URL> urls = serviceProviders.get(service);
        return urls == null ? null : Collections.unmodifiableList(urls);
    }

    public List<URL> getProvidersByHost(String host) {
        List<URL> urls = new ArrayList<URL>();
        if (host != null && host.length() > 0) {
            for (List<URL> providers : serviceProviders.values()) {
                for (URL url : providers) {
                    if (host.equals(url.getHost())) {
                        urls.add(url);
                    }
                }
            }
        }
        return urls;
    }

    public List<URL> getProvidersByApplication(String application) {
        List<URL> urls = new ArrayList<URL>();
        if (application != null && application.length() > 0) {
            for (List<URL> providers : serviceProviders.values()) {
                for (URL url : providers) {
                    if (application.equals(url.getParameter(Constants.APPLICATION_KEY))) {
                        urls.add(url);
                    }
                }
            }
        }
        return urls;
    }

    public Set<String> getHosts() {
        Set<String> addresses = new HashSet<String>();
        for (List<URL> providers : serviceProviders.values()) {
            for (URL url : providers) {
                addresses.add(url.getHost());
            }
        }
        for (List<URL> providers : serviceConsumers.values()) {
            for (URL url : providers) {
                addresses.add(url.getHost());
            }
        }
        return addresses;
    }

    public Map<String, List<URL>> getServiceConsumers() {
        return Collections.unmodifiableMap(serviceConsumers);
    }

    public List<URL> getConsumersByService(String service) {
        List<URL> urls = serviceConsumers.get(service);
        return urls == null ? null : Collections.unmodifiableList(urls);
    }

    public List<URL> getConsumersByHost(String host) {
        List<URL> urls = new ArrayList<URL>();
        if (host != null && host.length() > 0) {
            for (List<URL> consumers : serviceConsumers.values()) {
                for (URL url : consumers) {
                    if (host.equals(url.getHost())) {
                        urls.add(url);
                    }
                }
            }
        }
        return Collections.unmodifiableList(urls);
    }

    public List<URL> getConsumersByApplication(String application) {
        List<URL> urls = new ArrayList<URL>();
        if (application != null && application.length() > 0) {
            for (List<URL> consumers : serviceConsumers.values()) {
                for (URL url : consumers) {
                    if (application.equals(url.getParameter(Constants.APPLICATION_KEY))) {
                        urls.add(url);
                    }
                }
            }
        }
        return urls;
    }

    public GrayCmdService getGrayCmdService(String application){
    	return applicationCmds.get(application);
    }
    public void start() {
        URL subscribeUrl = new URL(Constants.ADMIN_PROTOCOL, NetUtils.getLocalHost(), 0, "",
                                    Constants.INTERFACE_KEY, Constants.ANY_VALUE, 
                                    Constants.GROUP_KEY, Constants.ANY_VALUE, 
                                    Constants.VERSION_KEY, Constants.ANY_VALUE,
                                    Constants.CLASSIFIER_KEY, Constants.ANY_VALUE,
                                    Constants.CATEGORY_KEY, Constants.PROVIDERS_CATEGORY + "," 
                                            + Constants.CONSUMERS_CATEGORY,
                                    Constants.CHECK_KEY, String.valueOf(false));
        registryService.subscribe(subscribeUrl, new NotifyListener() {
            public void notify(List<URL> urls) {
                if (urls == null || urls.size() == 0) {
                    return;
                }
                Map<String, List<URL>> proivderMap = new HashMap<String, List<URL>>();
                Map<String, List<URL>> consumerMap = new HashMap<String, List<URL>>();
                for (URL url : urls) {
                    String application = url.getParameter(Constants.APPLICATION_KEY);
                    if (application != null && application.length() > 0) {
                        applications.add(application);
                    }
                    String service = url.getServiceInterface();
                    services.add(service);
                    String category = url.getParameter(Constants.CATEGORY_KEY, Constants.DEFAULT_CATEGORY);
                    if (Constants.PROVIDERS_CATEGORY.equals(category)) {
                        if (Constants.EMPTY_PROTOCOL.equals(url.getProtocol())) {
                            serviceProviders.remove(service);
                        } else {
                            List<URL> list = proivderMap.get(service);
                            if (list == null) {
                                list = new ArrayList<URL>();
                                proivderMap.put(service, list);
                            }
                            list.add(url);
                            
                            if (application != null && application.length() > 0) {
                                Set<String> serviceApplications = providerServiceApplications.get(service);
                                if (serviceApplications == null) {
                                    providerServiceApplications.put(service, new ConcurrentHashSet<String>());
                                    serviceApplications = providerServiceApplications.get(service);
                                }
                                serviceApplications.add(application);
        
                                Set<String> applicationServices = providerApplicationServices.get(application);
                                if (applicationServices == null) {
                                    providerApplicationServices.put(application, new ConcurrentHashSet<String>());
                                    applicationServices = providerApplicationServices.get(application);
                                }
                                applicationServices.add(service);
                            }
                        }
                        //为当前应用创建接收命令的接口
                        GrayCmdService grayCmdService = applicationCmds.get(application);
                        if(grayCmdService == null){
                        	grayCmdService = createDubboP2P(url.getHost(),url.getPort());
                        	if(grayCmdService != null)
                        		applicationCmds.put(application, grayCmdService);
                        }
                    } else if (Constants.CONSUMERS_CATEGORY.equals(category)) {
                        if (Constants.EMPTY_PROTOCOL.equals(url.getProtocol())) {
                            serviceConsumers.remove(service);
                        } else {
                            List<URL> list = consumerMap.get(service);
                            if (list == null) {
                                list = new ArrayList<URL>();
                                consumerMap.put(service, list);
                            }
                            list.add(url);
                            if (application != null && application.length() > 0) {
                                Set<String> serviceApplications = consumerServiceApplications.get(service);
                                if (serviceApplications == null) {
                                    consumerServiceApplications.put(service, new ConcurrentHashSet<String>());
                                    serviceApplications = consumerServiceApplications.get(service);
                                }
                                serviceApplications.add(application);
        
                                Set<String> applicationServices = consumerApplicationServices.get(application);
                                if (applicationServices == null) {
                                    consumerApplicationServices.put(application, new ConcurrentHashSet<String>());
                                    applicationServices = consumerApplicationServices.get(application);
                                }
                                applicationServices.add(service);
                            }
                        }
                    }
                }
                if (proivderMap != null && proivderMap.size() > 0) {
                    serviceProviders.putAll(proivderMap);
                }
                if (consumerMap != null && consumerMap.size() > 0) {
                    serviceConsumers.putAll(consumerMap);
                }
                
                //临时打印所有的url
                for (List<URL> consumers : serviceConsumers.values()) {
                    for (URL url : consumers) {
                        System.out.println("---------------------------------------"+url);
                    }
                }
                //临时打印所有的url
                for (List<URL> consumers : serviceProviders.values()) {
                	for (URL url : consumers) {
                		System.out.println("========================================"+url);
                	}
                }
                for (Map.Entry<String, GrayCmdService> consumers : applicationCmds.entrySet()) {
                	System.out.println("========================================"+consumers.getKey());
                }
            }
            
            private GrayCmdService createDubboP2P(String ip,int port){
            	try {
					ReferenceConfig<GrayCmdService> reference = new ReferenceConfig<GrayCmdService>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
					// 如果点对点直连，可以用reference.setUrl()指定目标地址，设置url后将绕过注册中心，
					// 其中，协议对应provider.setProtocol()的值，端口对应provider.setPort()的值，
					// 路径对应service.setPath()的值，如果未设置path，缺省path为接口名
					reference.setApplication(application);
					reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
					reference.setInterface(GrayCmdService.class);
					reference.setCluster("broadcast");
					//reference.setUrl("dubbo://"+ip+":"+port+"/com.alibaba.dubbo.gray.service.GrayCmdService");
					//System.out.println("--------------"+reference.getUrl());
					return reference.get();
				} catch (Exception e) {
					e.printStackTrace();
				}
            	return null;
            }
        });
    }
}