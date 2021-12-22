package entities;

import java.time.LocalDate;

/**
 * Assessment 1: Mass Vaccination System
 * @author DAgustin
 * 03 Dec 2021
 */
public class VaccineRecipient extends Person
{
    private LocalDate dateOfBirth;
    private Vaccine[] vaccineDoses = new Vaccine[2];
    private Gender gender;
    private final String RECIPIENTID;
    private int doseCount = 0;
    
    private static int recipientCount = 0;
    
    public enum Gender {
        Unspecified, Male, Female
    }

    public VaccineRecipient(String firstName, String lastName, String phoneNumber, String emailAddress,
            LocalDate dateOfBirth, Gender gender)
    {
        super(firstName, lastName, phoneNumber, emailAddress);
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.RECIPIENTID = String.format("%05d", getRecipientCount());
        
        this.SETFIRSTVACCDOSE( new Vaccine() );
        this.SETSECONDVACCDOSE( new Vaccine() );
        
        this.doseCount = 0;
    }
    
    public VaccineRecipient(String firstName, String lastName, String phoneNumber, String emailAddress,
            LocalDate dateOfBirth, String gender)
    {
        super(firstName, lastName, phoneNumber, emailAddress);
        this.dateOfBirth = dateOfBirth;
        
        try {
            this.gender = Gender.valueOf(gender);
        }
        catch( IllegalArgumentException ex ) {
            this.gender = Gender.Unspecified;
        }
        
        this.RECIPIENTID = String.format("%05d", getRecipientCount());
        
        this.SETFIRSTVACCDOSE( new Vaccine() );
        this.SETSECONDVACCDOSE( new Vaccine() );
        
        this.doseCount = 0;
    }

    public VaccineRecipient()
    {
        super();
        this.dateOfBirth = LocalDate.now();
        this.gender = Gender.Unspecified;
        this.RECIPIENTID = "undefined";
        
        this.SETFIRSTVACCDOSE( new Vaccine() );
        this.SETSECONDVACCDOSE( new Vaccine() );
        
        this.doseCount = 0;
    }

    public LocalDate getDateOfBirth()
    {
        return dateOfBirth;
    }

    public Vaccine[] getVaccineDoses()
    {
        return vaccineDoses;
    }

    public Gender getGender()
    {
        return gender;
    }
    
    public Vaccine getFirstVaccineDose()
    {
        return vaccineDoses[0];
    }
    
    public Vaccine getSecondVaccineDose()
    {
        return vaccineDoses[1];
    }

    public String getRECIPIENTID()
    {
        return RECIPIENTID;
    }

    public static int getRecipientCount()
    {
        return recipientCount;
    }
    
    public int getDoseCount()
    {
        return doseCount;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public void setVaccineDoses(Vaccine[] vaccineDoses)
    {
        this.vaccineDoses = vaccineDoses;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }
    
    public void setGender(String gender)
    {
        try {
            this.gender = Gender.valueOf(gender);
        }
        catch( IllegalArgumentException ex ) {
            //this.gender = Gender.Unspecified;
        }
    }
    
    public final void SETFIRSTVACCDOSE(Vaccine vaccine)
    {
        if( this.doseCount < 1 ) {
            this.doseCount = 1;
            this.vaccineDoses[0] = vaccine;
        }
    }
    
    public final void SETSECONDVACCDOSE(Vaccine vaccine)
    {
        if( this.doseCount == 1 ) {
            this.doseCount = 2;
            this.vaccineDoses[1] = vaccine;
        }
    }
    
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
    
    @Override
    public String toCSV() {
        return String.format( "%s,%s,%s,%s,%s\n",
                super.toCSV(), getGender(), getDateOfBirth(), getFirstVaccineDose().toCSV(), getSecondVaccineDose().toCSV() );
    }
}
