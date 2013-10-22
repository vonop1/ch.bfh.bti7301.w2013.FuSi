package Source;

import Util.CPosition;
import Util.CVertex;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 27.09.13
 * Time: 14:09
 */
public class CWalker {
   static Integer idCounter = 0;

    static Integer getNextId()
    {
        return idCounter++;
    }


   private Integer id;
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
       this.id = getNextId();
   }

   public CPosition getPosition()
   {
       return oPos;
   }

   public CPosition getTarget()
   {
       return oTarget;
   }

   public Double getSize() {
       return size;
   }

    public void setDesiredPath(LinkedList<CVertex> vertexes) {
        if(vertexes == null) {
            this.desiredPath = new LinkedList<CVertex>();
        }
        else {
            this.desiredPath = vertexes;
            if(vertexes.size() > 0) {
                this.desiredPath.removeFirst(); // remove his own position
            }
        }
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

            Double x = 0.0;
            Double y = 0.0;

            Double dAngle = 0.0;
            if (xDelta != 0.0)
            {
                dAngle = Math.atan( Math.abs(yDelta) / Math.abs(xDelta) );
            }

            if (xDelta > 0)
            {
                // positive x direction
                x = Math.cos(dAngle) * stepSize;

            }
            else
            {
                //negative x Direction
                x = - Math.cos(dAngle) * stepSize;
            }

            if (yDelta > 0)
            {
                // positive y Direction
                y = Math.sin(dAngle) * stepSize;
            }
            else
            {
                // negative y Direction
                y = - (Math.sin(dAngle) * stepSize);
            }

            CPosition newPos = new CPosition(oPos.getX() + x, oPos.getY() + y);

            if(newPos.isNearBy(nextCheckPoint, stepSize / 0.5)) {
                // Yes, we reached a checkpoint, remove it from our list
                this.desiredPath.removeFirst();
            }

            this.oPos = newPos;
        }

        return this.desiredPath.size() == 0;
    }

    /**
     * Get the unique Identifier of the Walker
     * @return unique Identifier
     */
    public Integer getId ()
    {
        return id;
    }

    @Override
    public boolean equals (Object obj) {
        return obj.getClass() == this.getClass() && ((CWalker)obj).getId().equals(this.getId());
    }
}
