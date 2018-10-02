package tma.datraining.service;

import java.util.List;
import java.util.UUID;

import tma.datraining.model.Location;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.model.Time;

public interface SalesService {

	List<Sales> list();
	
	UUID save(Sales sale);
	
	Sales get(UUID id);
	
	void update(UUID id, Sales sale);
	
	void delete(UUID id);
	
	List<Sales> findByProduct(Product product);

	List<Sales> findByLocation(Location location);

	List<Sales> findByTime(Time time);
}
