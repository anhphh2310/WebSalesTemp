package tma.datraining.integrationTest.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;

import tma.datraining.model.Location;
import tma.datraining.service.LocationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationServiceIntegrationTest {

	@Autowired
	private LocationService locationService;
	
	UUID id1 = UUID.fromString("a3aa7f6d-7558-4ed1-bc64-72b0db8988fe");
	
	@Test
	public void testGetAllLocations() throws Exception{
		List<Location> results = locationService.list();
		assertThat(results).isNotNull().isNotEmpty();
		
	}
	
	@Test
	public void testGetLocationById() throws Exception{
		Location result = locationService.get(id1);
		assertNotNull(result);
		assertThat(result.getCountry()).isEqualTo("AUSTRALIA");
	}

	@Test
	public void testGetLocationByCountry() throws Exception{
		List<Location> results = locationService.findByCountry("VIETNAM");
		assertNotNull(results);
		assertThat(results.size()).isEqualTo(2);
	}
	
	@Test
	public void testGetLocationByCity() throws Exception{
		List<Location> results = locationService.findByCity("Ho Chi Minh City");
		assertNotNull(results);
		assertThat(results.size()).isEqualTo(1);
	}
	
}
