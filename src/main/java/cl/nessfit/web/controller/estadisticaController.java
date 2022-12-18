package cl.nessfit.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cl.nessfit.web.model.Solicitud;
import cl.nessfit.web.model.Usuario;
import cl.nessfit.web.service.ISolicitudService;
import cl.nessfit.web.service.IUsuarioService;
import cl.nessfit.web.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/administrativo/ver-estadisticas")
public class estadisticaController {
    @Autowired
    private ISolicitudService solicitudService;

    @Autowired
    private IUsuarioService usuarioService;

    public int contadorSolicitudes = 0;
    public int contadorCancha = 0;
    public int contadorGimnasio = 0;
    public int contadorPiscina = 0;
    public int contadorQuincho = 0;
    public int contadorEstadio = 0;
    public List<Solicitud> solicitudesList;
    @GetMapping("")
    public String estadistica (Model model,
                               @RequestParam(name = "inicio", required = false, defaultValue = "1900-01-01 00:00:00") String inicio,
                               @RequestParam(name = "fin", required = false, defaultValue = "2999-01-01 00:00:00") String fin) throws ParseException {

        //verificar que las fechas sean validas en rango
        contadorCancha = 0;
        contadorGimnasio = 0;
        contadorPiscina = 0;
        contadorQuincho = 0;
        contadorEstadio = 0;
        contadorSolicitudes=0;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse(inicio + " 00:00:00");
        Date finDate = formatter.parse(fin + " 23:59:59") ;
        if (date.after(finDate) || date.equals(finDate)) {
            model.addAttribute("esError", true);
            model.addAttribute("errorFormato", "La fecha de inicio no puede ser mayor o igual a la fecha de fin.");
            return "administrativo/ver-estadisticas";
        }
        if(solicitudesList == null){
            solicitudesList = solicitudService.buscarPorRangoFecha(date, finDate);

        }
        solicitudesList.clear();

        List<Solicitud> solicitudes2 = solicitudService.verTodasSolicitudes();
        for (Solicitud solicitud : solicitudes2) {
            if (solicitud.getFechaEmision().after(date) && solicitud.getFechaEmision().before(finDate)) {
                solicitudesList.add(solicitud);
            }
        }
        for (Solicitud solicitud : solicitudesList) {
            if (solicitud.getFechaEmision().after(date) && solicitud.getFechaEmision().before(finDate)) {
                if (solicitud.getInstalacion().getTipo().equalsIgnoreCase("Cancha")) {
                    contadorCancha++;
                }
                if (solicitud.getInstalacion().getTipo().equalsIgnoreCase("Gimnasio")) {
                    contadorGimnasio++;
                }
                if (solicitud.getInstalacion().getTipo().equalsIgnoreCase("Piscina")) {
                    contadorPiscina++;
                }
                if (solicitud.getInstalacion().getTipo().equalsIgnoreCase("Quincho")) {
                    contadorQuincho++;
                }
                if (solicitud.getInstalacion().getTipo().equalsIgnoreCase("Estadio")) {
                    contadorEstadio++;
                }
            }
            contadorSolicitudes++;
        }

        model.addAttribute("listaSolicitudes", solicitudesList);
        model.addAttribute("solicitudes", solicitudes2);
        model.addAttribute("cuentaCancha", contadorCancha);
        model.addAttribute("cuentaGimnasio", contadorGimnasio);
        model.addAttribute("cuentaPiscina", contadorPiscina);
        model.addAttribute("cuentaQuincho", contadorQuincho);
        model.addAttribute("cuentaEstadio", contadorEstadio);
        model.addAttribute("cuentaSolicitudes", contadorSolicitudes);

        model.addAttribute("inicio", inicio);
        model.addAttribute("fin", fin);

        return "administrativo/ver-estadisticas";
    }
    @ModelAttribute("nombreUser")
    public String authName () {
        String rut = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.buscarPorRut(rut);

        return usuario.getNombre() + " " + usuario.getApellido();

    }

}
