
package tma.datraining.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tma.datraining.converter.DateTimeToTimestampConverter;
import tma.datraining.dto.SalesDTO;
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Location;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.model.Time;
import tma.datraining.model.cassandra.CassSales;
import tma.datraining.service.LocationService;
import tma.datraining.service.ProductService;
import tma.datraining.service.SalesService;
import tma.datraining.service.TimeService;
import tma.datraining.service.cassandra.CassSalesService;

@RestController
@RequestMapping("/sales")
public class SalesController {

	@Autowired
	private SalesService salesSer;

	@Autowired
	private ProductService proSer;

	@Autowired
	private LocationService locaSer;

	@Autowired
	private TimeService timeSer;

	@Autowired
	private CassSalesService cassSer;

	@Autowired
	private DateTimeToTimestampConverter converter;

	@GetMapping(value = "/convert", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getConvert() {
		cassSer.list().forEach(e -> salesSer.save(convertCassToJPA(e)));
		List<SalesDTO> list = new ArrayList<>();
		salesSer.list().forEach(e -> list.add(convertDTO(e)));
		return list;
	}

	@GetMapping(value = { "/list" }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getSales() {
		List<SalesDTO> list = new ArrayList<>();
		salesSer.list().forEach(e -> list.add(convertDTO(e)));
		return list;
	}

	@GetMapping(value = { "/cass" }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<CassSales> getSalesCass() {
		List<CassSales> list = cassSer.list();
		return list;
	}

	@GetMapping(value = { "/{saleId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public SalesDTO getSale(@PathVariable("saleId") String saleId) {
		if(checkNullEmpty(saleId))
			throw new NotFoundDataException("");
		SalesDTO sales = null;
		try {
			sales = convertDTO(salesSer.get(UUID.fromString(saleId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		return sales;
	}

	@GetMapping(value = { "/product/{productId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getSaleByProduct(@PathVariable("productId") String productId) {
		if(checkNullEmpty(productId))
			throw new NotFoundDataException("");
		Product pro = null;
		try {
			pro = proSer.get(UUID.fromString(productId));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		if (pro == null) {
			throw new NotFoundDataException("");
		}
		List<SalesDTO> sales = new ArrayList<>();
		salesSer.findByProduct(pro).forEach(e -> sales.add(convertDTO(e)));
		return sales;
	}

	@GetMapping(value = { "/location/{locationId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getSaleByLocation(@PathVariable("locationId") String locationId) {
		if(checkNullEmpty(locationId))
			throw new NotFoundDataException("");
		Location location = null;
		try {
			location = locaSer.get(UUID.fromString(locationId));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		if (location == null) {
			throw new NotFoundDataException("");
		}
		List<SalesDTO> sales = new ArrayList<>();
		salesSer.findByLocation(location).forEach(e -> sales.add(convertDTO(e)));
		return sales;
	}

	@RequestMapping(value = { "/time/{timeId}" }, method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getSaleByTime(@PathVariable("timeId") String timeId) {
		if(checkNullEmpty(timeId))
			throw new NotFoundDataException("");
		Time time = null;
		try {
			time = timeSer.get(UUID.fromString(timeId));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		if (time == null) {
			throw new NotFoundDataException("");
		}
		List<SalesDTO> sales = new ArrayList<>();
		salesSer.findByTime(time).forEach(e -> sales.add(convertDTO(e)));
		return sales;
	}

	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public SalesDTO saveSales(@RequestBody SalesDTO saleDTO) {
		saleDTO.setSalesId(UUID.randomUUID());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		saleDTO.setCreateAt(time);
		saleDTO.setModifiedAt(time);
		Sales sale = convertSales(saleDTO);
		salesSer.save(sale);
		return saleDTO;
	}

	@PutMapping(value = { "/update/{saleId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public SalesDTO saveSales(@RequestBody SalesDTO saleDTO, @PathVariable("saleId") String saleId) {
		if(checkNullEmpty(saleId))
			throw new NotFoundDataException("");
		UUID id = null;
		try {
			id = UUID.fromString(saleId);
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Sales Id");
		}
		if (salesSer.get(id) == null) {
			throw new NotFoundDataException("Sales Id ");
		}
		saleDTO.setSalesId(id);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		saleDTO.setCreateAt(salesSer.get(id).getCreateAt());
		saleDTO.setModifiedAt(time);
		Sales sale = convertSales(saleDTO);
		salesSer.save(sale);
		return saleDTO;
	}

	@DeleteMapping(value = { "/delete/{saleId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public String deleteSales(@PathVariable("saleId") String saleId) {
		if(checkNullEmpty(saleId))
			throw new NotFoundDataException("");
		SalesDTO sales = null;
		try {
			sales = convertDTO(salesSer.get(UUID.fromString(saleId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		salesSer.delete(sales.getSalesId());
		return "Delete sale : " + saleId;
	}
	
	//Check null empty
	public boolean checkNullEmpty(String s) {
		if(s != null && !s.isEmpty() && !s.trim().isEmpty())
			return false;
		return true;
	}

	// Convert
	// Sales to DTO
	public SalesDTO convertDTO(Sales e) {
		if (e == null) {
			throw new NotFoundDataException("Sales Id");
		}
		SalesDTO sales = new SalesDTO();
		sales.setSalesId(e.getSalesId());
		sales.setProduct(e.getProduct().getProductId());
		sales.setLocation(e.getLocation().getLocationId());
		sales.setTime(e.getTime().getTimeId());
		sales.setDollars(e.getDollars());
		sales.setCreateAt(e.getCreateAt());
		sales.setModifiedAt((e.getModifiedAt()));
		return sales;
	}

	// DTO to Sales
	public Sales convertSales(SalesDTO salesDTO) {
		Sales sales = null;
		sales = new Sales(proSer.get(salesDTO.getProduct()), timeSer.get(salesDTO.getTime()),
				locaSer.get(salesDTO.getLocation()), salesDTO.getDollars(), salesDTO.getCreateAt(),
				salesDTO.getModifiedAt());
		sales.setSalesId(salesDTO.getSalesId());
		return sales;
	}

	// Cass to DTO
	public Sales convertCassToJPA(CassSales e) {
		if (e == null) {
			throw new NotFoundDataException("");
		}
		Sales sales = new Sales();
		sales.setSalesId(UUID.randomUUID());
		sales.setProduct(proSer.get(e.getProductId()));
		sales.setLocation(locaSer.get(e.getLocationId()));
		sales.setTime(timeSer.get(e.getTimeId()));
		sales.setDollars(e.getDollars());
		sales.setCreateAt(converter.convert(e.getCreateAt()));
		sales.setModifiedAt(converter.convert(e.getModifiedAt()));
		return sales;
	}
}
