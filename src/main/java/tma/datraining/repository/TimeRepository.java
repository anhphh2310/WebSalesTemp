package tma.datraining.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import tma.datraining.model.Time;

public interface TimeRepository extends CrudRepository<Time, UUID>{

	Time findByTimeId(UUID id);
	
	List<Time> findByMonth(int month);
	
	List<Time> findByQuarter(int quarter);
	
	List<Time> findByYear(int year);
	
}
