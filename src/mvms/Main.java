package mvms;

import entities.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mvms.controllers.*;

/**
 * Assessment 1: Mass Vaccination Management System
 *      Main class to handle top level methods and global variables
 *          contains loading of dump.tmp, saving the dump.tmp and csv files upon termination of the program
 *          contains handling of changing scenes and controllers on the top level
 *              - login window
 *              - main window
 *              - registration window
 * 
 * @author DAgustin
 * 03 Dec 2021
 */
public class Main extends Application
{
    // initialise global variables
    private Stage stage;
    private static Staff LoggedUser;
    
    // arraylists containing the staff and recipients
    private static List<Staff> staff = new ArrayList<>();
    private static List<VaccineRecipient> vaccRecipient = new ArrayList<>();
    
    // main method
    public static void main( String[] args )
    {
        launch( args );
        
        // if saving fails, show in terminal that file was not saved
        try {
            // save arraylists to csv file before end of program
            Operations.saveFile( staff, "staff.csv" );
            Operations.saveFile( vaccRecipient, "vacc.csv" );
            System.out.println( "saved csv files" );

            // dump staff and vaccine recipient to tmp file
            List<?> dump = Stream.of( staff, vaccRecipient ).flatMap( Collection::stream ).collect( Collectors.toList() );
            Operations.dumpFile((List<Person>) dump);
            System.out.println("saved dump");
        }
        catch( IOException ex ) {
            System.out.println( "File not saved" );
        }
    }
    
    // start the application
    @Override
    public void start(Stage primaryStage) {
        // handle launch of application by going to the login page
        try {
            stage = primaryStage;
            stage.getIcons().add(new Image("/resources/png/qld icon.png"));
            stage.setTitle("Login - MVMS");
            
            initLists();    // initialise staff and vaccRecipient arraylists
            
            gotoLogin();
            stage.show();
            
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // handles loading of the login controller and fxml
    public void gotoLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent("/resources/login.fxml");
            login.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // handles loading of the main window controller and fxml
    public void gotoHome() {
        try {
            MainController main = (MainController) replaceSceneContent("/resources/main.fxml");
            main.setApp(this);
            
            // show in terminal the logged in usercredentials
            System.out.println("Logging in as " + LoggedUser.getUsername() );
        }
        catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // handles loading of the registration window controller and fxml
    public void gotoRegistration() {
        try {
            RegistrationController register = (RegistrationController) replaceSceneContent("/resources/registration.fxml");
            register.setApp(this);
        }
        catch (Exception ex ) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // method to replace the contents of the stage and scene, changing the window size and controller
    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader =  new FXMLLoader( getClass().getResource( fxml ));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }
    
    // initialise the staff and vacc recipient lists
    private static void initLists() {
        
        // ensures to add one of each staff type in case the loading try block below fails
        staff.add( new AdminStaff( "unspecified", "unspecified", "unspecified", "unspecified", "adminstaff", "password", "unspecified", "unspecified", "unspecified", "unspecified" ));
        staff.add( new MedicalStaff( "unspecified", "unspecified", "unspecified", "unspecified", "medicalstaff", "password", "unspecified", "unspecified", "unspecified", "unspecified", "unspecified", "unspecified" ));
        
        try {
            // initialize -> staff and vaccine recipients into respective arraylists
            staff = (List<Staff>) Operations.loadFile().get(0); 
            vaccRecipient = (List<VaccineRecipient>) Operations.loadFile().get(1);
        }
        catch (IOException ex) {
            
            noUserError();  // will not be shown due to the two staff added above
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // if there is no dump file found in the files folder, show error.
    private static void noUserError() {
        Alert alert = new Alert( Alert.AlertType.WARNING );
        alert.setTitle( "No user detected" );
        alert.setHeaderText( "No user detected" );
        alert.setContentText( "Please register for a new staff by clicking on the \"New Staff? Register\" link." );
        alert.showAndWait();
    }
    
    /**
     * Get methods
     *      returns the various variables associated with this class
     * 
     * @return stage, LoggedUser, staff, vaccRecipient
     */
    
    public Stage getStage()
    {
        return stage;
    }

    public static Staff getLoggedUser()
    {
        return LoggedUser;
    }

    public static List<Staff> getStaff()
    {
        return staff;
    }

    public static List<VaccineRecipient> getVaccRecipient()
    {
        return vaccRecipient;
    }
    
    /**
     * Set methods
     *      enables the modification of the class variables
     */
    
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public static void setLoggedUser(String username)
    {
        Main.LoggedUser = Operations.getUser( username );
    }

    public static void setStaff(List<Staff> staff)
    {
        Main.staff = staff;
    }
    
    // overloaded method to accept setting staff at an index
    public static void setStaff( int staffIndex, Staff staff )
    {
        Main.staff.set(staffIndex, staff);
    }
    
    public static void setVaccRecipient(List<VaccineRecipient> vaccRecipient)
    {
        Main.vaccRecipient = vaccRecipient;
    }
    
    // overloaded method to accept setting recipient at an index
    public static void setVaccRecipient( int recipientIndex, VaccineRecipient vaccRecipient )
    {
        Main.vaccRecipient.set(recipientIndex, vaccRecipient);
    }
    
    // method to add a new entry into the staff list
    public static void addStaff( Staff staff )
    {
        Main.staff.add(staff);
    }
    
    // method to add a new entry into the vaccRecipient list
    public static void addVaccineRecipient( VaccineRecipient vaccRecipient )
    {
        Main.vaccRecipient.add(vaccRecipient);
    }
    
    // method to remove a recipient
    public static void removeVaccineRecipient( int recipientIndex )
    {
        Main.vaccRecipient.remove( recipientIndex );
    }
    
}
