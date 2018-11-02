package tma.datraining.unitTest.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tma.datraining.model.Product;
import tma.datraining.repository.ProductRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;
	
	
	UUID id1 = UUID.fromString("05ee5277-50c7-4168-af5f-941ea7ab8990");
	UUID id2 = UUID.fromString("f40d5aa4-dfd1-4f9a-b12f-a36ba800c08c");
	UUID wrongId = UUID.fromString("15ee5217-50c7-4168-af5f-941ea7ab8990");
	
	@Before
	public void init() throws Exception{
		Product product1 = new Product(15500, "USB-TYPE C", "Inv-12", null, null);
		product1.setProductId(id1);
		Product product2 = new Product(120,"SCANNER","Inv-12",null,null);
		product2.setProductId(id2);
		productRepository.save(product1);
		productRepository.save(product2);
	}
	
	@Ignore
	@Test
	public void testFindByClassProduct_thenReturnListProduct() throws Exception{
		Product result = productRepository.findByClassProduct("SCANNER");
		assertNotNull(result);
		assertThat(result.getInventory()).isEqualTo("Inv-12");
		
	}
	
	@Ignore
	@Test
	public void testGetProductWithId() throws Exception{
		Optional<Product> product = productRepository.findById(id1);
		assertTrue(product.isPresent());
		assertEquals(id1, product.get().getProductId());
	}
	
	@Ignore
	@Test
	public void testGetProductWithWrongId() throws Exception{
		Product result = productRepository.findByProductId(wrongId);
		assertNull(result);
	}
	
	@Ignore
	@Test
	public void testFindAllProduct() throws Exception{
		Iterable<Product> list = productRepository.findAll();
		assertThat(((Collection<Product>)list).size()).isEqualTo(2);
	}
	
	@Ignore
	@Test
	public void testAddProduct() throws Exception{
		Product product = new Product(100, "CORE", "Inv-7", null, null);
		product.setProductId(UUID.fromString("6368cfab-c58e-4d0d-9429-975d1bad0f60"));
		Product result = productRepository.save(product);
		assertNotNull(result);
		assertThat(result.getClassProduct()).isEqualTo("CORE");
	}

	@Ignore
	@Test
	public void testFindProductByInventory() throws Exception{
		List<Product> results = productRepository.findByInventory("Inv-12");
		assertNotNull(results);
		assertThat(results.size()).isEqualTo(2);
	}
	
	@Ignore
	@Test
	public void testUpdateProductWithId() throws Exception{
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Product product = new Product(120, "SCANNER", "Inv-8", time, time);
		product.setProductId(id2);
		Product result = productRepository.save(product);
		assertNotNull(result);
		assertThat(result.getInventory()).isEqualTo("Inv-8");
	}
	
	@Ignore
	@Test
	public void testDeleteProductWithId() {
		productRepository.deleteById(id1);
		Product result = productRepository.findByProductId(id1);
		assertNull(result);
		Iterable<Product> list = productRepository.findAll();
		assertThat(((Collection<Product>)list).size()).isEqualTo(1);
	}
}
