package facetmodeller.menutasks;

import facetmodeller.FacetModeller;
import facetmodeller.groups.Group;
import java.awt.Color;
import javax.swing.JColorChooser;

public final class ChangeGroupFacetColorMenuTask extends ControlledMenuTask {
    
    public ChangeGroupFacetColorMenuTask(FacetModeller con) { super(con); }

    @Override
    public String text() { return "Change group facet color"; }

    @Override
    public String tip() { return "Change the facet painting color for the group"; }

    @Override
    public String title() { return "Change Group Facet Color"; }

    @Override
    public boolean check() {
        if ( !controller.hasGroups() ) { return false; }
        return ( controller.getSelectedCurrentGroup() != null );
    }

    @Override
    public void execute() {
        // Check for the required information:
        if (!check()) { return; }
        // Get the selected group:
        Group group = controller.getSelectedCurrentGroup();
        // Ask for the colour:
        Color col = JColorChooser.showDialog(controller,title(),group.getFacetColor());
        // Check response:
        if (col == null) { return; }
        // Set painting colour to that specified:
        group.setFacetColor(col);
        // Repaint:
        controller.redraw();
    }
    
}
