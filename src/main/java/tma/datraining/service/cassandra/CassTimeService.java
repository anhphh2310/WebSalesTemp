package tma.datraining.service.cassandra;

import java.util.List;
import java.util.UUID;

import tma.datraining.model.cassandra.CassTime;

public interface CassTimeService {

List<CassTime> list();
	
	CassTime get(UUID id);
	
	UUID save(CassTime time);
	
	void update(UUID id,CassTime time);
	
	void delete(UUID id);
}
