/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import diploma.model.Fingerprint;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

/**
 *
 * @author user54
 */
public class datas {

    public static Connection con;
    public static Statement st;
    public static String Query;

    public datas() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fingerprint", "root", "");
    }

    public void insert(double[][] orient, double[][] period, String name) throws SQLException {
        Query = "insert into fingerprint.referencetable (orientationmap,periodmap,filename) values('" + Arrays.toString(orient) + "','" + Arrays.toString(period) + "','" + name + "')";
        st = con.createStatement();
        st.executeUpdate(Query);
    }

    public ResultSet select(Fingerprint file) throws SQLException {
        Query = "select reference from fingerprint.test where filename= '" + file + "'";
        st = con.createStatement();
        ResultSet rs = st.executeQuery(Query);
        return rs;
    }

    public void truncate(String table) throws SQLException {
        Query = "truncate " + table + "";
        // System.out.println(Query);
        try {
            st = con.createStatement();
            st.executeQuery(Query);
        } catch (Exception e) {
            // System.out.println(e);
        }
    }
}
