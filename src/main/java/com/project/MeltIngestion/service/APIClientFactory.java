package com.project.MeltIngestion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class APIClientFactory {

    @Autowired
    ApplicationContext applicationContext;

    public APIClient getClient(String type) {
        return applicationContext.getBean(type, APIClient.class);
    }
}
