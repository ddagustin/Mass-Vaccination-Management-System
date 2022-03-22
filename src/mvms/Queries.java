package mvms;

import entities.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Assessment 2: Mass Vaccination Management System
 *      Queries class contains all queries, separate from the databaseutility which implements the methods found here.
 *      SQL statements are added, executed, and modified in this class.
 * 
 * @author DAgustin
 * 28 Jan 2022
 */

public class Queries
{
    // sql statement for creating all tables of the database
    private final String CREATE_TABLES = "use vaccineregistration;" +
    "unlock tables;" +
    "set FOREIGN_KEY_CHECKS = 0;" +
    "create table if not exists staff" +
    "(   StaffID        int                     not null            auto_increment		primary key," +
    "    StaffType      varchar(30)             check(StaffType in (\"AdminStaff\", \"MedicalStaff\"))," +
    "    FirstName      varchar(30)		not null," +
    "    LastName       varchar(30)		not null," +
    "    PhoneNumber    varchar(10)," +
    "    EmailAddress   varchar(30)," +
    "    Street         varchar(50)," +
    "    Suburb         varchar(30)," +
    "    State          varchar(30)," +
    "    Username       varchar(30)		not null," +
    "    Password_      varchar(30)		not null);" +
    "create table if not exists adminstaff" +
    "(   StaffID        int                     null                unique," +
    "    PositionType   varchar(30)		not null            check(PositionType in (\"FullTime\", \"PartTime\", \"Volunteer\"))," +
    "    constraint FK_AdminStaffID foreign key (StaffID) references staff(StaffID));" +
    "create table if not exists medicalstaff" +
    "(   StaffID        int			null                unique," +
    "    Registration   varchar(30)		not null," +
    "    Affiliation    varchar(30)		not null," +
    "    Category       varchar(30)             not null            check(Category in (\"RegisteredNurse\", \"GeneralPractitioner\", \"Pharmacist\"))," +
    "    constraint FK_MedicalStaffID foreign key (StaffID) references staff(StaffID));" +
    "create table if not exists vaccinedoses" +
    "(   DoseID         int			not null            auto_increment		primary key," +
    "    VaccineType    varchar(30)		not null            check(VaccineType in (\"None\", \"AstraZeneca\", \"Pfizer\", \"Moderna\"))," +
    "    VaccineDate    date                    not null," +
    "    AdministeredBy int			null," +
    "    constraint FK_AdministeredBy foreign key (AdministeredBy) references staff(StaffID));" +
    "create table if not exists vaccinerecipient" +
    "(   RecipientID    int                     not null            auto_increment		primary key," +
    "    FirstName      varchar(30)		not null," +
    "    LastName       varchar(30)		not null," +
    "    PhoneNumber    varchar(10)," +
    "    EmailAddress   varchar(30)," +
    "    DateofBirth    date," +
    "    Gender         varchar(10)		not null            check(Gender in (\"Male\", \"Female\"))," +
    "    FirstDose      int," +
    "    SecondDose     int," +
    "    constraint FK_FirstDose foreign key (FirstDose) references vaccinedoses(DoseID)," +
    "    constraint FK_SecondDose foreign key (SecondDose) references vaccinedoses(DoseID));";
    
    // sql statement for inserting a new staff
    private final String INSERT_STAFF = "insert into staff(StaffType, FirstName, LastName, PhoneNumber, EmailAddress, Street, Suburb, State, Username, Password_) " +
            "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    // sql statements for inserting a new adminstaff or new medical staff
    private final String INSERT_ADMIN = "insert into adminstaff values(last_insert_id(), ?)";
    private final String INSERT_MEDICAL = "insert into medicalstaff values(last_insert_id(), ?, ?, ?)";
    
    // sql statement for updating an existing staff
    private final String UPDATE_STAFF = "update staff " +
            "set FirstName = ?," +
            "LastName = ?," +
            "PhoneNumber = ?," +
            "EmailAddress = ?," +
            "Street = ?," +
            "Suburb = ?," +
            "State = ? " +
            "where StaffID = ?";
    
    // sql statement for updating the credentials of an existing staff
    private final String UPDATE_STAFF_PASSWORD = "update staff " +
            "set Username = ?," +
            "Password_ = ? " +
            "where StaffID = ?";
    
    // sql statement to get a list of adminstaff
    private final String GET_ADMINSTAFF = "select staff.*, a.PositionType from staff, adminstaff as a " +
            "where staff.StaffID = a.StaffID";
    
    // sql statement to get a list of medical staff
    private final String GET_MEDICALSTAFF = "select staff.*, m.Registration, m.Affiliation, m.Category from staff, medicalstaff as m " +
            "where staff.StaffID = m.StaffID";
    
    // sql statement to insert a new vaccine recipient
    private final String INSERT_RECIPIENT = "insert into vaccinerecipient(FirstName, LastName, PhoneNumber, EmailAddress, DateofBirth, Gender) " +
            "values(?, ?, ?, ?, ?, ?)";
    
    // sql statement to insert a new vaccine record
    private final String INSERT_VACCINE = "insert into vaccinedoses(VaccineType, VaccineDate, AdministeredBy) " +
            "values(?, ?, ?)";
    
    // sql statement to update a vaccine recipient's details
    private final String UPDATE_RECIPIENT = "update vaccinerecipient " +
            "set FirstName = ?," +
            "LastName = ?," +
            "PhoneNumber = ?," +
            "EmailAddress = ?," +
            "DateofBirth = ?," +
            "Gender = ? " +
            "where RecipientID = ?";
    
    // sql statement to update the dose number 1 of a vaccine recipient
    private final String UPDATE_DOSE1 = "update vaccinerecipient " +
            "set FirstDose = ? where RecipientID = ?";
    
    // sql satetement to update the dose number 2 of a vaccine recipient
    private final String UPDATE_DOSE2 = "update vaccinerecipient " +
            "set SecondDose = ? " +
            "where RecipientID = ?";
    
    // sql statement to update a vaccine record
    private final String UPDATE_VACC = "update vaccinedoses " +
            "set VaccineType = ?,"+
            "VaccineDate = ?," +
            "AdministeredBy = ? " +
            "where DoseID = ?";
    
    // sql statement to get list of vaccine recipients
    private final String GET_RECIPIENT = "select * from vaccinerecipient";
    
    // sql statement to get a single vaccine recipient
    private final String GET_SINGLE_RECIPIENT = "select * from vaccinerecipient where RecipientID = ?";
    
    // sql statement to get a vaccine record
    private final String GET_VACCINE = "select * from vaccinedoses where DoseID = ?";
    
    
    // initialise the prepared statements that utilises above sql statements
    private PreparedStatement createTables;
    private PreparedStatement insertStaff;
    private PreparedStatement insertAdminStaff;
    private PreparedStatement insertMedicalStaff;
    private PreparedStatement insertVaccine;
    private PreparedStatement insertRecipient;
    private PreparedStatement updateStaff;
    private PreparedStatement updateStaffPassword;
    private PreparedStatement updateRecipient;
    private PreparedStatement updateDose1;
    private PreparedStatement updateDose2;
    private PreparedStatement updateVacc;
    private PreparedStatement getAdminStaff;
    private PreparedStatement getMedicalStaff;
    private PreparedStatement getRecipient;
    private PreparedStatement getSingleRecipient;
    private PreparedStatement getVaccine;
    
    // default constructor of the Queries class
    public Queries(Connection connect) throws SQLException
    {
        // the PreparedStatements and the SQL code strings are assigned and connected to each other.
        createTables = connect.prepareStatement(CREATE_TABLES);
        
        insertStaff = connect.prepareStatement(INSERT_STAFF);
        insertAdminStaff = connect.prepareStatement(INSERT_ADMIN);
        insertMedicalStaff = connect.prepareStatement(INSERT_MEDICAL);
        insertVaccine = connect.prepareStatement(INSERT_VACCINE, Statement.RETURN_GENERATED_KEYS);          // generated keys of the table are then returned for future use
        insertRecipient = connect.prepareStatement(INSERT_RECIPIENT, Statement.RETURN_GENERATED_KEYS);      // generated keys of the table are then returned for future use
        
        updateStaff = connect.prepareStatement(UPDATE_STAFF);
        updateStaffPassword = connect.prepareStatement(UPDATE_STAFF_PASSWORD);
        updateRecipient = connect.prepareStatement(UPDATE_RECIPIENT);
        updateDose1 = connect.prepareStatement(UPDATE_DOSE1);
        updateDose2 = connect.prepareStatement(UPDATE_DOSE2);
        updateVacc = connect.prepareStatement(UPDATE_VACC);
        
        getAdminStaff = connect.prepareStatement(GET_ADMINSTAFF);
        getMedicalStaff = connect.prepareStatement(GET_MEDICALSTAFF);
        getRecipient = connect.prepareStatement(GET_RECIPIENT);
        getSingleRecipient = connect.prepareStatement(GET_SINGLE_RECIPIENT);
        getVaccine = connect.prepareStatement(GET_VACCINE);
    }
    
    // execute the creation of tables
    public void createTables() throws SQLException {
        createTables.executeUpdate();
    }
    
    // add the strings needed to add a new staff
    private void addStaff(String staffType, String firstName, String lastName, String phoneNumber, String emailAddress,
            String street, String suburb, String state, String username, String password) throws SQLException {
        insertStaff.setString(1, staffType);
        insertStaff.setString(2, firstName);
        insertStaff.setString(3, lastName);
        insertStaff.setString(4, phoneNumber);
        insertStaff.setString(5, emailAddress);
        insertStaff.setString(6, street);
        insertStaff.setString(7, suburb);
        insertStaff.setString(8, state);
        insertStaff.setString(9, username);
        insertStaff.setString(10, password);
        
        insertStaff.executeUpdate();
    }
    
    // add the strings needed to add a new adminstaff. calls the addStaff() method in order to complete the transaction
    public void addAdminStaff(String firstName, String lastName, String phoneNumber, String emailAddress,
            String street, String suburb, String state, String username, String password, String positionType) throws SQLException {
        
        addStaff("AdminStaff", firstName, lastName, phoneNumber, emailAddress, street, suburb, state, username, password);
        
        insertAdminStaff.setString(1, positionType);
        insertAdminStaff.executeUpdate();
    }
    
    // add the strings needed to add a new medicalstaff. calls the addStaff() method in order to complete the transaction
    public void addMedicalStaff(String firstName, String lastName, String phoneNumber, String emailAddress,
            String street, String suburb, String state, String username, String password,
            String registration, String affiliation, String category) throws SQLException {
        
        addStaff("MedicalStaff", firstName, lastName, phoneNumber, emailAddress, street, suburb, state, username, password);
        
        insertMedicalStaff.setString(1, registration);
        insertMedicalStaff.setString(2, affiliation);
        insertMedicalStaff.setString(3, category);
        
        insertMedicalStaff.executeUpdate();
        
    }
    
    // add a new vaccine record to the database
    public int addVaccine(String vaccineType, LocalDate vaccineDate, int administeredBy) throws SQLException {
        insertVaccine.setString(1, vaccineType);
        insertVaccine.setDate(2, java.sql.Date.valueOf(vaccineDate));
        
        // if vaccine is not yet administered, insert a null in AdministeredBy column
        if(administeredBy == 0)
            insertVaccine.setNull(3, Types.NULL);
        else
            insertVaccine.setInt(3, administeredBy);
        
        insertVaccine.executeUpdate();
        
        // generated keys in the database are returned
        ResultSet r = insertVaccine.getGeneratedKeys();
        r.next();
        return r.getInt(1);
    }
    
    // add a new vaccine recipient record.
    public int addRecipient(String firstName, String lastName, String phoneNumber, String emailAddress,
            LocalDate dateofBirth, String gender) throws SQLException {
        insertRecipient.setString(1, firstName);
        insertRecipient.setString(2, lastName);
        insertRecipient.setString(3, phoneNumber);
        insertRecipient.setString(4, emailAddress);
        insertRecipient.setDate(5, java.sql.Date.valueOf(dateofBirth));
        insertRecipient.setString(6, gender);
        
        insertRecipient.executeUpdate();
        
        // generated keys in the database are returned
        ResultSet r = insertRecipient.getGeneratedKeys();
        r.next();
        return r.getInt(1);
    }
    
    // update an existing staff record
    public void updateStaff(String firstName, String lastName, String phoneNumber, String emailAddress,
            String street, String suburb, String state, int staffID) throws SQLException {
        updateStaff.setString(1, firstName);
        updateStaff.setString(2, lastName);
        updateStaff.setString(3, phoneNumber);
        updateStaff.setString(4, emailAddress);
        updateStaff.setString(5, street);
        updateStaff.setString(6, suburb);
        updateStaff.setString(7, state);
        updateStaff.setInt(8, staffID);
        
        updateStaff.executeUpdate();
    }
    
    // update only the username and password of an existing staff record
    public void updateStaffPassword(String username, String password, int staffID) throws SQLException {
        updateStaffPassword.setString(1, username);
        updateStaffPassword.setString(2, password);
        updateStaffPassword.setInt(3, staffID);
        
        updateStaffPassword.executeUpdate();
    }
    
    // update a vaccine recipient details
    public void updateRecipient(String firstName, String lastName, String phoneNumber, String emailAddress,
            LocalDate dateofBirth, String gender, int recipientID) throws SQLException {
        updateRecipient.setString(1, firstName);
        updateRecipient.setString(2, lastName);
        updateRecipient.setString(3, phoneNumber);
        updateRecipient.setString(4, emailAddress);
        updateRecipient.setDate(5, java.sql.Date.valueOf(dateofBirth));
        updateRecipient.setString(6, gender);
        updateRecipient.setInt(7, recipientID);
        
        updateRecipient.executeUpdate();
    }
    
    // update the dose 1 column of a vaccine recipient
    public void updateDose1(int dose1, int recipientID) throws SQLException {
        updateDose1.setInt(1, dose1);
        updateDose1.setInt(2, recipientID);
        
        updateDose1.executeUpdate();
    }
    
    // update the dose 2 column of a vaccine recipient
    public void updateDose2(int dose2, int recipientID) throws SQLException {
        updateDose2.setInt(1, dose2);
        updateDose2.setInt(2, recipientID);
        
        updateDose2.executeUpdate();
    }
    
    // update a vaccine record
    public void updateVaccine(String vaccineType, LocalDate vaccineDate, int administeredBy, int doseID) throws SQLException {
        updateVacc.setString(1, vaccineType);
        updateVacc.setDate(2, java.sql.Date.valueOf(vaccineDate));
        
        // if vaccine is not yet administered, insert a null in AdministeredBy column
        if(administeredBy == 0)
            updateVacc.setNull(3, Types.NULL);
        else
            updateVacc.setInt(3, administeredBy);
        
        updateVacc.setInt(4, doseID);
        
        updateVacc.executeUpdate();
    }
    
    // get the Dose ID of a vaccine from a vaccine recipient
    public int getDoseID(int recipientID, int doseNumber) throws SQLException {
        getSingleRecipient.setInt(1, recipientID);
        ResultSet results = getSingleRecipient.executeQuery();
        results.next();
        if(doseNumber == 1)
            return results.getInt("FirstDose");
        else
            return results.getInt("SecondDose");
    }
    
    // get a list of staff from the database
    public LinkedList<Staff> getStaff() throws SQLException {
        ResultSet resultsAdmin = getAdminStaff.executeQuery();
        ResultSet resultsMedical = getMedicalStaff.executeQuery();
        LinkedList<Staff> staffList = new LinkedList<>();
        while(resultsAdmin.next()) {
            AdminStaff admin = new AdminStaff(resultsAdmin.getString("FirstName"),
                                              resultsAdmin.getString("LastName"),
                                              resultsAdmin.getString("PhoneNumber"),
                                              resultsAdmin.getString("EmailAddress"),
                                              resultsAdmin.getString("Username"),
                                              resultsAdmin.getString("Password_"),
                                              resultsAdmin.getString("Street"),
                                              resultsAdmin.getString("Suburb"),
                                              resultsAdmin.getString("State"),
                                              resultsAdmin.getString("PositionType")
                                              );
            // set the staff id to the same id from the database
            admin.setStaffID(resultsAdmin.getInt("StaffID"));
            staffList.add(admin);
        }
        
        while(resultsMedical.next()) {
            MedicalStaff medic = new MedicalStaff(resultsMedical.getString("FirstName"),
                                              resultsMedical.getString("LastName"),
                                              resultsMedical.getString("PhoneNumber"),
                                              resultsMedical.getString("EmailAddress"),
                                              resultsMedical.getString("Username"),
                                              resultsMedical.getString("Password_"),
                                              resultsMedical.getString("Street"),
                                              resultsMedical.getString("Suburb"),
                                              resultsMedical.getString("State"),
                                              resultsMedical.getString("Registration"),
                                              resultsMedical.getString("Affiliation"),
                                              resultsMedical.getString("Category")
                                              );
            // set the staff id to the same id from the database
            medic.setStaffID(resultsMedical.getInt("StaffID"));
            staffList.add(medic);
        }
        
        return staffList;
    }
    
    // get a list of vaccine recipients from the database
    public LinkedList<VaccineRecipient> getRecipients() throws SQLException {
        ResultSet results = getRecipient.executeQuery();
        LinkedList<VaccineRecipient> recipientList = new LinkedList<>();
        while(results.next()) {
            VaccineRecipient recipient = new VaccineRecipient(results.getString("FirstName"),
                                              results.getString("LastName"),
                                              results.getString("PhoneNumber"),
                                              results.getString("EmailAddress"),
                                              results.getDate("DateofBirth").toLocalDate(),
                                              results.getString("Gender")
                                              );
            // set the recipient id to the same id from the database
            recipient.setRecipientID(results.getInt("RecipientID"));
            
            // set doses if exist
            // only if the doses exist will the code below be executed. the information from the recipient and vaccine tables
            //      are then used to create a vaccine recipient object inside the program
            if(results.getInt("FirstDose") > 0) {
                getVaccine.setInt(1, results.getInt("FirstDose"));
                ResultSet vacc = getVaccine.executeQuery();
                vacc.next();
                Vaccine vacc1 = new Vaccine(vacc.getString("VaccineType"),
                                            vacc.getDate("VaccineDate").toLocalDate()
                                            );
                vacc1.setVaccineID(vacc.getInt("DoseID"));
                if(vacc.getInt("AdministeredBy") > 0) {
                    vacc1.setAdministeredBy(vacc.getInt("AdministeredBy"));
                    vacc1.setConfirmed();
                }
                recipient.SETFIRSTVACCDOSE(vacc1);
            }
            if(results.getInt("SecondDose") > 0) {
                getVaccine.setInt(1, results.getInt("SecondDose"));
                ResultSet vacc = getVaccine.executeQuery();
                vacc.next();
                Vaccine vacc2 = new Vaccine(vacc.getString("VaccineType"),
                                            vacc.getDate("VaccineDate").toLocalDate()
                                            );
                vacc2.setVaccineID(vacc.getInt("DoseID"));
                if(vacc.getInt("AdministeredBy") > 0) {
                    vacc2.setAdministeredBy(vacc.getInt("AdministeredBy"));
                    vacc2.setConfirmed();
                }
                recipient.SETSECONDVACCDOSE(vacc2);
            }
            
            recipientList.add(recipient);
        }
        
        return recipientList;
    }
    
}
