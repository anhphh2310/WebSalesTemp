package tma.datraining.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Location;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.model.Time;
import tma.datraining.model.cassandra.CassSales;
import tma.datraining.repository.SalesRepository;
import tma.datraining.repository.cassandra.CassSalesRepo;

@Service
@Transactional
public class SalesServiceImp implements SalesService{

	@Autowired
	private SalesRepository salesRepo;
	
	@Autowired
	private CassSalesRepo cassRepo;
	
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
		Sales sales = salesRepo.findBySalesId(id);
		if(sales == null) {
			throw new NotFoundDataException("");
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

	@Override
	public List<CassSales> listCass() {
		// TODO Auto-generated method stub
		return (List<CassSales>) cassRepo.findAll();
	}

	@Override
	public UUID saveCass(CassSales sale) {
		// TODO Auto-generated method stub
		cassRepo.save(sale);
		return sale.getProductId();
	}

	@Override
	public CassSales getCass(UUID id) {
		// TODO Auto-generated method stub
		CassSales sales = null;
		sales = cassRepo.findById(id).get();
		return sales;
	}

	@Override
	public void updateCass(UUID id, CassSales sales) {
		// TODO Auto-generated method stub
		cassRepo.save(sales);
	}

	@Override
	public void deleteCass(UUID id) {
		// TODO Auto-generated method stub
		cassRepo.delete(cassRepo.findById(id).get());
	}
}
