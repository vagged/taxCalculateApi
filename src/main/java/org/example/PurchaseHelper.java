package org.example;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class PurchaseHelper {

    public static final List<BigDecimal> validVatRates = Arrays.asList(BigDecimal.valueOf(0.1),
            BigDecimal.valueOf(0.13),
            BigDecimal.valueOf(0.2));
}
