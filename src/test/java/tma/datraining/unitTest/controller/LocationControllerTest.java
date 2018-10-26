package tma.datraining.unitTest.controller;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import tma.datraining.controller.LocationController;
import tma.datraining.converter.DateTimeToTimestampConverter;
import tma.datraining.model.Location;
import tma.datraining.service.LocationService;
import tma.datraining.service.SalesService;
import tma.datraining.service.cassandra.CassLocationService;

@RunWith(SpringRunner.class)
@WebMvcTest(LocationController.class)
public class LocationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LocationService locationService;

	@MockBean
	private SalesService saleService;

	@MockBean
	private CassLocationService cassSer;

	@MockBean
	private DateTimeToTimestampConverter converter;

	
	@Test
	public void giveLocations_whenGetAllLocations_thenReturnJsonArray() throws Exception {
		Location location = new Location("VIETNAM", "HCM City", null, null);
		List<Location> locations = new ArrayList<>();
		locations.add(location);
		when(locationService.list()).thenReturn(locations);

		mockMvc.perform(MockMvcRequestBuilders.get("/location/list").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].city", is(location.getCity())));

	}
}
