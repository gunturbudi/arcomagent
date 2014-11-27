/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pantauharga;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Guntur
 */
public class Item {

    int query_id;
    String site;
    String name;
    double price;
    double discount;
    double price_discount;
    String stock;
    String description;
    String img_url;
    String url;

    private Connection con = null;
    private PreparedStatement pst = null;
    String url_con = "jdbc:mysql://localhost:3306/pantauharga";
    String user = "root";
    String password = "";

    public Item() {
      
    }

    public void saveItem() {
        try {
            con = DriverManager.getConnection(url_con, user, password);
            pst = con.prepareStatement("INSERT IGNORE INTO `query_result`\n"
                    + "            (`query_id`,\n"
                    + "             `site`,\n"
                    + "             `name`,\n"
                    + "             `price`,\n"
                    + "             `discount`,\n"
                    + "             `price_discount`,\n"
                    + "             `stock`,\n"
                    + "             `description`,\n"
                    + "             `img_url`,\n"
                    + "             `url`,\n"
                    + "             `created`)\n"
                    + "VALUES ('"+query_id+"',\n"
                    + "        '"+site+"',\n"
                    + "        '"+name+"',\n"
                    + "        '"+price+"',\n"
                    + "        '"+discount+"',\n"
                    + "        '"+price_discount+"',\n"
                    + "        '"+stock+"',\n"
                    + "        '"+description+"',\n"
                    + "        '"+img_url+"',\n"
                    + "        '"+url+"',\n"
                    + "        NOW())");
            
            pst.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Terdapat Error Pertama = " + ex.getMessage());
        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                System.out.println("Terdapat Error Kedua = " + ex.getMessage());
            }
        }
    }

}
