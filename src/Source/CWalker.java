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
   private Double size = 5.0;
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

   public Double getSize() {
       return size;
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

            Double xDelta = nextCheckPoint.getX() - this.oPos.getX();
            Double yDelta = nextCheckPoint.getY() - this.oPos.getY();

            // Divison by 0 prevention
            if(yDelta.compareTo(0.0) == 0) {
                yDelta = 0.000001;
            }
            Double quotientDelta = ( xDelta / yDelta) + 1;

            // Divison by 0 prevention
            if(quotientDelta.compareTo(0.0) == 0 ) { quotientDelta = 0.000001; }

            Double y = 0.0;
            if (quotientDelta > 0)
            {
                y = Math.sqrt(Math.pow(stepSize, 2) / quotientDelta);
            }
            else
            {
                y = -Math.sqrt(Math.pow(stepSize, 2) / Math.abs(quotientDelta));
            }
            Double x = y * (quotientDelta - 1);

            CPosition newPos = new CPosition(oPos.getX() + x, oPos.getY() + y);

            if(newPos.isNearBy(nextCheckPoint, stepSize / 0.5)) {
                // Yes, we reached a checkpoint, remove it from our list
                this.desiredPath.removeFirst();
            }

            this.oPos = newPos;
        }

        return this.desiredPath.size() == 0;
    }
}
