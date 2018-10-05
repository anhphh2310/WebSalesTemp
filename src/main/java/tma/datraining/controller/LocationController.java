package tma.datraining.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tma.datraining.dto.LocationDTO;
import tma.datraining.exception.BadRequestException;
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Location;
import tma.datraining.model.Sales;
import tma.datraining.service.LocationService;
import tma.datraining.service.SalesService;

@RestController
public class LocationController {

	@Autowired
	private LocationService locaSer;

	@Autowired
	private SalesService salesSer;

	@RequestMapping(value = { "/locations" }, method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<LocationDTO> getLocations() {
		List<LocationDTO> list = convertDTOList(locaSer.list());
		return list;
	}

	@RequestMapping(value = { "/location/{locationId}" }, method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
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

	@RequestMapping(value = { "/location" }, method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
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

	@RequestMapping(value = { "/location/{locationId}" }, method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
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
		location.setCreateAt(time);
		location.setModifiedAt(time);
		Location loca = convertLocation(location);
		Set<Sales> sales = new HashSet<>();
		sales.addAll(salesSer.findByLocation(loca));
		loca.setSales(sales);
		locaSer.update(loca.getLocationId(), loca);
		return location;
	}

	@RequestMapping(value = { "/location/{locationId}" }, method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
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

	public LocationDTO convertDTO(Location location) {
		LocationDTO temp = null;
		if (location == null) {
			throw new NotFoundDataException("Location Id");
		}
		temp = new LocationDTO(location.getLocationId(), location.getCountry(), location.getCity(),
				location.getCreateAt(), location.getModifiedAt());
		return temp;
	}

	public List<LocationDTO> convertDTOList(List<Location> list) {
		List<LocationDTO> list2 = new ArrayList<>();
		list.forEach(e -> list2.add(convertDTO(e)));
		return list2;
	}

	public Location convertLocation(LocationDTO location) {
		Location loca = null;
		loca = new Location(location.getCountry(), location.getCity(), location.getCreateAt(),
				location.getModifiedAt());
		loca.setLocationId(location.getLocationId());
		return loca;
	}
}
