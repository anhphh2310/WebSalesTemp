package tma.datraining.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import tma.datraining.model.Location;

public interface LocationRepository extends CrudRepository<Location, UUID> {

	Location findByLocationId(UUID locationId);
	
	List<Location> findByCity(String city);
	
	List<Location> findByCountry(String country);
	
}
