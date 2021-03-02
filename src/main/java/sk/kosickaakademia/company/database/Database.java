package sk.kosickaakademia.company.database;

import sk.kosickaakademia.company.log.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    String url= "jdbc:mysql:/itsovy.sk:3306/company";
    String username="mysqluser";
    String password="Kosice2021!";
    Log log = new Log();
    public Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            log.print("Connection successful");
            return con;
        } catch (SQLException | ClassNotFoundException throwables) {
           log.error(throwables.toString());
        }
        return null;
    }
    public void closeConnection(Connection con){
if (con!=null) {
    try {
        con.close();
        log.print("Connection closed");
    } catch (SQLException throwables) {
        log.error(throwables.toString());
    }
}
    }
}
