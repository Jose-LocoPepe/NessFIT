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

/**
 * Controlador de la vista de Gestión Administrativa
 *
 * @author BPCS Corporation
 */
@Controller
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
    /**
     * Inyeccion del bean de validacion de rut
     */
    @Autowired
    private RutValidacion validation;

    /**
     * Inicializa el binder para la validacion del rut
     *
     * @param binder binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(validation);
    }

    /**
     * Maneja la peticion GET para la vista de gestion de administrativos
     *
     * @param model modelo
     * @return vista de gestión de administrativos
     */
    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("usuarios", usuarioService.verAdministrativos());
        return "/administrador/gestion-adm";
    }

    /**
     * Maneja la peticion GET para la vista de edicion de un rut
     *
     * @param rut   rut de la peticion
     * @param model modelo
     * @return vista del menu de edicion de administrativos
     */
    @GetMapping("/editar/{rut}")
    public String formEditar(@PathVariable(value = "rut") String rut, Model model) {
        Usuario usuario = usuarioService.buscarPorRut(rut);
        model.addAttribute("usuario", usuario);
        return "/administrador/form-editar-administrativo";
    }

    /**
     * Maneja la peticion POST para la vista de la edicion de administrativos
     *
     * @param usuario usuario de la peticion
     * @param result  el resultado de la peticion
     * @param attr
     * @return vista del menu de edicion de administrativos
     */
    @PostMapping("/editar/{rut}")
    public String formEditarUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr) {
        // Si hay errores
        if (result.hasErrors()) {
            return "/administrador/form-editar-administrativo";
        }

        //Datos del administrativo a editar
        usuario.setContrasena(passwordEncoder.encode(usuario.getRut()));
        usuario.setEstado(1);
        Rol rolAdministrativo = new Rol();
        rolAdministrativo.setId(2);
        usuario.setRol(rolAdministrativo);

        // Guarda el administrativo
        usuarioService.guardar(usuario);

        // Redirecciona a la vista de gestion de administrativos
        return "redirect:/administrador/gestion-adm";
    }

    /**
     * Maneja la peticion GET para crear un administrativo
     *
     * @return retorna la vista para crear administrativo
     */
    @GetMapping("/crear")
    public String formUsuario(Usuario usuario) {
        return "/administrador/form-crear-administrativo";
    }

    /**
     * Maneja la peticion POST para crear un administrativo
     *
     * @param usuario usuario de la peticion
     * @param result  el resultado de la peticion
     * @param attr    atributos
     * @return retorna la vista para crear administrativos
     */
    @PostMapping("/crear")
    public String formCrearUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr) {
        //Verificadores del rut

        //verificar el campo rut si es un rut numerico

        if (result.hasErrors()) {
            return "/administrador/form-crear-administrativo";
        }


        Usuario userRut = usuarioService.buscarPorRut(usuario.getRut());

        if (userRut != null) {
            result.rejectValue("rut", null, "El rut ya existe");
        }
        if (usuario.getRut().contains(".") || usuario.getRut().contains("-")) {
            result.rejectValue("rut", null, "El rut no debe tener puntos ni guion");
        }
        if (!rutValido(usuario.getRut())) {
            result.rejectValue("rut", null, "El rut no es valido");
        }


        //verificar si el correo se encuentra en la base de datos
        Usuario userEmail = usuarioService.buscarPorEmail(usuario.getEmail());
        if (userEmail != null) {
            result.rejectValue("email", null, "El email ya existe");
        }
        //Verificador de nombres y rut
        if (usuario.getNombre().length() < 3) {
            result.rejectValue("nombre", null, "El nombre debe tener al menos 3 caracteres");
        }
        if (usuario.getApellido().length() < 3) {
            result.rejectValue("apellido", null, "El apellido debe tener al menos 3 caracteres");
        }
        if (usuario.getTelefono().length() < 11 || usuario.getTelefono().length() > 16) {
            result.rejectValue("telefono", null, "El telefono debe tener entre 11 y 16 caracteres");
        }

        // Si hay errores
        if (result.hasErrors()) {
            return "/administrador/form-crear-administrativo";
        }
        //Datos del administrativo a crear
        usuario.setContrasena(passwordEncoder.encode(usuario.getRut()));
        usuario.setEstado(1);
        Rol rolAdministrativo = new Rol();
        rolAdministrativo.setId(2);
        usuario.setRol(rolAdministrativo);
        System.out.println(usuario.toString());
        // Guarda el administrativo
        usuarioService.guardar(usuario);
        // Redirecciona a la vista de gestion de administrativos
        return "redirect:/administrador/gestion-adm";
    }

    /**
     * authName para buscar el nombre del rut logueado
     *
     * @return Nombre y apellido del usuario logueado
     */
    @ModelAttribute("nombreUser")
    public String authName() {
        String rut = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.buscarPorRut(rut);

        return usuario.getNombre() + " " + usuario.getApellido();

    }

    /**
     * auth para obtener el rut del usuario logueado
     *
     * @return retorna el rut
     */
    @ModelAttribute("rutUser")
    public String auth() {
        //Usuario usuario =usuarioService.buscarPorRut(SecurityContextHolder.getContext().getAuthentication().getName());
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    //verificador de rut con verificador de numeros
    public boolean rutValido(String rut) {
        try {
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));
            char dv = rut.charAt(rut.length() - 1);
            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            char dvCalculado = (char) (s != 0 ? s + 47 : 75);

            if (dvCalculado != dv) {
                return false;
            }
            return true;

        } catch (NumberFormatException excepcion) {
            return false;
        }
    }

}