package Source;

import Util.CPosition;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 15.11.13
 * Time: 14:33
 */

public class CWalkerRightLeft extends CWalker {
    public CWalkerRightLeft(CPosition start, CPosition target, CWorld worldReference) {
        super(start, target, worldReference);
    }
    /**
     * calculates the next position and saves the result to the nextDesiredPosition member var
     * @param roundCount the calculation round count
     */

    @Override
    public void calcNextDesiredPosition(Integer roundCount) {
        if(this.desiredPath.size() < 1) {
            this.desiredNextPosition = null;
        }

        CPosition nextCheckPoint = this.desiredPath.getFirst();

        Double xDelta = nextCheckPoint.getX() - this.currentPosition.getX();
        Double yDelta = nextCheckPoint.getY() - this.currentPosition.getY();

        Double dAngle = 0.0;
        if (xDelta != 0.0)
        {
            dAngle = Math.atan( Math.abs(yDelta) / Math.abs(xDelta) );
        }

        if (xDelta < 0)
        {
            if (yDelta < 0)
            {
                dAngle = Math.PI + dAngle;
            }
            else
            {
                dAngle = Math.PI - dAngle;
            }
        }
        else
        {
            if (yDelta < 0)
            {
                dAngle = -dAngle;
            }
        }

        dAngle = dAngle + ((CWorld.randomGenerator.nextBoolean() ? -1 : +1) * (roundCount - 1) * 0.1);

        if (dAngle > Math.PI)
        {
            dAngle = dAngle - 2 * Math.PI;
        }

        double nextStepDeltaX = Math.cos(dAngle) * stepSize; // * ( xDelta > 0 ? 1 : -1 );
        double nextStepDeltaY = Math.sin(dAngle) * stepSize; // * ( yDelta > 0 ? 1 : -1 );

        this.desiredNextPosition = new CPosition(currentPosition.getX() + nextStepDeltaX, currentPosition.getY() + nextStepDeltaY);

    }
}
