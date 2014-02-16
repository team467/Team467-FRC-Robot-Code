/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scouting2013;

import java.io.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author aidan
 */
public class SaveData implements Serializable
{
    //Match forms
    public MatchFormData[] matchForms;
    private ActiveFormData[] activeForms;
    public LinkedList<FormattedTeamData> formattedTeamForms;
    
    
    //Remembered server ip
    public String serverIp = "";
    
    //Remembered schedule
    public String[][] scheduleTableItems = new String[100][7];
    
    /**
     * Make a new save data object
     */
    

    
    public SaveData()
    {
        matchForms = new MatchFormData[0];
        activeForms = new ActiveFormData[0];
        formattedTeamForms = new LinkedList();
    }
    
    /**
     * Gets the number of formatted forms currently saved
     * @return 
     */
    public int getNumberFormattedForms()
    {
        return formattedTeamForms.size();
    }
    
    /**
     * Adds a match form to the save data. If a match form with the same team and
     * match number exists, the new form will replace the old one.
     * @param addForm
     */
    public void addMatchForm(MatchFormData addForm)
    {  
        //Check to see if this identifies as another form and replace the old one with
        //the newer one
        for (int i = 0; i < matchForms.length; i++)
        {
            MatchFormData form = matchForms[i];
            if (form.identifiesAs(addForm))
            {
                System.out.println("Same form");
                matchForms[i] = addForm;
                return;
            }
        }
        
        //Assume a new form is being added if no form identifies the same
        int prevLength = matchForms.length;
        MatchFormData[] temp = matchForms.clone();
        
        matchForms = new MatchFormData[prevLength + 1];
        
        //Add temp stored match forms back to list
        for (int i = 0; i < temp.length; i++)
        {
            matchForms[i] = temp[i];
        }
        
        //Add new form to larger array
        matchForms[prevLength] = addForm;
        
    }
    
    /**
     * Adds an active form to the save data. If an active form with the same team 
     * number exists, the new form will replace the old one.
     */
    public void addActiveForm(ActiveFormData addForm)
    {  
        //Check to see if this identifies as another form and replace the old one with
        //the newer one
        for (int i = 0; i < activeForms.length; i++)
        {
            ActiveFormData form = activeForms[i];
            if (form.teamNumber == addForm.teamNumber)
            {
                activeForms[i] = addForm;
                return;
            }
        }
        
        //Assume a new form is being added if no form identifies the same
        int prevLength = activeForms.length;
        ActiveFormData[] temp = activeForms.clone();
        
        activeForms = new ActiveFormData[prevLength + 1];
        
        //Add temp stored match forms back to list
        for (int i = 0; i < temp.length; i++)
        {
            activeForms[i] = temp[i];
        }
        
        //Add new form to larger array
        activeForms[prevLength] = addForm;
        
    }
    
    /**
     * Synchronizes all data currently in the data folder, and returns a single SaveData
     * object. This function takes care of conflicts between matches which identify
     * the same (same team and match number) and multiple active scouting forms
     * for the same team.
     * @param data
     * @return 
     */
    public static void syncData()
    {
        SaveData[] data = null;
        try
        {
            //Make input streams
            InputStream inputfile;
            InputStream inputbuffer;
            ObjectInput input = null;
            
            //Remove unneeded invisible file
            new File(System.getProperty("user.dir") + "/Save Data/.DS_Store").delete();
            
            //Get file directory
            File formDirectory = new File(System.getProperty("user.dir") + "/Save Data"); 
            String[] files = formDirectory.list();
            
            //Make array for gathered forms
            data = new SaveData[files.length];
            
            //Only sync if there are two or more files in the directory
            if (files.length < 2)
            {
                return;
            }

            
            //Read all forms
            for (int i = 0; i < files.length; i++)
            {
                inputfile = new FileInputStream("Save Data/" + files[i]);
                inputbuffer = new BufferedInputStream(inputfile);
                input = new ObjectInputStream(inputbuffer);
                
                data[i] = (SaveData) input.readObject();
            }

            //Close stream
            input.close();
            
            //Delete all files except the main one
            for (String file : files)
            {
                if (!file.equals("Save-Master.ser"))
                {
                    System.out.println(System.getProperty("user.dir") + "/Save Data/" + file + " Deleted");
                    new File(System.getProperty("user.dir") + "/Save Data/" + file).delete();
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
        }
        catch (IOException ex)
        {
            
        }
        
        //List of all match forms from all data locations
//        LinkedList<MatchFormData> finalMatchForms = new LinkedList();
//        LinkedList<ActiveFormData> finalActiveForms = new LinkedList();
        LinkedList<MatchFormData> finalMatchForms = new LinkedList();
        LinkedList<ActiveFormData> finalActiveForms = new LinkedList();
        
        for (SaveData saveData : data)
        {
            //Combine all match forms into one conglomerate
            for (MatchFormData form : saveData.matchForms)
            { 
                boolean match = false;
                //Iterate through every form thus added to see if any conflict
                //with the form currently being added
                for (int i = 0; i < finalMatchForms.size(); i++)
                {
                    //Skip over this form if the data is the same as in another form
                    if (!finalMatchForms.get(i).equalTo(form))
                    {
                        //Check if two forms identify the same (same match and team #)
                        if (finalMatchForms.get(i).identifiesAs(form))
                        {
                            //Resolve conflict and get new form
                            MatchFormData resolvedForm = solveFormConflict(form, finalMatchForms.get(i));

                            //If resolved form is new one, remove old form and add new
                            //one to final list
                            if (resolvedForm.equals(form))
                            {
                                finalMatchForms.remove(i);
                                finalMatchForms.add(form);
                            }
                            match = true;
                            break;
                        }
                    }
                    else
                    {
                        match = true;
                        break;
                    }
                }
                
                //If there are no forms in the final list, add this one
                if (!match)
                {
                    finalMatchForms.add(form);
                }
                
            }
            
            //Combine all active forms into one conglomerate
            for (ActiveFormData form : saveData.activeForms)
            { 
                boolean match = false;
                //Iterate through every form thus added to see if any conflict
                //with the form currently being added
                for (int i = 0; i < finalActiveForms.size(); i++)
                {
                    //Skip over this form if the data is the same as in another form
                    if (!finalActiveForms.get(i).equalTo(form))
                    {
                        //Check if two forms identify the same (team #)
                        if (finalActiveForms.get(i).teamNumber == form.teamNumber)
                        {
                            //Resolve conflict and get new form
                            ActiveFormData resolvedForm = solveFormConflict(form, finalActiveForms.get(i));

                            //If resolved form is new one, remove old form and add new
                            //one to final list
                            if (resolvedForm.equals(form))
                            {
                                finalActiveForms.remove(i);
                                finalActiveForms.add(form);
                            }
                            match = true;
                            break;
                        }
                    }
                    else
                    {
                        match = true;
                        break;
                    }
                }
                
                //If there are no forms in the final list, add this one
                if (!match)
                {
                    finalActiveForms.add(form);
                }
            }
        }
        
        //Set data
        ActiveFormData[] af = new ActiveFormData[finalActiveForms.size()];
        MatchFormData[] mf = new MatchFormData[finalMatchForms.size()];
        for (int i = 0; i < af.length; i++)
        {
            af[i] = finalActiveForms.get(i);
        }
        for (int i = 0; i < mf.length; i++)
        {
            mf[i] = finalMatchForms.get(i);
        }
        
        Globals.saveData.activeForms = af;
        Globals.saveData.matchForms = mf;
    }
    
    /**
     * Solves the conflict between two match forms by popping up a window which asks
     * for the name of the scouter whose form should be used. The corresponding
     * form is then returned
     * @param data1 First form
     * @param data2 Second form
     * @return 
     */
    public static MatchFormData solveFormConflict(MatchFormData data1, MatchFormData data2)
    {
        //Scouter names to be used as options in the JOptionPane
        String[] scouterNames = new String[]
        {
            data1.scouterName, data2.scouterName
        };
        
        //Displayed message
        String message = "Form Conflict: Choose the scouter whose form should be used. \n"
                       + "Team " + data1.teamNumber +  ", Match - " + data1.matchNumber;
        
        //Display conflict window
        int selection = JOptionPane.showOptionDialog(Globals.mainFrame, 
                message, 
                "Form Conflict", 
                JOptionPane.YES_NO_CANCEL_OPTION, 
                JOptionPane.ERROR_MESSAGE, 
                null, 
                scouterNames, 
                data1.scouterName);
        
        //Return chosen form
        if (selection == 0)
        {
            System.out.println(data1.scouterName);
            return data1;
        }
        else
        {
            return data2;
        }
    }
    
    /**
     * Solves the conflict between two active forms by popping up a window which asks
     * for the name of the scouter whose form should be used. The corresponding
     * form is then returned
     * @param data1 First form
     * @param data2 Second form
     * @return 
     */
    public static ActiveFormData solveFormConflict(ActiveFormData data1, ActiveFormData data2)
    {
        //Scouter names to be used as options in the JOptionPane
        String[] scouterNames = new String[]
        {
            data1.scouterName, data2.scouterName
        };
        
        //Displayed message
        String message = "Form Conflict: Choose the scouter whose form should be used. \n"
                       + "Team " + data1.teamNumber;
        
        //Display conflict window
        int selection = JOptionPane.showOptionDialog(Globals.mainFrame, 
                message, 
                "Form Conflict", 
                JOptionPane.YES_NO_CANCEL_OPTION, 
                JOptionPane.ERROR_MESSAGE, 
                null, 
                scouterNames, 
                data1.scouterName);
        
        //Return chosen form
        if (selection == 0)
        {
            return data1;
        }
        else
        {
            return data2;
        }
    }
    
    boolean match = false;
    
    /**
     * Organizes all forms into a list of FormattedTeamData objects
     * @param forms List of forms to organize
     */
    public void organizeForms()
    {   
        //Reset forms
        formattedTeamForms.clear();
        for (MatchFormData matchForm : matchForms) 
        {
            match = false;
            //Go through every form list currently stored in the organized list
            for (int j = 0; j < formattedTeamForms.size(); j++) {
                //If the team number of the current form matches one in the organized list,
                //add this form to that list of the organized list, flag that a match
                //has been made, and exit the loop
                if (formattedTeamForms.get(j).getTeamNumber() == matchForm.teamNumber) {
                    formattedTeamForms.get(j).addMatchForm(matchForm);
                    match = true;
                    break;
                }
            }
            //If no match is made between team numbers of the current form and one in the
            //organized list, add a new form list to the organized list and add this
            //form to that new list
            if (!match) {
                FormattedTeamData newTeam = new FormattedTeamData(matchForm.teamNumber);
                newTeam.addMatchForm(matchForm);
                formattedTeamForms.add(newTeam);
            }
        }
        for (ActiveFormData activeForm : activeForms) 
        {
            match = false;
            //Go through every form list currently stored in the organized list
            for (int j = 0; j < formattedTeamForms.size(); j++) {
                //If the team number of the current form matches one in the organized list,
                //add this form to that list of the organized list, flag that a match
                //has been made, and exit the loop
                if (formattedTeamForms.get(j).getTeamNumber() == activeForm.teamNumber) {
                    formattedTeamForms.get(j).setActiveForm(activeForm);
                    match = true;
                    break;
                }
            }
            //If no match is made between team numbers of the current form and one in the
            //organized list, add a new form list to the organized list and add this
            //form to that new list
            if (!match) {
                FormattedTeamData newTeam = new FormattedTeamData(activeForm.teamNumber);
                newTeam.setActiveForm(activeForm);
                formattedTeamForms.add(newTeam);
            }
        }
    }
    
    
    /**
     * Get the formatted form corresponding to the given team
     * @param team
     * @return 
     */
    public FormattedTeamData getFormattedDataByTeam(int team)
    {
        //Check through all formatted forms to see if one has a matching team number
        for (FormattedTeamData form : formattedTeamForms)
        {
            if (form.getTeamNumber() == team)
            {
                return form;
            }
        }
        
        //Return null if no matching team is found
        return null;
    }
    
    /**
     * Saves data
     */
    public void saveData()
    {
        try
        {
            //Make output stream
            OutputStream outputfile;
            outputfile = new FileOutputStream("Save Data/Save-Master.ser");

            OutputStream outputbuffer = new BufferedOutputStream(outputfile);
            ObjectOutput output = new ObjectOutputStream(outputbuffer);

            //Write form
            output.writeObject(this);

            //Close stream
            output.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(SaveData.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    /**
     * Reads and returns all the forms in the form directory
     * @return 
     */
    public static SaveData readData()
    {
        try
        {
            //Make input streams
            InputStream inputfile = new FileInputStream("Save Data/Save-Master.ser");
            InputStream inputbuffer = inputbuffer = new BufferedInputStream(inputfile);
            ObjectInput input = new ObjectInputStream(inputbuffer);
                
            SaveData data = (SaveData) input.readObject();

            //Close stream
            input.close();
            
            return data;
            
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println(ex);
        }
        catch (IOException ex)
        {
            System.out.println(ex);
        }
        //Make new savedata object if one can't be loaded
        return new SaveData();
    }
}
