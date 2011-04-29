package com.rj.research.uiuc.gesturesound.gestures.qualities;

import processing.core.PApplet;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.Point;
import com.rj.research.uiuc.filters.KalmanFilter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class MTRelVelocityArc extends Quality {
	public static String name="MTRelativeDistance";
	public static float SCALE = 5f;
	
	public MTRelVelocityArc() {
		type = FeatureMap.MTREL_VEL_ARC;
		setSmooth(1);
	}

	@Override
	public float getQuality(Cursor in) {
		if (in.curId == 0 && in.currentPoint != null  && in.points != null && in.points.size() >= 2) { //we're the first cursor id
			Cursor second = null;
			for (Cursor c : in.manager.cursors) {
				if (c != null && c.curId != 0 && c.currentPoint != null && c.points != null && c.points.size() >= 2) {
					second = c; //we found the second cursor!
					Point p1 = in.currentPoint;
					Point p2 = second.currentPoint;
					float angle1 = getAngle(p1, p2);
					Point p3 = in.points.get(in.points.size()-2);
					Point p4 = second.points.get(second.points.size()-2);
					float angle2 = getAngle(p3, p4);
//					if (Math.abs(angle1-angle2) > PApplet.PI)  {
//						System.out.println("EEK! "+((angle2-angle1)));
//						return ((angle2-angle1))*SCALE;
//					}
					return (Math.abs(angle1-angle2) > PApplet.PI)? -(angle2-angle1)*SCALE : (angle1-angle2)*SCALE;
				}
			}
		} 
		return Float.NEGATIVE_INFINITY;		
	}
	
	private float getAngle(Point p1, Point p2) {
		float angle = PApplet.atan2(p1.x-p2.x, p1.y-p2.y);	
		return angle;//return Math.abs(angle);//return angle = angle > 0 ? angle : PApplet.PI - angle;
	}
	
}
