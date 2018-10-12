package tma.datraining.repository;

import org.springframework.data.repository.CrudRepository;

import tma.datraining.model.AppUser;

public interface UserRepository extends CrudRepository<AppUser, Long>{

}
