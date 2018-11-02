package tma.datraining.unitTest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import tma.datraining.controller.ProductController;
import tma.datraining.converter.DateTimeToTimestampConverter;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.service.ProductService;
import tma.datraining.service.SalesService;
import tma.datraining.service.cassandra.CassProductService;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@WebMvcTest(value = ProductController.class, secure = false)
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@MockBean
	private SalesService salesService;

	@MockBean
	private CassProductService cassService;

	@MockBean
	private DateTimeToTimestampConverter converter;

	MediaType[] m = new MediaType[] { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML };
	UUID id = UUID.fromString("de2b46a4-b535-449e-971a-a1c8a4d65707");
	UUID id2 = UUID.fromString("6c433e06-d47b-4099-844c-699515214c64");

	@Ignore
	@Test
	public void giveProducts_whenGetAllProducts_thenReturn() throws Exception {
		List<Product> list = new ArrayList<>();
		Product product1 = new Product(15500, "USB-TYPE C", "Inv-12", null, null);
		product1.setProductId(id);
		list.add(product1);
		given(this.productService.list()).willReturn(list);
		this.mockMvc.perform(get("/product/list").accept(m)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Ignore
	@Test
	public void giveClassProduct_whenGetProductByClass_thenReturn() throws Exception {
		Product product1 = new Product(15500, "USB-TYPE C", "Inv-12", null, null);
		product1.setProductId(id);
		given(this.productService.findByClassProduct(anyString())).willReturn(product1);
		this.mockMvc.perform(get("/product/class/" + product1.getClassProduct()).accept(m)).andExpect(status().isOk())
				.andExpect(jsonPath("$.inventory", is(product1.getInventory())));
	}

	@Ignore
	@Test
	public void giveInventory_whenGetProductByInventory_thenReturn() throws Exception {
		List<Product> list = new ArrayList<>();
		Product product1 = new Product(15500, "USB-TYPE C", "Inv-12", null, null);
		product1.setProductId(id);
		Product product2 = new Product(150, "SCREEN", "Inv-12", null, null);
		product1.setProductId(id2);
		list.add(product1);
		list.add(product2);
		given(this.productService.findByInventory(anyString())).willReturn(list);
		this.mockMvc.perform(get("/product/inventory/" + product1.getInventory()).accept(m)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(2)))
				.andExpect(jsonPath("$[0].classProduct", is(product1.getClassProduct())));

	}

	@Ignore
	@Test
	public void giveProduct_whenAddProduct_thenReturn() throws Exception {
		Product product1 = new Product(15500, "USB-TYPE C", "Inv-12", null, null);
		given(this.productService.save(product1)).willReturn(product1);
		this.mockMvc.perform(
				post("/product/add").contentType(MediaType.APPLICATION_JSON).content(createProductJsonForAdd(product1)))
				.andExpect(status().isOk()).andDo(print());
	}

	@Ignore
	@Test
	public void giveProduct_whenUpdateProduct_thenReturn() throws Exception {
		Product product1 = new Product(15500, "USB-TYPE C", "Inv-12", null, null);
		product1.setProductId(id);
		List<Sales> list = new ArrayList<>();
		given(this.productService.get(any(UUID.class))).willReturn(product1);
		given(this.salesService.findByProduct(any(Product.class))).willReturn(list);
		given(this.productService.update(eq(UUID.randomUUID()), any(Product.class))).willReturn(product1);
		this.mockMvc.perform(put("/product/update").contentType(MediaType.APPLICATION_JSON)
				.content(createProductJsonUpdate(product1))).andExpect(status().isOk()).andDo(print());
	}

	@Ignore
	@Test
	public void giveId_whenDeleteProduct_thenReturn() throws Exception {
		Product product1 = new Product(15500, "USB-TYPE C", "Inv-12", null, null);
		product1.setProductId(id);
		List<Sales> list = new ArrayList<>();
		given(this.productService.get(any(UUID.class))).willReturn(product1);
		given(salesService.findByProduct(any(Product.class))).willReturn(list);
		given(salesService.delete(eq(UUID.randomUUID()))).willReturn(id);
		given(this.productService.delete(id)).willReturn(id);
		this.mockMvc.perform(delete("/product/delete/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	private String createProductJsonForAdd(Product product) {
		return "{ \"item\": \"" + product.getItem() + "\", " + "\"classProduct\":\"" + product.getClassProduct()
				+ "\", " + "\"inventory\":\"" + product.getInventory() + "\"}";
	}

	private String createProductJsonUpdate(Product product) {
		return "{ \"productId\": \"" + product.getProductId() + "\", " + "\"item\": \"" + product.getItem() + "\", "
				+ "\"classProduct\":\"" + product.getClassProduct() + "\", " + "\"inventory\":\""
				+ product.getInventory() + "\"}";
	}
	
}
