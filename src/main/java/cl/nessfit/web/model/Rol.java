package cl.nessfit.web.model;

import java.io.*;
import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Rol implements Serializable{
	
	private static final long serialVersionUID = 1009373009646485430L;
	@Id
	private int id;
	private String nombre;
	
	@OneToOne(mappedBy = "rol")
	private Usuario usuario;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}