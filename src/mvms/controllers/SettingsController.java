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
public class SettingsController implements Initializable
{
    // initialise unique variables to this class
    private Staff currentUser;
    private int currentUserIndex;
    
    @FXML
    private TextField affiliation;

    @FXML
    private TextField category;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField emailAddress;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private PasswordField password;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField positionType;

    @FXML
    private TextField registrationID;

    @FXML
    private TextField staffType;

    @FXML
    private TextField state;

    @FXML
    private TextField streetAddress;

    @FXML
    private TextField suburb;

    @FXML
    private TextField username;
    
    @FXML
    private Pane medicalPane;
    
    @FXML
    private Pane detailsPane;
    
    @FXML
    private Pane passwordPane;
    
    @FXML
    private Label staffID;
    
    @FXML
    private Button buttonChangePassword;
    
    @FXML
    private Button buttonChangeDetails;
    
    @FXML
    private Button buttonCancelDetailChanges;

    @FXML
    private Button buttonCancelPasswordChanges;

    /**
     * Initializes the controller class.
     * set visible nodes
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        password.setVisible(false);
        confirmPassword.setVisible(false);
    }    
    
    // handle changing of only the basic user details
    @FXML
    void changeDetails(ActionEvent event) {
        if( buttonChangeDetails.getText().equals( "Update Details" )) {
            setUserDetails( currentUser, true );
            buttonChangeDetails.setText( "Save User Details" );
        }
        else {
            try {
                // validate fields and inputs
                userStringCheck();
                currentUser.setFirstName( firstName.getText() );
                currentUser.setLastName( lastName.getText() );
                currentUser.setPhoneNumber( phoneNumber.getText() );
                currentUser.setEmailAddress( emailAddress.getText() );
                currentUser.setStreetAddress( streetAddress.getText() );
                currentUser.setSuburbAddress( suburb.getText() );
                currentUser.setStateAddress( state.getText() );
            
                Main.setStaff( currentUserIndex, currentUser );
            
                setUserDetails( currentUser, false );
                buttonChangeDetails.setText( "Update Details" );
            }
            catch( IllegalArgumentException ex ) {
                showError();
            }
        }
        
    }
    
    // handle changing of username password - separate from above method
    @FXML
    void changePassword(ActionEvent event) {
        
        if( buttonChangePassword.getText().equals( "Change Password" ))
        {
            setUsername( currentUser, true );
            buttonChangePassword.setText( "Save Account Details" );
        }
        else {
            try {
                passwordStringCheck();
                Authenticator.removeCredentials(currentUser);
                
                currentUser.setUsername( username.getText() );
                currentUser.setPassword( password.getText() );
            
                Main.setStaff( currentUserIndex, currentUser );
            
                Authenticator.loadCredentials(currentUser);
            
                setUsername( currentUser, false );
                buttonChangePassword.setText( "Change Password" );
            }
            catch( IllegalArgumentException ex )
            {
                showError();
            }
        }
    }
    
    // cancel basic details changes and reinitialise fields
    @FXML
    void cancelDetailChanges(ActionEvent event) {
        setUserDetails( currentUser, false );
        buttonChangeDetails.setText( "Update Details" );
        firstName.requestFocus();
    }

    // cancel username and passsword changes and reinitialise fields
    @FXML
    void cancelPasswordChanges(ActionEvent event) {
        setUsername( currentUser, false );
        buttonChangePassword.setText( "Change Password" );
        username.requestFocus();
    }
    
    // to populate fields using the current logged in staff information
    public void setLoggedUser() {
        currentUser = Main.getLoggedUser();
        currentUserIndex = Main.getStaff().indexOf( currentUser );
        
        setUserDetails( currentUser, false );
        setUsername( currentUser, false );
        
        staffType.setText( currentUser.getClass().getSimpleName() );
        
        // check staff type and change layout accordingly
        if( currentUser.getClass().getSimpleName().equals( "AdminStaff" )) {
            medicalPane.setVisible(false);
            positionType.setText( ((AdminStaff) currentUser).getPositionType().name() );
        }
        else {
            positionType.setVisible(false);
            registrationID.setText( ((MedicalStaff) currentUser).getRegistrationNumber()  );
            affiliation.setText( ((MedicalStaff) currentUser).getAffiliation() );
            category.setText( ((MedicalStaff) currentUser).getCategory().name() ); 
        }
    }
    
    // populate basic details fields
    // mode controls editability if in updating mode or view mode
    private void setUserDetails( Staff user, boolean mode )
    {
        // setting text to loggeduser details
        firstName.setText( user.getFirstName() );
        lastName.setText( user.getLastName() );
        streetAddress.setText( user.getStreetAddress() );
        suburb.setText( user.getSuburbAddress() );
        state.setText( user.getStateAddress() );
        emailAddress.setText( user.getEmailAddress() );
        phoneNumber.setText( user.getPhoneNumber() );
        staffID.setText( user.getStaffID() );
        
        // editable fields
        firstName.setEditable(mode);
        lastName.setEditable(mode);
        streetAddress.setEditable(mode);
        suburb.setEditable(mode);
        state.setEditable(mode);
        emailAddress.setEditable(mode);
        phoneNumber.setEditable(mode);
        
        buttonCancelDetailChanges.setDisable(!mode);
    }
    
    // populate username and password fields
    // mode controls editability if in updating mode or view mode
    private void setUsername( Staff user, boolean mode )
    {
        username.setText( user.getUsername() );
        
        password.setVisible(mode);
        confirmPassword.setVisible(mode);
            
        username.setEditable(mode);
        password.setEditable(mode);
        confirmPassword.setEditable(mode);
                        
        buttonCancelPasswordChanges.setDisable(!mode);
    }
    
    /*
        validates basic user details using Operations.valdateFields method
        additional validation on some fields:
        - firstName and lastName should have no digits
        - phoneNumber should be 10 digits long
        - email should be in sample@sample.sample format
    */ 
    private void userStringCheck()
    {
        if( Operations.validateFields( detailsPane ) ) {
            if( !firstName.getText().matches(".*\\d.*") && !lastName.getText().matches(".*\\d.*") && phoneNumber.getText().matches( "^\\d{10}$" ) && emailAddress.getText().matches( "^(.+)@(.+)$" ) ) {
                return;
            }
        }
        throw new IllegalArgumentException( "One of the input fields have an invalid argument." );
    }
    
    /*
        validates basic user details using Operations.valdateFields method
        additional validation on some fields:
        - passwords should match
        - username should be unique
    */ 
    private void passwordStringCheck()
    {
        if( Operations.validateFields( passwordPane ) ) {
            if( !Authenticator.usernameExists( username.getText() ) ) {
                if( password.getText().equals(confirmPassword.getText()) )
                    return;
            }
        }
        throw new IllegalArgumentException( "One of the input fields have an invalid argument." );
    }
    
    // error showing when the input fields have something wrong with the inputs
    private static void showError() {
        Alert alert = new Alert( Alert.AlertType.WARNING );
        alert.setTitle( "One or more fields have a problem" );
        alert.setHeaderText( "Please check input fields" );
        alert.setContentText( "Please check all blanks or invalid field inputs." );
        alert.showAndWait();
    }
    
}
