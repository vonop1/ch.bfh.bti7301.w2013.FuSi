package GUI;

import Source.CWorld;

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
    private CWorld worldReference = null;

    public CDrawObject(CWorld worldReference, Boolean isVisible, Integer visibleShortcutKey) {
        this.isVisible = isVisible;
        this.visibleShortcutKey = visibleShortcutKey;
        this.worldReference = worldReference;
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
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // currently do nothing
    }

    public abstract void doDrawing(Graphics2D g2d);

}
