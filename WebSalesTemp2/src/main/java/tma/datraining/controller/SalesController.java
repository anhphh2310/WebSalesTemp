
package tma.datraining.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tma.datraining.dto.SalesDTO;
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Location;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.model.Time;
import tma.datraining.service.LocationService;
import tma.datraining.service.ProductService;
import tma.datraining.service.SalesService;
import tma.datraining.service.TimeService;

@RestController
public class SalesController {

	@Autowired
	private SalesService salesSer;

	@Autowired
	private ProductService proSer;

	@Autowired 
	private LocationService locaSer;
	
	@Autowired
	private TimeService timeSer;
	
	@RequestMapping(value = { "/sales" }, method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getSales() {
		List<SalesDTO> list = convertDTOList(salesSer.list());
		return list;
	}

	@RequestMapping(value = { "/sale/{salesId}" }, method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public SalesDTO getSale(@PathVariable("salesId") String salesId) {
		SalesDTO sales = null;
		try{sales = convertDTO(salesSer.get(UUID.fromString(salesId)));}catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		return sales;
	}
	
	@RequestMapping(value = { "/sale/product/{productId}" }, method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getSaleByProduct(@PathVariable("productId") String productId) {
		Product pro = null;
		try{pro = proSer.get(UUID.fromString(productId));}catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		if(pro == null) {
			throw new NotFoundDataException("");
		}
		List<SalesDTO> sales = convertDTOList(salesSer.findByProduct(pro));
		return sales;
	}
	
	@RequestMapping(value = { "/sale/location/{locationId}" }, method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getSaleByLocation(@PathVariable("locationId") String locationId) {
		Location location = null;
		try{location = locaSer.get(UUID.fromString(locationId));}catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		if(location == null) {
			throw new NotFoundDataException("");
		}
		List<SalesDTO> sales = convertDTOList(salesSer.findByLocation(location));
		return sales;
	}
	
	@RequestMapping(value = { "/sale/time/{timeId}" }, method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getSaleByTime(@PathVariable("timeId") String timeId) {
		Time time = null;
		try{time = timeSer.get(UUID.fromString(timeId));}catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		if(time == null) {
			throw new NotFoundDataException("");
		}
		List<SalesDTO> sales = convertDTOList(salesSer.findByTime(time));
		return sales;
	}

	@RequestMapping(value = { "/sale" }, method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public SalesDTO saveSales(@RequestBody SalesDTO saleDTO) {
		Sales sale = convertSales(saleDTO);
		salesSer.save(sale);
		return saleDTO;
	}

	@RequestMapping(value = { "/sale/{salesId}" }, method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public void deleteSales(@PathVariable("salesId") String salesId) {
		SalesDTO sales = null;
		try{sales = convertDTO(salesSer.get(UUID.fromString(salesId)));}catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		salesSer.delete(sales.getSalesId());
		System.out.println("Delete sale : " + salesId);
	}

	public SalesDTO convertDTO(Sales sales) {
		SalesDTO temp = null;
		temp = new SalesDTO(sales.getSalesId(), sales.getProduct().getProductId(), sales.getTime().getTimeId(),
				sales.getLocation().getLocationId(), sales.getDollars(), sales.getCreateAt(), sales.getModifiedAt());
		return temp;
	}

	public List<SalesDTO> convertDTOList(List<Sales> list) {
		List<SalesDTO> list2 = new ArrayList<>();
		list.forEach(e -> list2.add(convertDTO(e)));
		return list2;
	}

	public Sales convertSales(SalesDTO salesDTO) {
		Sales sales = null;
		sales = new Sales(proSer.get(salesDTO.getProduct()), timeSer.get(salesDTO.getTime()), locaSer.get(salesDTO.getLocation()), salesDTO.getDollars(), salesDTO.getCreateAt(), salesDTO.getModifiedAt());
		sales.setSalesId(salesDTO.getSalesId());
		return sales;
	}
}
