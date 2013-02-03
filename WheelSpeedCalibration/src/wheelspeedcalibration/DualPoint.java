/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

/**
 * Wrapper class to hold lines 2 pts and the line's y int + slope
 * @author Kyle
 */
public class DualPoint
{

    LinePoint point1 = new LinePoint();
    LinePoint point2 = new LinePoint();
    double slope;
    double yint;
}
