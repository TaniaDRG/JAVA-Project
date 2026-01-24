package modelo;

public class Compra {

	
	private int idCompra;
	private int totalEntradas;
	private String fecha;
	private String hora;
	private double precioTotal;
	private double descuentoAplicado;
	
	
	
	
	public Compra(int idCompra, int totalEntradas, String fecha, String hora, double precioTotal, double descuentoAplicado) {
		this.idCompra = idCompra;
		this.totalEntradas = totalEntradas;
		this.fecha = fecha;
		this.hora = hora;
		this.precioTotal = precioTotal;
		this.descuentoAplicado = descuentoAplicado;
	}
	
	
	
	
	public int getIdCompra() {
		return idCompra;
	}
	
	public void setIdCompra(int idCompra) {
		this.idCompra = idCompra;
	}
	
	public int getTotalEntradas() {
		return totalEntradas;
	}
	
	public void setTotalEntradas(int totalEntradas) {
		this.totalEntradas = totalEntradas;
	}
	
	public String getFecha() {
		return fecha;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public String getHora() {
		return hora;
	}
	
	public void setHora(String hora) {
		this.hora = hora;
	}
	
	public double getPrecioTotal() {
		return precioTotal;
	}
	
	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
	}
	
	public double getDescuentoAplicado() {
		return descuentoAplicado;
	}
	
	public void setDescuentoAplicado(double descuentoAplicado) {
		this.descuentoAplicado = descuentoAplicado;
	}
	
	@Override
	public String toString() {
		return "Compra \nIdCompra:" + idCompra + "\nTotalEntradas:" + totalEntradas + "\nFecha:" + fecha + "\nHora:" + hora + "\nPrecioTotal:" + precioTotal +
				"\nDescuentoAplicado:" + descuentoAplicado;
	}

}
