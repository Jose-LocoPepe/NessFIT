package cl.nessfit.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
	
	//@RequestMapping(value = "/",method = RequestMethod.GET)
	@RequestMapping({"/","login"})
	public String index() {
		return "index";
		
	}
	@RequestMapping("/menu")
	public String menu() {
		return "menu";
	}
	@RequestMapping("/olvido")
	public String olvido() {
		return "olvido";
	}
	@GetMapping("/bootstrap.js")  //whichever file it is
	  public String falseLoginRedirect(Model model) {
	    return "redirect:/menu"; //redirect to my preferred default
	}

}