package Util;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 04.10.13
 * Time: 08:34
 */
public class CVertex implements Comparable<CVertex> {
    protected static int lastId = 0;
    private Integer iId;
    private CPosition oPos;


    public CVertex (CPosition pos)
    {

        this.iId = CVertex.incrementId();
        this.oPos = pos;
    }

    public Integer getId ()
    {
        return iId;
    }

    public CPosition getPos() {
        return oPos;
    }

    public static int incrementId() {
        CVertex.lastId += 1;
        return CVertex.lastId;
    }

    @Override
    public int compareTo(CVertex o) {
        return o.getId().compareTo(this.iId);
    }
}
