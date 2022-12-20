package org.example;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(value = PurchaseController.class)
class PurchaseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PurchaseService purchaseService;

    @Test
    void testHappyPath() throws Exception {

        PurchaseRequest purchaseRequest = new PurchaseRequest(null,
                null,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(0.1));
        when(purchaseService.calculate(eq(purchaseRequest)))
                .thenReturn(Purchase.fromVat(BigDecimal.valueOf(0.1),BigDecimal.valueOf(10)));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vatRate", 0.1);
        jsonObject.put("vatAmount", 10);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/purchasevat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObject.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult result = mockMvc
                .perform(request)
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).contains("\"vatAmount\":10");
        Assertions.assertThat(result.getResponse().getContentAsString()).contains("\"netAmount\":100");
        Assertions.assertThat(result.getResponse().getContentAsString()).contains("\"grossAmount\":110");
    }

    @Test
    void testNonNumericVatRateInput() throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vatRate", "half");
        jsonObject.put("vatAmount", 10);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/purchasevat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObject.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult result = mockMvc
                .perform(request)
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).contains("Couldn't parse request, invalid input.");
        Assertions.assertThat(result.getResponse().getStatus()).isEqualByComparingTo(400);
    }

    @Test
    void testNonNumericAmountInput() throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vatRate", 0.1);
        jsonObject.put("netAmount", "ten");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/purchasevat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObject.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult result = mockMvc
                .perform(request)
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).contains("Couldn't parse request, invalid input.");
        Assertions.assertThat(result.getResponse().getStatus()).isEqualByComparingTo(400);
    }

    @Test
    void testInternalError_InvalidVatRate() throws Exception {

        PurchaseRequest purchaseRequest = new PurchaseRequest(null,
                null,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(0.11));
        when(purchaseService.calculate(eq(purchaseRequest)))
                .thenThrow(new RuntimeException("Invalid vat rate. Only valid vat rates: " + PurchaseHelper.validVatRates));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vatRate", 0.11);
        jsonObject.put("vatAmount", 10);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/purchasevat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObject.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult result = mockMvc
                .perform(request)
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).contains("Invalid vat rate. Only valid vat rates: [0.1, 0.13, 0.2]");
        Assertions.assertThat(result.getResponse().getStatus()).isEqualByComparingTo(500);

    }
}