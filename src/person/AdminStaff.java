package person;

/**
 * Assessment 1: Mass Vaccination System
 * @author DAgustin
 * 03 Dec 2021
 */
public class AdminStaff extends Staff
{
    private PositionType positionType;
    
    public enum PositionType {
        None, FullTime, PartTime, Volunteer
    }

    public AdminStaff(String firstName, String lastName, String phoneNumber, String emailAddress, String username, String password, String streetAddress, String suburbAddress, String stateAddress,
            PositionType positionType)
    {
        super(firstName, lastName, phoneNumber, emailAddress, username, password, streetAddress, suburbAddress, stateAddress);
        this.positionType = positionType;
    }
    
    public AdminStaff(String firstName, String lastName, String phoneNumber, String emailAddress, String username, String password, String streetAddress, String suburbAddress, String stateAddress,
            String positionType)
    {
        super(firstName, lastName, phoneNumber, emailAddress, username, password, streetAddress, suburbAddress, stateAddress);
        try {
            this.positionType = PositionType.valueOf(positionType);
        }
        catch( IllegalArgumentException ex ) {
            this.positionType = PositionType.None;
        }
        
    }

    public AdminStaff()
    {
        super();
        this.positionType = PositionType.None;
    }

    public PositionType getPositionType()
    {
        return positionType;
    }

    public void setPositionType(PositionType positionType)
    {
        this.positionType = positionType;
    }
    
    public void setPositionType(String positionType)
    {
        try {
            this.positionType = PositionType.valueOf(positionType);
        }
        catch( IllegalArgumentException ex ) {
            //this.positionType = PositionType.None;
        }
    }
    
    @Override
    public String toString() {
        return String.format( "%s%s\n%s\n",
                "Admin ", super.toString(),
                getPositionType());
    }
    
    @Override
    public String toCSV() {
        return String.format( "%s,%s,%s\n",
                getClass().getSimpleName(), super.toCSV(), getPositionType() );
    }
}
