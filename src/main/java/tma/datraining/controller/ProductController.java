package tma.datraining.controller;

import java.net.URISyntaxException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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

import com.querydsl.core.types.Predicate;

import tma.datraining.converter.DateTimeToTimestampConverter;
import tma.datraining.dto.ProductDTO;
import tma.datraining.exception.BadRequestException;
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Product;
import tma.datraining.model.QProduct;
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

	private static final Logger LOG = LogManager.getLogger(ProductController.class);

	@RequestMapping(value = { "/convert" }, method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	@ResponseBody
	public List<ProductDTO> convertToProduct() {
		LogUtil.info(LOG, "Request convert data from Cassandra.");
		cassSer.list().forEach(e -> productSer.save(convertCassToJPA(e)));
		List<ProductDTO> list = new ArrayList<>();
		productSer.list().forEach(e -> list.add(convertProductToDTO(e)));
		return list;
	}

	@GetMapping(value = "/list", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<ProductDTO> getProducts() {
		LogUtil.info(LOG, "Request list product.");
		List<ProductDTO> list = convertDTOList(productSer.list());
		return list;
	}
	
	@GetMapping(value = "/{productId}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") String productId) {
		LogUtil.info(LOG, "Request product with id :" + productId + ".");
		ProductDTO pro = null;
		try {
			pro = convertProductToDTO(productSer.get(UUID.fromString(productId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Product Id ");
		}
		LogUtil.info(LOG, "Response product " + pro.toString());
		return new ResponseEntity<>(pro, HttpStatus.OK);

	}

	@GetMapping(value = "/inventory/{inventory}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<ProductDTO>> getProductsByInventory(@PathVariable("inventory") String inventory) {
		LogUtil.info(LOG, "Request get products by inventory :" + inventory + ".");
		List<Product> list = productSer.findByInventory(inventory);
		List<ProductDTO> listResult = new ArrayList<>();
		list.forEach(e -> listResult.add(convertProductToDTO(e)));
		return new ResponseEntity<>(listResult, HttpStatus.OK);

	}

	@GetMapping(value = { "/class/{classProduct}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public ProductDTO getProductByClass(@PathVariable("classProduct") String classProduct) {
		LogUtil.info(LOG, "Request product with class :" + classProduct + ".");
		Product pro = productSer.findByClassProduct(classProduct);
		ProductDTO pr = convertProductToDTO(pro);
		return pr;

	}

	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public ProductDTO addProduct(@RequestBody ProductDTO product, Principal principal) throws URISyntaxException {
		if (check(product.getClassProduct()) || check(product.getInventory())) {
			throw new BadRequestException("");
		}
		product.setProductId(UUID.randomUUID());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		product.setCreateAt(time);
		product.setModifiedAt(time);
		Product pro = convertDTOToProduct(product);
		productSer.save(pro);
		LogUtil.info(LOG, "Request add a new product "+ product.toString() + ".");
		return product;

	}

	@PutMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public ProductDTO updateProduct(@RequestBody ProductDTO pro, Principal principal) {
		if (pro.getItem() <= 0 || check(pro.getClassProduct()) || check(pro.getInventory())) {
			throw new BadRequestException("");
		}
		if (productSer.get(pro.getProductId()) == null) {
			throw new NotFoundDataException("Product Id ");
		}
		Timestamp time = new Timestamp(System.currentTimeMillis());
		pro.setCreateAt(productSer.get(pro.getProductId()).getCreateAt());
		pro.setModifiedAt(time);
		Product product = convertDTOToProduct(pro);
		Set<Sales> sales = new HashSet<>();
		sales.addAll(salesSer.findByProduct(product));
		product.setSales(sales);
		productSer.update(product.getProductId(), product);
		LogUtil.info(LOG, "Request update a product with id :" + pro.getProductId());
		return pro;
	}

	@DeleteMapping(value = { "/delete/{productId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public String deleteProduct(@PathVariable("productId") String productId, Principal principal) {
		ProductDTO pro = null;
		try {
			pro = convertProductToDTO(productSer.get(UUID.fromString(productId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("No data found!");
		}
		List<Sales> list = salesSer.findByProduct(productSer.get(UUID.fromString(productId)));
		if (list.size() > 0) {
			list.forEach(e -> salesSer.delete(e.getSalesId()));
		}
		productSer.delete(pro.getProductId());
		LogUtil.info(LOG, "Delete product with id :" + productId + ".");
		return "Delete success PRODUCT{ " + productId + "}";
	}

	//DSL
		@GetMapping(value = "/queryDsl/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
		@ResponseBody
		public ProductDTO getProducTByQueryDsl(@PathVariable("id") String id) {
			LogUtil.info(LOG, "Request get product by queryDsl.");
			QProduct qProduct = QProduct.product;
			Predicate predicate; 
			try{ predicate = qProduct.productId.eq(UUID.fromString(id));}
			catch(IllegalArgumentException e) {
				throw new NotFoundDataException("Product id ");
			}
			Product product = productSer.getProductByQueryDsl(predicate);
			ProductDTO result = convertProductToDTO(product);
			return result;
		}
		
		@GetMapping(value = "/list/queryDsl", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
		@ResponseBody
		public List<ProductDTO> getProductsByQueryDsl(@QuerydslPredicate(root=Product.class) Predicate predicate) {
			LogUtil.info(LOG, "Request list product by queryDsl.");
			List<Product> list = productSer.getProductsByQueryDsl(predicate);
			List<ProductDTO> result = new ArrayList<>();
			list.forEach(e -> result.add(convertProductToDTO(e)));
			return result;
		}
		
		@GetMapping(value = "/list/queryDsl/class", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
		@ResponseBody
		public List<ProductDTO> getProductsByQueryDslSortClass() {
			LogUtil.info(LOG, "Request list product sorting by class.");
			List<Product> list = productSer.getProductsByQueryDslSortClass();
			List<ProductDTO> result = new ArrayList<>();
			list.forEach(e -> result.add(convertProductToDTO(e)));
			return result;
		}
		
		@GetMapping(value = "/list/queryDsl/inventory", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
		@ResponseBody
		public List<ProductDTO> getProductsByQueryDslSortInventory() {
			LogUtil.info(LOG, "Request list product sorting by class.");
			List<Product> list = productSer.getProductsByQueryDslSortInventory();
			List<ProductDTO> result = new ArrayList<>();
			list.forEach(e -> result.add(convertProductToDTO(e)));
			return result;
		}
		
		@PutMapping(value = { "/queryDsl/update" }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
		@ResponseBody
		public ProductDTO updateProductByQueryDsl(@RequestBody ProductDTO pro) {
			if (pro.getItem() <= 0 || check(pro.getClassProduct()) || check(pro.getInventory())) {
				throw new BadRequestException("");
			}
			QProduct qProduct = QProduct.product;
			Predicate predicate = qProduct.productId.eq(pro.getProductId());
			Timestamp time = new Timestamp(System.currentTimeMillis());
			Product product = productSer.getProductByQueryDsl(predicate);
			productSer.updateByQueryDsl(pro.getProductId(), product);
			pro.setCreateAt(product.getCreateAt());
			pro.setModifiedAt(time);
			LogUtil.info(LOG, "Request update a product with id by queryDsl:" + pro.getProductId());
			return pro;
		}
		
		@DeleteMapping(value = { "/queryDsl/delete/{productId}" }, produces = { MediaType.APPLICATION_JSON_VALUE,
				MediaType.APPLICATION_XML_VALUE })
		@ResponseBody
		public String deleteProductByQueryDsl(@PathVariable("productId") String productId, Principal principal) {
			List<Sales> list = salesSer.findByProduct(productSer.get(UUID.fromString(productId)));
			if (list.size() > 0) {
				list.forEach(e -> salesSer.delete(e.getSalesId()));
			}
			productSer.deleteByQueryDsl(UUID.fromString(productId));
			LogUtil.info(LOG, "Delete product with id :" + productId + ".");
			return "Delete success PRODUCT{ " + productId + "}";
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
	public Product convertDTOToProduct(ProductDTO product) {
		Product pro = null;
		pro = new Product(product.getItem(), product.getClassProduct(), product.getInventory(), product.getCreateAt(),
				product.getModifiedAt());
		pro.setProductId(product.getProductId());
		return pro;
	}

	// Cass to DTO
	public Product convertCassToJPA(CassProduct product) {
		Product pro = new Product();
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

	// Check
	public boolean check(String s) {
		if (s != null && !s.isEmpty() && !s.trim().isEmpty())
			return false;
		return true;
	}
}
