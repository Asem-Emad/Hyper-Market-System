package Classes;

import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

/**
 * Employee is a subclass class extends/inherit from (create) class.  *
 * Employee class is a parent class to 3 subclasses (Market employees, inventory
 * employees, sales employees/sellers) in Employee class we give every employee
 * (object) a role_id and password.
 *
 * Here is 2 categories of methods that allow the employees to make operations:
 * 1) manage their profile(update profile & forget password) 2) manage the hyper
 * market products, list according price/expire date/ quantity, search for item,
 * make reports, view products.....etc *
 */
public class Employee extends Create {

    protected String password; //password from 5 digits for each employee
    protected int RoleID;      // role_id to determine weather the user is Admin(1), Market employee(2), inventory employees(3), sales employees/sellers(4)

    //setter method to set password for the current object
    public void setPassword(String password) {
        this.password = password;
    }

    //getter method to access and get the password for the object
    public String getPassword() {
        return password;
    }

    //setter method to set role_id for the current object
    public void setRoleID(int RoleID) {
        this.RoleID = RoleID;
    }
    //getter method to access and get role_id for the object

    public int getRoleID() {
        return RoleID;
    }

    //default constructor
    public Employee() {

    }

    //constructor that initial/set the data of the current employee(object)
    public Employee(int emp_id, int roleID, String emp_name, String emp_pass) {
        this.setId(emp_id);
        this.setRoleID(roleID);
        this.setName(emp_name);
        this.setPassword(emp_pass);
    }

    /**
     * This method works while log in and the user forget his/her password The
     * user have to choose his role_id,then enter his name and his id. If the
     * data is correct, his password will appear in an info message. If the data
     * is not correct, an error message will appear to tell him contact the ADMIN*
     */
    public void forgetPassword(JTextField id, JTextField username, int role) {
        boolean isEmp = false;
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from dbo.users";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                if ((rs.getInt("user_id") == Integer.parseInt(id.getText())) && (rs.getString("user_name").compareTo(username.getText()) == 0) && (rs.getInt("roleID") == role)) {
                    isEmp = true;
                    JOptionPane.showMessageDialog(null, "Your Password is " + rs.getString("user_password"), "Your Password", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (!isEmp) {
            JOptionPane.showMessageDialog(null, "Please Contact With Your Admin", "Block", JOptionPane.ERROR_MESSAGE);
        }
        id.setText("");
        username.setText("");
    }

    /**
     * This method make a list of products according the price By reading
     * item(id-name-price)from SQL database products table and view the data in
     * jTable in jFrame*
     */
    public void list_Price(JTable table) {

        DefaultTableModel dtm = new DefaultTableModel();
        table.setModel(dtm);
        dtm.addColumn("Product_Id");
        dtm.addColumn("Product_Name");
        dtm.addColumn("Product_Price");
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select prod_id,prod_name,prod_price from dbo.product order by prod_price";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                dtm.addRow(new Object[]{rs.getString("prod_id"), rs.getString("prod_name"), rs.getString("prod_price")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method make a list of products according the quantity By reading
     * item(id-name-quantity)from SQL database products table and view the data
     * in jTable in jFrame*
     */
    public void list_Quant(JTable table) {
        DefaultTableModel dtm = new DefaultTableModel();
        table.setModel(dtm);
        dtm.addColumn("Product_Id");
        dtm.addColumn("Product_Name");
        dtm.addColumn("Product_Quantity");

        try {
            Statement st = db.getCon().createStatement();
            String sql = "select prod_id,prod_name,prod_quant from dbo.product order by prod_quant";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                dtm.addRow(new Object[]{rs.getString("prod_id"), rs.getString("prod_name"), rs.getString("prod_quant")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * This method make a report about the expire date of products and list
     * them.By reading item(id-name-expire date)from SQL database products
 table and view the data in jTable in jFrame*
     * @param table
     */
    public void expireDateReport(JTable table) {
        DefaultTableModel dtm = new DefaultTableModel();
        table.setModel(dtm);
        dtm.addColumn("Product_Id");
        dtm.addColumn("Product_Name");
        dtm.addColumn("Product_EX_Date");
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select prod_id,prod_name,prod_ex_date from dbo.product order by prod_price";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                dtm.addRow(new Object[]{rs.getString("prod_id"), rs.getString("prod_name"), rs.getString("prod_ex_date")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * This method view all products in the hyper market and list them. By
     * reading item(id-name-price-quantity-expire date-inventory id that insert
     * the product)from SQL database products table and view the data in jTable
     * in jFrame*
     */
    public void view(JTable table) {
        DefaultTableModel dtm = new DefaultTableModel();
        table.setModel(dtm);
        dtm.addColumn("Product_Id");
        dtm.addColumn("Product_Name");
        dtm.addColumn("Product_Price");
        dtm.addColumn("Product_Quantity");
        dtm.addColumn("Product_EX_date");
        dtm.addColumn("Inventory employee ID");
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from product";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                dtm.addRow(new Object[]{rs.getString("prod_id"), rs.getString("prod_name"), rs.getString("prod_price"), rs.getString("prod_quant"), rs.getString("prod_ex_date"), rs.getString("inv_emp_id")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQLException", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method take a product id as a parameter and search for this id in
     * the products table in the SQL database. If it is found, the product data
     * will appear in the jTable in the jFrame if it isn't found, error message
     * (This id not found!!) will appear*
     */
    public void search(JTextField idField, JTable table) {
        boolean found = false;
        DefaultTableModel dtm = new DefaultTableModel();
        table.setModel(dtm);
        dtm.addColumn("Product_Id");
        dtm.addColumn("Product_Name");
        dtm.addColumn("Product_Price");
        dtm.addColumn("Product_Quantity");
        dtm.addColumn("Product_EX_date");
        dtm.addColumn("Inventory employee ID");
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from dbo.product";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                if (Integer.parseInt(idField.getText()) == rs.getInt("prod_id")) {
                    found = true;
                    dtm.addRow(new Object[]{rs.getString("prod_id"), rs.getString("prod_name"), rs.getString("prod_price"), rs.getString("prod_quant"), rs.getString("prod_ex_date"), rs.getString("inv_emp_id")});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (!found) {
            JOptionPane.showMessageDialog(null, "This id not found!!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //this method set jTable title as a default values. 

    public void setTableTitle(JTable table) {
        DefaultTableModel dtm = new DefaultTableModel();
        table.setModel(dtm);
        dtm.addColumn("Product_Id");
        dtm.addColumn("Product_Name");
        dtm.addColumn("Product_Price");
        dtm.addColumn("Product_Quantity");
        dtm.addColumn("Product_EX_date");
        dtm.addColumn("Inventory employee ID");
    }
}
