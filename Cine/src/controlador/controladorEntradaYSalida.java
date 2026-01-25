package controlador;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

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
			int eleccion = sc.nextInt();
			sc.nextLine();
			if (eleccion < 0 || eleccion > lista.size()) {
				System.out.println("El valor introducido no esta dentro de la lista");
				return 0;
			}
			return eleccion;

		} catch (Exception e) {
			System.out.println("Error durante la eleccion: " + e);
			return 0;
		}
	}

	public int numeroDeEspectadores() {// hago esto generico????????????? ->NO
		try {
			int eleccion = Integer.valueOf(sc.nextLine());

			if (eleccion <= 0 || eleccion > 5) {
				System.out.println("El valor introducido no está permitido");
				return 0;
			}
			return eleccion;

		} catch (Exception e) {
			System.out.println("Error durante la eleccion: " + e);
			return 0;
		}

	}

	public int continuarProcesoCompra() {

		try {
			int continuar = Integer.valueOf(sc.nextLine());

			if (continuar != 1 && continuar != 2) {
				System.out.println("Debe elegir una de las opciones");
				return -1;
			}
			return continuar;

		} catch (NumberFormatException e) {
			System.out.println("Error, se esperaba 1 o 2:");
		}
		return -1;

	}
	
	
	

}
