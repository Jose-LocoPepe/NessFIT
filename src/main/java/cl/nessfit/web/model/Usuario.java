package cl.nessfit.web.model;

import java.io.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

/**
 * Clase del modelo de usuarios
 *
 * @author BPCS Corp
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable{
	/**
	 * Serial unico para la conexión entre la clase y la tabla
	 */
	private static final long serialVersionUID = -3699177985262131693L;
	/**
	 * Id del Usuario
	 */
	@Id
	private String rut;
	/**
	 * Apellido del usuario
	 */
	private String apellido;
	/**
	 * Constraseña del usuario
	 */
	private String contrasena;
	/**
	 * Email del usuario
	 */
	@Email
	private String email;
	/**
	 * Estado si esta habilitado o deshabilitado
	 */
	private int estado;
	/**
	 * Nombre del usuario
	 */
	private String nombre;
	/**
	 * Telefono del usuario
	 */
	private String telefono;

	/**
	 * Rol asignado al usuario
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_rol", referencedColumnName = "id")
	private Rol rol;

	/**
	 * Getters y setters
	 */
	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}