package tma.datraining.service;

import java.util.List;
import java.util.UUID;

import com.querydsl.core.types.Predicate;

import tma.datraining.model.Location;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.model.Time;
import tma.datraining.model.cassandra.CassSales;

public interface SalesService {

	List<Sales> list();
	
	List<CassSales> listCass();
	
	Sales save(Sales sale);
	
	CassSales saveCass(CassSales sale);
	
	Sales get(UUID id);
	
	CassSales getCass(UUID id);
	
	Sales update(UUID id, Sales sale);
	
	CassSales updateCass(UUID id, CassSales sales);
	
	UUID delete(UUID id);
	
	UUID deleteCass(UUID id);
	
	List<Sales> findByProduct(Product product);

	List<Sales> findByLocation(Location location);

	List<Sales> findByTime(Time time);
	
	//QueryDsl
	Sales getSaleByQueryDsl(Predicate predicate);
	
	List<Sales> getListSalesByQueryDsl(Predicate predicate);
	
	void updateByQueryDsl(UUID id, Sales sales);
	
	UUID deleteByQueryDsl(UUID id);
	
}
