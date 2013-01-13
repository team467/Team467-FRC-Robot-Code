package edu.wpi.first.smartdashboard.gui.layout;

import java.awt.Point;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Highest level testing of LayoutAllocator.
 *
 * @author pmalmsten
 */
public class AcceptanceTest {
    @Test
    public void simpleUseCase() {
        LayoutAllocator.init(20, 200, 200);
        LayoutAllocator.LayoutAllocation alloc = LayoutAllocator.allocate(100, 100);

        Assert.assertNotNull("Allocation must be valid", alloc);
        Assert.assertEquals("Allocation point must be correct", new Point(0, 0),
                                                                alloc.point);

        alloc.deallocate();

        // Do the same thing over again to ensure proper assimilation of free space.
        alloc = LayoutAllocator.allocate(100, 100);

        Assert.assertNotNull("Reallocation must be valid", alloc);
        Assert.assertEquals("Reallocation point must be correct", new Point(0, 0),
                                                                alloc.point);
    }

    @Test
    public void requestAllocationAtSpecificPoint() {
        LayoutAllocator.init(20, 300, 300);
        LayoutAllocator.LayoutAllocation la = LayoutAllocator.allocate(new Point(100, 100), 100, 100);

        Assert.assertNotNull("LayoutAllocation must be valid", la);
        Assert.assertEquals("LayoutAllocation point must be correct", new Point(100,100),
                                                                      la.point);
    }

    @Test
    public void allocationRequestAtInvalidPointFails() {
        LayoutAllocator.init(20, 300, 300);
        LayoutAllocator.LayoutAllocation la = LayoutAllocator.allocate(new Point(400, 200), 100, 100);

        Assert.assertNull("An allocation request at an invalid point must fail", la);
    }

    @Test
    public void allocationMoveRequestDoesNothingOnInsufficientMove() {
        LayoutAllocator.init(20, 300, 300);
        LayoutAllocator.LayoutAllocation la = LayoutAllocator.allocate(new Point(0, 0), 100, 100);
        LayoutAllocator.LayoutAllocation newLa = LayoutAllocator.moveAllocation(la, new Point(10, 0));

        Assert.assertTrue("A move request which would end up in the same region as before"
                + " does nothing.",
                          la == newLa);
    }

    @Test
    public void allocationMoveRequestSucceeds() {
        LayoutAllocator.init(20, 300, 300);
        LayoutAllocator.LayoutAllocation la = LayoutAllocator.allocate(100, 100);

        LayoutAllocator.LayoutAllocation newLa = LayoutAllocator.moveAllocation(la, new Point(160, 160));

        Assert.assertNotNull("New layout allocation must not be null", newLa);
        Assert.assertEquals("New layout allocation should be at the requested point", new Point(160, 160),
                                                                                      newLa.point);

        LayoutAllocator.LayoutAllocation nextLa = LayoutAllocator.allocate(100, 100);
        Assert.assertNotNull("Next allocation request must succeed", nextLa);
        Assert.assertEquals("Next allocation should be at Point[0,0]", new Point(0, 0),
                                                                       nextLa.point);
    }

    @Test
    public void allocationMoveRequestFallsBackWhenAllocationNotPossible() {
        LayoutAllocator.init(20, 300, 300);
        LayoutAllocator.LayoutAllocation la = LayoutAllocator.allocate(100, 100);
        Point oldPoint = la.point;

        LayoutAllocator.LayoutAllocation newLa = LayoutAllocator.moveAllocation(la, new Point(100, 400));

        Assert.assertNotNull("New layout allocation must not be null", newLa);
        Assert.assertEquals("New layout allocation should fall back to the old point", oldPoint,
                                                                                       newLa.point);
    }

    @Test
    public void allocationMoveRequestFallsBackWhenCollisionWouldOccur() {
        LayoutAllocator.init(20, 300, 300);
        LayoutAllocator.LayoutAllocation la1 = LayoutAllocator.allocate(200, 100);
        LayoutAllocator.LayoutAllocation la2 = LayoutAllocator.allocate(200, 100);

        Assert.assertEquals("First allocation should be at the expected location",
                            new Point(0, 0),
                            la1.point);
        Assert.assertEquals("Second allocation should be at the expected location",
                            new Point(0, 100),
                            la2.point);

        Point oldLa1Point = la1.point;
        LayoutAllocator.LayoutAllocation newLa = LayoutAllocator.moveAllocation(la1, new Point(0, 1));

        Assert.assertNotNull("New layout allocation must not be null", newLa);
        Assert.assertEquals("New layout allocation should fall back to the old point",
                            oldLa1Point,
                            newLa.point);
    }

    @Test
    public void horizontalExpandShouldWorkProperly() {
        LayoutAllocator.init(20, 100, 100);
        Point requestedPoint = new Point(120, 0);
        int requestedWidth = 10;
        int requestedHeight = 10;

        Assert.assertNull("Initial layout request must fail",
                          LayoutAllocator.allocate(requestedPoint,
                                                 requestedWidth,
                                                 requestedHeight));

        Assert.assertEquals("Resize should meet requested size", 
                            200,
                            LayoutAllocator.restrictedWidthResize(200));
        LayoutAllocator.LayoutAllocation la = LayoutAllocator.allocate(requestedPoint,
                                                                   requestedWidth,
                                                                   requestedHeight);
        Assert.assertNotNull("Allocation attempt after resize must succeed", la);
        Assert.assertEquals("Allocation must be at the requested point",
                            requestedPoint,
                            la.point);
    }

    @Test
    public void horizontalShrinkShouldWorkProperly() {
        LayoutAllocator.init(20, 100, 100);
        LayoutAllocator.LayoutAllocation la = LayoutAllocator.allocate(60, 60);

        Assert.assertEquals("Result absolute size should be restricted properly",
                            60,
                            LayoutAllocator.restrictedWidthResize(20));
        Assert.assertNull("Allocation beyond new size must fail",
                          LayoutAllocator.allocate(new Point(60, 0), 20, 20));
    }

    @Test
    public void verticalExpandShouldWorkProperly() {
        LayoutAllocator.init(20, 100, 100);
        Point requestedPoint = new Point(0, 120);
        int requestedWidth = 10;
        int requestedHeight = 10;

        Assert.assertNull("Initial layout request must fail",
                          LayoutAllocator.allocate(requestedPoint,
                                                 requestedWidth,
                                                 requestedHeight));

        LayoutAllocator.restrictedHeightResize(200);
        LayoutAllocator.LayoutAllocation la = LayoutAllocator.allocate(requestedPoint,
                                                                   requestedWidth,
                                                                   requestedHeight);
        Assert.assertNotNull("Allocation attempt after resize must succeed", la);
        Assert.assertEquals("Allocation must be at the requested point",
                            requestedPoint,
                            la.point);
    }

    @Test
    public void verticalShrinkShouldWorkProperly() {
        LayoutAllocator.init(20, 100, 100);
        LayoutAllocator.LayoutAllocation la = LayoutAllocator.allocate(60, 60);

        Assert.assertEquals("Result absolute size should be restricted properly",
                            60,
                            LayoutAllocator.restrictedHeightResize(20));
        Assert.assertNull("Allocation beyond new size must fail",
                          LayoutAllocator.allocate(new Point(0, 60), 20, 20));
    }
}
