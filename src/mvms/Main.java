package mvms;

import entities.*;
import java.io.IOException;
import java.time.LocalDate;
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
 * Assessment 1: Mass Vaccination System
 * @author DAgustin
 * 03 Dec 2021
 */
public class Main extends Application
{
    private Stage stage;
    private static Staff LoggedUser;
    
    private static List<Staff> staff = new ArrayList<>();
    private static List<VaccineRecipient> vaccRecipient = new ArrayList<>();
    
    public static void main( String[] args ) throws IOException
    {
        //vaccRecipient.add( new VaccineRecipient( "unspecified", "unspecified", "unspecified", "unspecified", LocalDate.of(2021,05,29), "unspecified" ) );
        
        // launch app
        launch( args );
        
        
        /*
        staff.add( new AdminStaff( "Daniel", "Agustin", "0431133559", "danielroi.agustin@gmail.com", "ddagustin", "D@nnroi0529", "28 Viney St", "Gracemere", "QLD", "FullTime" ));
        staff.add( new MedicalStaff( "Daniel", "Agustin", "0431133559", "danielroi.agustin@gmail.com", "ddagustin", "D@nnroi0529", "28 Viney St", "Gracemere", "QLD", "2011-44869", "Woolies", "GeneralPracitioner" ));
        
        //staff.forEach(currentStaff -> {
        //    System.out.println( currentStaff );
        //});
        
        Operations.saveFile( staff, "staff.csv" );
        
        List< VaccineRecipient > vaccine = new ArrayList<>();
        vacc.add( new VaccineRecipient( "Mat", "Field", "123", "example@gmail.com", LocalDate.of(2021,05,29), "Male" ) );
        
        Operations.saveFile( vacc, "vacc.csv" );
        
        List<String[]> uniqueID = new ArrayList<>();
        uniqueID = Operations.getListID( "src/files/staff.csv" );
        
        
        */
        /*
        for( int i = 0; i < 2; i++ ) {
            System.out.print( uniqueID.get(i) + " " );
            System.out.println( staff.get(i).getStaffID() );
            System.out.println( uniqueID.get(i).equals(staff.get(i).getStaffID()) );
            
        }
        */
        
        /*
        for( String[] content : uniqueID ) {
            //System.out.println( content );
            //System.out.println( content[1] );
            
            if( content[0].equals("AdmminStaff") ) {
                
            }
            else {
                
            }
            
        }
        */
        
        // save arraylists to csv file before end of program
        Operations.saveFile( staff, "staff.csv" );
        Operations.saveFile( vaccRecipient, "vacc.csv" );
        
        // dump staff and vaccine recipient to tmp file
        List<?> dump = Stream.of( staff, vaccRecipient ).flatMap( Collection::stream ).collect( Collectors.toList() );
        System.out.println("loaded dump");
        Operations.dumpFile((List<Person>) dump);
        System.out.println("saved dump");
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;
            stage.getIcons().add(new Image("/resources/png/qld icon.png"));
            stage.setTitle("Login - MVMS");
            initLists();
            
            gotoLogin();
            stage.show();
            
            
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void gotoLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent("/resources/login.fxml");
            login.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void gotoHome() {
        try {
            MainController main = (MainController) replaceSceneContent("/resources/main.fxml");
            main.setApp(this);
            System.out.println("Logging in as " + LoggedUser.getUsername() );
            System.out.println( "Staff count is at " + Staff.getStaffCount() );
        }
        catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void gotoRegistration() {
        try {
            RegistrationController register = (RegistrationController) replaceSceneContent("/resources/registration.fxml");
            register.setApp(this);
        }
        catch (Exception ex ) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader =  new FXMLLoader( getClass().getResource( fxml ));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }
    
    private static void initLists() {
        staff.add( new AdminStaff( "unspecified", "unspecified", "unspecified", "unspecified", "adminstaff", "password", "unspecified", "unspecified", "unspecified", "unspecified" ));
        staff.add( new MedicalStaff( "unspecified", "unspecified", "unspecified", "unspecified", "medicalstaff", "password", "unspecified", "unspecified", "unspecified", "unspecified", "unspecified", "unspecified" ));
        try {
            // initialize -> staff and vaccine recipients into respective arraylists
            staff = (List<Staff>) Operations.loadFile().get(0); 
            vaccRecipient = (List<VaccineRecipient>) Operations.loadFile().get(1);
        }
        catch (IOException ex) {
            
            noUserError();
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void noUserError() {
        Alert alert = new Alert( Alert.AlertType.WARNING );
        alert.setTitle( "No user detected" );
        alert.setHeaderText( "No user detected" );
        alert.setContentText( "Please register for a new staff by clicking on the \"New Staff? Register\" link." );
        alert.showAndWait();
    }
    
    // get methods
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
    
    // set methods
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public static void setLoggedUser(String username)
    {
        Main.LoggedUser = Operations.getUser( Main.staff, username );
    }

    public static void setStaff(List<Staff> staff)
    {
        Main.staff = staff;
    }
    
    public static void setStaff( int staffIndex, Staff staff )
    {
        Main.staff.set(staffIndex, staff);
    }
    
    public static void setVaccRecipient(List<VaccineRecipient> vaccRecipient)
    {
        Main.vaccRecipient = vaccRecipient;
    }
    
    public static void setVaccRecipient( int recipientIndex, VaccineRecipient vaccRecipient )
    {
        Main.vaccRecipient.set(recipientIndex, vaccRecipient);
    }
    
    public static void addStaff( Staff staff )
    {
        Main.staff.add(staff);
    }
    
    public static void addVaccineRecipient( VaccineRecipient vaccRecipient )
    {
        Main.vaccRecipient.add(vaccRecipient);
    }
    
    public static void removeVaccineRecipient( int recipientIndex )
    {
        Main.vaccRecipient.remove( recipientIndex );
    }
    
}
