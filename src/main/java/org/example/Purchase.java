package org.example;

import lombok.Data;

import java.math.BigDecimal;
import java.math.MathContext;

@Data
public class Purchase {
    private BigDecimal netAmount;
    private BigDecimal grossAmount;
    private BigDecimal vatAmount;

    public static Purchase fromGross(BigDecimal vatRate, BigDecimal grossAmount) {
        Purchase purchase = new Purchase();
        purchase.grossAmount = grossAmount;
        purchase.netAmount = purchase.grossAmount.divide(BigDecimal.ONE.add(vatRate), MathContext.DECIMAL32);
        purchase.vatAmount = purchase.grossAmount.subtract(purchase.netAmount);
        return purchase;
    }

    public static Purchase fromNet(BigDecimal vatRate, BigDecimal netAmount) {
        Purchase purchase = new Purchase();
        purchase.netAmount = netAmount;
        purchase.vatAmount = purchase.netAmount.multiply(vatRate, MathContext.DECIMAL32);
        purchase.grossAmount = purchase.netAmount.add(purchase.vatAmount);
        return purchase;
    }


    public static Purchase fromVat(BigDecimal vatRate, BigDecimal vatAmount) {
        Purchase purchase = new Purchase();
        purchase.vatAmount = vatAmount;
        purchase.netAmount = vatAmount.divide(vatRate, MathContext.DECIMAL32);
        purchase.grossAmount = purchase.netAmount.add(vatAmount);
        return purchase;
    }
}
