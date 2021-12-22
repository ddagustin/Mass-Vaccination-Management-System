package mvms.controllers;

import mvms.Authenticator;
import mvms.Main;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import person.*;

/**
 * FXML Controller class
 *
 * @author DR
 */
public class RegistrationController implements Initializable
{
    private Main application;
    private String selectedStaffType;
    
    @FXML
    private ComboBox<String> chooseStaffType;
    
    @FXML
    private ComboBox<String> choosePositionType;
    
    @FXML
    private Pane contactPane;

    @FXML
    private Pane medicalPane;
    
    @FXML
    private TextField affiliation;

    @FXML
    private TextField category;
    
    @FXML
    private TextField emailAddress;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;
    
    @FXML
    private TextField password;
    
    @FXML
    private TextField confirmPassword;

    @FXML
    private TextField phoneNumber;
    
    @FXML
    private TextField registrationID;

    @FXML
    private TextField state;

    @FXML
    private TextField streetAddress;

    @FXML
    private TextField suburb;

    @FXML
    private TextField username;

    
    public void setApp(Main application){
        this.application = application;
    }
    
    @FXML
    void processGoBack(ActionEvent event) {
        application.gotoLogin();
    }
    
    @FXML
    void onSelectStaffType(ActionEvent event) {
        choosePositionType.setVisible(false);
        medicalPane.setVisible(false);
        selectedStaffType = chooseStaffType.getSelectionModel().getSelectedItem();
        
        switch( selectedStaffType ) {
            case "Administrative Staff" -> layoutAdmin();
            case "Medical Staff" -> layoutMedical();
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        chooseStaffType.getItems().add("Administrative Staff");
        chooseStaffType.getItems().add("Medical Staff");
        choosePositionType.getItems().add("Full Time");
        choosePositionType.getItems().add("Part Time");
        choosePositionType.getItems().add("Volunteer");
        
        choosePositionType.setVisible(false);
        medicalPane.setVisible(false);
        
        contactPane.setLayoutY(230);
    }
    
    private void layoutAdmin()
    {
        choosePositionType.setVisible(true);
        contactPane.setLayoutY(275);
        
        application.getStage().setHeight(875);
        
        //application.stage.setHeight(830);
    }
    
    private void layoutMedical()
    {
        medicalPane.setVisible(true);
        medicalPane.setLayoutY(230);
        contactPane.setLayoutY(345);
        
        application.getStage().setHeight(945);

    }
    
    @FXML
    void registerStaff(ActionEvent event) {
        addStaff();
    }
    
    private void addStaff() {
        selectedStaffType = chooseStaffType.getSelectionModel().getSelectedItem();
        
        if( checkFields() ) {
            Authenticator.loadCredentials( username.getText(), password.getText() );
            switch( selectedStaffType ) {
                case "Administrative Staff" 
                        -> Main.addStaff( new AdminStaff( firstName.getText(), lastName.getText(), phoneNumber.getText(), emailAddress.getText(),
                                    username.getText(), password.getText(), streetAddress.getText(), suburb.getText(), state.getText(),
                                    choosePositionType.getSelectionModel().getSelectedItem() ));
                case "Medical Staff" 
                        -> Main.addStaff( new MedicalStaff( firstName.getText(), lastName.getText(), phoneNumber.getText(), emailAddress.getText(),
                                    username.getText(), password.getText(), streetAddress.getText(), suburb.getText(), state.getText(),
                                    registrationID.getText(), affiliation.getText(), category.getText() ));
            }
        }
        else
            registrationError();
    }
    
    private boolean checkFields()
    {
        selectedStaffType = chooseStaffType.getSelectionModel().getSelectedItem();
        
        if( selectedStaffType == null )
            return false;
        
        if( !firstName.getText().isBlank() && !lastName.getText().isBlank() && !phoneNumber.getText().isBlank() && !emailAddress.getText().isBlank() &&
            !username.getText().isBlank() && !password.getText().isBlank() && !streetAddress.getText().isBlank() && !suburb.getText().isBlank() && !state.getText().isBlank() &&
            phoneNumber.getText().matches("^\\d{10}$") && password.getText().matches( "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}$" ) && password.getText().equals(confirmPassword.getText()) )
        {
            if( selectedStaffType.equals( "Administrative Staff" )){
                if( choosePositionType.getSelectionModel().getSelectedItem() != null )
                    return true;
            }
            else{
                if( !registrationID.getText().isBlank() & !affiliation.getText().isBlank() & !category.getText().isBlank() )
                    return true;
            }
        }
        return false;
    }
    
    private void registrationError() {
        Alert alert = new Alert( Alert.AlertType.WARNING );
        alert.setTitle( "Some fields are blank" );
        alert.setHeaderText( "Some fields are blank" );
        alert.setContentText( "Please check if all required fields are entered." );
        alert.showAndWait();
    }
}
