/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freestyle;


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
    /*public Point [] WorldToScreen (Coordinate [] coordinateArray, MapViewport  viewport ){
    };
    public Point WorldToScreen (Coordinate   coordinate, MapViewport  viewport ){ 
       AffineTransform WorldToScreen = viewport.getWorldToScreen();
       Point start;
       start = new Point(coordinate.x,coordinate.y);
       Point2D disPoint= WorldToScreen.transform(coordinate., null);
       Coordinate worldPoint =new Coordinate(disPoint.getX(), disPoint.getY());      
       return  worldPoint;
    }
     */   
}
