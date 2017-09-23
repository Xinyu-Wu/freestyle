/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.pku.datasource;

import java.util.HashMap;
import java.util.Map;
import org.geotools.jdbc.JDBCDataStoreFactory;

/**
 *
 * @author wuxinyu
 */
public class PostgisDataStoreConfig {
    private final Map<String, Object> dataStoreParams = new HashMap<>();
    
    public PostgisDataStoreConfig(){
        //default values are given
        dataStoreParams.put(JDBCDataStoreFactory.PORT.key, "5432");
        dataStoreParams.put(JDBCDataStoreFactory.DBTYPE.key, "postgis");
        dataStoreParams.put(JDBCDataStoreFactory.VALIDATECONN.key, true);
        dataStoreParams.put(JDBCDataStoreFactory.FETCHSIZE.key, 1000);
    }
    
    public void setHost(final String host) {
        dataStoreParams.put(JDBCDataStoreFactory.HOST.key, host);
    }
    
    public void setDatabaseName(final String databaseName){
        dataStoreParams.put(JDBCDataStoreFactory.DATABASE.key, databaseName);
    }
    
    public void setPort(final String port) {
        dataStoreParams.put(JDBCDataStoreFactory.PORT.key, port);
    }
    
    public void setUser(final String user) {
        dataStoreParams.put(JDBCDataStoreFactory.USER.key, user);
    }
    
    public void setPassword(final String password) { 
    dataStoreParams.put(JDBCDataStoreFactory.PASSWD.key, password); 
    } 
 
    public void setDatabaseType(final String databaseType) { 
      dataStoreParams.put(JDBCDataStoreFactory.DBTYPE.key, databaseType); 
    } 

    public void setSchema(final String schema) { 
      dataStoreParams.put(JDBCDataStoreFactory.SCHEMA.key, schema); 
    } 

    public void setValidateConnection(final boolean validateConnection) { 
      dataStoreParams.put(JDBCDataStoreFactory.VALIDATECONN.key, validateConnection); 
    } 

    public Map<String, Object> getDataStoreParams() { 
      return this.dataStoreParams; 
    } 
}
