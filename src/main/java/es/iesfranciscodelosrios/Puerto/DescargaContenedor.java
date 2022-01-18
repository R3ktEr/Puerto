package es.iesfranciscodelosrios.Puerto;

import java.util.Random;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class DescargaContenedor extends Thread {
	
	private ListView<String> lvDContenedores;
	
	private int nContainers;
	private int tNumber;
	
	public DescargaContenedor(int nContainers, int tNumber, ListView<String> lvDContenedores) {
		super();
		this.nContainers = nContainers;
		this.tNumber = tNumber;
		this.lvDContenedores = lvDContenedores;
	}

	@Override
	public void run() {
		Random rand=new Random();
		int unloadTime=rand.nextInt(6)+5;
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Equipo "+tNumber+" descargando "+nContainers+" contenedores. Tiempo estimado: "+unloadTime+" segundos.");
				lvDContenedores.getItems().add("Equipo "+tNumber+" descargando "+nContainers+" contenedores. Tiempo estimado: "+unloadTime+" segundos.");
				try {
					Thread.sleep(unloadTime*1000);
					System.out.println("El equipo "+tNumber+" ha descargando "+nContainers+" contenedores.");
					lvDContenedores.getItems().add("El equipo "+tNumber+" ha descargando "+nContainers+" contenedores.");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}								
			}			
		});
	}

	/**
	 * @return the nContainers
	 */
	public int getnContainers() {
		return nContainers;
	}

	/**
	 * @param nContainers the nContainers to set
	 */
	public void setnContainers(int nContainers) {
		this.nContainers = nContainers;
	}

	/**
	 * @return the tNumber
	 */
	public int gettNumber() {
		return tNumber;
	}

	/**
	 * @param tNumber the tNumber to set
	 */
	public void settNumber(int tNumber) {
		this.tNumber = tNumber;
	}

	@Override
	public String toString() {
		return "DescargaContenedor [nContainers=" + nContainers + ", tNumber=" + tNumber + "]";
	}
}
