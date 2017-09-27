/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.pku.datasource;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hwpf.converter.FontReplacer.Triplet;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
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
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

/**
 * 对SimpleFeature进行管理的类
 *
 * @version 1.0.2
 * @author sq
 */
public class SimpleFeatureManager {

    /**
     * 根据经纬度生成点
     *
     * @param latitude 纬度
     * @param longitude 经度
     * @return 点对象
     */
    public static Point createOnePoint(double latitude, double longitude) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        return point;
    }

    /**
     * 根据经纬度生成线
     *
     * @param latArray 纬度序列
     * @param lngArray 经度序列
     * @return 线对象
     * @throws Exception
     */
    public static LineString createOneLineString(double[] latArray, double[] lngArray) throws Exception {
        GeometryFactory geometryFactory = new GeometryFactory();
        int latLength = latArray.length;
        int lngLength = lngArray.length;
        if (latLength != lngLength) {
            throw new Exception("数据长度不匹配");
        }
        ArrayList<Coordinate> coordinateList = new ArrayList<Coordinate>();
        for (int i = 0; i < latLength; i++) {
            coordinateList.add(new Coordinate(lngArray[i], latArray[i]));
        }
        Coordinate[] coorArray = new Coordinate[coordinateList.size()];
        LineString lineString = geometryFactory.createLineString(coordinateList.toArray(coorArray));
        return lineString;
    }

    /**
     * 根据坐标点生成线
     *
     * @param coordinateArray 坐标点序列
     * @return 线对象
     * @throws Exception
     */
    public static LineString createOneLineString(Coordinate[] coordinateArray) throws Exception {
        GeometryFactory geometryFactory = new GeometryFactory();
        LineString lineString = geometryFactory.createLineString(coordinateArray);
        return lineString;
    }

    /**
     * 根据经纬度序列生成环
     *
     * @param latArray 纬度序列
     * @param lngArray 经度序列
     * @return 环对象
     * @throws Exception
     */
    public static LinearRing createOneLinearRing(double[] latArray, double[] lngArray) throws Exception {
        GeometryFactory geometryFactory = new GeometryFactory();
        int latLength = latArray.length;
        int lngLength = lngArray.length;
        if (latLength != lngLength) {
            throw new Exception("数据长度不匹配");
        }
        ArrayList<Coordinate> coordinateList = new ArrayList<Coordinate>();
        for (int i = 0; i < latLength; i++) {
            coordinateList.add(new Coordinate(lngArray[i], latArray[i]));
        }
        Coordinate[] coorArray = new Coordinate[coordinateList.size()];
        LinearRing linearRing = geometryFactory.createLinearRing(coordinateList.toArray(coorArray));
        return linearRing;
    }

    /**
     * 根据坐标点序列生成环
     *
     * @param coordinateArray 坐标点序列
     * @return 环对象
     * @throws Exception
     */
    public static LinearRing createOneLinearRing(Coordinate[] coordinateArray) throws Exception {
        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing linearRing = geometryFactory.createLinearRing(coordinateArray);
        return linearRing;
    }

    /**
     * 根据坐标点序列生成多边形
     *
     * @param latArray 纬度序列
     * @param lngArray 经度序列
     * @return 多边形对象
     * @throws Exception
     */
    public static Polygon createOnePolygon(double[] latArray, double[] lngArray) throws Exception {
        GeometryFactory geometryFactory = new GeometryFactory();
        int latLength = latArray.length;
        int lngLength = lngArray.length;
        if (latLength != lngLength) {
            throw new Exception("数据长度不匹配");
        }
        ArrayList<Coordinate> coordinateList = new ArrayList<Coordinate>();
        for (int i = 0; i < latLength; i++) {
            coordinateList.add(new Coordinate(lngArray[i], latArray[i]));
        }
        Coordinate[] coorArray = new Coordinate[coordinateList.size()];
        Polygon polygon = geometryFactory.createPolygon(coordinateList.toArray(coorArray));
        return polygon;
    }

    /**
     * 根据坐标点序列生成多边形
     *
     * @param coordinateArray 坐标点序列
     * @return 多边形对象
     * @throws Exception
     */
    public static Polygon createOnePolygon(Coordinate[] coordinateArray) throws Exception {
        GeometryFactory geometryFactory = new GeometryFactory();
        Polygon polygon = geometryFactory.createPolygon(coordinateArray);
        return polygon;
    }

    /**
     * 新建一个点要素
     * @deprecated 
     * @param point 点对象
     * @param poiid 点ID
     * @param fieldDefinition 字段定义及数据类型
     * @param fieldValue 字段值
     * @return 点要素对象
     * @throws Exception
     */
    public static SimpleFeature createOnePointFeature(Point point, String poiid,
            Map<String, Class> fieldDefinition, Map<String, Object> fieldValue) throws Exception {
        
        int sFieldDefinationCount = fieldDefinition.size();
        int sFieldValueCount = fieldValue.size();
        if (sFieldDefinationCount != sFieldValueCount) {
            throw new Exception("字段定义与值数目不相等");
        }
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Location");
        //builder.setCRS(DefaultGeographicCRS.WGS84); // <- Coordinate reference system  
        builder.add("the_geom", Point.class);  //这个为地理属性字段 postgis中为 the——geom  
        builder.add("Poiid", String.class);
        for (Map.Entry<String, Class> entry : fieldDefinition.entrySet()) {
            builder.add(entry.getKey(), entry.getValue()); // 这是其他属性字段 自己定义的....
        }
        // build the type  
        final SimpleFeatureType TYPE = builder.buildFeatureType();
        
        //GeometryFactory geometryFactory = new GeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        featureBuilder.set("the_geom",point);
        featureBuilder.set("Poiid",poiid);
        for (Map.Entry<String, Object> entry : fieldValue.entrySet()) {
            featureBuilder.set(entry.getKey(), entry.getValue()); // 写入属性值
        }
        SimpleFeature sfeature = featureBuilder.buildFeature(null);
        return sfeature;
    }

    /**
     * 新建一个点要素
     * @deprecated 
     * @param point 点对象
     * @param poiid 点ID
     * @param fieldDefinition 字段定义及数据类型
     * @param fieldValue 字段值
     * @return 点要素对象
     * @throws Exception
     */
    public static SimpleFeature createOnePointFeature2(Point point, String poiid,
            Map<String, String> fieldDefinition, Map<String, Object> fieldValue) throws Exception {
        int sFieldDefinationCount = fieldDefinition.size();
        int sFieldValueCount = fieldValue.size();
        if (sFieldDefinationCount != sFieldValueCount) {
            throw new Exception("字段定义与值数目不相等");
        }
        String sRestFeatureType = "";
        ArrayList<Object> sRestFieldValue = new ArrayList<Object>();

        for (Map.Entry<String, String> entry : fieldDefinition.entrySet()) {
            sRestFeatureType += entry.getKey() + ":" + entry.getValue() + ",";
            sRestFieldValue.add(fieldValue.get(entry.getKey()));// 写入属性值
        }
       sRestFeatureType =sRestFeatureType.substring(0, sRestFeatureType.length()-1);
        final SimpleFeatureType TYPE = DataUtilities.createType("Location",
                "Location:Point,"
                + // <- the geometry attribute: LineString type  
                "PointID:String," + sRestFeatureType
        );
        //GeometryFactory geometryFactory = new GeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        sRestFieldValue.add(0, point);
        sRestFieldValue.add(1, poiid);
        Object[] obj = sRestFieldValue.toArray();
        SimpleFeature sfeature = featureBuilder.buildFeature(null, obj);
        return sfeature;
    }
    
    /**
     * 新建一个线要素
     * @deprecated 
     * @param lineString 线对象
     * @param lineID 线ID
     * @param fieldDefinition 字段定义及数据类型
     * @param fieldValue 字段值
     * @return 线要素对象
     * @throws Exception
     */
    public static SimpleFeature createOneLineStringFeature(LineString lineString, String lineID,
            Map<String, String> fieldDefinition, Map<String, Object> fieldValue) throws Exception {
        int sFieldDefinationCount = fieldDefinition.size();
        int sFieldValueCount = fieldValue.size();
        if (sFieldDefinationCount != sFieldValueCount) {
            throw new Exception("字段定义与值数目不相等");
        }
        String sRestFeatureType = "";
        ArrayList<Object> sRestFieldValue = new ArrayList<Object>();

        for (Map.Entry<String, String> entry : fieldDefinition.entrySet()) {
            sRestFeatureType += entry.getKey() + ":" + entry.getValue() + ",";
            sRestFieldValue.add(fieldValue.get(entry.getKey()));// 写入属性值
        }
        final SimpleFeatureType TYPE = DataUtilities.createType("Location",
                "Location:LineString,"
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
     * 新建一个线性环要素 
     * @deprecated 
     * @param linearRing 环对象
     * @param ringID 环ID
     * @param fieldDefinition 字段定义及数据类型
     * @param fieldValue 字段值
     * @return 环要素对象
     * @throws Exception
     */
    public static SimpleFeature createOneLinearRingFeature(LinearRing linearRing, String ringID,
            Map<String, String> fieldDefinition, Map<String, Object> fieldValue) throws Exception {
        int sFieldDefinationCount = fieldDefinition.size();
        int sFieldValueCount = fieldValue.size();
        if (sFieldDefinationCount != sFieldValueCount) {
            throw new Exception("字段定义与值数目不相等");
        }
        String sRestFeatureType = "";
        ArrayList<Object> sRestFieldValue = new ArrayList<Object>();

        for (Map.Entry<String, String> entry : fieldDefinition.entrySet()) {
            sRestFeatureType += entry.getKey() + ":" + entry.getValue() + ",";
            sRestFieldValue.add(fieldValue.get(entry.getKey()));// 写入属性值
        }
        final SimpleFeatureType TYPE = DataUtilities.createType("Location",
                "Location:LinearRing,"
                + // <- the geometry attribute: LineString type  
                "RingID:String," + sRestFeatureType
        );
        //GeometryFactory geometryFactory = new GeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        sRestFieldValue.add(0, linearRing);
        sRestFieldValue.add(1, ringID);
        Object[] obj = sRestFieldValue.toArray();
        SimpleFeature sfeature = featureBuilder.buildFeature(null, obj);
        return sfeature;
    }

    /**
     * 新建一个多边形要素
     * @deprecated 
     * @param polygon 多边形对象
     * @param polygonID 多边形ID
     * @param fieldDefinition 字段定义及数据类型
     * @param fieldValue 字段值
     * @return 多边形要素
     * @throws Exception
     */
    public static SimpleFeature createOnePolygonFeature(Polygon polygon, String polygonID,
            Map<String, String> fieldDefinition, Map<String, Object> fieldValue) throws Exception {
        int sFieldDefinationCount = fieldDefinition.size();
        int sFieldValueCount = fieldValue.size();
        if (sFieldDefinationCount != sFieldValueCount) {
            throw new Exception("字段定义与值数目不相等");
        }
        String sRestFeatureType = "";
        ArrayList<Object> sRestFieldValue = new ArrayList<Object>();

        for (Map.Entry<String, String> entry : fieldDefinition.entrySet()) {
            sRestFeatureType += entry.getKey() + ":" + entry.getValue() + ",";
            sRestFieldValue.add(fieldValue.get(entry.getKey()));// 写入属性值
        }
        final SimpleFeatureType TYPE = DataUtilities.createType("Location",
                "Location:Polygon,"
                + // <- the geometry attribute: LineString type  
                "PolygonID:String," + sRestFeatureType
        );
        //GeometryFactory geometryFactory = new GeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        sRestFieldValue.add(0, polygon);
        sRestFieldValue.add(1, polygonID);
        Object[] obj = sRestFieldValue.toArray();
        SimpleFeature sfeature = featureBuilder.buildFeature(null, obj);
        return sfeature;
    }

    /**
     * 获取要素源的要素类型
     * @param sTargetFeatureSource 要素源
     * @return
     */
    public static SimpleFeatureType getSimpleFeatureType(SimpleFeatureSource sTargetFeatureSource)
    {
        return sTargetFeatureSource.getSchema();
    }
    
    /**
     * 获取要素源中字段的名称及数据类型
     * @param sTargetFeatureSource 要素源
     * @return
     */
    public static HashMap<String,Class> getSimpleFeatureFields(SimpleFeatureSource sTargetFeatureSource)
    {
        int sFieldsCount=sTargetFeatureSource.getSchema().getAttributeCount();
        SimpleFeatureType sType=sTargetFeatureSource.getSchema();
        HashMap<String,Class> sFields=new HashMap<String,Class>();
        for (int i = 0; i < sFieldsCount; i++) {
            sFields.put(sType.getType(i).getName().toString(), sType.getType(i).getBinding());
        }
        return sFields;
    }
    
    /**
     * 将几何图形添加到要素源中
     * @param sTargetFeatureSource 要素源
     * @param sTargetGeometry 几何图形
     * @param sFieldValue 属性值
     * @return 添加结果
     */
    public static boolean addGeometryToFeatureSource(SimpleFeatureSource sTargetFeatureSource,Geometry sTargetGeometry,Map<String,Object> sFieldValue)
    {
        //构建要素
        final SimpleFeatureType type = getSimpleFeatureType(sTargetFeatureSource);
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
        featureBuilder.add(sTargetGeometry);
        HashMap<String,Class> sFieldDefinition = getSimpleFeatureFields(sTargetFeatureSource);
        for (Map.Entry<String, Class> entry : sFieldDefinition.entrySet()) {
            if (sFieldValue.containsKey(entry.getKey()) == true) {
                featureBuilder.set(entry.getKey(), sFieldValue.get(entry.getKey())); // 写入属性值
            }
        }
        SimpleFeature simpleFeature = featureBuilder.buildFeature(null);
        //构建事务，添加要素
        Transaction transaction = new DefaultTransaction("Append");
        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        collection.add(simpleFeature);
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
     * 将要素添加至目标源
     * @deprecated 
     * @param sTargetFeatureSource 目标要素源
     * @param sTargetFeature 目标要素
     * @return 是否添加成功
     * @throws Exception
     */
    public static boolean addFeatureToFeatureSource(SimpleFeatureSource sTargetFeatureSource, SimpleFeature sTargetFeature) throws Exception {
        //判断两者要素类型是否一致
        /*这一部分代码还有bug，暂时不用，等找到更好的方法再更改
        String featureType = sTargetFeature.getAttribute(0).toString().split(" ")[0];
        String sourceType = sTargetFeatureSource.getFeatures().features().next().getAttribute(0).toString().split(" ")[0];
        if (featureType.toLowerCase().equals(sourceType.toLowerCase()) == false) {
            throw new Exception("要素类型不一致");
        }*/
        //添加事务
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
     * @deprecated 
     * @param sTargetFeatureSource 要素源
     * @param sTargetFeature 指定的要素
     * @return 删除是否成功
     * @throws Exception
     */
    public static boolean deleteFeatureFromFeatureSource(SimpleFeatureSource sTargetFeatureSource, SimpleFeature sTargetFeature) throws Exception {
        //判断两者要素类型是否一致
        /*这一部分代码还有bug，暂时不用，等找到更好的方法再更改
        String featureType = sTargetFeature.getAttribute(0).toString().split(" ")[0];
        String sourceType = sTargetFeatureSource.getFeatures().features().next().getAttribute(0).toString().split(" ")[0];
        if (featureType.toLowerCase().equals(sourceType.toLowerCase()) == false) {
            throw new Exception("要素类型不一致");
        }*/
        //添加事务
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
        //判断两者要素类型是否一致
        /*这一部分代码还有bug，暂时不用，等找到更好的方法再更改
        String featureType = sTargetFeatures.features().next().getAttribute(0).toString().split(" ")[0];
        String sourceType = sTargetFeatureSource.getFeatures().features().next().getAttribute(0).toString().split(" ")[0];
        if (featureType.toLowerCase().equals(sourceType.toLowerCase()) == false) {
            throw new Exception("要素类型不一致");
        }*/
        //添加事务
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
     *
     * @param sTargetFeatureSource 要素源
     * @param sDeleteFilter 删除筛选器
     * @return 删除是否成功
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

    //调试时主函数
    public static void main(String[] args) throws Exception {
        double latitude = Double.parseDouble("116.123234255");
        double longitude = Double.parseDouble("39.12051");
        String POIID = "139";
        String MESHID = "3";
        String OWNER = "cy";
        //Dictionary<String,Class> fieldDefination =new Dictionary<String,Class>();
        //Map<String, String> fieldDefinition = new HashMap<String, String>();
        //fieldDefinition.put("MeShID", "String");
        //fieldDefinition.put("Owner", "String");
        //fieldDefinition.put("wtf", "String");
        Map<String, Object> fieldValue = new HashMap<String, Object>();
        fieldValue.put("pop", "54");
        fieldValue.put("sum", "2");
       // fieldValue.put("wtf", "wtf");

        Point testPoint = createOnePoint(latitude, longitude);
        //SimpleFeature simpleFeature = createOnePointFeature2(testPoint, POIID, fieldDefinition, fieldValue);
        File newFile = new File("F:\\ArcGISDoc\\suzhou\\test.shp");
        FileDataStore store = FileDataStoreFinder.getDataStore(newFile);
        SimpleFeatureSource featureSource = store.getFeatureSource();
        //SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;  
        try
        {
            System.out.println(addGeometryToFeatureSource(featureSource, testPoint,fieldValue));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return;
    }
}
