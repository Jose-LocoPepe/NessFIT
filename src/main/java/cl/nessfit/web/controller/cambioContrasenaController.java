package cl.nessfit.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.nessfit.web.model.Usuario;
import cl.nessfit.web.service.IUsuarioService;


@Controller
public class cambioContrasenaController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/cambiar-contrasena")
    public String cambiarContrasenaForm(Model model) {
	model.addAttribute("nuevaContrasena", "");
	model.addAttribute("nuevaContrasenaRepetir", "");
	return "cambiarContrasena";
    }

    @PostMapping("/cambiar-contrasena")
    public String enviarForm(@RequestParam String nuevaContrasena, @RequestParam String nuevaContrasenaRepetir,
	    HttpServletRequest request, RedirectAttributes attr, Model model) {

	// 1.- usuario logeado
	Usuario usuario = usuarioService.buscarPorRut(SecurityContextHolder.getContext().getAuthentication().getName());

	// 2.- valida contraseñas iguales, usuario no null, contraseña mayor a 10 y
	// menor a 15 caracteres
	if (nuevaContrasena.compareTo(nuevaContrasenaRepetir)!=0) {
		model.addAttribute("msg", "Las contraseñas no son iguales");
	    model.addAttribute("nuevaContrasena", nuevaContrasena);
	    model.addAttribute("nuevaContrasenaRepetir", nuevaContrasenaRepetir);
	    return "cambiarContrasena";
	}
	if (nuevaContrasena.length()<=10 || nuevaContrasena.length()>=15) {
		model.addAttribute("msg", "La contraseña debe tener entre 10 y 15 caracteres");
	    model.addAttribute("nuevaContrasena", nuevaContrasena);
	    model.addAttribute("nuevaContrasenaRepetir", nuevaContrasenaRepetir);
	    return "cambiarContrasena";
	}

	// 3.- set usuario
	usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));

	// 4.- persistencia
	usuarioService.guardar(usuario);

	// 5.- redirección
	SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
	logoutHandler.logout(request, null, null);
	return "redirect:/login?cambio";
    }

    @ModelAttribute("rutUser")
    public String auth() {
	return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
