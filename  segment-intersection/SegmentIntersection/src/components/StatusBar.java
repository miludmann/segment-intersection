package components;

import java.awt.BorderLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory ;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <p>
 * La barre d'état située en bas de la fenêtre pour ainsi que d'autres informations
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 * @see FenetrePrincipale
 */
@SuppressWarnings("serial")
public class StatusBar extends JPanel implements Status
{
	// les zones d'information
	/**
	 * le Label correspondant aux coordonnées de la souris
	 * */
	private JLabel coord;
	
	/**
	 * le Label correspondant au message d'information générale
	 * 
	 * */
	private JLabel info;
	/** 
	 * Le format des coordonnées dans la barre d'état
	 * sur 3 digits, sans décimales
	 */
	private final static String NUMBERFORMAT = "000";

	/**
     * Constructeur de la barre d'état
     */
    public StatusBar()
    {
	    super();
		this.setLayout(new BorderLayout());
		this.add ( info = new JLabel("Zone d'Information"), BorderLayout.WEST );
		this.add ( coord = new JLabel(), BorderLayout.EAST);
		afficherCoordDefaut();
		this.setBorder(BorderFactory.createEtchedBorder());
    }
	
    /**
     * Affichage des coordonnées dans la barre d'état
     * @param float x abscisse
     * @param float x ordonnée
     */
	@Override
	public void afficherCoordonnees(float x, float y)
	{
		DecimalFormat coordFormat = new DecimalFormat(NUMBERFORMAT);
		String ys = coordFormat.format(y);
		String xs = coordFormat.format(x);
		coord.setText("x : " + xs + " y : " + ys);
	}

    /**
     * Affichage les coordonnées "vide" hors de la zone de dessin
     */
	@Override
    public void afficherCoordDefaut()
    {
		coord.setText("x : ___ y : ___");
    }

    /**
     * 
     */
	@Override
	public void afficherMessage(String message)
	{
		info.setText(message);
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
	public void setFenetrePrincipale(MainWindow fenetrePrincipale) {
		this.mainWindow = fenetrePrincipale;
	}
}
