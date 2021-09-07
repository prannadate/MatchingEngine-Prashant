package com.prashant.matchengine.controller;

import com.prashant.Utility;
import com.prashant.matchengine.models.*;
import com.prashant.matchengine.services.OrderMatchingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {

	private MockMvc mockMvc;

	@Mock
	OrderMatchingServiceImpl orderMatchingServiceMock;

	@InjectMocks
	private OrderController orderControllerMock;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(orderControllerMock)
//				.addFilters(new CORSFilter())
				.build();
	}

	@Test
	public void test_get_security() throws Exception {
		Security security = new Security(
				new ArrayList<BidOffer>() {{ add(new BidOffer (1, 2)); }},
				new ArrayList<AskOffer>() {{ add(new AskOffer (3, 4)); }},
				new ArrayList<Order>() {{ add(new AskOffer (3, 4)); }}
		);
		when(orderMatchingServiceMock.getSecurity()).thenReturn(security);
		mockMvc.perform(get("/security"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(Utility.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.buys.length()", is(1)))
				.andExpect(jsonPath("$.buys[0].qty", is(1)))
				.andExpect(jsonPath("$.buys[0].prc", is(2.0)))
				.andExpect(jsonPath("$.sells.length()", is(1)))
				.andExpect(jsonPath("$.sells[0].qty", is(3)))
				.andExpect(jsonPath("$.sells[0].prc", is(4.0)))
		;
	}

	@Test
	public void test_post_buy() throws Exception {
		BidOffer bidOffer = new BidOffer(
				1, 2.0
		);
		mockMvc.perform(post("/buy")
						.contentType(Utility.APPLICATION_JSON_UTF8)
						.content(Utility.convertObjectToJsonBytes(bidOffer))
				)
				.andDo(print())
				.andExpect(status().isOk())
		;
	}

	@Test
	public void test_post_sell() throws Exception {
		AskOffer askOffer = new AskOffer(
				1, 2.0
		);
		mockMvc.perform(post("/sell")
						.contentType(Utility.APPLICATION_JSON_UTF8)
						.content(Utility.convertObjectToJsonBytes(askOffer))
				)
				.andDo(print())
				.andExpect(status().isOk())
		;
	}
}
