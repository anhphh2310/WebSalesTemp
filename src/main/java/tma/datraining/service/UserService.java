package tma.datraining.service;

import java.util.List;

import tma.datraining.model.AppUser;

public interface UserService {

	List<AppUser> list();
	
	AppUser get(Long id);
	
	Long save(AppUser user);
	
	void update(Long id, AppUser user);
	
	void delete(Long id);
}
