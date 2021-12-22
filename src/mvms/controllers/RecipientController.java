/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mvms.controllers;

import entities.*;
import mvms.Main;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import mvms.Operations;

/**
 * FXML Controller class
 *
 * @author DR
 */
public class RecipientController implements Initializable
{
    private MainController mainController;
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
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField phoneNumber;

    @FXML
    private AnchorPane recipientPane;

    @FXML
    private TextField search;
    
    @FXML
    private Pane paneDose1;

    @FXML
    private Pane paneDose2;
    
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
    public void initialize(URL url, ResourceBundle rb)
    {
        comboGender.getItems().add( "Male" );
        comboGender.getItems().add( "Female" );
        comboGender.getItems().add( "Unspecified" );
        
        comboDose1.getItems().add( "AstraZeneca" );
        comboDose1.getItems().add( "Pfizer" );
        comboDose1.getItems().add( "Moderna" );
        
        comboDose2.getItems().add( "AstraZeneca" );
        comboDose2.getItems().add( "Pfizer" );
        comboDose2.getItems().add( "Moderna" );
        
        search.textProperty().addListener( new ChangeListener<String>() {
            @Override
            public void changed( ObservableValue<? extends String> observable, String oldVal, String newVal ) {
                searchFunction( newVal );
            }
        });
        
        for( VaccineRecipient vaccRecipient : Main.getVaccRecipient() )
        {
            recipientList.add( (String) (vaccRecipient.getFirstName() + " " + vaccRecipient.getLastName()) );
            preservedIndex.add( Main.getVaccRecipient().indexOf(vaccRecipient) );
        }
                
        System.out.println( preservedIndex );
    }
    
    public void setMainController( MainController mainController )
    {
        this.mainController = mainController;
    }
    
    @FXML
    void deleteRecord(ActionEvent event) {
        recipientList.remove( listIndex );
        Main.removeVaccineRecipient( listIndex );
        
        listView.setItems( recipientList );
                
        clear();
    }

    @FXML
    void saveRecord(ActionEvent event) {
        enterDetails();
    }

    @FXML
    void selectRecipient(MouseEvent event) {
        int index = preservedIndex.get( listView.getSelectionModel().getSelectedIndex() );
        if( index != listIndex ) {
            clear();
            listIndex = index;
            if( !listView.getItems().isEmpty() && listIndex >= 0 ) {
                VaccineRecipient v = Main.getVaccRecipient().get(listIndex);

                firstname.setText( v.getFirstName() );
                lastname.setText( v.getLastName() );
                phoneNumber.setText( v.getPhoneNumber() );
                emailAddress.setText( v.getEmailAddress() );

                comboGender.setValue( v.getGender().name() );
                comboBirthday.setValue( v.getDateOfBirth() );
                
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
                    else {
                        comboDose2.setDisable(false);
                        doseDate2.setDisable(false);
                        confirmedDose2.setDisable(false);
                    }
                }
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
    
    @FXML
    void clearSelection(ActionEvent event) {
        clear();
    }
    
    public void clear()
    {
        firstname.clear();
        lastname.clear();
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
                
        firstname.requestFocus();
        
        listIndex = -1;
        
    }
    
    public void enterDetails()
    {
        try {
            checker();
            VaccineRecipient entry = new VaccineRecipient( firstname.getText(), lastname.getText(), phoneNumber.getText(), emailAddress.getText(), comboBirthday.getValue(), comboGender.getValue() );
            
            setVaccDoses(entry);
            
            if( listIndex >= 0 ) {
                Main.setVaccRecipient(listIndex, entry);
                recipientList.set( listIndex, entry.getFirstName() + " " + entry.getLastName() );
            }
            else {
                Main.addVaccineRecipient( entry );
                recipientList.add( entry.getFirstName() + " " + entry.getLastName() );
            }

            listView.setItems( recipientList );            
            listView.getSelectionModel().clearSelection();
            
            clear();
        }
        catch( IllegalArgumentException | DateTimeParseException ex ) {
            showError();
        }
    }
    
    private void checker()
    {
        if( Operations.validateFields( paneRecipient ) ) {
            System.out.println( "Error" );
            if( !firstname.getText().matches( ".*\\d.*" ) && !lastname.getText().matches( ".*\\d.*" ) && phoneNumber.getText().matches( "^\\d{10}$" ) && emailAddress.getText().matches( "^(.+)@(.+)$" ) ) {
                return;
            }
        }
        throw new IllegalArgumentException( "One of the input fields have an invalid argument." );
    }
    
    public void setLoggedUser()
    {
        currentUser = Main.getLoggedUser();
        
        if( currentUser.getClass().getSimpleName().equals( "AdminStaff" ) )
        {
            buttonAddRecord.setText( "Add Record" );
            confirmedDose1.setVisible(false);
            confirmedDose2.setVisible(false);
        }
        else
        {
            paneRecipient.getChildren().forEach(node -> {
                node.setDisable(true);
            });
            
            buttonAddRecord.setText( "Confirm Vaccine Dose/s" );
            buttonDeleteRecord.setVisible(false);
        }
    }
    
    private void setVaccDoses( VaccineRecipient vaccRecipient )
    {
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
    
    private void searchFunction( String newVal ) {
        
        ObservableList<String> copy = FXCollections.observableArrayList();
        preservedIndex.clear();
        for( String name : recipientList )
        {
            if( (name.toLowerCase()).matches( "^.*?(" + newVal.toLowerCase() + ").*?$" ) ) {
                copy.add( name );
                preservedIndex.add( recipientList.indexOf( name ));
            }
        }
                
        listView.setItems(copy);
    }
    
    private static void showError() {
        Alert alert = new Alert( Alert.AlertType.WARNING );
        alert.setTitle( "One or more fields have a problem" );
        alert.setHeaderText( "Please check input fields" );
        alert.setContentText( "Please check all blanks or invalid field inputs." );
        alert.showAndWait();
    }
    
}
