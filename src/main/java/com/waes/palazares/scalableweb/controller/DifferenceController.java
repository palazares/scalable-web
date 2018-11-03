package com.waes.palazares.scalableweb.controller;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import com.waes.palazares.scalableweb.domain.DifferenceResponse;
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
    public ResponseEntity<DifferenceRecord> putLeft(@PathVariable String id, @RequestBody String data) {
        service.putLeft(id, data);
        return ResponseEntity.ok(new DifferenceRecord());
    }

    @PutMapping("{id}/right")
    public ResponseEntity<DifferenceRecord> putRight(@PathVariable String id, @RequestBody String data) {
        service.putRight(id, data);
        return ResponseEntity.ok(new DifferenceRecord());
    }

    @GetMapping("{id}")
    public ResponseEntity<DifferenceResponse> getDifference(@PathVariable String id) {
        DifferenceRecord difference = service.getDifference(id);
        return ResponseEntity.ok(new DifferenceResponse());
    }
}
