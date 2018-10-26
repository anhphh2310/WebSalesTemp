package tma.datraining.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tma.datraining.converter.DateTimeToTimestampConverter;
import tma.datraining.dto.LocationDTO;
import tma.datraining.exception.BadRequestException;
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Location;
import tma.datraining.model.Sales;
import tma.datraining.model.cassandra.CassLocation;
import tma.datraining.service.LocationService;
import tma.datraining.service.SalesService;
import tma.datraining.service.cassandra.CassLocationService;
import tma.datraining.util.LogUtil;

@RestController
@RequestMapping("/location")
public class LocationController {

	@Autowired
	private LocationService locaSer;

	@Autowired
	private SalesService salesSer;

	@Autowired
	private CassLocationService cassSer;

	@Autowired
	private DateTimeToTimestampConverter converter;
	
	private static final Logger LOG = LogManager.getLogger(LocationController.class);
	
	@GetMapping(value = "/convert", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<LocationDTO> getConvertLocations() {
		LogUtil.info(LOG, "Request convert from Cassandra.");
		cassSer.list().forEach(e -> locaSer.save(convertCassToJPA(e)));
		List<LocationDTO> list = new ArrayList<>();
		locaSer.list().forEach(e -> list.add(convertDTO(e)));
		return list;
	}

	@GetMapping(value = "/list", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<LocationDTO> getAllLocations() {
		LogUtil.info(LOG, "Request list Location.");
		List<LocationDTO> list = new ArrayList<>();
		locaSer.list().forEach(e -> list.add(convertDTO(e)));
		LogUtil.info(LOG, "Response list Location.");
		return list;
	}

	@GetMapping(value = { "/city/{city}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<LocationDTO> getLocationsByCity(@PathVariable("city") String city) {
		LogUtil.info(LOG, "Request get locations by city: " + city + ".");
		List<Location> list = locaSer.findByCity(city);
		List<LocationDTO> listResult = new ArrayList<>();
		list.forEach(e -> listResult.add(convertDTO(e)));
		return listResult;
	}
	
	@GetMapping(value = { "/country/{country}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<LocationDTO> getLocationsByCountry(@PathVariable("country") String country) {
		LogUtil.info(LOG, "Request get locations by country: " + country + ".");
		List<Location> list = locaSer.findByCountry(country);
		List<LocationDTO> listResult = new ArrayList<>();
		list.forEach(e -> listResult.add(convertDTO(e)));
		return listResult;
	}
	
	@GetMapping(value = { "/{locationId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public LocationDTO getLocation(@PathVariable("locationId") String locationId) {
		LogUtil.info(LOG, "Request get location with id: " + locationId + ".");
		LocationDTO loca = null;
		try {
			loca = convertDTO(locaSer.get(UUID.fromString(locationId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Location Id");
		}
		return loca;
	}

	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public LocationDTO saveLocation(@RequestBody LocationDTO location,Principal principal) {
		LogUtil.info(LOG, "Request add a new location.");
		
		if (check(location.getCity()) || check(location.getCountry())) {
			throw new BadRequestException("");
		}
		location.setLocationId(UUID.randomUUID());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		location.setCreateAt(time);
		location.setModifiedAt(time);
		Location loca = convertLocation(location);
		locaSer.save(loca);
		return location;
	}

	@PutMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public LocationDTO updateLocation(@RequestBody LocationDTO location
			) {
		LogUtil.info(LOG, "Request update Location with id: " + location.getLocationId() + ".");
		
		if (check(location.getCity()) || check(location.getCountry())) {
			throw new BadRequestException("");
		}

		if (locaSer.get(location.getLocationId()) == null) {
			throw new NotFoundDataException("Location Id");
		}
		location.setLocationId(location.getLocationId());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		location.setCreateAt(locaSer.get(location.getLocationId()).getCreateAt());
		location.setModifiedAt(time);
		Location loca = convertLocation(location);
		Set<Sales> sales = new HashSet<>();
		sales.addAll(salesSer.findByLocation(loca));
		loca.setSales(sales);
		locaSer.update(loca.getLocationId(), loca);
		return location;
	}

	@DeleteMapping(value = { "/delete/{locationId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public String deleteLocation(@PathVariable("locationId") String locationId,Principal principal) {
		LogUtil.info(LOG, "Request delete a location with id: " + locationId + ".");
		
		LocationDTO loca = null;
		try {
			loca = convertDTO(locaSer.get(UUID.fromString(locationId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		salesSer.findByLocation(locaSer.get(UUID.fromString(locationId))).forEach(e -> salesSer.delete(e.getSalesId()));
		locaSer.delete(loca.getLocationId());
		return "Delete successfull LOCATION{ " + locationId + "}";
	}

	//
	// Convert
	// Location to DTO
	public LocationDTO convertDTO(Location location) {
		LocationDTO temp = new LocationDTO();
		if (location == null) {
			throw new NotFoundDataException("Location Id");
		}
		temp.setLocationId(location.getLocationId());
		temp.setCity(location.getCity());
		temp.setCountry(location.getCountry());
		temp.setCreateAt(location.getCreateAt());
		temp.setModifiedAt(location.getModifiedAt());
		return temp;
	}

	// DTO to Location
	public Location convertLocation(LocationDTO location) {
		Location loca = null;
		loca = new Location(location.getCountry(), location.getCity(), location.getCreateAt(),
				location.getModifiedAt());
		loca.setLocationId(location.getLocationId());
		return loca;
	}

	public Location convertCassToJPA(CassLocation loca) {
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
	
	//Check
	public boolean check(String s) {
		if(s !=null && !s.isEmpty() && !s.trim().isEmpty())
			return false;
		return true;
	}

}
