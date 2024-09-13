package org.alwayslearning.frauddetection.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscoveryClientService {

  @Autowired
  private DiscoveryClient discoveryClient;

  public String getServiceUrl(String serviceName) {
    List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
    if (instances.isEmpty()) {
      return null;
    }
    // For simplicity, just return the URL of the first instance
    return instances.get(0).getUri().toString();
  }
}

