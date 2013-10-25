package Source;

import Util.CPosition;

import java.util.LinkedList;
import java.util.NoSuchElementException;

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
   private CPosition currentPosition;
   private CPosition targetPosition;
   private CPosition startPosition;
   private CPosition desiredNextPosition;
   private Double nextStepDeltaX;
   private Double nextStepDeltaY;

   private Double size = 5.0;
   private Double stepSize = 2.0;

   private LinkedList<CPosition> desiredPath = new LinkedList<CPosition>();

   public CWalker(CPosition start, CPosition target) {
       this.startPosition = start;
       this.currentPosition = start;
       this.targetPosition = target;
       this.id = getNextId();
   }

   public CPosition getPosition()
   {
       return currentPosition;
   }

   public CPosition getTarget()
   {
       return targetPosition;
   }

   public CPosition getDesiredNextPosition() {
       // if desiredNextPosition is null, he wants to stay at his location
       if(desiredNextPosition == null) {
           return currentPosition;
       }
       else {
           return desiredNextPosition;
       }
   }

   public Double getSize() {
       return size;
   }

    public Boolean isStanding() {
        return this.desiredNextPosition == null;
    }

    public void setDesiredPath(LinkedList<CVertex> vertexes) {
        if(vertexes == null || vertexes.size() == 0) {
            throw new IllegalArgumentException("Der desiredPath darf nicht Null oder leer sein --> vermutlich Dijsktra kam zu keinem Ergebnis!");
        }

        this.desiredPath = vertexes;

        if(currentPosition.isNearBy(this.desiredPath.getFirst(), stepSize / 0.5)) {
            // the first checkpoint is the current position, remove it from our list
            this.desiredPath.removeFirst();
        }
    }

    public LinkedList<CPosition> getDesiredPath() {
        return this.desiredPath;
    }

    public boolean checkAndHandleCollisionWith(CWalker other) {

        if(this.equals(other)) {
            return false;
        }

        Boolean hasCollision = this.getDesiredNextPosition().isNearBy(other.getDesiredNextPosition(), this.getSize() + other.getSize() + 1);

        if(hasCollision) {

            // show if the collision is in the walk direction of the walker
            if(this.desiredNextPosition != null && (new CPosition(this.desiredNextPosition.getX() + this.nextStepDeltaX, this.desiredNextPosition.getY() + this.nextStepDeltaY))
                    .isNearBy(other.getDesiredNextPosition(), this.getSize() + other.getSize() + 1)) {

                if(!other.isStanding()) {
                    // when we have a collision, just wait if the other is not waiting
                    this.desiredNextPosition = null;
                }
            }
            else {
                hasCollision = false;
            }
        }

        return hasCollision;
    }


    public boolean isInFrontOf(CWalker other, Double maxDistance) {

        return false;

    }

    /**
     * calculates the next position and saves the result to the nextDesiredPosition member var
     * if there is no next position, desiredNextPosition is set to NULL
     */
    public void calcNextDesiredPosition() {
        if(this.desiredPath.size() < 1) {
            this.desiredNextPosition = null;
            return;
        }

        CPosition nextCheckPoint = this.desiredPath.getFirst();

        Double xDelta = nextCheckPoint.getX() - this.currentPosition.getX();
        Double yDelta = nextCheckPoint.getY() - this.currentPosition.getY();

        Double dAngle = 0.0;
        if (xDelta != 0.0)
        {
            dAngle = Math.atan( Math.abs(yDelta) / Math.abs(xDelta) );
        }

        this.nextStepDeltaX = Math.cos(dAngle) * stepSize * ( xDelta > 0 ? 1 : -1 );
        this.nextStepDeltaY = Math.sin(dAngle) * stepSize * ( yDelta > 0 ? 1 : -1 );

        this.desiredNextPosition = new CPosition(currentPosition.getX() + nextStepDeltaX, currentPosition.getY() + nextStepDeltaY);
    }

    /**
     * walks a step to the next desired Position and resets desiredNextPosition member variable to NULL
     * @return true if ok or false if the walker is on the target
     */
    public boolean walkToNextDesiredPosition() {

        if(this.desiredPath.size() > 0)
        {
            if(this.desiredNextPosition == null) {
                return false;
            }

            if(this.desiredNextPosition.isNearBy(this.desiredPath.getFirst(), stepSize / 0.5)) {
                // Yes, we reached a checkpoint, remove it from our list
                this.desiredPath.removeFirst();
            }

            this.currentPosition = this.desiredNextPosition;

        }

        this.desiredNextPosition = null;
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
