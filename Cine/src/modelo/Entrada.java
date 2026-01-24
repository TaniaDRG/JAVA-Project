package modelo;

public class Entrada {
	
	private int idEntrada;
	private double precioSesion;
	private int descuento;
	private int numEntradas;
	
	
	
	public Entrada (int idEntrada, double precioSesion, int descuento, int numEntradas) {
		this.idEntrada = idEntrada;
		this.precioSesion = precioSesion;
		this.descuento = descuento;
		this.numEntradas = numEntradas;
	}
	
	
	
	
	
	
	public int getIdEntrada() {
		return idEntrada;
	}
	public void setIdEntrada(int idEntrada) {
		this.idEntrada = idEntrada;
	}
	public double getPrecioSesion() {
		return precioSesion;
	}
	public void setPrecioSesion(double precioSesion) {
		this.precioSesion = precioSesion;
	}
	public int getDescuento() {
		return descuento;
	}
	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}
	public int getNumEntradas() {
		return numEntradas;
	}
	public void setNumEntradas(int numEntradas) {
		this.numEntradas = numEntradas;
	}
	@Override
	public String toString() {
		return "Entrada \nIdEntrada:" + idEntrada + "\nPrecioSesion:" + precioSesion + "\nDescuento:" + descuento
				+ "\nNumEntradas:" + numEntradas;
	}
	
}


