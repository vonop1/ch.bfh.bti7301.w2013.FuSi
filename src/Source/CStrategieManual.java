package Source;

import Util.CPosition;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * User: bohnp1
 * Date: 05.11.13
 * Time: 19:08
 */
public class CStrategieManual implements CStrategie, KeyListener {

    private Double angle = 0.0;
    private Double stepSize = 3.0;

    /**
     * calculates the next position and saves the result to the nextDesiredPosition member var
     * @param roundCount the calculation round count
     */
    public CPosition calcNextDesiredPosition(Integer roundCount, CWalker walker) {

        if(roundCount == 1) {
            /* CPosition nextCheckPoint = walker.desiredPath.getFirst();

            Double xDelta = nextCheckPoint.getX() - walker.getPosition().getX();
            Double yDelta = nextCheckPoint.getY() - walker.getPosition().getY();

            Double dAngle = 0.0;
            if (xDelta != 0.0)
            {
                dAngle = Math.atan( Math.abs(yDelta) / Math.abs(xDelta) );
            } */

            double nextStepDeltaX = Math.cos(this.angle) * this.stepSize; // * ( xDelta > 0 ? 1 : -1 );
            double nextStepDeltaY = Math.sin(this.angle) * this.stepSize; // * ( yDelta > 0 ? 1 : -1 );

            return new CPosition(walker.getPosition().getX() + nextStepDeltaX, walker.getPosition().getY() + nextStepDeltaY);

        }

        if( walker.hasCollisions() ) {

            // stand still and remove us from the collision object
            walker.getCollisionWith().remove(walker);
        }

        return walker.getPosition();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                this.angle = 0.0;
                e.consume();
                break;
            case KeyEvent.VK_LEFT:
                this.angle = Math.PI;
                e.consume();
                break;
            case KeyEvent.VK_UP:
                this.angle = Math.PI * 3/2;
                e.consume();
                break;
            case KeyEvent.VK_DOWN:
                this.angle = Math.PI / 2;
                e.consume();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
