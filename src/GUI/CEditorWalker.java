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

    private int count;

    public CEditorWalker(CPosition start, CPosition target, int count) {
        super(start, target, null);
        this.count = count;
    }

    public int getCount(){
        return this.count;
    }

    public void setCount(int count){
        this.count = count;
    }
}
