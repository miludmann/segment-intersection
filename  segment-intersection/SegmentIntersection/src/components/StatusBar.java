package components;

import java.awt.BorderLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory ;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <p>
 * La barre d'�tat situ�e en bas de la fen�tre pour ainsi que d'autres informations
 * </p>
 * @author Depoyant Guillaume & Ludmann Micha�l
 * @see FenetrePrincipale
 */
@SuppressWarnings("serial")
public class StatusBar extends JPanel implements Status
{
	// les zones d'information
	/**
	 * le Label correspondant aux coordonn�es de la souris
	 * */
	private JLabel coord;
	
	/**
	 * le Label correspondant au message d'information g�n�rale
	 * 
	 * */
	private JLabel info;
	/** 
	 * Le format des coordonn�es dans la barre d'�tat
	 * sur 3 digits, sans d�cimales
	 */
	private final static String NUMBERFORMAT = "000";

	/**
     * Constructeur de la barre d'�tat
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
     * Affichage des coordonn�es dans la barre d'�tat
     * @param float x abscisse
     * @param float x ordonn�e
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
     * Affichage les coordonn�es "vide" hors de la zone de dessin
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
