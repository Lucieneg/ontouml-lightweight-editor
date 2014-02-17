package br.ufes.inf.nemo.oled.ui.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;

import RefOntoUML.Class;
import RefOntoUML.Classifier;
import RefOntoUML.DataType;
import RefOntoUML.Element;
import RefOntoUML.Property;
import br.ufes.inf.nemo.oled.DiagramManager;
import br.ufes.inf.nemo.oled.model.UmlProject;
import br.ufes.inf.nemo.oled.umldraw.structure.ClassElement;
import br.ufes.inf.nemo.oled.util.ColorPalette;
import br.ufes.inf.nemo.oled.util.ColorPalette.ThemeColor;
import br.ufes.inf.nemo.oled.util.ModelHelper;

public class AttributesEditionPanel extends JPanel {

private static final long serialVersionUID = 1L;
	
	private ClassElement classElement;	
	private Classifier element;
	private DiagramManager diagramManager;
	@SuppressWarnings("unused")
	private JFrame parent;
		
	private Map<String, DataType> modelDataTypes; 
	private JButton btnDelete;
	private JButton btnCreate;
	private JButton btnUp;
	private JButton btnDown;
	private JScrollPane scrollpane;
	private JTable table;
	private AttributeTableModel attributesTableModel;
	private JPanel panel;
	private JButton btnEdit;
	private JPanel panel_1;
	private JCheckBox cbxVisible;
		
	@SuppressWarnings({ })
	public AttributesEditionPanel(DiagramManager diagramManager, ClassElement classElement, boolean modal) 
	{
		this.diagramManager = diagramManager;
		this.classElement = classElement;
		this.element = classElement.getClassifier();
						
		attributesTableModel = new AttributeTableModel(element);
	    setMinimumSize(new Dimension(0, 0));
	    
	    JSeparator separator_1 = new JSeparator();
		
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(""));
		
		panel_1 = new JPanel();
		panel_1.setBorder(BorderFactory.createTitledBorder(""));
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(429)
							.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 423, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 423, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(11, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 154, Short.MAX_VALUE)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		cbxVisible = new JCheckBox("Turn attributes visible");
		cbxVisible.setPreferredSize(new Dimension(400, 20));
		cbxVisible.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(cbxVisible);
		
		scrollpane = new JScrollPane();		
		scrollpane.setMinimumSize(new Dimension(0, 0));
		scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollpane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		table = new JTable();		
		scrollpane.setViewportView(table);
		table.setModel(attributesTableModel);
		
		table.setBorder(new EmptyBorder(0, 0, 0, 0));
		table.setFillsViewportHeight(true);
		table.setGridColor(Color.LIGHT_GRAY);		
		table.setSelectionBackground(ColorPalette.getInstance().getColor(ThemeColor.GREEN_MEDIUM));
		table.setSelectionForeground(Color.BLACK);
		table.setFocusable(false);	    
		table.setRowHeight(23);
		
		btnCreate = new JButton("");
		btnCreate.setToolTipText("Add new attribute to this class");
		btnCreate.setIcon(new ImageIcon(AttributesEditionPanel.class.getResource("/resources/br/ufes/inf/nemo/oled/ui/cross.png")));
		btnCreate.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addAttribute(arg0);
			}
		});
		
		btnDelete = new JButton("");
		btnDelete.setToolTipText("Delete selected attribute");
		btnDelete.setIcon(new ImageIcon(AttributesEditionPanel.class.getResource("/resources/br/ufes/inf/nemo/oled/ui/delete.png")));
		btnDelete.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteAttribute(arg0);
			}
		});
		
		btnUp = new JButton("");
		btnUp.setToolTipText("Move up selected attribute");
		btnUp.setIcon(new ImageIcon(AttributesEditionPanel.class.getResource("/resources/br/ufes/inf/nemo/oled/ui/arrowup.png")));
		btnUp.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				moveUpAttribute();
			}
		});
		
		btnDown = new JButton("");
		btnDown.setToolTipText("Move down selected attribute");
		btnDown.setIcon(new ImageIcon(AttributesEditionPanel.class.getResource("/resources/br/ufes/inf/nemo/oled/ui/arrowdown.png")));
		btnDown.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				moveDownAttribute();
			}
		});
		
		btnEdit = new JButton("");
		btnEdit.setToolTipText("Edit selected attribute");
		btnEdit.setIcon(new ImageIcon(AttributesEditionPanel.class.getResource("/resources/br/ufes/inf/nemo/oled/ui/edit.png")));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollpane, GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnCreate, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnEdit, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 208, Short.MAX_VALUE)
							.addComponent(btnUp, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDown, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollpane, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnEdit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
							.addComponent(btnCreate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnDown, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnDelete, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnUp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		this.setLayout(groupLayout);
		
		setSize(450,293);
		
		cbxVisible.setSelected(classElement.showAttributes());
		
		myPostInit();
	}	

	private void myPostInit() 
	{						
		modelDataTypes = new HashMap<String, DataType>();
		List<DataType> dataTypes = ModelHelper.getModelDataTypes(diagramManager.getCurrentEditor().getProject());
		for (DataType item : dataTypes) {			
			modelDataTypes.put(item.getName(), item);
		}
		
		TableColumn typeColumn = table.getColumnModel().getColumn(1);	
		typeColumn.setCellEditor(createEditor(modelDataTypes.keySet().toArray()));

		TableColumn typeColumn2 = table.getColumnModel().getColumn(2);	
		typeColumn2.setCellEditor(createEditor(new String[]{"1","0..1","0..*","1..*"}));
		
		table.setSurrendersFocusOnKeystroke(true);
		
		if(classElement.getClassifier() instanceof DataType)
		{
			DataType dataType = (DataType) classElement.getClassifier();			
			for (Property attribute : dataType.getAttribute()) {
				attributesTableModel.addEntry(attribute);
			}
		} else {
			Class umlclass = (Class) classElement.getClassifier();
			for (Property attribute : umlclass.getAttribute()) {
				attributesTableModel.addEntry(attribute);
			}			
		}
	}
	
	private TableCellEditor createEditor(Object[] objects) 
	{
        @SuppressWarnings({ "rawtypes", "unchecked" })
		JComboBox combo = new JComboBox(objects) {
        	private static final long serialVersionUID = 1L;			
			@Override
			protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) 
			{
				boolean retValue = super.processKeyBinding(ks, e, condition,pressed);
                if (!retValue && isStartingCellEdit() && editor != null) {
                    // this is where the magic happens
                    // not quite right; sets the value, but doesn't advance the
                    // cursor position for AC
                    editor.setItem(String.valueOf(ks.getKeyChar()));
                }
                return retValue;
			}			
            private boolean isStartingCellEdit() 
            {
                JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, this);
                return table != null && table.isFocusOwner() && !Boolean.FALSE.equals((Boolean)table.getClientProperty("JTable.autoStartsEdit"));
            }
        };        
        //AutoCompleteDecorator.decorate(combo);
        combo.setEditable(true);
        return new DefaultCellEditor(combo);
    }
	
	private void moveUpAttribute() 
	{
		int row = table.getSelectedRow();
		if (row >=0  && row < table.getRowCount()) 
		{
			attributesTableModel.moveUpEntry(row);
			table.setRowSelectionInterval(row - 1, row - 1);
		}
	}

	private void moveDownAttribute() 
	{
		int row = table.getSelectedRow();
		if (row >=0  && row < table.getRowCount()) 
		{
			attributesTableModel.moveDownEntry(row);
			table.setRowSelectionInterval(row + 1, row + 1);
		}
	}
	
	protected void deleteAttribute(ActionEvent evt) 
	{
		int selectedRow = table.getSelectedRow();
		if (selectedRow >= 0 && selectedRow < attributesTableModel.getRowCount()) 
		{
			attributesTableModel.removeEntryAt(selectedRow);
		}
	}
	
	protected void addAttribute(ActionEvent evt) 
	{
		attributesTableModel.addEmptyEntry();		
	}
	
	public static String getStereotype(EObject element)
	{
		String type = element.getClass().toString().replaceAll("class RefOntoUML.impl.","");
	    type = type.replaceAll("Impl","");
	    type = Normalizer.normalize(type, Normalizer.Form.NFD);
	    type = type.replace("Association","");
	    return type;
	}
	
	public void transferAttributesData()
	{
		classElement.setShowAttributes(cbxVisible.isSelected());
		diagramManager.updatedOLEDFromInclusion(element);
		
		transferDataTypes();	
		
		List<Property> classAttributes = attributesTableModel.getEntries();
		for (Property property : classAttributes) 
		{			
			if(!property.getName().isEmpty() || !property.getType().getName().isEmpty())
			{				
				DataType existingType = modelDataTypes.get(property.getType().getName().trim());
				if(existingType != null){ 
					property.setType(existingType); 
				}				
				if(classElement.getClassifier() instanceof DataType)
					((DataType)classElement.getClassifier()).getOwnedAttribute().add(property);
				else				
					((Class)classElement.getClassifier()).getOwnedAttribute().add(property);
				
				diagramManager.updatedOLEDFromInclusion(property);
			}
		}
	}
	
	private void transferDataTypes() 
	{
		List<Property> classAttributes = attributesTableModel.getEntries();
		ArrayList<Element> createdList = new ArrayList<Element>();
		for (Property property : classAttributes) 
		{
			//Avoid the creation of duplicated types			
			if(modelDataTypes.keySet().contains(property.getType().getName().trim()) == false)
			{	
				UmlProject project = diagramManager.getCurrentProject();				
				AddCommand cmd = new AddCommand(project.getEditingDomain(), project.getModel().getPackagedElement(), property.getType());
				project.getEditingDomain().getCommandStack().execute(cmd);				
				modelDataTypes.put(property.getType().getName(),(DataType)property.getType());
				createdList.add((Element) property.getType());
			}
		}		
		
		for(Element element: createdList) diagramManager.updatedOLEDFromInclusion(element);		
	}
}