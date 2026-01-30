package controlador;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import vista.Principal;

public class controladorEntradaYSalida {

	private Scanner sc;

	public controladorEntradaYSalida() {
		sc = new Scanner(System.in);
	}

	// <T> Recibir cualquier tipo de objeto/Array (Pelicula, sesion....) y poder
	// modificarlo
	// Tambien se puede usar <?> pero este no permite modificaciones
	public <T> int elegirOpcion(List<T> lista) {
		try {
			int eleccion = Integer.valueOf(sc.nextLine());
			
			if (eleccion < 1 || eleccion > lista.size()) {
				System.out.println("El valor introducido no esta dentro de la lista");
				return -1;
			}
			return eleccion;

		} catch (Exception e) {
			System.out.println("Error durante la eleccion: " + e);//?????Le puedo quitar la e
			return -1;
		}
	}

	
	
	public int numeroDeEspectadores() {// generico
		try {
			int eleccion = Integer.valueOf(sc.nextLine());

			if (eleccion < 1 || eleccion > 5) {
				System.out.println("El valor introducido no está permitido");
				return -1;
			}
			return eleccion;

		} catch (Exception e) {
			System.out.println("Error durante la eleccion: " + e);//////???????????Le puedo quitar la e
			return -1;
		}

	}


	/** Método genérico para leer un número dentro de un rango. **/
	public int leerOpciones(int min, int max) {

		try {

			int opcion = Integer.valueOf(sc.nextLine());
			if (opcion < min || opcion > max) {
				System.out.println("Debe elegir un número entre " + min + " y " + max);
				return -1;
			}
			return opcion;
		} catch (NumberFormatException e) {
			System.out.println("Error: se esperaba un número.");
			return -1;
		}
	}

	public void recogerDatosUsuarioExistente() {

		String dniUsuario;
		String contraseñaUsuario;

		System.out.println("Escribe tu DNI");
		dniUsuario = sc.nextLine().trim();
		if (dniUsuario.isEmpty()) {
			System.out.println("Error, DNI incorrecto.");
			return;
		}

		System.out.println("Escribe tu contraseña");
		contraseñaUsuario = sc.nextLine().trim();
		if (contraseñaUsuario.isEmpty()) {
			System.out.println("Error, contraseña incorrecta.");
			return;
		}

		Principal.iniciarSesion(dniUsuario, contraseñaUsuario);
		

	}

	
	public void recogerDatosNuevoUsuario() {

		String dni, nombre, apellido, correo, contrasena;

		System.out.println("====== NUEVO USUARIO ======");

		System.out.print("DNI: ");
		dni = sc.nextLine().trim();

		if (dni.isEmpty() || dni.length() != 9) {
			System.out.println("Error: DNI incorrecto.");
			return;
		}

		System.out.print("Nombre: ");
		nombre = sc.nextLine().trim();
		if (nombre.isEmpty()) {
			System.out.println("Error, el nombre es obligatorio.");
			return;
		}

		System.out.print("Apellido: ");
		apellido = sc.nextLine().trim();
		if (apellido.isEmpty()) {
			System.out.println("Error, el apellido es obligatorio.");
			return;
		}

		System.out.print("Correo electrónico: ");
		correo = sc.nextLine().trim();
		if (correo.isEmpty() || !correo.contains("@")) {
			System.out.println("Error, el correo electrónico no es válido.");
			return;
		}

		System.out.print("Contraseña: ");
		contrasena = sc.nextLine().trim();
		if (contrasena.isEmpty()) {
			System.out.println("Error: la contraseña es obligatoria.");
			return;
		}

		Principal.registrarse(dni, nombre, apellido, correo, contrasena);
	}

}
