package vista;

import java.util.ArrayList;

import controlador.ControladorFichero;
import modelo.Carrito;
import modelo.Compra;

public class PruebasTest {
	
	public static ArrayList<Carrito> carritoTemporal = new ArrayList<Carrito>();
	public static Compra compraFinal = new Compra();

	
	
	/**
	 * /**
	 * Método que calcula los datos necesarios a almacenar/insertar en la BD
	 * guardandolos en su correspondiente var./obj. carritotemporal: descuento y
	 * PrecioEntrada (tabla Entrada). compraFinal: descuentoAplicado y precioTotal
	 * (la tabla Compra).
	 *
	 * @param porcentajeDescuento
	 */
	public static void calcularDatosTabla(double porcentajeDescuento) {

		double descuentoAplicado = 0.0;
		double precioTotal = 0.0;

		for (int i = 0; i < carritoTemporal.size(); i++) {

			double precioSesion = carritoTemporal.get(i).getSesion().getPrecio();
			int entradas = carritoTemporal.get(i).getEntrada().getNumEspectadores();

			// Calculo/Guardo el precio de la tabla Entrada
			double precioEnt = precioSesion * entradas;
			carritoTemporal.get(i).getEntrada().setPrecioEntrada(precioEnt);

			// Calculo/Guardo el descuento de la tabla Entrada
			double descuento = precioEnt * porcentajeDescuento;
			descuento = Math.round(descuento * 100.0) / 100.0;
			carritoTemporal.get(i).getEntrada().setDescuento(descuento);

			descuentoAplicado += descuento;
			precioTotal += precioEnt;
		}

		compraFinal.setPrecioTotal(precioTotal);
		compraFinal.setDescuentoAplicado(descuentoAplicado);
		mostrarResumenDeCompra();
	}

	/**
	 * Muestra un resumen detallado de la compra realizada. Calcula y muestra el
	 * totalApagar tras aplicar descuentos.
	 */
	public static void mostrarResumenDeCompra() {

		System.out.println("\n===== RESUMEN DE COMPRA ======");

		for (Carrito carrito2 : carritoTemporal) {

			System.out.println("------------------------------");
			System.out.println("\nPelicula: " + carrito2.getSesion().getPeli().getNomPeli() + "\nFecha:"
					+ carrito2.getSesion().getFecha() + "\nHora:" + carrito2.getSesion().getHoraInicio() + "\nSala:"
					+ carrito2.getSesion().getSala().getNomSala() + "\nPrecio sesión:"
					+ String.format("%.2f", carrito2.getSesion().getPrecio()) + "€" + "\nNúmero de entradas:"
					+ carrito2.getEntrada().getNumEspectadores() + "\nDescuento:"
					+ String.format("%.2f", carrito2.getEntrada().getDescuento()) + "€");
		}

		System.out.println("\nPrecio de la compra: " + String.format("%.2f", compraFinal.getPrecioTotal()) + "€");
		System.out.println("Total descuento: " + String.format("%.2f", compraFinal.getDescuentoAplicado()) + "€");

		double totalAPagar = compraFinal.getPrecioTotal() - compraFinal.getDescuentoAplicado();
		System.out.println("Total a pagar: " + String.format("%.2f", totalAPagar) + "€");

	
	}
	


}
