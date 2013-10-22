package Util;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 18.10.13
 * Time: 18:46
 */
public class CMathFunctions {

    /**
     * calculates the intersection point of two lines with two given points
     */
    static CPosition calcIntersectionPoint(CPosition firstPos1, CPosition secondPos1, CPosition firstPos2, CPosition secondPos2)
    {

        // errechne Schnittpunkt
        // a0*x + b0*y + c0 = 0
        // a1*x + b1*y + c1 = 0    --> auflösen nach x,y
        //
        // wenn edgeConstantA 0 ist, muss auf eine andere Formel ausgewichen werden
//        Double y = ( other.getEdgeConstantA() / this.edgeConstantA * this.edgeConstantC - other.getEdgeConstantC() ) / (other.edgeConstantB - other.getEdgeConstantA() / this.edgeConstantA * this.edgeConstantB );
//        Double x = (this.edgeConstantB * y + this.edgeConstantC) / (-this.edgeConstantA);
//
//        CPosition returnValue = new CPosition(x, y);
//
//        if(oSource.getPos().isPointInRectangleBetween(oDestination.getPos(), returnValue)) {
//            return returnValue;
//        }

        // new version from http://stackoverflow.com/questions/385305/efficient-maths-algorithm-to-calculate-intersections
        // also interesting would be this: https://code.google.com/p/straightedge/

        // siehe auch http://www.java-forum.org/spiele-multimedia-programmierung/6588-einiges-geometrie-punkte-vektoren-geraden.html
        Double x12 = firstPos1.getX() - secondPos1.getX();
        Double x34 = firstPos2.getX() - secondPos2.getX();
        Double y12 = firstPos1.getY() - secondPos1.getY();
        Double y34 = firstPos2.getY() - secondPos2.getY();

        Double c = x12 * y34 - y12 * x34;

        if (Math.abs(c) < 0.01)
        {
            // No intersection , lines are parallel in this case
            return null;
        }
        // Intersection
        Double a = firstPos1.getX() * secondPos1.getY() - firstPos1.getY() * secondPos1.getX();
        Double b = firstPos2.getX() * secondPos2.getY() - firstPos2.getY() * secondPos2.getX();

        // this could be split to get an value r, which makes it possible to remove the isPointInRectangleBetween() Function
        Double x = (a * x34 - b * x12) / c;
        Double y = (a * y34 - b * y12) / c;

        return new CPosition(x, y);
    }
}
