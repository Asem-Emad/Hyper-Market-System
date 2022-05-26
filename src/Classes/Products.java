/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;
import java.util.* ;

public class Products extends Create{
    
    private double price , discount, offer_price;
    private int quantity , minquant ;
    private String EX_date ;
    Date date = new Date();
    public static long counter = 0 ;
    
    public Products(){
        
    }
    
    //constractor (set values) to add pro
    public Products(int id, String name, double price, int quant, int minQuant, String Ex_Date){
        this.setId(id);
        this.setName(name);
        this.setPrice(price);
        this.setQuantity(quant);
        this.setMinquant(minQuant);
        this.setEX_date(Ex_Date);
    }

    //Setter
    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMinquant(int minquant) {
        this.minquant = minquant;
    }

    public void setEX_date(String EX_date) {
        this.EX_date = EX_date;
    }

    public void setDiscount(double discount){
        this.discount = (discount * this.price) / 100;
    }
    
    public void setOffer_price(double offer_price) {
        this.offer_price = offer_price;
    }
    
    //Getter
    public double getDiscount(){
        return discount;
    }

    public double getOffer_price() {
        return offer_price;
    }
    
    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMinquant() {
        return minquant;
    }

    public String getEX_date() {
        return EX_date;
    }
}
