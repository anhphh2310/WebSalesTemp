package tma.datraining.repository.cassandra;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import tma.datraining.model.cassandra.CassTime;


public interface CassTimeRepo extends CrudRepository<CassTime, UUID>{

}
