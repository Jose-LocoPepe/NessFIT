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
 * Controlador de la vista de Gestión de Clientes
 *
 * @author BPCS Corporation
 */
@Controller
@RequestMapping("/administrativo/gestion-cliente")
public class GestionClienteController {

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
     * Inyección del bean de validación de rut
     */
    @Autowired
    private RutValidacion validation;

    /**
     * Inicializa el binder para la validación de rut
     *
     * @param binder binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(validation);
    }

    /**
     * Maneja la petición GET para la volver al menu
     *
     * @return Menu principal
     */
    @GetMapping({"/index", "/menu"})
    public String menu() {
        return "menu";
    }

    /**
     * Maneja la petición GET para la vista de gestión de clientes
     *
     * @param model modelo
     * @return vista de gestión de clientes
     */
    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("usuarios", usuarioService.verClientes());
        return "/administrativo/gestion-cliente";
    }

    /**
     * Maneja la petición GET para la vista de edicion de clientes
     *
     * @param rut   rut de la peticion
     * @param model modelo
     * @return vista del menu de edicion de clientes
     */
    @GetMapping("/editar/{rut}")
    public String formEditar(@PathVariable(value = "rut") String rut, Model model) {
        Usuario usuario = usuarioService.buscarPorRut(rut);
        model.addAttribute("usuario", usuario);
        return "/administrativo/form-editar-cliente";
    }

    /**
     * Maneja la peticion POST para la vista de la edicion de clientes
     *
     * @param usuario usuario de la peticion
     * @param result  el resultado de la peticion
     * @param attr
     * @return vista del menu de edicion de clientes
     */
    @PostMapping("/editar/{rut}")
    public String formEditarUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr) {
        //Verificador de nombres y rut
        if (usuario.getNombre().length() < 3) {
            result.rejectValue("nombre", null, "El nombre debe tener al menos 3 caracteres");
        }
        if (usuario.getApellido().length() < 3) {
            result.rejectValue("apellido", null, "El apellido debe tener al menos 3 caracteres");
        }
        if(hayNumeros(usuario.getNombre())){
            result.rejectValue("nombre", null, "El nombre no debe contener numeros");
        } else if (hayNumeros(usuario.getApellido())){
            result.rejectValue("apellido", null, "El apellido no debe contener numeros");
        }
        //Si hay errores
        if (result.hasErrors()) {
            return "/administrativo/form-editar-cliente";
        }
        //Datos del cliente a editar
        usuario.setContrasena(passwordEncoder.encode(usuario.getRut()));
        Rol rolCliente = new Rol();
        rolCliente.setId(3);
        usuario.setRol(rolCliente);
        if(usuarioService.buscarEstado(usuario.getRut()) == 1){
            usuario.setEstado(1);
        } else {
            usuario.setEstado(0);
        }
        //Guarda el cliente
        usuarioService.guardar(usuario);

        //Redirecciona a la vista de gestion de clientes
        return "redirect:/administrativo/gestion-cliente";
    }

    /**
     * Maneja la peticion GET para crear un usuario
     *
     * @return retorna la vista para crear clientes
     */
    @GetMapping("/crear")
    public String formUsuario(Usuario usuario) {
        return "/administrativo/form-crear-cliente";
    }

    /**
     * Maneja la peticion POST para crear un usuario
     *
     * @param usuario usuario de la peticion
     * @param result  el resultado de la peticion
     * @param attr    atributos
     * @return retorna la vista para crear clientes
     */

    @PostMapping("/crear")
    public String formCrearUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr) {
        //Verificadores del rut
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
        if(hayNumeros(usuario.getNombre())){
            result.rejectValue("nombre", null, "El nombre no debe contener numeros");
        } else if (hayNumeros(usuario.getApellido())){
            result.rejectValue("apellido", null, "El apellido no debe contener numeros");
        }

        // Verifica si hay errores

        if (result.hasErrors()) {
            return "/administrativo/form-crear-cliente";
        }

        // Atributos del formulario
        usuario.setContrasena(passwordEncoder.encode(usuario.getRut()));
        usuario.setEstado(1);
        Rol rolCliente = new Rol();
        rolCliente.setId(3);
        usuario.setRol(rolCliente);
        System.out.println(usuario.toString());

        // Guardado del usuario
        usuarioService.guardar(usuario);

        // Redireccionamiento a la vista principal
        return "redirect:/administrativo/gestion-cliente";
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
    //boolean para ver si hay numeros en un string
    public boolean hayNumeros(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }
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