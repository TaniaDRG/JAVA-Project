package controlador;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import modelo.Carrito;
import modelo.Cliente;
import modelo.Compra;
import modelo.Entrada;
import modelo.Pelicula;
import modelo.Sala;
import modelo.Sesion;

import vista.PruebasTest;

public class ControladorBDTest {

	private static ControladorBD controlador;

	/**
	 * Comprobamos que se genera correctamente el Objeto cuando le pasamos una BD
	 * existente
	 */
	@Test
	public void ControladorValido() {
		controlador = new ControladorBD("cine_daw");
		assertNotNull(controlador);// Obj. no es nulo
	}

	/**
	 * Comprobamos que se genera correctamente el Objeto cuando le pasamos una BD no
	 * existente
	 */
	@Test
	public void constructorNoValido() {
		controlador = new ControladorBD(null);
		assertNotNull(controlador); // Obj. no es nulo
	}

	/**
	 * Comprobamos que la conexion con la BD existente se ejecute correctamete
	 */
	@Test
	public void inicioDeConexionNormal() {
		controlador = new ControladorBD("cine_daw");
		boolean conexion = controlador.iniciarConexion();
		assertTrue(conexion);
		controlador.cerrarConexion();// Ojo: No olvidar
	}

	/**
	 * Comprobamos que la conexion con la una BD que NO existe falle
	 */
	@Test
	public void inicioDeConexionNoExistente() {
		controlador = new ControladorBD("cine");
		boolean conexion = controlador.iniciarConexion();
		assertFalse(conexion);
	}

	/**
	 * Comprobamos que se cierra la conexion con la BD
	 */
	@Test
	public void cerrarConexionNornal() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		assertTrue(controlador.cerrarConexion());
	}

	/**
	 * Comprobamos que NO se cierra la conexion con la BD enviando una conexion
	 * fallida por lo tanto la conexio será null
	 */
	@Test
	public void cerrarConexionFallida() {
		controlador = new ControladorBD("cine");
		controlador.iniciarConexion();
		assertFalse(controlador.cerrarConexion());
	}

	/**
	 * Comprobamos que no se cierran conexiones inexistentes
	 */
	@Test
	public void cerrarConexionNoExistente() {
		controlador = new ControladorBD("cine_daw");
		assertFalse(controlador.cerrarConexion());
	}

	/**
	 * Comprobamos que se obtiene una lista de películas (por lo menos una fila)
	 */
	@Test
	public void buscarPeliculasDevuelvePeliculas() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		ArrayList<Pelicula> peliculas = controlador.buscarPeliculas();
		assertFalse(peliculas.isEmpty());
		controlador.cerrarConexion();
	}

	/**
	 * Comprobamos que se obtiene lo esperado cuando hacemos la query que recoge
	 * todas las películas de la BD
	 */
	@Test
	public void obtenerPeliculasCorrectas() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		ArrayList<Pelicula> peliculas = controlador.buscarPeliculas();
		assertEquals("Se esperaban 6 películas", 6, peliculas.size());
		controlador.cerrarConexion();

	}

	/**
	 * Comprobamos que se obtiene una lista de fechas para un IdPeli existente
	 */
	@Test
	public void buscarFechasPeliDevuelveFechas() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		ArrayList<Sesion> fechas = controlador.buscarFechasPeli("MTRX");
		assertFalse(fechas.isEmpty());
		controlador.cerrarConexion();
	}

	/**
	 * Comprobamos que se obtiene una lista de fechas vacía para un IdPeli
	 * inexistente
	 */
	@Test
	public void buscarFechasPeliNoDevuelveFechas() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		ArrayList<Sesion> fechas = controlador.buscarFechasPeli("COCO");
		assertTrue(fechas.isEmpty());
		controlador.cerrarConexion();
	}

	/**
	 * Comprobamos que se obtiene una lista de sesiones para un IdPeli y fechaInicio
	 * existentes en la BD
	 */
	@Test
	public void buscarSesionesDevuelveSesiones() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		ArrayList<Sesion> sesiones = controlador.buscarSesiones("AVAT", "2026-02-07");
		assertFalse(sesiones.isEmpty());
		controlador.cerrarConexion();
	}

	/**
	 * Comprobamos que las sesiones que devuelve sean el mismo número de sesiones
	 * que hay para esa película y fecha en la BD
	 */
	@Test
	public void buscarSesionesCorrectas() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		ArrayList<Sesion> sesiones = controlador.buscarSesiones("AVAT", "2026-02-07");
		assertTrue(sesiones.size() == 3);
		controlador.cerrarConexion();
	}

	/**
	 * Comprobamos que el precio de las sesiones obtenidas para una película y fecha
	 * sea correcto según lo almacenado en BD
	 */
	@Test
	public void buscarSesionesPrecioCorrecto() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		ArrayList<Sesion> sesiones = controlador.buscarSesiones("GREY", "2026-02-08");

		for (Sesion s : sesiones) {
			assertEquals(8.50, s.getPrecio(), 0.01);
		}
		controlador.cerrarConexion();
	}

	/**
	 * Comprobamos que devuelve un objeto vacío según los parámetros inexistentes
	 * que enviamos a la BD
	 */
	@Test
	public void buscarClienteBDNoExiste() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		Cliente clienteBuscado = controlador.buscarClienteBD("XOXOXOXOX", "III");
		assertNull(clienteBuscado.getDNI());
		;
		controlador.cerrarConexion();
	}

	/**
	 * Comprobamos que se crea un nuevo objeto-cliente al enviar unos valores
	 * correctos a la BD
	 */
	@Test
	public void insertarClienteValido() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		Cliente nuevoCliente = new Cliente("98765432A", "Marvin", "Martian", "marciano@spam.com", "bipbip");
		assertTrue(controlador.insertarCliente(nuevoCliente));
		controlador.cerrarConexion();
	}// delete from cliente where DNI = '98765432A'
		// SELECT * FROM CLIENTE WHERE CORREO = 'marciano@spam.com'
		// delete from cliente where CORREO = 'marciano@spam.com'

	/**
	 * Comprobamos que NO se crea un nuevo objeto-cliente al enviar unos valores
	 * incorrectos a la BD (ya existe)
	 */
	@Test
	public void insertarClienteNoValido() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		Cliente nuevoCliente = new Cliente("98765432A", "Marvin", "Martian", "marciano@spam.com", "bipbip");
		assertFalse(controlador.insertarCliente(nuevoCliente));
		controlador.cerrarConexion();
	}

	/**
	 * Comprobamos que se inserte correctamente en la BD una compra cuyos valores
	 * son correctos y que el IdCompra generado sea distinto de 0.
	 */
	@Test
	public void GuardarCompraValida() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();

		Compra compraFinal = new Compra();
		Cliente clienteExistente = new Cliente();
		clienteExistente.setDNI("11122233D");
		compraFinal.setCliente(clienteExistente);
		compraFinal.setPrecioTotal(29.50);
		compraFinal.setDescuentoAplicado(5.90);

		assertTrue(controlador.guardarDatosEnBDCompra(compraFinal));
		assertNotEquals(0, compraFinal.getIdCompra());
		controlador.cerrarConexion();

	}

	/**
	 * Comprobamos que se inserte correctamente en la BD las entradas de una compra,
	 * cuyos valores son correctos (el IdCompra se corresponde con la compra
	 * insertada en el test anterior).
	 */
	@Test
	public void guardarEntradaValida() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		// ---Compra final---
		Compra compraFinal = new Compra();
		compraFinal.setPrecioTotal(29.50);
		compraFinal.setDescuentoAplicado(5.90);
		compraFinal.setIdCompra(86);// ojo tabla compra ult. fila

		ArrayList<Carrito> carritoTemporal = new ArrayList<>();
		// ------Primera Entrada
		Carrito carrito1 = new Carrito();

		Entrada entrada = new Entrada();
		entrada.setPrecioEntrada(21);
		entrada.setDescuento(4.20);
		entrada.setNumEspectadores(3);
		carrito1.setEntrada(entrada);

		Sesion sesion1 = new Sesion();
		sesion1.setIdSesion("SE008");
		carrito1.setSesion(sesion1);

		carritoTemporal.add(carrito1);
		// ------Segundo Entrada
		Carrito carrito2 = new Carrito();

		Entrada entrada2 = new Entrada();
		entrada2.setPrecioEntrada(8.50);
		entrada2.setDescuento(1.70);
		entrada2.setNumEspectadores(1);
		carrito2.setEntrada(entrada2);

		Sesion sesion2 = new Sesion();
		sesion2.setIdSesion("SC018");
		carrito2.setSesion(sesion2);

		carritoTemporal.add(carrito2);

		assertTrue(controlador.guardarDatosEnBDEntrada(carritoTemporal, compraFinal));
		controlador.cerrarConexion();
	}// delete from compra where IdCompra = 86

	/**
	 * Comprobamos que el mét. calcularDatosTabla genera correctamente el
	 * precioTotal de la compra y su descuentoAplicado tras unos valores dados.
	 */
	@Test
	public void CalculoPrecioTotalCompraCorrectoPruebas() {

		PruebasTest.carritoTemporal = new ArrayList<Carrito>();
		PruebasTest.compraFinal = new Compra();

		// ------Primera Compra
		Carrito carrito1 = new Carrito();

		Pelicula peli1 = new Pelicula();
		peli1.setNomPeli("El conjuro");

		Sala sala1 = new Sala();
		sala1.setNomSala("Primera");

		Sesion sesion1 = new Sesion();
		sesion1.setPrecio(7.0);
		sesion1.setPeli(peli1);
		sesion1.setSala(sala1);
		carrito1.setSesion(sesion1);

		Entrada entrada1 = new Entrada();
		entrada1.setNumEspectadores(3);
		carrito1.setEntrada(entrada1);

		PruebasTest.carritoTemporal.add(carrito1);

		// ------Segunda Compra
		Carrito carrito2 = new Carrito();

		Pelicula peli2 = new Pelicula();
		peli2.setNomPeli("Armageddon");

		Sala sala2 = new Sala();
		sala2.setNomSala("Séptima");

		Sesion sesion2 = new Sesion();
		sesion2.setPrecio(8.50);
		sesion2.setPeli(peli2);
		sesion2.setSala(sala2);
		carrito2.setSesion(sesion2);

		Entrada entrada2 = new Entrada();
		entrada2.setNumEspectadores(1);
		carrito2.setEntrada(entrada2);

		PruebasTest.carritoTemporal.add(carrito2);

		PruebasTest.calcularDatosTabla(0.20);
		assertEquals(29.50, PruebasTest.compraFinal.getPrecioTotal(), 0.01);
		assertEquals(5.90, PruebasTest.compraFinal.getDescuentoAplicado(), 0.01);

	}

	/**
	 * Comprobamos que el método genera un fichero (está vacío)
	 */

	@Test
	public void GenerarFicheroSinEntradas() {

		PruebasTest.carritoTemporal = new ArrayList<>();

		Cliente cliente = new Cliente();
		cliente.setDNI("PruebaTXT");

		PruebasTest.compraFinal = new Compra();
		PruebasTest.compraFinal.setCliente(cliente);

		ControladorFichero fichero = new ControladorFichero("ficheros/");
		boolean generado = fichero.escribirCompra("test_compras.txt", PruebasTest.carritoTemporal, cliente,
				PruebasTest.compraFinal);

		assertTrue(generado);
	}

}
