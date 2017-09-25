/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.pku.datasource;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

/**
 * 对SimpleFeature进行管理的类
 *
 * @version 1.0.1
 * @author sq
 */
public class SimpleFeatureManager {

    public static Point createOnePoint(double latitude,double longitude)
    {      
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        return point;
    }
    
    public static LineString createOneLineString (double[] lat,double[] lng) throws Exception
    {
        GeometryFactory geometryFactory = new GeometryFactory();
        int latLength=lat.length;
        int lngLength=lng.length;
        if(latLength != lngLength) throw new Exception("数据长度不匹配");
        ArrayList<Coordinate> coordinateList=new ArrayList<Coordinate> ();
        for (int i = 0; i < latLength; i++) {
            coordinateList.add(new Coordinate(lng[i], lat[i]));
        }       
        Coordinate[] coorArray = new Coordinate[coordinateList.size()]; 
        LineString lineString = geometryFactory.createLineString(coordinateList.toArray(coorArray));
        return lineString;
    }
    
    public static LineString createOneLineString (Coordinate[] coordinateArray)throws Exception
    {
        GeometryFactory geometryFactory = new GeometryFactory();
        LineString lineString = geometryFactory.createLineString(coordinateArray);
        return lineString;
    }
    
    public static LinearRing createOneLinearRing (double[] lat,double[] lng) throws Exception
    {
        GeometryFactory geometryFactory = new GeometryFactory();
        int latLength=lat.length;
        int lngLength=lng.length;
        if(latLength != lngLength) throw new Exception("数据长度不匹配");
        ArrayList<Coordinate> coordinateList=new ArrayList<Coordinate> ();
        for (int i = 0; i < latLength; i++) {
            coordinateList.add(new Coordinate(lng[i], lat[i]));
        }       
        Coordinate[] coorArray = new Coordinate[coordinateList.size()]; 
        LinearRing linearRing = geometryFactory.createLinearRing(coordinateList.toArray(coorArray));
        return linearRing;
    }
    
    public static LinearRing createOneLinearRing (Coordinate[] coordinateArray)throws Exception
    {
        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing linearRing = geometryFactory.createLinearRing(coordinateArray);
        return linearRing;
    }
    
    public static Polygon createOnePolygon (double[] lat,double[] lng) throws Exception
    {
        GeometryFactory geometryFactory = new GeometryFactory();
        int latLength=lat.length;
        int lngLength=lng.length;
        if(latLength != lngLength) throw new Exception("数据长度不匹配");
        ArrayList<Coordinate> coordinateList=new ArrayList<Coordinate> ();
        for (int i = 0; i < latLength; i++) {
            coordinateList.add(new Coordinate(lng[i], lat[i]));
        }       
        Coordinate[] coorArray = new Coordinate[coordinateList.size()]; 
        Polygon polygon = geometryFactory.createPolygon(coordinateList.toArray(coorArray));
        return polygon;
    }
    
    public static  Polygon createOnePolygon (Coordinate[] coordinateArray)throws Exception
    {
        GeometryFactory geometryFactory = new GeometryFactory();
        Polygon polygon = geometryFactory.createPolygon(coordinateArray);
        return polygon;
    }
    
    /**
     * 新建一个点要素
     *
     * @param latitude 纬度
     * @param longitude 经度
     * @param poiid 点ID
     * @param fieldDefination 字段定义及数据类型
     * @param fieldValue 字段值
     * @return 点要素对象
     * @throws Exception
     */
    public static SimpleFeature createOnePointFeature(Point point, String poiid,
            Dictionary<String, String> fieldDefination, Dictionary<String, Object> fieldValue) throws Exception {
        int sFieldDefinationCount = fieldDefination.size();
        int sFieldValueCount = fieldValue.size();
        if (sFieldDefinationCount != sFieldValueCount) {
            throw new Exception("字段定义与值数目不相等");
        }
        String sRestFeatureType = "";
        ArrayList<Object> sRestFieldValue = new ArrayList<Object>();

        for (Enumeration e = fieldDefination.keys(); e.hasMoreElements();) {
            sRestFeatureType += e.toString() + ":" + fieldDefination.get(e) + ",";
            sRestFieldValue.add(fieldValue.get(e));
        }
        final SimpleFeatureType TYPE = DataUtilities.createType("Location",
                "location:Point,"
                + // <- the geometry attribute: Point type  
                "POIID:String," + sRestFeatureType
        );
        //GeometryFactory geometryFactory = new GeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        sRestFieldValue.add(0, point);
        sRestFieldValue.add(1, poiid);
        Object[] obj = sRestFieldValue.toArray();
        SimpleFeature sfeature = featureBuilder.buildFeature(null, obj);
        return sfeature;
    }
    
    public static SimpleFeature createOneLineStringFeature(LineString lineString, String lineID,
            Dictionary<String, String> fieldDefination, Dictionary<String, Object> fieldValue) throws Exception 
    {
        int sFieldDefinationCount = fieldDefination.size();
        int sFieldValueCount = fieldValue.size();
        if (sFieldDefinationCount != sFieldValueCount) {
            throw new Exception("字段定义与值数目不相等");
        }
        String sRestFeatureType = "";
        ArrayList<Object> sRestFieldValue = new ArrayList<Object>();

        for (Enumeration e = fieldDefination.keys(); e.hasMoreElements();) {
            sRestFeatureType += e.toString() + ":" + fieldDefination.get(e) + ",";
            sRestFieldValue.add(fieldValue.get(e));
        }
        final SimpleFeatureType TYPE = DataUtilities.createType("Location",
                "location:LineString,"
                + // <- the geometry attribute: LineString type  
                "LineID:String," + sRestFeatureType
        );
        //GeometryFactory geometryFactory = new GeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        sRestFieldValue.add(0, lineString);
        sRestFieldValue.add(1, lineID);
        Object[] obj = sRestFieldValue.toArray();
        SimpleFeature sfeature = featureBuilder.buildFeature(null, obj);
        return sfeature;
    }

    /**
     * 将要素添加至目标源
     *
     * @param sTargetFeatureSource 目标要素源
     * @param sTargetFeature 目标要素
     * @return 是否添加成功
     * @throws Exception
     */
    public static boolean addFeatureToFeatureSource(SimpleFeatureSource sTargetFeatureSource, SimpleFeature sTargetFeature) throws Exception {
        Transaction transaction = new DefaultTransaction("Append");
        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        collection.add(sTargetFeature);
        try {
            if (sTargetFeatureSource instanceof SimpleFeatureStore) {
                SimpleFeatureStore featureStore = (SimpleFeatureStore) sTargetFeatureSource;
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
                System.out.println(sTargetFeatureSource.getSchema().toString() + " does not support read/write access");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从要素源中删除指定要素
     *
     * @param sTargetFeatureSource 要素源
     * @param sTargetFeature 指定的要素
     * @return 删除是否成功
     * @throws Exception
     */
    public static boolean deleteFeatureFromFeatureSource(SimpleFeatureSource sTargetFeatureSource, SimpleFeature sTargetFeature) throws Exception {
        Transaction transaction = new DefaultTransaction("Delete");
        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        collection.add(sTargetFeature);
        FeatureIterator<SimpleFeature> itertor = collection.features();
        Set<FeatureId> fids = new HashSet<FeatureId>();
        while (itertor.hasNext()) {
            SimpleFeature feature = itertor.next();
            fids.add(feature.getIdentifier());
        }
        itertor.close();
        FilterFactory2 sFilterFactory2 = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        Filter sRemoveFilter = (Filter) sFilterFactory2.id(fids);

        try {
            if (sTargetFeatureSource instanceof SimpleFeatureStore) {
                SimpleFeatureStore featureStore = (SimpleFeatureStore) sTargetFeatureSource;
                featureStore.setTransaction(transaction);
                try {
                    featureStore.removeFeatures(sRemoveFilter);
                    transaction.commit();
                } catch (Exception problem) {
                    problem.printStackTrace();
                    transaction.rollback();
                } finally {
                    transaction.close();
                }
            } else {
                System.out.println(sTargetFeatureSource.getSchema().toString() + " does not support read/write access");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从要素源中删除若干指定要素
     *
     * @param sTargetFeatureSource 要素源
     * @param sTargetFeatures 指定的若干要素
     * @return 删除是否成功
     * @throws Exception
     */
    public static boolean deleteFeatureFromFeatureSource(SimpleFeatureSource sTargetFeatureSource, SimpleFeatureCollection sTargetFeatures) throws Exception {
        Transaction transaction = new DefaultTransaction("Delete");
        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        collection.addAll(sTargetFeatures);
        FeatureIterator<SimpleFeature> itertor = collection.features();
        Set<FeatureId> fids = new HashSet<FeatureId>();
        while (itertor.hasNext()) {
            SimpleFeature feature = itertor.next();
            fids.add(feature.getIdentifier());
        }
        itertor.close();
        FilterFactory2 sFilterFactory2 = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        Filter sRemoveFilter = (Filter) sFilterFactory2.id(fids);
        try {
            if (sTargetFeatureSource instanceof SimpleFeatureStore) {
                SimpleFeatureStore featureStore = (SimpleFeatureStore) sTargetFeatureSource;
                featureStore.setTransaction(transaction);
                try {
                    featureStore.removeFeatures(sRemoveFilter);
                    transaction.commit();
                } catch (Exception problem) {
                    problem.printStackTrace();
                    transaction.rollback();
                } finally {
                    transaction.close();
                }
            } else {
                System.out.println(sTargetFeatureSource.getSchema().toString() + " does not support read/write access");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从要素源中按筛选器删除要素
     * @param sTargetFeatureSource 要素源
     * @param sDeleteFilter 删除筛选器
     * @return  删除是否成功
     * @throws Exception
     */
    public static boolean deleteFeatureFromFeatureSource(SimpleFeatureSource sTargetFeatureSource, Filter sDeleteFilter) throws Exception {
        try {
            Transaction transaction = new DefaultTransaction("Delete");
            if (sTargetFeatureSource instanceof SimpleFeatureStore) {
                SimpleFeatureStore featureStore = (SimpleFeatureStore) sTargetFeatureSource;
                featureStore.setTransaction(transaction);
                try {
                    featureStore.removeFeatures(sDeleteFilter);
                    transaction.commit();
                } catch (Exception problem) {
                    problem.printStackTrace();
                    transaction.rollback();
                } finally {
                    transaction.close();
                }
            } else {
                System.out.println(sTargetFeatureSource.getSchema().toString() + " does not support read/write access");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
