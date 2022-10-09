package cl.nessfit.web.model;

import java.io.*;
import javax.persistence.*;

/**
 * Clase del modelo del Rol
 *
 * @author BPCS Corp
 */
@Entity
@Table(name = "roles")
public class Rol implements Serializable{
	/**
	 * Serial unico para la conexión entre la clase y la tabla
	 */
	private static final long serialVersionUID = 1009373009646485430L;
	/**
	 * ID del Rol
	 */
	@Id
	private int id;
	/**
	 * Nombre del rol
	 */
	private String nombre;

	/**
	 * Getters And Setters
	 */
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
