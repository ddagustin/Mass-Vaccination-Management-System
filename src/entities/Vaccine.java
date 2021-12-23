package entities;

// import dependencies
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

/**
 * Assessment 1: Mass Vaccination Management System
 *      Vaccine class contains all data and functions related to the vaccines
 *      stores vaccine dose name, scheduled/confirmed date, status (if already confirmed), and 
 *          the medical staff who administered the vaccine
 *      also stores a unique vaccineID
 *          - used the UUID generation method in java to generate a unique value0
 * 
 * @author DAgustin
 * 03 Dec 2021
 */
public class Vaccine implements Serializable
{
    // initialise unique variables to this class
    private LocalDate vaccinationDate;
    private String vaccineID;
    private VaccineType vaccineName;
    private boolean status = false;
    private String administeredBy = "";
    
    // VaccineType is final - added 'None' type as default value
    public enum VaccineType {
        None, AstraZeneca, Pfizer, Moderna
    }

    /**
     * Parameterised constructors for the Vaccine class
     * @param vaccineName : VaccineType
     *          can also accept a String to handle javafx getText() results
     * @param vaccinationDate : LocalDate
     *          can also accept a String to handle javafx getText() results
     */
    public Vaccine(VaccineType vaccineName, LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
        this.vaccineName = vaccineName;
        this.vaccineID = ( vaccineName.toString().substring(0,1) + UUID.randomUUID().toString().substring(0,8) ).toUpperCase();
    }
    
    public Vaccine( String vaccineName, String vaccinationDate ) {
        
        // if vaccineName string is not in enum, set to 'none'
        // if vaccinationDate is not parse-able, set today
        try {
            this.vaccineName = VaccineType.valueOf( vaccineName );
            this.vaccinationDate = LocalDate.parse( vaccinationDate );
        }
        catch( IllegalArgumentException ex ) {
            this.vaccineName = VaccineType.None;
        }
        catch( DateTimeParseException dtex ) {
            this.vaccinationDate = LocalDate.now();
        }
        
        // generate vaccineID
        this.vaccineID = ( vaccineName.substring(0,1) + UUID.randomUUID().toString().substring(0,8) ).toUpperCase();
    }
    
    public Vaccine( String vaccineName, LocalDate vaccinationDate ) {
        
        // if vaccineName string is not in enum, set to 'none'
        try {
            this.vaccineName = VaccineType.valueOf( vaccineName );
        }
        catch( IllegalArgumentException ex ) {
            this.vaccineName = VaccineType.None;
        }
        
        this.vaccinationDate = vaccinationDate;
        
        // generate vaccineID
        this.vaccineID = ( vaccineName.substring(0,1) + UUID.randomUUID().toString().substring(0,8) ).toUpperCase();
    }
    
    // default constructor
    public Vaccine() {
        this( VaccineType.None, LocalDate.now() );
        this.vaccineID = "undefined";
    }

    /**
     * Get methods
     *      returns the various variables associated with this class
     * 
     * @return vaccinationDate, vaccineID, vaccineName, status, administeredBy
     */
    
    public LocalDate getVaccinationDate() {
        return vaccinationDate;
    }

    public String getVaccineID() {
        return vaccineID;
    }
    
    public VaccineType getVaccineName() {
        return vaccineName;
    }
    
    public boolean getStatus() {
        return status;
    }
    
    public String getAdministeredBy() {
        return administeredBy;
    }

    /**
     * Set methods
     *      enables the modification of the class variables
     */
    
    public void setVaccinationDate(LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    public void setVaccineID(String vaccineID) {
        this.vaccineID = vaccineID;
    }

    public void setVaccineName(VaccineType vaccineName) {
        this.vaccineName = vaccineName;
    }
    
    public void setVaccineName(String vaccineName) {
        // if vaccineName string is not in enum, set to 'none'
        try {
            this.vaccineName = VaccineType.valueOf( vaccineName );
        }
        catch( IllegalArgumentException ex ) {
            this.vaccineName = VaccineType.None;
        }
    }
    
    public void setConfirmed()
    {
        // can only change status once
        if( status == false )
            this.status = true;
        else
            System.out.println( "Vaccine already confirmed" );
    }
    
    public void setAdministeredBy( String administeredBy )
    {
        // can only change status once
        if( status == false ) {
            this.administeredBy = administeredBy;
        }
        else
            System.out.println( "Dose already administered" );
    }
    
    /**
     * Overriden toString() method to display class in terminal
     *      displays the string in a readable format, including newlines as necessary
     * @return String
     */
    @Override
    public String toString() {
        return String.format( "%s\n%s\n%s\n",
                getVaccineName(),
                getVaccineID(),
                getVaccinationDate()
        );
    }
    
    /**
     * Overriden toCSV() method to write class variables to csv file separated by comma
     *      differs to the toString() method by only separating by commas.
     * @return String
     */
    public String toCSV() {
        return String.format( "%s,%s,%s",
                getVaccineID(), getVaccineName(), getVaccinationDate() );
    }
    
}
