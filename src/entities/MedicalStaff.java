package entities;

/**
 * Assessment 1: Mass Vaccination System
 * @author DAgustin
 * 03 Dec 2021
 */
public class MedicalStaff extends Staff
{
    private String registrationNumber;
    private String affiliation;
    private Category category;
    
    public enum Category {
        Unspecified, RegisteredNurse, GeneralPractitioner, Pharmacist
    }

    public MedicalStaff(String firstName, String lastName, String phoneNumber, String emailAddress, String username, String password, String streetAddress, String suburbAddress, String stateAddress,
            String registrationNumber, String affiliation, Category category)
    {
        super(firstName, lastName, phoneNumber, emailAddress, username, password, streetAddress, suburbAddress, stateAddress);
        this.registrationNumber = registrationNumber;
        this.affiliation = affiliation;
        this.category = category;
    }
    
    public MedicalStaff(String firstName, String lastName, String phoneNumber, String emailAddress, String username, String password, String streetAddress, String suburbAddress, String stateAddress,
            String registrationNumber, String affiliation, String category)
    {
        super(firstName, lastName, phoneNumber, emailAddress, username, password, streetAddress, suburbAddress, stateAddress);
        this.registrationNumber = registrationNumber;
        this.affiliation = affiliation;
        
        try {
            this.category = Category.valueOf(category);
        }
        catch( IllegalArgumentException ex ) {
            this.category = Category.Unspecified;
        }
    }

    public MedicalStaff()
    {
        super();
        this.registrationNumber = "undefined";
        this.affiliation = "undefined";
        this.category = Category.Unspecified;
    }

    public String getRegistrationNumber()
    {
        return registrationNumber;
    }

    public String getAffiliation()
    {
        return affiliation;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setRegistrationNumber(String registrationNumber)
    {
        this.registrationNumber = registrationNumber;
    }

    public void setAffiliation(String affiliation)
    {
        this.affiliation = affiliation;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }
    
    public void setCategory(String category)
    {
        try {
            this.category = Category.valueOf(category);
        }
        catch( IllegalArgumentException ex ) {
            //this.category = Category.Unspecified;
        }
    }
    
    @Override
    public String toString() {
        return String.format( "%s%s\n%s\n%s\n%s\n",
                "Medical ", super.toString(),
                getCategory(),
                getRegistrationNumber(),
                getAffiliation());
    }
    
    @Override
    public String toCSV() {
        return String.format( "%s,%s,%s,%s,%s\n",
                getClass().getSimpleName(), super.toCSV(), getCategory(), getRegistrationNumber(), getAffiliation() );
    }
}
