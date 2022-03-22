package mvms.controllers;

import entities.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import mvms.*;

/**
 *Assessment 2: Mass Vaccination Management System
 *      ReportsController class contains all methods related to accessing and
 *      displaying reports.
 *      By default, criteria will be "any" and displays everything that matches the parent
 *          query
 * 
 * @author DAgustin
 * 26 Jan 2022
 */
public class ReportsController implements Initializable
{
    // initialise unique variables to this class
    private Main application;
    private String selectedReport;
    private String selectedStaffType;
    private ObservableList<String> displayList = FXCollections.observableArrayList();
    private List<? extends Person> reportPeople;
    
    @FXML
    private ComboBox<String> categoryCriteria;

    @FXML
    private RadioButton checkAll;

    @FXML
    private RadioButton checkFirstDose;

    @FXML
    private RadioButton checkSecondDose;
    
    @FXML
    private TextField nameCriteria;

    @FXML
    private ComboBox<String> positionTypeCriteria;

    @FXML
    private ListView<String> results;

    @FXML
    private ComboBox<String> selectReport;

    @FXML
    private Pane staffPane;

    @FXML
    private ComboBox<String> staffTypeCriteria;

    @FXML
    private DatePicker vaccineDate;

    @FXML
    private Pane vaccinePane;
    
    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb)
    {
        vaccinePane.setVisible(false);
        staffPane.setVisible(false);
        positionTypeCriteria.setVisible(false);
        categoryCriteria.setVisible(false);
        
        selectReport.getItems().add("Staff Report");
        selectReport.getItems().add("Vaccine Recipient Report");
        
        staffTypeCriteria.getItems().add("Any Staff Type");
        staffTypeCriteria.getItems().add("Administrative Staff");
        staffTypeCriteria.getItems().add("Medical Staff");
        
        positionTypeCriteria.getItems().add("Any Position Type");
        positionTypeCriteria.getItems().add("FullTime");
        positionTypeCriteria.getItems().add("PartTime");
        positionTypeCriteria.getItems().add("Volunteer");
        
        categoryCriteria.getItems().add("Any Category");
        categoryCriteria.getItems().add("RegisteredNurse");
        categoryCriteria.getItems().add("GeneralPractitioner");
        categoryCriteria.getItems().add("Pharmacist");
        
        vaccineDate.setValue(LocalDate.now());
        
        // make radio buttons connected to each other through a togglegroup
        ToggleGroup radioToggle = new ToggleGroup();
        checkAll.setToggleGroup(radioToggle);
        checkFirstDose.setToggleGroup(radioToggle);
        checkSecondDose.setToggleGroup(radioToggle);
        
    }
    
    // sets the scene objects when a report type is seleted
    @FXML
    void onSelectReport(ActionEvent event) {
        vaccinePane.setVisible(false);
        staffPane.setVisible(false);
        
        selectedReport = selectReport.getSelectionModel().getSelectedItem();
        
        // depends on what kind of report is selected and requested
        // reportPeople is a generic list of Person
        switch(selectedReport) {
            case "Staff Report":
                staffPane.setVisible(true);
                staffTypeCriteria.getSelectionModel().selectFirst();
                reportPeople = Main.getStaff();
                break;
            case "Vaccine Recipient Report":
                vaccinePane.setVisible(true);
                checkAll.setSelected(true);
                reportPeople = Main.getVaccRecipient();
                break;
        }
    }
    
    // changes the layout of the scene if a staff type is selected. The pertinent fields will be shown for each staff type
    @FXML
    void onSelectStaffType(ActionEvent event) {
        positionTypeCriteria.setVisible(false);
        categoryCriteria.setVisible(false);
        
        selectedStaffType = staffTypeCriteria.getSelectionModel().getSelectedItem();
        
        switch(selectedStaffType) {
            case "Any Staff Type":
                break;
            case "Administrative Staff":
                positionTypeCriteria.setVisible(true);
                positionTypeCriteria.getSelectionModel().selectFirst();
                break;
            case "Medical Staff":
                categoryCriteria.setVisible(true);
                categoryCriteria.getSelectionModel().selectFirst();
                break;
        }
    }
    
    // display the list of matches in the listview on the right of the scene
    @FXML
    void displaySearch(ActionEvent event) {
        displayList.clear();
        
        try {
            checker();
            
            search();
            
        }
        catch(IllegalArgumentException ex) {
            showError();
        }
            
    }
    
    
    // clear the criteria and reset them to default "any" or date to current day
    @FXML
    void clearCriteria(ActionEvent event) {
        nameCriteria.clear();
        
        staffTypeCriteria.getSelectionModel().clearAndSelect(0);
        positionTypeCriteria.getSelectionModel().clearAndSelect(0);
        categoryCriteria.getSelectionModel().clearAndSelect(0);
        
        vaccineDate.setValue(LocalDate.now());
        checkAll.setSelected(true);
        
        results.getItems().clear();
        
    }
    
    // performs the search from the stored lists
    private void search() {
        switch(selectedReport) {
            case "Staff Report":
                // iterate through the staff list and if criteria is met, add to the display
                reportPeople.forEach(p -> {
                    if(staffCriteria(p)) {
                        displayList.add(p.toString());
                    }
                    
                });
                break;
            case "Vaccine Recipient Report":
                // iterate through the recipients and if criteria is met, add to the display
                // the list is sorted in reverse Last Name order
                reportPeople.sort(Comparator.comparing(Person::getLastName).reversed());
                reportPeople.forEach(p -> {
                    if(recipientCriteria(p)) {
                        displayList.add(p.toString());
                        
                    }
                });
                break;
        }
        
        results.setItems(displayList);
    }
    
    // sets the criteria for the staff search report. creates a boolean array of 3 values for each criteria
    private boolean staffCriteria(Person person) {
        Boolean criteria[] = new Boolean[3];
        
        // check if the name matches (always true if name field is left blank)
        String name = person.getFirstName() + " " + person.getLastName();
        if(name.toLowerCase().matches("^.*?(" + nameCriteria.getText().toLowerCase() + ").*?$")) {
            criteria[0] = true;
        }
        
        // checks the person if it matches any of the set criteria regarding staff type
        int staffTypeIndex = staffTypeCriteria.getSelectionModel().getSelectedIndex();
        switch (staffTypeIndex) {
            case 0:
                // for any
                criteria[1] = true;
                criteria[2] = true;
                break;
            case 1:
                // if staff type criteria is set to "Administrative Staff", only check if person is an admin staff.
                if(person instanceof AdminStaff) {
                    criteria[1] = true;
                    
                    // check if position type criteria is also met (or always true if "any" is selected)
                    String positionType = positionTypeCriteria.getSelectionModel().getSelectedItem();
                    if(positionType.equals("Any Position Type") || positionType.equals(((AdminStaff) person).getPositionType().name()))
                        criteria[2] = true;
                }   break;
            case 2:
                // if staff type criteria is set to "Medical Staff", only check if person is an medical staff.
                if(person instanceof MedicalStaff) {
                    criteria[1] = true;
                    
                    // check if category criteria is also met (or always true if "any" is selected)
                    String category = categoryCriteria.getSelectionModel().getSelectedItem();
                    if(category.equals("Any Category") || category.equals(((MedicalStaff) person).getCategory().name()))
                        criteria[2] = true;
                }   break;
            default:
                break;
        }
        
        // return false if there's a null in the array
        return !Arrays.asList(criteria).contains(null);
    }
    
    // sets the criteria for the recipient search report. creates a boolean array of 2 values for each criteria
    private boolean recipientCriteria(Person person) {
        VaccineRecipient recipient = (VaccineRecipient) person;
        Boolean criteria[] = new Boolean[2];
        
        // check if the name matches (always true if name field is left blank)
        String name = recipient.getFirstName() + " " + recipient.getLastName();
        if(name.toLowerCase().matches("^.*?(" + nameCriteria.getText().toLowerCase() + ").*?$")) {
            criteria[0] = true;
        }
        
        // check which radiobutton is selected and change array value to true only if a match
        if(checkAll.isSelected()) {
            if(getMatchDoseDate(recipient.getFirstVaccineDose(), vaccineDate.getValue()) ||
                    getMatchDoseDate(recipient.getSecondVaccineDose(), vaccineDate.getValue())) {
                criteria[1] = true;
            }
        }
        else if(checkFirstDose.isSelected()) {
            if(getMatchDoseDate(recipient.getFirstVaccineDose(), vaccineDate.getValue())) {
                criteria[1] = true;
            }
        }
        else if(checkSecondDose.isSelected()) {
            if(getMatchDoseDate(recipient.getSecondVaccineDose(), vaccineDate.getValue())) {
                criteria[1] = true;
            }
        }
        
        // return false if there's a null in the array
        return !Arrays.asList(criteria).contains(null);
    }
    
    // check if a vaccine dose matches a given date
    private boolean getMatchDoseDate(Vaccine vaccine, LocalDate date) {
        return vaccine.getVaccinationDate().equals(date);
    }
    
    // checker of valid fields - only if the report type is not selected
    private void checker() {
        
        if(selectedReport == null) {
            throw new IllegalArgumentException("Report type is not selected");
        }
            
    }
    
    // error showing when the input fields have something wrong with the inputs
    private void showError() {
        Alert alert = new Alert( Alert.AlertType.WARNING );
        alert.setTitle( "Problem with search critera" );
        alert.setHeaderText( "Some fields have blank or have invalid inputs." );
        alert.setContentText( "Please select a report type" );
        alert.showAndWait();
    }
}
