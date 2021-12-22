package mvms;

import entities.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

/**
 * Assessment 1: Mass Vaccination System
 * @author DAgustin
 * 03 Dec 2021
 */
public class Operations
{
    private static String fileName;
    
    // read csv file
    public static void saveFile( List<?> arraylist, String filename ) throws IOException
    {
        fileName = "src/files/" + filename;
        
        
        try {
            File myFile = new File( fileName );
            if( myFile.createNewFile( )) {
                System.out.println( "file created" );
            }
            else {
                System.out.println( "file already exists" );
                // update file
            }
        }
        catch( IOException ex ) {
            System.out.println( "An error occured in creating file" );
        }
        finally {
        
            //id = arraylist;
            //System.out.println( arraylist );
        
            try {
                FileWriter myWriter = new FileWriter( fileName );
                    //for( Staff line : arraylist ) {
                    //    myWriter.write( line.toCSV() );
                    //}
                    
                for( int i = 0; i < arraylist.size(); i++ ) {
                    myWriter.write( ((Person) arraylist.get(i)).toCSV() );
                }
                    
                myWriter.flush(); myWriter.close();                    
            }
            catch( IOException ex ) {
                System.out.println( "An error occured in writing file" );
            }
        }
    }
    
    // do not need this??
    public static List<String[]> getListID( String filename ) throws IOException
    {
        BufferedReader reader = new BufferedReader( new FileReader( filename ));
        List<String[]> contents = new ArrayList<>();
        
        String line;
        
        while( (line = reader.readLine()) != null ) {
            //System.out.println( line );
            contents.add(line.split(","));
        }
        
        return contents;
    }
    
    public static void dumpFile( List<Person> arraylist ) throws IOException
    {
        FileOutputStream fos = new FileOutputStream("src/files/dump.tmp");
        try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject( (List<Person>) arraylist );
            oos.flush(); oos.reset();
        }
            
    }
    
    public static ArrayList< List<?> > loadFile() throws IOException, ClassNotFoundException
    {
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
        
        ArrayList< List<?> > person = new ArrayList<>();
        person.add(staff); person.add(vaccRecipient);
        
        return person;
    }
    
    public static Staff getUser( List<Staff> staff, String username )
    {
        for( Staff currentStaff : staff )
        {
            if( currentStaff.getUsername().equals(username))
                return currentStaff;
        }
        return null;
    }
    
    public static boolean validateFields( Pane pane )
    {
        String input;
        for( Node node : pane.getChildren() ) {
            if( node instanceof TextField ) {
                input = ((TextField) node).getText();
                if( input.isBlank() ) {
                    System.out.println( "textfield is blank" );
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
                    System.out.println( "combobox is blank" );
                    return false;
                }
            }
            else if( node instanceof DatePicker ) {
                DatePicker dp = (DatePicker) node;
                DateTimeFormatter df = DateTimeFormatter.ofPattern("d/M/yyyy");
                if( dp.getEditor().getText().isBlank() ) {
                    System.out.println( "is blank" );
                    return false;
                }
                else {
                    dp.setValue ( LocalDate.parse(dp.getEditor().getText(), df) );
                    if( dp.getValue() == null ) {
                        System.out.println( "is null" );
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
}
