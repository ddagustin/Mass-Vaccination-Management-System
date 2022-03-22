package mvms;

import entities.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Assessment 2: Mass Vaccination Management System
 *      DatabaseUtility class contains all operations pertaining to connecting to the MySQL database and executing queries
 * 
 * @author DAgustin
 * 28 Jan 2022
 */
public class DatabaseUtility
{
    private final String MYSQL_URL;
    private final String DB_URL;
    private Connection sqlConnection, dbConnection;
    private Statement statement;
    private final String DBCREATESQL;
    private final String USERNAME;
    private final String PASSWORD;
    
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("d/M/yyyy");
    private Queries queries;
    
    // default constructor. only called once in the Main class and is used throughout the program
    public DatabaseUtility() {
        MYSQL_URL = "jdbc:mysql://localhost:3306";
        DB_URL = MYSQL_URL + "/vaccineregistration?allowMultiQueries=true";
        
        // REMEMBER TO CHANGE THESE PARAMETERS
        USERNAME = "root";
        PASSWORD = "PLEASE PUT YOUR PASSWORD HERE";
        
        statement = null;
        DBCREATESQL = "CREATE DATABASE vaccineregistration";
        
        boolean dbExists = false;
        String databaseName = "";
        
        // try connecting to the server
        try {
            sqlConnection = DriverManager.getConnection(MYSQL_URL, USERNAME, PASSWORD);
            statement = sqlConnection.createStatement();
        }
        catch(SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        
        // once out of the try-catch block, print success
        System.out.println("Connection Success! Connected to the server");
        
        // try connecting to the database
        try {
            ResultSet dbData = sqlConnection.getMetaData().getCatalogs();
            while(dbData.next()) {
                databaseName = dbData.getString(1);
                if(databaseName.equalsIgnoreCase("vaccineregistration"))
                    dbExists = true;
            }
            if(!dbExists)
                statement.executeUpdate(DBCREATESQL);    
            if(sqlConnection != null)
                sqlConnection.close();
            
            dbConnection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = dbConnection.createStatement();
            queries = new Queries(dbConnection);
            queries.createTables();
            
            if(!dbExists) {
                System.out.println("Attempting to load data from the csv file");
                loadCSV();
                System.out.println("Sucessfully loaded the csv file data into the database");
            }
            
        }
        catch(SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
        }
        catch (IOException ex) {
            Logger.getLogger(DatabaseUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // once out of the try-catch block, print success
        System.out.println("Connection Success! Connected to the database");
    }
    
    // load csv data from assessment 1 into the database
    public final void loadCSV() throws FileNotFoundException, IOException, SQLException {
        BufferedReader csvReader = new BufferedReader(new FileReader("src/files/staff.csv"));
        String row;
        while((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            
            if(data[4].length() == 9)
                data[4] = "0" + data[4];
            
            if(data[1].equals("AdminStaff")) {
                queries.addAdminStaff(data[2], data[3], data[4], data[5], data[6], data[7],
                                      data[8], data[9], data[10], data[11]);
            }
            else if(data[1].equals("MedicalStaff")){
                queries.addMedicalStaff(data[2], data[3], data[4], data[5], data[6], data[7],
                                      data[8], data[9], data[10], data[11], data[12], data[13]);
            }
        }
        csvReader.close();
        
        BufferedReader csvReader2 = new BufferedReader(new FileReader("src/files/vacc.csv"));
        int vacc_id;
        int rec_id;
        while((row = csvReader2.readLine()) != null) {
            String[] data = row.split(",");
            
            if(data[3].length() == 9)
                data[3] = "0" + data[3];
            
            rec_id = queries.addRecipient(data[1], data[2], data[3], data[4], LocalDate.parse(data[5], df), data[6]);
            if(!data[7].isEmpty()) {
                vacc_id = queries.addVaccine(data[9], LocalDate.parse(data[10], df), Integer.valueOf(data[11]));
                queries.updateDose1(vacc_id, rec_id);
            }
            if(!data[8].isEmpty()) {
                vacc_id = queries.addVaccine(data[12], LocalDate.parse(data[13], df), Integer.valueOf(data[14]));
                queries.updateDose2(vacc_id, rec_id);
            }
        }
        csvReader2.close();
    }
    
    // get the staff list from the database
    public LinkedList<Staff> getStaffList() throws SQLException {
        return queries.getStaff();
    }
    
    // get the recipient list from the database
    public LinkedList<VaccineRecipient> getRecipientList() throws SQLException {
        return queries.getRecipients();
    }
    
    // add a new admin staff into the database
    public void addAdminStaff(AdminStaff adminStaff) throws SQLException {
        
        queries.addAdminStaff(adminStaff.getFirstName(), adminStaff.getLastName(), adminStaff.getPhoneNumber(), adminStaff.getEmailAddress(),
                adminStaff.getStreetAddress(), adminStaff.getSuburbAddress(), adminStaff.getStateAddress(), adminStaff.getUsername(), adminStaff.getPassword(), adminStaff.getPositionType().name());    
    }
    
    // add a new medical staff into the database
    public void addMedicalStaff(MedicalStaff medStaff) throws SQLException {
        
        queries.addMedicalStaff(medStaff.getFirstName(), medStaff.getLastName(), medStaff.getPhoneNumber(), medStaff.getEmailAddress(),
                medStaff.getStreetAddress(), medStaff.getSuburbAddress(), medStaff.getStateAddress(), medStaff.getUsername(), medStaff.getPassword(),
                medStaff.getRegistrationNumber(), medStaff.getAffiliation(), medStaff.getCategory().name());
    }
    
    // update an existing staff record in the database
    public void updateStaff(Staff staff, int staffID) throws SQLException {
        
        queries.updateStaff(staff.getFirstName(), staff.getLastName(), staff.getPhoneNumber(), staff.getEmailAddress(),
                staff.getStreetAddress(), staff.getSuburbAddress(), staff.getStateAddress(), staffID);
    }
    
    // update an existing staff record's username and password in the database
    public void updateStaffPassword(Staff staff, int staffID) throws SQLException {
        queries.updateStaffPassword(staff.getUsername(), staff.getPassword(), staffID);
    }
    
    // add a new vaccine recipient to the database
    public void addRecipient(VaccineRecipient vaccRecipient) throws SQLException {
        // the ids generated from the new recipient will be used in generating a vaccine and updating it back into the recipient
        int id;
        int v_id;
        
        id = queries.addRecipient(vaccRecipient.getFirstName(), vaccRecipient.getLastName(), 
                vaccRecipient.getPhoneNumber(), vaccRecipient.getEmailAddress(), 
                vaccRecipient.getDateOfBirth(), vaccRecipient.getGender().name());
        
        Vaccine dose1 = vaccRecipient.getFirstVaccineDose();
        Vaccine dose2 = vaccRecipient.getSecondVaccineDose();
        
        v_id = queries.addVaccine(dose1.getVaccineName(), dose1.getVaccinationDate(), 0);
        updateRecipientDoses(1, v_id, id); // the FirstDose column of the recipient is updated 
        
        v_id = queries.addVaccine(dose2.getVaccineName(), dose1.getVaccinationDate().plusDays(60), 0);
        updateRecipientDoses(2, v_id, id); // the SecondDose column of the recipient is updated 
        
    }
    
    // update an existing recipient record in the database
    public void updateRecipient(VaccineRecipient vaccRecipient, int recipientID) throws SQLException {
        queries.updateRecipient(vaccRecipient.getFirstName(), vaccRecipient.getLastName(), 
                vaccRecipient.getPhoneNumber(), vaccRecipient.getEmailAddress(), 
                vaccRecipient.getDateOfBirth(), vaccRecipient.getGender().name(), recipientID);
        
        Vaccine dose1 = vaccRecipient.getFirstVaccineDose();
        Vaccine dose2 = vaccRecipient.getSecondVaccineDose();
        
        queries.updateVaccine(dose1.getVaccineName(), dose1.getVaccinationDate(), dose1.getAdministeredBy(), queries.getDoseID(recipientID, 1));
        
        if(dose1.getVaccinationDate().plusDays(60).compareTo(LocalDate.now()) < 0)
            queries.updateVaccine(dose1.getVaccineName(), LocalDate.now(), dose2.getAdministeredBy(), queries.getDoseID(recipientID, 2));
        else
            queries.updateVaccine(dose1.getVaccineName(), dose1.getVaccinationDate().plusDays(60), dose2.getAdministeredBy(), queries.getDoseID(recipientID, 2));
    }
    
    // update a vaccine record
    public void updateVaccine(String vaccineType, LocalDate vaccineDate, int administeredBy, int doseID) throws SQLException {
        queries.updateVaccine(vaccineType, vaccineDate, administeredBy, doseID);
    }
    
    // update a recipient's dose number. attach and connect the recipient to an existing vaccine record
    public void updateRecipientDoses(int doseNumber, int doseID, int recipientID) throws SQLException {
        if(doseNumber == 1)
            queries.updateDose1(doseID, recipientID);
        else
            queries.updateDose2(doseID, recipientID);
    }

    //used to check the class separately from the code
    /*
    public static void main(String args[]) {
        DatabaseUtility dbUtil = new DatabaseUtility();
        try {
            dbUtil.loadCSV();
        }
        catch (IOException | SQLException ex) {
            Logger.getLogger(DatabaseUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    
}
