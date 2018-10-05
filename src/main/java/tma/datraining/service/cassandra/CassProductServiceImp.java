package tma.datraining.service.cassandra;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tma.datraining.model.cassandra.CassProduct;
import tma.datraining.repository.cassandra.CassProductRepo;

@Service
public class CassProductServiceImp implements CassProductService {

	@Autowired
	private CassProductRepo cassRepo;
	
	@Override
	public List<CassProduct> list() {
		return (List<CassProduct>) cassRepo.findAll();
	}

	@Override
	public CassProduct get(UUID id) {
		CassProduct product = null;
		product = cassRepo.findById(id).get();
		return product;
	}

	@Override
	public UUID save(CassProduct product) {
		cassRepo.save(product);
		return product.getProductId();
	}

	@Override
	public void update(UUID id, CassProduct product) {
		cassRepo.save(product);

	}

	@Override
	public void delete(UUID id) {
		// TODO Auto-generated method stub
		cassRepo.delete(cassRepo.findById(id).get());
	}

}
