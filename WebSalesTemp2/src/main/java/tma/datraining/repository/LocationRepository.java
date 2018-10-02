package tma.datraining.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import tma.datraining.model.Location;

public interface LocationRepository extends CrudRepository<Location, UUID> {

}
