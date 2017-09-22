/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.pku.datasource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.FeatureSource;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.postgis.PostgisNGJNDIDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;

/**
 *
 * @author wuxinyu
 */
public class PostgreSQLManager {
    Connection conn = null;
    
    /**
     * default constructor with parameters.
     * @param host
     * @param port
     * @param database
     * @param user
     * @param pwd
     */
//    public PostgreSQLManager(String host, String port, String database,
//            String user, String pwd){
//        try {
//            String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
//            Class.forName("org.postgresql.Driver");
//            conn = DriverManager.getConnection(url, user, pwd);
//        } catch (ClassNotFoundException | SQLException ex) {
//            Logger.getLogger(PostgreSQLManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    
    /**
     * params for connection to Database
     * @param host ipv4
     * @param port
     * @param database db name
     * @param user username
     * @param pwd password
     * @return 
     */
    public Connection connetToPostgre(String host, String port, String database,
            String user, String pwd ){
        try {
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, pwd);
            return conn;
//            System.out.println(conn.isClosed());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PostgreSQLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * necessary to state
     */
    public void disconnection(){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSQLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * get tablename list based on constructor of the class
     * @return tablename list 
     */
    public List getTableNames(){
        try {
            Statement st = conn.createStatement();
            String sql = "select relname as TABLE_NAME from pg_class c \n" +
                    "where  relkind = 'r' and relname not like 'pg_%' and relname not like 'sql_%'  order by relname";
            ResultSet rs = st.executeQuery(sql);
            List tableList = new ArrayList();
//            ResultSetMetaData md = rs.getMetaData();
            while(rs.next()){
                tableList.add(rs.getString(1));
            }
            return tableList;
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSQLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public JDBCDataStore importShpFromPostgre(String dbtype, String host, 
            String port, String database, String user, String pwd){
        JDBCDataStore datastore = new JDBCDataStore();
        PostgisNGDataStoreFactory factory = new PostgisNGDataStoreFactory();
        Map params = new HashMap();
        params.put("dbtype", dbtype);
        params.put("host", host);
        params.put("port", Integer.parseInt(port));
        params.put("database", database);
        params.put("user", user);
        params.put("passwd", pwd);
        
        try {
            datastore = (JDBCDataStore) factory.createDataStore(params);
            return datastore;
        } catch (IOException ex) {
            Logger.getLogger(PostgreSQLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (datastore != null)
            System.out.println("Connected to spatial database.");
        else
            System.out.println("Not connected to spatial database.");
        return null;
    }
    
    public FeatureSource getFeatureByTableName(JDBCDataStore ds, String tablename){
        try {
            FeatureSource fs = ds.getFeatureSource(tablename);
            return fs;
        } catch (IOException ex) {
            Logger.getLogger(PostgreSQLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void main(String[] args){
        String host = "localhost";
        String port = "5432";
        String database = "gis";
        String user = "postgres";
        String pwd = "123";
        PostgreSQLManager manager = new PostgreSQLManager();
        manager.connetToPostgre(host, port, database, user, pwd);
        manager.importShpFromPostgre("postgis", host, port, database, user, pwd);
//        List list = manager.getTableNames();
//        System.out.println(Arrays.toString(list.toArray()));
        manager.disconnection();
    }
    
}
