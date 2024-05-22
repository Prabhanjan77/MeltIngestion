package com.project.MeltIngestion.service;

import com.project.MeltIngestion.util.UtilService;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service("events")
public class EventAPIClient implements APIClient {
    private static final String url = "https://insights-collector.newrelic.com/v1/accounts/4454598/events";

    @Autowired
    UtilService utilService;

    @Override
    public void sendData(JSONArray eventPayload) {
        log.info("Sending data to event API: {}", eventPayload.toString());
        utilService.sendToAPI(url, eventPayload);
    }
}
