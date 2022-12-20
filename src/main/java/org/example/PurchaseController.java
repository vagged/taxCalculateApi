package org.example;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/purchasevat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Purchase calculatePurchaseAmounts(@RequestBody PurchaseRequest purchaseRequest) {
        objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        return purchaseService.calculate(purchaseRequest);
    }


}
