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
    private final String userPass = "";
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
        if(connection.isClosed())
            connect();
        long saldo = 0;
        PreparedStatement ps = connection.prepareStatement("SELECT balance FROM accounts WHERE id=?");
        ps.setInt(1, rekeningnummer);
        ResultSet set = ps.executeQuery();
        set.next();
        saldo = set.getInt("balance");
        return saldo;        
    }
    
   
    
    public boolean authenticate(int rekeningnummer, int pincode, String kaartnummer) throws SQLException{
        if(connection.isClosed())
            connect();
        PreparedStatement ps = connection.prepareStatement("SELECT COUNT(passes.id) as a FROM passes WHERE passes.accounts_id = ? and passes.pin = ? and passes.passnumber = ?");
        ps.setInt(1, rekeningnummer);
        ps.setInt(2, pincode);
        ps.setString(3, kaartnummer);
        ResultSet set = ps.executeQuery();
        set.next();
        int count = set.getInt("a");
        return (count == 1);
    }
    
    public long maximumWithdraw(int rekeningnummer) throws SQLException{
        try{
        if(connection == null || connection.isClosed())
            connect();
        PreparedStatement ps = connection.prepareStatement("SELECT products.max_debit FROM accounts, products WHERE accounts.id = ? and accounts.products_id = products.id");
        ps.setInt(1, rekeningnummer);
        ResultSet set = ps.executeQuery();
        set.next();
        double maxWithrawAmount = set.getDouble(1)*100;
        
        long balance = this.getBalance(rekeningnummer);
        maxWithrawAmount += balance;
        return (long)(maxWithrawAmount );
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    
    public long performWithdraw(int rekeningnummer, long amount){
        try{
            long balance = getBalance(rekeningnummer);
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `transactions` (`completion_date`, `amount`, `machines_id`, `receiver_bank_id`, `sender_bank_id`, `receiver_id`, `sender_id`) VALUES (NOW(), ?, 1, 1, 1, -1, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, amount);
            ps.setInt(2, rekeningnummer);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            ps = connection.prepareStatement("UPDATE accounts SET balance = ? WHERE id = ?");
            ps.setLong(1, balance - amount);
            ps.setInt(2, rekeningnummer);
            ps.execute();
            
            rs.next();
            return rs.getInt(1);
        }catch( Exception e){
            e.printStackTrace();
        }
        return 0;
    }

}
