package tp4.view.tabbedPane;

import java.awt.Color;
import javax.swing.JTabbedPane;



/**
 *
 * @author RAVEN
 */
public class TabbedPaneCustom extends JTabbedPane {

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
        repaint();
    }

    public Color getUnselectedColor() {
        return unselectedColor;
    }

    public void setUnselectedColor(Color unselectedColor) {
        this.unselectedColor = unselectedColor;
        repaint();
    }

    private Color selectedColor = new Color(46, 135, 60);
    private Color unselectedColor = new Color(241,233,220);

    public TabbedPaneCustom() {
        setBackground(new Color(241,233,220));
        setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        setUI(new TabbedPaneCustomUI(this));
    }
}
