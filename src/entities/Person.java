package entities;

import java.io.Serializable;

/**
 * Assessment 1: Mass Vaccination System
 * @author DAgustin
 * 03 Dec 2021
 */
public abstract class Person implements Serializable
{
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;

    public Person(String firstName, String lastName, String phoneNumber, String emailAddress)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public Person()
    {
        this( "undefined", "undefined", "undefined", "undefined" );
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }
    
    @Override
    public String toString() {
        return String.format(
                "%s %s\n%s\n%s",
                getFirstName(), getLastName(),
                getPhoneNumber(),
                getEmailAddress()
        );
    }
    
    public String toCSV() {
        return String.format( "%s,%s,%s,%s",
                getFirstName(), getLastName(), getPhoneNumber(), getEmailAddress());
    }
    
}
