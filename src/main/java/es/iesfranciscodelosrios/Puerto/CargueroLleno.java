package es.iesfranciscodelosrios.Puerto;

public class CargueroLleno extends Carguero implements Runnable{
	
	private Muelle m;
	
	private int numeroContenedores;
	
	public CargueroLleno() {
		super("");
		this.numeroContenedores=-1;
	}
	
	public CargueroLleno(Muelle m, String nombre, int numeroContenedores) {
		super(nombre);
		this.m=m;
		
		//Compruebo que el numero de contenedores no exceda el maximo que es capaz de cargar el carguero
		if(numeroContenedores>ESPACIOMAXIMO) {
			this.numeroContenedores=ESPACIOMAXIMO;
		}else {
			this.numeroContenedores = numeroContenedores;			
		}
	}

	@Override
	public void run() {
		m.descargaCarguero(this);
	}

	/**
	 * @return the numeroContenedores
	 */
	public int getNumeroContenedores() {
		return numeroContenedores;
	}
}
