package Source;

import Util.CPosition;

/**
 * Created with IntelliJ IDEA.
 * author: bohnp1
 * Date: 29.11.13
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
public interface IStrategie {
    public CPosition calcNextDesiredPosition(Integer roundCount, CWalker walker);
}
