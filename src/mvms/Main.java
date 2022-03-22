package mvms;

import entities.*;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public static boolean noUser = false;
    
    // arraylists containing the staff and recipients
    private static LinkedList<Staff> staff = new LinkedList<>();
    private static LinkedList<VaccineRecipient> vaccRecipient = new LinkedList<>();
    
    public static DatabaseUtility dbUtil;
    
    // main method
    public static void main( String[] args )
    {
        dbUtil = new DatabaseUtility();
        launch( args );
        
    }
    
    // start the application
    @Override
    public void start(Stage primaryStage) {
        // handle launch of application by going to the login page
        try {
            stage = primaryStage;
            stage.getIcons().add(new Image("/resources/png/qld icon.png"));
            stage.setTitle("Login - MVMS");
            stage.setResizable(false);
            
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
        
        try {
            staff = dbUtil.getStaffList();
            vaccRecipient = dbUtil.getRecipientList();
            
            for(Staff s : staff) {
                Authenticator.loadCredentials(s);
            }
                      
            if(staff.isEmpty())
                noUser = true;
        }
        catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
        Main.LoggedUser = Authenticator.getUser( username );
    }

    // method to refresh staff list from SQL database
    public static void refreshStaffList()
    {
        try {
            Main.staff = dbUtil.getStaffList();
        }
        catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // method to refresh recipient list from SQL database
    public static void refreshRecipientList()
    {
        try {
            Main.vaccRecipient = dbUtil.getRecipientList();
        }
        catch(SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
