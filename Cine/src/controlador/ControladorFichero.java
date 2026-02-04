package controlador;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import modelo.Carrito;
import modelo.Cliente;
import modelo.Compra;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ControladorFichero {
	
	private String ruta;
	
	private static int numeroCompra = 1;
	
	public ControladorFichero(String ruta) {
		this.ruta = ruta;
	}
	
	public boolean escribirCompra(String nombreFichero, ArrayList<Carrito> carrito, Cliente cliente, Compra compra) {

	    try {
	        File directorio = new File(ruta);
	        if (!directorio.exists()) {
	            directorio.mkdirs();
	        }
	        
	        File fichero = new File (ruta + nombreFichero);
	        if (!fichero.exists()) {
	        	fichero.createNewFile();
	        }


	        BufferedWriter escritor = new BufferedWriter(
	                new FileWriter(ruta + nombreFichero, true)
	        );
	        
	        LocalDateTime fechaHoraActual = LocalDateTime.now();
	        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	        
	        //CABECERA
	        escritor.write("==========TICKET DE COMPRA==========");
	        escritor.newLine();
	        
	        escritor.write("Compra nº " + numeroCompra);
	        escritor.newLine();
	        
	        escritor.write("Fecha impresion: " + fechaHoraActual.format(formato));
            escritor.newLine();
            
            escritor.write("Cliente: " + cliente.getNomCliente());
            escritor.newLine();
            
            escritor.write("----------");
            escritor.newLine();
	        
            //DETALLES DE SESION
	        for (Carrito c : carrito) {

	            escritor.write("Pelicula: " + c.getSesion().getPeli().getNomPeli());
	            escritor.newLine();

	            escritor.write("Fecha: " + c.getSesion().getFecha());
	            escritor.newLine();

	            escritor.write("Hora: " + c.getSesion().getHoraInicio());
	            escritor.newLine();

	            escritor.write("Sala: " + c.getSesion().getSala().getIdSala());
	            escritor.newLine();

	            escritor.write("Entradas: " + c.getEntrada().getNumEspectadores());
	            escritor.newLine();

	            double subtotal = c.getEntrada().getPrecioEntrada();
	            escritor.write("Subtotal: " + String.format("%.2f", subtotal) + " €");
	            escritor.newLine();

	            escritor.write("------------------------------------");
	            escritor.newLine();
	        }

	        // ===== TOTALES =====
	        escritor.write("TOTAL SIN DESCUENTO: "
	                + String.format("%.2f", compra.getPrecioTotal()) + " €");
	        escritor.newLine();

	        escritor.write("DESCUENTO APLICADO: -"
	                + String.format("%.2f", compra.getDescuentoAplicado()) + " €");
	        escritor.newLine();

	        double totalFinal = compra.getPrecioTotal() - compra.getDescuentoAplicado();
	        escritor.write("TOTAL A PAGAR: "
	                + String.format("%.2f", totalFinal) + " €");
	        escritor.newLine();

	        escritor.write("====================================");
	        escritor.newLine();
	        escritor.newLine();

	        numeroCompra++;
	        
	        escritor.close();
	        
	        return true;

	    } catch (IOException e) {
	        System.out.println("Error al escribir el fichero de compras");
	        e.printStackTrace();
	        return false;
	    }
	}
}