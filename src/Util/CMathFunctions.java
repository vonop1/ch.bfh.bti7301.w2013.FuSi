package Util;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
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

    public static Double angleBetween2Lines(CPosition startLine1, CPosition endLine1, CPosition startLine2, CPosition endLine2) {

        double angle1 = Math.atan2(startLine1.getY() - endLine1.getY(), startLine1.getX() - endLine1.getX());

        double angle2 = Math.atan2(startLine2.getY() - endLine2.getY(), startLine2.getX() - endLine2.getX());

        return angle1-angle2;
    }

    /*
    public CPosition rotateAround(CPosition point, CPosition center, double angle) {
        x = center.x + (Math.cos(Math.toRadians(angle)) * (x - center.x) - Math.sin(Math.toRadians(angle)) * (y - center.y));
        y = center.y + (Math.sin(Math.toRadians(angle)) * (x - center.x) + Math.cos(Math.toRadians(angle)) * (y - center.y));
    }
    */
}
