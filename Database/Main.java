/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author ychabcho
 */
import java.sql.*;

public class Main {

    Connection conn;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //ATTENTION AU PORT
            String url = "jdbc:mysql://localhost:3306/coffeebreak"; 
            conn = DriverManager.getConnection(url, "root", "root");
            
            doInsert("Chocolat", 5);
            
            doInsert("Capuccino", 6);
            
            doUpdateAllPlusOne();
            
            doDeletePrice(6);
            
            doDeleteTable();
            
            conn.close();
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IllegalAccessException ex) {
            System.err.println(ex.getMessage());
        } catch (InstantiationException ex) {
            System.err.println(ex.getMessage());
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /* permet de vérifier si l'entité existe, si un COF_NAME existe deja, 
    * ça realise l'update, autrement ca permet l'INSERT */
    private int doCheck(String str, int checkPrice) {
        int isOk = 0;
        System.out.println("[CHECKING OUT existings]");
        String query = "SELECT COF_NAME, PRICE FROM COFFEES "
                + "WHERE COF_NAME ='" + str + "'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next() == false) {
                System.out.print("\n[pas de tel café] ... ");
                isOk = 1;
            } else {
                isOk = 0;
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return isOk;
    }

    private void doInsert(String str, int price) {
        if (doCheck(str, price) == 1) {
            System.out.print("\n[Performing INSERT] ... ");
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO COFFEES "
                        + "VALUES ('" + str + "', " + price + ")");
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        } else {
            doUpdate(str, price);
        }
    }

    private void doDeleteTable() {
        System.out.print("\n[ERASING TABLE coffees] ... ");
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("TRUNCATE TABLE  `coffees`");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void doDeletePrice(int maxPrice) {
        System.out.print("\n[Performing DELETE] ... ");
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM COFFEES "
                    + "WHERE PRICE>" + maxPrice + "");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private void doUpdate(String str, int price) {
        System.out.print("\n[Performing UPDATE] ... ");
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE COFFEES SET PRICE=" + price + " "
                    + "WHERE COF_NAME='" + str + "'");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void doUpdateAllPlusOne() {

        System.out.print("\n[Performing GLOBAL UPDATE] ... ");


        ////
        System.out.println("[OUTPUT FROM SELECT]");
        String query = "SELECT COF_NAME, PRICE FROM COFFEES";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            System.out.println("[OLD PRICES]");

            while (rs.next()) {
                String s = rs.getString("COF_NAME");
                float n = rs.getFloat("PRICE");
                System.out.println(s + "   " + n);
                try {
                    Statement st2 = conn.createStatement();
                    n=n+1;
                    st2.executeUpdate("UPDATE COFFEES SET PRICE="+n+
                            "WHERE COF_NAME='"+s+"'");
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        ////
                    System.out.println("[NEW PRICES = OLD PRICES + 1]");

    }
}
