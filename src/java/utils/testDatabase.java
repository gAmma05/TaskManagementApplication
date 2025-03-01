/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.SQLException;
import static utils.DBConnection.getConnection;

/**
 *
 * @author gAmma
 */
public class testDatabase {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection con = getConnection();
        if (con != null) {
            System.out.println("OK");
        } else {
            System.out.println("Not ok");
        }
    }
}
