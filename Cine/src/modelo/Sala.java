package modelo;

public class Sala {
	
	private int idSala;
	private String nomSala;
	
	
	public Sala(int idSala, String nomSala) {
		this.idSala = idSala;
		this.nomSala = nomSala;
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
	
	

}
