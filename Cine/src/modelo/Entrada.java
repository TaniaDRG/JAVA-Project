package modelo;

public class Entrada {
	
	private int idEntrada;
	private double precioEntrada;
	private double descuento;
	private int numEspectadores;
	private Sesion sesion;
	
	
	
	public Entrada (int idEntrada, double precioEntrada, double descuento, int numEspectadores, Sesion sesion) {
		this.idEntrada = idEntrada;
		this.precioEntrada = precioEntrada;
		this.descuento = descuento;
		this.numEspectadores = numEspectadores;
		this.sesion = sesion;
	}
	
	
	
	public Entrada() {	
	}


	public int getIdEntrada() {
		return idEntrada;
	}
	public void setIdEntrada(int idEntrada) {
		this.idEntrada = idEntrada;
	}
	public double getPrecioSesion() {
		return precioEntrada;
	}
	public void setPrecioSesion(double precioEntrada) {
		this.precioEntrada = precioEntrada;
	}
	public double getDescuento() {
		return descuento;
	}
	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}
	public int getNumEntradas() {
		return numEspectadores;
	}
	public void setNumEntradas(int numEspectadores) {
		this.numEspectadores = numEspectadores;
	}
	
	public Sesion getSesion() {
		return sesion;
	}



	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
	}
	
	
	@Override
	public String toString() {
		return "Entrada \nIdEntrada:" + idEntrada + "\nPrecioSesion:" + precioEntrada + "\nDescuento:" + descuento
				+ "\nNumEntradas:" + numEspectadores;
	}


	

	
}


