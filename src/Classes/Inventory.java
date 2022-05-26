package Classes;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;
/** Inventory class is a subclass from Employee class.
 There is a new data field (notif) 
 The class give the inventory employee access to manage the products through  several methods
 such as adding new product, deleting a specific product, updating the data of existing product
 and finally keep in touch with sales employees through messages and notifications to return the expired product
 and follow up if a product reached the minimum quantity to add new quantity  **/
public class Inventory extends Employee {
 
    Products p = new Products();    // An object from product class to make operations on it
    String notif;                   // A new data field, every inventory employee has a notifications from the sale employees
    boolean oldID = false;          // old_id is a global flag that we use to check whether the product id is aleardy found/exist or not
    
    // default constructor
    public Inventory(){
    }
   /** constructor that use in Admin class while adding a new inventory class and insert/set his id,name, and password**/
    public Inventory(int id, String name, String pass){
                this.setId(id);
                this.setName(name);
                this.setPassword(pass);
    }
    
    /**viewNot method used to read the notification messages from the SQL notification table and view it in a jTable in the jFrame**/
    public void viewNot(JTable table) {
        DefaultTableModel dtm = new DefaultTableModel();
        table.setModel(dtm);
        dtm.addColumn("Message");

        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from dbo.notification";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                dtm.addRow(new Object[]{ rs.getString("notf")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
        
    /**AddNew method is used to add new product data and insert this data in the products SQL table**/
    public void AddNew(Products p){
        if(p.name.matches("[a-zA-z]+") && check_date(p.getEX_date()) == 1)
        {
        check(p.getId());
        regestration r = new regestration(); //we use this object to get returned id and insert the inventory_id who add it in the product information
        try {
            Statement st = db.getCon().createStatement();
            String sql = "insert into product(prod_id,prod_name,prod_price,prod_quant,prod_ex_date,inv_emp_id,prod_min_quant) "
                    + "values(" + p.getId() + ",'" + p.getName() + "'," + p.getPrice() + "," + p.getQuantity() +  ",'" + p.getEX_date() + "',"+ r.getReturnID() +"," + p.getMinquant() + " )";
            st.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Added Successfully", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            if (!oldID) {
                JOptionPane.showMessageDialog(null,ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        }
        else
            JOptionPane.showMessageDialog(null, "invaild input", "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    
    /**delete_Product method is used to delete selected product from the jTable and delete this product from the SQL database**/
    public void delete_Product(JTable table) {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        if (table.getSelectedRowCount() == 1) { //if the user select a spacific raw in the jTable, it will take the product id and delete it from the SQL database
            Object id = table.getValueAt(table.getSelectedRow(), 0);
            try {
                Statement st = db.getCon().createStatement();
                String sql = "delete from product where prod_id = " + id + "";
                st.executeUpdate(sql);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            }
            dtm.removeRow(table.getSelectedRow());
        } else { //if there is now selected raw we will check if the table is empty or not, then view an info message 
            if (table.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Table is empty!", "Error", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Please select single row for delete.", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

   /**check method is used to make sure and check whether the product id is found/exist in the hyper market or not**/
    public void check(int id) {
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from dbo.product";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt("prod_id") == id) {
                    oldID = true;
                    JOptionPane.showMessageDialog(null, "This id used before", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
   /**UpdatePro method is used to take a specific product and update/edit its data**/
    public void UpdatePro(Products p) {
        try {
            Statement st = db.getCon().createStatement();
            String sql = "update product set prod_name = '"+p.getName()+"', prod_price = '"+p.getPrice()+"', prod_quant = '"+p.getQuantity()+"', prod_ex_date = '"+p.getEX_date()+"', prod_min_quant = '"+p.getMinquant()+"' where prod_id = '"+p.getId()+"'";
            st.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Updated Successfully", "Update", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   /**viewProd method is used to take a specific product id and view its data in a text fields in jFrame to allow the inventory employee update/ edit it**/
    public void ViewProd(Products p , Object id) {
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from product where prod_id = " + id;
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                p.setId(rs.getInt("prod_id"));
                p.setName(rs.getString("prod_name"));
                p.setPrice(rs.getDouble("prod_price"));
                p.setQuantity(rs.getInt("prod_quant"));
                p.setMinquant(rs.getInt("prod_min_quant"));
                p.setEX_date(rs.getString("prod_ex_date"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public int check_date(String ex_date) {                           // check date is passed or not 
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyy");
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd");
        LocalDateTime now = LocalDateTime.now();
        int current_y = Integer.parseUnsignedInt(dtf.format(now));
        int current_m = Integer.parseUnsignedInt(dtf1.format(now));
        int current_d = Integer.parseUnsignedInt(dtf2.format(now));

        String[] values = ex_date.split("/");
        int ex_day = Integer.parseInt(values[0]);
        int ex_month = Integer.parseInt(values[1]);
        int ex_year = Integer.parseInt(values[2]);

        if (current_y == ex_year) {
            if (current_m == ex_month) {
                if (current_d == ex_day) {
                    return 0;

                } else if (current_d > ex_day) {
                    return 0;
                }
            } else if (current_m > ex_month) {
                return 0;
            }
        } else if (current_y > ex_year) {
            return 0;
        }
        return 1;
    }
}