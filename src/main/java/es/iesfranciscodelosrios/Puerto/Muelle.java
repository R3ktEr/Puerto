package es.iesfranciscodelosrios.Puerto;

import java.util.Random;

import es.iesfranciscodelosrios.Utils.Utils;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class Muelle{

	private ListView<String> lvCarga;
	private ListView<String> lvDescarga;
	private ListView<String> lvDContenedores;
	
	private Label lNContenedores;

	/**
	 * Variables que compartiran los metodos sincronizados.
	 * En este caso seran el espacio total y el espacio libre
	 */
	private int espacioTotal;
	private int espacioLibre;
	private boolean haycontenedores;
	
	public Muelle() {
		this.espacioTotal=120;
		this.espacioLibre=this.espacioTotal;
	}
		
	public Muelle(int espacioTotal, ListView<String> lvCarga, ListView<String> lvDescarga, ListView<String> lvDContenedores, Label lNContenedores) {
		super();
		this.espacioTotal = espacioTotal;
		this.espacioLibre = espacioTotal;
		this.lvCarga=lvCarga;
		this.lvDescarga=lvDescarga;
		this.lvDContenedores=lvDContenedores;
		this.lNContenedores=lNContenedores;
		this.lNContenedores.setText(Integer.toString(espacioTotal));
		this.haycontenedores=false;
	}

	/**
	 * Metodo sincronizado que descarga el carguero y lo notifica
	 * 
	 * @param c El carguero lleno
	 */
	public synchronized void descargaCarguero(CargueroLleno c) {    		
		if(espacioLibre>0&&(espacioLibre>=c.getNumeroContenedores())) {
			espacioLibre=espacioLibre-c.getNumeroContenedores();
			
			Random rand=new Random();
			int unloadTime=rand.nextInt(6)+5; //Numero aleatorio entre 5 y 10 (incluidos)
			
			printLine(lvDescarga, c.getNombre()+" descargando. Tiempo estimado: "+unloadTime+" segundos");
			
			try {
				Thread.sleep(unloadTime*1000); //Espera el timepo generado aleatoriamente (en milisegundos)
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			printLine(lvDescarga, c.getNombre()+" ha descargado "+c.getNumeroContenedores()+" contenedores\n");	
			
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					lNContenedores.setText(Integer.toString(espacioLibre));			
				}			
			});
		}else {
			printLine(lvDescarga, "No hay suficiente espacio en el puerto\n");
		}   
		
		haycontenedores=true;
		notifyAll();
		
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo sincronizado que carga el carguero y lo notifica
	 * 
	 * @param c El carguero lleno
	 */
	public synchronized void cargaCarguero(CargueroVacio c) {
		if(haycontenedores==true) {
			int espacioOcupado=espacioTotal-espacioLibre;
			
			if(espacioOcupado>0) {
				/**
				 * Carga el carguero con el numero de contenedores indicados siempre y cuando dichos contenedores esten disponibles en el muelle
				 * De lo contrario, cargará el carguero con el numero de contenedores restantes en el muelle.
				 */
				if(espacioOcupado>=c.getNumeroContenedores()) { 			
					espacioLibre=espacioLibre+c.getNumeroContenedores();
					
					Random rand=new Random();
					int loadTime=rand.nextInt(6)+5;
					
					printLine(lvCarga, c.getNombre()+" cargando. Tiempo estimado: "+loadTime+" segundos");
					
					try {
						Thread.sleep(loadTime*1000);
						
						printLine(lvCarga, c.getNombre()+" ha cargado "+c.getNumeroContenedores()+" contenedores\n");	
						
						Platform.runLater(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								lNContenedores.setText(Integer.toString(espacioLibre));			
							}			
						});
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					espacioLibre=espacioLibre+espacioOcupado;
					
					Random rand=new Random();
					int loadTime=rand.nextInt(6)+5;
					
					printLine(lvCarga, "Solo quedan "+espacioOcupado+" contenedores en el puerto");
					printLine(lvCarga, c.getNombre()+" cargando "+espacioOcupado+" contenedores. Tiempo estimado: "+loadTime+" segundos");
					
					try {
						Thread.sleep(loadTime*1000);
						
						printLine(lvCarga, c.getNombre()+" ha cargado "+espacioOcupado+" contenedores\n");
						
						Platform.runLater(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								lNContenedores.setText(Integer.toString(espacioLibre));			
							}			
						});
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}			
			}
			
			if(espacioOcupado>0) {
				haycontenedores=true; 
			}else {
				haycontenedores=false;
			}
			
			notifyAll();
			
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				printLine(lvCarga, "No hay contenedores en el puerto, carguero en espera");
				wait();
				cargaCarguero(c); //Recursividad. Vuelve a intentar cargar el carguero despues del wait
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Codigo para imprimir en los listView del MainController evitando la excepcion "Not on Thread FX"
	 * 
	 * @param lv lista en la que se va a imprimir
	 * @param st cadena que se va a imprimir
	 */
	private void printLine(ListView<String> lv, String st) {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				lv.getItems().add(st);				
			}			
		});
	}
	
	/**
	 * Metodo al que se le pasa un numero de contendores y un numero de equipos de trabajo y distribuye equitativamente el trabajo de descargar dichos
	 * contenedores entre los equipos. Los equipos trabajan por turnos, es decir, solo puede haber trabajando un equipo a la vez y hasta que no termine 
	 * uno no empieza otro.
	 * 
	 * @param nTeams el numero de equipos de trabajo
	 * @param nContainers el numero de contenedores a descargar
	 */
	public void descargaContenedores(int nTeams, int nContainers) {
		int initialContainers=nContainers;
		int containersPerTeam=0;
		
		if(nTeams>0) {
			if(nContainers>0) {
				if(nTeams<nContainers) {
					containersPerTeam=Math.abs(nContainers/nTeams);
					
					for(int i=0; i<nTeams; i++) {
						DescargaContenedor d=new DescargaContenedor(containersPerTeam, (i+1), lvDContenedores);
						d.start();
						try {
							d.join();
							initialContainers-=containersPerTeam;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}
					
					/*
					 * En caso de que al repartir los contenedores entre los equipos de trabajadores, de un numero decimal, sobraran paquetes.
					 * En este caso se asignara un ultimo equipo para descargar los contenedores que falten
					 */
					if(initialContainers>0) {
						DescargaContenedor d=new DescargaContenedor(initialContainers, (nTeams+1), lvDContenedores);
						d.start();
						try {
							d.join();
							initialContainers=0;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}			
				}else {
					Utils.popError("Error. El numero de contenedores no puede ser menor que el numero de equipos");
				}
			}else {
				Utils.popError("Error. El numero de contenedores no puede ser menor que 0");
			}
		}else {
			Utils.popError("Error. El numero de equipos no puede ser menor que 0");
		}
	}
}
