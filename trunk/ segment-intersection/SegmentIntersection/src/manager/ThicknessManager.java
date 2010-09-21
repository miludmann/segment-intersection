package manager;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import components.DrawArea;

import sceneGraph.Skin;


/**
 * Gestionnaire d'action pour changer l'�paisseur du trait de dessin
 * @author Depoyant Guillaume & Ludmann Micha�l
 *
 */
public class ThicknessManager implements ItemListener
{
	/** La liste des �paisseurs de trait */
	private float[] widths;
	private DrawArea drawPanel;
	
	/**
	 * Constructeur du gestionnaire d'�paisseurs de trait
	 * @param widths la liste des �paisseurs possibles
	 * @param zone la zone de dessin
	 */
	public ThicknessManager(float[] widths, DrawArea drawPanel)
	{
		this.widths = widths;
		this.drawPanel = drawPanel;
		drawPanel.getApparenceCourante().setLineThickness(this.widths[0]);
	}
	
	/**
	 * Action d�clench�e lorsque l'on change d'�paisseur de trait.
	 * on r�cup�re l'indice de l'�paisseur s�lectionn�e pour l'appliquer � 
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
