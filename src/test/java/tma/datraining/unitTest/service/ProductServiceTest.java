package tma.datraining.unitTest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tma.datraining.model.Product;
import tma.datraining.repository.ProductRepository;
import tma.datraining.service.ProductServiceImp;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class ProductServiceTest {

	@InjectMocks
	private ProductServiceImp productService;

	@Mock
	private ProductRepository productRepository;
	
	UUID id1 = UUID.fromString("6c433e06-d47b-4099-844c-699515214c64");
	UUID id2 = UUID.fromString("6368cfab-c58e-4d0d-9429-975d1bad0f60");
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Ignore
	@Test
	public void testGetAllProducts() throws Exception {
		List<Product> list = new ArrayList<>();
		Product product1 = new Product();
		product1.setProductId(id1);
		product1.setClassProduct("SCREEN");
		product1.setInventory("Inv-1");
		Timestamp time = new Timestamp(System.currentTimeMillis());
		product1.setModifiedAt(time);
		product1.setCreateAt(time);
		Product product2 = new Product();
		product2.setProductId(id2);
		product2.setClassProduct("CORE");
		product2.setInventory("Inv-7");
		product2.setModifiedAt(time);
		product2.setCreateAt(time);
		list.add(product1);
		list.add(product2);
		when(productRepository.findAll()).thenReturn(list);
		List<Product> list2 = productService.list();
		assertNotNull(list2);
		assertEquals(2,list2.size());
	}

	@Ignore
	@Test
	public void testGetProductById() throws Exception {
		Product product = new Product();
		product.setClassProduct("SCREEN");
		product.setInventory("Inv-1");
		when(productRepository.findByProductId(any(UUID.class))).thenReturn(product);
		Product product2 = productService.get(id1);
		assertNotNull(product2);
		assertEquals("SCREEN", product2.getClassProduct());
	}

//	@Ignore
	@Test
	public void testGetProductByClass() throws Exception{
		Product pro = new Product();
		pro.setProductId(id1);
		pro.setInventory("Inv-1");
		Timestamp time = new Timestamp(System.currentTimeMillis());
		pro.setModifiedAt(time);
		pro.setCreateAt(time);
		when(productRepository.findByClassProduct(anyString())).thenReturn(pro);
		Product product = productService.findByClassProduct("SCREEN");
		assertNotNull(product);
		assertEquals("Inv-1",product.getInventory());
	}
	
	@Ignore
	@Test
	public void testAddNewProduct() throws Exception {
		Product product1 = new Product();
		product1.setProductId(id1);
		product1.setClassProduct("SCREEN");
		product1.setInventory("Inv-1");
		Timestamp time = new Timestamp(System.currentTimeMillis());
		product1.setModifiedAt(time);
		product1.setCreateAt(time);
		when(productRepository.save(any(Product.class))).thenReturn(product1);
		Product result = productService.save(product1);
		assertNotNull(result);
		assertEquals(result.getProductId(), id1);
	}

	@Ignore
	@Test
	public void testUpdateProductWithId() throws Exception {
		Product product1 = new Product();
		product1.setProductId(id1);
		product1.setClassProduct("SCREEN");
		product1.setInventory("Inv-2");
		Timestamp time = new Timestamp(System.currentTimeMillis());
		product1.setModifiedAt(time);
		when(productRepository.save(any(Product.class))).thenReturn(product1);
		Product result = productService.update(id1, product1);
		assertNotNull(result);
		assertEquals(result.getClassProduct(), product1.getClassProduct());
	}

//	@Ignore
	@Test
	public void testDeleteProduct() throws Exception{
		Product product1 = new Product();
		product1.setProductId(id1);
		product1.setClassProduct("SCREEN");
		product1.setInventory("Inv-2");
		when(productRepository.findByProductId(any(UUID.class))).thenReturn(product1);
		doNothing().when(productRepository).delete(any(Product.class));
		UUID id = productService.delete(id1);
		assertNotNull(id);
		assertEquals(id, product1.getProductId());
	}
 }
