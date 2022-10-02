package cl.nessfit.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import cl.nessfit.web.model.Usuario;
import cl.nessfit.web.service.IUsuarioService;

@Controller
public class HomeController {
	@Autowired
    IUsuarioService usuarioService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
	
	//@RequestMapping(value = "/",method = RequestMethod.GET)
	@RequestMapping({"/","inicio","","index"})
	public String index() {
		return "menu";
	}
	@RequestMapping("/menu")
	public String menu() {
		return "menu";
	}
	@RequestMapping("/olvido")
	public String olvido() {
		return "olvido";
	}
	@RequestMapping("/registrar")
	public String registrarUsuario() {
		return "register";	
	}
	@GetMapping("/bootstrap.js")  //whichever file it is
	  public String falseLoginRedirect(Model model) {
	    return "redirect:/menu"; //redirect to my preferred default
	}
	@GetMapping("/logout")
    public String logout(HttpServletRequest request) {
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    logoutHandler.logout(request, null, null);
    return "redirect:/login?logout";
    }
	@GetMapping("/cambio")
    public String cambio(HttpServletRequest request) {
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    logoutHandler.logout(request, null, null);
    return "redirect:/login?cambio";
    }
	@ModelAttribute("rutUser")
    public String auth() {
	return SecurityContextHolder.getContext().getAuthentication().getName();
    }
	@GetMapping("/perfil")
    public String perfil(Model model) {
	Usuario usuario = usuarioService.buscarPorRut(SecurityContextHolder.getContext().getAuthentication().getName());
	model.addAttribute("usuario", usuario);
	return "perfil";
    }

    @PostMapping("/perfil")
    public String perfilForm(@Valid Usuario usuario, BindingResult result, Model model) {

	Usuario usuarioAuth = usuarioService
		.buscarPorRut(SecurityContextHolder.getContext().getAuthentication().getName());

	// Paso 1.-Validaciones
	if (result.hasErrors()) {
	    return "perfil";
	}

	// Paso 2.- Set´s
	usuarioAuth.setNombre(usuario.getNombre());
	usuarioAuth.setApellido(usuario.getApellido());
	usuarioAuth.setEmail(usuario.getEmail());
	usuarioAuth.setTelefono(usuario.getTelefono());

	// Paso 3.- Persistencia
	usuarioService.guardar(usuarioAuth);

	// Paso 4.- Redireccion
	return "redirect:/menu";
    }

}