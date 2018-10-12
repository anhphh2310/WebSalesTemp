package tma.datraining.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;

import tma.datraining.model.AppRole;
import tma.datraining.model.AppUser;
import tma.datraining.model.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

	@EntityGraph(value="userRoleEntityGraph",type=EntityGraphType.LOAD)
	List<UserRole> findByAppUser(AppUser appUser);

	@EntityGraph(value="userRoleEntityGraph",type=EntityGraphType.LOAD)
	List<UserRole> findByAppRole(AppRole appRole);
	
	@EntityGraph(value="userRoleEntityGraph",type=EntityGraphType.LOAD)
	List<UserRole> findAll();
}
