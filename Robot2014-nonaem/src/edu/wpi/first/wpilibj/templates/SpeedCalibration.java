package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author Spencer
 */
public class SpeedCalibration {
    private static GearToothSensor[] sensors = new GearToothSensor[4];
    private static Drive drive = Drive.getInstance();
    private static DataStorage data = DataStorage.getInstance();
    
    private static final int LOOKUP_TABLE_STEPS = 128;
    private static final double LOOKUP_INC = 1 / LOOKUP_TABLE_STEPS;
    private static double lookupSpeed = 0.0;
    private static boolean calibrationFinished = false;
    private static int lookupDelay = 0;
    private static Point[][] lookupTableForward = new Point[4][LOOKUP_TABLE_STEPS + 1];
    private static Point[][] lookupTableReverse = new Point[4][LOOKUP_TABLE_STEPS + 1];
    
    public static class Point {
        public double x;
        public double y;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public static void init() {
        sensors[RobotMap.FRONT_LEFT] = new GearToothSensor(RobotMap.FRONT_LEFT_GEAR_TOOTH_SENSOR_CHANNEL, RobotMap.WHEEL_CIRCUMFRENCE, 60);
        sensors[RobotMap.FRONT_RIGHT] = new GearToothSensor(RobotMap.FRONT_RIGHT_GEAR_TOOTH_SENSOR_CHANNEL, RobotMap.WHEEL_CIRCUMFRENCE, 60);
        sensors[RobotMap.BACK_LEFT] = new GearToothSensor(RobotMap.BACK_LEFT_GEAR_TOOTH_SENSOR_CHANNEL, RobotMap.WHEEL_CIRCUMFRENCE, 60);
        sensors[RobotMap.BACK_RIGHT] = new GearToothSensor(RobotMap.BACK_RIGHT_GEAR_TOOTH_SENSOR_CHANNEL, RobotMap.WHEEL_CIRCUMFRENCE, 60);
    }
    
    public static void resetCalibration() {
        calibrationFinished = false;
        lookupDelay = 0;
        lookupSpeed = 0.0;
    }
    
    public static void update() {
        sensors[RobotMap.FRONT_LEFT].update();
        sensors[RobotMap.FRONT_RIGHT].update();
        sensors[RobotMap.BACK_LEFT].update();
        sensors[RobotMap.BACK_RIGHT].update();
    }
    
    public static void start() {
        sensors[RobotMap.FRONT_LEFT].start();
        sensors[RobotMap.FRONT_RIGHT].start();
        sensors[RobotMap.BACK_LEFT].start();
        sensors[RobotMap.BACK_RIGHT].start();
    }
    
    public static void printAll() {
        GearToothSensor fl = sensors[RobotMap.FRONT_LEFT];
        GearToothSensor fr = sensors[RobotMap.FRONT_RIGHT];
        GearToothSensor bl = sensors[RobotMap.BACK_LEFT];
        GearToothSensor br = sensors[RobotMap.BACK_RIGHT];
        
//        System.out.println("[GEAR TOOTH CALIBRATION] "
//                + fl.toString() + "; " 
//                + fr.toString() + "; "
//                + bl.toString() + "; "
//                + br.toString());
    }
    
    public static GearToothSensor getSensor(int id) {
        return sensors[id];
    }
    
    public static boolean buildLookupTable(int direction) {
        if (!calibrationFinished && lookupDelay == 20) {
            lookupSpeed = lookupSpeed + ((direction == RobotMap.FORWARD) ? LOOKUP_INC : -LOOKUP_INC);

            drive.crabDrive(0.0, lookupSpeed, false);

            int currentInc = (int) ((LOOKUP_TABLE_STEPS) * (Math.abs(lookupSpeed)));

            for (int i = 0; i < 4; i++) {
                if (direction == RobotMap.FORWARD) {
                    lookupTableForward[i][currentInc] = new Point(sensors[i].getAccurateRPM(), lookupSpeed);
                } else if (direction == RobotMap.REVERSE) {
                    lookupTableReverse[i][currentInc] = new Point(sensors[i].getAccurateRPM(), lookupSpeed);
                }
            }

            if (Math.abs(lookupSpeed) >= 1.0) {
                calibrationFinished = true;
            } 
            
            lookupDelay = 0;
        }
        
        lookupDelay++;
        
        return calibrationFinished;
    }
    
    public static void writeLookupTables() {        
        for (int i = 0; i < 4; i++) {
            double[] forwardPower = new double[LOOKUP_TABLE_STEPS];
            double[] forwardRPM = new double[LOOKUP_TABLE_STEPS];
            double[] reversePower = new double[LOOKUP_TABLE_STEPS];
            double[] reverseRPM = new double[LOOKUP_TABLE_STEPS];
        
            for (int q = 0; q < LOOKUP_TABLE_STEPS + 1; q++) {
                Point forward = lookupTableForward[i][q];
                Point reverse = lookupTableReverse[i][q];
                
                forwardPower[q] = forward.x;
                forwardRPM[q] = forward.y;
                reversePower[q] = reverse.x;
                reverseRPM[q] = reverse.y;
            }
            
            FileWriter.openWriteFile("/" + RobotMap.LOOKUP_TABLES[i] + "ForwardPower.lut");
            FileWriter.writeDoubleArray(forwardPower);
            FileWriter.closeFile();
            
            FileWriter.openWriteFile("/" + RobotMap.LOOKUP_TABLES[i] + "ForwardRPM.lut");
            FileWriter.writeDoubleArray(forwardRPM);
            FileWriter.closeFile();
            
            FileWriter.openWriteFile("/" + RobotMap.LOOKUP_TABLES[i] + "ReversePower.lut");
            FileWriter.writeDoubleArray(reversePower);
            FileWriter.closeFile();
            
            FileWriter.openWriteFile("/" + RobotMap.LOOKUP_TABLES[i] + "ReverseRPM.lut");
            FileWriter.writeDoubleArray(reverseRPM);
            FileWriter.closeFile();
            
            FileWriter.openWriteFile("/" + RobotMap.LOOKUP_TABLES[i] + "ForwardRPMHumanReadable.txt");
            FileWriter.writeDoubleArrayAsString(forwardRPM);
            FileWriter.closeFile();
            
            FileWriter.openWriteFile("/" + RobotMap.LOOKUP_TABLES[i] + "ReverseRPMHumanReadable.txt");
            FileWriter.writeDoubleArrayAsString(reverseRPM);
            FileWriter.closeFile();
        }
    }
    
    public static Point[][] readForwardLookupTables() {
        Point[][] table = new Point[4][LOOKUP_TABLE_STEPS + 1];
        
        for(int i = 0; i < 4; i++) {
            FileWriter.openReadFile("/" + RobotMap.LOOKUP_TABLES[i] + "ForwardPower.lut");
            double[] power = FileWriter.readDoubleArray(LOOKUP_TABLE_STEPS + 1);
            FileWriter.closeFile();
            
            FileWriter.openReadFile("/" + RobotMap.LOOKUP_TABLES[i] + "ForwardRPM.lut");
            double[] rpm = FileWriter.readDoubleArray(LOOKUP_TABLE_STEPS + 1);
            FileWriter.closeFile();
            
            for (int q = 0; q < LOOKUP_TABLE_STEPS + 1; q++) {
                table[i][q] = new Point(power[q], rpm[q]);
            }
        }
        
        return table;
    }
    
    /**
     * Takes in ArrayList of forward (POS Values) or backward (NEG Values), and
     * computes lines, slope, y intercept, and two points to draw lines in Swing
     *
     * @param arrayList takes in ArrayList and returns DualPoint for the
     *                  LeastSquaredRegression
     * @param sign      Use the ints in WheelSpeedCalibrationMap called FORWARD
     *                  and BACKWARD, FORWARD is a positive value, BACKWARD is a
     *                  negative value. This value is used to get a point with a
     *                  negative X value for backwards, and a positive X value
     *                  for forwards
     * @return DualPoint w/ the two points for drawing lines in the Frame
     */
    public static Point LeastSquaredRegression(Point[] points, int sign)
    {
        double sumX = 0;
        double sumY = 0;
        double sumXX = 0;
        double sumYY = 0;
        double sumXY = 0;
        double meanX = 0;
        double meanY = 0;
        double b = 0;
        double a = 0;
        double r = 0;
        int numData = 0;
        double x;
        double y;

        for (int i = 0; i < points.length; i++)
        {
                x = points[i].x;
                y = points[i].y;
                sumX += x;
                sumY += y;
                sumXX += (x * x);
                sumYY += (y * y);
                sumXY += (x * y);
                numData++;
        }
        double N = (double) numData;
        meanX = sumX / N;
        meanY = sumY / N;

        //Equation: y = a + b * x
        //b = slope
        b = ((N * sumXY) - (sumX * sumY)) / ((N * sumXX) - (sumX * sumX));
        //System.out.println("Slope: " + b);
        // y = a + b * x, so a = y - b * x
        //a = yint
        a = ((sumY * sumXX) - (sumX * sumXY)) / ((N * sumXX) - (sumX * sumX));
        //System.out.println("Y Int: " + a);
        //r term
        r = Math.sqrt((sumXY * sumXY) / (sumXX * sumYY));

        Point p = new Point(a, b * sign);

        return p;
    }
}
