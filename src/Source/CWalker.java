package Source;

import Util.CPosition;
import Util.CVertex;

import java.util.LinkedList;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 27.09.13
 * Time: 14:09
 */
public class CWalker {
   private CPosition oPos;
   private CPosition oTarget;
   private CPosition oStart;
   private Integer iSize = 5;
   private Double stepSize = 2.0;

   private LinkedList<CVertex> desiredPath = new LinkedList<CVertex>();

   public CWalker(CPosition start, CPosition target) {
       this.oStart = start;
       this.oPos = start;
       this.oTarget = target;
   }

   public CPosition getPosition()
   {
       return oPos;
   }

   public CPosition getTarget()
   {
       return oTarget;
   }

    public CPosition getStart()
    {
        return oStart;
    }

   public Integer getSize() {
       return iSize;
   }

    public void setDesiredPath(LinkedList<CVertex> vertexes) {
        if(vertexes == null) {
            this.desiredPath = new LinkedList<CVertex>();
        }
        this.desiredPath = vertexes;
        this.desiredPath.removeFirst(); // remove his own position
    }

    public LinkedList<CVertex> getDesiredPath() {
        return this.desiredPath;
    }

    public boolean walkAStep() {

        if(this.desiredPath.size() > 0)
        {
            CVertex nextCheckPoint = this.desiredPath.getFirst();

            Double delta = (nextCheckPoint.getX() - this.oPos.getX()) / (nextCheckPoint.getY() - this.oPos.getY());
            if(delta == 1 ) { delta = 0.9999; }

            Double y = Math.sqrt(Math.pow(stepSize, 2) /  (1 + delta));
            Double x = y * delta;

            CPosition newpos = new CPosition(oPos.getX() + x, oPos.getY() + y);

            if(newpos.isNearBy(nextCheckPoint, stepSize)) {
                // Yes, we reached a checkpoint, remove it from our list
                this.desiredPath.removeFirst();
            }

            this.oPos = newpos;
        }

        return this.desiredPath.size() == 0;
    }
}
