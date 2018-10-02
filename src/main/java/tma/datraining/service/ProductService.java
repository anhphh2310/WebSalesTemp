package tma.datraining.service;

import java.util.List;
import java.util.UUID;

import tma.datraining.model.Product;

public interface ProductService {

	List<Product> list();

	UUID save(Product product);

	Product get(UUID id);

	void update(UUID id, Product product);
	
	void delete(UUID id);
	
	List<Product> findByClassProduct(String classProduct);
}
