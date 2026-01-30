package modelo;

public class Carrito {

	private Cliente cliente;
	private Compra compra;
	private Entrada entrada;
	private Sesion sesion;

	public Carrito(Cliente cliente, Compra compra, Entrada entrada, Sesion sesion) {
		this.cliente = cliente;
		this.compra = compra;
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
	
	public Compra getCompra() {
		return compra;
	}
	
	public void setCompra(Compra compra) {
		this.compra = compra;
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
	
	
}
