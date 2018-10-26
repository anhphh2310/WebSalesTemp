package tma.datraining.service;

import java.util.List;
import java.util.UUID;

import tma.datraining.model.Time;
import tma.datraining.model.cassandra.CassTime;

public interface TimeService {
	
	List<Time> list();
	
	List<CassTime> listCass();
	
	UUID save(Time time);
	
	UUID saveCass(CassTime time);
	
	Time get(UUID id);
	
	CassTime getCass(UUID id);
	
	void update(UUID id,Time time);

	void updateCass(UUID id , CassTime time);
	
	void delete(UUID id);
	
	void deleteCass(UUID id);
	
	List<Time> findByMonth(int month);
	
	List<Time> findByYear(int year);
	
	List<Time> findByQuarter(int quarter);
	
}
