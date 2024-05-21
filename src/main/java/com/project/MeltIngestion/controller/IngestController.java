package com.project.MeltIngestion.controller;

import com.project.MeltIngestion.service.FileProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class IngestController {

    @Autowired
    FileProcessor fileProcessor;

    @GetMapping("/ingest")
    @ResponseStatus(value = HttpStatus.OK)
    public String ingestLogs() {
        fileProcessor.process();
        return "Success";
    }
}
