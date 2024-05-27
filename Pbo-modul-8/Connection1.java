/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pbo;

import java.sql.DriverManager;

public class Connection1 {
    private static java.sql.Connection Connection1;
    
    public static java.sql.Connection getConnection1() {
        if (Connection1 == null) {
            try{
                String url = "jdbc:mysql://localhost/pbo";
                String user = "root";
                String pass = "";
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                Connection1 = DriverManager.getConnection(url, user, pass);
                System.out.println("Connection Successfully");
            }catch (Exception e) {
                System.out.println("Error b");
            }
        }
        return Connection1;
    }
    public static void main(String[] args) {
        getConnection1();
    }
    
}
