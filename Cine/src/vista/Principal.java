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
		mostrarMenuPelis();
		controlador.cerrarConexion();
	}

	public static void mostrarMenuPelis() {
		System.out.println("Lista de peliculas: \n");

		ArrayList<Pelicula> listaPeliculasOrdenadas = controlador.buacarPeliculas();

		int cont = 1;

		for (Pelicula peli : listaPeliculasOrdenadas) {
			System.out.println(cont + " - " + peli.getNomPeli());
			cont++;
		}

		int numElegido = -1;
		do {
			System.out.println("Elija una pelicula: ");
			numElegido = controladorES.elegirOpcion(listaPeliculasOrdenadas);
		} while (numElegido == -1);

		mostrarFechasDisponiblesPeli(listaPeliculasOrdenadas.get(numElegido - 1));// Obj Pelicula (idPeli+NomPeli)

	}

	public static void mostrarFechasDisponiblesPeli(Pelicula peliElegida) {
		System.out.println("Las fechas disponibles para la pelicula (" + peliElegida.getNomPeli() + ") son:");

		String IdPeli = peliElegida.getIdPeli();
		ArrayList<Sesion> FechasPeliElegida = controlador.buscarFechasPeli(IdPeli);

		int cont = 1;

		for (Sesion fechas : FechasPeliElegida) {
			System.out.println(cont + " - " + fechas.getFecha());
			cont++;
		}

		int numElegido = -1;
		do {
			System.out.println("Elija una fecha: ");
			numElegido = controladorES.elegirOpcion(FechasPeliElegida);
		} while (numElegido == -1);

		String fechaElegida = FechasPeliElegida.get(numElegido - 1).getFecha();
		mostrarSesionDeEsaPeliYFecha(IdPeli, fechaElegida);

	}

	private static void mostrarSesionDeEsaPeliYFecha(String IdPeli, String fechaElegida) {

		System.out.println("\nSesiones disponibles: ");
		ArrayList<Sesion> sesionesDisponibles = controlador.buscarSesiones(IdPeli, fechaElegida);

		int cont = 1;

		for (Sesion sesion : sesionesDisponibles) {
			System.out.println(cont + " - FECHA: " + sesion.getFecha() + ", Horario: " + sesion.getHoraInicio()
					+ ", Sala: " + sesion.getSala().getIdSala() + ", Precio: "
					+ String.format("%.2f", sesion.getPrecio()) + "€");
			cont++;
		} // QUITAR FECHA DEL SYSO

		int numElegido = -1;
		do {
			System.out.println("Elija una sesion: ");
			numElegido = controladorES.elegirOpcion(sesionesDisponibles);
		} while (numElegido == -1);

		Sesion sesionFinal = sesionesDisponibles.get(numElegido - 1);
		elegirEspectadores(sesionFinal);

	}

	private static void elegirEspectadores(Sesion sesionFinal) {

		int numEspectElegido = -1;
		do {
			System.out.println("Elija el numero de espectadores (max. 5):");
			numEspectElegido = controladorES.numeroDeEspectadores();
		} while (numEspectElegido == -1);

		guardarDatosEnCarrito(sesionFinal, numEspectElegido);

	}

	public static void guardarDatosEnCarrito(Sesion sesionFinal, int numEspectElegido) {
		Carrito carrito = new Carrito();
		carrito.setSesion(sesionFinal);// guardo la fila/sesion selecionada en mi carrito

		Entrada entradaTemporal = new Entrada();
		entradaTemporal.setNumEntradas(numEspectElegido);

		carrito.setEntrada(entradaTemporal);

		carritoTemporal.add(carrito);
		mostrarSuEleccionDePelis(carritoTemporal);// ArrayList para almacenar todo lo elegido (Tipo: Carrito)
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
			mostrarMenuPelis();

		} else {
			calcularPorcentajeDescuento();

		}
	}

	public static void calcularPorcentajeDescuento() {

		int peliculasDistintas = 0;

		ArrayList<String> nombresPeliculas = new ArrayList<>();

		for (int i = 0; i < carritoTemporal.size(); i++) {
			String nombreActual = carritoTemporal.get(i).getSesion().getPeli().getNomPeli();

			boolean nombreEncontrado = false;

			// Comprobar si la Peli ya existe en arrayList
			for (int j = 0; j < nombresPeliculas.size(); j++) {
				if (nombresPeliculas.get(j).equals(nombreActual)) {
					nombreEncontrado = true;

				}
			}
			// Si no estaba contado, sumamos a la lista
			if (!nombreEncontrado) {
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
		aplicarDescuento(porcentajeDescuento);
	}

	/** Método para aplicar el descuento a cada entrada del carrito **/
	public static void aplicarDescuento(double porcentajeDescuento) {

		for (int i = 0; i < carritoTemporal.size(); i++) {

			double precio = carritoTemporal.get(i).getSesion().getPrecio();
			int entradas = carritoTemporal.get(i).getEntrada().getNumEntradas();

			double descuento = (precio * entradas) * porcentajeDescuento;
			descuento = Math.round(descuento * 100.0) / 100.0;

			carritoTemporal.get(i).getEntrada().setDescuento(descuento);// tabla entrada descuento ok
		}

		mostrarResumenDeCompra();
	}

	public static void mostrarResumenDeCompra() {

		System.out.println("\n===== RESUMEN DE COMPRA ======");

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
					+ carrito2.getSesion().getSala().getNomSala() + "\nPrecio por entrada:"
					+ String.format("%.2f", carrito2.getSesion().getPrecio()) + "€" + "\nNúmero de entradas:"
					+ carrito2.getEntrada().getNumEntradas() + "\nDescuento:"
					+ String.format("%.2f", carrito2.getEntrada().getDescuento()) + "€");
		}

		System.out.println("\nPrecio de la compra: " + String.format("%.2f", precioTotalCompra) + "€");
		System.out.println("Total descuento: " + String.format("%.2f", descuentoAplicado) + "€");

		double totalAPagar = precioTotalCompra - descuentoAplicado;
		System.out.println("Total a pagar: " + String.format("%.2f", totalAPagar) + "€");

		confirmarCompra();
	}

	public static void confirmarCompra() {

		int comprar;
		do {
			System.out.println("\nDesear confirmar la compra:");
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

	public static void mostrarMenuOpcionesDeLogin() {

		int opcion;

		do {
			System.out.println("\n======== LOGIN ========");
			System.out.println("1 - Inicie Sesión");
			System.out.println("2 - Acceder como invitado");
			System.out.println("3 - Registrarse");
			System.out.println("4 - Cancelar compra");
			opcion = controladorES.leerOpciones(1, 4);

		} while (opcion == -1);

		switch (opcion) {
		case 1:
			controladorES.recogerDatosUsuarioExistente();
			break;
		case 2:
			accedeComoInvitado();
			break;
		case 3:
			controladorES.recogerDatosNuevoUsuario();
			break;
		case 4:
			resetearCarrito();
			break;
		}
	}

	public static void iniciarSesion(String dniUsuario, String contraseñaUsuario) {

		ArrayList<Cliente> existeCliente = controlador.buscarClienteBD(dniUsuario, contraseñaUsuario);
		/* guardo el resultado de la busqueda del usuario en:existeCliente */

		if (existeCliente.isEmpty()) {
			System.out.println("DNI o contraseña incorrectos.");
			mostrarMenuOpcionesDeLogin();/// no se cómo lo devuelvo allá sin hacer esto
		} else {
			Cliente cliente = existeCliente.get(0);
			System.out.println("Bienvenida/o " + cliente.getNomCliente());
			finalizarCompra();
		}
	}

	public static void accedeComoInvitado() {

		String dniInvitado = "Invitado1";
		String contrasenaInvitado = "prisma";

		ArrayList<Cliente> existeCliente = controlador.buscarClienteBD(dniInvitado, contrasenaInvitado);

		// No se si poner esto porque el usuario Invitado siempre va a estar en la base
		// de datos y si entra en este IF no tengo a donde devolverlo/enviarlo
		if (existeCliente.isEmpty()) {
			System.out.println("No se pudo acceder como invitado.");

		} else {
			Cliente invitado = existeCliente.get(0);
			System.out.println("Acceso como invitado.");
			finalizarCompra();

		}
	}

	/**
	 * Metodo para crear un objeto cliente (los datos recogidos ya son correctos)
	 **/
	public static void registrarse(String dni, String nombre, String apellido, String correo, String contrasena) {

		Cliente nuevoCliente = new Cliente();
		nuevoCliente.setDNI(dni);
		nuevoCliente.setNomCliente(nombre);
		nuevoCliente.setApellido(apellido);
		nuevoCliente.setCorreo(correo);
		nuevoCliente.setContraseña(contrasena);

		// Guardar en BD
		boolean insertado = controlador.insertarCliente(nuevoCliente);

		if (insertado) {
			System.out.println("Usuario registrado correctamente.");
			finalizarCompra();
		} else {
			System.out.println("Error al registrar el usuario.");// Cuándo va a haber un error aquí?
			mostrarMenuOpcionesDeLogin();
		}
	}

	public static void resetearCarrito() {

		carritoTemporal.clear();
		System.out.println("Compra cancelada");
		mostrarMenuPelis();
	}

	private static void finalizarCompra() {

		System.out.println("Compra realizada con éxito");
		// guardarDatosEnBDCompra();
		// mostrarMenu();
	}



}
