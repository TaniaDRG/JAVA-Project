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
			conexion = DriverManager.getConnection("jdbc:mysql://localhost/" + this.nombreBD, "root", "");
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

	public ArrayList<Sesion> buscarSesionesYPeliculas(String where, String groupBy, String orderBy) {

		ArrayList<Sesion> listaSesionesYPeliculas = new ArrayList<Sesion>();
		String query = "SELECT S.IdSesion, S.Fecha, S.HoraInicio,  S.HoraFin, S.Precio, S.IdSala, "
				+ "P.IdPeli, P.NomPeli, P.genero, P.duracion, P.precio "
				+ "FROM sesion S JOIN pelicula P ON S.IdPeli = P.IdPeli ";

		if (where != null && !where.isEmpty()) {
			query = query + where;
		}
		if (groupBy != null && !groupBy.isEmpty()) {
			query = query + " GROUP BY " + groupBy;
		}
		if (orderBy != null && !orderBy.isEmpty()) {
			query = query + " ORDER BY " + orderBy;
			;
		}

		try {
			Statement consulta = conexion.createStatement();
			ResultSet resultado = consulta.executeQuery(query);

			while (resultado.next()) {
				Sesion sesionYPelicula = new Sesion();
				sesionYPelicula.setIdSesion(resultado.getString(1));
				sesionYPelicula.setFecha(resultado.getString(2));
				sesionYPelicula.setHoraInicio(resultado.getString(3));
				sesionYPelicula.setHoraFin(resultado.getString(4));
				sesionYPelicula.setPrecio(Double.parseDouble(resultado.getString(5)));
				Sala sala = new Sala();
				sesionYPelicula.setSala(sala); // Se crea el objeto sala para evitar error al guardar el idSala (error
												// nullpointer)
				sesionYPelicula.getSala().setIdSala(Integer.parseInt(resultado.getString(6)));
				Pelicula pelicula = new Pelicula();
				sesionYPelicula.setPeli(pelicula);
				sesionYPelicula.getPeli().setIdPeli(resultado.getString(7));
				/*
				 * Abrir el objeto sesionYPelicula (tipo Sesion), luego abrir el objeto Pelicula
				 * y despues almacenar en setXXX el valor devuelto por la consulta
				 */
				sesionYPelicula.getPeli().setNomPeli(resultado.getString(8));
				sesionYPelicula.getPeli().setGenero(resultado.getString(9));
				sesionYPelicula.getPeli().setDuracion(Integer.parseInt(resultado.getString(10)));
				sesionYPelicula.getPeli().setPrecio(Double.parseDouble(resultado.getString(11)));
				listaSesionesYPeliculas.add(sesionYPelicula);
			}

			consulta.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaSesionesYPeliculas;

	}

	public ArrayList<Sala> buscarSalas(Sesion sesion) {// ALGO FALLA MIRAR
		String query = "SELECT * FROM Sala WHERE IdSala = " + sesion.getSala().getIdSala();
		ArrayList<Sala> listaSalas = new ArrayList<Sala>();
		try {
			Statement consulta = conexion.createStatement();
			ResultSet resultado = consulta.executeQuery(query);

			while (resultado.next()) {
				Sala sala = new Sala();
				sala.setIdSala(Integer.parseInt(resultado.getString(1)));
				sala.setNomSala(resultado.getString(2));
				listaSalas.add(sala);
			}

			consulta.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaSalas;
	}

	public ArrayList<Cliente> buscarClienteBD(String dniUsuario, String contrasenaUsuario) {

		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		//defino la estructura de la query pues los ? son el espacio que reservo para 
		String query = "SELECT DNI, NomCliente, Apellido, Correo " 
						+ "FROM Cliente " 
						+ "WHERE DNI = ? "
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
			System.out.println("Error al iniciar sesión: " + e.getMessage());//? no sé para que es:+ e.getMessage()
			
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

}
