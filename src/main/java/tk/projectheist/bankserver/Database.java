/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author roy
 */
public class Database {
    private Connection connection = null;
    private final String host = "jdbc:mysql://localhost:3306/projectheist";
    private final String userName = "root";
    private final String userPass = "Pasword";
    private Statement stmt = null;
    private static Database database = null;
    
    private Database(){
        this.connect();
    }
    
    public static synchronized Database getDatabase(){
        if(database == null)
            database = new Database();
        return database;
        
    }
    
    private void connect()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(host, userName, userPass);
            stmt = connection.createStatement();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public long getBalance(int rekeningnummer) throws SQLException{
        long saldo = 0;
        PreparedStatement ps = connection.prepareStatement("SELECT balance FROM account WHERE id=?");
        ps.setInt(1, rekeningnummer);
        ResultSet set = ps.executeQuery();
        set.next();
        saldo = set.getInt("balance");
        return saldo;
    }
    
    public boolean withdraw(int rekeningnummer, long amount) throws SQLException{
        long saldo = getBalance(rekeningnummer);
        if(saldo >= amount){
            System.out.println("U mag pinnen!");
            PreparedStatement ps = connection.prepareStatement("UPDATE accounts SET balance = ? WHERE rekeningnummer = ?");
            ps.setLong(1, (saldo - amount));
            ps.setInt(2, rekeningnummer);
            return ps.execute();
        }
    }

}
