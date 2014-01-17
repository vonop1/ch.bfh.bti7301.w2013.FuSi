package GUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Author: jaggr2 <roger.jaggi@blastbeatsyndicate.com>
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
}
