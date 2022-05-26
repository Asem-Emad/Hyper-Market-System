/*
    this class for sellar who make orders
 */
package Classes;

import java.sql.*;
import javax.swing.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;

/*
    
 */
public class Seller extends Employee {
    
    private static double price;

    public Seller() {

    }

    public Seller(int id, String name, String pass) {                   // for read seller info.
        this.setId(id);
        this.setName(name);
        this.setPassword(pass);
    }

    public void list(JTable table) {                                // for display products table for seller 
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

    public double getPrice() {
        return price;
    }

    public boolean cheack_exict(int id, int quant) {                       // to check is that product id exict or not 
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from product";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                if (id == rs.getInt("prod_id")) {
                    return true;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, "this id not found!!", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public void Make_oreder(Products p) {                              // main method thats make ordar
        int id = p.getId();
        int quant = p.getQuantity();
        if (cheack_exict(id, quant)) {
            try {
                Statement sb = db.getCon().createStatement();
                String sql2 = "select * from product where prod_id = " + id;
                ResultSet result = sb.executeQuery(sql2);
                int dbquant = 0;
                String ex = "";
                double pri = 0.0;
                while (result.next()) {
                    ex = result.getString("prod_ex_date");
                    dbquant = result.getInt("prod_quant");
                    pri = result.getDouble("prod_price");
                }
                if (check_quant(id, dbquant) == 1 && check_ex_date(ex, id) == 1) {
                    dbquant = dbquant - quant;
                    String sql3 = "update product set prod_quant = " + dbquant + " where prod_id = " + id;
                    sb.executeUpdate(sql3);
                    JOptionPane.showMessageDialog(null, "DONE", "SUCCES", JOptionPane.INFORMATION_MESSAGE);
                    price = pri * quant;
                    price = price - (price * (check_offer() / 100));
                }
            } catch (SQLException ee) {
                JOptionPane.showMessageDialog(null, ee.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public int check_quant(int id, int quant) {                                 //check product quant. is bigger than zero or less than min.quant.        
        try {
            Statement sb = db.getCon().createStatement();
            String sql = "select * from product where prod_id = " + id;
            ResultSet result = sb.executeQuery(sql);
            //int min_quant =  result.getInt("prod_min_quant");
            int min_quant = 0;
            while (result.next()) {
                min_quant = result.getInt("prod_min_quant");
            }
            if (quant == 0) {
                JOptionPane.showMessageDialog(null, "This product was Saled Out", "Error", JOptionPane.ERROR_MESSAGE);
                String sql2 = "insert into notification(notf) values ('This product was Saled Out id = " + id + "')";
                sb.executeUpdate(sql2);
            } else if (quant <= min_quant) {
                String sql2 = "insert into notification(notf) values ('WE reached min. quantity from Product id = " + id + "')";
                sb.executeUpdate(sql2);
                return 1;
            } else if (quant > min_quant) {
                return 1;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    public int check_ex_date(String ex_date, int id) {                           // check expiry date is passed or not 
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
                    try {
                        Statement ex = db.getCon().createStatement();
                        String sql = "insert into notification(notf) values ('The Ex_Date is Today for Product id = " + id + " ')";
                        ex.executeUpdate(sql);
                        JOptionPane.showMessageDialog(null, "Expiry_Date will end Today", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex1) {
                        JOptionPane.showMessageDialog(null, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    return 0;

                } else if (current_d > ex_day) {
                    try {
                        Statement ex = db.getCon().createStatement();
                        String sql = "insert into notification(notf) values ('The Ex_Date was Passed for Product id = " + id + " ')";
                        ex.executeUpdate(sql);
                        JOptionPane.showMessageDialog(null, "Expiry_Date was passed", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex1) {
                        JOptionPane.showMessageDialog(null, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    return 0;
                }
            } else if (current_m > ex_month) {
                try {
                    Statement ex = db.getCon().createStatement();
                    String sql = "insert into notification(notf) values ('The Ex_Date was Passed for Product id = " + id + " ')";
                    ex.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "Expiry_Date was passed", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex1) {
                    JOptionPane.showMessageDialog(null, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                return 0;
            }

        } else if (current_y > ex_year) {
            try {
                Statement ex = db.getCon().createStatement();
                String sql = "insert into notification(notf) values ('The Ex_Date was Passed for Product id = " + id + " ')";
                ex.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Expiry_Date was passed", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex1) {
                JOptionPane.showMessageDialog(null, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return 0;
        }
        return 1;
    }

    public double check_offer() {                                         // check date offer is today or not and aplay discount on total price 
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyy");
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd");
        LocalDateTime now = LocalDateTime.now();
        int current_y = Integer.parseUnsignedInt(dtf.format(now));
        int current_m = Integer.parseUnsignedInt(dtf1.format(now));
        int current_d = Integer.parseUnsignedInt(dtf2.format(now));
        double discount = 0;
        String offer_date = "";
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from offers";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                offer_date = rs.getString("offer_date");
                String[] values = offer_date.split("/");
                int off_day = Integer.parseInt(values[0]);
                int off_month = Integer.parseInt(values[1]);
                int off_year = Integer.parseInt(values[2]);
                if (current_y == off_year && current_m == off_month && current_d == off_day) {
                    discount = rs.getDouble("offer_discount");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "SQL Exception", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return discount;
    }

    public String today() // method gives today day 
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String today = dtf.format(now);
        return today;
    }

    public double Cancel_oreder(int id, int quant) {
        try {
            Statement sb = db.getCon().createStatement();
            String sql2 = "select * from product where prod_id = " + id;
            ResultSet result = sb.executeQuery(sql2);
            int dbquant = 0;
            double pri = 0.0;
            while (result.next()) {
                dbquant = result.getInt("prod_quant");
                pri = result.getDouble("prod_price");
            }
            dbquant = dbquant + quant;
            String sql3 = "update product set prod_quant = " + dbquant + " where prod_id = " + id;
            sb.executeUpdate(sql3);
            String sql = "insert into notification(notf) values ('" + quant + " items of product with id = " + id + " has been returned')";
            sb.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "DONE", "Cancel SUCCES", JOptionPane.INFORMATION_MESSAGE);
            return getPrice() - (pri * quant);
        } catch (SQLException ee) {
            JOptionPane.showMessageDialog(null, ee.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return 0.0;
        }
    }
}
