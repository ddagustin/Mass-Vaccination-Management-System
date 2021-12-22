package mvms.controllers;

import mvms.Main;
import mvms.Authenticator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author DR
 */
public class LoginController implements Initializable
{
    private Main application;
    
    @FXML
    private Hyperlink linkRegistration;
    
    @FXML
    private PasswordField password;

    @FXML
    private TextField username;
    
    public void setApp(Main application){
        this.application = application;
    }
    
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
        // TODO
    }
    
    private static void loginError() {
        Alert alert = new Alert( Alert.AlertType.WARNING );
        alert.setTitle( "Invalid username or password!" );
        alert.setHeaderText( "Invalid username or password" );
        alert.setContentText( "Please enter valid username and password." );
        alert.showAndWait();
    }
    
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
