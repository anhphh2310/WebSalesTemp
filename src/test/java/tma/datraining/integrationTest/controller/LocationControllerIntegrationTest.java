package tma.datraining.integrationTest.controller;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import tma.datraining.controller.LocationController;
import tma.datraining.model.Location;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LocationControllerIntegrationTest {

	@Autowired
	private MockMvc mock;

	@Autowired
	protected WebApplicationContext weapp;
	
	@Autowired
	private LocationController locationControlller;

	UUID id1; /*= UUID.fromString("d3103a21-f550-4249-ba8b-04c0357edcd7");*/
	
	@Before
	public void init() throws Exception {
		//this.mock = standaloneSetup(this.locationControlller).build();
		this.mock = MockMvcBuilders.webAppContextSetup(weapp).build();
		Location location1 = new Location();
		location1.setCity("Venice city temp");
		location1.setCountry("ITALY");
		mock.perform(post("/location/add").contentType(MediaType.APPLICATION_JSON).content(createLocationJsonForAdd(location1)));
		locationControlller.getAllLocations().forEach(e -> id1 = e.getLocationId());
	}
	@Ignore
	@Test
	public void testGetAllLocations() throws Exception {
		mock.perform(get("/location/list").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.*.country", hasItem(is("ITALY"))));
	}
	@Ignore
	@Test
	public void testGetLocationById() throws Exception{
		mock.perform(get("/location/" + id1).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.country", is("ITALY")));
	}
	@Ignore
	@Test
	public void testGetLocationsByCountry() throws Exception{
		mock.perform(get("/location/country/ITALY").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()",is(1)));
	}
	
	@Ignore
	@Test
	public void testGetLocationsByCity()  throws Exception{
		String city = "Venice city temp";
		mock.perform(get("/location/city/" + city ).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)))
		.andExpect(jsonPath("$[0].country",is("ITALY")));
	}
	
	@Ignore
	@Test
	public void testAddLocation() throws Exception{
		Location location1 = new Location();
		location1.setCity("Tokyo City Temp");
		location1.setCountry("JAPAN");
		mock.perform(post("/location/add").contentType(MediaType.APPLICATION_JSON).content(createLocationJsonForAdd(location1)))
		.andExpect(status().isOk()).andDo(print());
	}
	
	@Ignore
	@Test
	public void testUpdateLocation() throws Exception{
		Location location1 = new Location();
		location1.setLocationId(id1);
		location1.setCity("Tokyo City");
		location1.setCountry("JAPAN");
		this.mock.perform(put("/location/update").contentType(MediaType.APPLICATION_JSON).content(createLocationJsonForUpdate(location1)))
		.andExpect(status().isOk()).andDo(print());
	}
	
//	@Ignore
	@Test
	public void testDelteLocation() throws Exception{
		this.mock.perform(delete("/location/delete/{id}",id1).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}
	
	private static String createLocationJsonForAdd(Location location) {
		return "{ \"city\": \"" + location.getCity() + "\", " + "\"country\":\"" + location.getCountry() + "\"}";
	}
	
	private static String createLocationJsonForUpdate(Location location) {
		return "{ \"locationId\": \"" + location.getLocationId() + "\", " + "\"city\": \"" + location.getCity() + "\", " + "\"country\":\"" + location.getCountry() + "\"}";
	}

}