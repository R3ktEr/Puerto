package es.iesfranciscodelosrios.Puerto;

public class CargueroVacio extends Carguero implements Runnable{
	
	private Muelle m;
	
	private int numeroContenedores;
	
	public CargueroVacio(Muelle m, String nombre, int numeroContenedores) {
		super(nombre);
		this.m=m;
		this.numeroContenedores = numeroContenedores;
	}

	@Override
	public void run() {
		m.cargaCarguero(this);
	}

	/**
	 * @return the numeroContenedores
	 */
	public int getNumeroContenedores() {
		return numeroContenedores;
	}
}
