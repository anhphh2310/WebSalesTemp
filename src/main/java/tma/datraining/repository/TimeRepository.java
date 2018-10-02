package tma.datraining.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import tma.datraining.model.Time;

public interface TimeRepository extends CrudRepository<Time, UUID>{

}
