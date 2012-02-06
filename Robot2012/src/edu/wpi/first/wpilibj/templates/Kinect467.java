/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Kinect;
import edu.wpi.first.wpilibj.KinectStick;
import edu.wpi.first.wpilibj.Skeleton;
import edu.wpi.first.wpilibj.Skeleton.Joint;

/**
 *
 * @author aidan
 */
//public class Kinect467
//{
//    //Single instance
//    private static Kinect467 instance;
//    
//    //Objects
//    private Kinect kinect;
//    private Skeleton skeleton;
//    private Joint[] joints;
//    private Joint[] lastJoints;
//    
//    //Constants corresponding to joint ids in the array
//    public static final int LEFT_HAND = 0;
//    public static final int LEFT_ELBOW = 1;
//    public static final int RIGHT_HAND = 2;
//    public static final int RIGHT_ELBOW = 3;
//    
//    
//    public static final int X = 0;
//    public static final int Y = 1;
//    public static final int Z = 2;
//    
//    //Constants for defining movement direction
//    public static final int DIR_NONE = 0;
//    public static final int DIR_UP = 1;
//    public static final int DIR_DOWN = 2;
//    public static final int DIR_LEFT = 3;
//    public static final int DIR_RIGHT = 4;
//    
//    //Dead zone for direction (in pixels of the image)
//    private static final int DEAD_ZONE = 1;
//    
//    //Direction variables
//    private int leftVerticalDirection = 0;
//    private int leftHorizontalDirection = 0;
//    private int rightVerticalDirection = 0;
//    private int rightHorizontalDirection = 0;
//            
//    
//    /**
//     * Get the single instance of this class
//     */
//    public static Kinect467 getInstance()
//    {
//     
//        if (instance == null)
//        {
//            instance = new Kinect467();
//        }
//        return instance;
//    }
//    
//    //Private constructor for singleton
//    private Kinect467()
//    {
//        kinect = Kinect.getInstance();
//        skeleton = kinect.getSkeleton();
//        joints = new Joint[10];
//    }
//    
//    /**
//     * Periodic update function which reads joint objects into an array
//     */
//    public void update(){
//        
//        //Moves previous joints to array lastJoints
//        lastJoints = joints;
//        joints = new Joint[4];
//        
//        //Updates joints in array joints
//        joints[LEFT_HAND] = skeleton.GetHandLeft();
//        joints[LEFT_ELBOW] = skeleton.GetElbowLeft();
//        joints[RIGHT_HAND] = skeleton.GetHandRight();
//        joints[RIGHT_ELBOW] = skeleton.GetElbowRight();   
//        
//        //Determine difference between current and previous x and y positions  
//        //for the left hand
//        int yDif = 0;
//        int xDif = 0;
//        
//        if (joints[LEFT_HAND] != null)
//        {
//            yDif = (int) (joints[LEFT_HAND].getY() - lastJoints[LEFT_HAND].getY());
//            xDif = (int) (joints[LEFT_HAND].getX() - lastJoints[LEFT_HAND].getX());
//        }
//        
//        //Determine vertical direction
//        if (Math.abs(yDif) < DEAD_ZONE)
//        {
//            leftVerticalDirection = DIR_NONE;
//        }
//        else
//        {
//            if (yDif > 0)
//            {
//                leftVerticalDirection = DIR_DOWN;
//            }
//            else
//            {
//                leftVerticalDirection = DIR_UP;
//            }
//        }
//        
//        //Determine horizontal direction
//        if (Math.abs(xDif) < DEAD_ZONE)
//        {
//            leftHorizontalDirection = DIR_NONE;
//        }
//        else
//        {
//            if (xDif > 0)
//            {
//                leftHorizontalDirection = DIR_RIGHT;
//            }
//            else
//            {
//                leftHorizontalDirection = DIR_LEFT;
//            }
//        }
//        
//        //Determine difference between current and previous x and y positions  
//        //for the right hand
//        if (joints[RIGHT_HAND] != null)
//        {
//            yDif = (int) (joints[RIGHT_HAND].getY() - lastJoints[RIGHT_HAND].getY());
//            xDif = (int) (joints[RIGHT_HAND].getX() - lastJoints[RIGHT_HAND].getX());
//        }
//        
//        //Determine vertical direction
//        if (Math.abs(yDif) < DEAD_ZONE)
//        {
//            rightVerticalDirection = DIR_NONE;
//        }
//        else
//        {
//            if (yDif > 0)
//            {
//                rightVerticalDirection = DIR_DOWN;
//            }
//            else
//            {
//                rightVerticalDirection = DIR_UP;
//            }
//        }
//        
//        //Determine horizontal direction
//        if (Math.abs(xDif) < DEAD_ZONE)
//        {
//            rightHorizontalDirection = DIR_NONE;
//        }
//        else
//        {
//            if (xDif > 0)
//            {
//                rightHorizontalDirection = DIR_RIGHT;
//            }
//            else
//            {
//                rightHorizontalDirection = DIR_LEFT;
//            }
//        }
//        
//        
//    }
//    
//   /**
//    * Get the specified joint object
//    * @param j The id of the joint in the joint array
//    * @return 
//    */
//    public Joint getJoint(int j) 
//    {
//        return joints[j];      
//    }
//    
//}
public class Kinect467
{
    //Single instance
    private static Kinect467 instance;
    
    //Objects
    private KinectStick rightArm;
    private KinectStick leftArm;
    private static final GenericHID.Hand leftHand = GenericHID.Hand.kLeft;
    private static final GenericHID.Hand rightHand = GenericHID.Hand.kRight;
      
    //object definitions
    public static final int LEFT_HAND = 0;
    public static final int RIGHT_HAND = 1;
        
    //Constants defining the arm
    public static final int LEFT_ARM = 1;
    public static final int RIGHT_ARM = 2;
    
    //Positions constants
    public static final int MIDDLE = 0;
    public static final int UP = 1;
    public static final int DOWN = 2;
    
    //Dead zone for coordinates
    private static final double DEAD_ZONE = .01;
    
    //Hand position angle
    public static double yHandRight = 0.0;
    public static double yHandLeft = 0.0;
    
    public static int posHandRight = 0;
    public static int posHandLeft = 0;
   
    
    

    

    
    /**
     * Get the single instance of this class
     */
    public static Kinect467 getInstance()
    {
     
        if (instance == null)
        {
            instance = new Kinect467();
        }
        return instance;
    }
    
    //Private constructor for singleton
    private Kinect467()
    {
        //Kinect stick objects
        
        leftArm = new KinectStick(LEFT_ARM);
        rightArm = new KinectStick(RIGHT_ARM);  
    }
    
    /**
     * Periodic updateKinect function which reads joint objects into an array
     */
    public void updateKinect()
    {        
        
        //Gets the coordinates of the hands
        yHandLeft = leftArm.getY(leftHand);
        yHandRight = rightArm.getY(rightHand);
               
        //Prints out the coordinates of the hands
//        System.out.println("Left Hand: " + yHandLeft + " Right Hand: " + yHandRight);
        
       
        posHandLeft = getPosition(leftArm, leftHand); 
        posHandRight = getPosition(rightArm, rightHand);
        System.out.println(getGesture());
                
        
          //print hand position relative hand position
//        System.out.println("Left Hand: " + posHandLeft + " Right Hand: " + posHandRight);
        
        //Reset the coordinates
        yHandRight = 0.0;
        yHandLeft = 0.0; 
                
    }
    
    /**
     * get hand position relative to the shoulder
     */
    private int getPosition(KinectStick arm, GenericHID.Hand hand)
    {
        double position = arm.getY(hand);
        int vertPosition;
        if (position == 0 + DEAD_ZONE || position == 0 - DEAD_ZONE )
        {
            vertPosition = MIDDLE;
        }
        else if (position <= 0 - DEAD_ZONE)
        {
            vertPosition = UP;
        }
        else if (position >= 0 + DEAD_ZONE)
        {
            vertPosition = DOWN;
        }
        else
        {
            vertPosition = MIDDLE;
        }
        return vertPosition;
    
    }
    private String getGesture()
    {
        String text = "";
        if (posHandLeft == DOWN && posHandRight == UP)
        {
            text = "left";
        }
        if (posHandLeft == UP && posHandRight == DOWN)
        {
            text = "right";
        }
        if (posHandLeft == DOWN && posHandRight == DOWN)
        {
            text = "back";
        }
        if (posHandLeft == UP && posHandRight == UP)
        {
            text = "forward";
        }
        if (posHandLeft == MIDDLE && posHandRight == MIDDLE)
        {
            text = "stop";
        }
            
        return text;
    }
}
