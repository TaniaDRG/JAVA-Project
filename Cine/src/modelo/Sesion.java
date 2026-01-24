package modelo;

public class Sesion {
	
	
	private String idSesion;
	private String fecha;
	private String horaInicio;
	private String horaFin;
	private double precio;
	private int numEspctTotal;
	
	public Sesion(String idSesion, String fecha, String horaInicio, String horaFin, double precio, int numEspctTotal) {
		
		this.idSesion = idSesion;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.precio = precio;
		this.numEspctTotal = numEspctTotal;
	}

	public String getIdSesion() {
		return idSesion;
	}

	public void setIdSesion(String idSesion) {
		this.idSesion = idSesion;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getNumEspctTotal() {
		return numEspctTotal;
	}

	public void setNumEspctTotal(int numEspctTotal) {
		this.numEspctTotal = numEspctTotal;
	}

	@Override
	public String toString() {
		return "Sesion \nIdSesion:" + idSesion + "\nFecha:" + fecha + "\nHoraInicio:" + horaInicio + "\nHoraFin:"
				+ horaFin + "\nPrecio:" + precio + "\nNumEspectTotal:" + numEspctTotal;
	}
	
	

	
}
