package Classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/** This is a super class aims to create any objects in the project and give id and name for each object 
 * 
 *(Employee) class and (Products)class are 2 classes that extend/inherit from this class(create) **/
public abstract class Create {
    /*we create the data field protected to access them throw the child classes*/
    protected int id;
    protected String name;
    database db = new database();
     
    //setter method to set id for the current object
    public void setId(int id) {
        this.id = id;
    }

    //setter method to set name for the current object
    public void setName(String name) {
        this.name = name;
    }
    
    //getter method to access and get the id for the object
    public int getId() {
        return id;
    }

    //getter method to access and get the name for the object
    public String getName() {
        return name;
    }
    //Update data in database
    public void UpdateInfo(int id, String name, String pass) {
        regestration r = new regestration();
        try {
            Statement st = db.getCon().createStatement();
            String sql = "update users set user_name = '" + name + "' , user_password = '" + pass + "' where user_id = " + r.getReturnID();
            st.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Updated Successfully", "Update", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Check about id before update data
    public boolean ifID(int id, String name, String pass) {
        boolean trueID = false;
        regestration r = new regestration();
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from users";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt("user_id") == Integer.parseInt(r.getReturnID()) && Integer.parseInt(r.getReturnID()) == id) {
                    name = rs.getString("user_name");
                    pass = rs.getString("user_password");
                    trueID = true;
                    return true;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "SQLException me", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (!trueID) {
            JOptionPane.showMessageDialog(null, "It's not your ID", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    public boolean TextCheck(String data) {
        if (!data.matches("[a-zA-Z]+(.*)")) {
            JOptionPane.showMessageDialog(null, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    public boolean NumCheck(String data) {
        if (data.matches("[0-9]+") || data.matches("[-+]?[0-9]+\\.?[0-9]+")) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean DateCheck(String data) {
        if (data.matches("^\\d{1,2}\\/\\d{1,2}\\/\\d{4}$")) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
