package tma.datraining.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Product;
import tma.datraining.model.cassandra.CassProduct;
import tma.datraining.repository.ProductRepository;
import tma.datraining.repository.cassandra.CassProductRepo;

@Service
@Transactional
public class ProductServiceImp implements ProductService{

	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private CassProductRepo cassRepo;
	
	@Override
	public List<Product> list() {
		List<Product> list = new ArrayList<>();
		productRepo.findAll().forEach(e ->list.add(e));
		return list;
	}

	@Override
	public UUID save(Product product) {
		productRepo.save(product);
		return product.getProductId();
	}

	@Override
	public Product get(UUID id) {
		// TODO Auto-generated method stub
		Product pro = null;
		List<Product> list = new ArrayList<>();
		productRepo.findAll().forEach(e ->list.add(e));
		for (Product product : list) {
			if(product.getProductId().toString().equals(id.toString())) {
				pro = product;
			}
		}
		if(pro == null)
			throw new NotFoundDataException("Product id ");
		return pro;
	}

	@Override
	public void update(UUID id, Product product) {
		// TODO Auto-generated method stub
		productRepo.save(product);
	}

	@Override
	public void delete(UUID id) {
		// TODO Auto-generated method stub
		productRepo.delete(productRepo.findById(id).get());
	}

	@Override
	public List<Product> findByClassProduct(String classProduct) {
		List<Product> list = null;
		list = productRepo.findByClassProduct(classProduct);
		return list;
	}

	@Override
	public List<CassProduct> listCass() {
		// TODO Auto-generated method stub
		return (List<CassProduct>) cassRepo.findAll();
	}

	@Override
	public UUID saveCass(CassProduct product) {
		cassRepo.save(product);
		return product.getProductId();
	}

	@Override
	public CassProduct getCass(UUID id) {
		// TODO Auto-generated method stub
		CassProduct product = null;
		product = cassRepo.findById(id).get();
		return product;
	}

	@Override
	public void updateCass(UUID id, CassProduct product) {
		// TODO Auto-generated method stub
		cassRepo.save(product);
	}

	@Override
	public void deleteCass(UUID id) {
		// TODO Auto-generated method stub
		cassRepo.delete(cassRepo.findById(id).get());
	}

}
