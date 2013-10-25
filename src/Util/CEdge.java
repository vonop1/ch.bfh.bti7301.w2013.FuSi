package Util;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 04.10.13
 * Time: 08:49
 */
public class CEdge {

    private int iId;
    private CPosition oSource;
    private CPosition oDestination;
    private int iWeight;

    public CEdge (CPosition oSource, CPosition oDestination, Integer iWeight)
    {
        this.iId = CGraph.incrementId();
        this.oSource = oSource;
        this.oDestination = oDestination;

        // we calculate the line representation in the form a*x + b*y + c = 0
        // a, b and c are constant values

        //Double dx = oDestination.getX() - oSource.getX();
        //Double dy = oDestination.getY() - oDestination.getY();

        //this.edgeConstantA = +dy;
        //this.edgeConstantB = -dx;
        //this.edgeConstantC = -(edgeConstantA*oDestination.getX() + edgeConstantB*oDestination.getY());

        if(iWeight == null) {
            this.iWeight = oSource.getDistanceTo(oDestination).intValue();
        }
        else {
            this.iWeight = iWeight;
        }
    }

    public int getId ()
    {
        return iId;
    }

    public CPosition getSource ()
    {
        return oSource;
    }

    public CPosition getDestination ()
    {
        return oDestination;
    }

    public int getWeight ()
    {
        return iWeight;
    }

    public boolean hasIntersectionWith(CEdge other) {

        CPosition returnValue = CMathFunctions.calcIntersectionPoint(this.getSource(), this.getDestination(), other.getSource(), other.getDestination(), false, false);

        return returnValue != null;
    }
}
