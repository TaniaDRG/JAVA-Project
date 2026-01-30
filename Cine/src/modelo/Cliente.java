package modelo;

public class Cliente {
	
	
	
	private String DNI;
	private String nomCliente;
	private String apellido;
	private String correo;
	private String contraseña;
	



	public Cliente(String DNI, String nomCliente, String apellido, String correo, String contraseña) {
		
		this.DNI = DNI;
		this.nomCliente = nomCliente;
		this.apellido = apellido;
		this.correo = correo;
		this.contraseña = contraseña;
	}




	public Cliente() {
		
	}




	public String getDNI() {
		return DNI;
	}




	public void setDNI(String DNI) {
		this.DNI = DNI;
	}




	public String getNomCliente() {
		return nomCliente;
	}




	public void setNomCliente(String nomCliente) {
		this.nomCliente = nomCliente;
	}




	public String getApellido() {
		return apellido;
	}




	public void setApellido(String apellido) {
		this.apellido = apellido;
	}




	public String getCorreo() {
		return correo;
	}




	public void setCorreo(String correo) {
		this.correo = correo;
	}




	public String getContraseña() {
		return contraseña;
	}




	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}




	@Override
	public String toString() {
		return "Cliente \nDNI:" + DNI + "\nNombre:" + nomCliente + "\nApellido:" + apellido + "\nCorreo:" + correo
				+ "\nContraseña:" + contraseña;
	}
	
	
	
	
}
