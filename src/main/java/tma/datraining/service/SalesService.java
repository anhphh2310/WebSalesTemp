package tma.datraining.service;

import java.util.List;
import java.util.UUID;

import tma.datraining.model.Location;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.model.Time;
import tma.datraining.model.cassandra.CassSales;

public interface SalesService {

	List<Sales> list();
	
	List<CassSales> listCass();
	
	UUID save(Sales sale);
	
	UUID saveCass(CassSales sale);
	
	Sales get(UUID id);
	
	CassSales getCass(UUID id);
	
	void update(UUID id, Sales sale);
	
	void updateCass(UUID id, CassSales sales);
	
	void delete(UUID id);
	
	void deleteCass(UUID id);
	
	List<Sales> findByProduct(Product product);

	List<Sales> findByLocation(Location location);

	List<Sales> findByTime(Time time);
}
