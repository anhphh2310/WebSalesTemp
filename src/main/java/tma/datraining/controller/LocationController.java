package tma.datraining.controller;

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
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Location;
import tma.datraining.model.Sales;
import tma.datraining.model.cassandra.CassLocation;
import tma.datraining.service.LocationService;
import tma.datraining.service.SalesService;
import tma.datraining.service.cassandra.CassLocationService;

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
		List<LocationDTO> list = new ArrayList<>();
		cassSer.list().forEach(e -> list.add(convertCassToDTO(e)));
		for (LocationDTO location : list) {
			locaSer.save(convertLocation(location));
		}
		return list;
	}

	@GetMapping(value = "/list", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<LocationDTO> getLocations() {
		List<LocationDTO> list = new ArrayList<>();
		locaSer.list().forEach(e -> list.add(convertDTO(e)));
		return list;
	}

	@GetMapping(value = { "/{locationId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public LocationDTO getLocation(@PathVariable("locationId") String locationId) {
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
	public LocationDTO saveLocation(@RequestBody LocationDTO location) {
		if (location.getCity().isEmpty() || location.getCountry().isEmpty()) {
			throw new BadRequestException("");
		}
		Timestamp time = new Timestamp(System.currentTimeMillis());
		location.setCreateAt(time);
		location.setModifiedAt(time);
		Location loca = convertLocation(location);
		locaSer.save(loca);
		return location;
	}

	@PutMapping(value = { "/update/{locationId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public LocationDTO updateLocation(@RequestBody LocationDTO location,
			@PathVariable("locationId") String locationId) {
		UUID id = null;
		try {
			id = UUID.fromString(locationId);
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Location Id");
		}
		if (location.getCity().isEmpty() || location.getCountry().isEmpty()) {
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
		return location;
	}

	@DeleteMapping(value = { "/delete/{locationId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public void deleteLocation(@PathVariable("locationId") String locationId) {
		LocationDTO loca = null;
		try {
			loca = convertDTO(locaSer.get(UUID.fromString(locationId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		locaSer.delete(loca.getLocationId());
		System.out.println("Delete location : " + locationId);
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

	public LocationDTO convertCassToDTO(CassLocation loca) {
		if (loca == null) {
			throw new NotFoundDataException("");
		}
		LocationDTO location = new LocationDTO();
		location.setLocationId(loca.getLocationId());
		location.setCity(loca.getCity());
		location.setCountry(loca.getCountry());
		location.setCreateAt(converter.convert(loca.getCreateAt()));
		location.setModifiedAt(converter.convert(loca.getModifiedAt()));
		return location;
	}

}
