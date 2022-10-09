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

/**
 * Controlador de la vista de cambio de contraseña
 *
 * @author BPCS Corporation
 *
 */
@Controller
public class cambioContrasenaController {
	/**
	 * Inyección de servicio de usuario
	 */
    @Autowired
    private IUsuarioService usuarioService;
	/**
	 * Inyeccion de encriptador de contraseñas
	 */

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

	/**
	 * Atributos del cambio de contraseña
	 * @param model modelo de la vista
	 * @return vista de cambio de contraseña
	 */
    @GetMapping("/cambiar-contrasena")
    public String cambiarContrasenaForm(Model model) {
	model.addAttribute("nuevaContrasena", "");
	model.addAttribute("nuevaContrasenaRepetir", "");
	return "cambiarContrasena";
    }

	/**
	 * Cambia la contraseña del usuario
	 * @param nuevaContrasena nueva contraseña
	 * @param nuevaContrasenaRepetir repetición de la nueva contraseña
	 * @param request petición
	 * @param attr	redirección
	 * @param model modelo de la vista
	 * @return Vista del cambio de Contraseña
	 */
    @PostMapping("/cambiar-contrasena")
    public String enviarForm(@RequestParam String nuevaContrasena, @RequestParam String nuevaContrasenaRepetir,
	    HttpServletRequest request, RedirectAttributes attr, Model model) {

	// Verifica si el usuario esta logueado
	Usuario usuario = usuarioService.buscarPorRut(SecurityContextHolder.getContext().getAuthentication().getName());

	// Valida contraseñas iguales, usuario no null, contraseña mayor a 10 y
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

	// Set del usuario
	usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));

	// Guardado
	usuarioService.guardar(usuario);

	// Redireccion
	SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
	logoutHandler.logout(request, null, null);
	return "redirect:/login?cambio";
    }

	/**
	 * authName para buscar el nombre del usuario en la base de datos
	 * @return nombre y apellido en formato string
	 */
	@ModelAttribute("nombreUser")
	public String authName() {
		String rut = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioService.buscarPorRut(rut);

		return usuario.getNombre() + " " + usuario.getApellido();

	}

	/**
	 * auth para buscar el rut en la base de datos
	 * @return rut en formato string
	 */
    @ModelAttribute("rutUser")
    public String auth() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
