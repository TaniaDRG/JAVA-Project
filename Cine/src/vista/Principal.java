package vista;

import java.util.ArrayList;

import controlador.controladorBD;
import controlador.controladorEntradaYSalida;
import modelo.Carrito;
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
		mostrarMenu();
		controlador.cerrarConexion();
	}

	public static void mostrarMenu() {
		System.out.println("Lista de peliculas: \n");
		ArrayList<Sesion> listaPeliculasOrdenadas = controlador.buscarSesionesYPeliculas(null, "P.IdPeli");
		int cont = 1;
		for (Sesion peliPorSesion : listaPeliculasOrdenadas) {
			System.out.println(cont + " - " + peliPorSesion.getPeli().getNomPeli());
			cont++;
		}

		int numElegido = 0;
		do {
			System.out.println("Elija una opcion: ");
			numElegido = controladorES.elegirOpcion(listaPeliculasOrdenadas);
		} while (numElegido == 0);

		mostrarSesionesDeUnaPeli(listaPeliculasOrdenadas.get(numElegido - 1).getPeli());

	}

	public static void mostrarSesionesDeUnaPeli(Pelicula pelicula) {
		System.out.println("Las fechas disponibles para la pelicula (" + pelicula.getNomPeli() + ") son:");
		String where = " WHERE P.IdPeli = '" + pelicula.getIdPeli() + "'";
		String groupBy = "s.Fecha";
		ArrayList<Sesion> listaSesionesDePeli = controlador.buscarSesionesYPeliculas(where, groupBy);
		int cont = 1;
		for (Sesion sesion : listaSesionesDePeli) {
			System.out.println(cont + " - " + sesion.getFecha());
			cont++;
		}

		int numElegido = 0;
		do {
			System.out.println("Elija una opcion: ");
			numElegido = controladorES.elegirOpcion(listaSesionesDePeli);
		} while (numElegido == 0);
		String fechaElegida = listaSesionesDePeli.get(numElegido - 1).getFecha();
		listaSesionesDePeli = controlador.buscarSesionesYPeliculas(where, null);
		mostrarSesion(listaSesionesDePeli, numElegido, fechaElegida);
	}

	
	private static void mostrarSesion(ArrayList<Sesion> listaSesionesDePeli, int sesionElegida, String fechaElegida) {
		
		ArrayList<Sala> listaSalas = controlador.buscarSalas(listaSesionesDePeli.get(sesionElegida - 1));
		int contList = 1;
		int i = 0;
		ArrayList<Sesion> horaElegida = new ArrayList<Sesion>();
		for (Sesion sesion : listaSesionesDePeli) {
			if (sesion.getFecha().equals(fechaElegida)) {
				System.out.println(contList + " - FECHA: " + sesion.getFecha() + " - Horario: " + sesion.getHoraInicio()
						+ " Sala: " + listaSalas.get(0).getNomSala() + " Precio: " + sesion.getPrecio());
				horaElegida.add(contList - 1, sesion);
				contList++;
			}
			i++;
		}

		int numElegido = 0;
		do {
			System.out.println("Elija una opcion: ");
			numElegido = controladorES.elegirOpcion(horaElegida);
		} while (numElegido == 0);

		elegirEspectadores(horaElegida.get(numElegido - 1));
	}

	private static void elegirEspectadores(Sesion sesionFinal) {

		int numEspectElegido = 0;
		do {
			System.out.println("Elija el numero de espectadores (max. 5):");
			numEspectElegido = controladorES.numeroDeEspectadores();
		} while (numEspectElegido == 0);

		guardarDatosEnCarrito(sesionFinal, numEspectElegido);

	}

	public static void guardarDatosEnCarrito(Sesion sesionFinal, int numEspectElegido) {
		Carrito carrito = new Carrito();
		carrito.setSesion(sesionFinal);// guardo la fila/sesion selecionada a mi carrito

		Entrada entradaTemporal = new Entrada();
		entradaTemporal.setNumEntradas(numEspectElegido);
		//entradaTemporal.getSesion().getSala().setNomSala();
		carrito.setEntrada(entradaTemporal);

		carritoTemporal.add(carrito);
		mostrarSuEleccionDePelis(carritoTemporal);//ArrayList para almacenar lo elegido (Tipo: Carrito)
	}

	public static void mostrarSuEleccionDePelis(ArrayList<Carrito> carritoTemporal) {
		System.out.println("Tu Carrito:\n");

		for (Carrito carrito2 : carritoTemporal) {

			System.out.println("----------------------------------------");
			System.out.println("\nPelicula: " + carrito2.getSesion().getPeli().getNomPeli()
					+ "\nFecha:" + carrito2.getSesion().getFecha() + "\nHora Inicio:" + carrito2.getSesion().getHoraInicio()+ 
					"\nSala:" + carrito2.getSesion().getSala().getNomSala() + "\nPrecio:" +carrito2.getSesion().getPrecio() +
					"\nNúmero de entradas:" +carrito2.getEntrada().getNumEntradas());
		}
		SeguirComprando();
	}

	public static void SeguirComprando() {

		int comprarMás;
		do {

			System.out.println("Quiere comprar más sesiones: ");
			System.out.println("1. Sí");
			System.out.println("2. No");
			comprarMás = controladorES.continuarProcesoCompra();

		} while (comprarMás != 1 && comprarMás != 2);

		if (comprarMás == 1) {
			mostrarMenu();

		} else {
			calcularDescuento();

		}
	}

	private static void calcularDescuento() {
		
		  int peliculasDistintas = 0;
		  ArrayList<String> nombresPeliculas = new ArrayList<>();
		  
		  for (int i = 0; i < carritoTemporal.size(); i++) {
			  String nombreActual = carritoTemporal.get(i).getSesion().getPeli().getNomPeli();
			  
			  boolean yaContado = false;
			  
			   // Comprobar si la Peli ya existe en arrayList
			  for (int j = 0; j < nombresPeliculas.size(); j++) {
			      if (nombresPeliculas.get(j).equals(nombreActual)) {
		                yaContado = true;
		              
		            }
		        }
		       // Si no estaba contado, sumamos a la lista
		        if (!yaContado) {
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

		    // Calculo descuento para cada carrito
		    for (int i = 0; i < carritoTemporal.size(); i++) {
		    	
		        double precio = carritoTemporal.get(i).getSesion().getPrecio();
		        int entradas = carritoTemporal.get(i).getEntrada().getNumEntradas();

		        double descuento = (precio * entradas) * porcentajeDescuento;

		        carritoTemporal.get(i).getEntrada().setDescuento(descuento);
		    }
		    
		    for (Carrito carrito2 : carritoTemporal) {
		  System.out.println("---------------------\nPelicula: " + carrito2.getSesion().getPeli().getNomPeli() 
				  + "\nPrecio:" +carrito2.getSesion().getPrecio() +
					"\nNúmero de entradas:" + carrito2.getEntrada().getNumEntradas() + "\nDescuento:"+ carrito2.getEntrada().getDescuento());
		};
	}


	
	
}
