package tma.datraining.repository.cassandra;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import tma.datraining.model.cassandra.CassLocation;

public interface CassLocationRepo extends CrudRepository<CassLocation, UUID> {

}
