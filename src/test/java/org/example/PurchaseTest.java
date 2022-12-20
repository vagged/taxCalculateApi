package org.example;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class PurchaseTest {

    @Test
    void testFromGross() {
        Purchase purchase = Purchase.fromGross(BigDecimal.valueOf(0.1), BigDecimal.valueOf(110));
        Assertions.assertThat(purchase.getNetAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        Assertions.assertThat(purchase.getVatAmount()).isEqualByComparingTo(BigDecimal.valueOf(10));
        Assertions.assertThat(purchase.getGrossAmount()).isEqualByComparingTo(BigDecimal.valueOf(110));
    }

    @Test
    void testFromNet() {
        Purchase purchase = Purchase.fromNet(BigDecimal.valueOf(0.1), BigDecimal.valueOf(100));
        Assertions.assertThat(purchase.getNetAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        Assertions.assertThat(purchase.getVatAmount()).isEqualByComparingTo(BigDecimal.valueOf(10));
        Assertions.assertThat(purchase.getGrossAmount()).isEqualByComparingTo(BigDecimal.valueOf(110));
    }

    @Test
    void testFromVat() {
        Purchase purchase = Purchase.fromVat(BigDecimal.valueOf(0.1), BigDecimal.valueOf(10));
        Assertions.assertThat(purchase.getNetAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        Assertions.assertThat(purchase.getVatAmount()).isEqualByComparingTo(BigDecimal.valueOf(10));
        Assertions.assertThat(purchase.getGrossAmount()).isEqualByComparingTo(BigDecimal.valueOf(110));
    }
}