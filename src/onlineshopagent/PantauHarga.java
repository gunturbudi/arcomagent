/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pantauharga;

import java.util.HashSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guntur
 */
public class PantauHarga {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Keyword key = new Keyword();
        Scrap st = new Scrap();
        Item it = new Item();

        ResultSet queries = key.getAllQuery();

        try {
            while (queries.next()) {
                int id = queries.getInt(1);
                String keyword = queries.getString(2);
                
                st.setQueryId(id);
                st.setKeyword(keyword);
                st.ScrapBhinneka();
                key.updateLastExecuted(id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PantauHarga.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (key.rs != null) {
                    key.rs.close();
                }
                if (key.pst != null) {
                    key.pst.close();
                }
                if (key.con != null) {
                    key.con.close();
                }

            } catch (SQLException ex) {
                System.out.println("Terdapat Error di Keyword = " + ex.getMessage());
            }
        }

    }

}
