package tma.datraining.unitTest.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tma.datraining.model.Location;
import tma.datraining.repository.LocationRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class LocationRepositoryTest {

	@Autowired
	private LocationRepository locationRepository;
	
	UUID id1 = UUID.fromString("ea0c29eb-3c4e-42ee-8599-91d1305c80aa");
	UUID id2 = UUID.fromString("775cb14d-6de4-4ce2-b5d1-8c56f54ef792");
	UUID wrongId = UUID.fromString("29f43143-14e4-4817-9a22-58ef75f58ddc");
	
	@Before
	public void init() {
		Location location1 = new Location("VIETNAM","Ho Chi Minh City",null,null);
		location1.setLocationId(id1);
		Location location2 = new Location("VIETNAM","Ha Noi City",null,null);
		location2.setLocationId(id2);
		locationRepository.save(location1);
		locationRepository.save(location2);
	}
	
	@Test
	public void testGetAllLocations() throws Exception{
		Iterable<Location> results = locationRepository.findAll();
		assertNotNull(results);
		assertThat(((Collection<Location>)results).size()).isEqualTo(2);
	}

	@Test
	public void testGetLocationById() throws Exception {
		Location result = locationRepository.findByLocationId(id1);
		assertNotNull(result);
		assertThat(result.getCountry()).isEqualTo("VIETNAM");
	}
	
	@Test
	public void testGetLocationByWrongId() throws Exception{
		Location result = locationRepository.findByLocationId(wrongId);
		assertNull(result);
	}
	
	@Test
	public void testGetLocationByCity() throws Exception{
		List<Location> results = locationRepository.findByCity("Ho Chi Minh City");
		assertThat(results.size()).isEqualTo(1);
	}
	
	@Test
	public void testGetLocationByCountry() throws Exception{ 
		List<Location> results = locationRepository.findByCountry("VIETNAM");
		assertThat(results.size()).isEqualTo(2);
 	}
	
	@Ignore
	@Test
	public void testAddLocation() throws Exception{
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Location location = new Location("JAPAN","Tokyo",time,time);
		location.setLocationId(UUID.randomUUID());
		Location result = locationRepository.save(location);
		assertNotNull(result);
		assertThat(result.getCity()).isEqualTo("Tokyo");
	}
	
	@Test
	public void testUpdateLocation() throws Exception{
		Location location = new Location("VIETNAM", "HCM City", null, null);
		location.setLocationId(id1);
		Location result = locationRepository.save(location);
		assertNotNull(result);
		assertThat(result.getCity()).isEqualTo("HCM City");
		Iterable<Location> list = locationRepository.findAll();
		assertEquals(((Collection<Location>)list).size(), 2);
	}
	
	
	@Test
	public void testDeleteLocationById() throws Exception{
		locationRepository.deleteById(id1);
		Iterable<Location> list = locationRepository.findAll();
		assertEquals(((Collection<Location>)list).size(), 1);
	}
}
