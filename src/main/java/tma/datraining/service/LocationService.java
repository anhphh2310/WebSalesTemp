package tma.datraining.service;

import java.util.List;
import java.util.UUID;

import com.querydsl.core.types.Predicate;

import tma.datraining.model.Location;
import tma.datraining.model.cassandra.CassLocation;

public interface LocationService {

	List<Location> list();
	
	List<CassLocation> listCass();
	
	Location get(UUID id);
	
	CassLocation getCass(UUID id);
	
	Location save(Location location);
	
	CassLocation saveCass(CassLocation location);
	
	Location update(UUID id, Location location);
	
	CassLocation updateCass(UUID id, CassLocation location);
	
	UUID delete(UUID id);
	
	UUID deleteCass(UUID id);
	
	List<Location> findByCity(String city);
	
	List<Location> findByCountry(String country);
	
	//QueryDsl
	Location getLocationByQueryDsl(Predicate predicate);
	
	List<Location> getListLocationsByQueryDsl(Predicate predicate);
	
	List<Location> getListLocationsByQueryDslSortCity();
	
	List<Location> getListLocationsByQueryDslSortCountry();
	
	void updateByQueryDsl(UUID id, Location location);
	
	UUID deleteByQueryDsl(UUID id);
	
	
	
}
