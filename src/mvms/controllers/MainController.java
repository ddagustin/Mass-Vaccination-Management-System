/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mvms.controllers;

import mvms.Main;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * Assessment 1: Mass Vaccination Management System
 *      MainController class handles the main window.
 *      this class only contains navigation controls when accessing the left pane in the main window
 *      handles switching the right pane
 *          - vaccine recipients
 *          - user settings
 * 
 * @author DAgustin
 * 03 Dec 2021
 */
public class MainController implements Initializable
{
    // initialise unique variables to this class
    public Main application;
    
    @FXML
    public AnchorPane mainPane;

    // handles going back to the login page
    @FXML
    void processLogout(ActionEvent event) {
        application.gotoLogin();
    }
    
    // handles right pane switching to vaccine recipient
    @FXML
    void showVaccineRecipient(ActionEvent event) throws Exception {
        gotoVaccRecipient();
    }
    
    // handles right pane switching to user settings
    @FXML
    void showSettings( ActionEvent event ) throws Exception
    {
        gotoAccountSettings();
    }

    /**
     * Initializes the controller class.
     * shows the vaccine recipient pane by default
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
    
    // handles setting Main class instance to this class for access
    public void setApp(Main application){
        this.application = application;
    }
    
    // handles scene replacement and controller management - adds all children of recipient fxml to the right pane
    public void gotoVaccRecipient() throws Exception 
    {
        FXMLLoader loader =  new FXMLLoader( getClass().getResource( "/resources/recipient.fxml" ));
        mainPane.getChildren().setAll( (AnchorPane) loader.load() );
        
        RecipientController recipient = loader.getController();
        
        recipient.setLoggedUser();
    }
    
    // handles scene replacement and controller management - adds all children of settings fxml to the right pane
    public void gotoAccountSettings() throws Exception
    {
        FXMLLoader loader = new FXMLLoader( getClass().getResource( "/resources/settings.fxml" ));
        mainPane.getChildren().setAll( (AnchorPane) loader.load() );
        
        SettingsController settings = loader.getController();
        
        settings.setLoggedUser();
    }
}
