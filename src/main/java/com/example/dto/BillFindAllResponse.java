package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record BillFindAllResponse(
        Long billId,
        LocalDate issueDate,
        String status,
        String paymentMethod,
        BigDecimal total,
        String customerName,
        String billNumber
) {
}
