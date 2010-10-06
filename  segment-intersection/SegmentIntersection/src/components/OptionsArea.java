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
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
import computation.FindIntersections;



/**
 * <p>
 * <b>Zone d'options</b
 * <p>
 * Cette classe permet d'implémenter la zone d'options qui gère:
 * <ul>
 * <li>le nombre de côtés des figures (ayant un nombre de côté qui peut changer)</li>
 * <li>l'épaisseur des traits</li>
 * <li>la couleur des traits</li>
 * <li>la couleur de remplissage des formes</li>
 * </ul>
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
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
        
        /* ComboBox pour les épaisseurs de ligne */
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
        
        /* Combobox pour les textures (qui peuvent être des couleurs, ou les motifs ci-dessus */
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
            	getDrawArea().setTypeDessin(TypeAction.DESSIN);
            	getDrawArea().setCaracFormeCourante((CaracForme) listeFormes.getSelectedItem());
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
	 * Pour supprimer la sélection
	 */
	public void supprimer() {
		drawArea.supprimer();
	}
	
	/**
	 * Pour tout supprimer
	 */
	public void deleteAll() {
		drawArea.deleteAll();
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
	 * Couper une sélection
	 */
	public void couper() {
		drawArea.couper();
	}

	/**
	 * Copier une sélection
	 */
	public void copier() {
		drawArea.copier();
	}

	/**
	 * Coller une sélection préalablement coupiée/coupée
	 */
	public void coller() {
		drawArea.coller();
	}

	/**
	 * Définir le type de dessin
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
	
	
	public DrawArea getDrawArea() {
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

	public void openFile(File file) {
		
		FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    BufferedReader d = null;

	    
        System.out.printf("Opening: " + file.getName() + ".\n");
        
        try {
            fis = new FileInputStream(file);

            // Here BufferedInputStream is added for fast reading.
            bis = new BufferedInputStream(fis);
            d = new BufferedReader(new InputStreamReader(bis));

            String line = null;
            String[] lineSplit ;
            Float x1, y1, x2, y2;
            
            while ((line = d.readLine()) != null) {

            // this statement reads the line from the file and print it to
              // the console.
              System.out.println(line);
              lineSplit = line.split(" ");
              
              // look for four floating numbers/line this way : x1 y1 x2 y2
              if (lineSplit.length == 4)
              {
	              x1 = Float.parseFloat(lineSplit[0]);
	              y1 = Float.parseFloat(lineSplit[1]);
	              x2 = Float.parseFloat(lineSplit[2]);
	              y2 = Float.parseFloat(lineSplit[3]);
	              
	              float[] pointSegmentStart = {x1, y1}; 
	              float[] pointSegmentEnd = {x2, y2}; 
	              
	              getDrawArea().loadSegment(pointSegmentStart, pointSegmentEnd);
	              System.out.println("x1 = "+x1+" y1 = "+y1+" x2 = "+x2+" y2 = "+y2);
              } else {
            	  System.out.println("openFile : Need four (4) floating numbers per line in the file, found "+lineSplit.length+" instead.");
              }
            }

            // dispose all the resources after using them.
            fis.close();
            bis.close();
            d.close();

          } catch (FileNotFoundException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
        
	}

	public void findIntersections() {
		getDrawArea().getSceneGraphArea().removeIntersections();
		FindIntersections fi ;
		fi = new FindIntersections(getDrawArea(), getDrawArea().getSceneGraphArea().getSegments());		
	}


}
	
