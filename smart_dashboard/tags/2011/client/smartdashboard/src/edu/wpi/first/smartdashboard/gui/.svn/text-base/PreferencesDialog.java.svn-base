package edu.wpi.first.smartdashboard.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class PreferencesDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    JCheckBox chckbxAutocreateDashboardUi;
    JCheckBox chckbxShowCameraVideo;
    JCheckBox chckbxDebugVideoErrors;
    JFormattedTextField fFieldCameraVideoTeamNumber;
    JCheckBox chckbxLogToCSV;
    JButton btnChooseCSVFilePath;
    JFileChooser csvFilePathChooser;
    String csvFilePath;
    
    /**
     * Create the dialog.
     */
    public PreferencesDialog(JFrame frame) {
	
	super(frame, true);
        csvFilePathChooser = new JFileChooser();
        csvFilePath = null;

	setBounds(100, 100, 550, 300);
	getContentPane().setLayout(new BorderLayout());
	getContentPane().add(contentPanel, BorderLayout.NORTH);
	contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
        JPanel inputPanel = new JPanel(new GridLayout(0, 1));
	{
            JLabel autocreateDashboardUiLabel = new JLabel("Automatically add widgets to the dashboard");
	    chckbxAutocreateDashboardUi = new JCheckBox(); 
            autocreateDashboardUiLabel.setLabelFor(chckbxAutocreateDashboardUi);
	    //labelPanel.add(autocreateDashboardUiLabel);
            //inputPanel.add(chckbxAutocreateDashboardUi);
	}
	{
            
	    chckbxShowCameraVideo = new JCheckBox();
            JLabel showCameraVideoLabel = new JLabel("Show camera video");
            showCameraVideoLabel.setLabelFor(chckbxShowCameraVideo);
            labelPanel.add(showCameraVideoLabel);
	    inputPanel.add(chckbxShowCameraVideo);
	}
        {
            fFieldCameraVideoTeamNumber = new JFormattedTextField(NumberFormat.getInstance());
            JLabel cameraVideoTeamNumberLabel = new JLabel("Team number (must be non-zero for camera video)");
            cameraVideoTeamNumberLabel.setLabelFor(fFieldCameraVideoTeamNumber);
            fFieldCameraVideoTeamNumber.setColumns(4);
            labelPanel.add(cameraVideoTeamNumberLabel);
            inputPanel.add(fFieldCameraVideoTeamNumber);
        }
        {
            JLabel debugVideoErrorsLabel = new JLabel("Show video connection problems");
            debugVideoErrorsLabel.setToolTipText("When disabled, errors encountered when establishing\n"
                                                + " a video steam will be ignored until an initial\n"
                                                + " connection is established. Otherwise, video\n"
                                                + " connection errors will be immediately displayed\n"
                                                + " and no further connection attempts will be made.");
	    chckbxDebugVideoErrors = new JCheckBox();
            debugVideoErrorsLabel.setLabelFor(chckbxDebugVideoErrors);
	    labelPanel.add(debugVideoErrorsLabel);
            inputPanel.add(chckbxDebugVideoErrors);
	}
        {
	    chckbxLogToCSV = new JCheckBox();
            JLabel logToCSVLabel = new JLabel("Enable logging to CSV");
            logToCSVLabel.setLabelFor(chckbxLogToCSV);
            labelPanel.add(logToCSVLabel);
	    inputPanel.add(chckbxLogToCSV);
	}
        {
            btnChooseCSVFilePath = new JButton("Browse");

            btnChooseCSVFilePath.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int retCode = csvFilePathChooser.showOpenDialog(PreferencesDialog.this);

                    if(retCode == JFileChooser.APPROVE_OPTION) {
                        csvFilePath = csvFilePathChooser.getSelectedFile().getAbsolutePath();
                    }
                }
            });
            JLabel chooseCSVFilePathLabel = new JLabel("Choose a CSV file path");
            chooseCSVFilePathLabel.setLabelFor(btnChooseCSVFilePath);
            labelPanel.add(chooseCSVFilePathLabel);
            inputPanel.add(btnChooseCSVFilePath);
        }
        {
            contentPanel.add(labelPanel, BorderLayout.CENTER);
            contentPanel.add(inputPanel, BorderLayout.LINE_END);
        }
	{
	    JPanel buttonPane = new JPanel();
	    buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
	    getContentPane().add(buttonPane, BorderLayout.SOUTH);
	    {
		JButton okButton = new JButton("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent ae) {
                        if(chckbxShowCameraVideo.isSelected() &&
                           ((Number) fFieldCameraVideoTeamNumber.getValue()).intValue() < 1) {
                            JOptionPane.showMessageDialog(PreferencesDialog.this,
                                                          "A valid team number (greater than zero)"
                                                          + " must be provided when showing camera video.",
                                                          "Invalid Team Number",
                                                          JOptionPane.ERROR_MESSAGE);
                            chckbxShowCameraVideo.setSelected(false);
                            return;
                        }

                        if(chckbxLogToCSV.isSelected() &&
                           csvFilePath == null) {
                           JOptionPane.showMessageDialog(PreferencesDialog.this,
                                                          "Please select a target CSV file"
                                                          + " before continuing.",
                                                          "CSV File Not Specified",
                                                          JOptionPane.ERROR_MESSAGE);
                           return;
                        }

                        final DashboardPrefs prefs = DashboardPrefs.getInstance();
			//prefs.setAutoCreate(chckbxAutocreateDashboardUi.isSelected());
                        prefs.setCameraVideoTeamNumber(((Number) fFieldCameraVideoTeamNumber.getValue()).intValue());
			prefs.setShowCameraVideo(chckbxShowCameraVideo.isSelected());
                        prefs.setDebugVideoErrors(chckbxDebugVideoErrors.isSelected());
                        prefs.setLogToCSVEnabled(chckbxLogToCSV.isSelected());
                        prefs.setLogToCSVFilePath(csvFilePath);
                        
			PreferencesDialog.this.setVisible(false);
		    }
		});
	    }
	    {
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent ae) {
			PreferencesDialog.this.setVisible(false);
		    }
		});
		buttonPane.add(cancelButton);
	    }
	}
    }

    public void updateFromPreferences() {
        final DashboardPrefs prefs = DashboardPrefs.getInstance();
        
        chckbxAutocreateDashboardUi.setSelected(prefs.getAutoCreate());
        chckbxShowCameraVideo.setSelected(prefs.getShowCameraVideo());
        fFieldCameraVideoTeamNumber.setValue(prefs.getCameraVideoTeamNumber());
        chckbxDebugVideoErrors.setSelected(prefs.getDebugVideoErrors());
        chckbxLogToCSV.setSelected(prefs.getLogToCSVEnabled());
        csvFilePath = prefs.getLogToCSVFilePath();

        if(csvFilePath != null)
            csvFilePathChooser.setSelectedFile(new File(csvFilePath));
    }
}
