package modelo;

public class Pelicula {
	
	private int idPeli;
	private String nomPeli;
	private String genero;
	private int duracion;
	private double precio;
	
	
	public Pelicula(int idPeli, String nomPeli, String genero, int duracion, double precio) {
		this.idPeli = idPeli;
		this.nomPeli = nomPeli;
		this.genero = genero;
		this.duracion = duracion;
		this.precio = precio;
	}


	public int getIdPeli() {
		return idPeli;
	}


	public void setIdPeli(int idPeli) {
		this.idPeli = idPeli;
	}


	public String getNomPeli() {
		return nomPeli;
	}


	public void setNomPeli(String nomPeli) {
		this.nomPeli = nomPeli;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public int getDuracion() {
		return duracion;
	}


	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}


	public double getPrecio() {
		return precio;
	}


	public void setPrecio(double precio) {
		this.precio = precio;
	}


	@Override
	public String toString() {
		return "Pelicula \nIdPeli:" + idPeli + "\nNomPeli:" + nomPeli + "\nGénero:" + genero + "\nDuracion:" + duracion
				+ "\nPrecio:" + precio;
	}
	
	

}
