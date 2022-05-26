/*
    validation for users
 */
package Classes;

import java.sql.*;
import javax.swing.*;

public class regestration 
{
    database db = new database();
    
    private static String ReturnID ;

    public String getReturnID() {
        return ReturnID;
    }
    
    public boolean cheackNameAndPass (String id ,String pass,String role)      //if id or pass exict then complete 
    {
        ReturnID  = id ;
        int count = 0;
        try 
        {
            Connection gcon = db.getCon();
            Statement ru = gcon.createStatement();
            String sql = "select count(1) from users where user_id = "+id+" and user_password = '"+pass+"' and roleID = "+role+" ";
            ResultSet result = ru.executeQuery(sql);
            while(result.next())
            {
                count = result.getInt(1);
            }
            if (count == 1)
                return true;
            else if (count == 0)
                return false;    
        }
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}
