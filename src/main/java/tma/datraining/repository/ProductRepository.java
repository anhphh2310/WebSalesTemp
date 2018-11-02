package tma.datraining.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import tma.datraining.model.Product;

public interface ProductRepository extends CrudRepository<Product, UUID>,QuerydslPredicateExecutor<Product>{

	Product findByClassProduct(String classProduct);
	
	Product findByProductId(UUID id);
	
	List<Product> findByInventory(String inventory);
}
