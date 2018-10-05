package tma.datraining.service.cassandra;

import java.util.List;
import java.util.UUID;

import tma.datraining.model.cassandra.CassLocation;

public interface CassLocationService {

	List<CassLocation> list();
	
	CassLocation get(UUID id);
	
	UUID save(CassLocation location);
	
	void update(UUID id, CassLocation location);
	
	void delete(UUID id);
}
