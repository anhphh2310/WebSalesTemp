
package tma.datraining.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tma.datraining.exception.ForbiddentException;
import tma.datraining.util.WebUtils;

@Controller
public class SecurityController {

	@RequestMapping(value= {"/","/welcome"}, method=RequestMethod.GET)
	public String welcomePage(Model model) {
		model.addAttribute("title", "Welcome");
		model.addAttribute("message", "This is welcome page!");
		return "welcomePage";
	}
	
	@RequestMapping(value= {"/admin"}, method=RequestMethod.GET)
	public String adminPage(Model model,Principal principal) {
		User loginUser = (User) ((Authentication) principal).getPrincipal();
		String userInfo =  WebUtils.toString(loginUser);
		model.addAttribute("userInfo", userInfo);
		return "adminPage";
	}
	
	@RequestMapping(value= {"/login"}, method=RequestMethod.GET)
	public String loginPage(Model model) {
		return "loginPage";
	}
	
	@RequestMapping(value= {"/logoutSuccessful"}, method=RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		model.addAttribute("title", "Log out");
		return "logoutSuccessfulPage";
	}
	
	@RequestMapping(value= {"/userInfo"}, method=RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {
		User loginUser = (User) ((Authentication) principal).getPrincipal();
		String userInfo = WebUtils.toString(loginUser);
		model.addAttribute("userInfo", userInfo);
		return "userInfoPage";
	}
	
	@RequestMapping(value= {"/403"}, method=RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {
		if(principal!=null) {
			User loginUser = (User) ((Authentication) principal).getPrincipal();
			String userInfo = WebUtils.toString(loginUser);
			model.addAttribute("userInfo", userInfo);
			String message = "Hi" + principal.getName() + "<br> You do not have permission to access this page";
			model.addAttribute("message", message);
		}
		return "403Page";
	}
	
	@RequestMapping(value= {"/403"}, method=RequestMethod.DELETE)
	public String accessDenied2(Model model, Principal principal) {
		if(principal!=null) {
			User loginUser = (User) ((Authentication) principal).getPrincipal();
			String userInfo = WebUtils.toString(loginUser);
			model.addAttribute("userInfo", userInfo);
			String message = "Hi" + principal.getName() + "<br> You do not have permission to access this page";
			model.addAttribute("message", message);
			throw new ForbiddentException("User " + principal.getName() + " ");
		}
		return "403Page";
	}
	
	@RequestMapping(value= {"/403"}, method=RequestMethod.POST)
	public String accessDenied3(Model model, Principal principal) {
		if(principal!=null) {
			User loginUser = (User) ((Authentication) principal).getPrincipal();
			String userInfo = WebUtils.toString(loginUser);
			model.addAttribute("userInfo", userInfo);
			String message = "Hi" + principal.getName() + "<br> You do not have permission to access this page";
			model.addAttribute("message", message);
			throw new ForbiddentException("User " + principal.getName() + " ");
		}
		return "403Page";
	}
	@RequestMapping(value= {"/403"}, method=RequestMethod.PUT)
	public String accessDenied4(Model model, Principal principal) {
		if(principal!=null) {
			User loginUser = (User) ((Authentication) principal).getPrincipal();
			String userInfo = WebUtils.toString(loginUser);
			model.addAttribute("userInfo", userInfo);
			String message = "Hi" + principal.getName() + "<br> You do not have permission to access this page";
			model.addAttribute("message", message);
			throw new ForbiddentException("User " + principal.getName() + " ");
		}
		return "403Page";
	}
	
	
	
}
