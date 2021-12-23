package mvms;

import entities.Staff;
import java.util.HashMap;
import java.util.Map;

/**
 * Assessment 1: Mass Vaccination Management System
 *      Authenticator class contains all functions related to the management, accessing and storing 
 *          user credentials
 *      contains a CREDENTIALS variable storing usernames and password combinations
 *          utilises HashMap/dictionary to call values
 * 
 * @author DAgustin
 * 03 Dec 2021
 */
public class Authenticator
{
    // initialise CREDENTIALS
    private static final Map< String, String > CREDENTIALS = new HashMap< String, String>();
        
    // put staff username and password to hashmap
    public static void loadCredentials( Staff staff ) {
        CREDENTIALS.put( staff.getUsername(), staff.getPassword() );
    }
    
    // put staff username and password directly
    public static void loadCredentials( String username, String password ) {
        CREDENTIALS.put( username, password );
    }
    
    // check whether username and password exists - used when logging in
    public static boolean checker( String username, String password ) {
        String validUsername = CREDENTIALS.get(username);
        return validUsername != null && validUsername.equals(password);
    }
    
    // check whether username exists - used when registering
    public static boolean usernameExists( String username ) {
        String validUsername = CREDENTIALS.get(username);
        return validUsername != null;
    }
    
    // remove user in the hashmap
    public static void removeCredentials( Staff staff ) {
        CREDENTIALS.remove( staff.getUsername() );
    }
    
    // remove user in the hashmap using the username
    public static void removeCredentials( String username ) {
        CREDENTIALS.remove( username );
    }
    
}
