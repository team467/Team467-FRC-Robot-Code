package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.BooleanProperty;

/**
 *
 * @author Joe Grinstead
 */
public abstract class EditableDisplayElement extends Widget {

    public final BooleanProperty editable = new BooleanProperty(this, "Editable", true);
}
