package mvms.controllers;

import entities.*;
import mvms.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private String alertString;
    
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
            case "Administrative Staff":
                layoutAdmin();
                break;
            case "Medical Staff":
                layoutMedical();
                break;
        }
        
    }
    
    // handles setting Main class instance to this class for access
    public void setApp(Main application){
        this.application = application;
    }
    
    // layout of the window if selected staff type is AdminStaff
    private void layoutAdmin() {
        choosePositionType.setVisible(true);        
        //application.getStage().setHeight(400); 
    }
    
    // layout of the window if selected staff type is MedicalStaff
    private void layoutMedical() {
        medicalPane.setVisible(true);
        medicalPane.setLayoutY(160);
        
        //application.getStage().setHeight(400);
    }
    
    // method to add staff into the Main.staff
    private void addStaff() {
        selectedStaffType = chooseStaffType.getSelectionModel().getSelectedItem();
        
        try {
            checkFields();
            Authenticator.loadCredentials( username.getText(), password.getText() );
            switch( selectedStaffType ) {
                case "Administrative Staff":
                    AdminStaff adminStaff = new AdminStaff(firstName.getText(), lastName.getText(), phoneNumber.getText(), emailAddress.getText(),
                                    username.getText(), password.getText(), streetAddress.getText(), suburb.getText(), state.getText(),
                                    choosePositionType.getSelectionModel().getSelectedItem());
                    Main.dbUtil.addAdminStaff(adminStaff);
                    Main.refreshStaffList();
                    break;
                case "Medical Staff":
                    MedicalStaff medStaff = new MedicalStaff(firstName.getText(), lastName.getText(), phoneNumber.getText(), emailAddress.getText(),
                                    username.getText(), password.getText(), streetAddress.getText(), suburb.getText(), state.getText(),
                                    registrationID.getText(), affiliation.getText(), chooseCategory.getSelectionModel().getSelectedItem());
                    Main.dbUtil.addMedicalStaff(medStaff);
                    Main.refreshStaffList();
                    break;
            }
            registrationSuccess();
        }
        catch (SQLException ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalArgumentException ex1) {
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
        alertString = "Please check if all required fields are entered correctly.";
        selectedStaffType = chooseStaffType.getSelectionModel().getSelectedItem();
        
        if(selectedStaffType == null)
            alertString = alertString.concat("\n- Please select a staff type");
        else {
            if(selectedStaffType.equals("Administrative Staff")) {
                if(choosePositionType.getSelectionModel().getSelectedItem() == null)
                    alertString = alertString.concat("\n- Choose a position type!");
            } 
            else if(selectedStaffType.equals("Medical Staff")) {
                if(registrationID.getText().isBlank())
                    alertString = alertString.concat("\n- Registration ID field is blank");
                if(affiliation.getText().isBlank())
                    alertString = alertString.concat("\n- Affiliation field is blank");
                if(chooseCategory.getSelectionModel().getSelectedItem() == null)
                    alertString = alertString.concat("\n- Category is not chosen");
            }  
        }
        
        if(firstName.getText().isBlank())
            alertString = alertString.concat("\n- First name field is blank");
        else if(firstName.getText().matches(".*\\d.*"))
            alertString = alertString.concat("\n- First name has invalid characters");
        
        if(lastName.getText().isBlank())
            alertString = alertString.concat("\n- Last name field is blank");
        else if(lastName.getText().matches(".*\\d.*"))
            alertString = alertString.concat("\n- Last name has invalid characters");
        
        if(streetAddress.getText().isBlank())
            alertString = alertString.concat("\n- Street address field is blank");
        if(suburb.getText().isBlank())
            alertString = alertString.concat("\n- Suburb address field is blank");
        if(state.getText().isBlank())
            alertString = alertString.concat("\n- State address field is blank");
        
        if(phoneNumber.getText().isBlank())
            alertString = alertString.concat("\n- Phone number field is blank");
        else if(!phoneNumber.getText().matches("^\\d{10}$"))
            alertString = alertString.concat("\n- Phone number should be 10 digits");
        
        if(emailAddress.getText().isBlank())
            alertString = alertString.concat("\n- Email address field is blank");
        else if(!emailAddress.getText().matches("^(.+)@(.+)$"))
            alertString = alertString.concat("\n- Emailaddress should be in email@email.com format");
        
        if(username.getText().isBlank())
            alertString = alertString.concat("\n- Username field is blank");
        else if(Authenticator.usernameExists(username.getText()))
            alertString = alertString.concat("\n- Username already exists!");
        if(password.getText().isBlank())
            alertString = alertString.concat("\n- Passsword field is blank");
        if(confirmPassword.getText().isBlank())
            alertString = alertString.concat("\n- Confirm passsword field is blank");
        else if(!password.getText().equals(confirmPassword.getText()))
            alertString = alertString.concat("\n- Passwords don't match!");
        
        if(!alertString.equalsIgnoreCase("Please check if all required fields are entered correctly."))
            throw new IllegalArgumentException( "One of the input fields have an invalid argument." );
    }
    
    // error showing when the input fields have something wrong with the inputs
    private void registrationError() {
        Alert alert = new Alert( Alert.AlertType.WARNING );
        alert.setTitle( "Problem with Registration Form" );
        alert.setHeaderText( "Some fields have invalid inputs." );
        alert.setContentText(alertString);
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
