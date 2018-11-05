package com.waes.palazares.scalableweb.controller;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import com.waes.palazares.scalableweb.domain.DifferenceResult;
import com.waes.palazares.scalableweb.exception.InavlidIdException;
import com.waes.palazares.scalableweb.exception.InvalidBase64Exception;
import com.waes.palazares.scalableweb.exception.InvalidRecordContentException;
import com.waes.palazares.scalableweb.service.DifferenceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Difference endpoints controller.
 * Provides put endpoints to add left and right side document and get endpoint for difference
 */
@Api(description = "Difference endpoints. Store base64 left and right strings, then get the difference")
@Controller
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
     * @throws InavlidIdException when id is empty or null
     * @throws InvalidBase64Exception when content is not valid base64 string
     */
    @PutMapping("{id}/left")
    public ResponseEntity<DifferenceRecord> putLeft(@PathVariable String id, @RequestBody String data) throws InavlidIdException, InvalidBase64Exception {
        return ResponseEntity.ok(service.putLeft(id, data));
    }

    /**
     * Endpoint to add the right side of the document.
     *
     * @param id id of the document
     * @param data base64 encoded content
     * @return Difference record persisted into storage
     * @throws InavlidIdException when id is empty or null
     * @throws InvalidBase64Exception when content is not valid base64 string
     */
    @PutMapping("{id}/right")
    public ResponseEntity<DifferenceRecord> putRight(@PathVariable String id, @RequestBody String data) throws InavlidIdException, InvalidBase64Exception {
        return ResponseEntity.ok(service.putRight(id, data));
    }

    /**
     * Endpoint to get the differences between left and right documents
     *
     * @param id id of the document
     * @return Difference result object with difference type and message
     * @throws InavlidIdException when id is empty or null
     * @throws InvalidRecordContentException when can't find record with provided id, only one side has been stored so far or record content is empty
     */
    @GetMapping("{id}")
    public ResponseEntity<DifferenceResult> getDifference(@PathVariable String id) throws InavlidIdException, InvalidRecordContentException {
        return ResponseEntity.ok(service.getDifference(id).getResult());
    }
}
