package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PurchaseRequest {
    private BigDecimal netAmount;
    private BigDecimal grossAmount;
    private BigDecimal vatAmount;
    private BigDecimal vatRate;
}
