package tma.datraining.service;

import java.util.List;
import java.util.UUID;

import tma.datraining.model.Location;

public interface LocationService {

	List<Location> list();
	
	Location get(UUID id);
	
	UUID save(Location location);
	
	void update(UUID id, Location location);
	
	void delete(UUID id);
}
