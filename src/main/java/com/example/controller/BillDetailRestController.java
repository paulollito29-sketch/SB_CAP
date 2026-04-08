package com.example.controller;

import com.example.dto.BillDetailRequest;
import com.example.dto.BillDetailResponse;
import com.example.service.BillDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bill-details")
public class BillDetailRestController {

    private final BillDetailService billDetailService;

    public BillDetailRestController(BillDetailService billDetailService) {
        this.billDetailService = billDetailService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillDetailResponse> update(@PathVariable Long id, @RequestBody BillDetailRequest dto) {
        var billDetail = billDetailService.update(id, dto);
        return ResponseEntity.ok(billDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        billDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
