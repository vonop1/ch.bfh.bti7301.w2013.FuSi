package Util;

/**
 * Created with IntelliJ IDEA.
 * author: bohnp1, jaggr2
 * Date: 04.10.13
 * Time: 08:49
 */
public class CEdge {

    private CPosition oSource;
    private CPosition oDestination;
    private int iWeight;

    public CEdge (CPosition oSource, CPosition oDestination, Integer iWeight)
    {
        this.oSource = oSource;
        this.oDestination = oDestination;

        if(iWeight == null) {
            this.iWeight = oSource.getDistanceTo(oDestination).intValue();
        }
        else {
            this.iWeight = iWeight;
        }
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

    /**
     * Intended for debugging.
     */
    @Override
    public String toString() {
        return "CEdge[" + oSource + "->" + oDestination + "]";
    }
}
