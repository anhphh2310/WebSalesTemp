package tma.datraining.integrationTest.controller;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import tma.datraining.controller.ProductController;
import tma.datraining.model.Product;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

	@Autowired
	private MockMvc mock;

	@Autowired
	protected WebApplicationContext weapp;
	
	@Autowired
	private ProductController productController;

	Product product1;
	UUID id1;

	@Before
	public void init() throws Exception {
	//	this.mock = standaloneSetup(this.productController).build();
		this.mock = MockMvcBuilders.webAppContextSetup(weapp).build();
		product1 = new Product(15500, "USB", "Inv-12", null, null);
		mock.perform(post("/product/add").contentType(MediaType.APPLICATION_JSON)
				.content(createProductJsonForAdd(product1)));
		productController.getProducts().forEach(e -> id1 = e.getProductId());
	}

	@Ignore
	@Test
	public void testGetAllProducts() throws Exception {
		mock.perform(get("/product/list").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(1))).andExpect(jsonPath("$.*.classProduct", hasItem(is("USB"))))
				.andDo(print());
	}

	@Ignore
	@Test
	public void testGetLocationById() throws Exception {
		mock.perform(get("/product/{id1}", id1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Ignore
	@Test
	public void testGetProductByClassProduct() throws Exception {
		String classPro = "USB";
		mock.perform(get("/product/class/{classPro}", classPro).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Ignore
	@Test
	public void testGetProductByInventory() throws Exception {
		String inventory = "Inv-12";
		mock.perform(get("/product/inventory/{inventory}", inventory).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*.classProduct", hasItem(is("USB"))));

	}

	@Ignore
	@Test
	public void testAddProduct() throws Exception {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Product product = new Product(15500, "USB", "Inv-12", time, time);
		mock.perform(
				post("/product/add").contentType(MediaType.APPLICATION_JSON).content(createProductJsonForAdd(product)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.classProduct", is("USB"))).andDo(print());
	}

	@Ignore
	@Test
	public void tesUpdateProduct() throws Exception {
		Product product = new Product(155, "USB-type c", "Inv-102", null, null);
		product.setProductId(id1);
		mock.perform(put("/product/update").contentType(MediaType.APPLICATION_JSON)
				.content(createProductJsonForUpdate(product))).andExpect(status().isOk()).andDo(print());

	}

	@Ignore
	@Test
	public void tesDeleteProduct() throws Exception {
		mock.perform(delete("/product/delete/{id1}", id1)).andExpect(status().isOk()).andDo(print());
	}

	private String createProductJsonForAdd(Product product) {
		return "{ \"item\": \"" + product.getItem() + "\", " + "\"classProduct\":\"" + product.getClassProduct()
				+ "\", " + "\"inventory\":\"" + product.getInventory() + "\"}";
	}

	private String createProductJsonForUpdate(Product product) {
		return "{ \"productId\": \"" + product.getProductId() + "\", " + "\"item\": \"" + product.getItem() + "\", "
				+ "\"classProduct\":\"" + product.getClassProduct() + "\", " + "\"inventory\":\""
				+ product.getInventory() + "\"}";
	}

}
