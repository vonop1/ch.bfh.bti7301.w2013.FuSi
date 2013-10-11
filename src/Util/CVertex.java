package Util;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 04.10.13
 * Time: 08:34
 */
public class CVertex extends CPosition {
    public CVertex(int X, int Y)
    {
        super(X, Y);
    }

    public CVertex(Double X, Double Y)
    {
        super(X, Y);
    }

    public CVertex(CPosition copy) {
        super(copy.getX(), copy.getY());
    }
}
