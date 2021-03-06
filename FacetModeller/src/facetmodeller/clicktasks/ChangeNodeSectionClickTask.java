package facetmodeller.clicktasks;

import dialogs.Dialogs;
import facetmodeller.FacetModeller;
import facetmodeller.commands.MergeNodesCommand;
import facetmodeller.gui.ClickModeManager;
import facetmodeller.plc.Node;
import facetmodeller.plc.NodeOffSection;
import facetmodeller.sections.Section;
import geometry.MyPoint2D;
import geometry.MyPoint3D;

public final class ChangeNodeSectionClickTask extends ControlledClickTask {
    
    public ChangeNodeSectionClickTask(FacetModeller con) { super(con); }
    
    @Override
    public int mode() { return ClickModeManager.MODE_CHANGE_NODES_SECTION; }

    @Override
    public String tip() { return "Change a node's section"; }

    @Override
    public String title() { return ClickTaskUtil.CHANGE_NODE_SECTION_TITLE; }

    @Override
    public boolean check() {
        if (!controller.hasSections()) { return false; }
        if (controller.getSelectedCurrentSection()==null) { return false; }
        return controller.hasNodes();
    }

    @Override
    public void mouseClick(MyPoint2D p) {
        // Check for the required information:
        if (!check()) { return; }
        if (p==null) { return; }
        // Get the current section:
        Section currentSection = controller.getSelectedCurrentSection();
        // Calculate the closest node to the clicked point:
        if (!controller.calculateClosestNode(p)) { return; }
        Node node = controller.getClosestNode(); // just incase the closestNode object gets nullified by a mouse move (not sure if that is possible but better safe than sorry)
        // Check if the node is already in the current section:
        if ( node.getSection() == currentSection ) { return; }
        // Nullify temporary objects:
        controller.clearClosestNode(); // (or else the old closest node point will be painted)
        // Ask user for confirmation:
        if (!node.isOff()) {
            int response = Dialogs.confirm(controller,"The node will be changed to a 3D off-section node.",title());
            if (response!=Dialogs.OK_OPTION) { return; }
        }
        // Convert point from image pixel coordinates to spatial coordinates:
        MyPoint3D p3 = currentSection.imageToSpace(p);
        // Create a new off-section node object:
        Node newNode = new NodeOffSection(p3,currentSection,node.getGroup());
        // Replace the old node with the new node:
        MergeNodesCommand com = new MergeNodesCommand(controller.getModelManager(),node,newNode,title()); com.execute();
        controller.undoVectorAdd(com);
        // Enable or disable menu items:
        controller.checkItemsEnabled();
        // Repaint:
        controller.redraw();
    }
    
    @Override
    public void mouseMove(MyPoint2D p) {
        // Check for the required information:
        if (!check()) { return; }
        if (p==null) { return; }
        // Clear temporary overlays:
        controller.clearClosestTemporaryOverlays();
        // Calculate the closest node to the cursor position:
        controller.calculateClosestNode(p);
        // Redraw:
        controller.redraw();
        // Use the cursor bar to show information for the closest node:
        controller.updateClosestBar(p);
    }
    
}
