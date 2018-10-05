package tma.datraining.service.cassandra;

import java.util.List;
import java.util.UUID;

import tma.datraining.model.cassandra.CassSales;

public interface CassSalesService {

	List<CassSales> list();

	CassSales get(UUID id);

	UUID save(CassSales sales);

	void update(UUID id, CassSales sales);

	void delete(UUID id);
}
