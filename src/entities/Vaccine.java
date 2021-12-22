package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Assessment 1: Mass Vaccination System
 * @author DAgustin
 * 03 Dec 2021
 */
public class Vaccine implements Serializable
{
    private LocalDate vaccinationDate;
    private String vaccineID;
    private VaccineType vaccineName;
    private boolean status = false;
    private String administeredBy = "";
    
    public enum VaccineType {
        None, AstraZeneca, Pfizer, Moderna
    }

    public Vaccine(VaccineType vaccineName, LocalDate vaccinationDate)
    {
        this.vaccinationDate = vaccinationDate;
        this.vaccineName = vaccineName;
        this.vaccineID = ( vaccineName.toString().substring(0,1) + UUID.randomUUID().toString().substring(0,8) ).toUpperCase();
    }
    
    public Vaccine( String vaccineName, String vaccinationDate ) {
        
        try {
            this.vaccineName = VaccineType.valueOf( vaccineName );
        }
        catch( IllegalArgumentException ex ) {
            this.vaccineName = VaccineType.None;
        }
        
        // try catch here - if not parse-able, set default
        this.vaccinationDate = LocalDate.parse( vaccinationDate );
        this.vaccineID = ( vaccineName.substring(0,1) + UUID.randomUUID().toString().substring(0,8) ).toUpperCase();
    }
    
    public Vaccine( String vaccineName, LocalDate vaccinationDate ) {
        
        try {
            this.vaccineName = VaccineType.valueOf( vaccineName );
        }
        catch( IllegalArgumentException ex ) {
            this.vaccineName = VaccineType.None;
        }
        
        this.vaccinationDate = vaccinationDate;
        this.vaccineID = ( vaccineName.substring(0,1) + UUID.randomUUID().toString().substring(0,8) ).toUpperCase();
    }
    
    public Vaccine()
    {
        this( VaccineType.None, LocalDate.now() );
        this.vaccineID = "undefined";
    }

    public LocalDate getVaccinationDate()
    {
        return vaccinationDate;
    }

    public String getVaccineID()
    {
        return vaccineID;
    }
    
    public VaccineType getVaccineName()
    {
        return vaccineName;
    }
    
    public boolean getStatus()
    {
        return status;
    }
    
    public String getAdministeredBy()
    {
        return administeredBy;
    }

    public void setVaccinationDate(LocalDate vaccinationDate)
    {
        this.vaccinationDate = vaccinationDate;
    }

    public void setVaccineID(String vaccineID)
    {
        this.vaccineID = vaccineID;
    }

    public void setVaccineName(VaccineType vaccineName)
    {
        this.vaccineName = vaccineName;
    }
    
    public void setVaccineName(String vaccineName)
    {
        try {
            this.vaccineName = VaccineType.valueOf( vaccineName );
        }
        catch( IllegalArgumentException ex ) {
            // do nothing
        }
    }
    
    public void setConfirmed()
    {
        if( status == false )
            this.status = true;
        else
            System.out.println( "Vaccine already confirmed" );
    }
    
    public void setAdministeredBy( String administeredBy )
    {
        if( status == false ) {
            this.administeredBy = administeredBy;
            System.out.println( administeredBy );
        }
        else
            System.out.println( "Dose already administered" );
    }
    
    @Override
    public String toString() {
        return String.format( "%s\n%s\n%s\n",
                getVaccineName(),
                getVaccineID(),
                getVaccinationDate()
        );
    }
    
    public String toCSV() {
        return String.format( "%s,%s,%s",
                getVaccineID(), getVaccineName(), getVaccinationDate() );
    }
    
}
