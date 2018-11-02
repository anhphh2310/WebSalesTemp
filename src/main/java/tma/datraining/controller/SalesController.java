
package tma.datraining.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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

import com.querydsl.core.types.Predicate;

import tma.datraining.converter.DateTimeToTimestampConverter;
import tma.datraining.dto.SalesDTO;
import tma.datraining.exception.ForbiddentException;
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Location;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.model.QSales;
import tma.datraining.model.Time;
import tma.datraining.model.cassandra.CassSales;
import tma.datraining.service.LocationService;
import tma.datraining.service.ProductService;
import tma.datraining.service.SalesService;
import tma.datraining.service.TimeService;
import tma.datraining.service.cassandra.CassSalesService;
import tma.datraining.util.LogUtil;

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

	private Logger LOG = LogManager.getLogger(SalesController.class);
//	private static final Logger LOG = LoggerFactory.getLogger(SalesController.class);

	@GetMapping(value = "/convert", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getConvert() {
		LogUtil.debug(LOG, "Request convert data from Cassandra.");
		cassSer.list().forEach(e -> salesSer.save(convertCassToJPA(e)));
		List<SalesDTO> list = new ArrayList<>();
		salesSer.list().forEach(e -> list.add(convertDTO(e)));
		LogUtil.debug(LOG, "Response convert data form Cassandra.");
		return list;
	}

	@GetMapping(value = { "/list" }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getSales() {
		LogUtil.debug(LOG, "Request list data from Postgresql.");
		List<SalesDTO> list = new ArrayList<>();
		salesSer.list().forEach(e -> list.add(convertDTO(e)));
		LogUtil.debug(LOG, "Response list data from Postgresql.");
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
		LogUtil.debug(LOG, "Request get sale with id: " + saleId + ".");
		if (checkNullEmpty(saleId))
			throw new NotFoundDataException("");
		SalesDTO sales = null;
		try {
			sales = convertDTO(salesSer.get(UUID.fromString(saleId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		LogUtil.debug(LOG, "Response get sale with id: " + saleId + ".");
		return sales;
	}

	@GetMapping(value = { "/product/{productId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getSaleByProduct(@PathVariable("productId") String productId) {
		LogUtil.debug(LOG, "Request get sales by product with id: " + productId + ".");
		if (checkNullEmpty(productId))
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
		LogUtil.debug(LOG, "Response get sales by product with id: " + productId + ".");
		return sales;
	}

	@GetMapping(value = { "/location/{locationId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getSaleByLocation(@PathVariable("locationId") String locationId) {
		LogUtil.debug(LOG, "Request convert data from Cassandra.");
		if (checkNullEmpty(locationId))
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
		LogUtil.debug(LOG, "Response convert data form Cassandra.");
		return sales;
	}

	@RequestMapping(value = { "/time/{timeId}" }, method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getSaleByTime(@PathVariable("timeId") String timeId) {
		if (checkNullEmpty(timeId))
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
	public SalesDTO saveSales(@RequestBody SalesDTO saleDTO, Principal principal) {
		if (principal == null) {
			throw new ForbiddentException("Not login, ");
		}
		saleDTO.setSalesId(UUID.randomUUID());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		saleDTO.setCreateAt(time);
		saleDTO.setModifiedAt(time);
		Sales sale = convertSales(saleDTO);
		salesSer.save(sale);
		return saleDTO;
	}

	@PutMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public SalesDTO updateSales(@RequestBody SalesDTO saleDTO, Principal principal) {
		if (salesSer.get(saleDTO.getSalesId()) == null) {
			throw new NotFoundDataException("Sales Id ");
		}
		Timestamp time = new Timestamp(System.currentTimeMillis());
		saleDTO.setCreateAt(salesSer.get(saleDTO.getSalesId()).getCreateAt());
		saleDTO.setModifiedAt(time);
		Sales sale = convertSales(saleDTO);
		salesSer.save(sale);
		return saleDTO;
	}

	@DeleteMapping(value = { "/delete/{saleId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public String deleteSales(@PathVariable("saleId") String saleId, Principal principal) {
		if (checkNullEmpty(saleId))
			throw new NotFoundDataException("");
		SalesDTO sales = null;
		try {
			sales = convertDTO(salesSer.get(UUID.fromString(saleId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		salesSer.delete(sales.getSalesId());
		return "Delete successful SALES{ " + saleId + "}";
	}

	// QueryDsl
	//
	@GetMapping(value = { "/list/queryDsl" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<SalesDTO> getSalesByQueryDsl(@QuerydslPredicate(root = Sales.class) Predicate predicate) {
		List<SalesDTO> list = new ArrayList<>();
		salesSer.getListSalesByQueryDsl(predicate).forEach(e -> list.add(convertDTO(e)));
		LogUtil.debug(LOG, "Request list sales by queryDsl.");
		return list;
	}

	@GetMapping(value = { "/queryDsl/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public SalesDTO getSaleByQueryDsl(@PathVariable("id") String id) {
		QSales qSales = QSales.sales;
		Predicate predicate;
		try {
			predicate = qSales.salesId.eq(UUID.fromString(id));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Sales id ");
		}
		Sales sales = salesSer.getSaleByQueryDsl(predicate);
		SalesDTO result = convertDTO(sales);
		LogUtil.debug(LOG, "Request sale by queryDsl.");
		return result;
	}

	@PutMapping(value = { "/queryDsl/update" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public SalesDTO updateSalesByQueryDsl(@RequestBody SalesDTO saleDTO, Principal principal) {
		if (salesSer.get(saleDTO.getSalesId()) == null) {
			throw new NotFoundDataException("Sales Id ");
		}
		QSales qSales = QSales.sales;
		Predicate predicate = qSales.salesId.eq(saleDTO.getSalesId());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Sales sales = salesSer.getSaleByQueryDsl(predicate);
		sales.setDollars(saleDTO.getDollars());
		sales.setProduct(proSer.get(saleDTO.getProduct()));
		sales.setLocation(locaSer.get(saleDTO.getLocation()));
		sales.setTime(timeSer.get(saleDTO.getTime()));
		sales.setModifiedAt(time);
		salesSer.updateByQueryDsl(saleDTO.getSalesId(), sales);
		saleDTO = convertDTO(sales);
		LogUtil.debug(LOG, "Request update sale by queryDsl.");
		return saleDTO;
	}

	@DeleteMapping(value = { "/queryDsl/delete/{saleId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public String deleteSalesByQueryDsl(@PathVariable("saleId") String saleId, Principal principal) {
		try {
			salesSer.deleteByQueryDsl(UUID.fromString(saleId));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Sales id " + saleId + " ");
		}
		return "Delete successful SALES by queryDsl{ " + saleId + "}";
	}

	// Check null empty
	public boolean checkNullEmpty(String s) {
		if (s != null && !s.isEmpty() && !s.trim().isEmpty())
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
