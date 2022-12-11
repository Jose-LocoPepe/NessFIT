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
public class solicitudesController {
    @Autowired
    private IInstalacionService instalacionService;

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

    @GetMapping("/administrativo/administrar-solicitudes-arriendo")
    public String solicitudes(){
        return "/administrativo/administrar-solicitudes-arriendo";
    }


    @ModelAttribute("nombreUser")
    public String authName() {
        String rut = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.buscarPorRut(rut);

        return "  " +usuario.getNombre() + " " + usuario.getApellido();

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
