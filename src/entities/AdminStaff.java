package entities;

/**
 * Assessment 1: Mass Vaccination Management System
 *      Adminstaff Class contains all data and functions related to the Administrator entity
 *      Extends from Staff class and adds PositionType as unique variable
 * 
 *      handles all admin functions of this program:
 *          - add records of vaccine recipients
 *          - cannot confirm if vaccines are administered
 * 
 * @author DAgustin
 * 03 Dec 2021
 */
public class AdminStaff extends Staff
{
    // initialise unique variables to this class
    private PositionType positionType;
    
    // PositionType is final - added 'None' type as default value
    public enum PositionType {
        None, FullTime, PartTime, Volunteer
    }
    
    /**
     * Parameterised constructors for the AdminStaff class
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
     * @param positionType  : PositionType
     *          this class also accepts String type variable for this parameter in the next overloaded constructor
     *          to handle javafx getText() results
     */
    public AdminStaff(String firstName, String lastName, String phoneNumber, String emailAddress, String username, String password, String streetAddress, String suburbAddress, String stateAddress,
            PositionType positionType) {
        super(firstName, lastName, phoneNumber, emailAddress, username, password, streetAddress, suburbAddress, stateAddress);
        this.positionType = positionType;
    }
    
    public AdminStaff(String firstName, String lastName, String phoneNumber, String emailAddress, String username, String password, String streetAddress, String suburbAddress, String stateAddress,
            String positionType) {
        super(firstName, lastName, phoneNumber, emailAddress, username, password, streetAddress, suburbAddress, stateAddress);
        
        // if string is not in enum, set to 'None'
        try {
            this.positionType = PositionType.valueOf(positionType);
        }
        catch( IllegalArgumentException ex ) {
            this.positionType = PositionType.None;
        }
    }
    
    // default constructor
    public AdminStaff() {
        super();
        this.positionType = PositionType.None;
    }
    
    /**
     * Get methods
     *      returns the various variables associated with this class
     * 
     * @return positionType
     */
    public PositionType getPositionType() {
        return positionType;
    }
    
    /**
     * Set methods
     *      enables the modification of the PositionType variable
     * 
     * @param positionType : PositionType
     *      can also accept String type to handle javafx getText() results
     */
    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }
    
    public void setPositionType(String positionType) {
        // if string is not in enum, set to 'None'
        try {
            this.positionType = PositionType.valueOf(positionType);
        }
        catch( IllegalArgumentException ex ) {
            //this.positionType = PositionType.None;
        }
    }
    
    /**
     * Overriden toString() method to display class in terminal
     *      displays the string in a readable format, including newlines as necessary
     * @return String
     */
    @Override
    public String toString() {
        return String.format( "%s%s\n%s\n",
                "Admin ", super.toString(),
                getPositionType());
    }
    
    /**
     * Overriden toCSV() method to write class variables to csv file separated by comma
     *      differs to the toString() method by only separating by commas.
     * @return String
     */
    @Override
    public String toCSV() {
        return String.format( "%s,%s,%s\n",
                getClass().getSimpleName(), super.toCSV(), getPositionType() );
    }
}
