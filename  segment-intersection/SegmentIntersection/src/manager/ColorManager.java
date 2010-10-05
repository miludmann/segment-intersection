package manager;

import java.awt.Color;
import java.awt.Paint;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import components.DrawArea;

import sceneGraph.Skin;


/**
 * Gestionnaire d'action pour changer la couleur de dessin
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
public class ColorManager implements ItemListener
{
	/** Le type d'utilisation de la couleur : 0 pour la ligne, 1 pour un remplissage */
	private int type;
	/** La liste des couleurs */
	private Paint[] colors;
	
	private DrawArea drawPanel;
	
	/**
	 * Constructeur du gestionnaire de couleurs
	 * @param colors la liste des couleurs
	 * @param type 
	 */
	public ColorManager(Paint[] colors, int type, DrawArea drawPanel)
	{
        this.type = type;
        
		this.colors = colors;
		this.drawPanel = drawPanel;
		
		Paint paint = this.colors[0];
        if (type==0) 
        {
        	drawPanel.getCurrentSkin().setLineColor((Color)paint);
        } else if (type==1) {
            drawPanel.getCurrentSkin().setInnerPaint(paint);
        }
	}
	
	/**
	 * Action déclenchée lorsque l'on change de couleur.
	 * on récupère l'indice de la couleur sélectionnée pour l'appliquer à 
	 * la zone de dessin. 
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent e)
	{
		JComboBox liste = (JComboBox) e.getSource();
		
		Paint paint = colors[liste.getSelectedIndex()];
		if (type == 0) 
		{
			drawPanel.getCurrentSkin().setLineColor((Color)paint);
        } else if (type == 1) {
            drawPanel.getCurrentSkin().setInnerPaint(paint);
        }

		drawPanel.setApparenceSelection(new Skin(drawPanel.getCurrentSkin()));
	}
}
