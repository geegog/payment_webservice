package com.luminor.webservice.payment.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luminor.webservice.PaymentApplication;
import com.luminor.webservice.payment.application.dto.PaymentDTO;
import com.luminor.webservice.payment.domain.model.Status;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PaymentApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PaymentRestControllerTests {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testCreatePayment() throws Exception {
        String paymentDTO = "{\n" +
                "\t\"type\": \"TYPE3\",\n" +
                "\t\"creditorIBAN\": \"EE782200221070069410\",\n" +
                "\t\"debtorIBAN\": \"EE782200221070069410\",\n" +
                "\t\"BICCode\": \"24564275\",\n" +
                "\t\"money\": {\n" +
                "\t\t\"amount\": 500.00,\n" +
                "\t\t\"currency\": \"EUR\"\n" +
                "\t}\n" +
                "}";

        MvcResult result = mockMvc.perform(
                post("http://localhost:8080/api/payment/type/TYPE3/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentDTO))
                .andExpect(status().isCreated())
                .andReturn();

        PaymentDTO payment =
                mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assert.assertNotNull(result);
        Assert.assertEquals(201, result.getResponse().getStatus());
        Assert.assertEquals(Status.CREATED.name(), payment.getStatus());

    }

    @Test
    public void testCancelPayment() throws Exception {
        String paymentDTO = "{\n" +
                "\t\"type\": \"TYPE3\",\n" +
                "\t\"creditorIBAN\": \"EE782200221070069410\",\n" +
                "\t\"debtorIBAN\": \"EE782200221070069410\",\n" +
                "\t\"BICCode\": \"24564275\",\n" +
                "\t\"money\": {\n" +
                "\t\t\"amount\": 500.00,\n" +
                "\t\t\"currency\": \"EUR\"\n" +
                "\t}\n" +
                "}";

        MvcResult result = mockMvc.perform(
                post("http://localhost:8080/api/payment/type/TYPE3/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentDTO))
                .andExpect(status().isCreated())
                .andReturn();

        PaymentDTO payment =
                mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assert.assertNotNull(result);
        Assert.assertEquals(201, result.getResponse().getStatus());
        Assert.assertEquals(Status.CREATED.name(), payment.getStatus());

        String cancelUrl = "http://localhost:8080/api/payment/" + payment.getId() + "/cancel";

        MvcResult cancelResult = mockMvc.perform(
                patch(cancelUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();

        PaymentDTO paymentCancelled =
                mapper.readValue(cancelResult.getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assert.assertNotNull(cancelResult);
        Assert.assertEquals(202, cancelResult.getResponse().getStatus());
        Assert.assertEquals(Status.CANCELLED.name(), paymentCancelled.getStatus());

    }

}
