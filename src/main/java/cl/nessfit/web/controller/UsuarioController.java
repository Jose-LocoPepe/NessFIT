package cl.nessfit.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cl.nessfit.web.model.Rol;
import cl.nessfit.web.model.Usuario;
import cl.nessfit.web.service.IUsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private IUsuarioService usuarioServicio;
	
	public String guardar() {
		Usuario usuario = new Usuario();
		usuario.setNombre("Pepe");
		usuario.setApellido("Prueba");
		usuario.setContrasena("pepe123");
		usuario.setEmail("babyotaku@gmail.com");
		usuario.setEstado(1);
		usuario.setRut("202939283");
		usuario.setTelefono("124142424");
		
		Rol rol = new Rol();
		rol.setId(1);
		
		usuario.setRol(rol);
		
		usuarioServicio.guardar(usuario);
		return "guardar";
	}
}
