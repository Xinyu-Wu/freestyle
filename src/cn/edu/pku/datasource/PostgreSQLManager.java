/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.pku.datasource;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.Transaction;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author wuxinyu
 */
public class PostgreSQLManager {
    Connection conn = null;
    private PostgisDataStoreConfig postgisConfig = new PostgisDataStoreConfig();
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
    
    /**
     * get JDBCDataStore from PostgreSQL
     * @param dbtype postgis
     * @param host 
     * @param port 
     * @param database 
     * @param user
     * @param pwd
     * @return 
     */
    public JDBCDataStore getDataStoreFromPostgre(String dbtype, String host, 
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
        
        //check it
        if (datastore != null)
            System.out.println("Connected to spatial database.");
        else
            System.out.println("Not connected to spatial database.");
        return null;
    }
    
    /**
     * divide importshpfromdatabase into getDataSource and getFeature
     * getDataSource means read all tables from one database
     * getFeature means read feature source from one table
     * @param ds source build with Factory
     * @param tablename tablename in database
     * @return FeatureSource, could be drawn
     */
    public FeatureSource getFeatureByTableName(JDBCDataStore ds, String tablename){
        try {
            FeatureSource fs = ds.getFeatureSource(tablename);
            return fs;
        } catch (IOException ex) {
            Logger.getLogger(PostgreSQLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * feature 2 shapefile, shp2sql by command line
     * store featureCollection to postgre
     * @param collection FeatureCollection, interface, come from FeatureSource
     * @param tablename TableName in Database
     */
    public void storeFeatureCollectionToPostgis(
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection,
            String tablename){
        try { 
            DataStore dataStore = DataStoreFinder.getDataStore(postgisConfig.getDataStoreParams());
            SimpleFeatureType featureType = collection.getSchema();
            System.out.println(featureType);
            SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
            builder.init(featureType);
            builder.setName(tablename);
            
            SimpleFeatureType newFeatureType = builder.buildFeatureType();
            dataStore.createSchema(newFeatureType);
            
            Transaction transaction = new DefaultTransaction("create");
            
            String typeName = newFeatureType.getTypeName();
            SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
            
            if (featureSource instanceof SimpleFeatureStore) {
                SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
                featureStore.setTransaction(transaction);
                try {
                    featureStore.addFeatures(collection);
                    transaction.commit();
                } catch (IOException ex) {
                    transaction.rollback();
                    Logger.getLogger(PostgreSQLManager.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    transaction.close();
                }
                System.exit(0);//success
            }
            else {
                System.exit(1);
            }
            if (featureSource != null) {
                featureSource.getDataStore().dispose();
            }
            System.out.println("done!");
        } catch (IOException ex) {
            Logger.getLogger(PostgreSQLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * shp 2 postgis by shp2sql command.exe
     * @param filepath filepath without filename, just folder
     * @param filename file name with .shp
     * @param database database name
     * @param user user name
     */
    public void transformShapefileToPostgis(String filepath, String filename, String database, String user){
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.directory(new File(filepath));
            List<String> commands = new ArrayList();
            commands.add("cmd.exe");
            commands.add("/c");
            String cmd = "shp2pgsql " + filename + " | psql -d " + database + " -U " + user;
            commands.add(cmd);
            pb.command(commands);
            Process process = pb.start();
            if (process.waitFor(10000, TimeUnit.MICROSECONDS)) {
                System.out.println("Timeout");
                return;
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(PostgreSQLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        /**
         * test part
         */
        String host = "localhost";
        String port = "5432";
        String database = "gis";
        String user = "postgres";
        String pwd = "123";
        PostgreSQLManager manager = new PostgreSQLManager();
        manager.connetToPostgre(host, port, database, user, pwd);
//        manager.transformShapefileToPostgis("D:/", "test.shp", database, user);
        //read shapefile from disk
        ShapefileManager shpManager = new ShapefileManager();
        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        if (file == null)
            System.out.print("wrong file");
        SimpleFeatureSource featureSource = shpManager.readShpFromFile(file);
        manager.postgisConfig.setDatabaseName(database);
        manager.postgisConfig.setHost(host);
        manager.postgisConfig.setPassword(pwd);
        manager.postgisConfig.setUser(user);
        manager.postgisConfig.setValidateConnection(true);//why this
//        manager.postgisConfig.setSchema(featureSource.getSchema().getTypeName());
        try {
            SimpleFeatureCollection featureCollection = featureSource.getFeatures();
            //drop suffix name
            String name = file.getName().substring(0,file.getName().lastIndexOf("."));
            manager.storeFeatureCollectionToPostgis(featureCollection, name);
        } catch (IOException ex) {
            Logger.getLogger(PostgreSQLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        manager.disconnection();
    }
    
}
