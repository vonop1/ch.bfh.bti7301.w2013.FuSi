package Source;

import Util.CPosition;

/**
 * Created with IntelliJ IDEA.
 * author: bohnp1
 * Date: 15.11.13
 * Time: 14:33
 */

public class CStrategieRightLeft implements IStrategie {
    /**
     * calculates the next position and saves the result to the nextDesiredPosition member var
     * @param roundCount the calculation round count
     */
    public CPosition calcNextDesiredPosition(Integer roundCount, CWalker walker) {
        if(walker.desiredPath.size() < 1) {
            return null;
        }

        CPosition nextCheckPoint = walker.desiredPath.getFirst();

        Double xDelta = nextCheckPoint.getX() - walker.getPosition().getX();
        Double yDelta = nextCheckPoint.getY() - walker.getPosition().getY();

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

        double nextStepDeltaX = Math.cos(dAngle) * walker.stepSize; // * ( xDelta > 0 ? 1 : -1 );
        double nextStepDeltaY = Math.sin(dAngle) * walker.stepSize; // * ( yDelta > 0 ? 1 : -1 );

        return new CPosition(walker.getPosition().getX() + nextStepDeltaX, walker.getPosition().getY() + nextStepDeltaY);

    }
}
