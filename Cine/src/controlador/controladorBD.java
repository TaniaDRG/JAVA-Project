package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import modelo.Cliente;
import modelo.Pelicula;
import modelo.Sala;
import modelo.Sesion;

public class controladorBD {

	private Connection conexion;// Guarda la conexión con la base de datos.
	private String nombreBD;// Guarda el nombre de la base de datos

	// Constructores
	public controladorBD(String nombreBD) {
		this.nombreBD = nombreBD;
	}

	// Iniciar conexion
	public boolean iniciarConexion() {
		boolean conexionRealizada = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Parametros para la conexion --> URL, user, pass puede hacer falta el puerto
			// localhost:puerto/
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:33060/" + this.nombreBD, "daw",
					"daw2468");
			conexionRealizada = true;
		} catch (ClassNotFoundException e) {
			System.out.println("No se encontró la librería de sqlconnection.jar");
		} catch (SQLException e) {
			System.out.println("no se encontró la BD " + this.nombreBD);
		}

		return conexionRealizada;
	}

	// Cerrar conexion
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

	public ArrayList<Pelicula> buacarPeliculas() {

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return peliculasOrdenadas;

	}

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return FechasPeliElegida;

	}

	public ArrayList<Sesion> buscarSesiones(String IdPeli, String fechaElegida) {

		ArrayList<Sesion> sesionesDisponibles = new ArrayList<Sesion>();

		String query = " SELECT S.IdSesion, S.Fecha, S.HoraInicio, S.HoraFin, S.Precio, Sa.IdSala, Sa.NomSala, P.IdPeli, P.NomPeli " 
						+ "FROM Sesion S join Pelicula P on S.IdPeli = P.IdPeli join Sala Sa ON S.IdSala = Sa.IdSala " 
						+ "WHERE S.IdPeli= '" + IdPeli + "' and S.Fecha = '" + fechaElegida + "'";
		 				//S.IdPeli = ? AND S.Fecha = ?
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
				
				//Tengo que guardarlo primero en el subObjeto 
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

	

	public ArrayList<Cliente> buscarClienteBD(String dniUsuario, String contrasenaUsuario) {

		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		// defino la estructura de la query pues los ? son el espacio que reservo para
		String query = "SELECT DNI, NomCliente, Apellido, Correo " + "FROM Cliente " + "WHERE DNI = ? "
				+ "AND Contraseña = AES_ENCRYPT(?, 'elorrieta')";

		try {
			// PreparedStatement evita SQL Injection
			PreparedStatement consulta = conexion.prepareStatement(query);
			// relleno los ? (empiezan en 1, NO en 0)
			consulta.setString(1, dniUsuario);// primer ?
			consulta.setString(2, contrasenaUsuario);// segundo ?

			ResultSet resultado = consulta.executeQuery();

			while (resultado.next()) {
				Cliente clienteBuscado = new Cliente();
				clienteBuscado.setDNI(resultado.getString(1));
				clienteBuscado.setNomCliente(resultado.getString(2));
				clienteBuscado.setApellido(resultado.getString(3));
				clienteBuscado.setCorreo(resultado.getString(4));
				// no guardo la contraseña porque nunca la necesito una vez validado el login
				clientes.add(clienteBuscado);
			}

			resultado.close();
			consulta.close();

		} catch (SQLException e) {
			System.out.println("Error al iniciar sesión: " + e.getMessage());// ? no sé para que es:+ e.getMessage()

		}

		return clientes;

	}

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

	/*
	 * public boolean guardarDatosEnBDCompra() {
	 * 
	 * String query =
	 * "INSERT INTO Compra (IdCompra, Fecha, Hora, PrecioTotal, DescuentoAplicado, DNI) "
	 * + "VALUES (?,?,?,?,?,? ?)";
	 * 
	 * try { PreparedStatement consulta = conexion.prepareStatement(query);
	 * consulta.setString(1, nuevoCliente.getDNI()); consulta.setString(2,
	 * nuevoCliente.getNomCliente()); consulta.setString(3,
	 * nuevoCliente.getApellido()); consulta.setString(4, nuevoCliente.getCorreo());
	 * consulta.setString(5, nuevoCliente.getContraseña());
	 * 
	 * consulta.executeUpdate(); consulta.close(); return true;
	 * 
	 * } catch (SQLException e) {
	 * System.out.println("Error al insertar cliente (DNI o correo duplicado)");
	 * return false; }
	 * 
	 * }
	 */

}
