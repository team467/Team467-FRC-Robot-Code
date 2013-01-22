/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

/**
 *
 * @author Kyle
 */
public class ComputePoints
{
    public static DualPoint computePoint(double a, double b, int sign)
    {
        //Given a = y-intercept and b = slope, computes two
        //end points that can be used to plot this line
        //y = a + b * x   

        double minX = 0.0;
        double maxX = 8.0;
        DualPoint dualPoint = new DualPoint();
        dualPoint.point1.x = sign * maxX;
        dualPoint.point1.y = computeY((sign * maxX), a, b);

        dualPoint.point2.x = sign * minX;
        dualPoint.point2.y = computeY((sign * minX), a, b);
        return dualPoint;
    }

    private static double computeY(double x, double a, double b)
    {
        //y = a + b * x
        double y = a + (b * x);
        return y;
    }
}
