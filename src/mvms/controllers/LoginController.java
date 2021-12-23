package mvms.controllers;

import mvms.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Assessment 1: Mass Vaccination Management System
 *      LoginController class contains all methods related to the login window
 * 
 * @author DAgustin
 * 03 Dec 2021
 */
public class LoginController implements Initializable
{
    // initialise unique variables to this class
    private Main application;
    
    @FXML
    private PasswordField password;
    
    @FXML
    private TextField username;
    
    // FXML methods
    @FXML
    void buttonLogin(ActionEvent event) {
        processLogin();
    }
    
    @FXML
    void processRegistration(ActionEvent event) {
        application.gotoRegistration();
    }
    
    @FXML
    void onEnter(KeyEvent event) {
        if (event.getCode() == (KeyCode.ENTER)) {
            processLogin();
        }
    }
    
    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb)
    {
        // nothing to do
    }
    
    // setApp to cast Main class into this class for easy access
    public void setApp(Main application){
        this.application = application;
    }
    
    // error showing when credentials are not valid
    private static void loginError() {
        Alert alert = new Alert( Alert.AlertType.WARNING );
        alert.setTitle( "Invalid username or password!" );
        alert.setHeaderText( "Invalid username or password" );
        alert.setContentText( "Please enter valid username and password." );
        alert.showAndWait();
    }
    
    // method to handle validation of login credentials
    public void processLogin() {
        if( Authenticator.checker(username.getText(), password.getText()) ) {
            Main.setLoggedUser( username.getText() );
            application.gotoHome();
        }
        else {
            loginError();
        }
    }
}
