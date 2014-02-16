/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scouting2013;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.FileReader;
import java.io.IOException;
import java.text.AttributedString;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author aidan Match scouting form. Object itself is a child of JFrame and has
 * panel components within.
 */
public class MatchForm extends JFrame
{      
    //Point values and strategies
    
    public String[] autonomousStrategies = {"Drive forward","Shoot (high goal) (hot)","Shoot (high goal) (cold)","Shoot (low goal) (hot)", "Shoot (low goal) (cold)","None"}; 
    String autonomousAction;
    int autonomousDropdown1Points;
    int autonomousDropdown2Points;
    int autonomousDropdown3Points;
    int autonomousTotalPoints;
    String informationOnHowToScout = "Match Scouting Instructions\n" +
    "____________________________________________________________________\n" +
    "Autonomous\n" +
    "- DO NOT select drive forward more than once, or in any dropdown\n" +
    "besides the first one\n" +
    "- Use the third dropdown only if the robot shoots more than one ball\n" +
    "during the autonomous period\n" +
    "____________________________________________________________________\n" +
    "Robot Abilities\n" +
    "- Select each checkbox pertaining to the abilities that the robot\n" +
    "is capable of\n" +
    "- This will make available the robot abilities subpanels\n" +
    "____________________________________________________________________\n" +
    "Catch Mechanisms\n" +
    "- The catch count is the number of times the robot gains possession of a ball coming from another allied robot\n" +
    "\n" +
    "POSSESSION is defined as:\n" +
    "	carrying - (moving while supporting BALLS in or on the 	ROBOT)\n" +
    "	herding - (repeated pushing or bumping)\n" +
    "	launching - (impelling BALLS to a desired location or 	direction via a MECHANISM in motion relative to the ROBOT)\n" +
    "	trapping - (overt isolation or holding one or more BALLS against a FIELD element or ROBOT in an attempt to shield them)\n" +
    "____________________________________________________________________\n" +
    "Pass Mechanisms\n" +
    "- A successful pass is defined as getting the ball successfully into an allied robot's possession\n" +
    "____________________________________________________________________";
    
    //Dropdown for team colors
    public String[] possibleTeamColors = {"Red","Blue"};
    
    //Components
    JLabel autonomousPointsLabel = new JLabel("0");
    private JScrollPane scrollPane;
    private JPanel mainPanel;
    private JLabel title;
    private JTextField teamNumberArea;
    private JTextField matchNumberArea;
    private JTextField scouterNameArea;
    private JComboBox teamColorDropdown;
    private JButton saveButton;
    private JButton infoButton;
    private JButton resetButton;
    private JRadioButton activeYesButton;
    private JRadioButton activeNoButton;
    private JRadioButton activePartialButton;
    private JTextArea commentsArea;
    private JComboBox autonomousDropdown1;
    private JComboBox autonomousDropdown2;
    private JComboBox autonomousDropdown3;
    private JRadioButton maneuverePoorButton;
    private JRadioButton maneuvereAverageButton;
    private JRadioButton maneuvereSuperbButton;
    private JRadioButton defensePoorButton;
    private JRadioButton defenseAverageButton;
    private JRadioButton defenseSuperbButton;
    private JRadioButton defenseNAButton;
    private JRadioButton catchPoorButton;
    private JRadioButton catchAverageButton;
    private JRadioButton catchSuperbButton;
    private JRadioButton catchNAButton;
    private JRadioButton pickupPoorButton;
    private JRadioButton pickupAverageButton;
    private JRadioButton pickupSuperbButton;
    private JRadioButton pickupNAButton;
    private JRadioButton passPoorButton;
    private JRadioButton passAverageButton;
    private JRadioButton passSuperbButton;
    private JRadioButton passNAButton;
    private JLabel accuratePasses;
    private JSpinner passSpinner;
    private JLabel catchesJLablel;
    private JSpinner catchSpinner;
    private JRadioButton shootPoorButton;
    private JRadioButton shootAverageButton;
    private JRadioButton shootSuperbButton;
    private JRadioButton shootNAButton;
    private JLabel successfulShotsHighJLabel;
    private JLabel successfulShotsLowJLabel;
    private JSpinner shootSpinnerHigh;
    private JSpinner shootSpinnerLow;
    private JRadioButton outcomeWinButton;
    private JRadioButton outcomeLoseButton;
    private JRadioButton outcomeTieButton;
    private JTextArea outcomePenaltiesArea;
    private JSpinner outcomeScoreSpinner;
    private JCheckBox catchAbilityButton;
    private JCheckBox pickupAbilityButton;
    private JCheckBox passAbilityButton;
    private JCheckBox shootAbilityButton;
    private JCheckBox defenseAbilityButton;
    private JCheckBox passOverTrussButton;
    private JCheckBox catchOverTrussButton;


    
    //Category panels
    private JPanel robotActivePanel;
    private JPanel autonomousPanel;
    private JPanel driveTrainPanel;
    private JPanel robotAbilitiesPanel;
    private JPanel outcomePanel;
    
    //Nested panels (for organization)
    private JPanel titlePanel;
    private JPanel identifierPanel;
    private JPanel panel1;
    private JPanel panel4;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel commentsPanel;
    private JPanel activeSubPanel;
    private JPanel autoSubPanel1;
    private JPanel driveSubPanel1;
    private JPanel intakeSubPanel;
    private JPanel releaseSubPanel;
    private JPanel intakeReleaseCenterPanel;
    private JPanel defenseSubPanel;
    private JPanel catchSubPanel;
    private JPanel pickupSubPanel;
    private JPanel passSubPanel;
    private JPanel shootSubPanel;
    private JPanel pickSubPanel;
    private JPanel outcomeSubPanel1;
    private JPanel outcomeSubPanel2;
    private JPanel outcomeSubPanel3;
    private JPanel abilitiesSubPanel;
    private JPanel catchOverTrussSubPanel;
    
    //Button grouping pannels
    private JPanel catchButtonPanel;
    private JPanel pickupButtonPanel;
    private JPanel passButtonPanel;
    private JPanel shootButtonPanel;
    private JPanel defenseButtonPanel;
    private JPanel infoResetSaveButtonPanel;
    

    //Spinner grouping pannels
    private JPanel shootSpinnerPanel;
    private JPanel passSpinnerPanel;
    
    //Button groups
    private ButtonGroup robotActiveBG;
    private ButtonGroup autonomousSuccessBG;
    private ButtonGroup maneuvereBG;
    private ButtonGroup defenseBG;
    private ButtonGroup catchBG;
    private ButtonGroup pickupBG;
    private ButtonGroup passBG;
    private ButtonGroup shootBG;
    private ButtonGroup outcomeBG;    
  
    
    //Listeners
    
    /*
     * Save button listener which saves data when the save button is pressed
     */
    private ActionListener saveButtonListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            //Generate the new form
            MatchFormData formData = getSaveData();
            
            //Save only if the new form was successfully created
            if (formData != null)
            {
                Globals.saveData.addMatchForm(formData);
                Globals.saveData.saveData();
                
                
                //Display popup window
                JOptionPane.showMessageDialog(MainFrame.form, "Form Saved", "Form Saved", JOptionPane.PLAIN_MESSAGE);
                
                //Display prompt asking whether or not to clear the form
                int result = JOptionPane.showConfirmDialog(MainFrame.form,"Would you like to clear the form? \n Warning: Unsaved changes will be lost.", "Clear Form", JOptionPane.YES_NO_OPTION);
                
                if (result == JOptionPane.YES_OPTION){
                    //If they choose yes, set all elements back to defaults
                    resetForm();
                }
                else{
                    //Dont clear form to defaults
                }
                //Refresh
                Globals.mainFrame.refreshData();
            }
            
        }
    };
    
    
     /*
     * info button listener which opens info pannel when the info button is pressed
     */
    private ActionListener infoButtonListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e)
        {
                
                //Display popup window
                JOptionPane.showMessageDialog(MainFrame.form, informationOnHowToScout , "Info", JOptionPane.PLAIN_MESSAGE);
                
                //Refresh
                Globals.mainFrame.refreshData();
            
            
        }
    };
    
         /*
     * Reset button listener which opens info pannel when the info button is pressed
     */
     private ActionListener resetButtonListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e)
        {
                //Display prompt asking whether or not to clear the form
                int result = JOptionPane.showConfirmDialog(MainFrame.form,"Would you like to clear the form? \n Warning: Unsaved changes will be lost.", "Clear Form", JOptionPane.YES_NO_OPTION);
                
                if (result == JOptionPane.YES_OPTION){
                    //If they choose yes, set all elements back to defaults
                    resetForm();
                }
                else{
                    //Dont clear form to defaults
                }
                //Refresh
                Globals.mainFrame.refreshData();

            
        }
    };
    
    /**
     * Listener which disables/enables components based on the "Robot Active" buttons
     */
    private ActionListener robotActiveListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            
                 robotAbilitiesPanel.setEnabled(true);       
                 //Enable all components if robot active is "Yes"
            if (command.equals("Yes") || command.equals("Partial"))
            {
                if (!catchAbilityButton.isSelected())
                 catchSubPanel.setEnabled(false);
                if (!pickupAbilityButton.isSelected())
                 pickupSubPanel.setEnabled(false);
                 if (!passAbilityButton.isSelected())
                 passSubPanel.setEnabled(false);               
                if (!shootAbilityButton.isSelected())
                 shootSubPanel.setEnabled(false);
                if (!defenseAbilityButton.isSelected())
                 defenseSubPanel.setEnabled(false);
                autonomousPanel.setEnabled(true);
                driveTrainPanel.setEnabled(true);
                               
            }
            
            //Disable all components if robot active is "No" or "Partial"
            if (command.equals("No"))
            {
                autonomousPanel.setEnabled(false);
                driveTrainPanel.setEnabled(false);
                robotAbilitiesPanel.setEnabled(false);
            }
        }
    };
    
    
    
       /**
     * Listener which disables/enables components based on the "Robot Active" buttons
     */
    private ActionListener robotAbilitiesListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            
            //Enable ability panel if corresponding ablility checbox is checked
            if (command.equals("Has Catch Mechanism"))
            {
                if (catchSubPanel.isEnabled())
                catchSubPanel.setEnabled(false);
                else
                catchSubPanel.setEnabled(true);

            }
                       
            if (command.equals("Has Pickup Mechanism"))
            {
                if (pickupSubPanel.isEnabled())
                pickupSubPanel.setEnabled(false);
                else
                pickupSubPanel.setEnabled(true);                               
            }
                                    
            if (command.equals("Has Pass Mechanism"))
            {
                 if (passSubPanel.isEnabled())
                passSubPanel.setEnabled(false);
                else
                passSubPanel.setEnabled(true);                              
            }
           
            if (command.equals("Has Shoot Mechanism"))
            {
                if (shootSubPanel.isEnabled())
                shootSubPanel.setEnabled(false);
                else
                shootSubPanel.setEnabled(true);                               
            }            
            if (command.equals("Has Defense Mechanism"))
            {
                 if (defenseSubPanel.isEnabled())
                defenseSubPanel.setEnabled(false);
                else
                defenseSubPanel.setEnabled(true);                              
            }
            
            
        }
    };
    
    
    /**
     * Listener which retrieves selected dropdown options in order to calculate point total
     */
    //Need to update the JLabel based on all of the dropdown menus, not just each individually (in order to prevent increase when changing options)
    private ActionListener autonomousDropdownListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            JComboBox cb = (JComboBox)e.getSource();
            autonomousAction = (String)cb.getSelectedItem();
            if (cb == autonomousDropdown1){
                autonomousDropdown1Points = 0;
                if (autonomousAction == autonomousStrategies[0]){
                    autonomousDropdown1Points += 5;
                }
                else if (autonomousAction == autonomousStrategies[1]){
                    autonomousDropdown1Points += 20;
                }
                else if (autonomousAction == autonomousStrategies[2]){
                    autonomousDropdown1Points += 15;
                }
                else if (autonomousAction == autonomousStrategies[3]){
                    autonomousDropdown1Points += 11;
                }
                else if (autonomousAction == autonomousStrategies[4]){
                    autonomousDropdown1Points += 6;
                }
                else if (autonomousAction == autonomousStrategies[5]){
                    autonomousDropdown1Points += 0;
                }
            }
            else if (cb == autonomousDropdown2){
                autonomousDropdown2Points = 0;
                if (autonomousAction == autonomousStrategies[0]){
                    autonomousDropdown2Points += 5;
                }
                else if (autonomousAction == autonomousStrategies[1]){
                    autonomousDropdown2Points += 20;
                }
                else if (autonomousAction == autonomousStrategies[2]){
                    autonomousDropdown2Points += 15;
                }
                else if (autonomousAction == autonomousStrategies[3]){
                    autonomousDropdown2Points += 11;
                }
                else if (autonomousAction == autonomousStrategies[4]){
                    autonomousDropdown2Points += 6;
                }
                else if (autonomousAction == autonomousStrategies[5]){
                    autonomousDropdown2Points += 0;
                }

            }
            else if (cb == autonomousDropdown3){
                autonomousDropdown3Points = 0;
                if (autonomousAction == autonomousStrategies[0]){
                    autonomousDropdown3Points += 5;
                }
                else if (autonomousAction == autonomousStrategies[1]){
                    autonomousDropdown3Points += 20;
                }
                else if (autonomousAction == autonomousStrategies[2]){
                    autonomousDropdown3Points += 15;
                }
                else if (autonomousAction == autonomousStrategies[3]){
                    autonomousDropdown3Points += 11;
                }
                else if (autonomousAction == autonomousStrategies[4]){
                    autonomousDropdown3Points += 6;
                }
                else if (autonomousAction == autonomousStrategies[5]){
                    autonomousDropdown3Points += 0;
                }

            }
            autonomousPointsLabel.setText(Integer.toString(autonomousDropdown1Points+autonomousDropdown2Points+autonomousDropdown3Points));
        }
    };
      

    /*
     * New match form creation
     */
    public MatchForm(String teamNumber, String matchNumber) throws IOException
    {
        super("Match Form");
        
        //Make components
        scrollPane = new JScrollPane();
        mainPanel = new WatermarkPanel("Images/aerialassistlogo.png");
        title = new JLabel("Match Scouting");
        teamNumberArea = new JTextField(4);
        matchNumberArea = new JTextField(4);
        scouterNameArea = new JTextField(10);
        teamColorDropdown = new JComboBox(possibleTeamColors);
        saveButton = new JButton();
        infoButton = new JButton();
        resetButton = new JButton();
        activeYesButton = new JRadioButton("Yes");
        activeNoButton = new JRadioButton("No");
        activePartialButton = new JRadioButton("Partial");
        commentsArea = new JTextArea();
        autonomousDropdown1 = new JComboBox(autonomousStrategies);
        autonomousDropdown2 = new JComboBox(autonomousStrategies);
        autonomousDropdown3 = new JComboBox(autonomousStrategies);
        //autonomousScoreSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        catchAbilityButton = new JCheckBox("Has Catch Mechanism");
        pickupAbilityButton = new JCheckBox("Has Pickup Mechanism");
        passAbilityButton = new JCheckBox("Has Pass Mechanism");
        shootAbilityButton = new JCheckBox("Has Shoot Mechanism");
        defenseAbilityButton = new JCheckBox("Has Defense Mechanism");    
        passOverTrussButton = new JCheckBox("Can pass over truss");        
        catchOverTrussButton = new JCheckBox("Can catch over truss");
        maneuverePoorButton = new JRadioButton("Poor");
        maneuvereAverageButton = new JRadioButton("Average");
        maneuvereSuperbButton = new JRadioButton("Superb");
        defensePoorButton = new JRadioButton("Poor");
        defenseAverageButton = new JRadioButton("Average");
        defenseSuperbButton = new JRadioButton("Superb");
        defenseNAButton = new JRadioButton("Not working");
        catchPoorButton = new JRadioButton("Poor");
        catchAverageButton = new JRadioButton("Average");
        catchSuperbButton = new JRadioButton("Superb");
        catchNAButton = new JRadioButton("Not working");
        pickupPoorButton = new JRadioButton("Poor");
        pickupAverageButton = new JRadioButton("Average");
        pickupSuperbButton = new JRadioButton("Superb");
        pickupNAButton = new JRadioButton("Not working");
        passPoorButton = new JRadioButton("Poor");
        passAverageButton = new JRadioButton("Average");
        passSuperbButton = new JRadioButton("Superb");
        passNAButton = new JRadioButton("Not working");
        accuratePasses = new JLabel("Accurate Passes: ");
        catchesJLablel = new JLabel("Catches:");
        passSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        catchSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        shootPoorButton = new JRadioButton("Poor");
        shootAverageButton = new JRadioButton("Average");
        shootSuperbButton = new JRadioButton("Superb");
        shootNAButton = new JRadioButton("Not working");
        successfulShotsHighJLabel = new JLabel("Successful shots (high goal): ");
        successfulShotsLowJLabel = new JLabel("Successful shots (low goal): ");
        shootSpinnerHigh = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        shootSpinnerLow = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        outcomeWinButton = new JRadioButton("Win");
        outcomeLoseButton = new JRadioButton("Lose");
        outcomeTieButton = new JRadioButton("Tie");
        outcomePenaltiesArea = new JTextArea();
        outcomeScoreSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 500, 1));
        
        //Make category panels
        robotActivePanel = new FullDisablePanel();
        autonomousPanel = new FullDisablePanel();
        driveTrainPanel = new FullDisablePanel();
        robotAbilitiesPanel = new FullDisablePanel();
        abilitiesSubPanel = new FullDisablePanel();
        outcomePanel = new FullDisablePanel();
        
        //Make nested panels
        titlePanel = new JPanel();
        infoResetSaveButtonPanel = new JPanel();
        identifierPanel = new JPanel();
        panel1 = new FullDisablePanel();
        panel4 =  new FullDisablePanel();
        panel2 = new FullDisablePanel();
        panel3 = new FullDisablePanel();
        commentsPanel = new FullDisablePanel();
        activeSubPanel = new FullDisablePanel();
        autoSubPanel1 = new FullDisablePanel();
        //autoSubPanel2 = new FullDisablePanel();
        driveSubPanel1 = new FullDisablePanel();
        intakeSubPanel = new FullDisablePanel();
        releaseSubPanel = new FullDisablePanel();
        intakeReleaseCenterPanel = new FullDisablePanel();
        defenseSubPanel = new FullDisablePanel();
        catchSubPanel = new FullDisablePanel();
        pickupSubPanel = new FullDisablePanel();
        passSubPanel = new FullDisablePanel();
        shootSubPanel = new FullDisablePanel();
        pickSubPanel = new FullDisablePanel();
        outcomeSubPanel1 = new FullDisablePanel();
        outcomeSubPanel2 = new FullDisablePanel();
        outcomeSubPanel3 = new FullDisablePanel();
        catchButtonPanel = new FullDisablePanel();
        pickupButtonPanel = new FullDisablePanel();
        passButtonPanel = new FullDisablePanel();
        shootButtonPanel = new FullDisablePanel();
        defenseButtonPanel = new FullDisablePanel();
        shootSpinnerPanel = new FullDisablePanel();
        passSpinnerPanel = new FullDisablePanel();
        catchOverTrussSubPanel = new FullDisablePanel();
        
        //Set layouts
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); //new GridLayout(4, 1)
        titlePanel.setLayout(new BorderLayout());
        identifierPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        robotActivePanel.setLayout(new BoxLayout(robotActivePanel, BoxLayout.X_AXIS));
        autonomousPanel.setLayout(new GridLayout(1, 0));
        driveTrainPanel.setLayout(new GridLayout(1, 0));
        robotAbilitiesPanel.setLayout(new BorderLayout());
        outcomePanel.setLayout(new BoxLayout(outcomePanel, BoxLayout.X_AXIS));
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        commentsPanel.setLayout(new BorderLayout());
        activeSubPanel.setLayout(new GridLayout(3, 0));
        autoSubPanel1.setLayout(new FlowLayout(FlowLayout.LEADING));
        //autoSubPanel2.setLayout(new FlowLayout(FlowLayout.LEADING));
        intakeSubPanel.setLayout(new GridLayout(0,1));
        releaseSubPanel.setLayout(new GridLayout(0,1));
        intakeReleaseCenterPanel.setLayout(new GridLayout(0,2));
        intakeReleaseCenterPanel.setBackground(Color.RED);
        defenseSubPanel.setLayout(new BorderLayout());
        catchSubPanel.setLayout(new BorderLayout());
        pickupSubPanel.setLayout(new BorderLayout());
        passSubPanel.setLayout(new BorderLayout());
        shootSubPanel.setLayout(new BorderLayout());
        catchOverTrussSubPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        driveSubPanel1.setLayout(new FlowLayout(FlowLayout.LEADING));
        pickSubPanel.setLayout(new GridLayout(2, 0));
        outcomeSubPanel1.setLayout(new GridLayout(3, 0));
        outcomeSubPanel2.setLayout(new BorderLayout());
        outcomeSubPanel3.setLayout(new FlowLayout());
        infoResetSaveButtonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        
        //Set borders
        marginBorder(titlePanel, 3, 3, 3, 3);
        marginBorder(activeSubPanel, 0, 0, 2, 10);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        marginBorder(scrollPane, 0, 3, 3, 3);
        identifierPanel.setBorder(BorderFactory.createEtchedBorder());
        robotActivePanel.setBorder(createInsetTitledBorder("Robot Activity"));
        autonomousPanel.setBorder(createInsetTitledBorder("Autonomous"));
        driveTrainPanel.setBorder(createInsetTitledBorder("Drive Train"));
        robotAbilitiesPanel.setBorder(createInsetTitledBorder("Robot Abilities"));
        outcomePanel.setBorder(createInsetTitledBorder("Outcome"));
        activeSubPanel.setBorder(createEtchedTitledBorder("Active?"));
        commentsPanel.setBorder(createEtchedTitledBorder("Comments"));
        autoSubPanel1.setBorder(createEtchedTitledBorder("Score Progression"));
        //autoSubPanel2.setBorder(createEtchedTitledBorder("Autonomous Points"));
        driveSubPanel1.setBorder(createEtchedTitledBorder("Maneuverability"));
        intakeSubPanel.setBorder(createEtchedTitledBorder("Intake Mechanisms"));
        releaseSubPanel.setBorder(createEtchedTitledBorder("Release Mechanisms"));
        defenseSubPanel.setBorder(createEtchedTitledBorder("Defense Mechanisms"));
        catchSubPanel.setBorder(createEtchedTitledBorder("Catch Mechanisms"));
        pickupSubPanel.setBorder(createEtchedTitledBorder("Pickup Mechanisms"));
        passSubPanel.setBorder(createEtchedTitledBorder("Pass Mechanisms"));
        shootSubPanel.setBorder(createEtchedTitledBorder("Shoot Mechanisms"));
        pickSubPanel.setBorder(createEtchedTitledBorder("Location"));
        outcomeSubPanel1.setBorder(createEtchedTitledBorder("Win/loss?"));
        outcomeSubPanel2.setBorder(createEtchedTitledBorder("Penalties"));
        outcomeSubPanel3.setBorder(createEtchedTitledBorder("Final Score"));
        
        //Initialize settings
        title.setFont(MainFrame.font);
        saveButton.setPreferredSize(new Dimension(50, 50));
        saveButton.setIcon(createImageIcon("Images/SaveIcon.png", "Save"));
        infoButton.setPreferredSize(new Dimension(50, 50));
        infoButton.setIcon(createImageIcon("Images/InfoIcon.png", "Info"));
        resetButton.setPreferredSize(new Dimension(50,50));
        resetButton.setIcon(createImageIcon("Images/ResetIcon.png", "Trash"));
        activeSubPanel.setPreferredSize(new Dimension(100, 70));
        outcomeSubPanel1.setPreferredSize(new Dimension(100, 70));
        outcomePenaltiesArea.setPreferredSize(new Dimension(100, 50));
        outcomeSubPanel3.setPreferredSize(new Dimension(100, 70));
        commentsArea.setWrapStyleWord(true);
        commentsArea.setLineWrap(true);
        commentsArea.setPreferredSize(new Dimension(600, 50));
        outcomePenaltiesArea.setWrapStyleWord(true);
        outcomePenaltiesArea.setLineWrap(true);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        teamNumberArea.setText(teamNumber);
        matchNumberArea.setText(matchNumber);
        autonomousDropdown1.setSelectedItem(autonomousStrategies[5]);
        autonomousDropdown2.setSelectedItem(autonomousStrategies[5]);
        autonomousDropdown3.setSelectedItem(autonomousStrategies[5]);
        catchNAButton.setSelected(true);
        pickupNAButton.setSelected(true);
        passNAButton.setSelected(true);
        shootNAButton.setSelected(true);
        defenseNAButton.setSelected(true);
        activeYesButton.setSelected(true);

        //Add components
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane);
        scrollPane.getViewport().add(mainPanel);
        //titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(title, BorderLayout.WEST);
        identifierPanel.add(new JLabel("Team #:"));
        identifierPanel.add(teamNumberArea);
        identifierPanel.add(new JLabel("Match #:"));
        identifierPanel.add(matchNumberArea);
        identifierPanel.add(new JLabel("Scouter:"));
        identifierPanel.add(scouterNameArea);
        identifierPanel.add(new JLabel("Team Color:"));
        identifierPanel.add(teamColorDropdown);
        titlePanel.add(identifierPanel, BorderLayout.SOUTH);
        infoResetSaveButtonPanel.add(resetButton);
        infoResetSaveButtonPanel.add(Box.createRigidArea(new Dimension(5,0)));
        infoResetSaveButtonPanel.add(infoButton);
        infoResetSaveButtonPanel.add(Box.createRigidArea(new Dimension(5,0)));
        infoResetSaveButtonPanel.add(saveButton);
        titlePanel.add(infoResetSaveButtonPanel, BorderLayout.EAST);
        mainPanel.add(robotActivePanel);
        mainPanel.add(panel1);
        mainPanel.add(panel4);
        mainPanel.add(panel2);
        mainPanel.add(panel3);
        panel1.add(autonomousPanel);
        panel1.add(driveTrainPanel);
        panel4.add(robotAbilitiesPanel);
        panel3.add(outcomePanel);
        robotActivePanel.add(activeSubPanel);
        robotActivePanel.add(commentsPanel);
        autonomousPanel.add(autoSubPanel1);
        //autonomousPanel.add(autoSubPanel2);
        driveTrainPanel.add(driveSubPanel1);
        //robotAbilitiesPanel.add(abilitiesSubPanel1);
        abilitiesSubPanel.add(catchAbilityButton);
        abilitiesSubPanel.add(Box.createRigidArea(new Dimension(15,0)));
        abilitiesSubPanel.add(pickupAbilityButton);
        abilitiesSubPanel.add(Box.createRigidArea(new Dimension(15,0)));
        abilitiesSubPanel.add(passAbilityButton);
        abilitiesSubPanel.add(Box.createRigidArea(new Dimension(15,0)));
        abilitiesSubPanel.add(shootAbilityButton);
        abilitiesSubPanel.add(Box.createRigidArea(new Dimension(15,0)));
        abilitiesSubPanel.add(defenseAbilityButton);
        robotAbilitiesPanel.add(abilitiesSubPanel, BorderLayout.NORTH);
        robotAbilitiesPanel.add(intakeReleaseCenterPanel, BorderLayout.CENTER);
        intakeReleaseCenterPanel.add(intakeSubPanel);
        intakeReleaseCenterPanel.add(releaseSubPanel);
        robotAbilitiesPanel.add(defenseSubPanel,BorderLayout.SOUTH);
        intakeSubPanel.add(catchSubPanel);
        intakeSubPanel.add(pickupSubPanel);
        releaseSubPanel.add(passSubPanel);
        releaseSubPanel.add(shootSubPanel);
        outcomePanel.add(outcomeSubPanel1, BorderLayout.WEST);
        outcomePanel.add(outcomeSubPanel3);
        outcomePanel.add(outcomeSubPanel2);
        activeSubPanel.add(activeYesButton);
        activeSubPanel.add(activeNoButton);
        activeSubPanel.add(activePartialButton);
        commentsPanel.add(commentsArea);
        autoSubPanel1.add(autonomousDropdown1);
        autoSubPanel1.add(new JLabel("+"));
        autoSubPanel1.add(autonomousDropdown2);
        autoSubPanel1.add(new JLabel("+"));
        autoSubPanel1.add(autonomousDropdown3);
        autoSubPanel1.add(new JLabel("="));
        autoSubPanel1.add(autonomousPointsLabel);
        driveSubPanel1.add(maneuverePoorButton);
        driveSubPanel1.add(maneuvereAverageButton);
        driveSubPanel1.add(maneuvereSuperbButton);
        defenseButtonPanel.add(defenseNAButton);
        defenseButtonPanel.add(defensePoorButton);
        defenseButtonPanel.add(defenseAverageButton);
        defenseButtonPanel.add(defenseSuperbButton);
        defenseSubPanel.add(defenseButtonPanel,BorderLayout.NORTH);
        catchButtonPanel.add(catchNAButton);
        catchButtonPanel.add(catchPoorButton);
        catchButtonPanel.add(catchAverageButton);
        catchButtonPanel.add(catchSuperbButton);

        catchSubPanel.add(catchButtonPanel,BorderLayout.NORTH);
        catchOverTrussSubPanel.add(catchesJLablel);
        catchOverTrussSubPanel.add(catchSpinner);
        catchOverTrussSubPanel.add(Box.createRigidArea(new Dimension(20,0)));
        catchOverTrussSubPanel.add(catchOverTrussButton);
        catchSubPanel.add(catchOverTrussSubPanel,BorderLayout.SOUTH);
        pickupButtonPanel.add(pickupNAButton);
        pickupButtonPanel.add(pickupPoorButton);
        pickupButtonPanel.add(pickupAverageButton);
        pickupButtonPanel.add(pickupSuperbButton);
        
        pickupSubPanel.add(pickupButtonPanel,BorderLayout.NORTH);
        passButtonPanel.add(passNAButton);
        passButtonPanel.add(passPoorButton);
        passButtonPanel.add(passAverageButton);
        passButtonPanel.add(passSuperbButton);

        passSubPanel.add(passButtonPanel,BorderLayout.NORTH);
        passSpinnerPanel.add(accuratePasses);
        passSpinnerPanel.add(passSpinner);
        passSpinnerPanel.add(Box.createRigidArea(new Dimension(20,0)));
        passSpinnerPanel.add(passOverTrussButton);
        passSubPanel.add(passSpinnerPanel,BorderLayout.SOUTH);
        shootButtonPanel.add(shootNAButton); 
        shootButtonPanel.add(shootPoorButton);
        shootButtonPanel.add(shootAverageButton);
        shootButtonPanel.add(shootSuperbButton);      
        shootSubPanel.add(shootButtonPanel,BorderLayout.NORTH);
        shootSpinnerPanel.add(successfulShotsHighJLabel);        
        shootSpinnerPanel.add(shootSpinnerHigh);
        shootSpinnerPanel.add(Box.createRigidArea(new Dimension(20,0)));
        shootSpinnerPanel.add(successfulShotsLowJLabel);
        shootSpinnerPanel.add(shootSpinnerLow);
        shootSubPanel.add(shootSpinnerPanel,BorderLayout.SOUTH);
        outcomeSubPanel1.add(outcomeWinButton);
        outcomeSubPanel1.add(outcomeLoseButton);
        outcomeSubPanel1.add(outcomeTieButton);
        outcomeSubPanel3.add(outcomeScoreSpinner);
        outcomeSubPanel2.add(outcomePenaltiesArea);

        
        //Add radio buttons to button groups
        robotActiveBG = new ButtonGroup();
        robotActiveBG.add(activeYesButton);
        robotActiveBG.add(activeNoButton);
        robotActiveBG.add(activePartialButton);
        maneuvereBG = new ButtonGroup();
        maneuvereBG.add(maneuverePoorButton);
        maneuvereBG.add(maneuvereAverageButton);
        maneuvereBG.add(maneuvereSuperbButton);
        defenseBG = new ButtonGroup();
        defenseBG.add(defensePoorButton);
        defenseBG.add(defenseAverageButton);
        defenseBG.add(defenseSuperbButton);
        defenseBG.add(defenseNAButton);
        catchBG = new ButtonGroup();
        catchBG.add(catchPoorButton);
        catchBG.add(catchAverageButton);
        catchBG.add(catchSuperbButton);
        catchBG.add(catchNAButton);
        pickupBG = new ButtonGroup();
        pickupBG.add(pickupPoorButton);
        pickupBG.add(pickupAverageButton);
        pickupBG.add(pickupSuperbButton);
        pickupBG.add(pickupNAButton);
        passBG = new ButtonGroup();
        passBG.add(passPoorButton);
        passBG.add(passAverageButton);
        passBG.add(passSuperbButton);
        passBG.add(passNAButton);
        shootBG = new ButtonGroup();
        shootBG.add(shootPoorButton);
        shootBG.add(shootAverageButton);
        shootBG.add(shootSuperbButton);
        shootBG.add(shootNAButton);
        outcomeBG = new ButtonGroup();
        outcomeBG.add(outcomeWinButton);
        outcomeBG.add(outcomeLoseButton);
        outcomeBG.add(outcomeTieButton);
        
        //Add listeners
        saveButton.addActionListener(saveButtonListener);
        infoButton.addActionListener(infoButtonListener);
        resetButton.addActionListener(resetButtonListener);
        activeYesButton.addActionListener(robotActiveListener);
        activeNoButton.addActionListener(robotActiveListener);
        activePartialButton.addActionListener(robotActiveListener);
        catchAbilityButton.addActionListener(robotAbilitiesListener);
        pickupAbilityButton.addActionListener(robotAbilitiesListener);
        passAbilityButton.addActionListener(robotAbilitiesListener);
        shootAbilityButton.addActionListener(robotAbilitiesListener);
        defenseAbilityButton.addActionListener(robotAbilitiesListener);        
        autonomousDropdown1.addActionListener(autonomousDropdownListener);
        autonomousDropdown2.addActionListener(autonomousDropdownListener);
        autonomousDropdown3.addActionListener(autonomousDropdownListener);
                
        //Set frame size and visibility
        Toolkit screenDementions = Toolkit.getDefaultToolkit();  
        setSize(new Dimension( (int) screenDementions.getScreenSize().getWidth(), ((int) screenDementions.getScreenSize().getHeight()) - 30));
        setMinimumSize(new Dimension(800, 0));
        setVisible(true);
        
        
        // Set abilities disabed by default
        catchSubPanel.setEnabled(false);
        pickupSubPanel.setEnabled(false);
        passSubPanel.setEnabled(false);
        shootSubPanel.setEnabled(false);
        defenseSubPanel.setEnabled(false);


    }
    
    /**
     * Apply margin to the specified component
     * @param component
     * @param top
     * @param bottom
     * @param left
     * @param right 
     */
    public void marginBorder(JComponent component, int top, int bottom, int left, int right)
    {
        Border current = component.getBorder();
        Border empty = new EmptyBorder(top, left, bottom, right
        );
        if (current == null)
        {
            component.setBorder(empty);
        }
        else
        {
            component.setBorder(new CompoundBorder(empty, current));
        }
    }
    
    /**
     * Generates an inset border with a title in the top left hand corner
     * @param title The title to give the border
     * @return 
     */
    private Border createInsetTitledBorder(String title)
    {
        Border border = UIManager.getDefaults().getBorder("TitledBorder.border");//BorderFactory.createCompoundBorder(
        border = BorderFactory.createTitledBorder(
                border, title, TitledBorder.LEFT, TitledBorder.TOP);
        return border;
    }
    
    /**
     * Generates an etched border with a title in the top left hand corner
     * @param title The title to give the border
     * @return 
     */
    private Border createEtchedTitledBorder(String title)
    {
        Border border = BorderFactory.createTitledBorder(new EtchedBorder(), title, TitledBorder.LEFT, TitledBorder.TOP);
        return border;
    }
    
    /**
     * Creates a new separator with the specified orientation and size
     * @param orientation
     * @param width
     * @param height
     * @return 
     */
    private JSeparator createSeparator(int orientation, int width, int height)
    {
        JSeparator s = new JSeparator(orientation);
        s.setPreferredSize(new Dimension(width, height));
        return s;
    }
    
    /**
     * Creates an image icon from the image at the given path
     * @param path The path of the image
     * @param description The image description
     * @return 
     */
    private ImageIcon createImageIcon(String path,
            String description)
    {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null)
        {
            ImageIcon i = new ImageIcon(imgURL, description);
            return i;
        }
        else
        {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    /**
     * Get a MatchFormData object containing all the data as it currently appears
     * on the match form. All the components are individually interpreted and the
     * data from them is stored in the MatchFormData.
     * @return A MatchFormData object with the data as it currently is
     */
    private MatchFormData getSaveData()
    {
        //Make blank match form data object
        MatchFormData saveData = new MatchFormData();

        //Send an error message if one of the three identifier fields is missing
        if (teamNumberArea.getText().equals("") || matchNumberArea.getText().equals("") ||
            scouterNameArea.getText().equals(""))
        {
            //Display popup window
            JOptionPane.showMessageDialog(MainFrame.form, "Missing Field", "Required field missing",
                                          JOptionPane.PLAIN_MESSAGE);
            return null;
        }
        
        //Send error message if drive forward is selected more than once
        if (autonomousDropdown2.getSelectedIndex() == 0 || autonomousDropdown3.getSelectedIndex() == 0){
            //Display popup window
            JOptionPane.showMessageDialog(MainFrame.form, "You can only select 'drive forward' in the first dropdown", "Dropdown Error", JOptionPane.PLAIN_MESSAGE);
            return null;
        }
                
        
        //Identifiers
        saveData.teamNumber = Integer.parseInt(teamNumberArea.getText());
        saveData.matchNumber = Integer.parseInt(matchNumberArea.getText());
        saveData.scouterName = scouterNameArea.getText();
        saveData.teamColor = teamColorDropdown.getSelectedIndex();

        //Robot active
        if (activeYesButton.isSelected())
        {
            saveData.robotActive = MatchFormData.YES;
        }
        if (activeNoButton.isSelected())
        {
            saveData.robotActive = MatchFormData.NO;
        }
        if (activePartialButton.isSelected())
        {
            saveData.robotActive = MatchFormData.PARTIAL;
        }

        //Comments
        saveData.comments = commentsArea.getText();
        
        //Autonomous score
        saveData.autonomousScore = (Integer) autonomousDropdown1Points+autonomousDropdown2Points+autonomousDropdown3Points;
        saveData.autonomousActions[0] = (String) autonomousDropdown1.getSelectedItem();
        saveData.autonomousActions[1] = (String) autonomousDropdown2.getSelectedItem();
        saveData.autonomousActions[2] = (String) autonomousDropdown3.getSelectedItem();
        
        //Maneuverability
        if (maneuverePoorButton.isSelected())
        {
            saveData.maneuverability = MatchFormData.POOR;
        }
        if (maneuvereAverageButton.isSelected())
        {
            saveData.maneuverability = MatchFormData.AVERAGE;
        }
        if (maneuvereSuperbButton.isSelected())
        {
            saveData.maneuverability = MatchFormData.SUPERB;
        }

        //Robot abilities
        if (catchAbilityButton.isSelected()){
            saveData.hasCatchMechanism = true;
            if (catchPoorButton.isSelected())
            {
                saveData.catchMechanism = MatchFormData.POOR;
            }
            if (catchAverageButton.isSelected())
            {
                saveData.catchMechanism = MatchFormData.AVERAGE;
            }
            if (catchSuperbButton.isSelected())
            {
                saveData.catchMechanism = MatchFormData.SUPERB;
            }
            if (catchNAButton.isSelected())
            {
                saveData.catchMechanism = MatchFormData.NA;
            }
            
            if(catchOverTrussButton.isSelected()){
                saveData.canCatchOverTruss = true;
            }
            saveData.catchesCompletedCount = (Integer) catchSpinner.getValue();
        }
        
        if (pickupAbilityButton.isSelected()){
            saveData.hasPickupMechanism = true;
            
            if (pickupPoorButton.isSelected())
            {
                saveData.pickupMechanism = MatchFormData.POOR;
            }
            if (pickupAverageButton.isSelected())
            {
                saveData.pickupMechanism = MatchFormData.AVERAGE;
            }
            if (pickupSuperbButton.isSelected())
            {
                saveData.pickupMechanism = MatchFormData.SUPERB;
            }
            if (pickupNAButton.isSelected())
            {
                saveData.pickupMechanism = MatchFormData.NA;
            }
        }
        
        if (passAbilityButton.isSelected()){
            saveData.hasPassMechanism = true;
            
            if (passPoorButton.isSelected())
            {
                saveData.passMechanism = MatchFormData.POOR;
            }
            if (passAverageButton.isSelected())
            {
                saveData.passMechanism = MatchFormData.AVERAGE;
            }
            if (passSuperbButton.isSelected())
            {
                saveData.passMechanism = MatchFormData.SUPERB;
            }
            if (passNAButton.isSelected())
            {
                saveData.passMechanism = MatchFormData.NA;
            }
            
            if(passOverTrussButton.isSelected()){
                saveData.canPassOverTruss = true;
            }
            
            saveData.accuratePassesCount = (Integer) passSpinner.getValue();
        }
        
        if (shootAbilityButton.isSelected()){
            saveData.hasShootMechanism = true;
            
            if (shootPoorButton.isSelected())
            {
                saveData.shootMechanism = MatchFormData.POOR;
            }
            if (shootAverageButton.isSelected())
            {
                saveData.shootMechanism = MatchFormData.AVERAGE;
            }
            if (shootSuperbButton.isSelected())
            {
                saveData.shootMechanism = MatchFormData.SUPERB;
            }
            if (shootNAButton.isSelected())
            {
                saveData.shootMechanism = MatchFormData.NA;
            }
            
            saveData.successfulHighGoalShots = (Integer) shootSpinnerHigh.getValue();
            
            saveData.successfulLowGoalShots = (Integer) shootSpinnerLow.getValue();
            
            
        }
        
        if (defenseAbilityButton.isSelected()){
            saveData.hasDefenseMechanism = true;
            
            if (defensePoorButton.isSelected())
            {
                saveData.defenseMechanism = MatchFormData.POOR;
            }
            if (defenseAverageButton.isSelected())
            {
                saveData.defenseMechanism = MatchFormData.AVERAGE;
            }
            if (defenseSuperbButton.isSelected())
            {
                saveData.defenseMechanism = MatchFormData.SUPERB;
            }
            if (defenseNAButton.isSelected())
            {
                saveData.defenseMechanism = MatchFormData.NA;
            }
        }

        //Outcome
        if (outcomeWinButton.isSelected())
        {
            saveData.outcome = MatchFormData.WIN;
        }
        if (outcomeLoseButton.isSelected())
        {
            saveData.outcome = MatchFormData.LOSE;
        }
        if (outcomeTieButton.isSelected())
        {
            saveData.outcome = MatchFormData.TIE;
        }
        
        //Final score
        saveData.finalScore = (Integer) outcomeScoreSpinner.getValue();
        //Penalties
        saveData.penalties = outcomePenaltiesArea.getText();
        
        return saveData;
    }
    /**
     * Set all the selected areas based on a form data object
     */
   public void resetForm()
        {
            teamNumberArea.setText("");
            matchNumberArea.setText("");
            scouterNameArea.setText("");
            teamColorDropdown.setSelectedIndex(0);
            activeYesButton.setSelected(true);
            commentsArea.setText("");
            autonomousDropdown1.setSelectedIndex(5);
            autonomousDropdown2.setSelectedIndex(5);
            autonomousDropdown3.setSelectedIndex(5);
            catchAbilityButton.setSelected(false);
            pickupAbilityButton.setSelected(false);
            passAbilityButton.setSelected(false);
            shootAbilityButton.setSelected(false);
            defenseAbilityButton.setSelected(false);
            catchNAButton.setSelected(true);
            pickupNAButton.setSelected(true);
            shootNAButton.setSelected(true);
            passNAButton.setSelected(true);
            defenseNAButton.setSelected(true);
            catchOverTrussButton.setSelected(false);
            passOverTrussButton.setSelected(false);
            catchSpinner.setValue((Integer) 0);
            passSpinner.setValue((Integer) 0);
            shootSpinnerHigh.setValue((Integer) 0);
            shootSpinnerLow.setValue((Integer) 0); 
            outcomeWinButton.setSelected(false);
            outcomeLoseButton.setSelected(false);
            outcomeTieButton.setSelected(false);
            outcomePenaltiesArea.setText("");
            outcomeScoreSpinner.setValue((Integer) 0);
            maneuvereAverageButton.setSelected(false);
            maneuverePoorButton.setSelected(false);
            maneuvereSuperbButton.setSelected(false);
            
            // Reset Panels
            robotAbilitiesPanel.setEnabled(true);       
            catchSubPanel.setEnabled(false);
            pickupSubPanel.setEnabled(false);
            passSubPanel.setEnabled(false);               
            shootSubPanel.setEnabled(false);
            defenseSubPanel.setEnabled(false);
            autonomousPanel.setEnabled(true);
            driveTrainPanel.setEnabled(true);
           
            
            

            
     
            
          
            
            
        }
    
    /**
     * Set all the selected areas based on a form data object
     */
    public void setSelections(MatchFormData saveData)
    {   
        //Identifiers
        teamNumberArea.setText(Integer.toString(saveData.teamNumber));
        matchNumberArea.setText(Integer.toString(saveData.matchNumber));
        scouterNameArea.setText(saveData.scouterName);

        //Robot active
        activeYesButton.setSelected(saveData.robotActive == MatchFormData.YES);
        activeNoButton.setSelected(saveData.robotActive == MatchFormData.NO);
        activePartialButton.setSelected(saveData.robotActive == MatchFormData.PARTIAL);

        //Comments
        commentsArea.setText(saveData.comments);

        
        //Autonomous Dropdowns

         


        //Maneuverability
        maneuverePoorButton.setSelected(saveData.maneuverability == MatchFormData.POOR);
        maneuvereAverageButton.setSelected(saveData.maneuverability == MatchFormData.AVERAGE);
        maneuvereSuperbButton.setSelected(saveData.maneuverability == MatchFormData.SUPERB);

        //Pickup


        //Outcome
        outcomeWinButton.setSelected(saveData.outcome == MatchFormData.WIN);
        outcomeLoseButton.setSelected(saveData.outcome == MatchFormData.LOSE);
        outcomeTieButton.setSelected(saveData.outcome == MatchFormData.TIE);
        
//        //Final Score
//        outcomeScoreArea.setText(saveData.finalScore);

        //Penalties
        outcomePenaltiesArea.setText(saveData.penalties);
        
        repaint();
    }
    
    /**
     * This class serves to give the ability to easily disable a vast quantity 
     * of components simply by disabling the panel they are contained within. 
     */
    private class FullDisablePanel extends JPanel
    {
        public FullDisablePanel()
        {
            super(); 
            setOpaque(false);
        }
        
        /**
         * Overridden setEnabled function which disables/enables all components contained 
         * within.
         */
        public void setEnabled(boolean enabled)
        {
            //Call parent disable function to disable the panel itself
            super.setEnabled(enabled);
            
            //Get a list of all components in this panel
            Component[] components = getComponents();
            
            //Disable each component
            for (Component component : components)
            {
                component.setEnabled(enabled);
            }
        }
    }
}
