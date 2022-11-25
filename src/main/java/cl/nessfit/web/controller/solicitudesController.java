package cl.nessfit.web.controller;

import cl.nessfit.web.model.Usuario;
import cl.nessfit.web.model.Solicitud;
import cl.nessfit.web.model.Instalacion;
import cl.nessfit.web.service.IInstalacionService;
import cl.nessfit.web.service.ISolicitudService;
import cl.nessfit.web.service.IUsuarioService;
import cl.nessfit.web.util.RutValidacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Controlador de la vista de solicitudes
 *
 * @author BPCS Corporation
 */
@Controller
@RequestMapping("/administrativo/administrar-solicitudes-arriendo")
public class solicitudesController {
    /**
     * Servicio de las instalaciones
     */
    @Autowired
    private IInstalacionService instalacionService;
    /**
     * Solicitud de servicio en uso
     */
    private Solicitud solicitudUso;

    /**
     * Servicio de las solicitudes
     */
    @Autowired
    private ISolicitudService solicitudService;
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
     * GetMapping para la vista de solicitudes
     * @param model modelo de la vista
     * @return redireccion a la vista de solicitudes
     */
    @GetMapping("")
    public String listarSolicitudes(Model model) {
        model.addAttribute("solicitudes", solicitudService.verTodasSolicitudes());
        return "/administrativo/administrar-solicitudes-arriendo";
    }

    /**
     * Maneja la peticion GET de rechazar una solicitud
     * @param Id id de la solicitud
     * @param model modelo de la vista
     * @return redireccion a la vista de rechazar solicitud
     */
    @GetMapping("/r/{id}")
    public String formRechazarSolicitud(@PathVariable("id") String Id, Model model) {
        Optional<Solicitud> solicitud = solicitudService.buscarPorId(Id);
        model.addAttribute("solicitud",solicitud.get());
        solicitudUso = solicitud.get();
        return "administrativo/solicitud-informacion-rechazo";
    }
    /**
     * Maneja la peticion POST de rechazar una solicitud
     * @param solicitud solicitud a rechazar
     * @param result resultado de la validacion
     * @param attr atributos de la vista
     * @return redireccion a la vista de solicitudes
     */
    @PostMapping("/r/{id}")
    public String formRechazarSolicitudCliente(@Valid Solicitud solicitud, BindingResult result, RedirectAttributes attr){
        solicitud.setId(solicitudUso.getId());
        solicitud.setFechaEmision(solicitudUso.getFechaEmision());
        solicitud.setFechaEstado(solicitudUso.getFechaEstado());
        solicitud.setInstalacion(solicitudUso.getInstalacion());
        solicitud.setUsuario(solicitudUso.getUsuario());
        solicitud.setEstado(3);

        solicitudService.guardar(solicitud);

        return "redirect:/administrativo/administrar-solicitudes-arriendo";
    }

    /**
     * Maneja la peticion GET de aceptar una solicitud
     * @param Id id de la solicitud
     * @param model modelo de la vista
     * @return redireccion a la vista de aceptar solicitud
     */
    @GetMapping("/c/{id}")
    public String formAceptarSolicitud(@PathVariable(value = "id") String Id, Model model){
        Optional<Solicitud> solicitud = solicitudService.buscarPorId(Id);
        model.addAttribute("solicitud",solicitud.get());
        solicitudUso = solicitud.get();
        return "administrativo/solicitud-informacion";
    }
    /**
     * Maneja la peticion POST de aceptar una solicitud
     * @param solicitud solicitud a aceptar
     * @param result resultado de la validacion
     * @param attr atributos de la vista
     * @return redireccion a la vista de solicitudes
     */
    @PostMapping("/c/{id}")
    public String formAceptarSolicitudCliente(@Valid Solicitud solicitud, BindingResult result, RedirectAttributes attr){
           //Si hay errores
        solicitud.setId(solicitudUso.getId());
        solicitud.setFechaEmision(solicitudUso.getFechaEmision());
        solicitud.setFechaEstado(solicitudUso.getFechaEstado());
        solicitud.setInstalacion(solicitudUso.getInstalacion());
        solicitud.setUsuario(solicitudUso.getUsuario());

        solicitud.setEstado(2);

        solicitudService.guardar(solicitud);

        return "redirect:/administrativo/administrar-solicitudes-arriendo";
    }
    /**
     * Modelo del nombre del usuario
     * @return nombre del usuario
     */
    @ModelAttribute("nombreUser")
    public String authName () {
        String rut = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.buscarPorRut(rut);
        return usuario.getNombre() + " " + usuario.getApellido();
    }
    @ModelAttribute("contadorSolicitudesPendientes")
    public int contadorSolicitudesPendientes(){
        int contadorSolicitudes = 0;
        for (Solicitud solicitud : solicitudService.verTodasSolicitudes()) {
            if (solicitud.getEstado() == 1) {
                contadorSolicitudes++;
            }
        }
        return contadorSolicitudes;
    }
    @ModelAttribute("contadorSolicitudesAceptadas")
    public int contadorSolicitudesAceptadas(){
        int contadorSolicitudes = 0;
        for (Solicitud solicitud : solicitudService.verTodasSolicitudes()) {
            if (solicitud.getEstado() == 2) {
                contadorSolicitudes++;
            }
        }
        return contadorSolicitudes;
    }
    @ModelAttribute("contadorSolicitudesRechazadas")
    public int contadorSolicitudesRechazadas(){
        int contadorSolicitudes = 0;
        for (Solicitud solicitud : solicitudService.verTodasSolicitudes()) {
            if (solicitud.getEstado() == 3) {
                contadorSolicitudes++;
            }
        }
        return contadorSolicitudes;
    }
    @ModelAttribute("contadorSolicitudes")
    public int contadorSolicitudes(){
        int contadorSolicitudes = 0;
        for (int i = 0; i < solicitudService.verTodasSolicitudes().size(); i++) {
                contadorSolicitudes++;
        }
        return contadorSolicitudes;
    }



}
