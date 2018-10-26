package tma.datraining.service;

import java.util.List;
import java.util.UUID;

import tma.datraining.model.Product;
import tma.datraining.model.cassandra.CassProduct;

public interface ProductService {

	List<Product> list();

	List<CassProduct> listCass();
	
	UUID save(Product product);

	UUID saveCass(CassProduct product);
	
	Product get(UUID id);

	CassProduct getCass(UUID id);
	
	void update(UUID id, Product product);
	
	void updateCass(UUID id, CassProduct product);
	
	void delete(UUID id);
	
	void deleteCass(UUID id);
	
	Product findByClassProduct(String classProduct);
	
	List<Product> findByInventory(String inventory);
	
}
