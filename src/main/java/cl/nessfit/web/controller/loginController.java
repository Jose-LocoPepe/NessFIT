package cl.nessfit.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Controlador de la vista de Login
 *
 * @author BPCS Corporation
 */
@Controller
public class loginController {
    /**
     * Maneja la peticion GET para la vista del login
     * @return vista del login
     */
    @GetMapping("/login")
    public String login() {
	    return "login";
    }

}
