package tma.datraining.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tma.datraining.model.Location;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.model.Time;
import tma.datraining.repository.SalesRepository;

@Service
@Transactional
public class SalesServiceImp implements SalesService{

	@Autowired
	private SalesRepository salesRepo;
	
	@Override
	public List<Sales> list() {
		List<Sales> list = new ArrayList<>();
		salesRepo.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public UUID save(Sales sale) {
		salesRepo.save(sale);
		return sale.getSalesId();
	}

	@Override
	public Sales get(UUID id) {
		Sales sales = null;
		for (Sales sale : list()) {
			if(sale.getSalesId().equals(id))
				sales = sale;
		}
		return sales;
	}

	@Override
	public void update(UUID id, Sales sale) {
		salesRepo.save(sale);
		
	}

	@Override
	public void delete(UUID id) {
		salesRepo.delete(salesRepo.findById(id).get());
	}

	@Override
	public List<Sales> findByProduct(Product product) {
		List<Sales> sales = salesRepo.findByProduct(product);
		return sales;
	}

	@Override
	public List<Sales> findByLocation(Location location) {
		List<Sales> sales = salesRepo.findByLocation(location);
		return sales;
	}

	@Override
	public List<Sales> findByTime(Time time) {
		List<Sales> sales = salesRepo.findByTime(time);
		return sales;
	}
}
