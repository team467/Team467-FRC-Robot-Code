package edu.wpi.first.smartdashboard.gui.layout;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a fixed size portion of screen real estate;
 * @author pmalmsten
 */
public class Region implements Serializable {
    public class FreeSpaceSearchResult {
        public boolean viable = true;
        public Set<Region> memberRegions = new HashSet<Region>();
    }

    static int size = 0;
    Point upperLeft;
    private static Set<Region> freeSpace;

    static void init(int regionSize, Set<Region> freeRegions) {
        freeSpace = freeRegions;
        size = regionSize;
    }

    public Region(Point upperLeft) {
        this.upperLeft = upperLeft;
    }

    public FreeSpaceSearchResult startSearch(int width, int height) {
        FreeSpaceSearchResult fssr = new FreeSpaceSearchResult();

        this.search(fssr, width, height, 0, 0);

        return fssr;
    }

    private void search(FreeSpaceSearchResult fssr,
                        int maxWidth,
                        int maxHeight,
                        int currentWidth,
                        int currnetHeight) {

        if(fssr.memberRegions.contains(this))
            return;
        
        fssr.memberRegions.add(this);

        if(currentWidth + size < maxWidth) {
            Region right = getRight();
            if(right != null)
                right.search(fssr, maxWidth, maxHeight, currentWidth + size, currnetHeight);
            else
                fssr.viable = false;
        }

        if(currnetHeight + size < maxHeight) {
            Region bottom = getBottom();
            if(bottom != null)
                bottom.search(fssr, maxWidth, maxHeight, currentWidth, currnetHeight + size);
            else
                fssr.viable = false;
        }
    }

    Region getBottom() {
        Point p = new Point(upperLeft);
        p.y += size;
        Region r = new Region(p);

        if(freeSpace.contains(r))
            return r;
        return null;
    }

    Region getRight() {
        Point p = new Point(upperLeft);
        p.x += size;
        Region r = new Region(p);

        if(freeSpace.contains(r))
            return r;
        return null;
    }

    @Override
    public int hashCode() {
        return upperLeft.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Region other = (Region) obj;
        if (this.upperLeft != other.upperLeft && (this.upperLeft == null || !this.upperLeft.equals(other.upperLeft))) {
            return false;
        }
        return true;
    }
}
