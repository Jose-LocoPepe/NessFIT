package cl.nessfit.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import cl.nessfit.web.model.Solicitud;
import cl.nessfit.web.service.IInstalacionService;
import cl.nessfit.web.service.ISolicitudService;
import net.bytebuddy.asm.Advice;
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

/**
 * Controlador de la vista de Home
 *
 * @author BPCS Corporation
 */

@Controller
public class HomeController {
    /**
     *
     */
    @Autowired
    IUsuarioService usuarioService;
    @Autowired
    ISolicitudService solicitudService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    IInstalacionService instalacionService;

    //@RequestMapping(value = "/",method = RequestMethod.GET)
    @RequestMapping({"/", "inicio", "", "index"})
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

    /**
     * auth para buscar el nombre del rut logueado
     *
     * @return Nombre y apellido del usuario logueado
     */
    @ModelAttribute("nombreUser")
    public String auth() {
        String rut = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.buscarPorRut(rut);
        return "  " + usuario.getNombre() + " " + usuario.getApellido();
    }
    //obtener contador de todos los usuarios con el rol CLIENTE
    @ModelAttribute("contadorClientes")
    public int contador() {
        int contador=0;
        for(Usuario u : usuarioService.verClientes()){
            contador++;
        }
        return contador;
    }
    @ModelAttribute("contadorInstalaciones")
    public int contadorInstalaciones() {
        return instalacionService.verTodasIntalaciones().size();
    }
    @ModelAttribute("solicitesEnTotal")
    public int solicitesEnTotal() {
        int contador = 0;
        for(Solicitud s : solicitudService.verTodasSolicitudes()){
            contador++;
        }
        return contador;
    }
    @ModelAttribute("solicitesEnTotalPendientes")
    public int solicitesEnTotalPendientes() {
        int contador = 0;
        for(Solicitud s : solicitudService.verTodasSolicitudes()){
            if(s.getEstado() == 0 ){
                contador++;
            }
        }
        return contador;
    }
    @ModelAttribute("contadorReservas")
    public int contadorSolicitudes(){
        int contador = 0;
        String rut = SecurityContextHolder.getContext().getAuthentication().getName();
        for(Solicitud s : solicitudService.verTodasSolicitudes()){
            if(s.getUsuario().getRut().equalsIgnoreCase(rut)){
                contador++;
            }
        }
        return contador;
    }
    //contador solicitudes pendientes
    @ModelAttribute("contadorSolicitudesPendientes")
    public int contadorSolicitudesPendientes(){
        int contador=0;
        String rut = SecurityContextHolder.getContext().getAuthentication().getName();
        for(Solicitud s : solicitudService.verTodasSolicitudes()){
            if(s.getUsuario().getRut().equalsIgnoreCase(rut)){
                if(s.getEstado() == 0) {
                    contador++;
                }
            }
        }
        return contador;
    }
    @ModelAttribute("contadorSolicitudesAprobadas")
    public int contadorSolicitudesAprobadas(){
        int contador=0;
        String rut = SecurityContextHolder.getContext().getAuthentication().getName();
        for(Solicitud s : solicitudService.verTodasSolicitudes()){
            if(s.getUsuario().getRut().equalsIgnoreCase(rut)){
                if(s.getEstado() == 1) {
                    contador++;
                }
            }
        }
        return contador;
    }
    @ModelAttribute("contadorSolicitudesRechazadas")
    public int contadorSolicitudesRechazadas(){
        int contador=0;
        String rut = SecurityContextHolder.getContext().getAuthentication().getName();
        for(Solicitud s : solicitudService.verTodasSolicitudes()){
            if(s.getUsuario().getRut().equalsIgnoreCase(rut)){
                if(s.getEstado() == 2) {
                    contador++;
                }
            }
        }
        return contador;
    }
    /**
     * authRut para obtener el rut del usuario logueado
     *
     * @return retorna el rut
     */
    @ModelAttribute("rutUser")
    public String authRut() {
        //Usuario usuario =usuarioService.buscarPorRut(SecurityContextHolder.getContext().getAuthentication().getName());
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}