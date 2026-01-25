package modelo;

public class Sala {
	
	private int idSala;
	private String nomSala;
	private Sesion sesion; //me traigo el obj sesion a este obj sala
	
	
	
	public Sala() {
	}


	public Sala(int idSala, String nomSal, Sesion session) {
		this.idSala = idSala;
		this.nomSala = nomSala;
		this.sesion = session;
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


	/**
	 * @return the sesion
	 */
	public Sesion getSesion() {
		return sesion;
	}


	/**
	 * @param sesion the sesion to set
	 */
	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
	}
	
	

}
