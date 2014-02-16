/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scouting2013;

import java.io.Serializable;

/**
 *
 * @author aidan
 */
public class ActiveFormData implements Serializable
{
    //Indentifiers
    public int teamNumber = 0;
    public String teamName = "";
    public String scouterName = "";
    
    //Text data
    public String drivetrainText = "";
    public String issuesText = "";
    
    /**
     * Checks whether two sets of active data are equivalent to each other, meaning
     * the values of all data variables are equal. As this is used in determining 
     * conflicts, it doesn't account for differences in the "Scouter Name" field, 
     * as a conflict here will make no difference.
     * @param data
     * @return 
     */
    public boolean equalTo(ActiveFormData data)
    {
        //Comapare every value
        boolean equal = teamNumber == data.teamNumber &&
                drivetrainText.equals(data.drivetrainText) &&
                issuesText.equals(data.issuesText);
        
        return equal;
                
    }
}
