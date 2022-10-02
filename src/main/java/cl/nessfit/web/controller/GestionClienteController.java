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

    @Autowired
    private RutValidacion validation;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(validation);
    }
    @GetMapping({"/index","/menu"})
    public String menu() {
        return "menu";
    }
    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("usuarios", usuarioService.verClientes());
        return "administrativo/gestion-cliente";
    }

    @GetMapping("/editar/{rut}")
    public String formEditar(@PathVariable(value = "rut") String rut, Model model) {
        Usuario usuario = usuarioService.buscarPorRut(rut);
        model.addAttribute("usuario", usuario);
        return "/administrativo/form-editar-cliente";
    }

    @PostMapping("/editar/{rut}")
    public String formEditarUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr) {

        // paso 1 validaciones
        // result.rejectValue("rut", null, "rut inválido");

        if (result.hasErrors()) {
            return "/administrativo/form-editar-cliente";
        }

        // paso 2 set atributos no ingresados por usuario
        usuario.setContrasena(passwordEncoder.encode(usuario.getRut()));
        usuario.setEstado(2);
        Rol rolCliente = new Rol();
        rolCliente.setId(3);
        usuario.setRol(rolCliente);

        // paso 3 persistencia
        usuarioService.guardar(usuario);

        // paso 4 redireccionamiento
        return "redirect:/administrativo/gestion-cliente";
    }

    @GetMapping("/crear")
    public String formUsuario(Usuario usuario) {
        return "/administrativo/form-crear-cliente";
    }

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
        //verificar si el correo se encuentra en la base de datos
        Usuario userEmail = usuarioService.buscarPorEmail(usuario.getEmail());
        if (userEmail != null) {
            result.rejectValue("email", null, "El correo ya existe");
        }
        //Verificador de nombres y rut
        if (usuario.getNombre().length() < 3) {
            result.rejectValue("nombre", null, "El nombre debe tener al menos 3 caracteres");
        }
        if (usuario.getApellido().length() < 3) {
            result.rejectValue("apellido", null, "El apellido debe tener al menos 3 caracteres");
        }
        if (usuario.getTelefono().length() < 11 || usuario.getTelefono().length() > 16) {
            result.rejectValue("telefono", null, "El movil debe tener entre 11 y 16 caracteres");
        }

        String rut = usuario.getRut();
        int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));
        char dv = rut.charAt(rut.length() - 1);
        int m = 0, s = 1;
        for (; rutAux != 0; rutAux /= 10) {
            s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
        }
        char dvCalculado = (char) (s != 0 ? s + 47 : 75);

        if (dvCalculado != dv) {
            result.rejectValue("rut", null, "El rut ingresado es invalido♿");
        }



        // paso 1 validaciones
        // result.rejectValue("rut", null, "rut inválido");

        if (result.hasErrors()) {
            return "/administrativo/form-crear-cliente";
        }

        // paso 2 set atributos no ingresados por usuario
        usuario.setContrasena(passwordEncoder.encode(usuario.getRut()));
        usuario.setEstado(2);
        Rol rolCliente = new Rol();
        rolCliente.setId(3);
        usuario.setRol(rolCliente);
        System.out.println(usuario.toString());

        // paso 3 persistencia
        usuarioService.guardar(usuario);

        // paso 4 redireccionamiento
        return "redirect:/administrativo/gestion-cliente";
    }

    @ModelAttribute("rutUser")
    public String auth() {
        //Usuario usuario =usuarioService.buscarPorRut(SecurityContextHolder.getContext().getAuthentication().getName());
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}