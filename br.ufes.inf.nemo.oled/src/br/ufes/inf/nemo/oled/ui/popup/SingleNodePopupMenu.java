package br.ufes.inf.nemo.oled.ui.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import br.ufes.inf.nemo.oled.umldraw.shared.UmlNode;
import br.ufes.inf.nemo.oled.umldraw.structure.ClassElement;
import br.ufes.inf.nemo.oled.util.AppCommandListener;
import br.ufes.inf.nemo.oled.util.ApplicationResources;
import br.ufes.inf.nemo.oled.util.IconLoader;

public class SingleNodePopupMenu extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Set<AppCommandListener> commandListeners = new HashSet<AppCommandListener>();
	private UmlNode node;
	final JMenuItem showAttrItem;
	final JMenuItem showOperItem;
	
	public SingleNodePopupMenu()
	{
		JMenuItem propertyItem = createMenuItem(this, "editproperties");
		propertyItem.setAccelerator(KeyStroke.getKeyStroke("F9"));		
		
		JMenu drawOrderMenu = new JMenu(ApplicationResources.getInstance().getString("submenu.draworder.name"));
		add(drawOrderMenu);
		
		createMenuItem(drawOrderMenu, "draworder.tofront");
		createMenuItem(drawOrderMenu, "draworder.toback");
		
		JMenu visibilityMenu = new JMenu(ApplicationResources.getInstance().getString("submenu.visibility.name"));
		add(visibilityMenu);
		
		showOperItem = createCheckBoxMenuItem(visibilityMenu, "visibility.showoperation");
		showOperItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (node instanceof ClassElement) ((ClassElement)node).setShowOperations(showOperItem.isSelected());
			}
		});
		
		showAttrItem = createCheckBoxMenuItem(visibilityMenu, "visibility.showattribute");
		showAttrItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (node instanceof ClassElement) ((ClassElement)node).setShowAttributes(showAttrItem.isSelected());
			}
		});
		
//		final JMenuItem showStereoItem = createCheckBoxMenuItem(visibilityMenu, "visibility.showstereotype");
//		showStereoItem.addActionListener(new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				
//				if (node instanceof ClassElement) ((ClassElement)node).setShowStereotypes(showStereoItem.isSelected());
//			}
//		});
		
		addSeparator();
		
		createMenuItem(this, "exclude");
		
		addSeparator();
		
		createMenuItem(this, "delete");		
	}
	
	public void setNode (UmlNode node)
	{
		this.node = node;
		if (node instanceof ClassElement){
			showAttrItem.setSelected(((ClassElement)node).showAttributes());
			showOperItem.setSelected(((ClassElement)node).showOperations());
		}
	}	
	
	/**
	 * Adds the specified AppCommandListener.
	 * 
	 * @param l
	 *            the AppCommandListener to add
	 */
	public void addAppCommandListener(AppCommandListener l) {
		commandListeners.add(l);
	}
	
	/**
	 * Generic helper method to construct a menu according to the resource
	 * strings.
	 * 
	 * @param menu
	 *            the menu to create the item in
	 * @param name
	 *            the menu item name
	 * @return the JMenuItem
	 */
	public JMenuItem createMenuItem(JComponent menu, String name) {
		String prefix = "menuitem." + name;
		JMenuItem menuitem = new JMenuItem(getResourceString(prefix + ".name"));

		// Command
		String actionCommand = getResourceString(prefix + ".command");
		menuitem.setActionCommand(actionCommand);
		menuitem.addActionListener(this);

		// icon
		String iconType = getResourceString(prefix + ".icon");
		if (iconType != null) {
			menuitem.setIcon(IconLoader.getInstance().getIcon(iconType));
		}
		menu.add(menuitem);
		return menuitem;
	}
	
	/**
	 * Returns the specified resource as a String object.
	 * 
	 * @param property
	 *            the property name
	 * @return the property value or null if not found
	 */
	private String getResourceString(String property) {
		return ApplicationResources.getInstance().getString(property);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		for (AppCommandListener l : commandListeners) {
			l.handleCommand(e.getActionCommand());
		}
	}

	/**
	 * Helper method to construct a check box menu item according to resource
	 * strings.
	 * 
	 * @param menu
	 *            the parent menu
	 * @param name
	 *            the menuitem name
	 * @return the menu item
	 */
	private JCheckBoxMenuItem createCheckBoxMenuItem(JComponent menu,
			String name) {
		String prefix = "menuitem." + name;
		JCheckBoxMenuItem menuitem = new JCheckBoxMenuItem(
				getResourceString(prefix + ".name"));

		// Command
		String actionCommand = getResourceString(prefix + ".command");
		menuitem.setActionCommand(actionCommand);
		menuitem.addActionListener(this);
		menu.add(menuitem);
		return menuitem;
	}
}