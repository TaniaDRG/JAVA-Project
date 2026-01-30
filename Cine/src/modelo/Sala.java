package modelo;

public class Sala {
	
	private int idSala;
	private String nomSala;
	private Sesion sesion; //No lo estoy usando.. REVISAR
	
	
	
	public Sala() {
	}


	public Sala(int idSala, String nomSala, Sesion sesion) {
		this.idSala = idSala;
		this.nomSala = nomSala;
		this.sesion = sesion;
	}


	public int getIdSala() {
		return idSala;
	}


	public void setIdSala(int idSala) {
		this.idSala = idSala;
	}


	public String getNomSala() {
		return nomSala;
	}


	public void setNomSala(String nomSala) {
		this.nomSala = nomSala;
	}


	@Override
	public String toString() {
		return "Sala \nIdSala:" + idSala + "\nNomSala:" + nomSala;
	}


	
	public Sesion getSesion() {
		return sesion;
	}


	
	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
	}
	
	

}
