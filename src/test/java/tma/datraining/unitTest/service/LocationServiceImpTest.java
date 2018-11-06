package tma.datraining.unitTest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tma.datraining.model.Location;
import tma.datraining.repository.LocationRepository;
import tma.datraining.service.LocationServiceImp;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationServiceImpTest {

	@InjectMocks
	private LocationServiceImp locationService;

	@Mock
	private LocationRepository locationRepository;

	UUID id1 = UUID.fromString("ea0c29eb-3c4e-42ee-8599-91d1305c80aa");// VietNam
	UUID id2 = UUID.fromString("eba9a9ff-1b24-4b24-96e2-c2e09ff60f1b");// England
	UUID id3 = UUID.fromString("0f9aec60-3b9f-4dc2-b5af-88c36fe77d65");
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	

	@Test
	public void testGetAllLocations() throws Exception {
		List<Location> list = new ArrayList<>();
		Location location1 = new Location();
		location1.setCity("Ho Chi Minh City");
		location1.setCountry("VIETNAM");
		Location location2 = new Location();
		location2.setCity("London City");
		location2.setCountry("ENGLAND");
		list.add(location1);
		list.add(location2);
		when(locationRepository.findAll()).thenReturn(list);
		List<Location> list2 = locationService.list();
		assertNotNull(list2);
		assertEquals(2, list2.size());
	}


	@Test
	public void testGetLocationById() throws Exception {
		Location location1 = new Location();
		location1.setLocationId(id1);
		location1.setCity("Ho Chi Minh City");
		location1.setCountry("VIETNAM");
		when(locationRepository.findByLocationId(any(UUID.class))).thenReturn(location1);
		Location location2 = locationService.get(id1);
		assertNotNull(location2);
		assertEquals("Ho Chi Minh City", location2.getCity());
	}
	

	@Test
	public void testGetLocationsByCity() throws Exception{
		List<Location> list = new ArrayList<>();
		Location location1 = new Location();
		location1.setLocationId(id1);
		location1.setCity("Ho Chi Minh City");
		location1.setCountry("VIETNAM");
		list.add(location1);
		when(locationRepository.findByCity(anyString())).thenReturn(list);
		List<Location> list2 = locationService.findByCity("VIETNAM");
		assertNotNull(list2);
		assertEquals("VIETNAM", list2.get(0).getCountry());
		
	}
	

	@Test
	public void testGetLocationsByCountry() throws Exception{
		List<Location> list = new ArrayList<>();
		Location location1 = new Location();
		location1.setLocationId(id1);
		location1.setCity("Ho Chi Minh City");
		location1.setCountry("VIETNAM");
		list.add(location1);
		when(locationRepository.findByCountry(anyString())).thenReturn(list);
		List<Location> list2 = locationService.findByCountry("VIETNAM");
		assertNotNull(list2);
		assertEquals("Ho Chi Minh City", list.get(0).getCity());
	}
	

	@Test
	public void testAddLocation() throws Exception{
		Location location1 = new Location();
		location1.setLocationId(id3);
		location1.setCity("Tokyo City");
		location1.setCountry("JAPAN");
		when(locationRepository.save(any(Location.class))).thenReturn(location1);
		Location result = locationService.save(location1);
		assertNotNull(result);
		assertEquals(result.getLocationId(), id3);
	}
	

	@Test
	public void testUpdateLocation() throws Exception{
		Location location1 = new Location();
		location1.setLocationId(id3);
		location1.setCity("OSAKA City");
		location1.setCountry("JAPAN");
		when(locationRepository.save(any(Location.class))).thenReturn(location1);
		Location result = locationService.update(id3, location1);
		assertNotNull(result);
		assertEquals(result.getCity(), location1.getCity());
	}


	@Test
	public void testDeleteLocation() throws Exception{
		Location location1 = new Location();
		location1.setLocationId(id3);
		location1.setCity("OSAKA City");
		location1.setCountry("JAPAN");
		when(locationRepository.findByLocationId(any(UUID.class))).thenReturn(location1);
		doNothing().when(locationRepository).delete(any(Location.class));
		UUID id = locationService.delete(id3);
		assertNotNull(id);
		assertEquals(id, location1.getLocationId());
	}
}
