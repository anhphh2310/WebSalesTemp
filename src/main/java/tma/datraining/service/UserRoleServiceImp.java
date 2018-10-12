package tma.datraining.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tma.datraining.model.AppRole;
import tma.datraining.model.AppUser;
import tma.datraining.model.UserRole;
import tma.datraining.repository.UserRoleRepository;

@Service
public class UserRoleServiceImp implements UserRoleService {

	@Autowired
	private UserRoleRepository repository;

	@Override
	public List<UserRole> list() {
		// TODO Auto-generated method stub
		return (List<UserRole>) repository.findAll();
	}

	@Override
	public UserRole get(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id).get();
	}

	@Override
	public Long save(UserRole e) {
		// TODO Auto-generated method stub
		repository.save(e);
		return e.getId();
	}

	@Override
	public void update(Long id, UserRole e) {
		// TODO Auto-generated method stub
		repository.save(e);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		repository.delete(repository.findById(id).get());
	}

	@Override
	public List<UserRole> listByUser(AppUser user) {
		// TODO Auto-generated method stub
		return (List<UserRole>) repository.findByAppUser(user);
	}

	@Override
	public List<UserRole> listByRole(AppRole role) {
		// TODO Auto-generated method stub
		return (List<UserRole>) repository.findByAppRole(role);
	}

}
