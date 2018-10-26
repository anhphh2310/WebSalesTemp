package tma.datraining.integrationTest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tma.datraining.model.Product;
import tma.datraining.service.ProductService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

	@Autowired
	private ProductService productService;
	
	UUID id1 = UUID.fromString("11984e09-c354-4d83-8a98-ddb705fb8f31");
	@Test
	public void testGetAllProducts() throws Exception{
		List<Product> results = productService.list();
		assertNotNull(results);
		assertThat(results.size()).isNotEqualTo(0);
	}
	
	@Test
	public void testGetProductById() throws Exception{
		Product result = productService.get(id1);
		assertNotNull(result);
		assertThat(result.getClassProduct()).isEqualTo("SPEAKER");
	}
	
	@Test
	public void testGetProductByClass() throws Exception{
		Product result = productService.findByClassProduct("CORE");
		assertNotNull(result);
		assertThat(result.getInventory()).isEqualTo("Inv-7");
	}
	
	@Test
	public void testGetProductByInventory() throws Exception{
		List<Product> results = productService.findByInventory("Inv-12");
		assertNotNull(results);
		assertThat(results.size()).isEqualTo(3);
	}
	
}
