package Util;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1, jaggr2
 * Date: 18.10.13
 * Time: 18:46
 */
public class CMathFunctions {

    /**
     * extended calculation of the intersection point of two lines within two given points each
     * @param line1StartPos start position of first line
     * @param line1EndPos end position of first line
     * @param line2StartPos start position of second line
     * @param line2EndPos end position of second line
     * @param isLine1Infinite indicate if the first line are infinite or only between the given points
     * @param isLine2Infinite indicate if the second line are infinite or only between the given points
     * @return intersection point as CPosition or null if there is none
      */
    public static CPosition calcIntersectionPoint(CPosition line1StartPos, CPosition line1EndPos, CPosition line2StartPos, CPosition line2EndPos, Boolean isLine1Infinite, Boolean isLine2Infinite)
    {
        // inspired by http://stackoverflow.com/questions/385305/efficient-maths-algorithm-to-calculate-intersections
        Double dx_cx = line2EndPos.getX() - line2StartPos.getX();
        Double dy_cy = line2EndPos.getY() - line2StartPos.getY();
        Double bx_ax = line1EndPos.getX() - line1StartPos.getX();
        Double by_ay = line1EndPos.getY() - line1StartPos.getY();

        Double de = (bx_ax) * (dy_cy) - (by_ay) * (dx_cx);

        if (Math.abs(de) < 0.01) {
            // lines are parallel in this case
            return null;
        }

        Double ax_cx = line1StartPos.getX() - line2StartPos.getX();
        Double ay_cy = line1StartPos.getY() - line2StartPos.getY();

        // line1 intersection position r
        Double r = ((ay_cy) * (dx_cx) - (ax_cx) * (dy_cy)) / de;

        // line2 intersection position s
        Double s = ((ay_cy) * (bx_ax) - (ax_cx) * (by_ay)) / de;

        // r and s must between 0 and 1, so that the intersection point is on both lines
        if( !(isLine1Infinite  || ( 0 <= r && r <= 1)) || !(isLine2Infinite ||(0 <= s && s <= 1 )) )
        {
            return null;
        }

        // calculate intersection point
        Double px = line1StartPos.getX() + r * (bx_ax);
        Double py = line1StartPos.getY() + r * (by_ay);

        return new CPosition(px, py);
    }

    /**
     * Computes the dot product AB . AC
     * @param pointA point A
     * @param pointB point B
     * @param pointC point C
     * @return dot product as Double
     */
    public static Double calcDotProduct(CPosition pointA, CPosition pointB, CPosition pointC)
    {
        Double ab_x = pointB.getX() - pointA.getX();
        Double ab_y = pointB.getY() - pointA.getY();
        Double bc_x = pointC.getX() - pointB.getX();
        Double bc_y = pointC.getY() - pointB.getY();

        return ab_x * bc_x + ab_y * bc_y;
    }

    /**
     * Computes the cross product AB x AC
     * @param pointA point A
     * @param pointB point B
     * @param pointC point C
     * @return the cross product as Double
     */
    public static Double calcCrossProduct(CPosition pointA, CPosition pointB, CPosition pointC)
    {
        Double ab_x = pointB.getX() - pointA.getX();
        Double ab_y = pointB.getY() - pointA.getY();
        Double ac_x = pointC.getX() - pointA.getX();
        Double ac_y = pointC.getY() - pointA.getY();

        return ab_x * ac_y - ab_y * ac_x;
    }

    /**
     * Compute the distance from A to B
     * @param pointA point A
     * @param pointB point B
     * @return the distance between the two points as Double
     */
    public static Double calcDistance(CPosition pointA, CPosition pointB)
    {
        if(pointA == null || pointB == null) {
            return null;
        }

        Double deltaX = pointA.getX() - pointB.getX();
        Double deltaY = pointA.getY() - pointB.getY();

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Compute the distance from the line AB to point C, if isInfiniteLine is true, line AB is infinite an not a segment
     * @param pointA point A
     * @param pointB point B
     * @param pointC point C
     * @param isInfiniteLine true if the line is infinite, false if the linie is a finite segment
     * @return the distance from point C to line AB as double
     */
    public static Double calcLineToPointDistance(CPosition pointA, CPosition pointB, CPosition pointC, Boolean isInfiniteLine)
    {
        Double dist = calcCrossProduct(pointA, pointB, pointC) / calcDistance(pointA, pointB);
        if (!isInfiniteLine) {
            double dot1 = calcDotProduct(pointA, pointB, pointC);
            if (dot1 > 0) {
                return calcDistance(pointB, pointC);
            }

            double dot2 = calcDotProduct(pointB, pointA, pointC);
            if (dot2 > 0) {
                return calcDistance(pointA, pointC);
            }
        }
        return Math.abs(dist);
    }
}
