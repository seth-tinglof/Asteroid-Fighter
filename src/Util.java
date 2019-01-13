
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Util {
	
	/**
	 * The point of intersection between two line2D objects. 2 lines are assumed to have an intercept. May not correctly
     * handle overlapping line segments.
	 * @param one being tested for point of intersection.
	 * @param two Line being tested for point of intersection.
	 * @return Point of intersection between two lines passed as arguments as a Point2D with double precision.
	 */
	static Point2D getIntersectionLines(Line2D one, Line2D two){
		double x1Start = one.getX1();
		double y1Start = one.getY1();
		
		double x1End   = one.getX2();
		double y1End   = one.getY2();
		
		double x2Start = two.getX1();
		double y2Start = two.getY1();
		
		double x2End   = two.getX2();
		double y2End   = two.getY2();
		
		double a1 = (y1End - y1Start) / (x1End - x1Start);	//Get slope of line 1.
		double b1 = y1Start - a1 * x1Start;					//Get intercept of line 1.
		double a2 = (y2End - y2Start) / (x2End - x2Start);  //Slope of line 2.
		double b2 = y2Start - a2 * x2Start;					//intercept of line 2.
		
		double xIntersect = -(b1-b2) / (a1-a2);
		if(Double.isNaN(xIntersect)){                       //Handle case where one line is vertical
			if(x1Start == x1End){
				xIntersect = x1Start;
			}
			else
				xIntersect = x2Start;
		}
		double yIntersect = a1 * xIntersect + b1;
		if(Double.isNaN(yIntersect))                        //Handle case where one line is vertical.
			yIntersect = a2 * xIntersect + b2;
		
		return new Point2D.Double(xIntersect, yIntersect);
	}

	/**
	 * Compares two doubles for near equality
	 * @param one
	 * @param two
	 * @param epsilon
	 * @return
	 */
	public static boolean almostEqual(double one, double two, double epsilon){
		return Math.abs(one - two) < epsilon;
	}

	public static double flipAngle(double angle){
	    if(angle < 0)
	        return angle + Math.PI;
	    return angle - Math.PI;
    }

    public static double angleBetweenPoints(double[] one, double[] two){
	    return Math.atan2(two[1] - one[1], two[0] - one[0]);
    }

    /**
     * Remove an element from an array by replacing it with the last element and deleting the last. This is a O(1) operation,
     * unlike normal remove.
     * @param list
     * @param index to remove at.
     */
    public static void replaceWithLast(ArrayList list, int index){
        list.set(index, list.get(list.size() - 1));
        list.remove(list.size() - 1);
    }

    /**
     * Determine if num is between min and max, inclusive.
     * @param num
     * @param min
     * @param max
     * @return
     */
    public static boolean isBetween(int num, int min, int max){
    	return num >= min && num <= max;
	}

    /**
     * Computes a mathematical modulus. The result is always positive, unlike %.
     * @param num
     * @param mod
     * @return modulus of num with respect to mod.
     */
    public static int modulus(int num, int mod){
    	int result = num % mod;
    	if(result >= 0){
    		return result;
		}
		return result + mod;
	}
}
