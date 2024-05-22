package com.project.MeltIngestion.service;

import com.project.MeltIngestion.util.UtilService;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service("traces")
public class TraceAPIClient implements APIClient {
    private static final String url = "https://trace-api.newrelic.com/trace/v1";

    @Autowired
    UtilService utilService;

    @Override
    public void sendData(JSONArray tracePayload) {
        JSONArray tracePayloadArray = new JSONArray();
        JSONObject traceObject = new JSONObject().put("spans", tracePayload);
        tracePayloadArray.put(traceObject);
        log.info("Sending data to trace API {}", tracePayloadArray);
        utilService.sendToAPI(url, tracePayloadArray);
    }
}
