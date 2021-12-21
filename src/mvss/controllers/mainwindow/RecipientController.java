/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mvss.controllers.mainwindow;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import mvss.*;
import mvss.controllers.*;
import person.*;

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
        
        comboDose1.getItems().add( "Astrazeneca" );
        comboDose1.getItems().add( "Pfizer" );
        comboDose1.getItems().add( "Moderna" );
        
        comboDose2.getItems().add( "Astrazeneca" );
        comboDose2.getItems().add( "Pfizer" );
        comboDose2.getItems().add( "Moderna" );
        
        search.textProperty().addListener( new ChangeListener<String>() {
            @Override
            public void changed( ObservableValue<? extends String> observable, String oldVal, String newVal ) {
                System.out.println( newVal );
            }
        });
        
        for( VaccineRecipient vaccRecipient : Main.getVaccRecipient() )
        {
            recipientList.add( (String) (vaccRecipient.getFirstName() + " " + vaccRecipient.getLastName()) );
        }
        
        listView.setItems(recipientList);
        
    }
    
    public void setMainController( MainController mainController )
    {
        this.mainController = mainController;
    }
    
    @FXML
    void deleteRecord(ActionEvent event) {

    }

    @FXML
    void saveRecord(ActionEvent event) {
        //System.out.println(comboBirthday.getValue());
    }

    @FXML
    void selectRecipient(MouseEvent event) {

    }
    
    private boolean checkFields()
    {
        return !firstname.getText().isBlank() &&
                !lastname.getText().isBlank() &&
                !comboGender.getSelectionModel().isEmpty() &&
                !(comboBirthday.getValue() == null) &&
                !phoneNumber.getText().isBlank() &&
                !emailAddress.getText().isBlank() &&
                !comboDose1.getSelectionModel().isEmpty() &&
                !(comboDose1.getValue() == null);
    }
    
    public void setLoggedUser()
    {
        currentUser = Main.getLoggedUser();
        
        if( currentUser.getClass().getSimpleName().equals( "AdminStaff" ) )
        {
            buttonAddRecord.setText( "Add Record" );
        }
        else
        {
            paneRecipient.getChildren().forEach(node -> {
                node.setDisable(true);
            });
            
            buttonAddRecord.setText( "Confirm Vaccine Dose/s" );
            buttonDeleteRecord.setVisible(false);
        }
        
        if( confirmedDose1.isSelected() ) {
            comboDose2.setDisable(true);
            doseDate2.setDisable(true);
            confirmedDose2.setDisable(true);
            
            comboDose1.setEditable(false);
            doseDate1.setEditable(false);
            confirmedDose1.setDisable(true);
        }
    }
    
}
