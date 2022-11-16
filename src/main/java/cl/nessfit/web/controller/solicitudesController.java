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

@Controller
@RequestMapping("/administrativo/administrar-solicitudes-arriendo")
public class solicitudesController {
    @Autowired
    private IInstalacionService instalacionService;

    public Solicitud solicitudUso;

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

    @GetMapping("")
    public String listarSolicitudes(Model model) {
        model.addAttribute("solicitudes", solicitudService.verTodasSolicitudes());
        return "/administrativo/administrar-solicitudes-arriendo";
    }

    @GetMapping("/r/{id}")
    public String formRechazarSolicitud(@PathVariable("id") String Id, Model model) {
        Optional<Solicitud> solicitud = solicitudService.buscarPorId(Id);
        model.addAttribute("solicitud",solicitud.get());
        solicitudUso = solicitud.get();
        return "administrativo/solicitud-informacion-rechazo";
    }
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

    @GetMapping("/c/{id}")
    public String formAceptarSolicitud(@PathVariable(value = "id") String Id, Model model){
        Optional<Solicitud> solicitud = solicitudService.buscarPorId(Id);
        model.addAttribute("solicitud",solicitud.get());
        solicitudUso = solicitud.get();
        return "administrativo/solicitud-informacion";
    }
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

    @ModelAttribute("nombreUser")
    public String authName () {
        String rut = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.buscarPorRut(rut);

        return usuario.getNombre() + " " + usuario.getApellido();

    }

}
