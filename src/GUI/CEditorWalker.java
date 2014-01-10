package GUI;

import Source.CWalker;
import Util.CPosition;

/**
 * Created with IntelliJ IDEA.
 * User: pvonow
 * Date: 10.01.14
 * Time: 13:08
 */
public class CEditorWalker extends CWalker {

    //number of walkers on a start position
    private int count;

    /**
     * constructor for new editor walkers
     * @param start the start position
     * @param target the end position
     * @param count the number of walkers on the position
     */
    public CEditorWalker(CPosition start, CPosition target, int count) {
        super(start, target, null);
        this.count = count;
    }

    /**
     * getter for count
     * @return numbers of walkers
     */
    public int getCount(){
        return this.count;
    }

    /**
     * setter for count
     * @param count new numbers of walker
     */
    public void setCount(int count){
        this.count = count;
    }
}
