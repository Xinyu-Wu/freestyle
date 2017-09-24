/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.pku.datasource;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/***
 * @Target(ElementType.TYPE)
 * @author wuxinyu
 */
public class ShapefileManager {
    /**
     * read shapefile from file storaged on disk
     * @param file FileType and end with .shp
     * @return a set of features
     */
    public SimpleFeatureSource readShpFromFile(File file){
        try {
            if (file == null) {
                return null;
            }
            FileDataStore store = FileDataStoreFinder.getDataStore(file);
            SimpleFeatureSource featureSource = store.getFeatureSource();
            return featureSource;
        } catch (IOException ex) {
            Logger.getLogger(ShapefileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * write shapefile to disk
     * @param layer map.layer
     * @param path destination filepath
     * @return ture or false
     */
    public boolean writeShpToFile(Layer layer, String path){
        try {
            //create shape destination file object
            Map<String, Serializable> params = new HashMap<>();
            FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
            params.put(ShapefileDataStoreFactory.URLP.key, new File(path).toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) factory.createNewDataStore(params);
            //set properties
            SimpleFeatureSource sfs = (SimpleFeatureSource) layer.getFeatureSource();
            //下面这行还有其他写法，根据源shape文件的simpleFeatureType可以不用retype，而直接用fs.getSchema设置  
            ds.createSchema(sfs.getSchema());
            
            //设置writer  
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], 
                    Transaction.AUTO_COMMIT);  
            //写记录  
            SimpleFeatureIterator it = sfs.getFeatures().features();  
            try {  
                while (it.hasNext()) {
                    SimpleFeature feature = it.next();  
                    SimpleFeature fNew = writer.next();  
                    fNew.setAttributes(feature.getAttributes());  
                    writer.write();  
                }  
            } finally {  
                it.close();  
            }  
            writer.close();  
            ds.dispose();  
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ShapefileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //write test case
    public void writeShpTest(){
        try {
            File file = JFileDataStoreChooser.showOpenFile("shp", null);
            if (file == null) {
                return;
            }
            FileDataStore store = FileDataStoreFinder.getDataStore(file);
            SimpleFeatureSource featureSource = store.getFeatureSource();
            
            // Create a map content and add our shapefile to it
            Style style = SLD.createSimpleStyle(featureSource.getSchema());
            Layer layer = new FeatureLayer(featureSource, style);
            if (this.writeShpToFile(layer, "D://test.shp"))
                System.out.println("written down");
        } catch (IOException ex) {
            Logger.getLogger(ShapefileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //read test case
    public void readShpTest(){
        try {
            File file = JFileDataStoreChooser.showOpenFile("shp", null);
            SimpleFeatureSource sfs = this.readShpFromFile(file);
            SimpleFeatureIterator iterator = sfs.getFeatures().features();
            
            while (iterator.hasNext()){
                SimpleFeature feature = iterator.next();
                Iterator<Property> pro = feature.getProperties().iterator();
                
                while (pro.hasNext())
                    System.out.println(pro.next().toString());
                iterator.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ShapefileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
//        ReadShapefile reader = new ReadShapefile();
//        reader.readShpTest();
FFeatureManager_Test.addFeatureToLayerTest();
        ShapefileManager sm = new ShapefileManager();
        sm.writeShpTest();
    }
}
