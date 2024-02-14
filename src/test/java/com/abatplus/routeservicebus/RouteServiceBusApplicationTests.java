package com.abatplus.routeservicebus;





import com.abatplus.routeservicebus.model.*;
import com.abatplus.routeservicebus.service.RouteplanerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = RouteServiceBusApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
		locations = "classpath:application.properties")
class RouteServiceBusApplicationTests {

	@Autowired
	private RouteplanerService routeplanerService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGetRoutesByLocations() throws Exception {
		String from = "Mainz";
		String to = "Hamborg";

		mockMvc.perform(MockMvcRequestBuilders.get("/locations/{from}/{to}", from, to)
				.contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	public void testGetRouteById() throws Exception {


		TaxiRoute taxiRoute = new TaxiRoute("12", "hamburg", "Mainz", 60, 100, "Achim");
		mockMvc.perform(MockMvcRequestBuilders.get("/route/{id}", taxiRoute.getId())
				.contentType(MediaType.APPLICATION_JSON));

	}

	@Test
	public void testSelectRoute() throws Exception {
		TaxiRoute taxiRoute = new TaxiRoute("12", "hamburg", "Mainz", 60, 100, "Adalfuns");
		taxiRoute.setVerkehrsmittel("Taxi");
		mockMvc.perform(MockMvcRequestBuilders.get("/route/select/{from}/{to}/{type}", taxiRoute.getSource(), taxiRoute.getDestination(), taxiRoute.getVerkehrsmittel())
				.contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	public void testGetFastestRoutes() throws Exception {
		TaxiRoute taxiRoute = new TaxiRoute("12", "hamburg", "Mainz", 60, 100, "Abeline ");

		mockMvc.perform(MockMvcRequestBuilders.get("/route/fastest/{from}/{to}", taxiRoute.getDistance() , taxiRoute.getAvg_vel() , taxiRoute.getSource(), taxiRoute.getDestination())
				.contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	public void testGetCheapestRoutes() throws Exception {
		TaxiRoute taxiRoute = new TaxiRoute("12", "hamburg", "Mainz", 60, 100, "Adalberga");

		mockMvc.perform(MockMvcRequestBuilders.get("/route/cheapest/{from}/{to}",taxiRoute.getPrice() , taxiRoute.getSource(), taxiRoute.getDestination())
				.contentType(MediaType.APPLICATION_JSON));
	}




	private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
