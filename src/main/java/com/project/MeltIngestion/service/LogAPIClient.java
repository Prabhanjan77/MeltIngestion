package com.project.MeltIngestion.service;

import com.project.MeltIngestion.util.UtilService;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service("logs")
public class LogAPIClient implements APIClient {
    private static final String url = "https://log-api.newrelic.com/log/v1";

    @Autowired
    UtilService utilService;

    @Override
    public void sendData(JSONArray logPayload) {
        JSONArray logPayloadArray = new JSONArray();
        JSONObject logObject = new JSONObject().put("logs", logPayload);
        logPayloadArray.put(logObject);
        log.info("Sending data to log API {} ", logPayloadArray);
        utilService.sendToAPI(url, logPayloadArray);
    }
}
