package edu.wpi.first.smartdashboard.gui.layout;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Manages layout of DisplayElements on the GUI.
 * 
 * @author pmalmsten
 */
public class LayoutAllocator {
    /**
     * Represents an allocation of space on the GUI.
     */
    public static class LayoutAllocation implements Serializable {
        private final Set<Region> memberRegions;
        public final Point point;
        public final int width;
        public final int height;

        public LayoutAllocation(Point point, 
                                int width,
                                int height,
                                Set<Region> members) {
            this.point = point;
            this.memberRegions = members;
            this.width = width;
            this.height = height;
        }

        /**
         * Invalidates this allocation and returns the space it occupied back
         * into the free space pool.
         */
        public void deallocate() {
            LayoutAllocator.deallocate(this);
        }
    };

    static Set<Region> freeSpace = new HashSet<Region>();
    private static int m_width;
    private static int m_height;
    private static Stack<LayoutAllocation> horizontalResizeConstraintStack = new Stack<LayoutAllocation>();
    private static Stack<LayoutAllocation> verticalResizeConstraintStack = new Stack<LayoutAllocation>();

    private static Comparator<Region> regionsByDistanceToUpperLeft = new Comparator<Region>() {
        public int compare(Region r1, Region r2) {
            return (r1.upperLeft.x + r1.upperLeft.y) - (r2.upperLeft.x + r2.upperLeft.y);
        }
    };

    /**
     * Initializes the LayoutAllocator by declaring the original height and width
     * of the GUI space it manages.
     *
     * @param width
     * @param height
     */
    public static synchronized void init(int regionSize, int width, int height) {
        Region.init(regionSize, freeSpace);

        freeSpace.clear();
        for(int i = 0; i < width; i += regionSize) {
            for(int j = 0; j < height; j += regionSize) {
                freeSpace.add(new Region(new Point(i, j)));
            }
        }

        m_width = width;
        m_height = height;

        horizontalResizeConstraintStack.clear();
        verticalResizeConstraintStack.clear();
    }

    /**
     * Request an exclusive GUI space allocation of the given width and height.
     *
     * @param width The requested allocation width.
     * @param height The requested allocation height.
     * @return A LayoutAllocation result. Null if the request could not be satisfied.
     */
    public static synchronized LayoutAllocation allocate(int width, int height) {
        return allocate(null, width, height);
    }

    /**
     * Request an exclusive GUI space allocation of the given width and height
     * at the given point.
     *
     * @param point A point which the result allocation must be located.
     * @param width The requested allocation height.
     * @param height The requested allocation width.
     * @return A LayoutAllocation result. Null if the request could not be satisfied.
     */
    public static synchronized LayoutAllocation allocate(Point loc, int width, int height) {
        List<Region> searchList;

        if(loc == null)
            searchList = new LinkedList<Region>(freeSpace);
        else {
            int regionX = (loc.x / Region.size) * Region.size;
            int regionY = (loc.y / Region.size) * Region.size;
            Region searchRegion = new Region(new Point(regionX, regionY));

            if(!freeSpace.contains(searchRegion))
                return null;

            searchList = new LinkedList<Region>();
            searchList.add(searchRegion);
        }
         
        Collections.sort(searchList, regionsByDistanceToUpperLeft);

        for(Region r : searchList) {
            Region.FreeSpaceSearchResult fssr = r.startSearch(width, height);

            if(fssr.viable) {
                freeSpace.removeAll(fssr.memberRegions);
                LayoutAllocation la = new LayoutAllocator.LayoutAllocation(r.upperLeft,
                                                          width,
                                                          height,
                                                          fssr.memberRegions);
                addToConstraintStacks(la);
                return la;
            }
        }

        return null;
    }

    public static synchronized LayoutAllocation forceAllocate(Point loc, int width, int height) {
	Set<Region> memberRegions = new HashSet<Region>();
	
	int regionX = floorCoordinateToRegion(loc.x);
	int regionY = floorCoordinateToRegion(loc.y);

	for(int x = regionX; x < regionX + width; x += Region.size) {
	    for(int y = regionY; y < regionY + height; y += Region.size) {
		Region member = new Region(new Point(x, y));
		if(freeSpace.contains(member)) {
		    memberRegions.add(member);
		    freeSpace.remove(member);
		}
	    }
	}

	return new LayoutAllocation(loc, width, height, memberRegions);
    }

    public static synchronized LayoutAllocation moveAllocation(LayoutAllocation la, Point newPoint) {
        int oldWidth = la.width;
        int oldHeight = la.height;
        Point oldPoint = la.point;

        if(oldPoint.x == floorCoordinateToRegion(newPoint.x) &&
           oldPoint.y == floorCoordinateToRegion(newPoint.y))
            return la;

        la.deallocate();

        LayoutAllocation newLa = allocate(newPoint, oldWidth, oldHeight);

        if(newLa != null) {
            return newLa;
        } else {
            // Move failed, try to re-acquire old allocation
            newLa = allocate(oldPoint, oldWidth, oldHeight);

            if(newLa != null)
                return newLa;
            else
                throw new RuntimeException("Critical error: unable to reclaim prior"
                                        + "layout allocation during move.");
        }
    }

    public static synchronized LayoutAllocation forceMoveAllocation(LayoutAllocation la, Point newPoint) {
        int oldWidth = la.width;
        int oldHeight = la.height;
        Point oldPoint = la.point;

        if(oldPoint.x == floorCoordinateToRegion(newPoint.x) &&
           oldPoint.y == floorCoordinateToRegion(newPoint.y))
            return la;

        la.deallocate();

        LayoutAllocation newLa = forceAllocate(newPoint, oldWidth, oldHeight);
	return newLa;
    }

    public static synchronized int restrictedHeightResize(int requested) {
        if(requested == m_height)
            return m_height;

        requested = floorCoordinateToRegion(requested);

        if(requested < m_height) {
            if(!verticalResizeConstraintStack.isEmpty() &&
                requested < verticalResizeConstraintStack.peek().point.y +
                            verticalResizeConstraintStack.peek().height) {
                requested = verticalResizeConstraintStack.peek().point.y +
                             verticalResizeConstraintStack.peek().height;
            }

            for(int y = m_height; y >= requested; y -= Region.size) {
                for(int x = 0; x < m_width; x += Region.size) {
                    freeSpace.remove(new Region(new Point(x, y)));
                }
            }
        } else {
            for(int y = m_height; y < requested; y += Region.size) {
                for(int x = 0; x < m_width; x += Region.size) {
                    freeSpace.add(new Region(new Point(x, y)));
                }
            }
        }

        m_height = requested;
        return requested;
    }

    public static synchronized int restrictedWidthResize(int requested) {
        if(requested == m_width)
            return m_width;

        requested = floorCoordinateToRegion(requested);

        if(requested < m_width) {
            if(!horizontalResizeConstraintStack.isEmpty() &&
                requested < horizontalResizeConstraintStack.peek().point.y +
                            horizontalResizeConstraintStack.peek().height) {
                requested = horizontalResizeConstraintStack.peek().point.y +
                             horizontalResizeConstraintStack.peek().height;
            }

            for(int x = m_width; x >= requested; x -= Region.size) {
                for(int y = 0; y < m_height; y += Region.size) {
                    freeSpace.remove(new Region(new Point(x, y)));
                }
            }
        } else {
            for(int x = m_width; x < requested; x += Region.size) {
                for(int y = 0; y < m_height; y += Region.size) {
                    freeSpace.add(new Region(new Point(x, y)));
                }
            }
        }

        m_width = requested;
        return requested;
    }

    private static synchronized void deallocate(LayoutAllocation la) {
        for(Region r : la.memberRegions) {
            if(r.upperLeft.x + r.size <= m_width &&
               r.upperLeft.y + r.size <= m_height)
                freeSpace.add(r);
        }
        removeFromConstraintStacks(la);
    }

    private static void addToConstraintStacks(LayoutAllocation la) {
        if(horizontalResizeConstraintStack.isEmpty())
            horizontalResizeConstraintStack.push(la);
        else {
            LayoutAllocation currentWidthConstraint = horizontalResizeConstraintStack.peek();
            if(la.point.x + la.width > currentWidthConstraint.point.x + currentWidthConstraint.width)
                horizontalResizeConstraintStack.push(la);
        }

        if(verticalResizeConstraintStack.isEmpty())
            verticalResizeConstraintStack.push(la);
        else {
            LayoutAllocation currentHeightConstraint = verticalResizeConstraintStack.peek();
            if(la.point.y + la.height > currentHeightConstraint.point.y + currentHeightConstraint.height)
                verticalResizeConstraintStack.push(la);
        }
    }

    private static void removeFromConstraintStacks(LayoutAllocation la) {
        if(!horizontalResizeConstraintStack.isEmpty())
            if(horizontalResizeConstraintStack.peek().equals(la))
                horizontalResizeConstraintStack.pop();

        if(!verticalResizeConstraintStack.isEmpty())
            if(verticalResizeConstraintStack.peek().equals(la))
                verticalResizeConstraintStack.pop();
    }

    public static int floorCoordinateToRegion(int coordinate) {
        return (coordinate / Region.size) * Region.size;
    }

    public static void saveState(ObjectOutputStream objOut) throws IOException {
        objOut.writeObject(freeSpace);
        objOut.writeObject(m_width);
        objOut.writeObject(m_height);
        objOut.writeObject(horizontalResizeConstraintStack);
        objOut.writeObject(verticalResizeConstraintStack);
    }

    public static void loadState(ObjectInputStream objIn) throws IOException, ClassNotFoundException {
        freeSpace = (Set<Region>) objIn.readObject();
        m_width = (Integer) objIn.readObject();
        m_height = (Integer) objIn.readObject();
        horizontalResizeConstraintStack = (Stack<LayoutAllocation>) objIn.readObject();
        verticalResizeConstraintStack = (Stack<LayoutAllocation>) objIn.readObject();
    }
}
