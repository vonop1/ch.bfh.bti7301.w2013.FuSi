package GUI;

import Util.CPosition;

import java.awt.*;
import java.util.Vector;

/**
 * Author: Roger Jaggi
 * Date: 13.12.13
 * Time: 16:43
 */
public class CDrawHelper {

    public static void drawPointAsCircle(Graphics2D g2d, CPosition point, Double size, Boolean filled) {

        if(filled) {
            g2d.fillOval(((Double) (point.getX() - (size / 2))).intValue(),
                ((Double) (point.getY() - (size / 2))).intValue(),
                size.intValue(), size.intValue());
        }
        else {
            g2d.drawOval(((Double) (point.getX() - (size / 2))).intValue(),
                    ((Double) (point.getY() - (size / 2))).intValue(),
                    size.intValue(), size.intValue());
        }
    }

    public static void drawPointAsX(Graphics2D g2d, CPosition point, Double size) {
        int upperLeftX = ((Double) (point.getX() - (size / 2))).intValue();
        int upperLeftY = ((Double) (point.getY() - (size / 2))).intValue();
        g2d.drawLine(upperLeftX, upperLeftY, upperLeftX + size.intValue(), upperLeftY + size.intValue());
        g2d.drawLine(upperLeftX + size.intValue(), upperLeftY, upperLeftX, upperLeftY + size.intValue());
    }

    public static void drawLine(Graphics2D g2d, CPosition start, CPosition target) {
        g2d.drawLine(start.getX().intValue(), start.getY().intValue(), target.getX().intValue(), target.getY().intValue());
    }

    public static void drawArrow(Graphics2D g2d, CPosition start, CPosition target, Double arrowSize) {
        g2d.drawLine(start.getX().intValue(), start.getY().intValue(), target.getX().intValue(), target.getY().intValue());

        /*
        AffineTransform tx = new AffineTransform();
        Line2D.Double line = new Line2D.Double(x1,y1,x2,y2);

        Polygon arrowHead = new Polygon();
        arrowHead.addPoint( 0,0);
        arrowHead.addPoint( -5, -10);
        arrowHead.addPoint( 5,-10);

        tx.setToIdentity();
        double angle = Math.atan2(line.y2-line.y1, line.x2-line.x1);
        tx.translate(line.x2, line.y2);
        tx.rotate((angle-Math.PI/2d));

        Graphics2D g = (Graphics2D) g2d.create();
        g.setTransform(tx);
        g.fill(arrowHead);
        g.dispose();
        //g.drawLine(x1,y1,x2,y2); */
    }

    public static void drawPolygon(Graphics2D g2d, Vector<CPosition> positions, Boolean filled) {
        int[] xCoordinates = new int[positions.size()];
        int[] yCoordinates = new int[positions.size()];

        int j = 0;
        for (CPosition position : positions) {
            xCoordinates[j] = position.getX().intValue();
            yCoordinates[j] = position.getY().intValue();
            j += 1;
        }

        if(filled) {
            g2d.fillPolygon(xCoordinates, yCoordinates, positions.size());
        }
        else {
            g2d.drawPolygon(xCoordinates, yCoordinates, positions.size());
        }
    }

}
