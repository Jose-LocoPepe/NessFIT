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
 * Controlador de la vista de Habilitacion de clientes
 *
 * @author BPCS Corporation
 */
@Controller
@RequestMapping("/administrador/habilitacion-cliente")
public class HabiliationClienteController {

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
        model.addAttribute("usuarios", usuarioService.verClientes());
        return "/administrador/habilitacion-cliente";
    }
    @GetMapping("/estado-cliente/{rut}")
    public String formEstado(@PathVariable(value = "rut") String rut, Model model) {
        Usuario usuario = usuarioService.buscarPorRut(rut);
        model.addAttribute("usuario", usuario);
        return "/administrador/estado-cliente";
    }

    /**
     * Maneja la peticion POST para la vista de la edicion de administrativos
     *
     * @param usuario usuario de la peticion
     * @param result  el resultado de la peticion
     * @param attr
     * @return vista del menu de edicion de administrativos
     */

    @PostMapping("/estado-cliente/{rut}")
    public String formEstadoCliente(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr) {


        usuario.setNombre(usuario.getNombre());
        usuario.setApellido(usuario.getApellido());
        usuario.setEmail(usuario.getEmail());
        usuario.setTelefono(usuario.getTelefono());
        usuario.setContrasena(passwordEncoder.encode(usuario.getRut()));
        Rol rolCliente = new Rol();
        rolCliente.setId(3);
        usuario.setRol(rolCliente);
        if(usuarioService.buscarEstado(usuario.getRut()) == 1){
            usuario.setEstado(0);
        } else {
            usuario.setEstado(1);
        }

        // Paso 3.- Persistencia
        usuarioService.guardar(usuario);

        // Paso 4.- Redireccion
        return "redirect:/administrador/habilitacion-cliente";
    }
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
    public boolean hayNumeros(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                return true;
            }
        }
        return false;
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
