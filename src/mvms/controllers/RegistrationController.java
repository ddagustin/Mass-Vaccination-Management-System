package mvms.controllers;

import entities.*;
import mvms.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

/**
 * Assessment 1: Mass Vaccination Management System
 *      RegistrationController class contains all functions and data when a new user registers
 * 
 * @author DAgustin
 * 03 Dec 2021
 */
public class RegistrationController implements Initializable
{
    // initialise unique variables to this class
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
    private Pane detailsPane;
    
    @FXML
    private TextField affiliation;

    @FXML
    private ComboBox<String> chooseCategory;
    
    @FXML
    private TextField emailAddress;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;
    
    @FXML
    private PasswordField password;
    
    @FXML
    private PasswordField confirmPassword;

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

    /**
     * Initializes the controller class.
     * adds the combobox options
     * set visible panes
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chooseStaffType.getItems().add("Administrative Staff");
        chooseStaffType.getItems().add("Medical Staff");
        choosePositionType.getItems().add("FullTime");
        choosePositionType.getItems().add("PartTime");
        choosePositionType.getItems().add("Volunteer");
        
        chooseCategory.getItems().add( "RegisteredNurse" );
        chooseCategory.getItems().add( "GeneralPractitioner" );
        chooseCategory.getItems().add( "Pharmacist" );
        
        choosePositionType.setVisible(false);
        medicalPane.setVisible(false);
        
        contactPane.setLayoutY(230);
    }
    
    // handles going back to the login page
    @FXML
    void processGoBack(ActionEvent event) {
        application.gotoLogin();
    }
    
    // handles adding of staff to the credentials Main.staff
    @FXML
    void registerStaff(ActionEvent event) {
        addStaff();
    }
    
    // handles showing of hidden panes
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
    
    // handles setting Main class instance to this class for access
    public void setApp(Main application){
        this.application = application;
    }
    
    // layout of the window if selected staff type is AdminStaff
    private void layoutAdmin() {
        choosePositionType.setVisible(true);
        contactPane.setLayoutY(275);
        
        application.getStage().setHeight(875); 
    }
    
    // layout of the window if selected staff type is MedicalStaff
    private void layoutMedical() {
        medicalPane.setVisible(true);
        medicalPane.setLayoutY(230);
        contactPane.setLayoutY(345);
        
        application.getStage().setHeight(945);
    }
    
    // method to add staff into the Main.staff
    private void addStaff() {
        selectedStaffType = chooseStaffType.getSelectionModel().getSelectedItem();
        
        try {
            checkFields();
            Authenticator.loadCredentials( username.getText(), password.getText() );
            switch( selectedStaffType ) {
                case "Administrative Staff" 
                        -> Main.addStaff( new AdminStaff( firstName.getText(), lastName.getText(), phoneNumber.getText(), emailAddress.getText(),
                                    username.getText(), password.getText(), streetAddress.getText(), suburb.getText(), state.getText(),
                                    choosePositionType.getSelectionModel().getSelectedItem() ));
                case "Medical Staff" 
                        -> Main.addStaff( new MedicalStaff( firstName.getText(), lastName.getText(), phoneNumber.getText(), emailAddress.getText(),
                                    username.getText(), password.getText(), streetAddress.getText(), suburb.getText(), state.getText(),
                                    registrationID.getText(), affiliation.getText(), chooseCategory.getSelectionModel().getSelectedItem() ));
            }
            registrationSuccess();
        }
        catch( IllegalArgumentException ex ) {
            registrationError();
        }
    }
    
    /*
        includes basic validation of input fields using Operations.valdateFields method
        includes validation of firstName, lastName without a digit
        includes validation of phoneNumber to be 10 digits
        includes validation of emailAddress to be of sample@sample.sample format
        includes validation whether username already exists
        includes validation whether passwords match
    */
    private void checkFields()
    {
        selectedStaffType = chooseStaffType.getSelectionModel().getSelectedItem();
        
        if( selectedStaffType != null ) {
            if( Operations.validateFields( detailsPane ) ) {
                if( !firstName.getText().matches(".*\\d.*") && !lastName.getText().matches(".*\\d.*") && phoneNumber.getText().matches( "^\\d{10}$" ) && emailAddress.getText().matches( "^(.+)@(.+)$" ) ) {
                    if( !Authenticator.usernameExists( username.getText() ) ) {
                        if( password.getText().equals( confirmPassword.getText() ) ) {
                            if( selectedStaffType.equals( "Administrative Staff" )){
                                if( choosePositionType.getSelectionModel().getSelectedItem() != null )
                                    return;
                            }
                            else{
                                if( Operations.validateFields( medicalPane ) )
                                    return;
                            }
                        }
                    }
                }
            }
        }
        
        throw new IllegalArgumentException( "One of the input fields have an invalid argument." );
    }
    
    // error showing when the input fields have something wrong with the inputs
    private void registrationError() {
        Alert alert = new Alert( Alert.AlertType.WARNING );
        alert.setTitle( "Some fields have invalid inputs" );
        alert.setHeaderText( "Problem with fields" );
        alert.setContentText( "Please check if all required fields are entered correctly." );
        alert.showAndWait();
    }
    
    // information showing when registration is successfull -> proceed to go back to login page.
    private void registrationSuccess() {
        Alert alert = new Alert( Alert.AlertType.INFORMATION );
        alert.setTitle( "Registration Success" );
        alert.setHeaderText( username.getText() + " successfully registered" );
        alert.setContentText( "Please go back to the login page by clicking 'ok'.\nIf you need to add another user, please register again." );
        alert.showAndWait();
        
        application.gotoLogin();
    }
}
