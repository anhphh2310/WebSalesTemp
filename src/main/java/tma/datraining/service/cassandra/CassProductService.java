package tma.datraining.service.cassandra;

import java.util.List;
import java.util.UUID;

import tma.datraining.model.cassandra.CassProduct;

public interface CassProductService {

	List<CassProduct> list();
	
	CassProduct get(UUID id);
	
	UUID save(CassProduct product);
	
	void update(UUID id,CassProduct product);
	
	void delete(UUID id);
	
}
