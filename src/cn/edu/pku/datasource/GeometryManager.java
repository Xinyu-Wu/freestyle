/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.pku.datasource;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.util.ArrayList;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

/**
 * 几何图像管理器
 * @author sq
 */
public class GeometryManager {
    
    /**
     * 获得要素类型（Class）
     *
     * @param feature 要素
     * @return
     */
    public static Class getFeatureClass(SimpleFeature feature) {
        return feature.getAttribute(0).getClass();
    }

    /**
     * 获取要素类型（String）
     *
     * @param feature 要素
     * @return
     */
    public static String getFeatureClassString(SimpleFeature feature) {
        return ((Geometry) feature.getAttribute(0)).getGeometryType();
    }

    
    /**
     * 获取要素的几何图形
     *
     * @param feature 要素
     * @return
     */
    public static Geometry getGeometryFromFeature(SimpleFeature feature) {
        //return (Geometry)feature.getAttribute(0);  这个是自己找的
        //下面这个是网上给的
        return (Geometry) feature.getDefaultGeometry();
    }
    
    /**
     * 获取要素集合的几何图形集合
     *
     * @param featureCollection 要素集合
     * @return
     */
    public static ArrayList<Geometry> getGeometriesFromFeatureCollection(SimpleFeatureCollection featureCollection) {
        ArrayList<Geometry> sGeometryList = new ArrayList<Geometry>();
        //GeometryCollection geometryCollection =new GeometryCollection();
        SimpleFeatureIterator it = featureCollection.features();
        try {
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                sGeometryList.add((Geometry) feature.getDefaultGeometry());
            }
        } finally {
            it.close();
        }
        return sGeometryList;
    }
    
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
     * 根据输入生成一个圆
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @param RADIUS 半径
     * @param SIDES 圆上点的个数
     * @return
     */
    public static Polygon createOneCircle(double x, double y, final double RADIUS, int SIDES) {
        Coordinate coords[] = new Coordinate[SIDES + 1];
        for (int i = 0; i < SIDES; i++) {
            double angle = ((double) i / (double) SIDES) * Math.PI * 2.0;
            double dx = Math.cos(angle) * RADIUS;
            double dy = Math.sin(angle) * RADIUS;
            coords[i] = new Coordinate((double) x + dx, (double) y + dy);
        }
        coords[SIDES] = coords[0];
        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing ring = geometryFactory.createLinearRing(coords);
        Polygon polygon = geometryFactory.createPolygon(ring, null);
        return polygon;
    }
    
    public static boolean insertPointToPolyline(SimpleFeature feature,Point insertPoint,int index) throws Exception
    {
        Class featureClass=getFeatureClass(feature);
        if (featureClass != LineString.class || featureClass != LinearRing.class) {
            throw new Exception("要素不是线,不可操作");
        }
        Geometry polyline=getGeometryFromFeature(feature);
        if (polyline instanceof LineString ) {
            LineString lineString = (LineString)polyline;
            //lineString.
        }
        return false;
    }
    
    public static boolean isPointOnLine(Point sPoint,LineString line,double tolerance)
    {
        Geometry sBuffer= line.buffer(tolerance);
        return sBuffer.intersects(sPoint);
    }

}
