package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.smartdashboard.types.DisplayElementRegistry;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import org.jfree.ui.ExtensionFileFilter;

/**
 * This is the menu bar on top of the window.
 * It can be set to hide automatically in the preferences.
 * @author Joe Grinstead
 */
public class DashboardMenu extends JMenuBar {

    /**
     * Creates a menu for the given panel.
     * @param panel the panel
     */
    DashboardMenu(final DashboardPanel panel) {
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenu = new JMenuItem("Open...");
        loadMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File(DashboardPrefs.getInstance().saveFile.getValue()));
                fc.addChoosableFileFilter(new ExtensionFileFilter("XML File", ".xml"));
                fc.setMultiSelectionEnabled(false);
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setApproveButtonText("Open");
                fc.setDialogTitle("Open");

                if (fc.showOpenDialog(DashboardFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
                    String filepath = fc.getSelectedFile().getAbsolutePath();

                    DashboardFrame.getInstance().load(filepath);
                    DashboardPrefs.getInstance().saveFile.setValue(filepath);
                }
            }
        });
        fileMenu.add(loadMenu);

        JMenuItem newMenu = new JMenuItem("New");
        newMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DashboardFrame.getInstance().getPanel().clear();
            }
        });
        fileMenu.add(newMenu);

        JMenuItem saveMenu = new JMenuItem("Save");
        saveMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DashboardFrame.getInstance().save(DashboardPrefs.getInstance().saveFile.getValue());
            }
        });
        fileMenu.add(saveMenu);

        JMenuItem saveAs = new JMenuItem("Save As...");
        saveAs.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser(".");
                fc.addChoosableFileFilter(new ExtensionFileFilter("XML File", ".xml"));
                fc.setApproveButtonText("Save");
                fc.setDialogTitle("Save As...");

                fc.setMultiSelectionEnabled(false);
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                if (fc.showOpenDialog(DashboardFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
                    String filepath = fc.getSelectedFile().getAbsolutePath();
                    if (!filepath.endsWith(".xml")) {
                        filepath = filepath + ".xml";
                    }
                    DashboardFrame.getInstance().save(filepath);
                    DashboardPrefs.getInstance().saveFile.setValue(filepath);
                }
            }
        });
        fileMenu.add(saveAs);

        JMenuItem prefMenu = new JMenuItem("Preferences");
        prefMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PropertyEditor editor = DashboardFrame.getInstance().getPropertyEditor();
                editor.setPropertyHolder(DashboardPrefs.getInstance());
                editor.setTitle("Edit Preferences");
                editor.setVisible(true);
            }
        });
        fileMenu.add(prefMenu);

        JMenuItem exitMenu = new JMenuItem("Exit");
        exitMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DashboardFrame.getInstance().exit();
            }
        });
        fileMenu.add(exitMenu);

        JMenu viewMenu = new JMenu("View");
        JCheckBoxMenuItem editMode = new JCheckBoxMenuItem("Editable");
        editMode.setSelected(panel.isEditable());
        editMode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                panel.setEditable(!panel.isEditable());
            }
        });
        viewMenu.add(editMode);

        JMenu addMenu = new JMenu("Add...");
        Set<Class<? extends StaticWidget>> panels = DisplayElementRegistry.getStaticWidgets();
        for (final Class<? extends StaticWidget> option : panels) {
            JMenuItem item = new JMenuItem(DisplayElement.getName(option));
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        StaticWidget element = option.newInstance();
                        panel.addElement(element, null);
                    } catch (InstantiationException ex) {
                    } catch (IllegalAccessException ex) {
                    }
                }
            });

            addMenu.add(item);
        }

        viewMenu.add(addMenu);

        final JMenu revealMenu = new JMenu("Reveal...");

        viewMenu.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent e) {
                revealMenu.removeAll();

                int count = 0;
                for (final String field : DashboardFrame.getInstance().getPanel().getHiddenFields()) {
                    if (Robot.getTable().containsKey(field)) {
                        count++;
                        revealMenu.add(new JMenuItem(new AbstractAction(field) {

                            public void actionPerformed(ActionEvent e) {
                                DashboardFrame.getInstance().getPanel().addField(field);
                            }
                        }));
                    }
                }

                revealMenu.setEnabled(count != 0);
            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });

        viewMenu.add(revealMenu);

        JMenuItem removeUnusedMenu = new JMenuItem("Remove Unused");
        removeUnusedMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DashboardFrame.getInstance().getPanel().removeUnusedFields();
            }
        });

        viewMenu.add(removeUnusedMenu);

        add(fileMenu);
        add(viewMenu);
    }
}
