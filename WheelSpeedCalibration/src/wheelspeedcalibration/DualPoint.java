/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

/**
 * Wrapper class to hold two points for drawing lines, the line's y intercept, and the line's slope
 * @author Kyle
 */
public class DualPoint
{

    LinePoint point1 = new LinePoint();
    LinePoint point2 = new LinePoint();
    double slope;
    double yint;
}
