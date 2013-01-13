package edu.wpi.first.smartdashboard.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author Mitchell Wills
 */
public class WidgetPanelGlassPanel extends JComponent implements MouseMotionListener, MouseListener, PopupMenuListener {

    private final WidgetPanel panel;
    private final WidgetPanelLayout layout;
    private final WidgetPopupMenu popupMenu;

    public WidgetPanelGlassPanel(final WidgetPanel panel, final WidgetPanelLayout layout) {
        this.panel = panel;
        this.layout = layout;
        popupMenu = new WidgetPopupMenu();
        setOpaque(false);
        setFocusable(true);
        addMouseMotionListener(this);
        addMouseListener(this);
        popupMenu.addPopupMenuListener(this);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                layout.keyPressed(e);
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                layout.keyReleased(e);
                repaint();
            }
        });
    }
    
    
    
    
    private WidgetContainer selectedContainer = null;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        layout.paintOverlay(g, selectedContainer);
    }
    private WidgetDragMode dragMode = WidgetDragMode.NONE;
    private Point mouseStart = null;
    private WidgetContainer draggedContainer = null;
    private Rectangle dragBounds = null;

    private void enterDragMode(WidgetDragMode dragMode, MouseEvent e) {
        this.dragMode = dragMode;
        if (e == null) {
            mouseStart = null;
        } else {
            mouseStart = e.getPoint();
        }
        draggedContainer = selectedContainer;
        if (draggedContainer == null) {
            dragBounds = null;
        } else {
            dragBounds = draggedContainer.getBounds();
        }
    }
    public static final int RESIZE_BORDER_WIDTH = 6;

    private WidgetDragMode getDragMode(Point p, WidgetContainer selectedContainer) {//TODO refactor this out to panel layout
        if (selectedContainer == null || p == null) {
            return WidgetDragMode.NONE;
        }
        if (!selectedContainer.isResizable() || !layout.supportsResize()) {
            return WidgetDragMode.MOVE;
        }

        Point translatedPoint = SwingUtilities.convertPoint(this, p, selectedContainer);
        int x = translatedPoint.x;
        int y = translatedPoint.y;
        int width = selectedContainer.getWidth();
        int height = selectedContainer.getHeight();

        if (x <= RESIZE_BORDER_WIDTH) {
            if (y <= RESIZE_BORDER_WIDTH) {
                return WidgetDragMode.RESIZE_NW;
            } else if (height - y <= RESIZE_BORDER_WIDTH) {
                return WidgetDragMode.RESIZE_SW;
            } else {
                return WidgetDragMode.RESIZE_W;
            }
        } else if (width - x <= RESIZE_BORDER_WIDTH) {
            if (y <= RESIZE_BORDER_WIDTH) {
                return WidgetDragMode.RESIZE_NE;
            } else if (height - y <= RESIZE_BORDER_WIDTH) {
                return WidgetDragMode.RESIZE_SE;
            } else {
                return WidgetDragMode.RESIZE_E;
            }
        } else if (y <= RESIZE_BORDER_WIDTH) {
            return WidgetDragMode.RESIZE_N;
        } else if (height - y <= RESIZE_BORDER_WIDTH) {
            return WidgetDragMode.RESIZE_S;
        }

        return WidgetDragMode.MOVE;
    }

    public void mousePressed(MouseEvent e) {
        recalcSelected(e);
        enterDragMode(WidgetDragMode.NONE, e);

        if (selectedContainer != null) {
            if (!tryPopup(e)){
                enterDragMode(getDragMode(e.getPoint(), selectedContainer), e);
                mouseStart = e.getPoint();
            }
        }
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        recalcSelected(e);
        tryPopup(e);
        enterDragMode(WidgetDragMode.NONE, e);
        repaint();
    }
    
    private boolean tryPopup(MouseEvent e){
        if (e.isPopupTrigger()){
            popupMenu.show(this, selectedContainer, layout, e.getX(), e.getY());
            return true;
        }
        return false;
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
    public void popupMenuCanceled(PopupMenuEvent e) {}
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        selectedContainer = null;//TODO figure out where the mouse is and recalc selected
        repaint();
    }

    
    private void recalcSelected(MouseEvent e){
        WidgetContainer container = panel.findWidgetAt(e.getPoint());
        if (container != selectedContainer) {
            selectedContainer = container;
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (mouseStart == null) {
            dragMode.dragged(layout, draggedContainer, dragBounds, 0, 0);
        } else {
            dragMode.dragged(layout, draggedContainer, dragBounds, e.getX() - mouseStart.x, e.getY() - mouseStart.y);
        }
        setCursor(dragMode.getCursor());//TODO change cursor even if it is outside of window
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
        recalcSelected(e);

        setCursor(getDragMode(e.getPoint(), selectedContainer).getCursor());

        repaint();
    }

    public void mouseExited(MouseEvent e) {
        if(dragMode==WidgetDragMode.NONE){
            selectedContainer = null;
            repaint();
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }
}
