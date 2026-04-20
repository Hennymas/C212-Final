import javax.swing.*;

class ToolsMenu extends JMenu {

    ToolsMenu(ImageEditor mainPanel) {
        super("Tools");
        // Create the menu items.
        JMenuItem zeroRed = new ZeroRedMenuItem(mainPanel);
        JMenuItem grayscale = new GrayscaleMenuItem(mainPanel);
        JMenuItem invert = new InvertMenuItem(mainPanel);
        JMenuItem mirror = new MirrorMenuItem(mainPanel);
        JMenuItem repeat = new RepeatMenuItem(mainPanel);
        JMenuItem rotate = new RotateMenuItem(mainPanel);

        // Add the menu items to the menu.
        this.add(zeroRed);
        this.add(grayscale);
        this.add(invert);
        this.add(mirror);
        this.add(repeat);
        this.add(rotate);
    }
}
