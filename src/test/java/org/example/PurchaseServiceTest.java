package org.example;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;


@SpringBootTest
class PurchaseServiceTest {

    @Autowired
    private PurchaseService purchaseService;

    @Test
    void testGrossRequest() {
        PurchaseRequest purchaseRequest = new PurchaseRequest(null, BigDecimal.valueOf(113), null, BigDecimal.valueOf(0.13));
        Purchase purchase = purchaseService.calculate(purchaseRequest);
        Assertions.assertThat(purchase.getNetAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        Assertions.assertThat(purchase.getVatAmount()).isEqualByComparingTo(BigDecimal.valueOf(13));
        Assertions.assertThat(purchase.getGrossAmount()).isEqualByComparingTo(BigDecimal.valueOf(113));
    }

    @Test
    void testNetRequest() {
        PurchaseRequest purchaseRequest = new PurchaseRequest(BigDecimal.valueOf(1111), null, null, BigDecimal.valueOf(0.2));
        Purchase purchase = purchaseService.calculate(purchaseRequest);
        Assertions.assertThat(purchase.getNetAmount()).isEqualByComparingTo(BigDecimal.valueOf(1111));
        Assertions.assertThat(purchase.getVatAmount()).isEqualByComparingTo(BigDecimal.valueOf(222.2));
        Assertions.assertThat(purchase.getGrossAmount()).isEqualByComparingTo(BigDecimal.valueOf(1333.2));
    }

    @Test
    void testVatRequest() {
        PurchaseRequest purchaseRequest = new PurchaseRequest(null, null, BigDecimal.valueOf(20), BigDecimal.valueOf(0.1));
        Purchase purchase = purchaseService.calculate(purchaseRequest);
        Assertions.assertThat(purchase.getNetAmount()).isEqualByComparingTo(BigDecimal.valueOf(200));
        Assertions.assertThat(purchase.getVatAmount()).isEqualByComparingTo(BigDecimal.valueOf(20));
        Assertions.assertThat(purchase.getGrossAmount()).isEqualByComparingTo(BigDecimal.valueOf(220));
    }

    @Test
    void testNoVatRate() {
        PurchaseRequest purchaseRequest = new PurchaseRequest(null, null, BigDecimal.valueOf(20), null);
        try {
            purchaseService.calculate(purchaseRequest);
        }
        catch (RuntimeException e)
        {
            Assertions.assertThat(e.getMessage()).isEqualTo("Must give a vat rate input.");
        }
    }

    @Test
    void testInvalidVatRate() {
        PurchaseRequest purchaseRequest = new PurchaseRequest(null, null, BigDecimal.valueOf(20),  BigDecimal.valueOf(0.11));
        try {
            purchaseService.calculate(purchaseRequest);
        }
        catch (RuntimeException e)
        {
            Assertions.assertThat(e.getMessage()).isEqualTo("Invalid vat rate. Only valid vat rates: [0.1, 0.13, 0.2]");
        }
    }

    @Test
    void testNoAmount() {
        PurchaseRequest purchaseRequest = new PurchaseRequest(null, null, null, BigDecimal.valueOf(0.1));
        try {
            purchaseService.calculate(purchaseRequest);
        }
        catch (RuntimeException e)
        {
            Assertions.assertThat(e.getMessage()).isEqualTo("Must give one amount input.");
        }
    }

    @Test
    void testMultipleAmount() {
        PurchaseRequest purchaseRequest = new PurchaseRequest(BigDecimal.valueOf(100), null, BigDecimal.valueOf(10), BigDecimal.valueOf(0.1));
        try {
            purchaseService.calculate(purchaseRequest);
        }
        catch (RuntimeException e)
        {
            Assertions.assertThat(e.getMessage()).isEqualTo("Can't give multiple amount inputs.");
        }
    }


    @Test
    void testZeroNetAmount() {
        PurchaseRequest purchaseRequest = new PurchaseRequest(BigDecimal.valueOf(0), null, null, BigDecimal.valueOf(0.1));
        try {
            purchaseService.calculate(purchaseRequest);
        }
        catch (RuntimeException e)
        {
            Assertions.assertThat(e.getMessage()).isEqualTo("Net amount must be positive number.");
        }
    }

    @Test
    void testZeroVatAmount() {
        PurchaseRequest purchaseRequest = new PurchaseRequest(null, null, BigDecimal.valueOf(0), BigDecimal.valueOf(0.1));
        try {
            purchaseService.calculate(purchaseRequest);
        }
        catch (RuntimeException e)
        {
            Assertions.assertThat(e.getMessage()).isEqualTo("Vat amount must be positive number.");
        }
    }

    @Test
    void testZeroGrossAmount() {
        PurchaseRequest purchaseRequest = new PurchaseRequest(null, BigDecimal.valueOf(0), null, BigDecimal.valueOf(0.1));
        try {
            purchaseService.calculate(purchaseRequest);
        }
        catch (RuntimeException e)
        {
            Assertions.assertThat(e.getMessage()).isEqualTo("Gross amount must be positive number.");
        }
    }


}