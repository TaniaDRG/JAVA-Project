package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import modelo.Carrito;
import modelo.Cliente;
import modelo.Compra;
import modelo.Pelicula;
import modelo.Sala;
import modelo.Sesion;

public class ControladorBD {

	private Connection conexion;
	private String nombreBD;

	public ControladorBD(String nombreBD) {
		this.nombreBD = nombreBD;
	}

	public boolean iniciarConexion() {
		boolean conexionRealizada = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Parametros para la conexion --> URL, user, pass puede hacer falta el puerto
			// localhost:puerto/
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:33060/" + this.nombreBD, "root",
					"elorrieta");
			conexionRealizada = true;
		} catch (ClassNotFoundException e) {
			System.out.println("No se encontró la librería de sqlconnection.jar");
		} catch (SQLException e) {
			System.out.println("no se encontró la BD " + this.nombreBD);
		}

		return conexionRealizada;
	}

	public boolean cerrarConexion() {
		boolean Conexioncerrada = false;

		try {
			if (conexion != null && !conexion.isClosed()) {
				conexion.close();
				Conexioncerrada = true;
			}
		} catch (SQLException e) {
			System.out.println("No hay conexion con la BD");
		}

		return Conexioncerrada;
	}

	/**
	 * Realiza una consulta a la BD para obtener el Nombre e ID de todas las
	 * películas cuya fecha y hora de sesion sea mayor o iagual al actual. Crea
	 * objetos Pelicula con la información obtenida y los almacena en un ArrayList.
	 * 
	 * @return ArrayList<Pelicula> con todas las películas disponibles según las
	 *         sesiones. Si ocurre un error de SQL, devuelve un ArrayList vacío.
	 **/
	public ArrayList<Pelicula> buscarPeliculas() {

		ArrayList<Pelicula> peliculasOrdenadas = new ArrayList<Pelicula>();

		String query = " SELECT P.IdPeli, P.NomPeli " + " FROM Pelicula P join Sesion S on S.IdPeli = P.IdPeli "
				+ " WHERE (S.Fecha > CURDATE() OR (S.Fecha = CURDATE() AND S.HoraInicio > CURTIME())) "
				+ " GROUP BY  P.IdPeli,P.NomPeli " + " ORDER BY MIN(timestamp(S.Fecha, S.HoraInicio)) asc; ";

		try {
			Statement consulta = conexion.createStatement();
			ResultSet resultado = consulta.executeQuery(query);
			while (resultado.next()) {
				Pelicula pelicula = new Pelicula();
				pelicula.setIdPeli(resultado.getString(1));
				pelicula.setNomPeli(resultado.getString(2));
				peliculasOrdenadas.add(pelicula);
			}

			consulta.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return peliculasOrdenadas;

	}

	/**
	 * Realiza una consulta a la BD para obtener todas las fechas disponibles para
	 * esa película con fecha u Hora posterior al actual. Almacena cada fecha en un
	 * objeto Sesion y se agrega a un ArrayList (FechasPeliElegida) que se devuelve
	 * al final del método.
	 * 
	 * @param IdPeli
	 * @return = FechasPeliElegida
	 */
	public ArrayList<Sesion> buscarFechasPeli(String IdPeli) {

		ArrayList<Sesion> FechasPeliElegida = new ArrayList<Sesion>();

		String query = " SELECT S.Fecha " + " FROM Sesion S join Pelicula P on S.IdPeli = P.IdPeli "
				+ " WHERE P.IdPeli = '" + IdPeli
				+ "' and (S.Fecha > CURDATE() OR (S.Fecha = CURDATE() AND S.HoraInicio > CURTIME())) "
				+ " GROUP BY  S.Fecha " + " ORDER BY S.Fecha; ";

		try {
			Statement consulta = conexion.createStatement();
			ResultSet resultado = consulta.executeQuery(query);
			while (resultado.next()) {
				Sesion fecha = new Sesion();
				fecha.setFecha(resultado.getString(1));

				FechasPeliElegida.add(fecha);
			}

			consulta.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return FechasPeliElegida;

	}

	/**
	 * Realiza una consulta a la BD para obtener todas las sesiones disponibles de
	 * una película y fecha específica. Cada sesión incluye información de Sala y
	 * película asociada, y se almacena en un objeto Sesion.
	 * 
	 * 
	 * @param IdPeli
	 * @param fechaElegida
	 * @return = sesionesDisponibles
	 */
	public ArrayList<Sesion> buscarSesiones(String IdPeli, String fechaElegida) {

		ArrayList<Sesion> sesionesDisponibles = new ArrayList<Sesion>();

		String query = " SELECT S.IdSesion, S.Fecha, S.HoraInicio, S.HoraFin, S.Precio, Sa.IdSala, Sa.NomSala, P.IdPeli, P.NomPeli "
				+ "FROM Sesion S join Pelicula P on S.IdPeli = P.IdPeli join Sala Sa ON S.IdSala = Sa.IdSala "
				+ "WHERE S.IdPeli= '" + IdPeli + "' and S.Fecha = '" + fechaElegida
				+ "' AND (S.Fecha > CURDATE() OR (S.Fecha = CURDATE() AND S.HoraInicio > CURTIME())) "
				+ "ORDER BY S.HoraInicio ";
		// S.IdPeli = ? AND S.Fecha = ?
		try {
			Statement consulta = conexion.createStatement();
			ResultSet resultado = consulta.executeQuery(query);
			while (resultado.next()) {
				Sesion sesion = new Sesion();
				sesion.setIdSesion(resultado.getString(1));
				sesion.setFecha(resultado.getString(2));
				sesion.setHoraInicio(resultado.getString(3));
				sesion.setHoraFin(resultado.getString(4));
				sesion.setPrecio(Double.parseDouble(resultado.getString(5)));

				// Tengo que guardarlo primero en el subObjeto
				Sala sala = new Sala();
				sala.setIdSala(resultado.getInt(6));
				sala.setNomSala(resultado.getString(7));
				sesion.setSala(sala);// ojo

				Pelicula peli = new Pelicula();
				peli.setIdPeli(resultado.getString(8));
				peli.setNomPeli(resultado.getString(9));
				sesion.setPeli(peli);// ojo

				sesionesDisponibles.add(sesion);
			}

			consulta.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sesionesDisponibles;

	}

	/**
	 * Busca un cliente en la base de datos según su DNI y contraseña. Se realiza
	 * una consulta segura utilizando PreparedStatement para evitar SQL Injection.
	 * Si se encuentra un cliente que coincide con el DNI y la contraseña, se
	 * devuelven sus datos en un obj. Cliente
	 * 
	 * @param dniUsuario
	 * @param contrasenaUsuario
	 * @return
	 */
	public Cliente buscarClienteBD(String dniUsuario, String contrasenaUsuario) {

		Cliente clienteBuscado = new Cliente();
		// defino la estructura de la query pues los ? son el espacio que reservo para
		String query = "SELECT DNI, NomCliente, Apellido, Correo " + "FROM Cliente " + "WHERE DNI = ? "
				+ "AND Contraseña = AES_ENCRYPT(?, 'elorrieta')";

		try {
			// evita SQL Injection
			PreparedStatement consulta = conexion.prepareStatement(query);
			// relleno los ? (empiezan en 1, NO en 0)
			consulta.setString(1, dniUsuario);// primer ?
			consulta.setString(2, contrasenaUsuario);// segundo ?

			ResultSet resultado = consulta.executeQuery();

			while (resultado.next()) {
				clienteBuscado.setDNI(resultado.getString(1));
				clienteBuscado.setNomCliente(resultado.getString(2));
				clienteBuscado.setApellido(resultado.getString(3));
				clienteBuscado.setCorreo(resultado.getString(4));
				// no guardo la contraseña porque nunca la necesito una vez validado el login
			}

			resultado.close();
			consulta.close();

		} catch (SQLException e) {
			System.out.println("Error al iniciar sesión ");

		}

		return clienteBuscado;

	}

	/**
	 * Inserta un nuevo cliente en la base de datos devolviendo true. Si ocurre
	 * algun problema en la inserción mensaje de esrror y devuelve false.
	 * 
	 * @param nuevoCliente
	 * @return true/false
	 */
	public boolean insertarCliente(Cliente nuevoCliente) {

		String query = "INSERT INTO Cliente (DNI, NomCliente, Apellido, Correo, Contraseña) "
				+ "VALUES (?,?,?,?, AES_ENCRYPT(?, 'elorrieta'))";

		try {
			PreparedStatement consulta = conexion.prepareStatement(query);
			consulta.setString(1, nuevoCliente.getDNI());
			consulta.setString(2, nuevoCliente.getNomCliente());
			consulta.setString(3, nuevoCliente.getApellido());
			consulta.setString(4, nuevoCliente.getCorreo());
			consulta.setString(5, nuevoCliente.getContraseña());

			consulta.executeUpdate();
			consulta.close();
			return true;

		} catch (SQLException e) {

			System.out.println("Error al insertar cliente (DNI o correo duplicado)");
			return false;
		}

	}

	/**
	 * Inserta una nueva compra en la tabla compra. Tras inserción, recupera el
	 * IdCompra generado automáticamente almacenandolo en el obj. compraFinal.
	 * Devuelve true si la inserción fué correcta o false si se produce algún error.
	 * 
	 * @param compraFinal
	 * @return true/false
	 */
	public boolean guardarDatosEnBDCompra(Compra compraFinal) {

		String query = "INSERT INTO Compra (Fecha, Hora, PrecioTotal, DescuentoAplicado, DNI) "
				+ "VALUES (current_date,current_time,?,?,?)";

		try {
			PreparedStatement consulta = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			consulta.setDouble(1, compraFinal.getPrecioTotal());
			consulta.setDouble(2, compraFinal.getDescuentoAplicado());
			consulta.setString(3, compraFinal.getCliente().getDNI());

			consulta.executeUpdate();

			// OBTENER ID AUTOGENERADO
			ResultSet resultado = consulta.getGeneratedKeys();
			if (resultado.next()) {
				int idCompraGenerado = resultado.getInt(1);
				compraFinal.setIdCompra(idCompraGenerado);
			}

			resultado.close();
			consulta.close();
			return true;

		} catch (SQLException e) {
			System.out.println("Error al insertar la compra");
			return false;
		}

	}

	/**
	 * Inserta en la base de datos todas las entradas almacenadas en el
	 * carritoTemporal. Obtiene el IdEntrada generado automáticamente y lo guarda en
	 * la fila correspondiente del arrayList. Devuelve true si la inserción fué
	 * correcta o false si se produce algún error.
	 * 
	 * @param carritoTemporal
	 * @param compraFinal
	 * @return
	 */
	public boolean guardarDatosEnBDEntrada(ArrayList<Carrito> carritoTemporal, Compra compraFinal) {

		String query = "INSERT INTO Entrada (PrecioEntrada, Descuento, NumEspectadores, IdCompra, IdSesion) "
				+ "VALUES (?,?,?,?,?)";

		try {
			PreparedStatement consulta = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			for (Carrito carrito2 : carritoTemporal) {

				consulta.setDouble(1, carrito2.getEntrada().getPrecioEntrada());
				consulta.setDouble(2, carrito2.getEntrada().getDescuento());
				consulta.setInt(3, carrito2.getEntrada().getNumEspectadores());
				consulta.setInt(4, compraFinal.getIdCompra());
				consulta.setString(5, carrito2.getSesion().getIdSesion());

				consulta.executeUpdate();

				ResultSet resultado = consulta.getGeneratedKeys();
				if (resultado.next()) {
					int idEntradaGenerado = resultado.getInt(1);
					carrito2.getEntrada().setIdEntrada(idEntradaGenerado);// ?????

				}

				resultado.close();
			}

			consulta.close();
			return true;

		} catch (SQLException e) {
			System.out.println("Error al insertar cliente (DNI o correo duplicado)");
			return false;
		}

	}

}
