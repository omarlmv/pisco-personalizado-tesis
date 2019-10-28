package com.tesis.microservice.pisco.model;

public class Cliente {
	
	private int idCliente;
    private String nombre;
    private String correo;
    private String contrasena;
    private String repetircontrasena;
    private String dni;
    private String sexo;
    private String gradoinstruccion;
    private Integer edad;
    private String resumen;
    
	public int getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public String getRepetircontrasena() {
		return repetircontrasena;
	}
	public void setRepetircontrasena(String repetircontrasena) {
		this.repetircontrasena = repetircontrasena;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getGradoinstruccion() {
		return gradoinstruccion;
	}
	public void setGradoinstruccion(String gradoinstruccion) {
		this.gradoinstruccion = gradoinstruccion;
	}
	public Integer getEdad() {
		return edad;
	}
	public void setEdad(Integer edad) {
		this.edad = edad;
	}
	public String getResumen() {
		return resumen;
	}
	public void setResumen(String resumen) {
		this.resumen = resumen;
	}
    
}
