package entities;

import java.io.Serializable;

/**
 * Assessment 1: Mass Vaccination Management System
 *      Person Abstract Class contains all data and functions related to the person entity
 *      All common variables and functions are stored in this class and extended by the other classes
 * 
 * @author DAgustin
 * 03 Dec 2021
 */
public abstract class Person
{
    // initialise unique variables to this class
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;

    /**
     * Parameterised Constructor
     * variables are explained when not self-explanatory
     * @param firstName : String
     * @param lastName : Stirng
     * @param phoneNumber : String
     * @param emailAddress  : String
     */
    public Person(String firstName, String lastName, String phoneNumber, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    // default constructor setting the variables to "undefined"
    public Person() {
        this( "undefined", "undefined", "undefined", "undefined" );
    }

    /**
     * Get methods
     *      returns the various variables associated with this class
     * 
     * @return firstName, lastName, phoneNumber, emailAddress
     */
    
    public String getFirstName() {
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

    /**
     * Set methods
     *      enables the modification of the class variables
     */
    
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
    
    /**
     * Overriden toString() method to display class in terminal
     *      displays the string in a readable format, including newlines as necessary
     * @return String
     */
    @Override
    public String toString() {
        return String.format(
                "Name: %s %s\nPhone: %s\nEmail: %s",
                getFirstName(), getLastName(),
                getPhoneNumber(),
                getEmailAddress()
        );
    }
    
    /**
     * declaration of toCSV() method to write class variables to csv file separated by comma
     *      differs to the toString() method by only separating by commas.
     * @return String
     */
    public String toCSV() {
        return String.format( "%s,%s,%s,%s",
                getFirstName(), getLastName(), getPhoneNumber(), getEmailAddress());
    }
    
}
