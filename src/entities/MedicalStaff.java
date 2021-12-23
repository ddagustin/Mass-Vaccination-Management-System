package entities;

/**
 * Assessment 1: Mass Vaccination Management System
 *      Medical Class contains all data and functions related to the Medical facilitator entity
 *      Extends from Staff class and adds registration number, affiliation, and category as unique variables
 * 
 *      handles all functions related to confirmation of vaccines
 * @author DAgustin
 * 03 Dec 2021
 */
public class MedicalStaff extends Staff
{
    // initialise unique variables to this class
    private String registrationNumber;
    private String affiliation;
    private Category category;
    
    // Category is final - added 'Unspecified' type as default value
    public enum Category {
        Unspecified, RegisteredNurse, GeneralPractitioner, Pharmacist
    }
    
    /**
     * Parameterised constructors for the MedicalStaff class
     * variables are explained when not self-explanatory
     * @param firstName : String
     * @param lastName : String
     * @param phoneNumber : String
     * @param emailAddress : String
     * @param username : String
     * @param password : String
     * @param streetAddress : String
     * @param suburbAddress : String
     * @param stateAddress : String
     * @param registrationNumber : String
     * @param affiliation : String
     * @param category  : Category
     *          can also accept a String type variable for this parameter in the other overloaded constructor 
     *          to handle javafx getText() results
     */
    public MedicalStaff(String firstName, String lastName, String phoneNumber, String emailAddress, String username, String password, String streetAddress, String suburbAddress, String stateAddress,
            String registrationNumber, String affiliation, Category category) {
        super(firstName, lastName, phoneNumber, emailAddress, username, password, streetAddress, suburbAddress, stateAddress);
        this.registrationNumber = registrationNumber;
        this.affiliation = affiliation;
        this.category = category;
    }
    
    public MedicalStaff(String firstName, String lastName, String phoneNumber, String emailAddress, String username, String password, String streetAddress, String suburbAddress, String stateAddress,
            String registrationNumber, String affiliation, String category) {
        super(firstName, lastName, phoneNumber, emailAddress, username, password, streetAddress, suburbAddress, stateAddress);
        this.registrationNumber = registrationNumber;
        this.affiliation = affiliation;
        
        // if string is not in enum, set to 'Unspecified'
        try {
            this.category = Category.valueOf(category);
        }
        catch( IllegalArgumentException ex ) {
            this.category = Category.Unspecified;
        }
    }

    // default constructor
    public MedicalStaff() {
        super();
        this.registrationNumber = "undefined";
        this.affiliation = "undefined";
        this.category = Category.Unspecified;
    }

    /**
     * Get methods
     *      returns the various variables associated with this class
     * 
     * @return registrationNumber, affiliation, category
     */
    
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public Category getCategory() {
        return category;
    }

    /**
     * Set methods
     *      enables the modification of the class variables
     */
    
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    public void setCategory(String category) {
        // if string is not in enum, set to 'Unspecified'
        try {
            this.category = Category.valueOf(category);
        }
        catch( IllegalArgumentException ex ) {
            //this.category = Category.Unspecified;
        }
    }
    
    /**
     * Overriden toString() method to display class in terminal
     *      displays the string in a readable format, including newlines as necessary
     * @return String
     */
    @Override
    public String toString() {
        return String.format( "%s%s\n%s\n%s\n%s\n",
                "Medical ", super.toString(),
                getCategory(),
                getRegistrationNumber(),
                getAffiliation());
    }
    
    /**
     * Overriden toCSV() method to write class variables to csv file separated by comma
     *      differs to the toString() method by only separating by commas.
     * @return String
     */
    @Override
    public String toCSV() {
        return String.format( "%s,%s,%s,%s,%s\n",
                getClass().getSimpleName(), super.toCSV(), getCategory(), getRegistrationNumber(), getAffiliation() );
    }
}
