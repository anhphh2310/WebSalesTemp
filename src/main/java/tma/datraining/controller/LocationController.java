package tma.datraining.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import tma.datraining.exception.ForbiddentException;
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
	
	private static final Logger LOG = LoggerFactory.getLogger(LocationController.class);
	
	@GetMapping(value = "/convert", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<LocationDTO> getConvertLocations() {
		LogUtil.debug(LOG, "Request convert from Cassandra.");
		cassSer.list().forEach(e -> locaSer.save(convertCassToJPA(e)));
		List<LocationDTO> list = new ArrayList<>();
		locaSer.list().forEach(e -> list.add(convertDTO(e)));
		LogUtil.debug(LOG, "Response convert from Cassandra.");
		return list;
	}

	@GetMapping(value = "/list", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<LocationDTO> getLocations() {
		LogUtil.debug(LOG, "Request list Location.");
		List<LocationDTO> list = new ArrayList<>();
		locaSer.list().forEach(e -> list.add(convertDTO(e)));
		LogUtil.debug(LOG, "Response list Location.");
		return list;
	}

	@GetMapping(value = { "/{locationId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public LocationDTO getLocation(@PathVariable("locationId") String locationId) {
		LogUtil.debug(LOG, "Request get Location by Id.");
		LocationDTO loca = null;
		try {
			loca = convertDTO(locaSer.get(UUID.fromString(locationId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Location Id");
		}
		LogUtil.debug(LOG, "Response get Location by Id.");
		return loca;
	}

	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public LocationDTO saveLocation(@RequestBody LocationDTO location,Principal principal) {
		LogUtil.debug(LOG, "Request add a new Location.");
		if(principal == null) {
			throw new ForbiddentException("Not login, ");
		}
		if (check(location.getCity()) || check(location.getCountry())) {
			throw new BadRequestException("");
		}
		location.setLocationId(UUID.randomUUID());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		location.setCreateAt(time);
		location.setModifiedAt(time);
		Location loca = convertLocation(location);
		locaSer.save(loca);
		LogUtil.debug(LOG, "Response add a new Location.");
		return location;
	}

	@PutMapping(value = { "/update/{locationId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public LocationDTO updateLocation(@RequestBody LocationDTO location,
			@PathVariable("locationId") String locationId,Principal principal) {
		LogUtil.debug(LOG, "Request update Location with id: " + locationId + ".");
		if(principal == null) {
			throw new ForbiddentException("Not login, ");
		}
		UUID id = null;
		try {
			id = UUID.fromString(locationId);
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Location Id");
		}
		if (check(location.getCity()) || check(location.getCountry())) {
			throw new BadRequestException("");
		}

		if (locaSer.get(id) == null) {
			throw new NotFoundDataException("Location Id");
		}
		location.setLocationId(id);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		location.setCreateAt(locaSer.get(id).getCreateAt());
		location.setModifiedAt(time);
		Location loca = convertLocation(location);
		Set<Sales> sales = new HashSet<>();
		sales.addAll(salesSer.findByLocation(loca));
		loca.setSales(sales);
		locaSer.update(loca.getLocationId(), loca);
		LogUtil.debug(LOG, "Response update a new Location with id: " + locationId + ".");
		return location;
	}

	@DeleteMapping(value = { "/delete/{locationId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public String deleteLocation(@PathVariable("locationId") String locationId,Principal principal) {
		LogUtil.debug(LOG, "Request delete a Location with id: " + locationId + ".");
		if(principal == null) {
			throw new ForbiddentException("Not login, ");
		}
		LocationDTO loca = null;
		try {
			loca = convertDTO(locaSer.get(UUID.fromString(locationId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		salesSer.findByLocation(locaSer.get(UUID.fromString(locationId))).forEach(e -> salesSer.delete(e.getSalesId()));
		locaSer.delete(loca.getLocationId());
		LogUtil.debug(LOG, "Response delete a Location with id: " + locationId + ".");
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
