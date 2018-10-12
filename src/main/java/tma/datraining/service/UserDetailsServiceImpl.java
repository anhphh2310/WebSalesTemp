package tma.datraining.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tma.datraining.model.AppRole;
import tma.datraining.model.AppUser;
import tma.datraining.model.UserRole;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserService userService;

	@Autowired 
	private RoleService roleService;
	
	@Autowired
	private UserRoleService userRoleService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = null;
		for(AppUser e : userService.list()) {
			if(e.getUserName().equals(username)) {
				user = e;
			}
		}
		if(user == null) {
			System.out.println("User not found! " + username);
			throw new UsernameNotFoundException("User " + username +  "was not found in the database");
		}
		System.out.println("Found user " + username);
		List<String> roles = new ArrayList<>();
		for(UserRole e : userRoleService.listByUser(user)) {
			AppRole role = e.getAppRole();
			roles.add(role.getRoleName());
		}
//		for(UserRole e : user.getUserRoles()) {
//			//roles.add(e.getAppRole().getRoleName());
//			System.out.println(e.toString());
//		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		if(roles != null) {
			for (String roleName : roles) {
				GrantedAuthority authority = new SimpleGrantedAuthority(roleName);
				authorities.add(authority);
			}
		}
		UserDetails userDetails = (UserDetails) new User(user.getUserName(),user.getEcryptedPassword(),authorities);
		return userDetails;
	}
	
	
}
