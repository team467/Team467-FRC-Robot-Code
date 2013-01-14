package edu.wpi.first.smartdashboard;

import edu.wpi.first.smartdashboard.extensions.FileSniffer;
import edu.wpi.first.smartdashboard.gui.DashboardFrame;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.smartdashboard.gui.DashboardPrefs;
import edu.wpi.first.smartdashboard.properties.IntegerProperty;
import edu.wpi.first.smartdashboard.types.DisplayElementRegistry;
import edu.wpi.first.wpilibj.networking.NetworkTable;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main SmartDashboard logic
 * @author Joe Grinstead
 * @author pmalmsten
 */
public class main {

    /** Variable used in the {@link main#inCompetition() inCompetition()} method */
    private static boolean inCompetition = false;

    /**
     * Returns whether or not this is in "competition" mode.
     * Competition mode should be used on the netbook provided for teams to use the dashboard.
     * If the SmartDashboard is in competition mode, then it automatically sizes itself to be
     * the standard dashboard size and to remove the frame around it.
     * It can be set to be in competition if "competition" is one of the words
     * passed in through the command line.
     * @return whether or not this is in "competition" mode
     */
    public static boolean inCompetition() {
        return inCompetition;
    }

    /**
     * Starts the program
     * @param args the standard arguments.  If "competition" is one of them, then the SmartDashboard will be in competition mode
     * @see main#inCompetition() inCompetition()
     */
    public static void main(final String[] args) {
        final Object lock = new Object();

        synchronized (lock) {
            // Present a loading bar (it will only show up if this is going slowly)
            final ProgressMonitor monitor = new ProgressMonitor(null, "Loading SmartDashboard", "Initializing internal code...", 0, 1000);

            // Initialize registry (1%)
            DisplayElementRegistry.initialize();
            monitor.setProgress(10);

            // Search the filesystem for extensions (49%)
            FileSniffer.findExtensions(monitor, 10, 490);

            for (String arg : args) {
                if (arg.equalsIgnoreCase("competition")) {
                    inCompetition = true;
                }
            }
            monitor.setProgress(500);
            monitor.setNote("Setting Theme");

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        try {
                            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                    UIManager.setLookAndFeel(info.getClassName());
                                }
                            }
                        } catch (Exception e) {
                        }

                        monitor.setProgress(600);
                        monitor.setNote("Getting Team Number");
                        IntegerProperty team = DashboardPrefs.getInstance().team;
                        while (team.getValue() <= 0) {
                            team.setValue(JOptionPane.showInputDialog("Input Team Number"));
                        }
                        NetworkTable.setTeam(DashboardPrefs.getInstance().team.getValue());
                        Robot.getTable();

                        monitor.setProgress(650);
                        monitor.setNote("Opening Window");
                        
                        // Initialize GUI
                        DashboardFrame.initialize(inCompetition);
                        DashboardFrame.getInstance().pack();
                        DashboardFrame.getInstance().setVisible(true);

                        monitor.setProgress(750);
                        monitor.setNote("Loading From Save");
                        
                        // Load
                        File file = new File(DashboardPrefs.getInstance().saveFile.getValue());
                        if (file.exists()) {
                            DashboardFrame.getInstance().load(file.getPath());
                        }

                        monitor.setProgress(900);
                        monitor.setNote("Connection To Robot");
                        DashboardFrame.getInstance().getPanel().beginListening();
                        monitor.setProgress(1000);

                        synchronized (lock) {
                            lock.notify();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                        System.exit(1);
                    }
                }
            });

            try {
                lock.wait();
            } catch (InterruptedException ex) {
                System.exit(2);
            }
        }
    }
}
