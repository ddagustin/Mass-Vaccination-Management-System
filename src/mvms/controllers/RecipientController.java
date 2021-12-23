package mvms.controllers;

import entities.*;
import mvms.*;
import java.net.URL;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
    
    @FXML
    private Label administeredBy1;

    @FXML
    private Label administeredBy2;
    
    @FXML
    private Label doseID1;
    
    @FXML
    private Label doseID2;
    
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
    private Button buttonDeleteRecord;
    
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
        
        initIndexList();
    }
    
    // method ot handle clicking on the deleteRecord button
    // remove from both list view and Main.vaccRecipient arraylist
    @FXML
    void deleteRecord(ActionEvent event) {
        recipientList.remove( listIndex );
        Main.removeVaccineRecipient( listIndex );
        
        listView.setItems( recipientList );
                
        clear();
        search.clear();
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
        // get relative index of selected item ( from the Main.vaccRecipient arraylist )
        int index = preservedIndex.get( listView.getSelectionModel().getSelectedIndex() );
        if( index != listIndex ) {
            clear();
            listIndex = index;
            
            // should only proceed if selection is valid and if it changed
            if( !listView.getItems().isEmpty() && listIndex >= 0 ) {
                VaccineRecipient v = Main.getVaccRecipient().get(listIndex);

                // set recipient information
                firstName.setText( v.getFirstName() );
                lastName.setText( v.getLastName() );
                phoneNumber.setText( v.getPhoneNumber() );
                emailAddress.setText( v.getEmailAddress() );

                comboGender.setValue( v.getGender().name() );
                comboBirthday.setValue( v.getDateOfBirth() );
                
                // set vaccine dose information
                if( v.getDoseCount() > 0 ) {

                    Vaccine vacc = v.getFirstVaccineDose();
                    
                    doseID1.setText( vacc.getVaccineID() );
                    comboDose1.setValue( vacc.getVaccineName().name() );
                    doseDate1.setValue( vacc.getVaccinationDate() );
                                                            
                    if( vacc.getStatus() ) {
                        confirmedDose1.setSelected( true );
                        administeredBy1.setText( vacc.getAdministeredBy() );

                        comboDose1.setDisable(true);
                        doseDate1.setDisable(true);
                        confirmedDose1.setDisable(true);
                    }

                    if( v.getDoseCount() == 2 ) {
                        vacc = v.getSecondVaccineDose();

                        doseID2.setText( vacc.getVaccineID() );
                        comboDose2.setValue( vacc.getVaccineName().name() );
                        doseDate2.setValue( vacc.getVaccinationDate() );
                        if( vacc.getStatus() ) {
                            confirmedDose2.setSelected( true );
                            administeredBy2.setText( vacc.getAdministeredBy() );

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
                buttonDeleteRecord.setDisable( false );
            }
        }
    }
    
    // handles the clearing of the nodes if the clearButton is clicked
    @FXML
    void clearSelection(ActionEvent event) {
        clear();
        search.clear();
    }
    
    // clear and reinitialise all fields 
    public void clear() {
        firstName.clear();
        lastName.clear();
        comboGender.setValue( "gender" );
        comboBirthday.setValue(null);
        phoneNumber.clear();
        emailAddress.clear();
        
        doseID1.setText( "----------" );
        comboDose1.setValue( "Vaccine" );
        doseDate1.setValue( null );
        confirmedDose1.setSelected(false);
        administeredBy1.setText( "-----" );
        
        doseID2.setText( "----------" );
        comboDose2.setValue( "Vaccine" );
        doseDate2.setValue( null );
        confirmedDose2.setSelected(false);
        administeredBy2.setText( "-----" );
        
        comboDose1.setDisable(false);
        doseDate1.setDisable(false);
        confirmedDose1.setDisable(false);
        comboDose2.setDisable(false);
        doseDate2.setDisable(false);
        confirmedDose2.setDisable(false);
        
        buttonAddRecord.setText( "Add Record" );
        buttonDeleteRecord.setDisable(true);
                
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
                Main.setVaccRecipient(listIndex, entry);
                recipientList.set( listIndex, entry.getFirstName() + " " + entry.getLastName() );
            }
            else {
                Main.addVaccineRecipient( entry );
                recipientList.add( entry.getFirstName() + " " + entry.getLastName() );
            }

            clear();
        }
        catch( IllegalArgumentException | DateTimeParseException ex ) {
            showError();
        }
    }
    
    /* utilises the Operations class to validate the fields in the pane
    * additional validation on some fields:
    *   - firstName and lastName should have no digits
    *   - phoneNumber should be 10 digits long
    *   - email should be in sample@sample.sample format
    */
    private void checker() {
        if( Operations.validateFields( paneRecipient ) ) {
            if( !firstName.getText().matches( ".*\\d.*" ) && !lastName.getText().matches( ".*\\d.*" ) && phoneNumber.getText().matches( "^\\d{10}$" ) && emailAddress.getText().matches( "^(.+)@(.+)$" ) ) {
                return;
            }
        }
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
        }
        // medical staff cannot add or update recipient data
        else {
            paneRecipient.getChildren().forEach(node -> {
                node.setDisable(true);
            });
            
            buttonAddRecord.setText( "Confirm Vaccine Dose/s" );
            buttonDeleteRecord.setVisible(false);
        }
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
        
        for( VaccineRecipient vaccRecipient : Main.getVaccRecipient() ) {
            recipientList.add( (String) (vaccRecipient.getFirstName() + " " + vaccRecipient.getLastName()) );
            preservedIndex.add( Main.getVaccRecipient().indexOf(vaccRecipient) );
        }
        
        listView.setItems(recipientList);
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
