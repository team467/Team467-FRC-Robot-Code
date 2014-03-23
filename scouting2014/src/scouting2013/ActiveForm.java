/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scouting2013;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author aidan
 */
public class ActiveForm extends JFrame
{
    //Components
    private final JScrollPane scrollPane;
    private final JScrollPane drivetrainScrollPane;
    private final JScrollPane issuesScrollPane;
    private final JLabel title;
    private final JTextField teamNumberArea;
    private final JTextField teamNameArea;
    private final JTextField scouterNameArea;
    private final JButton saveButton;
    private final JTextArea drivetrainTextArea;
    private final JTextArea issuesTextArea;
    
    //Nested panels
    private final JPanel titlePanel;
    private final JPanel identifierPanel;
    private final JPanel mainPanel;
    private final JPanel drivetrainPanel;
    private final JPanel issuesPanel;
    
    //Listeners
    
    /*
     * Save button listener which saves data when the save button is pressed
     */
    private final ActionListener saveButtonListener = new ActionListener() 
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            //Generate the new form
            ActiveFormData formData = getSaveData();
            
            //Save only if the new form was successfully created
            if (formData != null)
            {
                Globals.saveData.addActiveForm(formData);
                Globals.saveData.saveData();

                //Display popup window
                JOptionPane.showMessageDialog(MainFrame.form, "Form Saved", "Form Saved",
                        JOptionPane.PLAIN_MESSAGE);
                
                
                //Refresh
                Globals.mainFrame.refreshData();
            }
            
        }
    };
    
    /*
     * New active form creation
     */
    public ActiveForm(String teamNumber)
    {
        super("Active Form");
        
        //Make components
        scrollPane = new JScrollPane();
        title = new JLabel("Active Scouting");
        teamNumberArea = new JTextField(4);
        teamNameArea = new JTextField(20);
        scouterNameArea = new JTextField(10);
        saveButton = new JButton();
        drivetrainTextArea = new JTextArea();
        drivetrainScrollPane = new JScrollPane(drivetrainTextArea);
        issuesTextArea = new JTextArea();
        issuesScrollPane = new JScrollPane(issuesTextArea);
        
        //Make nested panels
        titlePanel = new JPanel();
        identifierPanel = new JPanel();
        mainPanel = new WatermarkPanel("Images/aerialassistlogo.png");
        drivetrainPanel = new JPanel();
        issuesPanel = new JPanel();
        
        //Set layouts
        titlePanel.setLayout(new BorderLayout());
        identifierPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        drivetrainPanel.setLayout(new BorderLayout());
        issuesPanel.setLayout(new BorderLayout());
        
        //Set borders
        marginBorder(titlePanel, 3, 3, 3, 3);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        marginBorder(scrollPane, 0, 3, 3, 3);
        identifierPanel.setBorder(BorderFactory.createEtchedBorder());
        drivetrainPanel.setBorder(createInsetTitledBorder("General Information"));
        issuesPanel.setBorder(createInsetTitledBorder("Issues"));
        
        //Initialize settings
        title.setFont(MainFrame.font);
        saveButton.setPreferredSize(new Dimension(60, 50));
        saveButton.setIcon(createImageIcon("Images/SaveIcon.png", "Save"));
        drivetrainTextArea.setWrapStyleWord(true);
        drivetrainTextArea.setLineWrap(true);
//        drivetrainTextArea.setPreferredSize(new Dimension(700, 50));
        issuesTextArea.setWrapStyleWord(true);
        issuesTextArea.setLineWrap(true);
//        issuesTextArea.setPreferredSize(new Dimension(700, 50));
        drivetrainPanel.setOpaque(false);
        issuesPanel.setOpaque(false);
        teamNumberArea.setText(teamNumber);
        drivetrainScrollPane.setViewportView(drivetrainTextArea);
        issuesScrollPane.setViewportView(issuesTextArea);

        
        //Add components
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane);
        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(saveButton, BorderLayout.EAST);
        titlePanel.add(identifierPanel, BorderLayout.SOUTH);
        identifierPanel.add(new JLabel("Team #:"));
        identifierPanel.add(teamNumberArea);
        identifierPanel.add(new JLabel("Team Name:"));
        identifierPanel.add(teamNameArea);
        identifierPanel.add(new JLabel("Scouter:"));
        identifierPanel.add(scouterNameArea);
        scrollPane.getViewport().add(mainPanel);
        mainPanel.add(drivetrainPanel);
        mainPanel.add(issuesPanel);
        drivetrainPanel.add(drivetrainTextArea);
        issuesPanel.add(issuesTextArea);
        
        //Add listeners
        saveButton.addActionListener(saveButtonListener);

        //Set frame size and visibility
        setSize(new Dimension(800, 660));
        setMinimumSize(new Dimension(800, 0));
        setVisible(true);
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
     * Get a ActiveFormData object containing all the data as it currently appears
     * on the active form. All the components are individually interpreted and the
     * data from them is stored in the MatchFormData.
     * @return An ActiveFormData object with the data as it currently is
     */
    private ActiveFormData getSaveData()
    {
        //Make blank match form data object
        ActiveFormData saveData = new ActiveFormData();

        //Send an error message if one of the three identifier fields is missing
        if (teamNumberArea.getText().equals("") || teamNameArea.getText().equals("")
                || scouterNameArea.getText().equals(""))
        {
            //Display popup window
            JOptionPane.showMessageDialog(MainFrame.form, "Missing Field", "Required field missing",
                                          JOptionPane.PLAIN_MESSAGE);
            return null;
        }
        
        //Identifiers
        saveData.teamNumber = Integer.parseInt(teamNumberArea.getText());
        saveData.teamName = teamNameArea.getText();
        saveData.scouterName = scouterNameArea.getText();
        
        //Data
        saveData.drivetrainText = drivetrainTextArea.getText();
        saveData.issuesText = issuesTextArea.getText();
        
        return saveData;
    }

    /**
     * Set the active form text to the text saved in data
     * @param saveData
     */
    public void setData(ActiveFormData saveData)
    {
        //Identifiers
        teamNumberArea.setText(Integer.toString(saveData.teamNumber));
        teamNameArea.setText(saveData.teamName);
        scouterNameArea.setText(saveData.scouterName);
        
        //Data
        drivetrainTextArea.setText(saveData.drivetrainText);
        issuesTextArea.setText(saveData.issuesText);
        
        repaint();
    }
}
