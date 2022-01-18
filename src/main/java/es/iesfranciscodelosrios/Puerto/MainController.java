package es.iesfranciscodelosrios.Puerto;

import java.net.URL;
import java.util.ResourceBundle;

import es.iesfranciscodelosrios.Utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * Crear una cola de cargueros a entrar como un arraylist. Igual para los camiones a salir
 * Los metodos sincronizados serán entracarguero y salecamion. Iran leyendo de la listas siempre y cuando haya espacios disponibles
 * 
 * El puerto tendrá que tener espacio para albergar contenedores
 * 
 * @author Cristian
 *
 */
public class MainController implements Initializable{
	
	@FXML
	private TextField tCNombre;
	
	@FXML
	private TextField tDNombre;
	
	@FXML
	private TextField tDContenedores;
	
	@FXML
	private TextField tCContenedores;
	
	@FXML
	private Button bCAdd;
	
	@FXML
	private Button bDAdd;
	
	@FXML
	private Label lNContenedores;
	
	@FXML
	private ListView<String> lvCarga;
	
	@FXML
	private ListView<String> lvDescarga;
	
	@FXML
	private ListView<String> lvDContenedores;
	
	@FXML
	private TextField tfWorkers;
	
	@FXML
	private TextField tfContainers;
	
	@FXML
	private Button bUnloadPackages;
	
	private Muelle muelle;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.muelle=new Muelle(120,lvCarga,lvDescarga,lvDContenedores,lNContenedores);
	}
	
	/**
	 * Metodo accionado por el boton de añadir un carguero vacio que pone en marcha el hilo para cargarlo con contenedores
	 */
	@FXML
	public void cargaCarguero(ActionEvent event) {
		try {		
			int numeroContenedores=Integer.parseInt(this.tCContenedores.getText());
			
			if(numeroContenedores>0&&numeroContenedores<21) {
				CargueroVacio cv=new CargueroVacio(muelle, this.tCNombre.getText(), numeroContenedores);
				
				Thread t=new Thread(cv);
				t.start();
				
				this.tCNombre.setText("");
				this.tCContenedores.setText("");
				
				this.bCAdd.setDisable(true);				
			}else {
				Utils.popError("Introduzca un numero entre 0 y 20");
				this.tCContenedores.setText("");
			}
		}catch(NumberFormatException e) {
			Utils.popError("Introduzca un numero valido");
		}
    }
	
	/**
	 * Metodo accionado por el boton de añadir un carguero lleno que pone en marcha el hilo para descargar sus contenedores
	 */
	@FXML
	public void descargaCarguero(ActionEvent event) {
		try {
			int numeroContenedores=Integer.parseInt(this.tDContenedores.getText());
			
			if(numeroContenedores>0&&numeroContenedores<21) {
				CargueroLleno cl=new CargueroLleno(muelle, this.tDNombre.getText(), numeroContenedores);
				
				Thread t=new Thread(cl);
				t.start();
				
				this.tDNombre.setText("");
				this.tDContenedores.setText("");
				
				this.bDAdd.setDisable(true);				
			}else {
				Utils.popError("Introduzca un numero entre 0 y 20");
				this.tDContenedores.setText("");
			}
		}catch(NumberFormatException e) {
			Utils.popError("Introduzca un numero valido");
		}
	}
	
	@FXML
	public void descargarPaquetes(ActionEvent event) {
		int nContainers=0;
		int nTeams=0;
		
		try {
			nContainers=Integer.parseInt(this.tfContainers.getText());
			nTeams=Integer.parseInt(this.tfWorkers.getText());
			
			this.muelle.descargaContenedores(nTeams, nContainers);
			
			this.tfContainers.setText("");
			this.tfWorkers.setText("");
			
			this.bUnloadPackages.setDisable(true);
		} catch (NumberFormatException e) {
			Utils.popWarning("Error. Ambos parametros deben ser un numero entero positivo");
		}
	}

	/**
	 * Comprueba si se han introducido datos en los campos de texto y habilita o deshabilita los botones en consecuencia
	 */
	@FXML
	public void checkTextFields(KeyEvent event) {
		if(!tDNombre.getText().isEmpty()&&!tDContenedores.getText().isEmpty()) {
			this.bDAdd.setDisable(false);
		}else {
			this.bDAdd.setDisable(true);
		}
		
		if(!tCNombre.getText().isEmpty()&&!tCContenedores.getText().isEmpty()) {
			this.bCAdd.setDisable(false);
		}else {
			this.bCAdd.setDisable(true);
		}
		
		if(!tfWorkers.getText().isEmpty()&&!tfContainers.getText().isEmpty()) {
			this.bUnloadPackages.setDisable(false);
		}else {
			this.bUnloadPackages.setDisable(true);
		}
	}
}
