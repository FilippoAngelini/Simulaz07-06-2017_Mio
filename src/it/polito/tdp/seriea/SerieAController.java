/**
 * Sample Skeleton for 'SerieA.fxml' Controller Class
 */

package it.polito.tdp.seriea;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class SerieAController {
	
	Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxSeason"
    private ChoiceBox<Season> boxSeason; // Value injected by FXMLLoader

    @FXML // fx:id="boxTeam"
    private ChoiceBox<Team> boxTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    public void setModel(Model model){
    	this.model = model;
    	boxSeason.getItems().addAll(this.model.getSeasons());
    }

    @FXML
    void handleCarica(ActionEvent event) {
    	
    	Season stagione = boxSeason.getValue();
    	
    	if(stagione==null){
    		txtResult.setText("Selezionare una stagione");
    		return ;
    	}
    	
    	txtResult.clear();
    	txtResult.setText(model.doCarica(stagione));
    	
    	boxTeam.getItems().clear();
    	
    	boxTeam.getItems().addAll(model.getTeamsForSeason(stagione));
    }

    @FXML
    void handleDomino(ActionEvent event) {
    	/*
    	Team team = boxTeam.getValue();
    	
    	if(team==null){
    		txtResult.setText("Selezionare una squadra");
    		return ;
    	}*/
    	
    	Season stagione = boxSeason.getValue();
    	
    	if(stagione==null){
    		txtResult.setText("Selezionare una stagione");
    		return ;
    	}
    	
    	txtResult.setText(model.trovaCammino(stagione));

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxSeason != null : "fx:id=\"boxSeason\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert boxTeam != null : "fx:id=\"boxTeam\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";
    }
}
