package cl.nessfit.web.controller;

import cl.nessfit.web.model.Solicitud;
import cl.nessfit.web.model.Usuario;
import cl.nessfit.web.service.IInstalacionService;
import cl.nessfit.web.service.ISolicitudService;
import cl.nessfit.web.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/cliente/solicitudes")
public class GestionArriendoController {

    @Autowired
    ISolicitudService solicitudService;

    @Autowired
    IInstalacionService instalacionService;

    @Autowired
    IUsuarioService usuarioService;


    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("rentas", solicitudService
                .verSolicitudesPorUsuario(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "cliente/gestion-rentas";
    }

    @GetMapping("/crear")
    public String formSolicitud(Solicitud renta, Model model,
                                @RequestParam(name = "instalacion", required = false) String instalacion) {

        if (instalacion == null) {
            model.addAttribute("canchas", instalacionService.verInstalacionesPorTipo("cancha"));
            model.addAttribute("gimnasios", instalacionService.verInstalacionesPorTipo("gimnasio"));
            model.addAttribute("piscinas", instalacionService.verInstalacionesPorTipo("piscina"));
            model.addAttribute("quinchos", instalacionService.verInstalacionesPorTipo("quincho"));
            model.addAttribute("estadios", instalacionService.verInstalacionesPorTipo("estadio"));
            return "cliente/form-crear-solicitud";
        }

        if (instalacionService.buscarPorNombre(instalacion) == null
                && solicitudService.buscarPorInstalacionAndEstado(instalacion, 0) == null) {
            model.addAttribute("canchas", instalacionService.verInstalacionesPorTipo("cancha"));
            model.addAttribute("gimnasios", instalacionService.verInstalacionesPorTipo("gimnasio"));
            model.addAttribute("piscinas", instalacionService.verInstalacionesPorTipo("piscina"));
            model.addAttribute("quinchos", instalacionService.verInstalacionesPorTipo("quincho"));
            model.addAttribute("estadios", instalacionService.verInstalacionesPorTipo("estadio"));
            return "cliente/form-crear-solicitud";
        }
        //formato de fecha dd-MM-yyyy HH:mm:ss

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String fecha = formatter.format(date);
        try {
            renta.setFechaEmision(formatter.parse(fecha));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        renta.setInstalacion(instalacionService.buscarPorNombre(instalacion));
        Usuario usuario = usuarioService.buscarPorRut(SecurityContextHolder.getContext().getAuthentication().getName());
        renta.setUsuario(usuario);
        renta.setEstado(0);
        model.addAttribute("rentas", solicitudService.buscarPorInstalacionAndEstado(instalacion, 1));
        model.addAttribute("renta", renta);
        return "cliente/form-crear-solicitud-2";

    }

    @PostMapping("/crear")
    public String formCrearSolicitud(Solicitud solicitud, @RequestParam(name = "dias", required = false) String dias)
            throws ParseException {

        String[] diasArr = dias.split(",");
        List<Date> diasSolicitud = new ArrayList<Date>();
        for (String dia : diasArr) {
            System.out.println(dia);
            diasSolicitud.add(new SimpleDateFormat("dd-MM-yyyy").parse(dia));
        }
        solicitud.setMonto(diasSolicitud.size() * solicitud.getInstalacion().getCosto());
        solicitud.setDiasSolicitud(diasSolicitud);

        //se le asigna un id aleatorio
        // Conjunto de id's ya usados
        Set<Integer> alreadyUsedIds = new HashSet<>();

        // ID aleatorio entre 1 y 1000000, excluido el 1000000.
        Random r = new Random();
        int randomId = r.nextInt(1000000)+1;
        while(alreadyUsedIds.contains(randomId)){
            randomId = r.nextInt(1000000)+1;
        }
        // Si no lo hemos usado ya, lo usamos y lo metemos en el conjunto de usados.
        if (!alreadyUsedIds.contains(randomId)){
            alreadyUsedIds.add(randomId);
        }
        //se transforma el id y se asigna
        String id = String.valueOf(randomId);
        solicitud.setId(id);
        solicitudService.guardar(solicitud);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String detalleSolicitud(@PathVariable("id") int id, Model model) {
        String idString= Integer.toString(id);
        model.addAttribute("solicitud", solicitudService.buscarPorId(idString));
        //model.addAttribute("renta", renta);
        System.out.println("PRUEBA DE SOLICITUD:  "+solicitudService.buscarPorId(idString).toString());
        return "cliente/ver-solicitud";
    }

    @GetMapping("/pdf-print")
    public String pdfPrint(Model model) {
        model.addAttribute("rentas",
                solicitudService.verSolicitudesPorUsuario(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "solicitudes/pdf";
    }

    @ModelAttribute("nombreUser")
    public String authName() {
        String rut = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.buscarPorRut(rut);

        return "  " +usuario.getNombre() + " " + usuario.getApellido();

    }
}
