package person;

/**
 * Assessment 1: Mass Vaccination System
 * @author DAgustin
 * 03 Dec 2021
 */
public abstract class Staff extends Person
{
    private String username;
    private String password;
    private String streetAddress;
    private String suburbAddress;
    private String stateAddress;
    private final String STAFFID;
    
    private static int staffCount = 0;

    public Staff(String firstName, String lastName, String phoneNumber, String emailAddress,
            String username, String password, String streetAddress, String suburbAddress, String stateAddress )
    {
        super(firstName, lastName, phoneNumber, emailAddress);
        this.username = username;
        this.password = password;
        this.streetAddress = streetAddress;
        this.suburbAddress = suburbAddress;
        this.stateAddress = stateAddress;
        
        Staff.incrementStaffCount();
        this.STAFFID = String.format("%05d", getStaffCount());
    }

    public Staff()
    {
        super();
        this.username = "undefined";
        this.password = "undefined";
        this.streetAddress = "undefined";
        this.suburbAddress = "undefined";
        this.stateAddress = "undefined";
        this.STAFFID = "undefined";
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getStreetAddress()
    {
        return streetAddress;
    }

    public String getSuburbAddress()
    {
        return suburbAddress;
    }

    public String getStateAddress()
    {
        return stateAddress;
    }

    public String getStaffID()
    {
        return STAFFID;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setStreetAddress(String streetAddress)
    {
        this.streetAddress = streetAddress;
    }

    public void setSuburbAddress(String suburbAddress)
    {
        this.suburbAddress = suburbAddress;
    }

    public void setStateAddress(String stateAddress)
    {
        this.stateAddress = stateAddress;
    }

    // cannot set STAFFID manually
    /*
    public void setStaffID(String STAFFID)
    {
        this.STAFFID = STAFFID;
    }
    */
    
    public static void setStaffCount(int staffCount)
    {
        Staff.staffCount = staffCount;
    }
    
    public static void incrementStaffCount() {
        staffCount += 1;
    }
    
    public static int getStaffCount() {
        return staffCount;
    }
    
    
    @Override
    public String toString() {
        return String.format( "%s\n%s\n%s, %s, %s\n%s\n%s",
                "Staff Details:",
                super.toString(),
                getStreetAddress(), getSuburbAddress(), getStateAddress(),
                getStaffID(),
                getUsername());
    }
    
    @Override
    public String toCSV() {
        return String.format( "%s,%s,%s,%s,%s,%s",
                getStaffID(), super.toCSV(), getStreetAddress(), getSuburbAddress(), getStateAddress(), getUsername() );
    }
    
}
