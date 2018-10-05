package tma.datraining.service;

import java.util.List;
import java.util.UUID;

import tma.datraining.model.Location;
import tma.datraining.model.cassandra.CassLocation;

public interface LocationService {

	List<Location> list();
	
	List<CassLocation> listCass();
	
	Location get(UUID id);
	
	CassLocation getCass(UUID id);
	
	UUID save(Location location);
	
	UUID saveCass(CassLocation location);
	
	void update(UUID id, Location location);
	
	void updateCass(UUID id, CassLocation location);
	
	void delete(UUID id);
	
	void deleteCass(UUID id);
}
