/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.smartdashboard.test;

import edu.wpi.first.smartdashboard.gui.AbsoluteWidgetPanelLayout;
import edu.wpi.first.smartdashboard.gui.AbstractWidgetRegistry;
import edu.wpi.first.smartdashboard.gui.SimpleValueWidgetFactory;
import edu.wpi.first.smartdashboard.gui.TableWidgetPanel;
import edu.wpi.first.smartdashboard.robot.Robot;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Mitchell Wills
 */
public class TestFrame extends JFrame {

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    new TestFrame().setVisible(true);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public TestFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Robot.setHost("localhost");
        AbstractWidgetRegistry widgetRegistry = new AbstractWidgetRegistry();
        widgetRegistry.addValueWidget(new SimpleValueWidgetFactory(BooleanBox.class));
        widgetRegistry.addValueWidget(UnkownValue.FACTORY);
        add(new TableWidgetPanel(Robot.getTable(), new AbsoluteWidgetPanelLayout(), widgetRegistry));
        setAlwaysOnTop(true);
        setSize(500, 500);
    }
}
