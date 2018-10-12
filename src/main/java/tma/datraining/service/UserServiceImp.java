package tma.datraining.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tma.datraining.model.AppUser;
import tma.datraining.repository.UserRepository;

@Service
public class UserServiceImp implements UserService{

	@Autowired
	private UserRepository repository;

	@Override
	public List<AppUser> list() {
		// TODO Auto-generated method stub
		return (List<AppUser>) repository.findAll();
	}

	@Override
	public AppUser get(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public Long save(AppUser user) {
		repository.save(user);
		return user.getUserId();
	}

	@Override
	public void update(Long id, AppUser user) {
		// TODO Auto-generated method stub
		repository.save(user);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		repository.delete(repository.findById(id).get());
	}
	
	
}
