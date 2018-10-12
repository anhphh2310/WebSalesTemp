package tma.datraining.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tma.datraining.model.AppRole;
import tma.datraining.repository.RoleRepository;

@Service
public class RoleServiceImp implements RoleService{

	@Autowired
	private RoleRepository repository;

	@Override
	public List<AppRole> list() {
		// TODO Auto-generated method stub
		return (List<AppRole>) repository.findAll();
	}

	@Override
	public AppRole get(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id).get();
	}

	@Override
	public Long save(AppRole role) {
		// TODO Auto-generated method stub
		repository.save(role);
		return role.getRoleId();
	}

	@Override
	public void update(Long id, AppRole role) {
		// TODO Auto-generated method stub
		repository.save(role);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		repository.delete(repository.findById(id).get());
	}
	
	
}
