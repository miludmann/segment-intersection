package manager;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import components.DrawArea;

import sceneGraph.Skin;


/**
 * Gestionnaire d'action pour changer l'épaisseur du trait de dessin
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
public class ThicknessManager implements ItemListener
{
	/** La liste des épaisseurs de trait */
	private float[] widths;
	private DrawArea drawPanel;
	
	/**
	 * Constructeur du gestionnaire d'épaisseurs de trait
	 * @param widths la liste des épaisseurs possibles
	 * @param zone la zone de dessin
	 */
	public ThicknessManager(float[] widths, DrawArea drawPanel)
	{
		this.widths = widths;
		this.drawPanel = drawPanel;
		drawPanel.getApparenceCourante().setLineThickness(this.widths[0]);
	}
	
	/**
	 * Action déclenchée lorsque l'on change d'épaisseur de trait.
	 * on récupère l'indice de l'épaisseur sélectionnée pour l'appliquer à 
	 * la zone de dessin. 
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent e)
	{
		JComboBox liste = (JComboBox) e.getSource();
		drawPanel.getApparenceCourante().setLineThickness(widths[liste.getSelectedIndex()]);

		drawPanel.setApparenceSelection(new Skin(drawPanel.getApparenceCourante()));
	}

}
