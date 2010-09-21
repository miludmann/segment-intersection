package main;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import components.MainWindow;


/**
 * Application Editeur de lignes
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
public class JApplicationEditor
{

	/**
	 * Programme principal.
	 * Instancie une nouvelle {@link MainWindow} en utilisant le {@link UIManager}
	 * @param args Arguments du programme
	 */
	public static void main(String[] args)
	{
		// 0. Mise en place du look and feel en fonction de l'OS

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}


		// 1. instanciation d'une nouvelle FenetrePrincipale
/*		if (args.length > 0)
		{
			new FenetrePrincipale(args[0]);
		}
		else
		{
*/		new MainWindow();
//		}
	}

}
