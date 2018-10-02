package tma.datraining.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import tma.datraining.model.Product;

public interface ProductRepository extends CrudRepository<Product, UUID>{

	List<Product> findByClassProduct(String classProduct);
}
