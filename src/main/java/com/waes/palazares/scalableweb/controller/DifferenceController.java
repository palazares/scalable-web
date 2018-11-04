package com.waes.palazares.scalableweb.controller;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import com.waes.palazares.scalableweb.domain.DifferenceResult;
import com.waes.palazares.scalableweb.exception.InavlidIdException;
import com.waes.palazares.scalableweb.exception.InvalidBase64Exception;
import com.waes.palazares.scalableweb.exception.InvalidRecordContentException;
import com.waes.palazares.scalableweb.service.DifferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("v1/diff")
public class DifferenceController {
    private DifferenceService service;

    @Autowired
    public DifferenceController(DifferenceService service) {
        this.service = service;
    }

    @PutMapping("{id}/left")
    public ResponseEntity<DifferenceRecord> putLeft(@PathVariable String id, @RequestBody String data) throws InavlidIdException, InvalidBase64Exception {
        return ResponseEntity.ok(service.putLeft(id, data));
    }

    @PutMapping("{id}/right")
    public ResponseEntity<DifferenceRecord> putRight(@PathVariable String id, @RequestBody String data) throws InavlidIdException, InvalidBase64Exception {
        return ResponseEntity.ok(service.putRight(id, data));
    }

    @GetMapping("{id}")
    public ResponseEntity<DifferenceResult> getDifference(@PathVariable String id) throws InavlidIdException, InvalidRecordContentException {
        return ResponseEntity.ok(service.getDifference(id).getResult());
    }
}
