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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrativo/administrar-solicitudes-arriendo")
public class solicitudesController {
    @Autowired
    private IInstalacionService instalacionService;

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

    @GetMapping("")
    public String listarSolicitudes(Model model) {
        model.addAttribute("solicitudes", solicitudService.verTodasSolicitudes());
        return "administrativo/administrar-solicitudes-arriendo";
    }

    @ModelAttribute("nombreUser")
    public String authName() {
        String rut = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.buscarPorRut(rut);

        return usuario.getNombre() + " " + usuario.getApellido();

    }
}
