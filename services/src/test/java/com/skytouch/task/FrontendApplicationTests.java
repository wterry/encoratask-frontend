package com.skytouch.task;

import com.skytouch.task.commons.dtos.requests.DeleteProductDTO;
import com.skytouch.task.commons.dtos.requests.ProductDTO;
import com.skytouch.task.commons.dtos.requests.UpdateProductDTO;
import com.skytouch.task.commons.dtos.responses.ProductResponse;
import com.skytouch.task.commons.dtos.responses.Response;
import com.skytouch.task.commons.dtos.responses.RestockInvoiceResponse;
import com.skytouch.task.commons.event.Event;
import com.skytouch.task.commons.event.services.Producer;

import com.skytouch.task.commons.utils.JsonUtils;
import com.skytouch.task.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;


@RunWith(SpringRunner.class)
@WebMvcTest(value = FrontendApplication.class)
@Slf4j
public class FrontendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private Producer producer;

	@Test
	public void testCreate(){

		ProductDTO testProduct = new ProductDTO();

		testProduct.setDescription("Test product created with JUnit");
		testProduct.setPrice(BigDecimal.TEN);
		testProduct.setSku("TESTSKU");
		testProduct.setInventoryStock(2);

		try {
			Mockito.when(producer.sendEvent(Mockito.any(Event.class))).thenReturn("placeholderCorrelationalId");


			RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/products/create")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtils.getJsonFromObject(testProduct));

			MvcResult result = mockMvc.perform(requestBuilder).andReturn();

			log.info(result.getResponse().getContentAsString());

			Response responseObj = JsonUtils.getObjectFromJson(result.getResponse().getContentAsString(), Response.class);

			Assertions.assertThat(responseObj.getStatusCode()).isEqualTo(200);
			Assertions.assertThat(responseObj.getContent()).isNotNull();
			Assertions.assertThat(responseObj.getMessage()).isEqualTo("Request queued");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testUpdate(){
		UpdateProductDTO testProduct = new UpdateProductDTO();

		testProduct.setId(1);
		testProduct.setDescription("Test product created with JUnit");
		testProduct.setPrice(BigDecimal.TEN);
		testProduct.setSku("TESTSKU");
		testProduct.setInventoryStock(2);

		try {
			Mockito.when(producer.sendEvent(Mockito.any(Event.class))).thenReturn("placeholderCorrelationalId");


			RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/products/update")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtils.getJsonFromObject(testProduct));

			MvcResult result = mockMvc.perform(requestBuilder).andReturn();

			log.info(result.getResponse().getContentAsString());

			Response responseObj = JsonUtils.getObjectFromJson(result.getResponse().getContentAsString(), Response.class);

			Assertions.assertThat(responseObj.getStatusCode()).isEqualTo(200);
			Assertions.assertThat(responseObj.getContent()).isNotNull();
			Assertions.assertThat(responseObj.getMessage()).isEqualTo("Request queued");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testDelete(){
		DeleteProductDTO testProduct = new DeleteProductDTO();

		testProduct.setId(1);

		try {
			Mockito.when(producer.sendEvent(Mockito.any(Event.class))).thenReturn("placeholderCorrelationalId");


			RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/products/delete")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtils.getJsonFromObject(testProduct));

			MvcResult result = mockMvc.perform(requestBuilder).andReturn();

			log.info(result.getResponse().getContentAsString());

			Response responseObj = JsonUtils.getObjectFromJson(result.getResponse().getContentAsString(), Response.class);

			Assertions.assertThat(responseObj.getStatusCode()).isEqualTo(200);
			Assertions.assertThat(responseObj.getContent()).isNotNull();
			Assertions.assertThat(responseObj.getMessage()).isEqualTo("Request queued");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testInvalidCreation() {

		ProductDTO testProduct = new ProductDTO();

		try {
			Mockito.when(producer.sendEvent(Mockito.any(Event.class))).thenReturn("placeholderCorrelationalId");


			RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/products/create")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtils.getJsonFromObject(testProduct));

			MvcResult result = mockMvc.perform(requestBuilder).andReturn();

			log.info(result.getResponse().getContentAsString());

			Response responseObj = JsonUtils.getObjectFromJson(result.getResponse().getContentAsString(), Response.class);

			Assertions.assertThat(responseObj.getStatusCode()).isEqualTo(400);
			Assertions.assertThat(responseObj.getContent()).isNotNull();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
