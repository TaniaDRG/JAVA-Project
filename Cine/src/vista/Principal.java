package vista;

import java.util.ArrayList;

import controlador.controladorBD;
import controlador.controladorEntradaYSalida;
import modelo.Carrito;
import modelo.Cliente;
import modelo.Entrada;
import modelo.Pelicula;
import modelo.Sala;
import modelo.Sesion;

public class Principal {

	public static controladorBD controlador;
	public static controladorEntradaYSalida controladorES;
	private static ArrayList<Carrito> carritoTemporal = new ArrayList<Carrito>();

	public static void main(String[] args) {
		System.out.println("Bienvenido");
		controlador = new controladorBD("cine_daw");
		controladorES = new controladorEntradaYSalida();

		controlador.iniciarConexion();
		mostrarMenu();
		controlador.cerrarConexion();
	}

	public static void mostrarMenu() {
		System.out.println("Lista de peliculas: \n");

		String where = " WHERE (S.Fecha > CURDATE() OR (S.Fecha = CURDATE() AND S.HoraInicio > CURTIME()))";
		String groupBy = " P.NomPeli";
		String orderBy = " MIN(timestamp(S.Fecha, S.HoraInicio)) asc ";
		ArrayList<Sesion> listaPeliculasOrdenadas = controlador.buscarSesionesYPeliculas(where, groupBy, orderBy);

		int cont = 1;
		for (Sesion peliPorSesion : listaPeliculasOrdenadas) {
			System.out.println(cont + " - " + peliPorSesion.getPeli().getNomPeli());
			cont++;
		}

		int numElegido = 0;
		do {
			System.out.println("Elija una opcion: ");
			numElegido = controladorES.elegirOpcion(listaPeliculasOrdenadas);
		} while (numElegido == 0);

		mostrarSesionesDeUnaPeli(listaPeliculasOrdenadas.get(numElegido - 1).getPeli());

	}

	public static void mostrarSesionesDeUnaPeli(Pelicula pelicula) {
		System.out.println("Las fechas disponibles para la pelicula (" + pelicula.getNomPeli() + ") son:");
		String where = " WHERE P.IdPeli = '" + pelicula.getIdPeli() + "'";
		String groupBy = "S.Fecha";
		ArrayList<Sesion> listaSesionesDePeli = controlador.buscarSesionesYPeliculas(where, groupBy, null);
		int cont = 1;
		for (Sesion sesion : listaSesionesDePeli) {
			System.out.println(cont + " - " + sesion.getFecha());
			cont++;
		}

		int numElegido = 0;
		do {
			System.out.println("Elija una fecha: ");
			numElegido = controladorES.elegirOpcion(listaSesionesDePeli);
		} while (numElegido == 0);
		String fechaElegida = listaSesionesDePeli.get(numElegido - 1).getFecha();
		listaSesionesDePeli = controlador.buscarSesionesYPeliculas(where, null, null);

		mostrarSesion(listaSesionesDePeli, numElegido, fechaElegida);
	}

	private static void mostrarSesion(ArrayList<Sesion> listaSesionesDePeli, int sesionElegida, String fechaElegida) {

		ArrayList<Sala> listaSalas = controlador.buscarSalas(listaSesionesDePeli.get(sesionElegida - 1));
		int contList = 1;
		int i = 0;
		ArrayList<Sesion> horaElegida = new ArrayList<Sesion>();
		for (Sesion sesion : listaSesionesDePeli) {
			if (sesion.getFecha().equals(fechaElegida)) {
				System.out.println(contList + " - FECHA: " + sesion.getFecha() + " - Horario: " + sesion.getHoraInicio()
						+ " Sala: " + listaSalas.get(0).getNomSala() + " Precio: " + sesion.getPrecio());
				horaElegida.add(contList - 1, sesion);
				contList++;
			}
			i++;
		}

		int numElegido = 0;
		do {
			System.out.println("Elija una sesion: ");
			numElegido = controladorES.elegirOpcion(horaElegida);
		} while (numElegido == 0);

		elegirEspectadores(horaElegida.get(numElegido - 1));
	}

	private static void elegirEspectadores(Sesion sesionFinal) {

		int numEspectElegido = 0;
		do {
			System.out.println("Elija el numero de espectadores (max. 5):");
			numEspectElegido = controladorES.numeroDeEspectadores();
		} while (numEspectElegido == 0);

		guardarDatosEnCarrito(sesionFinal, numEspectElegido);

	}

	public static void guardarDatosEnCarrito(Sesion sesionFinal, int numEspectElegido) {
		Carrito carrito = new Carrito();
		carrito.setSesion(sesionFinal);// guardo la fila/sesion selecionada a mi carrito

		Entrada entradaTemporal = new Entrada();
		entradaTemporal.setNumEntradas(numEspectElegido);
		// entradaTemporal.getSesion().getSala().setNomSala();
		carrito.setEntrada(entradaTemporal);

		carritoTemporal.add(carrito);
		mostrarSuEleccionDePelis(carritoTemporal);// ArrayList para almacenar lo elegido (Tipo: Carrito)
	}

	public static void mostrarSuEleccionDePelis(ArrayList<Carrito> carritoTemporal) {
		System.out.println("Tu Carrito:\n");

		for (Carrito carrito2 : carritoTemporal) {

			System.out.println("----------------------------------------");
			System.out.println("\nPelicula: " + carrito2.getSesion().getPeli().getNomPeli() + "\nFecha:"
					+ carrito2.getSesion().getFecha() + "\nHora Inicio:" + carrito2.getSesion().getHoraInicio()
					+ "\nSala:" + carrito2.getSesion().getSala().getNomSala() + "\nPrecio:"
					+ String.format("%.2f", carrito2.getSesion().getPrecio()) + "€" + "\nNúmero de entradas:"
					+ carrito2.getEntrada().getNumEntradas());
		}
		SeguirComprando();
	}

	public static void SeguirComprando() {

		int comprarMás;
		do {

			System.out.println("\nQuiere comprar más sesiones: ");
			System.out.println("1. Sí");
			System.out.println("2. No");

			comprarMás = controladorES.leerOpciones(1, 2);

		} while (comprarMás != 1 && comprarMás != 2);

		if (comprarMás == 1) {
			mostrarMenu();

		} else {
			calcularDescuento();

		}
	}

	public static void calcularDescuento() {

		int peliculasDistintas = 0;
		ArrayList<String> nombresPeliculas = new ArrayList<>();

		for (int i = 0; i < carritoTemporal.size(); i++) {
			String nombreActual = carritoTemporal.get(i).getSesion().getPeli().getNomPeli();

			boolean yaContado = false;

			// Comprobar si la Peli ya existe en arrayList
			for (int j = 0; j < nombresPeliculas.size(); j++) {
				if (nombresPeliculas.get(j).equals(nombreActual)) {
					yaContado = true;

				}
			}
			// Si no estaba contado, sumamos a la lista
			if (!yaContado) {
				nombresPeliculas.add(nombreActual);
				peliculasDistintas++;
			}
		}

		// Descuentos
		double porcentajeDescuento = 0.0;

		if (peliculasDistintas == 2) {
			porcentajeDescuento = 0.20;
		} else if (peliculasDistintas > 2) {
			porcentajeDescuento = 0.30;
		}

		// Calculo descuento para cada carrito
		for (int i = 0; i < carritoTemporal.size(); i++) {

			double precio = carritoTemporal.get(i).getSesion().getPrecio();
			int entradas = carritoTemporal.get(i).getEntrada().getNumEntradas();

			double descuento = (precio * entradas) * porcentajeDescuento;
			descuento = Math.round(descuento * 100.0) / 100.0;

			carritoTemporal.get(i).getEntrada().setDescuento(descuento);
		}

		mostrarResumenDeCompra();
	}

	public static void mostrarResumenDeCompra() {

		System.out.println("\n=====RESUMEN DE COMPRA======: ");

		double precioTotalCompra = 0.0;
		double descuentoAplicado = 0.0;

		for (Carrito carrito2 : carritoTemporal) {

			double precio = carrito2.getSesion().getPrecio();
			int entradas = carrito2.getEntrada().getNumEntradas();
			double descuento = carrito2.getEntrada().getDescuento();

			precioTotalCompra += precio * entradas;
			descuentoAplicado += descuento;

			System.out.println("------------------------------\nPelicula: "
					+ carrito2.getSesion().getPeli().getNomPeli() + "\nFecha:" + carrito2.getSesion().getFecha()
					+ "\nHora:" + carrito2.getSesion().getHoraInicio() + "\nSala:"
					+ carrito2.getSesion().getSala().getNomSala() + "\nPrecio:"
					+ String.format("%.2f", carrito2.getSesion().getPrecio()) + "€" + "\nNúmero de entradas:"
					+ carrito2.getEntrada().getNumEntradas() + "\nDescuento:"
					+ String.format("%.2f", carrito2.getEntrada().getDescuento()) + "€");
		}

		System.out.println("\nPrecio de la compra: " + String.format("%.2f", precioTotalCompra) + "€");
		System.out.println("Descuento: " + String.format("%.2f", descuentoAplicado) + "€");

		double totalAPagar = precioTotalCompra - descuentoAplicado;
		System.out.println("\nTotal a pagar: " + String.format("%.2f", totalAPagar) + "€");
		
		confirmarCompra();
	}

	public static void confirmarCompra() {

		int comprar;
		do {
			System.out.println("Desear realizar la compra:");
			System.out.println("1. Si");
			System.out.println("2. No");
			comprar = controladorES.leerOpciones(1, 2);

		} while (comprar != 1 && comprar != 2);

		if (comprar == 1) {
			mostrarMenuOpcionesDeLogin();

		} else {
			resetearCarrito();

		}

	}

	private static void mostrarMenuOpcionesDeLogin() {
		int opcion;
		do {
			System.out.println("1 - Inicie Sesión");
			System.out.println("2 - Acceder como invitado");
			System.out.println("3 - Registrarse");
			System.out.println("4 - Cancelar compra");
			opcion = controladorES.leerOpciones(1, 4);

		} while (opcion != 1 && opcion != 2 && opcion != 3 && opcion != 4);

		switch (opcion) {
		case 1:
			controladorES.recogerDatosUsuario();
			break;
		case 2:
			break;
		case 3:

			break;
		case 4:
			resetearCarrito();
			break;
		}
	}
	
	public static void iniciarSesion(String dniUsuario,String contraseñaUsuario) {
		
		ArrayList<Cliente> existeCliente = controlador.buscarClienteBD(dniUsuario, contraseñaUsuario);
		
		for (Cliente cliente1 : existeCliente) {
			
			if (cliente1.getDNI()!= null) {
			System.out.println("Bienvenido, " + " - " + cliente1.getNomCliente());
		
		 
		    } else {
		        System.out.println("DNI o contraseña incorrectos.");
		    }
		}
	}
	
		
		
	public static void resetearCarrito() {
		// TODO Auto-generated method stub

	}

}
