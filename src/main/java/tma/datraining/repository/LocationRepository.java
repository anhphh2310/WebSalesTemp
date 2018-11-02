package tma.datraining.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tma.datraining.model.Location;

@Repository
public interface LocationRepository extends CrudRepository<Location, UUID>, QuerydslPredicateExecutor<Location>{

	Location findByLocationId(UUID locationId);
	
	List<Location> findByCity(String city);
	
	List<Location> findByCountry(String country);
	
}
