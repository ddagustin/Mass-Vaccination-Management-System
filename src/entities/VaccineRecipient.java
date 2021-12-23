package entities;

import java.time.LocalDate;

/**
 * Assessment 1: Mass Vaccination Management System
 *      VaccineRecipient Class contains all data and functions related to the person taking the vaccine
 *      Extends from Person class and adds dateOfBirth, vaccineDoses, gender, RECIPIENTID, doseCount as unique variables
 * 
 * @author DAgustin
 * 03 Dec 2021
 */
public class VaccineRecipient extends Person
{
    // initialise unique variables to this class
    private LocalDate dateOfBirth;
    private Vaccine[] vaccineDoses = new Vaccine[2];
    private Gender gender;
    private final String RECIPIENTID;
    private int doseCount = 0;
    
    // recipient count static variable to help in generating RECIPIENTID
    private static int recipientCount = 0;
    
    // Gender is final - added 'unspecified' type as default value
    public enum Gender {
        Unspecified, Male, Female
    }

    /**
     * Parameterised constructors for the VaccineRecipient class
     * variables are explained when not self-explanatory
     * @param firstName : String
     * @param lastName : String
     * @param phoneNumber : String
     * @param emailAddress : String
     * @param dateOfBirth : LocalDate
     * @param gender : Gender
     *          can also accept String in overloaded constructor which is then converted to enum
     */
    public VaccineRecipient(String firstName, String lastName, String phoneNumber, String emailAddress,
            LocalDate dateOfBirth, Gender gender) {
        super(firstName, lastName, phoneNumber, emailAddress);
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        
        // generate RECIPIENTID as a 5 character string from recipientCount
        this.RECIPIENTID = String.format("%05d", getRecipientCount());
        
        // set default doses
        this.SETFIRSTVACCDOSE( new Vaccine() );
        this.SETSECONDVACCDOSE( new Vaccine() );
        this.doseCount = 0; // reinitialise to 0
    }
    
    public VaccineRecipient(String firstName, String lastName, String phoneNumber, String emailAddress,
            LocalDate dateOfBirth, String gender) {
        super(firstName, lastName, phoneNumber, emailAddress);
        this.dateOfBirth = dateOfBirth;
        
        // if gender string is not in enum, set to 'unspecified'
        try {
            this.gender = Gender.valueOf(gender);
        }
        catch( IllegalArgumentException ex ) {
            this.gender = Gender.Unspecified;
        }
        
        // generate RECIPIENTID as a 5 character string from recipientCount
        this.RECIPIENTID = String.format("%05d", getRecipientCount());
        
        // set default doses
        this.SETFIRSTVACCDOSE( new Vaccine() );
        this.SETSECONDVACCDOSE( new Vaccine() );
        this.doseCount = 0; // reinitialise to 0
    }

    // default constructor
    public VaccineRecipient() {
        super();
        this.dateOfBirth = LocalDate.now();
        this.gender = Gender.Unspecified;
        
        // generate RECIPIENTID as a 5 character string from recipientCount
        this.RECIPIENTID = "undefined";
        
        // set default doses
        this.SETFIRSTVACCDOSE( new Vaccine() );
        this.SETSECONDVACCDOSE( new Vaccine() );
        this.doseCount = 0; // reinitialise to 0
    }

    /**
     * Get methods
     *      returns the various variables associated with this class
     * 
     * @return dateOfBirth, vaccineDoses, gender, RECIPIENTID, recipientCount, doseCount
     */
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Vaccine[] getVaccineDoses() {
        return vaccineDoses;
    }

    public Gender getGender() {
        return gender;
    }
    
    public Vaccine getFirstVaccineDose() {
        return vaccineDoses[0];
    }
    
    public Vaccine getSecondVaccineDose() {
        return vaccineDoses[1];
    }

    public String getRECIPIENTID() {
        return RECIPIENTID;
    }

    public static int getRecipientCount() {
        return recipientCount;
    }
    
    public int getDoseCount() {
        return doseCount;
    }
    
    /**
     * Set methods
     *      enables the modification of the class variables
     */
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setVaccineDoses(Vaccine[] vaccineDoses) {
        this.vaccineDoses = vaccineDoses;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public void setGender(String gender) {
        // if gender string is not in enum, set to 'unspecified'
        try {
            this.gender = Gender.valueOf(gender);
        }
        catch( IllegalArgumentException ex ) {
            //this.gender = Gender.Unspecified;
        }
    }
    
    /**
     * set vaccine doses if confirmed by a medical staff
     */
    
    public final void SETFIRSTVACCDOSE(Vaccine vaccine) {
        // can only confirm once
        if( this.doseCount < 1 ) {
            this.doseCount = 1;
            this.vaccineDoses[0] = vaccine;
        }
    }
    
    public final void SETSECONDVACCDOSE(Vaccine vaccine) {
        // can only confirm once
        if( this.doseCount == 1 ) {
            this.doseCount = 2;
            this.vaccineDoses[1] = vaccine;
        }
    }
    
    /**
     * Overriden toString() method to display class in terminal
     *      displays the string in a readable format, including newlines as necessary
     * @return String
     */
    @Override
    public String toString() {
        return String.format( "%s\n%s\n%s\n%s\n%s\n\n%s\n%s%s%s%s",
                "Recipient Details:",
                getRECIPIENTID(),
                super.toString(),
                getGender(),
                getDateOfBirth(),
                "Vaccine Details:",
                "Dose 1: ", getFirstVaccineDose(),
                "Dose 2: ", getSecondVaccineDose()
        );
    }
    
    /**
     * Overriden toCSV() method to write class variables to csv file separated by comma
     *      differs to the toString() method by only separating by commas.
     * @return String
     */
    @Override
    public String toCSV() {
        return String.format( "%s,%s,%s,%s,%s\n",
                super.toCSV(), getGender(), getDateOfBirth(), getFirstVaccineDose().toCSV(), getSecondVaccineDose().toCSV() );
    }
}
