package cl.nessfit.web.controller;

import javax.validation.Valid;

import cl.nessfit.web.model.Instalacion;
import cl.nessfit.web.service.IInstalacionService;
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

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Controlador de la vista de Gestión de Complejos deportivos
 *
 * @author BPCS Corporation
 */
@Controller
@RequestMapping("/administrativo/gestion-complejo")
public class GestionComplejoController {

    /**
     * Inyección del servicio de complejos
     */
    @Autowired
    private IInstalacionService instalacionService;

    /**
     * Inyección del servicio de usuario
     */
    @Autowired
    private IUsuarioService usuarioService;

    /**
     * Maneja la petición GET para la volver al menu
     *
     * @return Menu principal
     */
    @GetMapping({"/index", "/menu"})
    public String menu() {
        return "menu";
    }

    /**
     * Maneja la petición GET para la vista de gestión de complejos
     *
     * @param model modelo
     * @return vista de gestión de clientes
     */
    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("instalaciones", instalacionService.verTodasIntalaciones());
        return "/administrativo/gestion-complejo";
    }

    /**
     * Maneja la petición GET para la vista de edicion de complejos
     *
     * @param nombre nombre de la peticion
     * @param model  modelo
     * @return vista del menu de edicion de complejo
     */
    @GetMapping("/editar/{nombre}")
    public String formEditar(@PathVariable(value = "nombre") String nombre, Model model) {
        Instalacion instalacion = instalacionService.buscarPorNombre(nombre);
        model.addAttribute("instalacion", instalacion);
        return "/administrativo/form-editar-complejo";
    }

    /**
     * Maneja la peticion POST para la vista de la edicion de complejos
     *
     * @param instalacion instalacion de la peticion
     * @param result      el resultado de la peticion
     * @param attr
     * @return vista del menu de edicion de clientes
     */
    @PostMapping("/editar/{nombre}")
    public String formEditarComplejo(@Valid Instalacion instalacion, BindingResult result, RedirectAttributes attr) {
        //Verificadores del nombre
        Instalacion nombreInstalacion = instalacionService.buscarPorNombre(instalacion.getNombre());
        if (nombreInstalacion != null) {
            if(!Objects.equals(nombreInstalacion.getNombre(), instalacion.getNombre())){
                result.rejectValue("nombre", null, "El nombre del complejo deportivo ya está en uso");
            }
        }
        if (instalacion.getNombre().length() < 3) {
            result.rejectValue("nombre", null, "El nombre debe tener al menos 3 caracteres");
        }
        if (instalacion.getDireccion().length() < 3) {
            result.rejectValue("direccion", null, "La direccion debe tener al menos 3 caracteres");
        }
        if (instalacion.getTipo().length() < 2 || instalacion.getTipo() == null) {
            result.rejectValue("tipo", null, "El complejo debe tener un tipo válido");
        }
        try {
            if (instalacion.getCosto() < 1000) {
                result.rejectValue("costo", null, "El costo debe ser mayor a 1000");
            }
        }
        catch (Exception e) {
            result.rejectValue("costo", null, "El costo debe ser completamente numerico");
        }
        //Si hay errores
        if (result.hasErrors()) {
            return "/administrativo/form-editar-complejo";
        }
        //Datos del complejo a editar
        if (instalacionService.buscarEstadoComplejo(instalacion.getNombre()) == 1) {
            instalacion.setEstado(1);
        } else {
            instalacion.setEstado(0);
        }
        instalacion.setId(nombreInstalacion.getId());

        //Guarda el cliente
        instalacionService.guardar(instalacion);

        //Redirecciona a la vista de gestion de clientes
        return "redirect:/administrativo/gestion-complejo";
    }

    @GetMapping("/estado-complejos/{nombre}")
    public String formEstado(@PathVariable(value = "nombre") String nombre, Model model) {
        Instalacion instalacion = instalacionService.buscarPorNombre(nombre);
        model.addAttribute("instalacion", instalacion);
        return "/administrativo/estado-complejos";
    }

    /**
     * Maneja la peticion POST para la vista de la edicion de instalaciones
     *
     * @param instalacion instalacion de la peticion
     * @param result      el resultado de la peticion
     * @param attr
     * @return vista del menu de edicion de instalaciones
     */

    @PostMapping("/estado-complejos/{nombre}")
    public String formEstadoComplejo(@Valid Instalacion instalacion, BindingResult result, RedirectAttributes attr) {


        instalacion.setNombre(instalacion.getNombre());
        instalacion.setDireccion(instalacion.getDireccion());
        instalacion.setTipo(instalacion.getTipo());
        instalacion.setCosto(instalacion.getCosto());
        if (instalacionService.buscarEstadoComplejo(instalacion.getNombre()) == 1) {
            instalacion.setEstado(0);
        } else {
            instalacion.setEstado(1);
        }
        Instalacion instalacion1 = instalacionService.buscarPorNombre(instalacion.getNombre());
        instalacion.setId(instalacion1.getId());

        // Paso 3.- Persistencia
        instalacionService.guardar(instalacion);

        // Paso 4.- Redireccion
        return "redirect:/administrativo/gestion-complejo";
    }

    /**
     * Maneja la peticion GET para crear una instalacion
     *
     * @return retorna la vista para crear instalaciones
     */
    @GetMapping("/crear")
    public String formComplejo(Instalacion instalacion) {
        return "/administrativo/form-crear-complejo";
    }

    /**
     * Maneja la peticion POST para crear un usuario
     *
     * @param instalacion instalacion de la peticion
     * @param result      el resultado de la peticion
     * @param attr        atributos
     * @return retorna la vista para crear clientes
     */


    //crear id random
    @PostMapping("/crear")
    public String formCrearComplejo(@Valid Instalacion instalacion, BindingResult result, RedirectAttributes attr) {
        //Verificadores del nombre
        Instalacion nombreInstalacion = instalacionService.buscarPorNombre(instalacion.getNombre());

            if (nombreInstalacion != null) {
            result.rejectValue("nombre", null, "El nombre del complejo deportivo ya está en uso");
            }
            //verificar si el costo es mayor a 1000
            try {
                if (instalacion.getCosto() < 1000) {
                    result.rejectValue("costo", null, "El costo debe ser mayor a 1000");
                }
            }
            catch (Exception e) {
                result.rejectValue("costo", null, "El costo debe ser completamente numerico");
            }
            //Verificador de campos rellenados
            if (instalacion.getNombre().length() < 3) {
                result.rejectValue("nombre", null, "El nombre debe tener al menos 3 caracteres");
            }
            if (instalacion.getDireccion().length() < 3) {
                result.rejectValue("direccion", null, "La direccion debe tener al menos 3 caracteres");
            }
            if (instalacion.getTipo().length() < 2 || instalacion.getTipo() == null) {
                result.rejectValue("tipo", null, "El tipo debe ser valido");
            }

            // Verifica si hay errores
            if (result.hasErrors()) {
                return "/administrativo/form-crear-complejo";
            }

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
            instalacion.setId(id);

            // Atributos del formulario
            instalacion.setEstado(1);

            // Guardado del usuario
            instalacionService.guardar(instalacion);

            // Redireccionamiento a la vista principal
            return "redirect:/administrativo/gestion-complejo";
        }


        /**
         * authName para buscar el nombre del rut logueado
         *
         * @return Nombre y apellido del usuario logueado
         */
        @ModelAttribute("nombreUser")
        public String authName () {
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
        public String auth () {
            //Usuario usuario =usuarioService.buscarPorRut(SecurityContextHolder.getContext().getAuthentication().getName());
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }

        //boolean para ver si hay numeros en un string
        public boolean hayNumeros (String s){
            for (int i = 0; i < s.length(); i++) {
                if (Character.isDigit(s.charAt(i))) {
                    return true;
                }
            }
            return false;
        }

    }

