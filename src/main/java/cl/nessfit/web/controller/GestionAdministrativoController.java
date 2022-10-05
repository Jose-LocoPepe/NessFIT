package cl.nessfit.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.nessfit.web.model.Rol;
import cl.nessfit.web.model.Usuario;
import cl.nessfit.web.service.IUsuarioService;
import cl.nessfit.web.util.RutValidacion;

@Controller
//Mapeo de clase (aplica a todas las funciones declaradas.
@RequestMapping("/administrador/gestion-adm")
public class GestionAdministrativoController {

    /**
     * Inyección del servicio de usuarios
     */
    @Autowired
    private IUsuarioService usuarioService;

    /**
     * Inyección del bean de encriptación de contraseñas
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RutValidacion validation;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
	binder.addValidators(validation);
    }

    @GetMapping("")
    public String index(Model model) {
	model.addAttribute("usuarios", usuarioService.verAdministrativos());
	return "/administrador/gestion-adm";
    }

    @GetMapping("/editar/{rut}")
    public String formEditar(@PathVariable(value = "rut") String rut, Model model) {
	Usuario usuario = usuarioService.buscarPorRut(rut);
	model.addAttribute("usuario", usuario);
	return "/administrador/form-editar-administrativo";
    }

    @PostMapping("/editar/{rut}")
    public String formEditarUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr) {

	// paso 1 validaciones
	// result.rejectValue("rut", null, "rut inválido");

	if (result.hasErrors()) {
	    return "/administrador/form-editar-administrativo";
	}

	// paso 2 set atributos no ingresados por usuario
	usuario.setContrasena(passwordEncoder.encode(usuario.getRut()));
	usuario.setEstado(1);
	Rol rolAdministrativo = new Rol();
	rolAdministrativo.setId(2);
	usuario.setRol(rolAdministrativo);

	// paso 3 persistencia
	usuarioService.guardar(usuario);

	// paso 4 redireccionamiento
	return "redirect:/administrador/gestion-adm";
    }

    @GetMapping("/crear")
    public String formUsuario(Usuario usuario) {
	return "/administrador/form-crear-administrativo";
    }

    @PostMapping("/crear")
    public String formCrearUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr) {

	// paso 1 validaciones
	// result.rejectValue("rut", null, "rut inválido");

	if (result.hasErrors()) {
	    return "/administrador/form-crear-administrativo";
	}

	// paso 2 set atributos no ingresados por usuario
	usuario.setContrasena(passwordEncoder.encode(usuario.getRut()));
	usuario.setEstado(1);
	Rol rolAdministrativo = new Rol();
	rolAdministrativo.setId(2);
	usuario.setRol(rolAdministrativo);
	System.out.println(usuario.toString());

	// paso 3 persistencia
	usuarioService.guardar(usuario);

	// paso 4 redireccionamiento
	return "redirect:/administrador/gestion-adm";
    }

    @ModelAttribute("rutUser")
    public String auth() {
	//Usuario usuario =usuarioService.buscarPorRut(SecurityContextHolder.getContext().getAuthentication().getName());
	return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}