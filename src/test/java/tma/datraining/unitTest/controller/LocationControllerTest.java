package tma.datraining.unitTest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import tma.datraining.controller.LocationController;
import tma.datraining.converter.DateTimeToTimestampConverter;
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Location;
import tma.datraining.model.Sales;
import tma.datraining.model.cassandra.CassLocation;
import tma.datraining.service.LocationService;
import tma.datraining.service.SalesService;
import tma.datraining.service.cassandra.CassLocationService;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@WebMvcTest(value = LocationController.class, secure = false)
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

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	MediaType[] m = new MediaType[] { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML };
	UUID id1 = UUID.fromString("ea0c29eb-3c4e-42ee-8599-91d1305c80aa");
	UUID id2 = UUID.fromString("0f9aec60-3b9f-4dc2-b5af-88c36fe77d65");

	@Ignore
	@Test
	public void giveLocation_whenConvertAllLocationFromCassandra_thenReturn() throws Exception {
		List<CassLocation> list = new ArrayList<>();
		CassLocation location1 = new CassLocation();
		location1.setCity("Ho Chi Minh City");
		location1.setCountry("VIETNAM");
		location1.setLocationId(id1);
		CassLocation location2 = new CassLocation();
		location2.setCity("Tokyo City");
		location2.setCountry("JAPAN");
		location2.setLocationId(id2);
		list.add(location1);
		list.add(location2);
		given(cassSer.list()).willReturn(list);
		List<Location> list2 = new ArrayList<>();
		list.forEach(e -> list2.add(convertCassToJPA(e)));
		list2.forEach(e -> locationService.save(e));
		given(locationService.list()).willReturn(list2);
		this.mockMvc.perform(get("/location/convert").accept(m)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(2)));
	}

	@Ignore
	@Test
	public void giveLocations_whenGetAllLocations_thenReturn() throws Exception {
		List<Location> list = new ArrayList<>();
		Location location1 = new Location();
		location1.setCity("Ho Chi Minh City");
		location1.setCountry("VIETNAM");
		list.add(location1);
		given(this.locationService.list()).willReturn(list);
		this.mockMvc.perform(get("/location/list").accept(m)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(1))).andExpect(jsonPath("$[0].city", is("Ho Chi Minh City")));
	}

	@Ignore
	@Test
	public void giveCity_whenGetLocationsByCity_theReturn() throws Exception {
		List<Location> list = new ArrayList<>();
		Location location1 = new Location();
		location1.setCity("Ho Chi Minh City");
		location1.setCountry("VIETNAM");
		list.add(location1);
		given(this.locationService.findByCity(anyString())).willReturn(list);
		this.mockMvc.perform(get("/location/city/Ho Chi Minh City").accept(m)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(1))).andExpect(jsonPath("$[0].country", is("VIETNAM")));
	}

	@Ignore
	@Test
	public void giveId_whenGetLocationById_thenReturn() throws Exception {
		Location location1 = new Location();
		location1.setLocationId(id1);
		location1.setCity("Tokyo City");
		location1.setCountry("JAPAN");
		given(this.locationService.get(any(UUID.class))).willReturn(location1);
		this.mockMvc.perform(get("/location/" + id1).accept(m)).andExpect(status().isOk())
				.andExpect(jsonPath("$.city", is(location1.getCity())));

	}

	@Ignore
	@Test
	public void giveLocation_whenAddLocation_thenReturn() throws Exception {
		Location location1 = new Location();
		location1.setLocationId(id1);
		location1.setCity("Tokyo City");
		location1.setCountry("JAPAN");
		given(this.locationService.save(any(Location.class))).willReturn(location1);
		this.mockMvc.perform(post("/location/add").contentType(MediaType.APPLICATION_JSON)
				.content(createLocationJsonForAdd(location1))).andExpect(status().isOk()).andDo(print());
	}

	@Ignore
	@Test
	public void giveLocation_whenUpdateLocation_thenReturn() throws Exception {
		Location location1 = new Location();
		location1.setLocationId(id1);
		location1.setCity("Tokyo City");
		location1.setCountry("JAPAN");
		List<Sales> list = new ArrayList<>();
		given(this.locationService.get(any(UUID.class))).willReturn(location1);
		given(this.saleService.findByLocation(any(Location.class))).willReturn(list);
		given(this.locationService.update(eq(UUID.randomUUID()), any(Location.class))).willReturn(location1);
		this.mockMvc.perform(put("/location/update").contentType(MediaType.APPLICATION_JSON)
				.content(createLocationJsonForUpdate(location1))).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void giveId_whenDeleteLocation_thenReturn() throws Exception {
		Location location1 = new Location();
		location1.setLocationId(id1);
		location1.setCity("Tokyo City");
		location1.setCountry("JAPAN");
		List<Sales> list = new ArrayList<>();
		given(locationService.get(any(UUID.class))).willReturn(location1);
		given(saleService.findByLocation(any(Location.class))).willReturn(list);
		given(locationService.delete(any(UUID.class))).willReturn(id1);
		this.mockMvc.perform(delete("/location/delete/{id}", id1).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	private static String createLocationJsonForAdd(Location location) {
		return "{ \"city\": \"" + location.getCity() + "\", " + "\"country\":\"" + location.getCountry() + "\"}";
	}

	private static String createLocationJsonForUpdate(Location location) {
		return "{ \"locationId\": \"" + location.getLocationId() + "\", " + "\"city\": \"" + location.getCity() + "\", "
				+ "\"country\":\"" + location.getCountry() + "\"}";
	}

	private Location convertCassToJPA(CassLocation loca) {
		if (loca == null) {
			throw new NotFoundDataException("");
		}
		Location location = new Location();
		location.setLocationId(loca.getLocationId());
		location.setCity(loca.getCity());
		location.setCountry(loca.getCountry());
		location.setCreateAt(converter.convert(loca.getCreateAt()));
		location.setModifiedAt(converter.convert(loca.getModifiedAt()));
		return location;
	}
}
