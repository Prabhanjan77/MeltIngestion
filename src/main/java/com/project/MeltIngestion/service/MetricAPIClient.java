package com.project.MeltIngestion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.MeltIngestion.util.UtilService;
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

    @Autowired
    private UtilService utilService;

    @Override
    public void sendData(JSONArray metricPayload) {
        JSONArray metricPayloadArray = new JSONArray();
        JSONObject metricsObject = new JSONObject().put("metrics", metricPayload);
        metricPayloadArray.put(metricsObject);
        log.info("Sending data to metric API: {}", metricPayloadArray.toString());
        utilService.sendToAPI(url, metricPayloadArray);
    }
}