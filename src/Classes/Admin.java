package Classes;

import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

/**
 * Admin class is a subclass from Create class. class gives Admins ability to
 * manage employees in the Hyper Market(add, delete, list, search, update).
 *
 */
public class Admin extends Create {

    private String user_name, password;

    //Setting admin user name which will appear in DataBase and when admin changing his information.
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    //Setting a password to an admin which will help in validation when we check if password is correct or not.
    public void setPassword(String password) {
        this.password = password;
    }

    //Get admin user name to view it in editing profile for admins.
    public String getUser_name() {
        return user_name;
    }

    /**
     * Get admin password to check it in editing profile to make sure that the
     * user who is going to change password is the real user.
     *
     * @return
     *
     */
    public String getPassword() {
        return password;
    }

    //this variable will be used in check method to find out if the id is used befor or not.
    boolean oldID = false;

    //check method will check if the id of new employee is used befor or not.
    /**
     * it takes one integer parameter and check on it if it was in our DataBase
     * or not to make sure that there will be a unique ID for each employee
     *
     * @param id
     *
     */
    public void check(int id) {
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from dbo.users";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt("user_id") == id) {
                    oldID = true;
                    JOptionPane.showMessageDialog(null, "This id used before", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "SQL Exception", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Add method is responsable to add a new employee in the hyper market.
    /**
     * it takes four parameters String for name and password, int for id and
     * role_id.
     *
     * @param name
     * @param pass
     * @param id
     * @param role
     *
     */
    public void add(String name, String pass, int id, int role) {
        // switch cases to check the role value through which we can know which department will we add this employee to.
        switch (role) {
            case 2 -> {
                Inventory inv = new Inventory(id, name, pass);
                //check method mintioned above.
                check(inv.getId());

                try {
                    Statement st = db.getCon().createStatement();
                    String sql = "insert into dbo.users(user_id,user_name,user_password,roleID) values('" + inv.getId() + "','" + inv.getName() + "','" + inv.getPassword() + "','" + 2 + "')";
                    st.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "Added Successfully", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    if (!oldID) {
                        JOptionPane.showMessageDialog(null, "SQLException_Error", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            case 3 -> {
                Market mar = new Market(id, name, pass);
                //check method mintioned above.
                check(mar.getId());

                try {
                    Statement st = db.getCon().createStatement();
                    String sql = "insert into dbo.users(user_id,user_name,user_password,roleID) values('" + mar.getId() + "','" + mar.getName() + "','" + mar.getPassword() + "','" + 3 + "')";
                    st.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "Added Successfully", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    if (!oldID) {
                        JOptionPane.showMessageDialog(null, "SQLException_Error", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            case 4 -> {
                Seller sell = new Seller(id, name, pass);
                //check method mintioned above.
                check(sell.getId());

                try {
                    Statement st = db.getCon().createStatement();
                    String sql = "insert into dbo.users(user_id,user_name,user_password,roleID) values('" + sell.getId() + "','" + sell.getName() + "','" + sell.getPassword() + "','" + 4 + "')";
                    st.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "Added Successfully", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    if (!oldID) {
                        JOptionPane.showMessageDialog(null, "SQLException_Error", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            default -> {
                JOptionPane.showMessageDialog(null, "Select role of employee", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Search method which use to search for employee with id.
    /**
     * it takes two inputs, id of the employee you search for and JTable which
     * will view all employees in our DataBase ones we press on search button.
     *
     * @param id
     * @param table
     *
     */
    public void search(int id, JTable table) {
        boolean found = false;

        //Create Table of Employee to show result in it
        DefaultTableModel dtm = new DefaultTableModel();
        table.setModel(dtm);
        dtm.addColumn("Id");
        dtm.addColumn("Name");
        dtm.addColumn("Password");
        dtm.addColumn("roleID");

        //search for employee by his id
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from dbo.users";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                if (id == rs.getInt("user_ID")) {
                    found = true;
                    dtm.addRow(new Object[]{rs.getString("user_id"), rs.getString("user_name"), rs.getString("user_password"), rs.getString("roleID")});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQLException", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Handeling if id Not Found
        if (!found) {
            JOptionPane.showMessageDialog(null, "this id not found!!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //delete method used to delete an employee from our DataBase
    /**
     * it takes one parameter which is a JTable to view all employees on our
     * DataBase.to delete an employee you just select a row of employee you want
     * to delete and press delete button.
     *
     * @param table
     *
     */
    public void delet(JTable table) {

        DefaultTableModel dtm = (DefaultTableModel) table.getModel();

        //after selecting a row
        if (table.getSelectedRowCount() == 1) {

            //get id of the selected row 
            Object id = table.getValueAt(table.getSelectedRow(), 0);

            //delete employee from dtabase
            try {
                Statement st = db.getCon().createStatement();
                String sql = "delete from users where user_id = " + id + "";
                st.executeUpdate(sql);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "SQLException", "Error", JOptionPane.INFORMATION_MESSAGE);
            }

            //delete employee from viewed table
            dtm.removeRow(table.getSelectedRow());

        } else {
            //handel if table is empty
            if (table.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "there is no employees!", "Error", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Please select single row for delete.", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    //list employees method to list employees with specific role
    /**
     * it takes two parameters first is the role id of employees you wanna to
     * list them and the second one is JTable which will be used to view
     * information about those employees.
     *
     * @param roleID
     * @param table
     *
     */
    public void List(int roleID, JTable table) {

        //Create Employee Table to return result in it
        DefaultTableModel dtm = new DefaultTableModel();
        table.setModel(dtm);
        dtm.addColumn("Id");
        dtm.addColumn("Name");
        dtm.addColumn("Password");

        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from dbo.users";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                // condition to checkout the role
                if (roleID == rs.getInt("roleID")) {
                    dtm.addRow(new Object[]{rs.getString("user_id"), rs.getString("user_name"), rs.getString("user_password")});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQLException", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //view method gets all data about employees and view it in a table.
    public void view(JTable table) {

        //Create table 
        DefaultTableModel dtm = new DefaultTableModel();
        table.setModel(dtm);
        dtm.addColumn("Id");
        dtm.addColumn("Name");
        dtm.addColumn("Password");
        dtm.addColumn("Role_ID");

        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from dbo.users";
            ResultSet rs = st.executeQuery(sql);
            //fill table with employees data.
            while (rs.next()) {
                dtm.addRow(new Object[]{rs.getString("user_id"), rs.getString("user_name"), rs.getString("user_password"), rs.getString("roleID")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQLException", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //update employee data methods
    /**
     * it takes two parameter. we use it to send data of a selected row to
     * another JFrame in which we will update the selected employee data.
     *
     * @param e
     * @param id
     *
     */
    public void ViewEmp(Employee e, Object id) {
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from users where user_id = " + id;
            ResultSet rs = st.executeQuery(sql);
            // fill values in fields
            while (rs.next()) {
                e.setId(rs.getInt("user_id"));
                e.setRoleID(rs.getInt("roleID"));
                e.setName(rs.getString("user_name"));
                e.setPassword(rs.getString("user_password"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "SQLException Error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // update data after editing in fields
    /**
     * it takes an object as a parameter uses its methods to get stored data and
     * update the selected employee information
     *
     * @param e
     */
    public void UpdateEmp(Employee e) {
        try {
            Statement st = db.getCon().createStatement();
            String sql = "update users set user_name = '" + e.getName() + "', roleID = '" + e.getRoleID() + "', " + "user_password = '" + e.getPassword() + "' where user_id = '" + e.getId() + "'";
            st.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Updated Successfully", "Update", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
