package com.project.MeltIngestion.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONArray;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class FileProcessor {
    // get the data from the csv file and send it to new relic
    @Autowired
    APIClientFactory apiClientFactory;

    public void process() {
        log.info("Preparing payload for ingestion");
        String csvFilePath = "D:\\Chrome Downloads\\MeltIngestion\\src\\main\\resources\\Sheet1.csv";
        Map<String, JSONArray> typeJson = readFromCSV(csvFilePath);
        for (Map.Entry<String, JSONArray> entry : typeJson.entrySet()) {
            String type = entry.getKey();
            JSONArray value = entry.getValue();
            APIClient client = apiClientFactory.getClient(type);
            client.sendData(value);
        }
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
                jsonObject.put("name", row[1]);
                jsonObject.put("type", row[2]);
                if (row[3].contains("{")) {
                    JSONObject valueObject = new JSONObject(row[3]);
                    jsonObject.put("value", valueObject);
                } else {
                    jsonObject.put("value", Double.parseDouble(row[3]));
                }

                JSONObject attributes = new JSONObject(row[4]);
                jsonObject.put("attributes", attributes);

                if(!row[5].isEmpty())
                    jsonObject.put("interval.ms", Long.parseLong(row[5]));

                jsonObject.put("timestamp", Instant.now().toEpochMilli());

                jsonArray.put(jsonObject);
                typeJson.put(telemetryType, jsonArray);
            }
        } catch (IOException e) {
            log.error("Something went wrong in preparing json payload from the file", e);
        }

        return typeJson;
    }
}
