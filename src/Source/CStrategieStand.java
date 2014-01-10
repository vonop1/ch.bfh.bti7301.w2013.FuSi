package Source;

import Util.CPosition;

/**
 * User: bohnp1
 * Date: 05.11.13
 * Time: 19:08
 */
public class CStrategieStand implements IStrategie {
    /**
     * calculates the next position and saves the result to the nextDesiredPosition member var
     * @param roundCount the calculation round count
     */
    public CPosition calcNextDesiredPosition(Integer roundCount, CWalker walker) {
        if(walker.desiredPath.size() < 1) {
            return null;
        }

        if(roundCount == 1) {
            CPosition nextCheckPoint = walker.desiredPath.getFirst();

            Double xDelta = nextCheckPoint.getX() - walker.getPosition().getX();
            Double yDelta = nextCheckPoint.getY() - walker.getPosition().getY();

            Double dAngle = 0.0;
            if (xDelta != 0.0)
            {
                dAngle = Math.atan( Math.abs(yDelta) / Math.abs(xDelta) );
            }

            double nextStepDeltaX = Math.cos(dAngle) * walker.stepSize * ( xDelta > 0 ? 1 : -1 );
            double nextStepDeltaY = Math.sin(dAngle) * walker.stepSize * ( yDelta > 0 ? 1 : -1 );

            return new CPosition(walker.getPosition().getX() + nextStepDeltaX, walker.getPosition().getY() + nextStepDeltaY);

        }

        if( walker.hasCollisions() ) {

            // stand still and remove us from the collision object
            walker.getCollisionWith().remove(walker);
        }

        return walker.getPosition();
    }
}
