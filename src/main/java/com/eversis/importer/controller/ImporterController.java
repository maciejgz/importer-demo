package com.eversis.importer.controller;

import com.eversis.importer.service.ImporterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ImporterController {

    private final ImporterService importerService;

    @Autowired
    public ImporterController(ImporterService importerService) {
        this.importerService = importerService;
    }

    @GetMapping("/runJob")
    public ResponseEntity<String> runJob(@RequestParam(name = "probes") long probes) {
        log.debug("run job");
        importerService.importData(probes);
        return ResponseEntity.ok("done");
    }
}
