package mvms.controllers;

import entities.AdminStaff;
import entities.MedicalStaff;
import mvms.Authenticator;
import mvms.Main;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import mvms.Operations;

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
    private Pane detailsPane;
    
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
        
        try {
            checkFields();
            System.out.println( "true" );
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
            
            registrationSuccess();
        }
        catch( IllegalArgumentException ex ) {
            registrationError();
        }
    }
    
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
    
    private void registrationError() {
        Alert alert = new Alert( Alert.AlertType.WARNING );
        alert.setTitle( "Some fields have invalid inputs" );
        alert.setHeaderText( "Problem with fields" );
        alert.setContentText( "Please check if all required fields are entered correctly." );
        alert.showAndWait();
    }
    
    private void registrationSuccess() {
        Alert alert = new Alert( Alert.AlertType.INFORMATION );
        alert.setTitle( "Registration Success" );
        alert.setHeaderText( username.getText() + " successfully registered" );
        alert.setContentText( "Please go back to the login page by clicking 'ok'.\nIf you need to add another user, please register again." );
        alert.showAndWait();
        
        application.gotoLogin();
    }
}
