package Classes;

import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
//subClass-->"Market" inheritance from superClass-->"Employee " 
public class Market extends Employee {
//to initialized "id , date ,disc"-->offer
    private int offer_id;
    private String offer_date;
    private int offer_disc;
//Empty constructor "default"..
    public Market(){
    
    }

//this function to set of offer_Id.
    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }
//this function to set offer-->date
    public void setOffer_date(String offer_date) {
        this.offer_date = offer_date;
    }
//this function to set the discount of offer
    public void setOffer_disc(int offer_disc) {
        this.offer_disc = offer_disc;
    }
//this function to get id of offer
    public int getOffer_id() {
        return offer_id;
    }
//this function to get the date 
    public String getOffer_date() {
        return offer_date;
    }
//this function to get dicount -->offer
    public int getOffer_disc() {
        return offer_disc;
    }
    
//constructor -->to set the value of " id , name , password " of the market.  
    public Market(int id, String name, String pass){//"Specially Employee"
                this.setId(id);
                this.setName(name);
                this.setPassword(pass);
    }
    
//constructor -->passing id && date && discount and set this value
    public Market(int id, String date, int disc) {//"Specially Discount "
        this.setOffer_id(id);
        this.setOffer_date(date);
        this.setOffer_disc(disc);
    }

//this function to viewOffersthat used to select and show all offer that stored in dataBase 
    public void viewOffers(JTable table) {
        DefaultTableModel dtm = new DefaultTableModel();
        table.setModel(dtm);
        dtm.addColumn("Offer Id");
        dtm.addColumn("Offer Date");
        dtm.addColumn("Discount percentage");
        try {
            Statement st = db.getCon().createStatement();
            String sql = "select * from dbo.offers";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                //row contains --> some of getter "get offer ID & offer Date & offer Discount "
                dtm.addRow(new Object[]{rs.getInt("offer_id"), rs.getString("offer_date"), rs.getFloat("offer_discount")});
            }
        } catch (SQLException e) {//handle any errors in dataBase "SQL Exception"
            JOptionPane.showMessageDialog(null, "SQLException", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void addOffer(Market m) {
        try {
            Statement st = db.getCon().createStatement();
            //Insert into the taable of offers "column1-->ID  & column2 -->Date & column3--> discount " with New Values.
            String sql = "insert into dbo.offers(offer_id,offer_date,offer_discount) values('" + m.getOffer_id() + "','" + m.getOffer_date() + "','" + m.getOffer_disc() + "')";
            st.executeUpdate(sql);
       //massege thar appears on screen to make sure that product added Successfully 
            JOptionPane.showMessageDialog(null, "Added Successfully", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "SQLException", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

}