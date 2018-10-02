package tma.datraining.controller;

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
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.service.ProductService;
import tma.datraining.service.SalesService;

@RestController
public class ProductController {

	@Autowired
	private ProductService productSer;

	@Autowired
	private SalesService salesSer;

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
			throw new NotFoundDataException("No data found!");
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

	@RequestMapping(value = { "/product" }, method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	@ResponseBody
	public ProductDTO addProduct(@RequestBody ProductDTO product) {
		Product pro = convertProduct(product);
		UUID id = productSer.save(pro);
		System.out.println("Add a new product : " + id);
		return product;

	}

	@RequestMapping(value = { "/product" }, method = RequestMethod.PUT, produces = { "application/json",
			"application/xml" })
	@ResponseBody
	public ProductDTO updateProduct(@RequestBody ProductDTO pro) {
		Product product = convertProduct(pro);
		Set<Sales> sales = new HashSet<>();
		sales.addAll(salesSer.findByProduct(product));
		product.setSales(sales);
		productSer.update(product.getProductId(), product);
		System.out.println("Edit a product : " + product.getProductId());
		ProductDTO pr = convertDTO(product);
		return pr;
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
			throw new NotFoundDataException("No data found!");
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
}
