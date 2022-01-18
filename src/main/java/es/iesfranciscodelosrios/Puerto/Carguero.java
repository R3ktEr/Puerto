package es.iesfranciscodelosrios.Puerto;

/**
 * Clase padre de la que extenderan el carguero vacio y el carguero lleno
 * 
 * @author Cristian
 *
 */
public class Carguero {
	public static final int ESPACIOMAXIMO=20;
	
	private String nombre;
	
	public Carguero(String nombre) {
		this.nombre=nombre;
	}

	/**
	 * @return the name
	 */
	public String getNombre() {
		return this.nombre;
	}
		
}
