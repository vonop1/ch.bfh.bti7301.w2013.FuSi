package Source;

import Util.CPosition;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

/**
 * User: bohnp1
 * Date: 05.11.13
 * Time: 19:08
 */
public class CStrategieManual implements CStrategie, KeyListener {

    private Double angle = 0.0;
    private Double stepSize = 0.0;

    private Boolean isRightPressed = false;
    private Boolean isLeftPressed = false;
    private Boolean isUpPressed = false;
    private Boolean isDownPressed = false;
    private Boolean isShiftPressed = false;

    public static final Double NORMAL_SPEED = 3.0;
    public static final Double HIGH_SPEED = 6.0;

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

    public void recalcValues(KeyEvent e, Boolean isPressed) {

        switch(e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                isRightPressed = isPressed;
                e.consume();
                break;
            case KeyEvent.VK_LEFT:
                isLeftPressed = isPressed;
                e.consume();
                break;
            case KeyEvent.VK_UP:
                isUpPressed = isPressed;
                e.consume();
                break;
            case KeyEvent.VK_DOWN:
                isDownPressed = isPressed;
                e.consume();
                break;
            case KeyEvent.VK_SHIFT:
                isShiftPressed = isPressed;
                e.consume();
                break;
        }

        if(e.isConsumed()) {

            if(isDownPressed || isUpPressed || isLeftPressed || isRightPressed) {
                this.stepSize = ( isShiftPressed ? HIGH_SPEED : NORMAL_SPEED );
            }
            else {
                this.stepSize = 0.0;
            }

            if(isUpPressed && isRightPressed) {
                this.angle = Math.PI * 7/4;
            }
            else if(isUpPressed && isLeftPressed) {
                this.angle = Math.PI * 5/4;
            }
            else if(isDownPressed && isRightPressed) {
                this.angle = Math.PI * 1/4;
            }
            else if(isDownPressed && isLeftPressed) {
                this.angle = Math.PI * 3/4;
            }
            else if(isRightPressed) {
                this.angle = 0.0;
            }
            else if(isLeftPressed) {
                this.angle = Math.PI;
            }
            else if(isUpPressed) {
                this.angle = Math.PI * 3/2;
            }
            else if(isDownPressed) {
                this.angle = Math.PI / 2;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        recalcValues(e, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        recalcValues(e, false);
    }
}
