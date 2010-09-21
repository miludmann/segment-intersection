/**
 * 
 */
package components;

import manager.ColorManager;
import manager.ThicknessManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import components.DrawArea.CaracForme;
import components.DrawArea.TypeAction;



/**
 * <p>
 * <b>Zone d'options</b
 * <p>
 * Cette classe permet d'impl�menter la zone d'options qui g�re:
 * <ul>
 * <li>le nombre de c�t�s des figures (ayant un nombre de c�t� qui peut changer)</li>
 * <li>l'�paisseur des traits</li>
 * <li>la couleur des traits</li>
 * <li>la couleur de remplissage des formes</li>
 * </ul>
 * </p>
 * @author Depoyant Guillaume & Ludmann Micha�l
 *
 */
@SuppressWarnings("serial")
public class OptionsArea extends JPanel implements ChangeListener {

	/**
	 * 
	 */

	private JLabeledComboBox listeEpaisseurs;
	private JLabeledComboBox listeCouleurs;
	private JLabeledComboBox listeMotifs;
    private DrawArea drawArea;
    private JComboBox listeFormes;
    private SpinnerNumberModel nbCotesRouletteModel;
    private JSpinner nbCotesRoulette;
       

    /**
     * Contructeur de la classe
     * 
     * @param drawArea
     */
	public OptionsArea(DrawArea drawArea) {
        super();
        
        this.drawArea = drawArea;        
        
        /* ComboBox pour les �paisseurs de ligne */
        float[] thickness = {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f};
        DecimalFormat numberFormat = new DecimalFormat("00");
        String[] thicknessNames = new String[10];
        for (int i = 0; i < thickness.length; i++) 
        {
            thicknessNames[i] = numberFormat.format(thickness[i]);
        }
        listeEpaisseurs = new JLabeledComboBox("Thickness", thicknessNames, 0, new ThicknessManager(thickness, drawArea));
        listeEpaisseurs.setToolTipText("Choose segment thickness");
        
        /* ComboBox pour les couleurs de trait */
        String colorsNames[] = {"Black", "Blue", "Cyan", "Green", "Yellow", "Orange",
                "Red", "Magenta"};
        Color[] colors = {Color.black, Color.blue, Color.cyan, Color.green, Color.yellow,
            Color.orange, Color.red, Color.magenta};
        listeCouleurs = new JLabeledComboBox("Color", colorsNames, 0,
                new ColorManager(colors, 0, drawArea));   
        listeCouleurs.setToolTipText("Choose segment color");
        
        
        /* Premier motif de remplissage (texture) */
        BufferedImage bI1 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d1 = bI1.createGraphics();
        ImageIcon img1 = new ImageIcon(OptionsArea.class.getResource("/images/pampa.gif"));
        g2d1.drawImage(img1.getImage(), 0, 0, this);
        Rectangle2D rect1 = new Rectangle2D.Double(0, 0, 32, 32);
        TexturePaint texture1 = new TexturePaint(bI1, rect1.getBounds2D());
        
        /* Second motif de remplissage */
        BufferedImage bI2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d2 = bI2.createGraphics();
        ImageIcon img2 = new ImageIcon(OptionsArea.class.getResource("/images/triforce.gif"));
        g2d2.drawImage(img2.getImage(), 0, 0, this);
        Rectangle2D rect2 = new Rectangle2D.Double(0, 0, 32, 32);
        TexturePaint texture2 = new TexturePaint(bI2, rect2.getBounds2D());
        
        /* Combobox pour les textures (qui peuvent �tre des couleurs, ou les motifs ci-dessus */
        String texturesNames[] = {"Black", "Blue", "Cyan", "Green", "Yellow", "Orange",
                "Red", "Magenta", "pampa", "triforce"};
        Paint[] patterns = {Color.black, Color.blue, Color.cyan, Color.green,
            Color.yellow, Color.orange, Color.red, Color.magenta,
            texture1, texture2};
      
        listeMotifs = new JLabeledComboBox("Filling pattern", texturesNames, 0,
                new ColorManager(patterns, 1, drawArea));
        listeMotifs.setToolTipText("Choose a filling patern");

        nbCotesRouletteModel = new SpinnerNumberModel(5, 3, null, 1);
        nbCotesRoulette = new JSpinner(nbCotesRouletteModel);
        nbCotesRoulette.addChangeListener(this);

        listeFormes = new JComboBox();
        listeFormes.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
            	getZoneDessin().setTypeDessin(TypeAction.DESSIN);
            	getZoneDessin().setCaracFormeCourante((CaracForme) listeFormes.getSelectedItem());
        		stateChanged(null);
            }
        });
        
        addShape(CaracForme.POLYGONE);       
        this.setLayout(new GridLayout(5, 2));		
        this.add(listeEpaisseurs);
        this.add(listeCouleurs);
        this.add(listeMotifs);
        this.setBorder(BorderFactory.createEtchedBorder());
	}

	/**
	 * Pour supprimer la s�lection
	 */
	public void supprimer() {
		drawArea.supprimer();
	}
	
	/**
	 * Pour tout supprimer
	 */
	public void supprimerTout() {
		drawArea.supprimerTout();
	}

	/**
	 * Annuler une action
	 */
	public void defaire() {
		drawArea.defaire();
	}

	/**
	 * Refaire de action
	 */
	public void refaire() {
		drawArea.refaire();
	}

	/**
	 * Couper une s�lection
	 */
	public void couper() {
		drawArea.couper();
	}

	/**
	 * Copier une s�lection
	 */
	public void copier() {
		drawArea.copier();
	}

	/**
	 * Coller une s�lection pr�alablement coupi�e/coup�e
	 */
	public void coller() {
		drawArea.coller();
	}

	/**
	 * Tout s�lectionner (les fils la racine)
	 */
	public void selectionnerTout() {
		drawArea.selectionnerTout();
	}

	/**
	 * Grouper la s�lection en cours
	 */
	public void grouper() {
		drawArea.grouper();
	}

	/**
	 * D�grouper le groupe s�lectionn�
	 */
	public void degrouper() {
		drawArea.degrouper();
	}

	/**
	 * D�finir le type de dessin
	 * @param typeDessin
	 */
	public void setTypeDessin(TypeAction typeDessin) {
		drawArea.setTypeDessin(typeDessin);
	}


	/**
	 */
	private MainWindow mainWindow;


	/**
	 * Getter of the property <tt>mainWindow</tt>
	 * @return  Returns the mainWindow.
	 */
	public MainWindow getMainWindow() {
		return mainWindow;
	}

	/**
	 * Setter of the property <tt>mainWindow</tt>
	 * @param fenetrePrincipale  The mainWindow to set.
	 */
	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	/**
	 */
	private JLabeledComboBox jLabeledComboBox;


	/**
	 * Getter of the property <tt>jLabeledComboBox</tt>
	 * @return  Returns the jLabeledComboBox.
	 */
	public JLabeledComboBox getJLabeledComboBox() {
		return jLabeledComboBox;
	}

	/**
	 * Setter of the property <tt>jLabeledComboBox</tt>
	 * @param jLabeledComboBox  The jLabeledComboBox to set.
	 */
	public void setJLabeledComboBox(JLabeledComboBox jLabeledComboBox) {
		this.jLabeledComboBox = jLabeledComboBox;
	}
	
	
	public DrawArea getZoneDessin() {
        return this.drawArea;
    }
	

	public void addShape(CaracForme caracForme) {	
		drawArea.setTypeDessin(TypeAction.DESSIN);
		
		listeFormes.removeAllItems();
		switch (caracForme) {

		case POLYGONE:
            listeFormes.addItem(CaracForme.POLYGONE);
			break;

		default:
			break;
		}
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
	
