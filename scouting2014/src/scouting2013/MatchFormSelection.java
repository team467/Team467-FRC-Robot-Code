/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scouting2013;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Cam and Tyler
 */
public class MatchFormSelection extends JFrame
{
    public int matchTeamNumber = 0;
    private int[][] teamMatchesTableItems;
    private static final String[] TEAM_MATCHES_COLUMN_NAMES  = new String[] 
    {
        "Match Number"
    };
    
    /*
     * Data model for team matches table
     */
    private int getNumberOfTeamMatches(int teamNumber)
    {
        int numberOfMatches = 0;
        for(MatchFormData matchForm : Globals.saveData.matchForms) 
        {
            if (matchForm.teamNumber == teamNumber) {
                numberOfMatches++;
            }
        }
        return numberOfMatches;  
    }
    
    private int getArrayIndex(int teamNumber, int matchNumber){
        
        for(int arrayIndex = 0; arrayIndex <= Globals.saveData.matchForms.length; arrayIndex++)
        {
            if (Globals.saveData.matchForms[arrayIndex].teamNumber == teamNumber && Globals.saveData.matchForms[arrayIndex].matchNumber == matchNumber)
            {
              return arrayIndex;  
            }
        }
        return 0;

    }
    private final TableModel teamMatchesTableModel = new AbstractTableModel()
    {

        @Override
        public String getColumnName(int i)
        {
            return TEAM_MATCHES_COLUMN_NAMES[i];
        }

        @Override
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }

        @Override
        public int getRowCount()
        {
            return teamMatchesTableItems.length;
        }

        @Override
        public int getColumnCount()
        {
            return TEAM_MATCHES_COLUMN_NAMES.length;
        }

        @Override
        public Object getValueAt(int row, int column)
        {
            return teamMatchesTableItems[row][column];
        }
    };
    
    private final MouseListener mouseClickListener;
    

    private final JScrollPane scrollPane;
    private final JScrollPane matchSelectorScrollPane;
    private JPanel mainPanel;
    private JPanel titlePanel;
    private final JPanel matchSelectionPanel;
    private final JPanel informationPanel;
    
    private  JLabel teamNumberLabel;
    private final JLabel title;
    private final JLabel matchNumberLabel;
    private final JLabel scouterNameLabel;
    private final JLabel teamColorLabel;
    private final JLabel robotActiveLabel;
    private final JLabel commentsLabel;
    private final JLabel autonomousProgressionLabel;
    private final JLabel driveTrainLabel;
    private final JLabel catchScoreLabel;
    private final JLabel catchOverTrussLabel;
    private final JLabel catchNumberLabel;
    private final JLabel pickupScoreLabel;
    private final JLabel passScoreLabel;
    private final JLabel passNumberLabel;
    private final JLabel passOverTrussLabel;
    private final JLabel shootScoreLabel;
    private final JLabel shotsHighGoalLabel;
    private final JLabel shotsLowGoalLabel;
    private final JLabel defenseScoreLabel;
    private final JLabel outcomeLabel;
    private final JLabel finalScoreLabel;
    private final JLabel penaltiesLabel;
    private final JTable teamMatchesTable;
    private final JLabel matchSelectionTitleLabel;
   
    
    // Panels for match scouting 
    private final JPanel IdPanel;
    private final JPanel panel1;
    private final JPanel panel2;
    private final JPanel panel3;
    private final JPanel panel4;
    private final JPanel autonomousPanel;
    private final JPanel driveTrainPanel;
    private final JPanel robotActivityPanel;
    private final JPanel outcomePanel;
    private final JPanel intakeReleasePanel;
    private final JPanel intakePanel;
    private final JPanel releasePanel;
    private final JPanel catchPanel;
    private final JPanel pickupPanel;
    private final JPanel passPanel;
    private final JPanel shootPanel;
    private final JPanel commentsPanel;
    private final JPanel penaltiesPanel;
    private final JPanel matchNumberPanel;
    private final JPanel colorPanel;
    private final JPanel scouterPanel;
    private final JPanel defensePanel;
    private final JPanel matchSelectionTitlePanel;
    private final JPanel catchInfoPanel;
    private final JPanel passInfoPanel;
    private final JPanel shootInfoPanel;    
    
    public MatchFormSelection(int teamNumber) throws IOException
        {
            super("Match Forms for Team " + teamNumber);
        this.mouseClickListener = new MouseListener()
        {
            
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int clickedMatchNumber;
                Point p = e.getPoint();
                int matchRow = teamMatchesTable.rowAtPoint(p);
                int matchColumn = teamMatchesTable.columnAtPoint(p);
                if ((teamMatchesTable.getSelectedColumn() != matchColumn ||
                        teamMatchesTable.getSelectedRow() != matchRow) && matchColumn != 0)
                    teamMatchesTable.changeSelection(matchRow, matchColumn, true, false);
                if (teamMatchesTable.getValueAt(matchRow, matchColumn) != null)
                {
                    clickedMatchNumber =  (Integer) teamMatchesTable.getValueAt(matchRow, matchColumn);
                    setMatchInfo(getArrayIndex(matchTeamNumber, clickedMatchNumber));
                }
                if (matchColumn == 0)
                {
                    teamMatchesTable.clearSelection();
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e)
            {
                
            }
            
            @Override
            public void mouseReleased(MouseEvent e)
            {
                
            }
            
            @Override
            public void mouseEntered(MouseEvent e)
            {
                
            }
            
            
            @Override
            public void mouseExited(MouseEvent e){
                teamMatchesTable.clearSelection();
            }
        };
            SaveData sdata = new SaveData();
            matchTeamNumber = teamNumber;
            teamMatchesTableItems = new int[getNumberOfTeamMatches(teamNumber)][1];
            int matches = 0;
        for(MatchFormData matchForm : Globals.saveData.matchForms) 
        {
            if (matchForm.teamNumber == teamNumber) {
                teamMatchesTableItems[matches][0] = matchForm.matchNumber;
                matches++;
            }
        }
                       
            mainPanel = new JPanel();
            titlePanel = new JPanel();
            informationPanel = new JPanel();
            matchSelectionPanel = new JPanel();
            

            //Components for Data Panel
            teamNumberLabel = new JLabel();
            
            
            //Make category panels
            IdPanel = new JPanel();
            panel1 = new JPanel();
            panel2 = new JPanel();
            panel3 = new JPanel();
            panel4 = new JPanel();
            autonomousPanel = new JPanel();
            driveTrainPanel = new JPanel();
            robotActivityPanel = new JPanel();
            outcomePanel = new JPanel();
            intakeReleasePanel = new JPanel();
            intakePanel = new JPanel();
            releasePanel = new JPanel();
            catchPanel = new JPanel();
            pickupPanel = new JPanel();
            passPanel = new JPanel();
            shootPanel = new JPanel();
            defensePanel = new JPanel();
            commentsPanel = new JPanel();
            penaltiesPanel = new JPanel();
            matchNumberPanel = new JPanel();
            colorPanel = new JPanel();
            scouterPanel = new JPanel();
            matchSelectionTitlePanel = new JPanel();
            shootInfoPanel = new JPanel();
            passInfoPanel = new JPanel();
            catchInfoPanel = new JPanel();
 
            //Make nested panels
            scrollPane = new JScrollPane();
            matchSelectorScrollPane = new JScrollPane();
            mainPanel = new WatermarkPanel("Images/aerialassistlogo.png");
            title = new JLabel("Match Forms: Team "+ teamNumber);
            titlePanel = new JPanel();
            teamNumberLabel = new JLabel("Team Number: ");
            matchNumberLabel = new JLabel("");
            scouterNameLabel = new JLabel("");
            teamColorLabel  = new JLabel("");
            robotActiveLabel  = new JLabel("");
            commentsLabel  = new JLabel("");
            autonomousProgressionLabel = new JLabel("");
            driveTrainLabel = new JLabel("");
            catchScoreLabel = new JLabel("");
            catchOverTrussLabel = new JLabel("");
            catchNumberLabel = new JLabel("");
            pickupScoreLabel = new JLabel("");
            passScoreLabel = new JLabel("");
            passNumberLabel = new JLabel("");
            passOverTrussLabel = new JLabel("");
            shootScoreLabel = new JLabel("");
            shotsHighGoalLabel = new JLabel("");
            shotsLowGoalLabel = new JLabel("");
            defenseScoreLabel = new JLabel("");
            outcomeLabel = new JLabel("");
            finalScoreLabel = new JLabel("");
            penaltiesLabel = new JLabel("");
            matchSelectionTitleLabel = new JLabel("Match #");
            teamMatchesTable = new JTable(teamMatchesTableModel);
            teamMatchesTable.setShowHorizontalLines(false);
            teamMatchesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            teamMatchesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            teamMatchesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            teamMatchesTable.setShowVerticalLines(false);
            TableRowSorter mySorter = new TableRowSorter(teamMatchesTable.getModel());
            teamMatchesTable.setRowSorter(mySorter);
            JTableHeader header = new JTableHeader(teamMatchesTable.getColumnModel())
            {
                @Override
                public void setDraggedColumn(TableColumn tc)
                {
                    super.setDraggedColumn(null);
                }
            };
            
            teamMatchesTable.addMouseListener(mouseClickListener);

            //Set layouts
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
            titlePanel.setLayout(new BorderLayout());
            informationPanel.setLayout(new BoxLayout(informationPanel, BoxLayout.Y_AXIS));
            matchSelectionPanel.setLayout(new BoxLayout(matchSelectionPanel, BoxLayout.Y_AXIS));
            matchSelectionPanel.setMaximumSize(new Dimension(100,1000));
            matchSelectionPanel.setMinimumSize(new Dimension(100,1000));

           
            IdPanel.setLayout(new BoxLayout(IdPanel, BoxLayout.X_AXIS));
            panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
            panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
            panel3.setLayout(new BorderLayout());
            panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
            autonomousPanel.setLayout(new GridLayout(1,1));
            driveTrainPanel.setLayout(new GridLayout(1,1));
            robotActivityPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
            outcomePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
            intakeReleasePanel.setLayout(new GridLayout(1,2));
            intakePanel.setLayout(new GridLayout(2,1));
            releasePanel.setLayout(new GridLayout(2,1));
            catchPanel.setLayout(new GridLayout(0,2));
            pickupPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
            passPanel.setLayout(new GridLayout(0,2));
            shootPanel.setLayout(new GridLayout(0,2));
            commentsPanel.setLayout(new  BorderLayout());
            penaltiesPanel.setLayout(new  BorderLayout());
            matchNumberPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
            colorPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
            scouterPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
            defensePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
            matchNumberPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
            passInfoPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
            catchInfoPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
            shootInfoPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

            //Set borders
            marginBorder(titlePanel, 3, 3, 3, 3);
            scrollPane.setBorder(BorderFactory.createEtchedBorder());
            marginBorder(scrollPane, 0, 3, 3, 3);
            matchSelectorScrollPane.setBorder(BorderFactory.createEtchedBorder());
            marginBorder(matchSelectorScrollPane, 0, 3, 3, 3);
            mainPanel.setBorder(BorderFactory.createEtchedBorder());
            matchSelectionPanel.setBorder(BorderFactory.createEtchedBorder());
            informationPanel.setBorder(BorderFactory.createEtchedBorder());
            matchSelectionTitlePanel.setBorder(BorderFactory.createEtchedBorder());
            matchSelectionTitlePanel.setMaximumSize(new Dimension(100,30));
            matchSelectionTitlePanel.setMinimumSize(new Dimension(100,30));
            
            autonomousPanel.setBorder(BorderFactory.createTitledBorder("Autonomous Score"));
            driveTrainPanel.setBorder(BorderFactory.createTitledBorder("Manueverability"));
            robotActivityPanel.setBorder(BorderFactory.createTitledBorder("Robot Active"));
            outcomePanel.setBorder(BorderFactory.createTitledBorder("Outcome"));
            catchPanel.setBorder(BorderFactory.createTitledBorder("Catch"));
            pickupPanel.setBorder(BorderFactory.createTitledBorder("Pickup"));
            passPanel.setBorder(BorderFactory.createTitledBorder("Pass"));
            shootPanel.setBorder(BorderFactory.createTitledBorder("Shoot"));
            commentsPanel.setBorder(BorderFactory.createTitledBorder("Comments"));
            penaltiesPanel.setBorder(BorderFactory.createTitledBorder("Penalties"));
            matchNumberPanel.setBorder(BorderFactory.createTitledBorder("Match #"));
            colorPanel.setBorder(BorderFactory.createTitledBorder("Team Color"));
            scouterPanel.setBorder(BorderFactory.createTitledBorder("Scouter"));
            defensePanel.setBorder(BorderFactory.createTitledBorder("Defense"));
  
            //Initialize settings
            title.setFont(MainFrame.font);

            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            matchSelectorScrollPane.getVerticalScrollBar().setUnitIncrement(16);


            //Add components
            add(titlePanel, BorderLayout.NORTH);
            add(scrollPane);
            add(mainPanel);
            matchSelectionPanel.add(matchSelectionTitlePanel);
            mainPanel.add(matchSelectionPanel);
            matchSelectionTitlePanel.add(matchSelectionTitleLabel);
            mainPanel.add(informationPanel);
            informationPanel.add(IdPanel);
            informationPanel.add(panel1);
            informationPanel.add(panel2);
            informationPanel.add(panel3);
            informationPanel.add(panel4);
            IdPanel.add(matchNumberPanel);
            matchNumberPanel.add(matchNumberLabel);
            IdPanel.add(colorPanel);
            colorPanel.add(teamColorLabel);
            IdPanel.add(scouterPanel);
            scouterPanel.add(scouterNameLabel);
            panel1.add(robotActivityPanel);
            robotActivityPanel.add(robotActiveLabel);
            panel1.add(outcomePanel);
            outcomePanel.add(outcomeLabel);
            panel2.add(autonomousPanel);
            panel2.add(driveTrainPanel);
            driveTrainPanel.add(driveTrainLabel);
            pickupPanel.add(pickupScoreLabel);
            passPanel.add(passScoreLabel);
            shootPanel.add(shootScoreLabel);
            shootPanel.add(shotsLowGoalLabel);
            shootPanel.add(shotsHighGoalLabel);
            passPanel.add(passNumberLabel);
            panel3.add(defensePanel,BorderLayout.SOUTH);
            defensePanel.add(defenseScoreLabel);
            panel3.add(intakeReleasePanel, BorderLayout.CENTER);
            intakeReleasePanel.add(intakePanel);
            intakeReleasePanel.add(releasePanel);
            intakePanel.add(catchPanel);      
            catchInfoPanel.add(catchScoreLabel);
            catchInfoPanel.add(catchOverTrussLabel);
            catchPanel.add(catchInfoPanel);
            catchPanel.add(catchNumberLabel);
            intakePanel.add(pickupPanel);
            releasePanel.add(passPanel);
            passPanel.add(passOverTrussLabel);
            releasePanel.add(shootPanel);
            panel4.add(commentsPanel);
            commentsPanel.add(commentsLabel);
            panel4.add(penaltiesPanel);
            penaltiesPanel.add(penaltiesLabel);         
            autonomousPanel.add(autonomousProgressionLabel);
            matchSelectionPanel.add(teamMatchesTable);
            titlePanel.add(title, BorderLayout.CENTER);

            //Set frame size and visibility
            Toolkit screenDementions = Toolkit.getDefaultToolkit();  
            setSize(new Dimension(1050,700));
            setMinimumSize(new Dimension(800, 0));
            setVisible(true);


    }
    
    public void setMatchInfo(int arrayIndex){
        teamNumberLabel.setText(""  + Globals.saveData.matchForms[arrayIndex].teamNumber);
        matchNumberLabel.setText("" + Globals.saveData.matchForms[arrayIndex].matchNumber);
        scouterNameLabel.setText("" + Globals.saveData.matchForms[arrayIndex].scouterName);
        if(Globals.saveData.matchForms[arrayIndex].teamColor == 0)
            teamColorLabel .setText("Red");
        else
        teamColorLabel .setText("Blue");
            switch(Globals.saveData.matchForms[arrayIndex].robotActive){
                case 0:
                     robotActiveLabel.setText("Yes");
                    break;
                case 1:
                     robotActiveLabel.setText("No");
                    break;
                case 2:
                     robotActiveLabel.setText("Partial");
                    break;
                default:
                    robotActiveLabel.setText("N/A");
            }
        commentsLabel .setText("<html>" + Globals.saveData.matchForms[arrayIndex].comments + "</html>");
        System.out.println("");
        autonomousProgressionLabel.setText(Globals.saveData.matchForms[arrayIndex].autonomousActions[0] + " + " + Globals.saveData.matchForms[arrayIndex].autonomousActions[1] + " + " + Globals.saveData.matchForms[arrayIndex].autonomousActions[2] + " = " + Globals.saveData.matchForms[arrayIndex].autonomousScore);
        switch(Globals.saveData.matchForms[arrayIndex].maneuverability){
            case 3:
                driveTrainLabel.setText("Quality:   Superb");
                break;
            case 1:
                 driveTrainLabel.setText("Quality:   Poor");
                break;
            case 2:
                 driveTrainLabel.setText("Quality:   Average");
                break;
            default:
                driveTrainLabel.setText("Quality:   N/A");         
        }        
                switch(Globals.saveData.matchForms[arrayIndex].catchMechanism){
            case 3:
                catchScoreLabel.setText("Quality:   Superb");
                break;
            case 1:
                 catchScoreLabel.setText("Quality:   Poor");
                break;
            case 2:
                 catchScoreLabel.setText("Quality:   Average");
                break;
            default:
                catchScoreLabel.setText("Quality:   N/A");         
        } 
        catchNumberLabel.setText("Number of catches = " + Globals.saveData.matchForms[arrayIndex].catchesCompletedCount);
        switch(Globals.saveData.matchForms[arrayIndex].pickupMechanism){
            case 3:
                pickupScoreLabel.setText("Quality:   Superb");
                break;
            case 1:
                 pickupScoreLabel.setText("Quality:   Poor");
                break;
            case 2:
                 pickupScoreLabel.setText("Quality:   Average");
                break;
            default:
                pickupScoreLabel.setText("Quality:   N/A");         
        }
        switch(Globals.saveData.matchForms[arrayIndex].passMechanism){
            case 3:
                passScoreLabel.setText("Quality:   Superb");
                break;
            case 1:
                 passScoreLabel.setText("Quality:   Poor");
                break;
            case 2:
                 passScoreLabel.setText("Quality:   Average");
                break;
            default:
                passScoreLabel.setText("Quality:   N/A");         
        }
        passNumberLabel.setText("Number of passes = " + Globals.saveData.matchForms[arrayIndex].accuratePassesCount);
        switch(Globals.saveData.matchForms[arrayIndex].shootMechanism){
            case 3:
                shootScoreLabel.setText("Quality:   Superb");
                break;
            case 1:
                 shootScoreLabel.setText("Quality:   Poor");
                break;
            case 2:
                 shootScoreLabel.setText("Quality:   Average");
                break;
            default:
                shootScoreLabel.setText("Quality:   N/A");         
        }
        shotsHighGoalLabel.setText("Number of high goal shots = " + Globals.saveData.matchForms[arrayIndex].successfulHighGoalShots);
        shotsLowGoalLabel.setText("Number of low goal shots = " + Globals.saveData.matchForms[arrayIndex].successfulLowGoalShots);
        if(Globals.saveData.matchForms[arrayIndex].canPassOverTruss)
             passOverTrussLabel.setText("<html><b>Can</b> pass over truss</html>");
        else
             passOverTrussLabel.setText("<html><b>Can't</b> pass over truss</html>");
        
        if(Globals.saveData.matchForms[arrayIndex].canCatchOverTruss)
            catchOverTrussLabel.setText("<html><b>Can</b> catch over truss</html>");
        else
            catchOverTrussLabel.setText("<html><b>Can't</b> pass over truss</html>");
        switch(Globals.saveData.matchForms[arrayIndex].defenseMechanism){
            case 3:
                defenseScoreLabel.setText("Quality:   Superb");
                break;
            case 1:
                 defenseScoreLabel.setText("Quality:   Poor");
                break;
            case 2:
                 defenseScoreLabel.setText("Quality:   Average");
                break;
            default:
                defenseScoreLabel.setText("Quality:   N/A");         
        }
                
        switch(Globals.saveData.matchForms[arrayIndex].outcome){
            case 0:
                 outcomeLabel.setText("Win");
                break;
            case 1:
                 outcomeLabel.setText("Lose");
                break;
            case 2:
                 outcomeLabel.setText("Tie");
                break;
            default:
                outcomeLabel.setText("N/A");
        }
        finalScoreLabel.setText("" + Globals.saveData.matchForms[arrayIndex].finalScore);
        penaltiesLabel.setText("<html>" + Globals.saveData.matchForms[arrayIndex].penalties + "</html>");
        
    }
    /**
     * Apply margin to the specified component
     * @param component
     * @param top
     * @param bottom
     * @param left
     * @param right 
     */
    public final void marginBorder(JComponent component, int top, int bottom, int left, int right)
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
        Border border = UIManager.getDefaults().getBorder("TitledBorder.border");
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
    
    
}

        









