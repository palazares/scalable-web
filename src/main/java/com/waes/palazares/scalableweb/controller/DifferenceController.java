package com.waes.palazares.scalableweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import com.waes.palazares.scalableweb.domain.DifferenceResult;
import com.waes.palazares.scalableweb.service.DifferenceService;

import io.swagger.annotations.Api;
import reactor.core.publisher.Mono;

/**
 * Difference endpoints controller.
 * Provides put endpoints to add left and right side document and get endpoint for difference
 */
// This class can be removed as long as RouterFunction bean defines all required endpoints.
// However RouterFunction endpoints are not supported by swagger yet (09-2019). This class is required only for swagger support ATM
@Api("Difference endpoints. Store base64 left and right strings, then get the difference")
@RestController
@RequestMapping("v1/diff")
public class DifferenceController {
    private DifferenceService service;

    @Autowired
    public DifferenceController(DifferenceService service) {
        this.service = service;
    }

    /**
     * Endpoint to add the left side of the document.
     *
     * @param id id of the document
     * @param data base64 encoded content
     * @return Difference record persisted into storage
     */
    @PutMapping("{id}/left")
    public Mono<DifferenceRecord> putLeft(@PathVariable String id, @RequestBody String data) {
        return service.putLeft(id, data);
    }

    /**
     * Endpoint to add the right side of the document.
     *
     * @param id id of the document
     * @param data base64 encoded content
     * @return Difference record persisted into storage
     */
    @PutMapping("{id}/right")
    public Mono<DifferenceRecord> putRight(@PathVariable String id, @RequestBody String data) {
        return service.putRight(id, data);
    }

    /**
     * Endpoint to get the differences between left and right documents
     *
     * @param id id of the document
     * @return Difference result object with difference type and message
     */
    @GetMapping("{id}")
    public Mono<DifferenceResult> getDifference(@PathVariable String id) {
        return service.getDifference(id).map(DifferenceRecord::getResult);
    }
}
