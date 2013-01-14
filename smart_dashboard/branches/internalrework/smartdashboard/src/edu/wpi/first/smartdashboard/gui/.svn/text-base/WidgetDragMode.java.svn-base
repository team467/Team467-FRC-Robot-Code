package edu.wpi.first.smartdashboard.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

/**
 *
 * @author Mitchell Wills
 */
public enum WidgetDragMode {

    NONE {
        public Cursor getCursor() {
            return Cursor.getDefaultCursor();
        }

        public void dragged(WidgetPanelLayout layout, WidgetContainer selectedContainer, Rectangle initialBounds, int dx, int dy) {
        }
    },
    MOVE {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
        }

        public void dragged(WidgetPanelLayout layout, WidgetContainer selectedContainer, Rectangle initialBounds, int dx, int dy) {
            resizeAndRelocate(layout, selectedContainer, initialBounds, dx, dy, 0, 0);
        }
    },
    RESIZE_SE {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
        }

        public void dragged(WidgetPanelLayout layout, WidgetContainer selectedContainer, Rectangle initialBounds, int dx, int dy) {
            resizeAndRelocate(layout, selectedContainer, initialBounds, 0, 0, dx, dy);
        }
    },
    RESIZE_E {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
        }

        public void dragged(WidgetPanelLayout layout, WidgetContainer selectedContainer, Rectangle initialBounds, int dx, int dy) {
            resizeAndRelocate(layout, selectedContainer, initialBounds, 0, 0, dx, 0);
        }
    },
    RESIZE_S {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
        }

        public void dragged(WidgetPanelLayout layout, WidgetContainer selectedContainer, Rectangle initialBounds, int dx, int dy) {
            resizeAndRelocate(layout, selectedContainer, initialBounds, 0, 0, 0, dy);
        }
    },
    RESIZE_NW {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
        }

        public void dragged(WidgetPanelLayout layout, WidgetContainer selectedContainer, Rectangle initialBounds, int dx, int dy) {
            resizeAndRelocate(layout, selectedContainer, initialBounds, dx, dy, -dx, -dy);
        }
    },
    RESIZE_N {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
        }

        public void dragged(WidgetPanelLayout layout, WidgetContainer selectedContainer, Rectangle initialBounds, int dx, int dy) {
            resizeAndRelocate(layout, selectedContainer, initialBounds, 0, dy, 0, -dy);
        }
    },
    RESIZE_W {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
        }

        public void dragged(WidgetPanelLayout layout, WidgetContainer selectedContainer, Rectangle initialBounds, int dx, int dy) {
            resizeAndRelocate(layout, selectedContainer, initialBounds, dx, 0, -dx, 0);
        }
    },
    RESIZE_NE {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
        }

        public void dragged(WidgetPanelLayout layout, WidgetContainer selectedContainer, Rectangle initialBounds, int dx, int dy) {
            resizeAndRelocate(layout, selectedContainer, initialBounds, 0, dy, dx, -dy);
        }
    },
    RESIZE_SW {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
        }

        public void dragged(WidgetPanelLayout layout, WidgetContainer selectedContainer, Rectangle initialBounds, int dx, int dy) {
            resizeAndRelocate(layout, selectedContainer, initialBounds, dx, 0, -dx, dy);
        }
    };

    public abstract Cursor getCursor();

    public abstract void dragged(WidgetPanelLayout layout, WidgetContainer selectedContainer, Rectangle initialBounds, int dx, int dy);

    protected void resizeAndRelocate(WidgetPanelLayout layout, WidgetContainer selectedContainer, Rectangle initialBounds, int dx, int dy, int dw, int dh) {
        if (initialBounds != null) {
            int expectedWidth = initialBounds.width + dw;
            int expectedHeight = initialBounds.height + dh;
            layout.resizeTo(selectedContainer, expectedWidth, expectedHeight);
            int widthDiff = expectedWidth-selectedContainer.getWidth();
            int heightDiff = expectedHeight-selectedContainer.getHeight();
            int x = initialBounds.x + dx;
            int y = initialBounds.y + dy;
            if(dx!=0)
                x+=widthDiff;
            if(dy!=0)
                y+=heightDiff;
            
            layout.moveTo(selectedContainer, x, y);
        }
    }
}
