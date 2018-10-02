package tma.datraining.repository.cassandra;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import tma.datraining.model.cassandra.CassProduct;

public interface CassProductRepo extends CrudRepository<CassProduct, UUID>{

}
