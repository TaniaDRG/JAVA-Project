package controlador;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import modelo.Pelicula;
import modelo.Sesion;

public class ControladorBDTest {

	private static ControladorBD controlador;

	/**
	 * Comprobamos que se genera correctamente el Objeto cuando le pasamos una BD
	 * existente
	 */
	@Test
	public void testControladorValido() {
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
		if (peliculas.size() != 7) {
			fail("Se esperaban 7 películas");// son 6
		}
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
		for (int i = 0; i < sesiones.size(); i++) {
			assertEquals("8.50", String.format("%.2f", sesiones.get(i).getPrecio()));
		}
		controlador.cerrarConexion();
	}

	
	
	

	
	@Test
	public void testBuscarClienteBD() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertarCliente() {
		fail("Not yet implemented");
	}

	@Test
	public void testGuardarDatosEnBDCompra() {
		fail("Not yet implemented");
	}

	@Test
	public void testGuardarDatosEnBDEntrada() {
		fail("Not yet implemented");
	}

}
