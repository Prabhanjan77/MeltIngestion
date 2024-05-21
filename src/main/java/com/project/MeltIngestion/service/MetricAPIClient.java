package com.project.MeltIngestion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
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
@Service("metrics")
public class MetricAPIClient implements APIClient {
    private static final String url = "https://metric-api.newrelic.com/metric/v1";

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void sendData(JSONArray array) {
        JSONArray jsonArray = new JSONArray();
        JSONObject metricsObject = new JSONObject().put("metrics", array);
        jsonArray.put(metricsObject);
        String jsonData = jsonArray.toString();
        log.info("Sending data to metric API: {}", jsonData);

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
        log.info("Request Id obtained from NewRelic API : {}", (String) feed.get("requestId"));
    }
}