package modelo;

public class Carrito {

	private Cliente cliente;
	private Entrada entrada;
	private Sesion sesion;

	public Carrito(Cliente cliente, Entrada entrada, Sesion sesion) {
		this.cliente = cliente;
		this.entrada = entrada;
		this.sesion = sesion;
	}

	public Carrito() {
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public Entrada getEntrada() {
		return entrada;
	}
	
	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}
	
	
	public Sesion getSesion() {
		return sesion;
	}
	
	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
	}

	//@Override
	//public String toString() {
		//return "Carrito= cliente=" + cliente + ", compra=" + compra + ", entrada=" + entrada
				//+ ", sesion=" + sesion;
	//}
	

	/*Mi push desde vs code */
	
}
