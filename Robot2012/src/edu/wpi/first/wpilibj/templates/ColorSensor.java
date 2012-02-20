/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.parsing.ISensor;

/**
 * HiTechnic NXT Compass.
 *
 * This class alows access to a HiTechnic NXT Compass on an I2C bus.
 * These sensors to not allow changing addresses so you cannot have more
 *   than one on a single bus.
 *
 * Details on the sensor can be found here:
 *   http://www.hitechnic.com/index.html?lang=en-us&target=d17.html
 *
 */
public class ColorSensor extends SensorBase implements ISensor{

    /**
     * An exception dealing with connecting to and communicating with the
     * HiTechnicCompass
     */
    public class ColorSensorException extends RuntimeException {

        /**
         * Create a new exception with the given message
         * @param message the message to pass with the exception
         */
        public ColorSensorException(String message) {
            super(message);
        }
       
    }

    //private static final byte kAddress = 0x02;
    private static final byte kManufacturerBaseRegister = 0x08;
    private static final byte kManufacturerSize = 0x08;
    private static final byte kSensorTypeBaseRegister = 0x10;
    private static final byte kSensorTypeSize = 0x08;
    //private static final byte kHeadingRegister = 0x44;
    private I2C m_i2c;

    /**
     * Constructor.
     *
     * @param slot The slot of the digital module that the sensor is plugged into.
     */
    public ColorSensor(int slot) 
    {
        DigitalModule module = DigitalModule.getInstance(slot);
        //m_i2c = module.getI2C(kAddress);

        // Verify Sensor
        //final byte[] kExpectedManufacturer = "HiTechnc".getBytes();
        //final byte[] kExpectedSensorType = "Compass ".getBytes();
        //if (!m_i2c.verifySensor(kManufacturerBaseRegister, kManufacturerSize, kExpectedManufacturer)) {
        //    throw new ColorSensorException("Invalid Compass Manufacturer");
        // }
        //if (!m_i2c.verifySensor(kSensorTypeBaseRegister, kSensorTypeSize, kExpectedSensorType)) {
        //    throw new ColorSensorException("Invalid Sensor type");
        // }
    }

    public void findSensor(int slot)
    {
        DigitalModule module = DigitalModule.getInstance(slot);
        
        for (int i = 0; i < 256; i++)
        {
            try
            {
                m_i2c = module.getI2C(i);
                byte[] data = new byte[kManufacturerSize];
                m_i2c.read(kManufacturerBaseRegister, kManufacturerSize, data);
                System.out.print("I: " + i);
                System.out.println("Data: " + data);
            }
            catch(RuntimeException ex)
            {
                 
            }
            finally
            {
                m_i2c.free();
                m_i2c = null;
            }
        }
    }
    
    /**
     * Destructor.
     */
    public void free() 
    {
        if (m_i2c != null) 
        {
            m_i2c.free();
        }
        m_i2c = null;
    }

    /**
     * Get the compass angle in degrees.
     *
     * The resolution of this reading is 1 degree.
     *
     * @return Angle of the compass in degrees.
     */
//    public double getAngle() {
//        byte[] heading = new byte[2];
//        m_i2c.read(kHeadingRegister, (byte) heading.length, heading);
//
//        return ((int) heading[0] + (int) heading[1] * (int) (1 << 8));
//    }
}
