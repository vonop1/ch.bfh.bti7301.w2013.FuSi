package Source;

import Util.CPosition;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

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

   private Double halfWalkerSize = 5.0;
   private Double stepSize = 2.0;

   private LinkedList<CPosition> desiredPath = new LinkedList<CPosition>();
   private LinkedList<CWalker> blockedWith = new LinkedList<CWalker>();


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
       return desiredNextPosition;
   }

   public Double getHalfWalkerSize() {
       return halfWalkerSize;
   }

    public Boolean isBlocked() {
        return this.blockedWith.size() > 0;
    }

    public void setDesiredPath(LinkedList<CPosition> vertexes) {
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

    public void resetBlockedOn() {
        this.blockedWith = new LinkedList<CWalker>();
    }

    public LinkedList<CWalker> getBlockedWith() {
        return this.blockedWith;
    }

    /**
     * checks if the walker has a collision with the desiredNextPosition of another walker
     * @param other the walker
     * @return true if we have a collision or false if not
     */
    public boolean checkCollisionWith(CWalker other) {

        if(this.equals(other) || this.getDesiredNextPosition() == null || other.getDesiredNextPosition() == null) {
            return false;
        }

        boolean hasCollision = this.getDesiredNextPosition().getDistanceTo(other.getDesiredNextPosition()) < this.getHalfWalkerSize() + other.getHalfWalkerSize();

        if(hasCollision) {
            this.blockedWith.add(other);
        }

        return hasCollision;
    }


    public boolean isSomeoneInFrontOf(Collection<CWalker> others) {
        if(this.desiredNextPosition == null) {
            return false;
        }
        // show if the collision is in the walk direction of the walker
        CPosition frontPosition = new CPosition(this.desiredNextPosition.getX() + this.nextStepDeltaX, this.desiredNextPosition.getY() + this.nextStepDeltaY);

        for(CWalker other : others) {
            if(other.getDesiredNextPosition() != null && frontPosition.getDistanceTo(other.getDesiredNextPosition()) < (this.getHalfWalkerSize() + other.getHalfWalkerSize()) ) {
                return true;
            }
        }

        return false;
    }

    /**
     * calculates the next position and saves the result to the nextDesiredPosition member var
     * if there is no next position, desiredNextPosition is set to NULL
     */
    /**
     * calculates the next position and saves the result to the nextDesiredPosition member var
     * @param roundCount the calculation round count
     * @return true if the new position has no collision with others
     */
    public boolean calcNextDesiredPosition(Integer roundCount) {
        if(this.desiredPath.size() < 1) {
            this.desiredNextPosition = null;
            return false;
        }

        if(roundCount == 1) {
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

            return false;
        }
        else {
            if( this.isBlocked() ) {
                this.desiredNextPosition = this.currentPosition;

                /* boolean minOneIsBlocked = false;
                for(CWalker blockedWalker : this.blockedWith) {
                    minOneIsBlocked = minOneIsBlocked || blockedWalker.isBlocked();
                }

                if(minOneIsBlocked) {
                    // if minimum one other is blocked, then we stand still and remove the blockation
                    this.desiredNextPosition = this.currentPosition;
                } */
            }
        }

        this.resetBlockedOn();
        return false;
    }

    /**
     * walks a step to the next desired Position and resets desiredNextPosition member variable to NULL
     * @return true if ok or false if the walker is on the target
     */
    public boolean walkToNextDesiredPosition() {

        if(this.isBlocked()) {
            throw new IllegalArgumentException("The Walker must no be blocked!");
        }

        if(this.desiredNextPosition == null) {
            throw new IllegalArgumentException("this.desiredNextPosition must not be NULL!");
        }

        if(this.desiredPath.size() > 0) {
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
