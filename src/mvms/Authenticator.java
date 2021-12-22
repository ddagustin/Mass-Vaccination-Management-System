package mvms;

import entities.Staff;
import java.util.HashMap;
import java.util.Map;

/**
 * Assessment 1: Mass Vaccination System
 * @author DAgustin
 * 03 Dec 2021
 */
public class Authenticator
{
    private static final Map< String, String > CREDENTIALS = new HashMap< String, String>();
        
    public static void loadCredentials( Staff staff )
    {
        CREDENTIALS.put( staff.getUsername(), staff.getPassword() );
    }
    
    public static void loadCredentials( String username, String password )
    {
        CREDENTIALS.put( username, password );
    }
    
    public static boolean checker( String username, String password )
    {
        String validUsername = CREDENTIALS.get(username);
        return validUsername != null && validUsername.equals(password);
    }
    
    public static boolean usernameExists( String username )
    {
        String validUsername = CREDENTIALS.get(username);
        return validUsername != null;
    }
    
    public static void removeCredentials( Staff staff )
    {
        CREDENTIALS.remove( staff.getUsername() );
    }
    
    public static void removeCredentials( String username )
    {
        CREDENTIALS.remove( username );
    }
    
}
