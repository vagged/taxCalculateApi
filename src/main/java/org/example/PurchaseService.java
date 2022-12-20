package org.example;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {
    public Purchase calculate(PurchaseRequest purchaseRequest) {
        if (purchaseRequest.getVatRate() == null)
            throw new RuntimeException("Must give a vat rate input.");

        if (!PurchaseHelper.validVatRates.contains(purchaseRequest.getVatRate()))
            throw new RuntimeException("Invalid vat rate. Only valid vat rates: " + PurchaseHelper.validVatRates);

        CalculationType calculationType = calculationType(purchaseRequest);

        //I made the assumption that negative amounts are invalid as well, if not, then I just replace >= with =.
        switch (calculationType) {
            case NET -> {
                if (BigDecimal.ZERO.compareTo(purchaseRequest.getNetAmount()) >= 0)
                    throw new RuntimeException("Net amount must be positive number.");
                return Purchase.fromNet(purchaseRequest.getVatRate(), purchaseRequest.getNetAmount());
            }
            case VAT -> {
                if (BigDecimal.ZERO.compareTo(purchaseRequest.getVatAmount()) >= 0)
                    throw new RuntimeException("Vat amount must be positive number.");
                return Purchase.fromVat(purchaseRequest.getVatRate(), purchaseRequest.getVatAmount());
            }
            case GROSS -> {
                if (BigDecimal.ZERO.compareTo(purchaseRequest.getGrossAmount()) >= 0)
                    throw new RuntimeException("Gross amount must be positive number.");
                return Purchase.fromGross(purchaseRequest.getVatRate(), purchaseRequest.getGrossAmount());
            }
            //Should not be possible
            default ->
                throw new IllegalArgumentException("Unknow type of calculation requested.");
        }
    }

    private CalculationType calculationType(PurchaseRequest purchaseRequest) {
        List<CalculationType> amountsReceived = amountsRecieved(purchaseRequest);

        if (amountsReceived.size() < 1)
            throw new RuntimeException("Must give one amount input.");

        if (amountsReceived.size() > 1)
            throw new RuntimeException("Can't give multiple amount inputs.");

        return amountsReceived.get(0);
    }

    private List<CalculationType> amountsRecieved(PurchaseRequest purchaseRequest) {
        List<CalculationType> receivedAmounts = new ArrayList<>();
        if (purchaseRequest.getNetAmount() != null)
            receivedAmounts.add(CalculationType.NET);

        if (purchaseRequest.getVatAmount() != null)
            receivedAmounts.add(CalculationType.VAT);

        if (purchaseRequest.getGrossAmount() != null)
            receivedAmounts.add(CalculationType.GROSS);

        return receivedAmounts;

    }

}
