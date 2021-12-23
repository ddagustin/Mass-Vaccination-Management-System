package entities;

/**
 * Assessment 1: Mass Vaccination Management System
 *      Staff Class contains all data and functions related to staff functionalities
 *      Extends from Person class and adds username, password, address, and staffID as unique variables
 * 
 * @author DAgustin
 * 03 Dec 2021
 */
public abstract class Staff extends Person
{
    // initialise unique variables to this class
    private String username;
    private String password;
    private String streetAddress;
    private String suburbAddress;
    private String stateAddress;
    private final String STAFFID; // 5 character long ID unique per instance of this class
    
    // staff count static variable to help in generating staffID
    private static int staffCount = 0;

    /**
     * Parameterised constructors for the Staff class
     * variables are explained when not self-explanatory
     * @param firstName : String
     * @param lastName : String
     * @param phoneNumber : String
     * @param emailAddress : String
     * @param username : String
     * @param password : String
     * @param streetAddress : String
     * @param suburbAddress : String
     * @param stateAddress  : String
     */
    public Staff(String firstName, String lastName, String phoneNumber, String emailAddress,
            String username, String password, String streetAddress, String suburbAddress, String stateAddress ) {
        super(firstName, lastName, phoneNumber, emailAddress);
        this.username = username;
        this.password = password;
        this.streetAddress = streetAddress;
        this.suburbAddress = suburbAddress;
        this.stateAddress = stateAddress;
        
        // call static method to increase staff count and generate a unique staffID
        Staff.incrementStaffCount();
        this.STAFFID = String.format("%05d", getStaffCount());
    }

    // default constructor
    public Staff() {
        super();
        this.username = "undefined";
        this.password = "undefined";
        this.streetAddress = "undefined";
        this.suburbAddress = "undefined";
        this.stateAddress = "undefined";
        this.STAFFID = "undefined";
    }

    /**
     * Get methods
     *      returns the various variables associated with this class
     * 
     * @return username, password, streetAddress, suburbAddress, stateAddress, STAFFID
     */
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getSuburbAddress() {
        return suburbAddress;
    }

    public String getStateAddress() {
        return stateAddress;
    }

    public String getStaffID() {
        return STAFFID;
    }

    /**
     * Set methods
     *      enables the modification of the class variables
     */
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setSuburbAddress(String suburbAddress) {
        this.suburbAddress = suburbAddress;
    }

    public void setStateAddress(String stateAddress) {
        this.stateAddress = stateAddress;
    }

    // cannot set STAFFID manually as it is austomatically generated upon instance creation
    /*
    public void setStaffID(String STAFFID) {
        this.STAFFID = STAFFID;
    }
    */
    
    public static void setStaffCount(int staffCount) {
        Staff.staffCount = staffCount;
    }

    // unique method to increment stafCount variable
    public static void incrementStaffCount() {
        staffCount += 1;
    }
    
    // get the staffCount
    public static int getStaffCount() {
        return staffCount;
    }
    
    /**
     * Overriden toString() method to display class in terminal
     *      displays the string in a readable format, including newlines as necessary
     * @return String
     */
    @Override
    public String toString() {
        return String.format( "%s\n%s\n%s, %s, %s\n%s\n%s",
                "Staff Details:",
                super.toString(),
                getStreetAddress(), getSuburbAddress(), getStateAddress(),
                getStaffID(),
                getUsername());
    }
    
    /**
     * Overriden toCSV() method to write class variables to csv file separated by comma
     *      differs to the toString() method by only separating by commas.
     * @return String
     */
    @Override
    public String toCSV() {
        return String.format( "%s,%s,%s,%s,%s,%s",
                getStaffID(), super.toCSV(), getStreetAddress(), getSuburbAddress(), getStateAddress(), getUsername() );
    }
    
}
