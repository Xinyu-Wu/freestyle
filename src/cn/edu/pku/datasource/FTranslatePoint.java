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
    public Coordinate [] ScreenToWorld (Point [] pointArray, MapViewport  viewport ){
       Coordinate[] worldPoints=new Coordinate[pointArray.length];
       FTranslatePoint translatePoint=new FTranslatePoint();
       for(int i=0;i<pointArray.length;i++){
            worldPoints[i]=translatePoint.ScreenToWorld(pointArray[i],viewport);
            }
       return  worldPoints;
    };
    public Coordinate  ScreenToWorld (Point point, MapViewport  viewport ){
       AffineTransform ScreenToWorld = viewport.getScreenToWorld(); 
       Point2D disPoint= ScreenToWorld.transform(point, null);
       Coordinate worldPoint =new Coordinate(disPoint.getX(), disPoint.getY());      
       return  worldPoint;
    }; 
}
