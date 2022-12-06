package cl.nessfit.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cl.nessfit.web.model.Fecha_Solicitud;
import cl.nessfit.web.service.IFecha_SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("administrativo/estadisticas")
public class estadisticaController {

    IFecha_SolicitudService fecha_solicitudService;

    @GetMapping("")
    public String estadistica (Model model, @RequestParam(name = "inicio", required = false, defaultValue = "1900-01-01") String inicio,  @RequestParam(name = "fin", required = false, defaultValue = "2999-01-01") String fin) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(inicio);
        Date finDate = formatter.parse(fin);
        int contadorCancha = 0;
        int contadorGimnasio = 0;
        int contadorPiscina = 0;
        int contadorQuincho = 0;
        int contadorEstadio = 0;

        List<Fecha_Solicitud> solicitudes = fecha_solicitudService.buscarPorRangoFecha(date, finDate);

        for (Fecha_Solicitud fecha_solicitud: solicitudes) {
            switch (fecha_solicitud.getSolicitud().getInstalacion().getTipo()) {
                case "Cancha":
                    contadorCancha++;
                    break;
                case "Gimnasio":
                    contadorGimnasio++;
                    break;
                case "Piscina":
                    contadorPiscina++;
                    break;
                case "Quincho":
                    contadorQuincho++;
                    break;
                case "Estadio":
                    contadorEstadio++;
                    break;
                default:
                    break;
            }
        }

        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("cuentaCancha", contadorCancha);
        model.addAttribute("cuentaGimnasio", contadorGimnasio);
        model.addAttribute("cuentaPiscina", contadorPiscina);
        model.addAttribute("cuentaQuincho", contadorQuincho);
        model.addAttribute("cuentaEstadio", contadorEstadio);

        model.addAttribute("inicio", inicio);
        model.addAttribute("fin", fin);

        return "administrativo/ver-estadisticas";

    }
}
