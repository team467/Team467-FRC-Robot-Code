package edu.wpi.first.smartdashboard.gui.layout;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the expected behavior of Region objects
 * @author Paul
 */
public class RegionTest {
    private Set<Region> freeSpace;
    Region topLeft;
    Region topRight;
    Region bottomLeft;
    Region bottomRight;

    @Before
    public void setUp() {
        freeSpace = new HashSet<Region>();
        Region.init(10, freeSpace);

        topLeft = new Region(new Point(0, 0));
        topRight = new Region(new Point(10, 0));
        bottomLeft = new Region(new Point(0, 10));
        bottomRight = new Region(new Point(10, 10));

        freeSpace.add(topLeft);
        freeSpace.add(topRight);
        freeSpace.add(bottomLeft);
        freeSpace.add(bottomRight);
    }

    @Test
    public void GetRightAndGetBottomShouldWork() {
        Assert.assertEquals("getRight on topLeft should equal topRight",
                            topLeft.getRight(),
                            topRight);
        Assert.assertEquals("getBottom on topLeft should equal bottomLeft",
                            topLeft.getBottom(),
                            bottomLeft);

        Assert.assertEquals("getRight on bottomLeft should equal bottomRight",
                            bottomLeft.getRight(),
                            bottomRight);
        Assert.assertNull("getBottom on bottomLeft should be null",
                          bottomLeft.getBottom());

        Assert.assertEquals("getBototm on topRight should equal bottomRight",
                            topRight.getBottom(),
                            bottomRight);
        Assert.assertNull("getRight on topRight should be null",
                          topRight.getRight());

        Assert.assertNull("getRight on bottomRight should be null",
                          bottomRight.getRight());
        Assert.assertNull("getBottom on bottomRight should be null",
                          bottomRight.getBottom());
    }

    @Test
    public void searchingForFreeSpaceShouldWork() {
        Region.FreeSpaceSearchResult fssr = topLeft.startSearch(5, 5);

        Assert.assertEquals("Member region set size should be correct",
                            1,
                            fssr.memberRegions.size());
        Assert.assertTrue("Member set should include topLeft",
                          fssr.memberRegions.contains(topLeft));
    }
}
