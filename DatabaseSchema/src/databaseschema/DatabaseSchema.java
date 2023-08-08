/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package databaseschema;

import java.awt.Container;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author mrich
 */
public class DatabaseSchema {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
        String sql="Select CustomerName from Customers;";
        businessLayer(getData(sql));
        }
    public static void businessLayer(ResultSet dataName) throws SQLException{
        //Sets up gui
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = frame.getContentPane();
        container.setLayout(new FlowLayout());
        

        JLabel text;
        text = new JLabel();
        dataName.last();
        int dataNum = dataName.getRow();
        dataName.first();
        text.setText("Number of customers= "+dataNum+"\n");
        container.add(text);
        while(dataName.next()){JLabel nameText = new JLabel();
        nameText.setText("*"+dataName.getString("CustomerName")+"*");
       container.add(nameText);
        }

        frame.setVisible(true);
    }
    
    public static ResultSet getData(String sql){
        PreparedStatement stat;
        ResultSet names;


        try {
            RowSetFactory fact = RowSetProvider.newFactory();
            CachedRowSet crs= fact.createCachedRowSet();
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind","root","Password")) { 
                stat = conn.prepareStatement(sql);
                names = stat.executeQuery();
                crs.populate(names);
                System.out.print("Connection successful.");
                conn.close();
                return crs ;
            }

            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseSchema.class.getName()).log(Level.SEVERE, null, ex);
            System.out.print("Connection failed.");
            return null;
        }
        
   }
   
    
}
