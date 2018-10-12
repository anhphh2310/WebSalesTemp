package tma.datraining.service;

import java.util.List;

import tma.datraining.model.AppRole;
import tma.datraining.model.AppUser;
import tma.datraining.model.UserRole;

public interface UserRoleService {

	List<UserRole> list();

	UserRole get(Long id);

	Long save(UserRole e);

	void update(Long id, UserRole e);

	void delete(Long id);

	List<UserRole> listByUser(AppUser user);

	List<UserRole> listByRole(AppRole role);
}
