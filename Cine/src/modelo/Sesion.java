package modelo;

public class Sesion {
	
	
	private String idSesion;
	private String fecha;
	private String horaInicio;
	private String horaFin;
	private double precio;
	private Pelicula peli;
	private Sala sala;
	
	
	public Sesion(String idSesion, String fecha, String horaInicio, String horaFin, double precio, Pelicula peli, Sala sala) {
		
		this.idSesion = idSesion;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.precio = precio;
		this.peli = peli;
		this.sala = sala;
	}


	public Sesion() {
		
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


	public Pelicula getPeli() {
		return peli;
	}

	
	public void setPeli(Pelicula peli) {
		this.peli = peli;
	}
	
	
	public Sala getSala() {
		return sala;
	}



	public void setSala(Sala sala) {
		this.sala = sala;
	}


	@Override
	public String toString() {
		return "Sesion \nIdSesion:" + idSesion + "\nFecha:" + fecha + "\nHoraInicio:" + horaInicio + "\nHoraFin:"
				+ horaFin + "\nPrecio:" + precio;
	}

//prueba
	
}
