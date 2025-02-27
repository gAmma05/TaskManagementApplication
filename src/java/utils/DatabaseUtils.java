/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author vothimaihoa
 */
public class DatabaseUtils {

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        Map<String, String> envVariables = EnvReader.readEnvFile();
        
        String dbUrl = envVariables.get("DB_URL");
        String dbUser = envVariables.get("DB_USER");
        String dbPassword = envVariables.get("DB_PASSWORD");
        
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}
