package com.example.controller;

import com.example.dto.BillFindAllResponse;
import com.example.dto.BillRequest;
import com.example.dto.BillResponse;
import com.example.service.BillService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillRestController {

    private final BillService billService;

    public BillRestController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public ResponseEntity<List<BillFindAllResponse>> get() {
        var bills = billService.findAll();
        return ResponseEntity.ok(bills);
    }

    @PostMapping
    public ResponseEntity<BillResponse> create(@Valid @RequestBody BillRequest request) {
        var bill = billService.create(request);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(bill.billId()).toUri();
        return ResponseEntity.created(location).body(bill);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponse> findOne(@PathVariable Long id) {
        var bill = billService.findOne(id);
        return ResponseEntity.ok(bill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody BillRequest request) {
        var bill = billService.update(id, request);
        return ResponseEntity.ok(bill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        billService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
