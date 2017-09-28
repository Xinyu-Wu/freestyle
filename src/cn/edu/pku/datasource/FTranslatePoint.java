/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.pku.datasource;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.JOptionPane;
import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.map.MapViewport;

/**
 *
 * @author cheng
 * To translate points between screen coordinate with world coordinate
 */
public class FTranslatePoint {
    public void FTranslatePoint(){}
    
    /**
     * 由屏幕点序列转换为坐标点序列
     * @param pointArray 屏幕点列
     * @param viewport 地图视窗
     * @return
     */
    public static Coordinate [] ScreenToWorld (java.awt.Point [] pointArray, MapViewport  viewport ){
       Coordinate[] worldPoints=new Coordinate[pointArray.length];
       FTranslatePoint translatePoint=new FTranslatePoint();
       for(int i=0;i<pointArray.length;i++){
            worldPoints[i]=translatePoint.ScreenToWorld(pointArray[i],viewport);
            }
       return  worldPoints;
    };
    
    /**
     * 由屏幕点转换为坐标点
     * @param point 屏幕点
     * @param viewport 地图视窗
     * @return
     */
    public static Coordinate  ScreenToWorld (java.awt.Point point, MapViewport  viewport ){
       AffineTransform ScreenToWorld = viewport.getScreenToWorld(); 
       Point2D disPoint= ScreenToWorld.transform(point, null);
       Coordinate worldPoint =new Coordinate(disPoint.getX(), disPoint.getY());      
       return  worldPoint;
    }; 
    
    /**
     * 由坐标点转换为屏幕点
     * @param sPoint 坐标点
     * @param viewport 地图视窗
     * @return
     */
    public static java.awt.Point WorldToScreen (Coordinate sPoint, MapViewport  viewport)
    {
        AffineTransform worldToScreen = viewport.getWorldToScreen();
        Point2D disPoint=worldToScreen.transform(new Point2D.Double(sPoint.x,sPoint.y) , null);
        return new Point((int)disPoint.getX(),(int)disPoint.getY());
    }
}
