package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author Spencer
 */
public class SpeedCalibration {
    private static GearToothSensor[] sensors = new GearToothSensor[4];
    private static Drive drive = Drive.getInstance();
    private static DataStorage data = DataStorage.getInstance();
    
    private static double[][] deadzones = new double[2][4];
    private static boolean[][] deadzoneMotorsFinished = new boolean[2][4];
    private static int finishedMotors = 0;
    private static double deadzoneSpeed = 0;
    
    private static final int LOOKUP_TABLE_STEPS = 256;
    private static final double LOOKUP_INC = 2 / LOOKUP_TABLE_STEPS;
    private static double lookupSpeed = 0.0;
    private static boolean calibrationDirection = true;
    private static boolean calibrationFinished = false;
    private static int lookupDelay = 0;
    private static double[][] lookupTable = new double[4][LOOKUP_TABLE_STEPS + 1];
    
    public static void init() {
        sensors[RobotMap.FRONT_LEFT] = new GearToothSensor(RobotMap.FRONT_LEFT_GEAR_TOOTH_SENSOR_CHANNEL, RobotMap.WHEEL_CIRCUMFRENCE, 60);
        sensors[RobotMap.FRONT_RIGHT] = new GearToothSensor(RobotMap.FRONT_RIGHT_GEAR_TOOTH_SENSOR_CHANNEL, RobotMap.WHEEL_CIRCUMFRENCE, 60);
        sensors[RobotMap.BACK_LEFT] = new GearToothSensor(RobotMap.BACK_LEFT_GEAR_TOOTH_SENSOR_CHANNEL, RobotMap.WHEEL_CIRCUMFRENCE, 60);
        sensors[RobotMap.BACK_RIGHT] = new GearToothSensor(RobotMap.BACK_RIGHT_GEAR_TOOTH_SENSOR_CHANNEL, RobotMap.WHEEL_CIRCUMFRENCE, 60);
    }
    
    public static void resetCalibration() {
        
    }
    
    public static void update() {
        sensors[RobotMap.FRONT_LEFT].update();
        sensors[RobotMap.FRONT_RIGHT].update();
        sensors[RobotMap.BACK_LEFT].update();
        sensors[RobotMap.BACK_RIGHT].update();
    }
    
    public static void printAll() {
        GearToothSensor fl = sensors[RobotMap.FRONT_LEFT];
        GearToothSensor fr = sensors[RobotMap.FRONT_RIGHT];
        GearToothSensor bl = sensors[RobotMap.BACK_LEFT];
        GearToothSensor br = sensors[RobotMap.BACK_RIGHT];
        
        System.out.println("[GEAR TOOTH CALIBRATION] "
                + fl.toString() + "; " 
                + fr.toString() + "; "
                + bl.toString() + "; "
                + br.toString());
    }
    
    public static GearToothSensor getSensor(int id) {
        return sensors[id];
    }
    
    /**
     * Gradually ramps up power fed to all drive motors to find their deadzones.
     * This behavior is automatic.
     * 
     * @return true if all motors are finished, false if otherwise.
     */
    public static boolean findMotorDeadzones() {
        int direction = (finishedMotors >= 4) ? RobotMap.REVERSE_DEADZONES : RobotMap.FORWARD_DEADZONES;
        
        deadzoneSpeed = deadzoneSpeed + ((direction == RobotMap.FORWARD_DEADZONES) ? 0.01 : -0.01);
        
        drive.crabDrive(0.0, deadzoneSpeed, false);
        
        for(int i = 0; i < 4; i++) {
            if (sensors[i].getRawRPM() > 0 && !deadzoneMotorsFinished[direction][i]) {
                deadzoneMotorsFinished[direction][i] = true;
                deadzones[direction][i] = deadzoneSpeed;
                
                finishedMotors++;
                
                if (finishedMotors == 4) {
                    deadzoneSpeed = 0;
                }
            }
        }
        
        // if all motors are finished, return true.
        return (finishedMotors == 8);
    }
    
    /**
     * Writes all deadzones to memory.
     */
    public static void writeDeadZones() {
        for(int i = 0; i < 4; i++) {
            data.putDouble(RobotMap.CALIBRATION_MOTOR_DEADZONES[RobotMap.FORWARD_DEADZONES][i], deadzones[RobotMap.FORWARD_DEADZONES][i]);
        }
        
        for(int i = 0; i < 4; i++) {
            data.putDouble(RobotMap.CALIBRATION_MOTOR_DEADZONES[RobotMap.REVERSE_DEADZONES][i], deadzones[RobotMap.REVERSE_DEADZONES][i]);
        }
    }
    
    public static boolean buildLookupTable() {
        if (!calibrationFinished) {
            lookupSpeed = lookupSpeed + ((calibrationDirection) ? LOOKUP_INC : -LOOKUP_INC);

            drive.crabDrive(0.0, lookupSpeed, false);

            int currentInc = (int) ((LOOKUP_TABLE_STEPS / 2) * (lookupSpeed - 1));

            for (int i = 0; i < 4; i++) {
                lookupTable[i][currentInc] = sensors[i].getAccurateRPM() * (lookupSpeed / Math.abs(lookupSpeed));
            }

            if (lookupSpeed >= 1.0) {
                calibrationDirection = false;
            }

            if (lookupSpeed <= -1.0) {
                calibrationFinished = true;
            }
        }
        
        return calibrationFinished;
    }
    
    public static void writeLookupTable() {
        for (int i = 0; i < 4; i++) {
            data.putDoubleArray(RobotMap.LOOKUP_TABLES[i], lookupTable[i]);
        }
    }
    
    public static double[][] makeInverseTable() {
        double[][] inverse = new double[4][LOOKUP_TABLE_STEPS + 1];
        
        for (int i = 0; i < 4; i++) {
            double slope = lookupTable[i][0];
        }
        
        return null;
    }
}
