/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scouting2013;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.AttributedString;
import java.util.Comparator;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.CellEditorListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.*;
/**
 *
 * @author aidan
 */
public class MainFrame extends JFrame
{
    public int teamNumber = 0;
    //Constants
    private static final String[] TEAM_DATA_COLUMN_NAMES  = new String[] 
    {
        "Team #", "Defense Score", "Average Passes", "Average Catches", "Highest Score",
        "Average Score"
    };
    
    private static final String[] SCHEDULE_COLUMN_NAMES  = new String[] 
    {
        "Match #", "Red Team 1", "Red Team 2", "Red Team 3", "Blue Team 1", 
        "Blue Team 2", "Blue Team 3"
    };
    
    //Table items
    private String[][] teamDataTableItems = new String[0][6];
    private String[][] scheduleTableItems = new String[100][7];
    
    //Forms
    public static MatchForm form;
    public static MatchFormSelection formSelection;
    
    //Boolean indicating whether a server is running
    private boolean serverRunning = false;
    
    //Components
    private JLabel title;
    private JButton runServerButton;
    private JButton sendDataButton;
    private JButton newMatchFormButton;
    private JButton newActiveFormButton;
    private JButton refreshButton;
    private JButton clearDataButton;
    private JLabel ipLabel;
    private JTabbedPane mainTabbedPane;
    private JScrollPane teamDataScrollPane;
    private JScrollPane scheduleScrollPane;
    private JTable teamDataTable;
    private JTable scheduleTable;
    private JList activeTeamList;
    private JButton activeFormEditButton;
    private BorderedTextBox teamNumberBox;
    private BorderedTextBox teamWinsBox;
    private BorderedTextBox teamHighestScoreBox;
    private BorderedTextBox teamAverageScoreBox;
    private BorderedTextBox teamBestDistanceBox;
    private BorderedTextBox teamDefenseBox;
    
    //Main panels
    private JPanel titlePanel;
    private JPanel toolbarPanel;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private JPanel teamDataPanel;
    private JPanel schedulePanel;
    private JPanel activeFormsPanel;
    
    //Nested panels (for organization)
    
    //Listeners
    
    /*
     * Button listener that handles toolbar buttons
     */
    private ActionListener toolbarListener = new ActionListener() {

        
        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand().equals("Run Server"))
            {
                if (!serverRunning)
                {
                    new Thread(new Server()).start();
                    serverRunning = true;
                    
                    //Display current ip
                    try
                    {
                        ipLabel.setText("IP: " + InetAddress.getLocalHost().getHostAddress());
                    }
                    catch (UnknownHostException ex)
                    {
                        System.err.println("Not connected to any network");
                    }
                }
            }
            if (e.getActionCommand().equals("Send Data"))
            {
                try
                {
                    Client client = new Client();
                    String ip = (String)JOptionPane.showInputDialog(Globals.mainFrame,
                            "Enter Server IP",
                            "Server IP",
                            JOptionPane.PLAIN_MESSAGE,
                            null, 
                            null, 
                            Globals.saveData.serverIp);
                    //Remember last entered ip
                    Globals.saveData.serverIp = ip;
                    
                    //Connect and send data
                    client.connect(ip);
                    client.sendFile("Save Data/Save-Master.ser");
                    client.closeConnections();
                    
                    //Successful send message
                    JOptionPane.showMessageDialog(Globals.mainFrame, 
                            "Data successfully sent", 
                            "Success",
                            JOptionPane.PLAIN_MESSAGE);
                    
                    //Save
                    Globals.saveData.saveData();
                }
                catch (IOException ex)
                {
                    //Failed send message
                    JOptionPane.showMessageDialog(Globals.mainFrame, 
                            "Data send to " + Globals.saveData.serverIp + " failed", 
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            if (e.getActionCommand().equals("New Match Form"))
            {
                try {
                    MatchForm form = new MatchForm("", "");
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (e.getActionCommand().equals("New Active Form"))
            {
                ActiveForm form = new ActiveForm("");
            }
            if (e.getActionCommand().equals("Refresh"))
            {
                SaveData.syncData();
                refreshData();
                Globals.saveData.saveData();
            }
            if (e.getActionCommand().equals("Clear All Data"))
            {
                //File savedData = new File("Save Data/Save-Master.ser");
                
                String input = JOptionPane.showInputDialog(mainPanel, "Please enter the password: ");
                if (input.toLowerCase().equals("llama font for life")){
                    //savedData.delete();
                     File directory = new File("Save Data");
                     // Get all files in directory
                     File[] files = directory.listFiles();
                     for (File file : files)
                     {
                        // Delete each file
 
                        if (!file.delete())
                        {
                            // Failed to delete file
 
                            System.out.println("Failed to delete "+file);
                        }
                     } 
                    System.out.println("Data has been deleted.");
                }
                else{
                    System.out.println("Incorrect password.");
                }
            }
        }
    };
    
    private MouseMotionListener mouseListener = new MouseMotionListener() 
    {

        @Override
        public void mouseDragged(MouseEvent e)
        {}
        
        
        public void mouseClicked(MouseEvent e)
        {
           
        }
        @Override
        public void mouseMoved(MouseEvent e)
        {
//            //Get mouse point
//            Point p = e.getPoint();
//            
//            //Get row and column at specified point
////            int teamDataRow = teamDataTable.rowAtPoint(p);
////            int teamDataColumn = teamDataTable.columnAtPoint(p);
//            int scheduleRow = scheduleTable.rowAtPoint(p);
//            int scheduleColumn = scheduleTable.columnAtPoint(p);
//            
//            //Change the selection if it is different and don't allow selection of first column
////            if ((teamDataTable.getSelectedColumn() != teamDataColumn ||
////                teamDataTable.getSelectedRow() != teamDataRow) && teamDataColumn != 0)
////            teamDataTable.changeSelection(teamDataRow, teamDataColumn, true, false);
////            if (teamDataTable.getValueAt(teamDataRow, teamDataColumn) != null)
////            {
////                int teamNumber = Integer.parseInt((String)teamDataTable.getValueAt(teamDataRow, teamDataColumn));
////                setTeamInfo(teamNumber);
////            }
//            
//                        
//            if ((scheduleTable.getSelectedColumn() != scheduleColumn ||
//                scheduleTable.getSelectedRow() != scheduleRow) && scheduleColumn != 0)
//            scheduleTable.changeSelection(scheduleRow, scheduleColumn, true, false);
//            if (scheduleTable.getValueAt(scheduleRow, scheduleColumn) != null)
//            {
//                int teamNumber = Integer.parseInt((String)scheduleTable.getValueAt(scheduleRow, scheduleColumn));
//                setTeamInfo(teamNumber);
//            }
//            
//            //Unselect if in first column
////            if (teamDataColumn == 0)
////            {
////                teamDataTable.clearSelection();
////            }
//            
//            if (scheduleColumn == 0)
//            {
//                scheduleTable.clearSelection();
//            }
        }
    };
    
    private MouseListener mouseClickListener = new MouseListener()
    {

        @Override
        public void mouseClicked(MouseEvent e)
        {
            teamNumber = 0;
            Point p = e.getPoint();
            int teamDataRow = teamDataTable.rowAtPoint(p);
            int teamDataColumn = teamDataTable.columnAtPoint(p);  
            if ((teamDataTable.getSelectedColumn() != teamDataColumn ||
                teamDataTable.getSelectedRow() != teamDataRow) && teamDataColumn != 0)
            teamDataTable.changeSelection(teamDataRow, teamDataColumn, true, false);
            if (teamDataTable.getValueAt(teamDataRow, teamDataColumn) != null)
            {
                 teamNumber = Integer.parseInt((String)teamDataTable.getValueAt(teamDataRow, 0));
                 setTeamInfo(teamNumber);
            }
               try {
                    MatchFormSelection form = new MatchFormSelection(teamNumber);
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            if (teamDataColumn == 0)
            {
                teamDataTable.clearSelection();
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
        public void mouseExited(MouseEvent e)
        {
            teamDataTable.clearSelection();
        }
        
    };
    
    private ActionListener activeEditButtonListener = new ActionListener()
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand().equals("Edit"))
            {
                //Get selected team number
                int teamNumber = Integer.parseInt((String)activeTeamList.getSelectedValue());
                
                //Get active form
                FormattedTeamData data = Globals.saveData.getFormattedDataByTeam(teamNumber);
                ActiveFormData editForm = data.activeForm;
                
                if (editForm != null)
                {
                    (new ActiveForm("")).setData(editForm);
                }
                else
                {
                    new ActiveForm(Integer.toString(teamNumber));
                }
                
            }
        }
        
    };
    
    /*
     * Window listener that stops the program when the window is closed
     */
    private WindowListener windowListener = new WindowListener() {

        /**
         * Window closing code
         */
        public void windowClosing(WindowEvent we)
        {
            System.exit(0);
        }

        public void windowOpened(WindowEvent we){}
        public void windowClosed(WindowEvent we){}
        public void windowIconified(WindowEvent we){}
        public void windowDeiconified(WindowEvent we){}
        public void windowActivated(WindowEvent we){}
        public void windowDeactivated(WindowEvent we){}
    };
    
    //Table models
    
    /*
     * Data model for team data table
     */
    private TableModel teamDataTableModel = new AbstractTableModel()
    {

        @Override
        public String getColumnName(int i)
        {
            return TEAM_DATA_COLUMN_NAMES[i];
        }

        public boolean isCellEditable(int row, int column)
        {
            return false;
        }

        @Override
        public int getRowCount()
        {
            return teamDataTableItems.length;
        }

        @Override
        public int getColumnCount()
        {
            return TEAM_DATA_COLUMN_NAMES.length;
        }

        @Override
        public Object getValueAt(int row, int column)
        {
            return teamDataTableItems[row][column];
        }
    };
    
    /*
     * Data model for schedule table
     */
    private TableModel scheduleTableModel = new AbstractTableModel()
    {

        @Override
        public String getColumnName(int i)
        {
            return SCHEDULE_COLUMN_NAMES[i];
        }

        public boolean isCellEditable(int row, int column)
        {
            //Dont allow editing of match number column
            if (column == 0)
            {
                return false;
            }
            return true;
        }

        @Override
        public int getRowCount()
        {
            return scheduleTableItems.length;
        }

        @Override
        public int getColumnCount()
        {
            return SCHEDULE_COLUMN_NAMES.length;
        }

        @Override
        public Object getValueAt(int row, int column)
        {
            return scheduleTableItems[row][column];
        }
        
        @Override
        public void setValueAt(Object value, int row, int col) 
        {
            scheduleTableItems[row][col] = (String)value;
        }
    };
    
    //Font

    public static Font font = loadFont("Fonts/Fine College.ttf").deriveFont(60f);


    public static Font generalTextFont = loadFont("Fonts/Sansation_Regular.ttf").deriveFont(14f);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Globals.mainFrame = new MainFrame();
        Globals.saveData = SaveData.readData();
        //Globals.mainFrame.refreshData();
        Globals.mainFrame.scheduleTableItems = Globals.saveData.scheduleTableItems;
        //Add match numbers to schedule table
        for (int i = 0; i < Globals.mainFrame.scheduleTableItems.length; i++)
        {
            Globals.mainFrame.scheduleTableItems[i][0] = Integer.toString(i + 1);
        }
    }
    
    /**
     * Makes a new main scouting frame
     */
    public MainFrame()
    {
        super("Team 467 Match Scouting");
        
        //Set nimbus look and feel
        setNimbusLook();
        
        //Make components
        title = new JLabel("Team 467 Scouting");
        runServerButton = new JButton("Run Server");
        sendDataButton = new JButton("Send Data");
        newMatchFormButton = new JButton("New Match Form");
        newActiveFormButton = new JButton("New Active Form");
        refreshButton = new JButton("Refresh");
        clearDataButton = new JButton("Clear All Data");
        ipLabel = new JLabel();
        mainTabbedPane = new JTabbedPane();
        teamDataScrollPane = new JScrollPane();
        scheduleScrollPane = new JScrollPane();
        teamDataTable = new JTable(teamDataTableModel);
        scheduleTable = new JTable(scheduleTableModel);
        activeTeamList = new JList();
        activeFormEditButton = new JButton("Edit");
        teamNumberBox = new BorderedTextBox("Team", "");
        teamWinsBox = new BorderedTextBox("Wins", "");
        teamHighestScoreBox = new BorderedTextBox("Highest Score", "");
        teamAverageScoreBox = new BorderedTextBox("Average Score", "");
        teamDefenseBox = new BorderedTextBox("Defense", "");
        
        //Make main panels
        titlePanel = new JPanel();
        toolbarPanel = new JPanel();
        mainPanel = new JPanel();
        infoPanel = new JPanel();
        teamDataPanel = new JPanel();
        schedulePanel = new JPanel();
        activeFormsPanel = new JPanel();
        
        //Make nested panels
        
        //Set layouts
        titlePanel.setLayout(new BorderLayout());
        toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        mainPanel.setLayout(new BorderLayout());
        teamDataPanel.setLayout(new BorderLayout());
        schedulePanel.setLayout(new BorderLayout());
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        activeFormsPanel.setLayout(new BorderLayout());
        
        //Set borders
        marginBorder(titlePanel, 3, 3, 3, 3);
        toolbarPanel.setBorder(BorderFactory.createEtchedBorder());
        mainPanel.setBorder(BorderFactory.createEtchedBorder());
        marginBorder(mainPanel, 0, 3, 3, 3);
        infoPanel.setBorder(BorderFactory.createEtchedBorder());
        marginBorder(infoPanel, 0, 3, 3, 0);
        
        //Initialize settings
        infoPanel.setPreferredSize(new Dimension(200, 0));
        title.setFont(font);
        teamDataTable.setShowHorizontalLines(true);
        teamDataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teamDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        teamDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        teamDataTable.setShowVerticalLines(true);
        TableRowSorter mySorter = new TableRowSorter(teamDataTable.getModel());
        teamDataTable.setRowSorter(mySorter);
        JTableHeader header = new JTableHeader(teamDataTable.getColumnModel())
        {
            public void setDraggedColumn(TableColumn tc)
            {
                super.setDraggedColumn(null);
            }
        };
        scheduleTable.setTableHeader(header);
        mySorter.setComparator(0, new NumericComparator());
        mySorter.setComparator(1, new NumericComparator());
        mySorter.setComparator(2, new NumericComparator());
        mySorter.setComparator(3, new NumericComparator());
        mySorter.setComparator(4, new NumericComparator());
        //Author Tyler Cote
        mySorter.setComparator(5, new NumericComparator());
        //
        scheduleTable.setCellSelectionEnabled(true);
        scheduleTable.setShowHorizontalLines(true);
        scheduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scheduleTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scheduleTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        scheduleTable.setShowVerticalLines(true);
        header = new JTableHeader(scheduleTable.getColumnModel())
        {
            public void setDraggedColumn(TableColumn tc)
            {
                super.setDraggedColumn(null);
            }
        };
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        scheduleTable.setTableHeader(header);
        scheduleTable.setCellSelectionEnabled(true);
        ColoredCellRenderer whiteRenderer = new ColoredCellRenderer(new Color(255, 255, 255), DefaultTableCellRenderer.CENTER);
        ColoredCellRenderer redRenderer = new ColoredCellRenderer(new Color(255, 128, 128), DefaultTableCellRenderer.CENTER);
        ColoredCellRenderer blueRenderer = new ColoredCellRenderer(new Color(128, 128, 255), DefaultTableCellRenderer.CENTER);
        scheduleTable.getColumnModel().getColumn(0).setCellRenderer(whiteRenderer);
        scheduleTable.getColumnModel().getColumn(1).setCellRenderer(redRenderer);
        scheduleTable.getColumnModel().getColumn(2).setCellRenderer(redRenderer);
        scheduleTable.getColumnModel().getColumn(3).setCellRenderer(redRenderer);
        scheduleTable.getColumnModel().getColumn(4).setCellRenderer(blueRenderer);
        scheduleTable.getColumnModel().getColumn(5).setCellRenderer(blueRenderer);
        scheduleTable.getColumnModel().getColumn(6).setCellRenderer(blueRenderer);
        activeTeamList.setBorder(BorderFactory.createEtchedBorder());
                
        //Add components
        add(titlePanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.WEST);
        add(mainPanel);
        titlePanel.add(toolbarPanel, BorderLayout.SOUTH);
        titlePanel.add(title);
        toolbarPanel.add(runServerButton);
        toolbarPanel.add(sendDataButton);
        toolbarPanel.add(newMatchFormButton);
        toolbarPanel.add(newActiveFormButton);
        toolbarPanel.add(refreshButton);
        toolbarPanel.add(clearDataButton);
        toolbarPanel.add(ipLabel);
        mainPanel.add(mainTabbedPane);
        mainTabbedPane.addTab("Team Data", teamDataPanel);
        mainTabbedPane.addTab("Schedule", schedulePanel);
        mainTabbedPane.addTab("Active Forms", activeFormsPanel);
        teamDataPanel.add(teamDataScrollPane, BorderLayout.CENTER);
        schedulePanel.add(scheduleScrollPane, BorderLayout.CENTER);
        teamDataScrollPane.getViewport().add(teamDataTable);
        scheduleScrollPane.getViewport().add(scheduleTable);
        activeFormsPanel.add(activeTeamList, BorderLayout.WEST);
        activeFormsPanel.add(activeFormEditButton, BorderLayout.NORTH);
        infoPanel.add(teamNumberBox);
        infoPanel.add(teamWinsBox);
        infoPanel.add(teamHighestScoreBox);
        infoPanel.add(teamAverageScoreBox);
        infoPanel.add(teamDefenseBox);
        
        //Add listeners
        addWindowListener(windowListener);
        runServerButton.addActionListener(toolbarListener);
        sendDataButton.addActionListener(toolbarListener);
        newMatchFormButton.addActionListener(toolbarListener);
        newActiveFormButton.addActionListener(toolbarListener);
        refreshButton.addActionListener(toolbarListener);
        clearDataButton.addActionListener(toolbarListener);
        scheduleTable.addMouseMotionListener(mouseListener);
        scheduleTable.addMouseListener(mouseClickListener);
        teamDataTable.addMouseMotionListener(mouseListener);
        teamDataTable.addMouseListener(mouseClickListener);
        activeFormEditButton.addActionListener(activeEditButtonListener);
        
        //Set frame size and visibility
        setSize(new Dimension(1000, 660));
        setMinimumSize(new Dimension(1000, 0));
        setVisible(true);
        
    }
    
    /**
     * Refreshes the table to display all currently saved data
     */
    public void refreshData()
    {
        Globals.saveData.organizeForms();
        teamDataTableItems = new String[Globals.saveData.getNumberFormattedForms()][TEAM_DATA_COLUMN_NAMES.length];
        for (int i = 0; i < Globals.saveData.getNumberFormattedForms(); i++)
        {
            FormattedTeamData data = Globals.saveData.formattedTeamForms.get(i);
            data.formatData();
            teamDataTableItems[i][0] = Integer.toString(data.getTeamNumber());
            teamDataTableItems[i][1] = Double.toString(data.defenseScore);
            teamDataTableItems[i][2] = Double.toString(data.averagePasses);
            teamDataTableItems[i][3] = Double.toString(data.averageCatches);
            teamDataTableItems[i][4] = Integer.toString(data.highestScore);
            teamDataTableItems[i][5] = Double.toString(data.averageScore);
        }
        teamDataTable.createDefaultColumnsFromModel();
        String[] teams = new String[Globals.saveData.formattedTeamForms.size()];
        for (int i = 0; i < teams.length; i++)
        {
            teams[i] = Integer.toString(Globals.saveData.formattedTeamForms.get(i).getTeamNumber());
        }
        activeTeamList.setListData(teams);
        this.validate();
        repaint();
    }
    
    /**
     * Set the team info data box to the data for the given team
     * @param team 
     */
    private void setTeamInfo(int team)
    {
        //Get the teams info
        FormattedTeamData form = Globals.saveData.getFormattedDataByTeam(team);
        
        if (form != null)
        {
            //Set all data boxes
            teamNumberBox.setText("# " + form.getTeamNumber());
            teamWinsBox.setText(form.wins + " / " + form.matchForms.size());
            teamHighestScoreBox.setText(form.highestScore + " Points");
            teamAverageScoreBox.setText(form.averageScore + " Points");
            teamDefenseBox.setText(Double.toString(form.defenseScore));
        }
    }
    
    /**
     * Sets the nimbus look and feel for the window
     */
    private void setNimbusLook()
    {
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(new NimbusLookAndFeel(){
                        
                        @Override
                        public UIDefaults getDefaults() {
                            UIDefaults ret = super.getDefaults();
                            ret.put("defaultFont", generalTextFont);
                            return ret;
                        }
                    });
                    break;
                }
            }
        }
//        catch (ClassNotFoundException ex)
//        {
//            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        catch (InstantiationException ex)
//        {
//            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        catch (IllegalAccessException ex)
//        {
//            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Load a font from the fonts folder with the given name
     * @param name
     * @return 
     */
    public static Font loadFont(String name)
    {
        InputStream is = MainFrame.class.getResourceAsStream(name);
        Font font = null;
        try
        {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            System.out.println("Loaded font from: " + MainFrame.class.getResource(name).getPath());
        }
        catch (FontFormatException ex)
        {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            
        }
        return font;
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
        Border empty = new EmptyBorder(top, left, bottom, right);
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
     * This is an extension of the cell renderer that allows a specific color to
     * be set for a given cell
     */
    private class ColoredCellRenderer extends DefaultTableCellRenderer
    {
        //Cell color
        private Color color;
        
        /**
         * Creates a new colored cell renderer with the given color
         * @param c The background color for the cell renderer
         * @param textAlign Horizontal alignment of the text in this cell
         */
        public ColoredCellRenderer(Color c, int textAlign)
        {
            super();
            
            color = c;
            setHorizontalAlignment(textAlign);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected)
            {
                c.setBackground(color);
            }
            return c;
        }
    }
    
    /**
     * A simple class which includes a titled border with text inside it
     */
    private class BorderedTextBox extends JPanel
    {   
        //Components
        private JLabel boxText;
        
        /**
         * Create a new bordered text box with the given border title and internal text
         * @param title
         * @param text 
         */
        public BorderedTextBox(String title, String text)
        {
            //Create components
            boxText = new JLabel(text);
            
            //Apply borders
            setBorder(createEtchedTitledBorder(title));
            
            //Set layout
            setLayout(new BorderLayout());
            
            //Initialize settings
            boxText.setFont(new Font("Arial", Font.BOLD, 24));
            boxText.setHorizontalAlignment(SwingConstants.CENTER);
            
            //Add components
            add(boxText);
        }
        
        /**
         * Set the text in this box to the given string
         * @param text 
         */
        public void setText(String text)
        {
            boxText.setText(text);
        }
        
        /**
         * Generates an etched border with a title in the top left hand corner
         *
         * @param title The title to give the border
         * @return
         */
        private Border createEtchedTitledBorder(String title)
        {
            Border border = BorderFactory.createTitledBorder(new EtchedBorder(), title, TitledBorder.LEFT, TitledBorder.TOP);
            return border;
        }
    }
    /**
     * Class to compare a fraction that is given as a string (ex. 7/8)
     */
    class FractionComparator implements Comparator
    {  
        /**
         * Creates a new fraction comparator
         * @param strings 
         */
        public FractionComparator()
        {
            
        }

        @Override
        public int compare(Object t, Object t1)
        {   
            //Get values as strings
            String str1 = (String)t;
            String str2 = (String)t1;
            
            //Split up the string
            String[] comps1 = str1.split(" / ");
            String[] comps2 = str2.split(" / ");
            
            //Get decimal value of fractions
            double num1 = Double.parseDouble(comps1[0]) / Double.parseDouble(comps1[1]);
            double num2 = Double.parseDouble(comps2[0]) / Double.parseDouble(comps2[1]);
            
            //Deal with 0's in denominator
            if (Double.parseDouble(comps1[1]) == 0.0)
            {
                num1 = 0.0;
            }
            if (Double.parseDouble(comps2[1]) == 0.0)
            {
                num2 = 0.0;
            }
            
            //Special handler if both numbers equal 0
            if (num1 == 0.0 || num2 == 0.0)
            {
                //If first number is greater
                if (Double.parseDouble(comps1[1]) > Double.parseDouble(comps2[1]))
                {
                    return -1;
                }
                
                //If second number is greater
                if (Double.parseDouble(comps1[1]) < Double.parseDouble(comps2[1]))
                {
                    return 1;
                }
            }
            
            //If first number is greater
            if (num1 > num2)
            {
                return -1;
            }
            
            //If second number is greater
            if (num1 < num2)
            {
                return 1;
            }
            
            return 0;

        }
        
    }
    
    /**
     * Comparator to numerically organize a group of integers
     */
    class NumericComparator implements Comparator
    {

        public int compare(Object t, Object t1)
        {
            String s1 = (String)t;
            String s2 = (String)t1;
            if (s1.endsWith("%") && s2.endsWith("%"))
            {
                s1 = s1.replaceAll("%", "");
                s2 = s2.replaceAll("%", "");
            }
            int returnVal = 0;
            double i1 = Double.parseDouble(s1);
            double i2 = Double.parseDouble(s2);
            
            //First integer is greater than the second
            if (i1 > i2)
            {
                returnVal = -1;
            }

            //Second integer is greater than the first
            if (i1 < i2)
            {
                returnVal = 1;
            }
            return returnVal;
        }
        
    }  
    
}
