package Util;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 18.10.13
 * Time: 18:46
 */
public class CMathFunctions {

    /**
     * calculates the intersection point of two lines within two given points each
     */
    public static CPosition calcIntersectionPoint(CPosition line1StartPos, CPosition line1EndPos, CPosition line2StartPos, CPosition line2EndPos, Boolean isLine1Infinite, Boolean isLine2Infinite)
    {
        // version from http://stackoverflow.com/questions/385305/efficient-maths-algorithm-to-calculate-intersections
        // see also http://www.java-forum.org/spiele-multimedia-programmierung/6588-einiges-geometrie-punkte-vektoren-geraden.html

        Double x12 = line1StartPos.getX() - line1EndPos.getX();
        Double x34 = line2StartPos.getX() - line2EndPos.getX();
        Double y12 = line1StartPos.getY() - line1EndPos.getY();
        Double y34 = line2StartPos.getY() - line2EndPos.getY();

        Double c = x12 * y34 - y12 * x34;

        if (Math.abs(c) < 0.01)
        {
            // No intersection , lines are parallel in this case
            return null;
        }

        // Intersection
        Double a = line1StartPos.getX() * line1EndPos.getY() - line1StartPos.getY() * line1EndPos.getX();
        Double b = line2StartPos.getX() * line2EndPos.getY() - line2StartPos.getY() * line2EndPos.getX();

        // this could be split to get an value r, which makes it possible to remove the isPointInRectangleBetween() Function
        Double x = (a * x34 - b * x12) / c;
        Double y = (a * y34 - b * y12) / c;

        // check if point lies between the x-Coordinates of the first limited line
        if( !isLine1Infinite ) {
            if(line1StartPos.getX() > line1EndPos.getX()) {
                if(!(line1EndPos.getX() <= x && x <= line1StartPos.getX())) {
                    return null;
                }
            }
            else {
                if(!(line1StartPos.getX() <= x && x <= line1EndPos.getX())) {
                    return null;
                }
            }
        }

        // check if point lies between the x-Coordinates of the second limited line
        if( !isLine2Infinite ) {
            if(line2StartPos.getX() > line2EndPos.getX()) {
                if(!(line2EndPos.getX() <= x && x <= line2StartPos.getX())) {
                    return null;
                }
            }
            else {
                if(!(line2StartPos.getX() <= x && x <= line2EndPos.getX())) {
                    return null;
                }
            }
        }

        return new CPosition(x, y);
    }

    /**
     * extended calculation of the intersection point of two lines within two given points each
     * @param line1StartPos start position of first line
     * @param line1EndPos end position of first line
     * @param line2StartPos start position of second line
     * @param line2EndPos end position of second line
     * @param isInfiniteLines indicate if the lines are infinite or only between the given points
     * @return intersection point as CPosition or null if there is none
      */
    public static CPosition calcIntersectionPoint2(CPosition line1StartPos, CPosition line1EndPos, CPosition line2StartPos, CPosition line2EndPos, Boolean isLine1Infinite, Boolean isLine2Infinite)
    {
        // -----------------------------------------------------------------------------------------
        // METHODE FUNKTIONIERT NOCH NICHT WIE GEWüNSCHT!
        // -----------------------------------------------------------------------------------------

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
}
