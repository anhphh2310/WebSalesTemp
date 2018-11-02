package tma.datraining.service;

import java.util.List;
import java.util.UUID;

import com.querydsl.core.types.Predicate;

import tma.datraining.model.Product;
import tma.datraining.model.cassandra.CassProduct;

public interface ProductService {

	List<Product> list();

	List<CassProduct> listCass();
	
	Product save(Product product);

	CassProduct saveCass(CassProduct product);
	
	Product get(UUID id);

	CassProduct getCass(UUID id);
	
	Product update(UUID id, Product product);
	
	CassProduct updateCass(UUID id, CassProduct product);
	
	UUID delete(UUID id);
	
	UUID deleteCass(UUID id);
	
	Product findByClassProduct(String classProduct);
	
	List<Product> findByInventory(String inventory);
	
	//QueryDsl
	Product getProductByQueryDsl(Predicate predicate);
	
	List<Product> getProductsByQueryDsl(Predicate predicate);
	
	List<Product> getProductsByQueryDslSortClass();
	
	List<Product> getProductsByQueryDslSortInventory();

	void updateByQueryDsl(UUID id, Product product);
	
	UUID deleteByQueryDsl(UUID id);
	
}
