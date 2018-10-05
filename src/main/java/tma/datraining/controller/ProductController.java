package tma.datraining.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tma.datraining.dto.ProductDTO;
import tma.datraining.exception.BadRequestException;
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.model.cassandra.CassProduct;
import tma.datraining.service.ProductService;
import tma.datraining.service.SalesService;
import tma.datraining.service.cassandra.CassProductService;

@RestController
public class ProductController {

	@Autowired
	private ProductService productSer;

	@Autowired
	private SalesService salesSer;

	@Autowired
	private CassProductService cassSer;

	@RequestMapping(value = { "/products" }, method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	@ResponseBody
	public List<ProductDTO> getProducts() {
		List<ProductDTO> list = convertDTOList(productSer.list());
		return list;
	}

	@RequestMapping(value = { "/product/{productId}" }, method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	@ResponseBody
	public ProductDTO getProduct(@PathVariable("productId") String productId) {
		ProductDTO pro = null;
		try {
			pro = convertDTO(productSer.get(UUID.fromString(productId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Product Id ");
		}
		return pro;

	}

	@RequestMapping(value = { "/product/cass/{productId}" }, method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	@ResponseBody
	public CassProduct getProductCass(@PathVariable("productId") String productId) {
		CassProduct pro = null;
		try {
			pro = productSer.getCass(UUID.fromString(productId));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Product Id ");
		}
		return pro;

	}

	@RequestMapping(value = { "/product/class/{classProduct}" }, method = RequestMethod.GET, produces = {
			"application/json", "application/xml" })
	@ResponseBody
	public List<ProductDTO> getProductByClass(@PathVariable("classProduct") String classProduct) {
		List<Product> pro = productSer.findByClassProduct(classProduct);
		System.out.println(pro.toString());
		System.out.println(pro);
		List<ProductDTO> pr = convertDTOList(pro);
		return pr;

	}

	@RequestMapping(value = { "/product/convert" }, method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	@ResponseBody
	public String convertToProduct() {
//		List<Product> list = new ArrayList<>();
		List<CassProduct> list2 = new ArrayList<>();
		Timestamp create = null;
		Timestamp modified = null;
		for (CassProduct cassProduct : cassSer.list()) {
			create = cassProduct.getCreateAt();
			modified = cassProduct.getModifiedAt();

		}
//		cassSer.list().forEach(e -> list2.add(e));
		// list.forEach(e -> productSer.save(e));
		return " " + create + "," + modified;
	}

	@RequestMapping(value = { "/product" }, method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	@ResponseBody
	public ProductDTO addProduct(@RequestBody ProductDTO product) throws BadRequestException {
		if (product.getInventory().isEmpty() || product.getClassProduct().isEmpty()) {
			throw new BadRequestException("");
		}
		Timestamp time = new Timestamp(System.currentTimeMillis());
		product.setCreateAt(time);
		product.setModifiedAt(time);
		Product pro = convertProduct(product);
		UUID id = productSer.save(pro);
		System.out.println("Add a new product : " + id);
		return product;

	}

	@RequestMapping(value = { "/product/productId" }, method = RequestMethod.PUT, produces = { "application/json",
			"application/xml" })
	@ResponseBody
	public ProductDTO updateProduct(@RequestBody ProductDTO pro, @PathVariable("productId") String productId) {
		UUID id = null;
		try {
			id = UUID.fromString(productId);
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("Product Id");
		}
		if (pro.getClassProduct().isEmpty() || pro.getInventory().isEmpty()) {
			throw new BadRequestException("");
		}
		if (productSer.get(id) == null) {
			throw new NotFoundDataException("Product Id ");
		}
		pro.setProductId(id);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		pro.setCreateAt(time);
		pro.setModifiedAt(time);
		Product product = convertProduct(pro);
		Set<Sales> sales = new HashSet<>();
		sales.addAll(salesSer.findByProduct(product));
		product.setSales(sales);
		productSer.update(product.getProductId(), product);
		return pro;
	}

	@RequestMapping(value = { "/product/{productId}" }, method = RequestMethod.DELETE, produces = { "application/json",
			"application/xml" })
	@ResponseBody
	public void deleteProduct(@PathVariable("productId") String productId) {
		ProductDTO pro = null;
		try {
			pro = convertDTO(productSer.get(UUID.fromString(productId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("No data found!");
		}
		productSer.delete(pro.getProductId());
		System.out.println("Delete product " + productId);
	}

	public ProductDTO convertDTO(Product product) {
		ProductDTO temp = null;
		if (product == null) {
			throw new NotFoundDataException("Product Id ");
		}
		temp = new ProductDTO(product.getProductId(), product.getItem(), product.getClassProduct(),
				product.getInventory(), product.getCreateAt(), product.getModifiedAt());
		return temp;
	}

	public List<ProductDTO> convertDTOList(List<Product> list) {
		List<ProductDTO> list2 = new ArrayList<>();
		if (list.size() == 0) {
			throw new NotFoundDataException("");
		}
		list.forEach(e -> list2.add(convertDTO(e)));
		return list2;
	}

	public Product convertProduct(ProductDTO product) {
		Product pro = null;
		pro = new Product(product.getItem(), product.getClassProduct(), product.getInventory(), product.getCreateAt(),
				product.getModifiedAt());
		pro.setProductId(product.getProductId());
		return pro;
	}

	public Product convertCassToProduct(CassProduct product) {
		Product pro = null;
		if (product == null) {
			throw new NotFoundDataException("Product Id ");
		}
		pro = new Product(product.getItem(), product.getClassProduct(), product.getInventory(),
				(Timestamp) product.getCreateAt(), (Timestamp) product.getModifiedAt());
		return pro;
	}

}
