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
import java.sql.ResultSet;

/**
 *
 * @author Guntur
 */
public class Keyword {

    public Connection con = null;
    public PreparedStatement pst = null;
    String url_con = "jdbc:mysql://localhost:3306/pantauharga";
    String user = "root";
    String password = "";
    ResultSet rs = null;

    public Keyword() {
    }

    public ResultSet getAllQuery() {
        try {
            con = DriverManager.getConnection(url_con, user, password);
            pst = con.prepareStatement("SELECT id,query FROM query WHERE last_executed IS NULL");
            rs = pst.executeQuery();

        } catch (SQLException ex) {
            System.out.println("Terdapat Error di Keyword = " + ex.getMessage());
        }

        return rs;
    }

    public void updateLastExecuted(int id) {
        try {
            con = DriverManager.getConnection(url_con, user, password);
            pst = con.prepareStatement("UPDATE query SET last_executed=NOW() WHERE id=" + id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Terdapat Error Pertama = " + ex.getMessage());
        }
    }

}
