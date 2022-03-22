package mvms.controllers;

import entities.*;
import mvms.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Assessment 1: Mass Vaccination Management System
 *      RecipientController class contains all data and functions related to the recipient window
 * 
 * @author DAgustin
 * 03 Dec 2021
 */
public class RecipientController implements Initializable
{
    // initialise unique variables to this class
    private Staff currentUser;
    private ObservableList<String> recipientList = FXCollections.observableArrayList();
    private int listIndex = -1;
    private ArrayList<Integer> preservedIndex = new ArrayList<>();
    private int recipientID = -1;
    private String alertString;
    
    @FXML
    private Label administeredBy1;

    @FXML
    private Label administeredBy2;
    
    @FXML
    private DatePicker comboBirthday;

    @FXML
    private ComboBox<String> comboDose1;

    @FXML
    private ComboBox<String> comboDose2;

    @FXML
    private ComboBox<String> comboGender;

    @FXML
    private CheckBox confirmedDose1;

    @FXML
    private CheckBox confirmedDose2;

    @FXML
    private DatePicker doseDate1;

    @FXML
    private DatePicker doseDate2;

    @FXML
    private TextField emailAddress;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField phoneNumber;
    
    @FXML
    private TextField search;
    
    @FXML
    private Pane paneRecipient;
    
    @FXML
    private Button buttonAddRecord;
    
    @FXML
    private DatePicker vaccDate;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboGender.getItems().add( "Male" );
        comboGender.getItems().add( "Female" );
        comboGender.getItems().add( "Unspecified" );
        
        comboDose1.getItems().add( "AstraZeneca" );
        comboDose1.getItems().add( "Pfizer" );
        comboDose1.getItems().add( "Moderna" );
        
        comboDose2.getItems().add( "AstraZeneca" );
        comboDose2.getItems().add( "Pfizer" );
        comboDose2.getItems().add( "Moderna" );
        
        // add listerner if the text inside the search box changes
        search.textProperty().addListener( new ChangeListener<String>() {
            @Override
            public void changed( ObservableValue<? extends String> observable, String oldVal, String newVal ) {
                searchFunction( newVal );
            }
        });
                
        vaccDate.setValue(LocalDate.now());
        vaccDate.valueProperty().addListener((observable, oldVal, newVal) -> initIndexList());
    }
    
    // handle adding and updating records
    @FXML
    void saveRecord(ActionEvent event) {
        enterDetails();
        initIndexList();
    }

    // handles selection of an item in the listview
    @FXML
    void selectRecipient(MouseEvent event) {
        if(listView.getItems().isEmpty() || listView.getSelectionModel().getSelectedIndex() == -1)
            return;
                
        // get relative index of selected item ( from the Main.vaccRecipient arraylist )
        int index = preservedIndex.get( listView.getSelectionModel().getSelectedIndex() );
        if( index != listIndex ) {
            clear();
            listIndex = index;
            
            // should only proceed if selection is valid and if it changed
            if( !listView.getItems().isEmpty() && listIndex >= 0 ) {
                                
                VaccineRecipient v = Main.getVaccRecipient().get(listIndex);

                // set recipient information
                recipientID = v.getRecipientID();
                firstName.setText( v.getFirstName() );
                lastName.setText( v.getLastName() );
                phoneNumber.setText( v.getPhoneNumber() );
                emailAddress.setText( v.getEmailAddress() );

                comboGender.setValue( v.getGender().name() );
                comboBirthday.setValue( v.getDateOfBirth() );
                
                // set vaccine dose information
                if( v.getDoseCount() > 0 ) {

                    Vaccine vacc = v.getFirstVaccineDose();
                    
                    comboDose1.setValue( vacc.getVaccineName() );
                    doseDate1.setValue( vacc.getVaccinationDate() );
                                                            
                    if( vacc.getStatus() ) {
                        confirmedDose1.setSelected( true );
                        administeredBy1.setText( String.valueOf(vacc.getAdministeredBy()) );

                        comboDose1.setDisable(true);
                        doseDate1.setDisable(true);
                        confirmedDose1.setDisable(true);
                    }
                    else {
                        confirmedDose2.setDisable(true);
                    }

                    if( v.getDoseCount() == 2 ) {
                        vacc = v.getSecondVaccineDose();

                        comboDose2.setValue( vacc.getVaccineName() );
                        doseDate2.setValue( vacc.getVaccinationDate() );
                        if( vacc.getStatus() ) {
                            confirmedDose2.setSelected( true );
                            administeredBy2.setText( String.valueOf(vacc.getAdministeredBy()) );

                            comboDose2.setDisable(true);
                            doseDate2.setDisable(true);
                            confirmedDose2.setDisable(true);
                        }
                    }
                    // disables the nodes if already confirmed
                    else {
                        comboDose2.setDisable(false);
                        doseDate2.setDisable(false);
                        confirmedDose2.setDisable(false);
                    }
                }
                // disables the nodes if already confirmed
                else {
                    comboDose1.setDisable(false);
                    doseDate1.setDisable(false);
                    confirmedDose1.setDisable(false);
                }


                buttonAddRecord.setText( "Update Record" );
            }
        }
    }
    
    // handles the clearing of the nodes if the clearButton is clicked
    @FXML
    void clearSelection(ActionEvent event) {
        clear();
        search.clear();
        vaccDate.setValue(LocalDate.now());
    }
    
    // clear and reinitialise all fields 
    public void clear() {
        firstName.clear();
        lastName.clear();
        comboGender.setValue( "gender" );
        comboBirthday.setValue(null);
        phoneNumber.clear();
        emailAddress.clear();
        
        comboDose1.setValue( "Vaccine" );
        doseDate1.setValue( null );
        confirmedDose1.setSelected(false);
        administeredBy1.setText( "-----" );
        
        comboDose2.setValue( "Vaccine" );
        doseDate2.setValue( null );
        confirmedDose2.setSelected(false);
        administeredBy2.setText( "-----" );
        
        comboDose1.setDisable(false);
        doseDate1.setDisable(false);
        confirmedDose1.setDisable(false);
        comboDose2.setDisable(false);
        //doseDate2.setDisable(false);
        confirmedDose2.setDisable(false);
        
        buttonAddRecord.setText( "Add Record" );
                
        firstName.requestFocus();
        
        listIndex = -1;
        
    }
    
    // add the details from the input nodes
    public void enterDetails() {
        // try adding the entry into the listview and Main.vaccRecipient. show an error message
        // if the adding fails at any point
        try {
            checker();
            VaccineRecipient entry = new VaccineRecipient( firstName.getText(), lastName.getText(), phoneNumber.getText(), emailAddress.getText(), comboBirthday.getValue(), comboGender.getValue() );
            
            setVaccDoses(entry);
            
            if( listIndex >= 0 ) {
                Main.dbUtil.updateRecipient(entry, recipientID);
                System.out.println("Successfully updated recipient record");
                /*
                Main.setVaccRecipient(listIndex, entry);
                recipientList.set( listIndex, entry.getFirstName() + " " + entry.getLastName() );
                */
            }
            else {
                Main.dbUtil.addRecipient(entry);
                System.out.println("Successfully added recipient record");
                /*
                Main.addVaccineRecipient( entry );
                recipientList.add( entry.getFirstName() + " " + entry.getLastName() );
                */
            }
            
            Main.refreshRecipientList();
            initIndexList();
            clear();
        }
        catch( IllegalArgumentException | DateTimeParseException ex ) {
            showError();
        }
        catch (SQLException ex) {
            Logger.getLogger(RecipientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /* Does the validation of the fields in the pane plus 
    *  additional validation on some fields:
    *   - firstName and lastName should have no digits
    *   - phoneNumber should be 10 digits long
    *   - email should be in sample@sample.sample format
    */
    private void checker() {
        
        alertString = "Please check if all required fields are entered correctly.";
        
        if(firstName.getText().isBlank())
            alertString = alertString.concat("\n- First name field is blank");
        else if(firstName.getText().matches(".*\\d.*"))
            alertString = alertString.concat("\n- First name has invalid characters");
        
        if(lastName.getText().isBlank())
            alertString = alertString.concat("\n- Last name field is blank");
        else if(lastName.getText().matches(".*\\d.*"))
            alertString = alertString.concat("\n- Last name has invalid characters");
        
        if(phoneNumber.getText().isBlank())
            alertString = alertString.concat("\n- Phone number field is blank");
        else if(!phoneNumber.getText().matches("^\\d{10}$"))
            alertString = alertString.concat("\n- Phone number should be 10 digits");
        
        if(emailAddress.getText().isBlank())
            alertString = alertString.concat("\n- Email address field is blank");
        else if(!emailAddress.getText().matches("^(.+)@(.+)$"))
            alertString = alertString.concat("\n- Emailaddress should be in email@email.com format");
        
        if(!alertString.equalsIgnoreCase("Please check if all required fields are entered correctly."))
            throw new IllegalArgumentException( "One of the input fields have an invalid argument." );
    }
    
    // set logged in user in this class to initialise certain fields
    public void setLoggedUser() {
        currentUser = Main.getLoggedUser();            
        
        // admin staff cannot confirm vaccine doses
        if( currentUser.getClass().getSimpleName().equals( "AdminStaff" ) ) {
            buttonAddRecord.setText( "Add Record" );
            confirmedDose1.setVisible(false);
            confirmedDose2.setVisible(false);
            vaccDate.setVisible(false);
        }
        // medical staff cannot add or update recipient data
        else {
            paneRecipient.getChildren().forEach(node -> {
                node.setDisable(true);
            });
            
            buttonAddRecord.setText( "Confirm Vaccine Dose/s" );
        }
        
        initIndexList();

    }
    
    // set vaccine doses if the fields have an input - can still be changed until confirmed
    // does not check for doseCount since confirmation checkboxes are disabled if once confirmed
    private void setVaccDoses( VaccineRecipient vaccRecipient ) {
        if( !comboDose1.getSelectionModel().isEmpty() && !(doseDate1.getValue() == null) ) {
                vaccRecipient.SETFIRSTVACCDOSE( new Vaccine( comboDose1.getValue(), doseDate1.getValue() ) );
                if( confirmedDose1.isSelected() ) {
                    vaccRecipient.getFirstVaccineDose().setAdministeredBy( currentUser.getStaffID() );
                    vaccRecipient.getFirstVaccineDose().setConfirmed();
                }
        }
        if( !comboDose2.getSelectionModel().isEmpty() && !(doseDate2.getValue() == null) ) {
            vaccRecipient.SETSECONDVACCDOSE( new Vaccine( comboDose2.getValue(), doseDate2.getValue() ) );
            if( confirmedDose2.isSelected() ) {
                    vaccRecipient.getSecondVaccineDose().setAdministeredBy( currentUser.getStaffID() );
                    vaccRecipient.getSecondVaccineDose().setConfirmed();
                }
        }
    }
    
    // search function to update listview if the search textfield changes
    private void searchFunction( String newVal ) {
        
        ObservableList<String> copy = FXCollections.observableArrayList();
        preservedIndex.clear();
        for( String name : recipientList ) {
            if( (name.toLowerCase()).matches( "^.*?(" + newVal.toLowerCase() + ").*?$" ) ) {
                copy.add( name );
                preservedIndex.add( recipientList.indexOf( name ));
            }
        }
                
        listView.setItems(copy);
    }
    
    //initialise lists for search function
    private void initIndexList() {
        // initialise recipient list
        recipientList.clear();
        preservedIndex.clear();
        
        for( VaccineRecipient vaccRecipient : Main.getVaccRecipient() ) {
            // if the user is a medical staff, narrow down list to those who match set date
            if(currentUser.getClass().getSimpleName().equals("MedicalStaff")) {
                
                // only if the vaccine dose matches the date and currently not administered will we do any changes to them
                if(vaccRecipient.getFirstVaccineDose().getVaccinationDate().compareTo(vaccDate.getValue()) <= 0 && vaccRecipient.getFirstVaccineDose().getAdministeredBy() == 0) {
                    recipientList.add((String) (vaccRecipient.getFirstName() + " " + vaccRecipient.getLastName()));
                    preservedIndex.add( Main.getVaccRecipient().indexOf(vaccRecipient) );
                }
                // only if first vaccine is alrady confirmed we will consider the second doses
                if(vaccRecipient.getFirstVaccineDose().getAdministeredBy() > 0) {
                    if(vaccRecipient.getSecondVaccineDose().getVaccinationDate().compareTo(vaccDate.getValue()) <= 0 && vaccRecipient.getSecondVaccineDose().getAdministeredBy() == 0) {
                        recipientList.add((String) (vaccRecipient.getFirstName() + " " + vaccRecipient.getLastName()));
                        preservedIndex.add( Main.getVaccRecipient().indexOf(vaccRecipient) );
                    }
                }
            }
            else {
                // get everything
                recipientList.add( (String) (vaccRecipient.getFirstName() + " " + vaccRecipient.getLastName()) );
                preservedIndex.add( Main.getVaccRecipient().indexOf(vaccRecipient) );
            }
            
        }
        
        // set list to the listview
        listView.setItems(recipientList);
    }
    
    // error showing when the input fields have something wrong with the inputs
    private void showError() {
        Alert alert = new Alert( Alert.AlertType.WARNING );
        alert.setTitle( "Problem with the changed fields" );
        alert.setHeaderText( "Some fields have invalid inputs." );
        alert.setContentText( alertString );
        alert.showAndWait();
    }
    
}
