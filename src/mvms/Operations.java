package mvms;

import entities.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

/**
 * Assessment 1: Mass Vaccination Management System
 *      Operations class contains all functions shared by the whole program - primarily in loading, saving, and dumping
 *          files into directory for management, accessing, and storage of information
 *      contains validateFields method to summarize node checking if each node contains a valid input
 * 
 * @author DAgustin
 * 03 Dec 2021
 */
public class Operations
{
    // initialise unique variables to this class
    private static String fileName;
    
    // read csv files
    public static void saveFile( List<?> arraylist, String filename ) throws IOException {
        fileName = "src/files/" + filename;
        
        // create or overwrite filename
        try {
            File myFile = new File( fileName );
            if( myFile.createNewFile( )) {
                System.out.println( "file created" );
            }
            else {
                System.out.println( filename +  " already exists, overwriting" );
                // update file
            }
        }
        catch( IOException ex ) {
            System.out.println( "An error occured in creating file" );
        }
        finally {
            // save classes to csv using toCSV method
            try {
                FileWriter myWriter = new FileWriter( fileName );
                for( int i = 0; i < arraylist.size(); i++ ) {
                    myWriter.write( ((Person) arraylist.get(i)).toCSV() );
                }
                // flush and close for memory refreshing    
                myWriter.flush(); myWriter.close();                    
            }
            catch( IOException ex ) {
                System.out.println( "An error occured in writing file" );
            }
        }
    }
    
    // dump to file as objects
    public static void dumpFile( List<Person> arraylist ) throws IOException {
        FileOutputStream fos = new FileOutputStream("src/files/dump.tmp");
        try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject( (List<Person>) arraylist );
            oos.flush(); oos.reset();
        }
            
    }
    
    // load dump to program
    public static ArrayList< List<?> > loadFile() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("src/files/dump.tmp");
        List<Person> dump;
        try (ObjectInputStream ois = new ObjectInputStream(fis)) {
            dump = (List<Person>) ois.readObject();
        }
        
        List< Staff > staff = new ArrayList<>();
        List< VaccineRecipient > vaccRecipient = new ArrayList<>();
        
        for( int i = 0; i < dump.size(); i++ ) {
            int staffCount = 0;
            Person currentPerson = (Person) dump.get(i);
            
            if( currentPerson instanceof Staff ) {
                staff.add((Staff) currentPerson );
                
                // load credentials of staff into authenticator
                Authenticator.loadCredentials((Staff) currentPerson);
                
                // check staff id for counting
                if( Integer.parseInt( ((Staff) currentPerson).getStaffID() ) > staffCount )
                {
                    staffCount = Integer.parseInt( ((Staff) currentPerson).getStaffID() );
                    Staff.setStaffCount(staffCount);
                }
                    
            }
            else {
                vaccRecipient.add((VaccineRecipient) currentPerson);
            }
            
            
        }
        
        // condense into one arraylist
        ArrayList< List<?> > person = new ArrayList<>();
        person.add(staff); person.add(vaccRecipient);
        
        return person;
    }
    
    // returns the currentStaff if it exists
    public static Staff getUser( String username ) {
        for( Staff currentStaff : Main.getStaff() )
        {
            if( currentStaff.getUsername().equals(username))
                return currentStaff;
        }
        return null;
    }
    
    // for controller class use - iterates through nodes in a pane to check if it matches the requirement
    public static boolean validateFields( Pane pane ) {
        String input;
        for( Node node : pane.getChildren() ) {
            if( node instanceof TextField ) {
                input = ((TextField) node).getText();
                if( input.isBlank() ) {
                    return false;
                }
            }
            else if( node instanceof PasswordField ) {
                input = ((PasswordField) node).getText();
                if( input.isBlank() || !input.matches( "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}$" ) )
                    return false;
            }
            else if( node instanceof ComboBox ) {
                if( ((ComboBox) node).getSelectionModel().isEmpty() ) {
                    return false;
                }
            }
            else if( node instanceof DatePicker ) {
                DatePicker dp = (DatePicker) node;
                DateTimeFormatter df = DateTimeFormatter.ofPattern("d/M/yyyy");
                if( dp.getEditor().getText().isBlank() ) {
                    return false;
                }
                else {
                    dp.setValue ( LocalDate.parse(dp.getEditor().getText(), df) );
                    if( dp.getValue() == null ) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
}
