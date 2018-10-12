package tma.datraining.service;

import java.util.List;

import tma.datraining.model.AppRole;

public interface RoleService {
	List<AppRole> list();
	
	AppRole get(Long id);
	
	Long save(AppRole role);
	
	void update(Long id, AppRole role);
	
	void delete(Long id);
}
