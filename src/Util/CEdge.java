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

    public CEdge (int iId, CVertex oSource, CVertex oDestination, int iWeight)
    {
        this.iId = iId;
        this.oSource = oSource;
        this.oDestination = oDestination;
        this.iWeight = iWeight;
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
}
