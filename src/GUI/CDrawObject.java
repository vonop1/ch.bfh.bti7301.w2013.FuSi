package GUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Copyright 2013 blastbeat syndicate gmbh
 * Author: Roger Jaggi <roger.jaggi@blastbeatsyndicate.com>
 * Date: 08.11.13
 * Time: 13:39
 */
public abstract class CDrawObject implements KeyListener {

    private Integer visibleShortcutKey = null;
    private Boolean isVisible = false;
    private String description = "";

    public CDrawObject(Boolean isVisible, Integer visibleShortcutKey, String description) {
        this.isVisible = isVisible;
        this.visibleShortcutKey = visibleShortcutKey;
        this.description = description;
    }

    public Integer getVisibleShortcutKey() {
        return visibleShortcutKey;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // currently nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if( visibleShortcutKey != null && visibleShortcutKey == e.getKeyCode() )
        {
            this.isVisible = !this.isVisible;
            e.consume();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // currently do nothing
    }

    public Boolean draw(Graphics2D g2d) {
        if(this.isVisible) {
            doDrawing(g2d);
        }
        return true;
    }

    public abstract void doDrawing(Graphics2D g2d);

    /*
    private void drawArrowLine(Graphics2D g2d, int x1, int y1, int x2, int y2) {

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
        //g.drawLine(x1,y1,x2,y2);
    }  */
}
