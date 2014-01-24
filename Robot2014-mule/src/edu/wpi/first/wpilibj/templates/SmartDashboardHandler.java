/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.NamedSendable;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 *
 * @author aidan
 */
public class SmartDashboardHandler
{
    //Subsytems to use with smartdashboard
    private static Drive drive;
    private static Driverstation driverstation;
    
    private static double setSteeringAngle = 0.0;
    private static Gyro467 gyro;
    /**
     * Initializes the smart dashboard
     */
    public static void init()
    {
        //Get all subsystems
        drive = Drive.getInstance();
        driverstation = Driverstation.getInstance();
        gyro = Gyro467.getInstance();
//        //Add motors to live window
//        LiveWindow.addActuator("Steering", "Front Left", 
//                new LiveWindowSteeringControl(drive.getSteering(RobotMap.FRONT_LEFT)));
//        LiveWindow.addActuator("Steering", "Front Right", 
//                new LiveWindowSteeringControl(drive.getSteering(RobotMap.FRONT_RIGHT)));
//        LiveWindow.addActuator("Steering", "Back Left", 
//                new LiveWindowSteeringControl(drive.getSteering(RobotMap.BACK_LEFT)));
//        LiveWindow.addActuator("Steering", "Back Right", 
//                new LiveWindowSteeringControl(drive.getSteering(RobotMap.BACK_RIGHT)));
        SmartDashboard.putData("Front Left", new DashBoardSteeringButton("Front Left"));
        
    }
    
    /**
     * Update the smartdashboard
     */
    public static void update()
    {
        Scheduler.getInstance().run();
    }
    
    public static class DashBoardSteeringButton extends Command
    {
        public DashBoardSteeringButton(String name)
        {
            super(name);
        }
        
        protected void initialize()
        {
        }

        protected void execute()
        {
            drive.crabDrive(setSteeringAngle, 0.0, false);
            SmartDashboard.putNumber("Steering Actual", drive.getNormalizedSteeringAngle(RobotMap.FRONT_LEFT));
            SmartDashboard.putNumber("Steering Set", setSteeringAngle);
            
            if (driverstation.JoystickRightButton4)
            {
                setSteeringAngle += 0.01;
                if (setSteeringAngle > 1.0)
                {
                    setSteeringAngle -= 2.0;
                }
            }
        }

        protected boolean isFinished()
        {
            return false;
        }

        protected void end()
        {
        }

        protected void interrupted()
        {
        }

    }
    
    /**
     * Live window sendable object used to set steering angles of steering objects.
     * Should be handled as a live window actuator.
     */
    private static class LiveWindowSteeringControl implements LiveWindowSendable
    {

        //Steering object to control
        Steering steering;

        /**
         * Creates a new live window steering control for a given steering
         * object
         *
         * @param st The steering object
         */
        public LiveWindowSteeringControl(Steering st)
        {
            steering = st;
        }
        private ITable m_table;
        private ITableListener m_table_listener;

        public void updateTable()
        {
            if (m_table != null)
            {
                m_table.putNumber("Value", steering.getSteeringAngle());
            }
        }

        public void startLiveWindowMode()
        {
            steering.setAngle(0.0);
            m_table_listener = new ITableListener()
            {
                public void valueChanged(ITable itable, String key, Object value, boolean bln)
                {
                    System.out.println(key + ": " + value);
                    steering.setAngle(((Double) value).doubleValue());
                    setSteeringAngle = ((Double) value).doubleValue();
                }
            };
            m_table.addTableListener("Value", m_table_listener, true);
        }

        public void stopLiveWindowMode()
        {
            steering.setAngle(0.0); // Stop for safety
            // TODO: Broken, should only remove the listener from "Value" only.
            m_table.removeTableListener(m_table_listener);
        }

        public void initTable(ITable subtable)
        {
            m_table = subtable;
            updateTable();
        }

        public ITable getTable()
        {
            return m_table;
        }

        public String getSmartDashboardType()
        {
            return "Steering Controller";
        }
    }
}
