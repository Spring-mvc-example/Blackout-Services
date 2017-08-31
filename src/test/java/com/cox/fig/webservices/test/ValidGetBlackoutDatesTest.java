package com.cox.fig.webservices.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.cox.fscm.common.Constants;
import com.cox.fscm.commontypes.FscmBlackout;
import com.cox.fscm.commontypes.FscmRequest;
import com.cox.fscm.commontypes.FscmResponse;
import com.cox.fscm.commontypes.TransactionHeader;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
@WebAppConfiguration
public class ValidGetBlackoutDatesTest {

	private static final Logger LOGGER = LogManager
			.getLogger(Constants.FSCMADMINBLACKOUT);

	private MediaType contentType = new MediaType(
			MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testGetServicePass() throws Exception {
		TransactionHeader transactionHeader = new TransactionHeader();
		transactionHeader.setApplicationId("FSCM");
		transactionHeader.setTransactionId("111ABC");
		transactionHeader.setUserId("deeraina");
		FscmRequest inputRequest = new FscmRequest();
		inputRequest.setTransactionHeader(transactionHeader);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(inputRequest);
		LOGGER.info("REQUEST: " + jsonInString);
		MvcResult result = mockMvc
				.perform(
						post("/service/getBlackoutDates").content(jsonInString)
								.contentType(contentType))
				.andExpect(status().isOk()).andReturn();
		// Response
		String resultContent = result.getResponse().getContentAsString();
		FscmResponse output = mapper.readValue(resultContent,
				FscmResponse.class);
		LOGGER.info("RESPONSE: " + resultContent);
		Assert.assertTrue(output.getSuccessFlag().equals("true"));
	}

	 @Test
	public void testSetServicePass() throws Exception {
		TransactionHeader transactionHeader = new TransactionHeader();
		transactionHeader.setApplicationId("FSCM");
		transactionHeader.setTransactionId("111ABD");
		transactionHeader.setUserId("deeraina");
		FscmRequest inputRequest = new FscmRequest();
		inputRequest.setTransactionHeader(transactionHeader);

		List<FscmBlackout> inputDates = new ArrayList<FscmBlackout>();
		FscmBlackout modelOhio = new FscmBlackout();
		modelOhio.setBlackoutName("Christmas");
		modelOhio.setOpenDate("20-07-2016");
		modelOhio.setRegion("Ohio");
		inputDates.add(modelOhio);

		FscmBlackout modelSD = new FscmBlackout();
		modelSD.setBlackoutName("Christmas");
		modelSD.setOpenDate("20-07-2016");
		modelSD.setRegion("San Diego");
		inputDates.add(modelSD);
		inputRequest.setBlackoutList(inputDates);

		FscmBlackout modelDallas = new FscmBlackout();
		modelDallas.setBlackoutName("Christmas");
		modelDallas.setOpenDate("20-07-2016");
		modelDallas.setRegion("Dallas");
		inputDates.add(modelDallas);

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(inputRequest);
		LOGGER.info("REQUEST: " + jsonInString);
		MvcResult result = mockMvc
				.perform(
						post("/service/setBlackoutDates").content(jsonInString)
								.contentType(contentType))
				.andExpect(status().isOk()).andReturn();
		// Response
		String resultContent = result.getResponse().getContentAsString();
		FscmResponse output = mapper.readValue(resultContent,
				FscmResponse.class);
		LOGGER.info("RESPONSE: " + resultContent);
		Assert.assertTrue(output.getSuccessFlag().equals("true"));
	}

	 @Test
	public void testRemoveServicePass() throws Exception {
		TransactionHeader transactionHeader = new TransactionHeader();
		transactionHeader.setApplicationId("FSCM");
		transactionHeader.setTransactionId("111ABCDE");
		transactionHeader.setUserId("deeraina");
		FscmRequest inputRequest = new FscmRequest();
		inputRequest.setTransactionHeader(transactionHeader);

		List<FscmBlackout> inputDates = new ArrayList<FscmBlackout>();
		FscmBlackout modelDallas = new FscmBlackout();
		modelDallas.setBlackoutName("Christmas");
		modelDallas.setOpenDate("09/22/2016");
		modelDallas.setRegion("Ohio");
		inputDates.add(modelDallas);
		inputRequest.setBlackoutList(inputDates);

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(inputRequest);
		LOGGER.info("REQUEST: " + jsonInString);
		MvcResult result = mockMvc
				.perform(
						post("/service/removeBlackoutDates").content(
								jsonInString).contentType(contentType))
				.andExpect(status().isOk()).andReturn();
		// Response
		String resultContent = result.getResponse().getContentAsString();
		FscmResponse output = mapper.readValue(resultContent,
				FscmResponse.class);
		LOGGER.info("RESPONSE: " + resultContent);
		Assert.assertTrue(output.getSuccessFlag().equals("true"));
	}

	 @Test
	public void testRemoveServiceFail() throws Exception {
		TransactionHeader transactionHeader = new TransactionHeader();
		transactionHeader.setApplicationId("FSCM");
		transactionHeader.setTransactionId("111ABCDE");
		transactionHeader.setUserId("deeraina");
		FscmRequest inputRequest = new FscmRequest();
		inputRequest.setTransactionHeader(transactionHeader);

		List<FscmBlackout> inputDates = new ArrayList<FscmBlackout>();
		FscmBlackout modelDallas = new FscmBlackout();
		modelDallas.setBlackoutName("Christmas");
		modelDallas.setOpenDate("20-07-2016");
		modelDallas.setRegion("Atlanta");
		inputDates.add(modelDallas);
		inputRequest.setBlackoutList(inputDates);

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(inputRequest);
		LOGGER.info("REQUEST: " + jsonInString);
		MvcResult result = mockMvc
				.perform(
						post("/service/removeBlackoutDates").content(
								jsonInString).contentType(contentType))
				.andExpect(status().isOk()).andReturn();
		// Response
		String resultContent = result.getResponse().getContentAsString();
		FscmResponse output = mapper.readValue(resultContent,
				FscmResponse.class);
		LOGGER.info("RESPONSE: " + resultContent);
		Assert.assertTrue(output.getSuccessFlag().equals("false"));
	}

	 @Test 
	public void testCheckStatusPass() throws Exception {
		TransactionHeader transactionHeader = new TransactionHeader();
		transactionHeader.setApplicationId("FSCM");
		transactionHeader.setTransactionId("111ABC");
		transactionHeader.setUserId("deeraina");
		FscmRequest inputRequest = new FscmRequest();
		inputRequest.setTransactionHeader(transactionHeader);
		inputRequest.setExceptionStartDate("09/21/2016");
		inputRequest.setExceptionEndDate("09/30/2016");
		inputRequest.setExceptionRegion("California");

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(inputRequest);
		LOGGER.info("REQUEST: " + jsonInString);
		MvcResult result = mockMvc
				.perform(
						post("/service/checkBlackoutStatus").content(
								jsonInString).contentType(contentType))
				.andExpect(status().isOk()).andReturn();
		// Response
		String resultContent = result.getResponse().getContentAsString();
		FscmResponse output = mapper.readValue(resultContent,
				FscmResponse.class);
		LOGGER.info("RESPONSE: " + resultContent);
		Assert.assertTrue(output.getSuccessFlag().equals("true"));
	}
}