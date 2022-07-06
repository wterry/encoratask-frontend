package com.skytouch.task;

import com.skytouch.task.commons.dtos.responses.ProductResponse;
import com.skytouch.task.commons.dtos.responses.Response;
import com.skytouch.task.commons.dtos.responses.RestockInvoiceResponse;
import com.skytouch.task.commons.event.Event;
import com.skytouch.task.commons.event.services.Producer;
import com.skytouch.task.commons.utils.JsonUtils;
import com.skytouch.task.services.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(value = FrontendApplication.class)
public class TestFindControllers {

    @Autowired
    private MockMvc mockMvc;

//	@MockBean
//	private ProductService service;

    @MockBean
    private Producer producer;

    @Test
    public void testFindAllProducts() {
        try {
            MockitoAnnotations.initMocks(this);
            String searchJson = "[\n" +
                    "        {\n" +
                    "            \"id\": 14,\n" +
                    "            \"sku\": \"SKU01\",\n" +
                    "            \"description\": \"Product generated via Mock\",\n" +
                    "            \"inventoryStock\": 2,\n" +
                    "            \"lastModified\": \"2022-07-04 05:00:00\",\n" +
                    "            \"price\": 10.5\n" +
                    "        }\n" +
                    "    ]";

            List<ProductResponse> mockResponse = JsonUtils.getObjectListFromJson(searchJson, ProductResponse.class);
            Mockito.when(producer.requestReplyEvent(Mockito.any(Event.class))).thenReturn(searchJson);
//            Mockito.when(service.findAllProducts()).thenReturn(mockResponse);


            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products");

            MvcResult result = mockMvc.perform(requestBuilder).andReturn();

            System.out.println(result.getResponse().getContentAsString());

            Response responseObj = JsonUtils.getObjectFromJson(result.getResponse().getContentAsString(), Response.class);

            Assertions.assertThat(responseObj.getStatusCode()).isEqualTo(200);
            Assertions.assertThat(responseObj.getContent()).isNotNull();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindOneProducts() {
        try {
            MockitoAnnotations.initMocks(this);
            String searchJson = "{\n" +
                    "            \"id\": 14,\n" +
                    "            \"sku\": \"SKU01\",\n" +
                    "            \"description\": \"Product generated via Mock\",\n" +
                    "            \"inventoryStock\": 2,\n" +
                    "            \"lastModified\": \"2022-07-04 05:00:00\",\n" +
                    "            \"price\": 10.5\n" +
                    "        }\n";

            ProductResponse response = JsonUtils.getObjectFromJson(searchJson, ProductResponse.class);
            Mockito.when(producer.requestReplyEvent(Mockito.any(Event.class))).thenReturn(searchJson);
//            Mockito.when(service.findAllProducts()).thenReturn(mockResponse);


            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/{id}/", new Integer(14));

            MvcResult result = mockMvc.perform(requestBuilder).andReturn();

            System.out.println(result.getResponse().getContentAsString());

            Response responseObj = JsonUtils.getObjectFromJson(result.getResponse().getContentAsString(), Response.class);

            Assertions.assertThat(responseObj.getStatusCode()).isEqualTo(200);
            Assertions.assertThat(responseObj.getContent()).isNotNull();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRestockInventory() {
        try {
            MockitoAnnotations.initMocks(this);
            String procedureResult = "[\n" +
                    "        {\n" +
                    "            \"id\": 14,\n" +
                    "            \"sku\": \"SKU01\",\n" +
                    "            \"description\": \"Product generated via not need restock\",\n" +
                    "            \"missingStock\": 18,\n" +
                    "            \"restockPrice\": 189.0\n" +
                    "        }\n" +
                    "    ]";

            List<RestockInvoiceResponse> mockResponse = JsonUtils.getObjectListFromJson(procedureResult, RestockInvoiceResponse.class);
            Mockito.when(producer.requestReplyEvent(Mockito.any(Event.class))).thenReturn(procedureResult);

            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/inventory/restockInvoices");

            MvcResult result = mockMvc.perform(requestBuilder).andReturn();

            System.out.println(result.getResponse().getContentAsString());

            Response responseObj = JsonUtils.getObjectFromJson(result.getResponse().getContentAsString(), Response.class);

            Assertions.assertThat(responseObj.getStatusCode()).isEqualTo(200);
            Assertions.assertThat(responseObj.getContent()).isNotNull();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
