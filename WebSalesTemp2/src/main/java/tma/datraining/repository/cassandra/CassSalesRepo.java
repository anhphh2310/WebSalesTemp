package tma.datraining.repository.cassandra;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import tma.datraining.model.cassandra.CassSales;

public interface CassSalesRepo extends CrudRepository<CassSales, UUID>{

}
