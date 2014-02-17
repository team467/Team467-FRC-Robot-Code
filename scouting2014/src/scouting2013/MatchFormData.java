/*
 * Data object which stores all data from a match form in a single object
 */
package scouting2013;

import java.io.Serializable;

/**
 *
 * @author aidan
 */
public class MatchFormData implements Serializable
{
    /*
     * ========================================================================
     * Data constants
     * ========================================================================
     */
    
    //Active
    public static final int YES = 0;
    public static final int NO = 1;
    public static final int PARTIAL = 2;
    
    //Color
    public static final int RED = 0;
    public static final int BLUE = 1;
    
    //Quality
    public static final int POOR = 1;
    public static final int AVERAGE = 2;
    public static final int SUPERB = 3;
    public static final int NA = 0;
    
    //Outcome specific
    public static final int WIN = 0;
    public static final int LOSE = 1;
    public static final int TIE = 2;
    
     /*
     * ========================================================================
     * Data constant strings 
     * ========================================================================
     */
    
    //Active
    public static final String[] activeDataStrings = new String[]
    {
        "Yes", "No", "Partial"
    };
    
    //Quality
    public static final String[] qualityDataStrings = new String[]
    {
        "Poor", "Average", "Superb", "Not working"
    };
 
    //Outcome
    public static final String[] outcomeDataStrings = new String[]
    {
        "Win", "Lose", "Tie"
    };
    
    //Colors
    public static final String[] colorDataStrings = new String[]
    {
        "Red", "Blue"
    };
    

    /*
     * ========================================================================
     * Stored data variables
     * ========================================================================
     */
    
    //Indentifiers
    public int teamNumber = 0;
    public int matchNumber = 0;
    public String scouterName = "";
    public int teamColor = 0;
    
    //Robot activity
    public int robotActive = NO;
    public String comments = "";
    
    //Autonomous
    public String[] autonomousActions = new String[3];
    public int autonomousScore = 0;
    
    //Drivetrain
    public int maneuverability;
    
    //Robot Abilities
    public boolean hasCatchMechanism = false;
    public boolean hasPickupMechanism = false;
    public boolean hasPassMechanism = false;
    public boolean hasShootMechanism = false;
    public boolean hasDefenseMechanism = false;
    
    //Catch
    public int catchMechanism = NA;
    public boolean canCatchOverTruss = false;
    public int catchesCompletedCount = 0;
    
    //Pickup
    public int pickupMechanism = NA;
    
    //Pass
    public int passMechanism = NA;
    public int accuratePassesCount = 0;
    public boolean canPassOverTruss = false;
    
    //Shoot
    public int shootMechanism = NA;
    public int successfulHighGoalShots = 0;
    public int successfulLowGoalShots = 0;
    
    //Defense
    public int defenseMechanism = NA;
    
    //Outcome
    public int outcome = LOSE;
    
    //Final Score
    public int finalScore = 0;
    
    //Penalties
    public String penalties = ""; 
    
    /**
     * Checks whether two sets of match data are equivalent to each other, meaning
     * the values of all data variables are equal. As this is used in determining 
     * conflicts, it doesn't account for differences in the "Scouter Name" field, 
     * as a conflict here will make no difference.
     * @param data
     * @return 
     */
    public boolean equalTo(MatchFormData data)
    {
        //Compare every value
        boolean equal = 
                data.autonomousScore == autonomousScore &&
                data.comments.equals(comments) &&
                data.maneuverability == maneuverability &&
                data.matchNumber == matchNumber &&
                data.outcome == outcome &&
                data.penalties.equals(penalties) &&
                data.robotActive == robotActive &&
                data.teamNumber == teamNumber;
        return equal;
    }
    
    /**
     * Checks whether two forms identify the same, meaning they have the same team
     * number and match number.
     * @param data
     * @return 
     */
    public boolean identifiesAs(MatchFormData data)
    {
        boolean equal = 
                data.teamNumber == teamNumber &&
                data.matchNumber == matchNumber;
        return equal;
    }
}
