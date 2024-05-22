package com.project.MeltIngestion.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONArray;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class FileProcessor {
    @Autowired
    APIClientFactory apiClientFactory;

    @Autowired
    private ResourceLoader resourceLoader;

    public void process() {
        log.info("Preparing payload for ingestion");
        try {
            String csvFilePath = readFile("melt.csv");
            Map<String, JSONArray> typeJson = readFromCSV(csvFilePath);

            for (Map.Entry<String, JSONArray> entry : typeJson.entrySet()) {
                String type = entry.getKey();
                JSONArray value = entry.getValue();
                APIClient client = apiClientFactory.getClient(type);
                client.sendData(value);
            }
            log.info("Ingestion process completed");
        }
        catch (Exception e){
            log.error("Ingestion process failed due to ", e);
        }
    }

    public String readFile(String filePath) throws IOException {
        // This is picked from target/classes/melt.csv, each time we update something in csv we need clean and build.
        Resource resource = resourceLoader.getResource("classpath:" + filePath);
        return resource.getFile().getPath();
    }

    private Map<String, JSONArray> readFromCSV(String filePath){
        JSONArray jsonArray = new JSONArray();
        Map<String, JSONArray> typeJson = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] headers = reader.readNext();

            String[] row;
            while ((row = reader.readNext()) != null) {
                JSONObject jsonObject = new JSONObject();
                String telemetryType = row[0];
                for (int i = 1; i < headers.length; i++) {
                    String key = headers[i];
                    String value = row[i];

                    if(value.isEmpty()) continue;

                    if (("value".equals(key) && value.contains("{")) || ("attributes".equals(key))) {
                        JSONObject valueObject = new JSONObject(value);
                        jsonObject.put(key, valueObject);
                    } else if ("value".equals(key) || "amount".equals(key)) {
                        jsonObject.put(key, Double.parseDouble(value));
                    } else if ("interval.ms".equals(key) || "timestamp".equals(key) || "account".equals(key)) {
                        jsonObject.put(key, Long.parseLong(value));
                    } else {
                        jsonObject.put(key, value);
                    }
                }

                jsonObject.put("timestamp", Instant.now().toEpochMilli());
                typeJson.computeIfAbsent(telemetryType, k -> new JSONArray()).put(jsonObject);
            }
        } catch (IOException e) {
            log.error("Something went wrong in preparing json payload from the file", e);
        }

        return typeJson;
    }
}
