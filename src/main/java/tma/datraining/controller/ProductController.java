package tma.datraining.controller;

import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import tma.datraining.dto.ProductDTO;
import tma.datraining.exception.BadRequestException;
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.model.cassandra.CassProduct;
import tma.datraining.service.ProductService;
import tma.datraining.service.SalesService;
import tma.datraining.service.cassandra.CassProductService;
import tma.datraining.util.LogUtil;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productSer;

	@Autowired
	private SalesService salesSer;

	@Autowired
	private CassProductService cassSer;

	@Autowired
	private DateTimeToTimestampConverter converter;

	private static final Logger LOG  = LoggerFactory.getLogger(ProductController.class);
			
	@RequestMapping(value = { "/convert" }, method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	@ResponseBody
	public List<ProductDTO> convertToProduct() {
		LogUtil.debug(LOG, "Request convert data from Cassandra");
		List<ProductDTO> list = new ArrayList<>();
		cassSer.list().forEach(e -> list.add(convertCassToDTO(e)));
		for (ProductDTO product : list) {
			productSer.save(convertProduct(product));
		}
		LogUtil.debug(LOG, "Response list product");
		return list;
	}

	@GetMapping(value = "/list", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<ProductDTO> getProducts() {
		LogUtil.debug(LOG, "Request list product");
		List<ProductDTO> list = convertDTOList(productSer.list());
		LogUtil.debug(LOG, "Response list Product");
		return list;
	}

	@GetMapping(value = "/{productId}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") String productId) {
		LogUtil.debug(LOG, "Request product with id :"+ productId);
		ProductDTO pro = null;
		try {
			pro = convertProductToDTO(productSer.get(UUID.fromString(productId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Product Id ");
		}
		LogUtil.debug(LOG, "Response product " + pro.toString());
		return new ResponseEntity<>(pro,HttpStatus.OK);

	}

	@GetMapping(value = { "/cass/{productId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public CassProduct getProductCass(@PathVariable("productId") String productId) {
		LogUtil.debug(LOG, "Request a product from Cassandra with id:" + productId);
		CassProduct pro = null;
		try {
			pro = productSer.getCass(UUID.fromString(productId));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Product Id ");
		}
		LogUtil.debug(LOG, "Response product "+ pro.toString());
		return pro;

	}

	@GetMapping(value = { "/class/{classProduct}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<ProductDTO> getProductByClass(@PathVariable("classProduct") String classProduct) {
		LogUtil.debug(LOG, "Request product with class :" + classProduct);
		List<Product> pro = productSer.findByClassProduct(classProduct);
		List<ProductDTO> pr = convertDTOList(pro);
		LogUtil.debug(LOG, "Reponse product " + pro.toString());
		return pr;

	}

	@PostMapping(value = { "/add" }, produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public ProductDTO addProduct(@RequestBody ProductDTO product) throws URISyntaxException {
		LogUtil.debug(LOG, "Add a new product");
		if (product.getClassProduct().isEmpty() || product.getInventory().isEmpty()) {
			throw new BadRequestException("");
		}
		product.setProductId(UUID.randomUUID());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		product.setCreateAt(time);
		product.setModifiedAt(time);
		Product pro = convertProduct(product);
		productSer.save(pro);
		LogUtil.debug(LOG, "Reponse save a new product " + pro.toString());
		return product;

	}

	@PutMapping(value = { "/update/{productId}" }, produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public ProductDTO updateProduct(@RequestBody ProductDTO pro, @PathVariable("productId") String productId) {
		LogUtil.debug(LOG, "Request update a product with id :" + productId);
		UUID id = null;
		try {
			id = UUID.fromString(productId);
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Product Id");
		}
		if (pro.getItem() <= 0 || pro.getClassProduct().isEmpty() || pro.getInventory().isEmpty()) {
			throw new BadRequestException("");
		}
		if (productSer.get(id) == null) {
			throw new NotFoundDataException("Product Id ");
		}
		pro.setProductId(id);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		pro.setCreateAt(productSer.get(id).getCreateAt());
		pro.setModifiedAt(time);
		Product product = convertProduct(pro);
		Set<Sales> sales = new HashSet<>();
		sales.addAll(salesSer.findByProduct(product));
		product.setSales(sales);
		productSer.update(product.getProductId(), product);
		LogUtil.debug(LOG, "Response update a product " + product.toString());
		return pro;
	}

	@DeleteMapping(value = { "/delete/{productId}" }, produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public String deleteProduct(@PathVariable("productId") String productId) {
		LogUtil.debug(LOG, "Delete product with id :"+productId);
		ProductDTO pro = null;
		try {
			pro = convertProductToDTO(productSer.get(UUID.fromString(productId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("No data found!");
		}
		productSer.delete(pro.getProductId());
		LogUtil.debug(LOG, "Response delete product with id :" + productId);
		return "Delete success product{" + productId + "}.";
	}

	
	
	// Convert--
	// Product to DTO
	public ProductDTO convertProductToDTO(Product product) {
		ProductDTO temp = new ProductDTO();
		if (product == null) {
			throw new NotFoundDataException("Product Id ");
		}
		temp.setProductId(product.getProductId());
		temp.setItem(product.getItem());
		temp.setInventory(product.getInventory());
		temp.setClassProduct(product.getClassProduct());
		temp.setCreateAt(product.getCreateAt());
		temp.setModifiedAt(product.getModifiedAt());
		return temp;
	}

	// List product to list DTO
	public List<ProductDTO> convertDTOList(List<Product> list) {
		List<ProductDTO> list2 = new ArrayList<>();
		if (list.size() == 0) {
			throw new NotFoundDataException("");
		}
		list.forEach(e -> list2.add(convertProductToDTO(e)));
		return list2;
	}

	// DTO to product
	public Product convertProduct(ProductDTO product) {
		Product pro = null;
		pro = new Product(product.getItem(), product.getClassProduct(), product.getInventory(), product.getCreateAt(),
				product.getModifiedAt());
		pro.setProductId(product.getProductId());
		return pro;
	}

	// Cass to DTO
	public ProductDTO convertCassToDTO(CassProduct product) {
		ProductDTO pro = new ProductDTO();
		if (product == null) {
			throw new NotFoundDataException("");
		}
		System.out.println(product.getProductId());
		pro.setProductId(product.getProductId());
		pro.setItem(product.getItem());
		pro.setInventory(product.getInventory());
		pro.setClassProduct(product.getClassProduct());
		pro.setCreateAt(converter.convert(product.getCreateAt()));
		pro.setModifiedAt(converter.convert(product.getModifiedAt()));
		return pro;
	}

}
