package vista;

import java.util.ArrayList;

import controlador.ControladorBD;
import controlador.ControladorEntradaYSalida;
import controlador.ControladorFichero;
import modelo.Carrito;
import modelo.Cliente;
import modelo.Entrada;
import modelo.Pelicula;
import modelo.Sala;
import modelo.Sesion;
import modelo.Compra;

/**
 * 
 */
public class Principal {

	public static ControladorBD controlador;
	public static ControladorEntradaYSalida controladorES;
	private static ArrayList<Carrito> carritoTemporal = new ArrayList<Carrito>();
	private static Compra compraFinal = new Compra();

	/**
	 * Empieza el programa, inicializa los controladores de BD y de entrada/salida,
	 * se conecta y desconecta con la BD, muestra el menú de películas
	 **/
	public static void iniciar() {
		System.out.println("*******************");
		System.out.println("    BIENVENIDO  ");
		System.out.println("*******************");

		controlador = new ControladorBD("cine_daw");
		controladorES = new ControladorEntradaYSalida();

		controlador.iniciarConexion();
		mostrarMenuPelis();
		controlador.cerrarConexion();
	}

	/**
	 * Muestra el menú de películas disponibles según lo obtenido de la consulta con
	 * la BD, solicita al usuario que elija una película y valida la opción
	 * seleccionada; Si la elección es válida, dirige al método:
	 * mostrarFechasDisponiblesPeli enviando como parámetro el objeto/fila Pelicula
	 * elegida, sino entra en bucle.
	 **/
	public static void mostrarMenuPelis() {

		ArrayList<Pelicula> listaPeliculasOrdenadas = controlador.buscarPeliculas();

		int cont = 1;
		int numElegido = -1;

		do {
			System.out.println("\nLista de peliculas: \n");

			for (Pelicula peli : listaPeliculasOrdenadas) {
				System.out.println(cont + " - " + peli.getNomPeli());
				cont++;
			}

			System.out.println("Elija una pelicula: ");
			numElegido = controladorES.elegirOpcion(listaPeliculasOrdenadas);
			if (numElegido == -1) {
				cont = 1;
			}
		} while (numElegido == -1);

		mostrarFechasDisponiblesPeli(listaPeliculasOrdenadas.get(numElegido - 1));// Obj Pelicula (idPeli+NomPeli)

	}

	/**
	 * Muestra las fechas disponibles de una película específica (recibidas del mét.
	 * buscarFechasPeli) y permite al usuario seleccionar una fecha. Si el usuario
	 * introduce un valor no valido, vuelve al menú de películas; sino va al método
	 * mostrarSesionDeEsaPeliYFecha().
	 * 
	 * @param peliElegida = Objeto Pelicula seleccionado.
	 */
	public static void mostrarFechasDisponiblesPeli(Pelicula peliElegida) {

		System.out.println("Las fechas disponibles para la película (" + peliElegida.getNomPeli() + ") son:");

		String IdPeli = peliElegida.getIdPeli();
		ArrayList<Sesion> FechasPeliElegida = controlador.buscarFechasPeli(IdPeli);

		int cont = 1;

		for (Sesion fechas : FechasPeliElegida) {
			System.out.println(cont + " - " + fechas.getFecha());
			cont++;
		}

		int numElegido = -1;

		System.out.println("Elija una fecha: ");
		numElegido = controladorES.elegirOpcion(FechasPeliElegida);

		if (numElegido == -1) {
			mostrarMenuPelis();
		}

		String fechaElegida = FechasPeliElegida.get(numElegido - 1).getFecha();
		mostrarSesionDeEsaPeliYFecha(IdPeli, fechaElegida);

	}

	/**
	 * Muestra todas las sesiones disponibles de una película en una fecha
	 * específica (recibidas del mét. buscarSesiones) y permite al usuario
	 * seleccionar una sesion. Si el usuario introduce un valor no valido, vuelve al
	 * menú de películas; sino va al método elegirEspectadores.
	 * 
	 * @param IdPeli
	 * @param fechaElegida
	 */
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

		System.out.println("Elija una sesion: ");
		numElegido = controladorES.elegirOpcion(sesionesDisponibles);
		if (numElegido == -1) {
			mostrarMenuPelis();
		}

		Sesion sesionFinal = sesionesDisponibles.get(numElegido - 1);
		elegirEspectadores(sesionFinal);

	}

	/**
	 * Solicita el número de espectadores, valida el número(numeroDeEspectadores) y
	 * va al mét. guardarDatosEnCarrito. Si número no valido cae en bucle.
	 * 
	 * @param sesionFinal= objeto Sesion (sesion elejida-tiene todos los datos
	 *                     necesitados)
	 */
	private static void elegirEspectadores(Sesion sesionFinal) {

		int numEspectElegido = -1;
		do {
			System.out.println("Elija el numero de espectadores (máx. 5):");
			numEspectElegido = controladorES.numeroDeEspectadores();
		} while (numEspectElegido == -1);

		guardarDatosEnCarrito(sesionFinal, numEspectElegido);

	}

	public static void guardarDatosEnCarrito(Sesion sesionFinal, int numEspectElegido) {
		Carrito carrito = new Carrito();
		carrito.setSesion(sesionFinal);// guardo la fila/sesion selecionada en mi carrito(tiene todos los atrib. de
										// buscarSesiones())

		Entrada entradaTemporal = new Entrada();
		entradaTemporal.setNumEspectadores(numEspectElegido);

		carrito.setEntrada(entradaTemporal);

		carritoTemporal.add(carrito);// aqui guardo la entrada realizada/ lo elegido en mi lista
		mostrarSuEleccionDePelis(carritoTemporal);// ArrayList para almacenar todo lo elegido (Tipo: Carrito)
	}

	public static void mostrarSuEleccionDePelis(ArrayList<Carrito> carritoTemporal) {
		System.out.println("\nTu Carrito:");

		for (Carrito carrito2 : carritoTemporal) {

			System.out.println("----------------------------------------");
			System.out.println("\nPelicula: " + carrito2.getSesion().getPeli().getNomPeli() + "\nFecha:"
					+ carrito2.getSesion().getFecha() + "\nHora Inicio:" + carrito2.getSesion().getHoraInicio()
					+ "\nSala:" + carrito2.getSesion().getSala().getNomSala() + "\nPrecio:"
					+ String.format("%.2f", carrito2.getSesion().getPrecio()) + "€" + "\nNúmero de entradas:"
					+ carrito2.getEntrada().getNumEspectadores());
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
		calcularDatosTabla(porcentajeDescuento);
	}

	/**
	 * Método que calcula los datos que necesito almacenar en la BD y los guardo en
	 * mi carritotemporal: descuento (tabla Entrada) y PrecioEntrada. También
	 * obtengo el descuentoAplicado y precioTotal para la tabla Compra
	 **/
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

	public static void mostrarResumenDeCompra() { // datos para tabla COMPRA

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

		confirmarCompra();
	}

	public static void confirmarCompra() {

		int comprar;
		do {
			System.out.println("\nDesear confirmar la compra:");
			System.out.println("1. Si");
			System.out.println("2. No");
			comprar = controladorES.leerOpciones(1, 2);

		} while (comprar == -1);

		if (comprar == 1) {
			mostrarMenuOpcionesDeLogin();

		} else {
			resetearCarrito();
			System.out.println("Compra cancelada");
			iniciar();
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
			System.out.println("Compra cancelada");
			iniciar();
			break;
		}

	}

	/**
	 * Método que recibe como parámetros:dniUsuario,contraseñaUsuario para buscarlos
	 * en la base de datos. Si existe guardo el obj cliente en el obj compraFinal,
	 * sino error y retorna a opciones de login
	 **/

	public static void iniciarSesion(String dniUsuario, String contraseñaUsuario) {

		Cliente existeCliente = controlador.buscarClienteBD(dniUsuario, contraseñaUsuario);
		/* guardo el resultado de la busqueda del usuario en obj existeCliente */

		if (existeCliente == null) {
			System.out.println("DNI o contraseña incorrectos.");
			mostrarMenuOpcionesDeLogin();
		} else {
			System.out.println("Bienvenida/o " + existeCliente.getNomCliente());

			compraFinal.setCliente(existeCliente);
			finalizarCompra();
		}
	}

	public static void accedeComoInvitado() {

		String dniInvitado = "Invitado1";
		String contrasenaInvitado = "prisma";

		Cliente existeCliente = controlador.buscarClienteBD(dniInvitado, contrasenaInvitado);

		// No se si poner esto porque el usuario Invitado siempre va a estar en la base
		// de datos y si entra en este IF no tengo a donde devolverlo/enviarlo
		if (existeCliente == null) {
			System.out.println("No se pudo acceder como invitado.");

		} else {
			System.out.println("Acceso como invitado.");

			compraFinal.setCliente(existeCliente);
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
			compraFinal.setCliente(nuevoCliente);
			finalizarCompra();
		} else {
			System.out.println("Error al registrar el usuario.");
			mostrarMenuOpcionesDeLogin();
		}
	}

	/**
	 * Método que borra todo lo almacenado en el array carritoTemporal(entradas
	 * realizadas) y en el objeto compraFinal
	 **/
	public static void resetearCarrito() {

		carritoTemporal.clear();
		compraFinal = new Compra();

	}

	private static void finalizarCompra() {

		guardarCompra();
		System.out.println("\nCompra realizada con éxito.");
		resetearCarrito();
		iniciar();

	}

	private static void guardarCompra() {

		boolean compraInsertada = controlador.guardarDatosEnBDCompra(compraFinal);
		if (compraInsertada) {
			System.out.println("Compra registrada correctamente.");
			guardarEntradasDeCompra();
		} else {
			System.out.println("Error al registrar la compra.");
			System.out.println("Por favor, vuelva a realizar la compra");
			resetearCarrito();
		}

	}

	private static void guardarEntradasDeCompra() {
		boolean entradaInsertada = controlador.guardarDatosEnBDEntrada(carritoTemporal, compraFinal);
		if (entradaInsertada) {
			System.out.println("Entrada registrada correctamente.");
			deseaTickect();

		} else {
			System.out.println("Error al registrar la entrada.");
			System.out.println("Por favor, vuelva a realizar la compra");
			resetearCarrito();
		}

	}

	private static void deseaTickect() {

		int ticket;
		do {
			System.out.println("¿ Desea ticket de compra ?");
			System.out.println("1. Si");
			System.out.println("2. No");
			ticket = controladorES.leerOpciones(1, 2);

		} while (ticket == -1);

		if (ticket == 1) {
			generarFichero();
			return;
		}
		return;
		// Al salir del método, el flujo continúa en finalizarCompra()
	}

	private static void generarFichero() {
		ControladorFichero fichero = new ControladorFichero("ficheros/");

		boolean generado = fichero.escribirCompra("compras.txt", carritoTemporal, compraFinal.getCliente(),
				compraFinal);

		if (generado) {
			System.out.println("Ticket de compra generado correctamente");
		} else {
			System.out.println("No se pudo generar el ticket de compra");
		}

	}

}
