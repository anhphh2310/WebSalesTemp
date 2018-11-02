package tma.datraining.service;

import java.util.List;
import java.util.UUID;

import com.querydsl.core.types.Predicate;

import tma.datraining.model.Time;
import tma.datraining.model.cassandra.CassTime;

public interface TimeService {
	
	List<Time> list();
	
	List<CassTime> listCass();
	
	Time save(Time time);
	
	CassTime saveCass(CassTime time);
	
	Time get(UUID id);
	
	CassTime getCass(UUID id);
	
	Time update(UUID id,Time time);

	CassTime updateCass(UUID id , CassTime time);
	
	UUID delete(UUID id);
	
	UUID deleteCass(UUID id);
	
	List<Time> findByMonth(int month);
	
	List<Time> findByYear(int year);
	
	List<Time> findByQuarter(int quarter);
	
	//QueryDsl
	Time getTimeByQueryDsl(Predicate predicate);
	
	List<Time> getListTimeByQueryDsl(Predicate predicate);
	
	List<Time> getTimesByQueryDslSortingMonth();
	
	List<Time> getTimesByQueryDslSortingByQuarter();
	
	List<Time> getTimesByQueryDslSortingByYear();
	
	void updateByQueryDsl(UUID id, Time time);
	
	UUID deleteByQueryDsl(UUID id);
	
}
