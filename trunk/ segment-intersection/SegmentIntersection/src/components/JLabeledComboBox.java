package components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * <p>
 * Classe contenant:
 * <ul>
 * <li>un titre</li>
 * <li>une liste déroulante </li>
 * </ul>
 * ... utilisant des JLabel avec des icones
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 */
@SuppressWarnings("serial")
public class JLabeledComboBox extends JPanel
{
	/** Le titre de cette liste */
	private String titre;

	/** Icons pour les labels */
	private ImageIcon[] icones;

	/** Les textes pour les items */
	private String[] texteItem;

	/**
	 * le répertoire des images. Attention, il s'agit du répertoire relatif à
	 * cette classe et non pas au programme principal
	 */
	private final static String ImageBase = "/images/";

	/** l'extension des images */
	private final static String ImageType = ".gif";

	/**
	 * Constructeur de la classe
	 * @param titre le titre du panel
	 * @param texteItem les légendes des éléments de la liste
	 * @param selectedIndex l'élément sélectionné initialement
	 * @param listener le listener à appeller quand l'élement sélectionné de la
	 *            liste change
	 * @see #creerImageIcon(String)
	 */
	public JLabeledComboBox(String title, String[] texteItem, int selectedIndex,
	        ItemListener listener)
	{
		super(new GridLayout(2, 1), true); // double buffered

		this.titre = title;
		this.texteItem = texteItem;

		// Charge les images et créé un tableau d'indexs pour le combobox
		// qui seront utilisés dans le ComboBoxRenderer
		icones = new ImageIcon[this.texteItem.length];
		Integer[] intArray = new Integer[this.texteItem.length];
		for (int i = 0; i < icones.length; i++)
		{
			intArray[i] = new Integer(i);
			icones[i] = creerImageIcon(ImageBase + this.texteItem[i] + ImageType);
			if (icones[i] != null)
			{
				icones[i].setDescription(this.texteItem[i]);
			}
		}

		// Création du titre
		JLabel label = new JLabel(this.titre);
		add(label);

		// Creation de la combobox Combobox
		JComboBox combobox = new JComboBox(intArray);
		combobox.setEditable(false);
		if (selectedIndex < 0 || selectedIndex > texteItem.length)
		{
			selectedIndex = 0;
		}
		combobox.setSelectedIndex(selectedIndex);
		combobox.addItemListener(listener);
		// Mise en place du renderer pour les élements de la liste
		JLabelRenderer renderer = new JLabelRenderer();
		renderer.setPreferredSize(new Dimension(100, 32));
		combobox.setRenderer(renderer);
		// Ajout de la liste
		add(combobox);
	}

	/**
	 * Création d'une icône à partir d'un fichier
	 * @param chemin le chemin du fichier
	 * @return une nouvelle icône si le fichier existe ou bien null
	 * si le fichier n'a pas été trouvé
	 */
	protected ImageIcon creerImageIcon(String chemin)
	{
		URL imgURL = JLabeledComboBox.class.getResource(chemin);
		if (imgURL != null)
		{
			return new ImageIcon(imgURL);
		}
		else
		{
			System.err.println("Fichier introuvable: " + chemin);
			return null;
		}
	}

	/**
	 * Renderer pour les Labels du combobox
	 */
	protected class JLabelRenderer extends JLabel implements ListCellRenderer
	{
		/** fonte pour les items à problèmes */
		private Font pbFont;

		/**
		 * Constructeur
		 */
		public JLabelRenderer()
		{
			setOpaque(true);
			setHorizontalAlignment(LEFT);
			setVerticalAlignment(CENTER);
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing
		 * .JList, java.lang.Object, int, boolean, boolean)
		 */
		@SuppressWarnings("synthetic-access")
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
		        int index, boolean isSelected, boolean cellHasFocus)
		{
			// Obtention de l'indice de l'élément selectionné : le paramètre
			// index est toujours valide, il faut juste le caster
			int selectedIndex = ((Integer) value).intValue();

			if (isSelected)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else
			{
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			// Mise en place de l'icone et du texte dans le label
			// Si l'icone est null afficher un label particulier avec
			// setPbText
			ImageIcon itemIcon = icones[selectedIndex];
			String itemString = texteItem[selectedIndex];
			setIcon(itemIcon);
			if (itemIcon != null)
			{
				setText(itemString);
				setFont(list.getFont());
			}
			else
			{
				setPbText(itemString + " (pas d'image)", list.getFont());
			}

			return this;
		}

		/**
		 * Mise en place du texte s'il y a un pb pour cet item
		 * @param pbText le texte à afficher
		 * @param normalFont la fonte à utiliser (italique)
		 */
		protected void setPbText(String pbText, Font normalFont)
		{
			if (pbFont == null)
			{ // lazily create this font
				pbFont = normalFont.deriveFont(Font.ITALIC);
			}
			setFont(pbFont);
			setText(pbText);
		}

	}

	/**
	 */
	private OptionsArea optionsArea;

	/**
	 * Getter of the property <tt>zoneOptions</tt>
	 * @return  Returns the zoneOptions.
	 */
	public OptionsArea getZoneOptions() {
		return optionsArea;
	}

	/**
	 * Setter of the property <tt>zoneOptions</tt>
	 * @param optionsArea  The zoneOptions to set.
	 */
	public void setZoneOptions(OptionsArea optionsArea) {
		this.optionsArea = optionsArea;
	}

}
