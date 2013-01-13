package edu.wpi.first.smartdashboard;

import java.io.*;

import javax.swing.*;

import edu.wpi.first.smartdashboard.extensions.*;
import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.*;
import edu.wpi.first.smartdashboard.robot.*;

/**
 * main SmartDashboard logic
 *
 * @author Joe Grinstead
 * @author pmalmsten
 */
public class main {
    
    /** Variable used in the {@link main#inCompetition() inCompetition()} method */
    private static boolean inCompetition = false;
    
    /**
     * Returns whether or not this is in "competition" mode. Competition mode
     * should be used on the netbook provided for teams to use the dashboard. If
     * the SmartDashboard is in competition mode, then it automatically sizes
     * itself to be the standard dashboard size and to remove the frame around
     * it. It can be set to be in competition if "competition" is one of the
     * words passed in through the command line.
     *
     * @return whether or not this is in "competition" mode
     */
    public static boolean inCompetition() {
        return inCompetition;
    }
    
    /**
     * Starts the program
     *
     * @param args
     *            the standard arguments. If "competition" is one of them, then
     *            the SmartDashboard will be in competition mode
     * @see main#inCompetition() inCompetition()
     */
    public static void main(final String[] args) {
        // Present a loading bar (it will only show up if this is going slowly)
        final ProgressMonitor monitor = new ProgressMonitor(null, "Loading SmartDashboard", "Initializing internal code...", 0, 1000);
        
        // Search the filesystem for extensions (49%)
        FileSniffer.findExtensions(monitor, 0, 490);
        
        ArgParser argParser = new ArgParser(args, true, true, new String[] { "ip" });
        inCompetition = argParser.hasFlag("competition");
        boolean customIP = false;
        if (argParser.hasValue("ip")) {
            Robot.setHost(argParser.getValue("ip"));
            customIP = true;
        }
        
        final boolean useTeamNumber = !customIP;
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                
                public void run() {
                    try {
                        
                        monitor.setProgress(500);
                        monitor.setNote("Setting Theme");
                        try {
                            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                    UIManager.setLookAndFeel(info.getClassName());
                                }
                            }
                        } catch (Exception e) {}
                        
                        // Initialize GUI
                        DashboardFrame frame = new DashboardFrame(inCompetition);
                        
                        monitor.setProgress(600);
                        monitor.setNote("Getting Team Number");
                        IntegerProperty team = frame.getPrefs().team;
                        while (team.getValue() <= 0) {
                            team.setValue(JOptionPane.showInputDialog("Input Team Number"));
                        }
                        if (useTeamNumber) {
                            Robot.setTeam(frame.getPrefs().team.getValue());
                        }
                        
                        frame.pack();
                        frame.setVisible(true);
                        
                        monitor.setProgress(750);
                        monitor.setNote("Loading From Save");
                        
                        // Load
                        File file = new File(frame.getPrefs().saveFile.getValue());
                        if (file.exists()) {
                            frame.load(file.getPath());
                        }
                        
                        monitor.setProgress(1000);
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        
                        System.exit(1);
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(2);
        }
    }
}