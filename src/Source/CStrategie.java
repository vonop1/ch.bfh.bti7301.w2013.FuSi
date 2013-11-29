package Source;

import Util.CPosition;

/**
 * Created with IntelliJ IDEA.
 * User: Pascal
 * Date: 29.11.13
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
public interface CStrategie {
    public CPosition calcNextDesiredPosition(Integer roundCount, CWalker walker);
}
