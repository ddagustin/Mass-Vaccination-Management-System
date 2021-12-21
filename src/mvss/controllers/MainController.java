/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mvss.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import mvss.*;
import mvss.controllers.mainwindow.*;

/**
 * FXML Controller class
 *
 * @author DR
 */
public class MainController implements Initializable
{
    public Main application;
    
    @FXML
    private Button buttonLogout;
    
    @FXML
    private Button buttonHome;
     
    @FXML
    public AnchorPane mainPane;

    
    public void setApp(Main application){
        this.application = application;
    }
    
    @FXML
    void processLogout(ActionEvent event) {
        application.gotoLogin();
    }
    
    @FXML
    void showVaccineRecipient(ActionEvent event) throws Exception {
        gotoVaccRecipient();
    }
    
    @FXML
    void showSettings( ActionEvent event ) throws Exception
    {
        gotoAccountSettings();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try {
            gotoVaccRecipient();
        }
        catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void gotoVaccRecipient() throws Exception 
    {
        FXMLLoader loader =  new FXMLLoader( getClass().getResource( "/resources/recipient.fxml" ));
        mainPane.getChildren().setAll( (AnchorPane) loader.load() );
        
        RecipientController recipient = loader.getController();
        recipient.setMainController(this);
        
        recipient.setLoggedUser();
    }
    
    public void gotoAccountSettings() throws Exception
    {
        FXMLLoader loader = new FXMLLoader( getClass().getResource( "/resources/settings.fxml" ));
        mainPane.getChildren().setAll( (AnchorPane) loader.load() );
        
        SettingsController settings = loader.getController();
        settings.setMainController(this);
        
        settings.setLoggedUser();
    }
}
