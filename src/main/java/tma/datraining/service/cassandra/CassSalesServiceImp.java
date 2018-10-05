package tma.datraining.service.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tma.datraining.model.cassandra.CassSales;
import tma.datraining.repository.cassandra.CassSalesRepo;

@Service
public class CassSalesServiceImp implements CassSalesService {

	@Autowired
	private CassSalesRepo cassRepo;
	
	@Override
	public List<CassSales> list() {
		List<CassSales> list = new ArrayList<>();
		cassRepo.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public CassSales get(UUID id) {
		CassSales sales = null;
		sales = cassRepo.findById(id).get();
		return sales;
	}

	@Override
	public UUID save(CassSales sales) {
		cassRepo.save(sales);
		return sales.getProductId();
	}

	@Override
	public void update(UUID id, CassSales sales) {
		// TODO Auto-generated method stub
		cassRepo.save(sales);
	}

	@Override
	public void delete(UUID id) {
		// TODO Auto-generated method stub
		cassRepo.delete(cassRepo.findById(id).get());
	}

}
