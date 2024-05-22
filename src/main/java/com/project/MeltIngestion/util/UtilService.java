package com.project.MeltIngestion.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class UtilService {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    RestTemplate restTemplate;

    public void sendToAPI(String url, JSONArray jsonArray){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Api-Key", apiKey);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<String> reqEntity = new HttpEntity<>(jsonArray.toString(), headers);
        ResponseEntity<String> respEntity = restTemplate.postForEntity(url, reqEntity, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> feed = new HashMap<>();
        try {
            feed = mapper.readValue(respEntity.getBody(), Map.class);
        }
        catch (IOException e) {
            int statusCode = respEntity.getStatusCodeValue();
            log.error("Could not read the body from API. API response code: " + statusCode, e);
        }

        log.info("RequestID/UUID obtained from NewRelic API : {}", feed);
    }
}
