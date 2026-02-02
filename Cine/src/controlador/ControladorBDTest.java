package controlador;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import modelo.Carrito;
import modelo.Cliente;
import modelo.Compra;
import modelo.Entrada;
import modelo.Pelicula;
import modelo.Sesion;

/**
 * 
 */
public class ControladorBDTest {

	private static ControladorBD controlador;

	/**
	 * Comprobamos que se genera correctamente el Objeto cuando le pasamos una BD
	 * existente
	 */
	@Test 
	public void _00_ControladorValido() {
		controlador = new ControladorBD("cine_daw");
		assertNotNull(controlador);// Obj. no es nulo
	}

	/**
	 * Comprobamos que se genera correctamente el Objeto cuando le pasamos una BD no
	 * existente
	 */
	@Test
	public void _01_constructorNoValido() {
		controlador = new ControladorBD(null);
		assertNotNull(controlador); // Obj. no es nulo
	}

	/**
	 * Comprobamos que la conexion con la BD existente se ejecute correctamete
	 */
	@Test
	public void _02_inicioDeConexionNormal() {
		controlador = new ControladorBD("cine_daw");
		boolean conexion = controlador.iniciarConexion();
		assertTrue(conexion);
		controlador.cerrarConexion();// Ojo: No olvidar
	}

	/**
	 * Comprobamos que la conexion con la una BD que NO existe falle
	 */
	@Test
	public void _03_inicioDeConexionNoExistente() {
		controlador = new ControladorBD("cine");
		boolean conexion = controlador.iniciarConexion();
		assertFalse(conexion);
	}

	/**
	 * Comprobamos que se cierra la conexion con la BD
	 */
	@Test
	public void _04_cerrarConexionNornal() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		assertTrue(controlador.cerrarConexion());
	}

	/**
	 * Comprobamos que NO se cierra la conexion con la BD enviando una conexion
	 * fallida por lo tanto la conexio será null
	 */
	@Test
	public void _05_cerrarConexionFallida() {
		controlador = new ControladorBD("cine");
		controlador.iniciarConexion();
		assertFalse(controlador.cerrarConexion());
	}

	/**
	 * Comprobamos que no se cierran conexiones inexistentes
	 */
	@Test
	public void _06_cerrarConexionNoExistente() {
		controlador = new ControladorBD("cine_daw");
		assertFalse(controlador.cerrarConexion());
	}

	/**
	 * Comprobamos que se obtiene una lista de películas (por lo menos una fila)
	 */
	@Test
	public void _07_buscarPeliculasDevuelvePeliculas() {
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
	public void _08_obtenerPeliculasCorrectas() {
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
	public void _09_buscarFechasPeliDevuelveFechas() {
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
	public void _10_buscarFechasPeliNoDevuelveFechas() {
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
	public void _11_buscarSesionesDevuelveSesiones() {
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
	public void _12_buscarSesionesCorrectas() {
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
	public void _13_buscarSesionesPrecioCorrecto() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		ArrayList<Sesion> sesiones = controlador.buscarSesiones("GREY", "2026-02-08");
		/*
		 * for (int i = 0; i < sesiones.size(); i++) { assertEquals("8.50",
		 * String.format("%.2f", sesiones.get(i).getPrecio()));
		 */
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
	public void _14_buscarClienteBDNoExiste() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		Cliente clienteBuscado = controlador.buscarClienteBD("XOXOXOXOX", "III");
		assertNull(clienteBuscado.getDNI());
		;
		controlador.cerrarConexion();
	}

	/**
	 * Comprobamos que se genera una excecion NullPointer cuando se intenta acceder
	 * a la BD sin haber iniciado la conexión antes (no devuelve nada porque
	 * revienta antes de llegar al return)
	 */
	@Test
	public void _15_buscarClienteBDSinConexion() {
		controlador = new ControladorBD("cine_daw");
		Cliente clienteBuscado = controlador.buscarClienteBD("01234567K", "elena123");
		assertNull(clienteBuscado);
		controlador.cerrarConexion();
	}

	/**
	 * Comprobamos que se crea un nuevo objeto-cliente al enviar unos valores
	 * correctos a la BD
	 */
	@Test
	public void _16_insertarClienteValido() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		Cliente nuevoCliente = new Cliente("98765432A", "Marvin", "Martian", "marciano@spam.com", "bipbip");
		assertTrue(controlador.insertarCliente(nuevoCliente));
		controlador.cerrarConexion();
	}// delete from cliente where DNI = '98765432A'

	/**
	 * Comprobamos que NO se crea un nuevo objeto-cliente al enviar unos valores
	 * incorrectos a la BD
	 */
	@Test
	public void _17_insertarClienteNoValido() {
		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();
		Cliente nuevoCliente = new Cliente("11001", "Marvin", "Martian", "marciano@spam.com", "bipbip");
		assertFalse(controlador.insertarCliente(nuevoCliente));
		controlador.cerrarConexion();
	}

	/**
	 * Comprobamos que se inserte correctamente en la BD una compra cuyos valores
	 * son correctos y que el IdCompra generado sea distinto de 0.
	 */
	@Test
	public void _17_GuardarCompraValida() {
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

	}// delete from compra where IdCompra = 91

	/**
	 * Comprobamos que la inserción de la compra en la BD falla pues sus valores son
	 * erroneos.
	 */
	/*@Test
	public void _19_GuardarCompraNoValida() {

		controlador = new ControladorBD("cine_daw");
		controlador.iniciarConexion();

		Compra compraFinal = new Compra();
		Cliente clienteNoExistente = new Cliente();
		clienteNoExistente.setDNI("NOOEXISTE");
		compraFinal.setCliente(clienteNoExistente);
		compraFinal.setPrecioTotal(29.50);
		compraFinal.setDescuentoAplicado(5.90);

		// La inserción falla → devuelve false
		assertFalse(controlador.guardarDatosEnBDCompra(compraFinal));
		// assertEquals(null, compraFinal.getIdCompra());

		controlador.cerrarConexion();
	}*/
	
	
	
	

	/**
	 * Comprobamos que se inserte correctamente en la BD las entradas de una compra, cuyos valores
	 * son correctos (el IdCompra se corresponde con la compra insertada en el test anterior).
	 */
	@Test
	public void _18_guardarEntradaValida() {
	    controlador = new ControladorBD("cine_daw");
	    controlador.iniciarConexion();
		  //---Compra final---
	    Compra compraFinal = new Compra();
	    compraFinal.setPrecioTotal(29.50); 
	    compraFinal.setDescuentoAplicado(5.90);
	    compraFinal.setIdCompra(96);//ojo tabla compra ult. fila
	    
	    ArrayList<Carrito> carritoTemporal = new ArrayList<>();
	    //------Primera Entrada 
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
	    //------Segundo Entrada 
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
	}
	
	
	
	/*@Test
	public void CalculoPrecioTotalCorrecto() {
		ArrayList<Carrito> carritoTemporal = new ArrayList<Carrito>();
	    Compra compraFinal = new Compra();

	    // rellenar carritoTemporal con Carrito/Entrada/Sesion
	    Carrito c1 = new Carrito();
	    Entrada e1 = new Entrada();
	    e1.setNumEspectadores(2);
	    Sesion s1 = new Sesion();
	    s1.setPrecio(10.0);
	    c1.setEntrada(e1);
	    c1.setSesion(s1);
	    carritoTemporal.add(c1);

	    // otro carrito
	    Carrito c2 = new Carrito();
	    Entrada e2 = new Entrada();
	    e2.setNumEspectadores(1);
	    Sesion s2 = new Sesion();
	    s2.setPrecio(10.0);
	    c2.setEntrada(e2);
	    c2.setSesion(s2);
	    carritoTemporal.add(c2);

	    // ejecutar método
	    calcularDatosTabla(0.10);

	    // aserciones
	    assertEquals(30.0, compraFinal.getPrecioTotal(), 0.01);
	}*/

}
