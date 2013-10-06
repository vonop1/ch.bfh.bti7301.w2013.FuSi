package Util;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 04.10.13
 * Time: 08:49
 */
public class CEdge {

    private int iId;
    private CVertex oSource;
    private CVertex oDestination;
    private int iWeight;
    private boolean bisObstacleEdge;

    private Double edgeConstantA;
    private Double edgeConstantB;
    private Double edgeConstantC;


    public CEdge (CVertex oSource, CVertex oDestination, Integer iWeight, boolean isObstacleEdge)
    {
        this.iId = CVertex.incrementId();
        this.oSource = oSource;
        this.oDestination = oDestination;

        // we calculate the line representation in the form a*x + b*y + c = 0
        // a, b and c are constant values

        Double dx = oDestination.getPos().getX() - oSource.getPos().getX();
        Double dy = oDestination.getPos().getY() - oDestination.getPos().getY();

        this.edgeConstantA = +dy;
        this.edgeConstantB = -dx;
        this.edgeConstantC = -(edgeConstantA*oDestination.getPos().getX() + edgeConstantB*oDestination.getPos().getY());

        if(iWeight == null) {
            this.iWeight = oSource.getPos().getDistanceTo(oDestination.getPos()).intValue();
        }
        else {
            this.iWeight = iWeight;
        }

        this.bisObstacleEdge = isObstacleEdge;
    }

    public int getId ()
    {
        return iId;
    }

    public CVertex getSource ()
    {
        return oSource;
    }

    public CVertex getDestination ()
    {
        return oDestination;
    }

    public int getWeight ()
    {
        return iWeight;
    }

    public Double getEdgeConstantA() {
        return edgeConstantA;
    }

    public Double getEdgeConstantB() {
        return edgeConstantB;
    }

    public Double getEdgeConstantC() {
        return edgeConstantC;
    }

    public boolean isObstacleEdge() {
        return bisObstacleEdge;
    }

    public CPosition calcIntersectionWith(CEdge other) {

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
//        if(oSource.getPos().isPointInRectancleBetween(oDestination.getPos(), returnValue)) {
//            return returnValue;
//        }

        // new version from http://stackoverflow.com/questions/385305/efficient-maths-algorithm-to-calculate-intersections
        // also interesting would be this: https://code.google.com/p/straightedge/
        Double x12 = this.oSource.getPos().getX() - this.oDestination.getPos().getX();
        Double x34 = other.getSource().getPos().getX() - other.getDestination().getPos().getX();
        Double y12 = this.oSource.getPos().getY() - this.oDestination.getPos().getY();
        Double y34 = other.getSource().getPos().getY() - other.getDestination().getPos().getY();

        Double c = x12 * y34 - y12 * x34;

        if (Math.abs(c) < 0.01)
        {
            // No intersection , lines are parallel in this case
            return null;
        }
        else
        {
            // Intersection
            Double a = this.oSource.getPos().getX() * this.oDestination.getPos().getY() - this.oSource.getPos().getY() * this.oDestination.getPos().getX();
            Double b = other.getSource().getPos().getX() * other.getDestination().getPos().getY() - other.getSource().getPos().getY() * other.getDestination().getPos().getX();

            // this could be split to get an value r, which makes it possible to remove the isPointInRectancleBetween() Function
            Double x = (a * x34 - b * x12) / c;
            Double y = (a * y34 - b * y12) / c;

            CPosition returnValue = new CPosition(x, y);

            if(oSource.getPos().isPointInRectancleBetween(oDestination.getPos(), returnValue, 1.0)) {
                return returnValue;
            }
        }
        return  null;
    }
}
