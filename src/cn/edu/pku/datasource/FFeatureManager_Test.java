/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.pku.datasource;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.ShapefileFeatureLocking;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.DefaultFeatureCollections;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.FeatureVisitor;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;

/**
 *要素管理类，实现对要素的增删改查
 * @author sq
 */
public class FFeatureManager_Test {
    
    /**
     * 将要素添加到图层中
     * @param sTargetLayer  目标图层
     * @param sTargetFeature    目标要素
     * @return
     */
    public static boolean addFeatureToLayer(Layer sTargetLayer, SimpleFeature sTargetFeature) throws Exception
    {
        SimpleFeatureSource sfs = (SimpleFeatureSource) sTargetLayer.getFeatureSource();
        SimpleFeatureIterator sfi = sfs.getFeatures().features();  
        while(sfi.hasNext())
        {
            SimpleFeature sCurfeature = sfi.next();  
        }  
        sfi.next().setValue(sTargetFeature);
        return false;         
    }
    
    public static boolean addFeatureToFeatureSource(SimpleFeatureSource sTargetFeatureSource, SimpleFeature sTargetFeature) throws Exception
    {
        SimpleFeatureIterator sfi = sTargetFeatureSource.getFeatures().features();  
        final SimpleFeatureType TYPE = DataUtilities.createType("Location",  
                "location:Point," + "NAME:String," + "INFO:String,"  
                        + "OWNER:String");  
        
        Map<String, Serializable> params = new HashMap<String, Serializable>(); 
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = DefaultFeatureCollections.newCollection();
        DefaultFeatureCollection   collection2 = new DefaultFeatureCollection();  
        collection2.add(sTargetFeature);
        SimpleFeatureStore featureStore = (SimpleFeatureStore) sTargetFeatureSource; 
        featureStore.getFeatures().features().next();
        ShapefileFeatureLocking featureLocking = (ShapefileFeatureLocking)sTargetFeatureSource;
        // 创建一个事务  
        Transaction transaction = new DefaultTransaction("create");
        featureStore.setTransaction(transaction);  
        try {  
            featureStore.addFeatures(collection2);
            // 提交事务       
            transaction.commit();  
            transaction.close();  
            return true;
        } catch (Exception problem) {  
            problem.printStackTrace();  
            transaction.rollback();  
            return false;
        } finally {  
            //transaction.close();  
        }  
        /*
        while(sfi.hasNext())
        {
            SimpleFeature sCurfeature = sfi.next();  
        }  
        try
        {
            sfi.next().setValue(sTargetFeature);
            return true;
        }
        catch(Exception e)
        {
            return false;         
        }*/
    }
    
    /**
     * 生成一个点要素
     * @param latitude  纬度
     * @param longitude 经度
     * @param poiid 点ID
     * @param meshid    MESHID
     * @param owner 拥有者
     * @return  生成的点要素
     * @throws Exception
     */
    public static SimpleFeature createOnePointFeature(double latitude,double longitude,String poiid,String meshid,String owner) throws Exception
    {
        final SimpleFeatureType TYPE = DataUtilities.createType("Location",  
                "location:Point," + // <- the geometry attribute: Point type  
                "POIID:String,"/* + // <- a String attribute  
                "MESHID:String," + // a number attribute  
                "OWNER:String"  */
            );  
        GeometryFactory geometryFactory = new GeometryFactory();  
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);  
            Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));  
            Object[] obj = {point, poiid/*, meshid, owner*/};  
            SimpleFeature sfeature = featureBuilder.buildFeature(null, obj);
            return sfeature;
    }
    
    
    public static void addFeatureToLayerTest()
    {
        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        ShapefileManager sShpFileManager=new ShapefileManager();
        SimpleFeatureSource sfs=sShpFileManager.readShpFromFile(file);
        try
        {
            SimpleFeature sPoint = createOnePointFeature(116.123456789,39.120001,"2050003092","0","340881");
       System.out.println(addFeatureToFeatureSource(sfs,sPoint));
        
        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
        
        
    }
      public static void main(String[] args) {  
    try{    
        //定义属性  
        final SimpleFeatureType TYPE = DataUtilities.createType("Location",  
            "location:Point," + // <- the geometry attribute: Point type  
            "POIID:String," + // <- a String attribute  
            "MESHID:String," + // a number attribute  
            "OWNER:String"  
        );  
        DefaultFeatureCollection   collection = new DefaultFeatureCollection();  
        GeometryFactory geometryFactory = new GeometryFactory();  
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);  
  
        double latitude = Double.parseDouble("116.123456789");  
        double longitude = Double.parseDouble("39.12099");  
        String POIID = "126";  
        String MESHID = "0";  
        String OWNER = "sq";  
  
        /* Longitude (= x coord) first ! */  
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));  
        Object[] obj = {point, POIID, MESHID, OWNER};  
        SimpleFeature feature = featureBuilder.buildFeature(null, obj);  
        collection.add(feature); 
  
        File newFile = new File("F:\\ArcGISDoc\\suzhou\\EmptyShp.shp");  /*
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();  
        Map<String, Serializable> params = new HashMap<String, Serializable>();  
        params.put("url", newFile.toURI().toURL());  
        params.put("create spatial index", Boolean.TRUE);  
        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createDataStore(params);  
        newDataStore.createSchema(TYPE);  
        newDataStore.forceSchemaCRS(DefaultGeographicCRS.WGS84);  */
  
        Transaction transaction = new DefaultTransaction("What is this???");/*
        String typeName = newDataStore.getTypeNames()[0];  
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName); */ 
        FileDataStore store = FileDataStoreFinder.getDataStore(newFile);
        SimpleFeatureSource featureSource = store.getFeatureSource();
        
  
        if (featureSource instanceof SimpleFeatureStore) {  
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;  
            featureStore.setTransaction(transaction);  
            try {  
                featureStore.addFeatures(collection);  
                transaction.commit();  
            } catch (Exception problem) {  
                problem.printStackTrace();  
            transaction.rollback();  
            } finally {  
                transaction.close();  
            }  
        } else {  
            System.out.println("typeName" + " does not support read/write access");  
        }  
    } catch (Exception e) {  
        e.printStackTrace();  
    }  
}
    
    
}
