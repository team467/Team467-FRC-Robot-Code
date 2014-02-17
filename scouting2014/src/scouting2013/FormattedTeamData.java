/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scouting2013;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author aidan This is a container for all the data that actually appears on
 * the table, as well as all the match forms for a specified team. Each formatted
 * team data object has data for a single team.
 */
public class FormattedTeamData implements Serializable
{
    //Team number
    private int teamNumber = 0;
    
    //Table display values
    public double defenseScore = 0;
    public double averagePasses = 0;
    public double averageCatches = 0;           
    public double averageScore = 0;
    public int highestScore = 0;
    public int wins = 0;
    public int totalMatches = 0;
    public int bestDistance = 0;
    
    //All the match forms for this team
    public LinkedList<MatchFormData> matchForms;
    
    //The active form for this team
    public ActiveFormData activeForm;
    
    /**
     * Creates a new formatted team data object with the given team number
     * @param number 
     */
    public FormattedTeamData(int number)
    {
        teamNumber = number;
        matchForms = new LinkedList<MatchFormData>();
    }
    
    /**
     * Get the team number associated with this team form data object
     * @return 
     */
    public int getTeamNumber()
    {
        return teamNumber;
    }
    
    /**
     * Adds a match form to this team form data
     * @param form 
     */
    public void addMatchForm(MatchFormData form)
    {
        matchForms.add(form);
    }
    
   public int getTeamColor(int teamNumber) 
   {
       
       return matchForms.get(1).teamColor;
   }
    public void setActiveForm(ActiveFormData form)
    {
        activeForm = form;
    }
    
    /**
     * Takes raw match value forms and turns the data into something meaningful
     * to be displayed on the data table
     */
    public void formatData()
    {
//        if (activeForm != null)
//        {
//            //Team name
//            teamName = activeForm.teamName;
//        }
        
        //Temporarily make variables to store data
        double defTotal = 0.0;    //Double for precision when dividing
        double scoreTotal = 0.0;  //Double for precision when dividing
        double passTotal = 0.0;
        double catchTotal = 0.0;
        double defMatchNum = 0.0;
        double passMatchNum = 0.0;
        double catchMatchNum = 0.0;
        double totalMatches = 0.0;
        wins = 0;
        
        //Iterate through all match forms
        for (MatchFormData form : matchForms)
        {
            //Add win number
            if (form.outcome == MatchFormData.WIN)
            {
                wins += 1;
            }
            //Check to see if its the highest score, if it is change int highest score
            if (form.finalScore > highestScore){
                highestScore = form.finalScore;
            }
            
            //totalMatches += 1;
            
            
            if (form.robotActive == MatchFormData.YES)
            {
                //Only check defense average if defense was used
                if (form.defenseMechanism != MatchFormData.NA)
                {
                    defMatchNum += 1;
                    //Add to defense total
                    defTotal += form.defenseMechanism;
                }
                
                //Only check average passes if pass mechanism is present
                if (form.passMechanism != MatchFormData.NA){
                    passMatchNum += 1;
                    //Add to pass total
                    passTotal += form.accuratePassesCount;
                }
                
                //Only check average catches if pass mechanism is present
                if (form.catchMechanism != MatchFormData.NA){
                    catchMatchNum += 1;
                    //Add to catch total
                    catchTotal += form.catchesCompletedCount;
                }
                scoreTotal += form.finalScore;
            

            }
        }
        
        //Calculate data
        defenseScore = Math.round((defTotal / defMatchNum)/.01)*.01;
        totalMatches = matchForms.size();
        averageScore = Math.round((scoreTotal / totalMatches)/.01)*.01;
        averagePasses = Math.round((passTotal / totalMatches)/.01)*.01;
        averageCatches = Math.round((catchTotal / totalMatches)/.01)*.01;
    }
}
