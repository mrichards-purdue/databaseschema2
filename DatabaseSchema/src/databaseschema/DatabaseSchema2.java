/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package databaseschema;

import java.awt.Container;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author mrich
 */
public class DatabaseSchema2 {  
    
    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */

    public static void main(String[] args) throws SQLException { 
        //Sets up gui
        JFrame frame =new JFrame();
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        container.setLayout(new GridLayout());
        
        

        frame.add(addPane());
        frame.setVisible(true);
        }
    
    
    public static JTabbedPane addPane() throws SQLException{
        String user = loginButton.Instance.username;
        String pass= loginButton.Instance.password;
        String url= loginButton.Instance.server;
        String database= loginButton.Instance.database;
        JTabbedPane pane = new JTabbedPane(JTabbedPane.TOP);
        ArrayList dataList = getData(url,database,user,pass);
        for(int k = 0;k<dataList.size();k++){
        JTable table = new JTable();        JScrollPane scroll = new JScrollPane(table);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
       
        
        CachedRowSet data=(CachedRowSet) dataList.get(k);
        
        ResultSetMetaData mD = data.getMetaData();
            int columns = mD.getColumnCount();
            String[] colName = new String[columns];
                for(int j =0;j<columns;j++)
               
                {colName[j]=mD.getColumnName(j+1);
                model.setColumnIdentifiers(colName);
                }
        
            
           while(data.next()){
                ArrayList<String> rows = new ArrayList<String>();
                for(int i = 0;i<columns;i++){
                    
                    String row = data.getString(i+1);
                    rows.add(row); 
                }
                model.addRow(rows.toArray());
            }
            table.setModel(model);
            pane.addTab(mD.getTableName(k+1), scroll);}
            return pane;
        }
        
    
    public static ArrayList getData( String url, String database, String user, String pass){
        //Passes data for all accessible databases using sql statements populated with user entered data
        PreparedStatement stat,stat2;
        ResultSet data,dbName;
        String dbURLString ="jdbc:mysql://"+url+"/"+database;
        String dbSql="Show tables from "+database;
        String sqlState;
        
        ArrayList<ResultSet> dataList;
        dataList = new ArrayList<ResultSet>();

                
        try {
            
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(dbURLString,user,pass)) { 
                stat = conn.prepareStatement(dbSql);
                dbName=stat.executeQuery();
               while(dbName.next()){
                RowSetFactory fact = RowSetProvider.newFactory();
                CachedRowSet crs = fact.createCachedRowSet();
                
               sqlState="Select * from "+dbName.getString("Tables_in_"+database)+";";
                stat2 = conn.prepareStatement(sqlState);
                data = stat2.executeQuery();
                crs.populate(data);
                dataList.add(crs);
                }
              
                conn.close();
                loginButton.Instance.dispose();
                return dataList;
            }

            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseSchema2.class.getName()).log(Level.SEVERE, "Failed to get data", ex);
            loginButton.Instance.errLabel.setVisible(true);
        }
        
        return (null);
        
   }
   
    
}
