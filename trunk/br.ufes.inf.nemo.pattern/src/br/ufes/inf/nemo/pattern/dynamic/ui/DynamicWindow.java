package br.ufes.inf.nemo.pattern.dynamic.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

/*
 * TODO LIST
 * 	- Implement reuse of classes used in the current table
 * */


/**
 * @author Victor Amorim
 */
public class DynamicWindow {
	private Shell shell;

	private HashMap<String,ArrayList<Object[]>> hashTable;
	public HashMap<String, ArrayList<Object[]>> getHashTable(){
		return hashTable;
	}

	public DynamicWindow(String imagePath, String title) {
		currentImage = SWTResourceManager.getImage(DynamicWindow.class, imagePath);
		display = Display.getCurrent();
		createContents(title);
	}

	public void bringToFront(final Shell shell) {
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				shell.forceActive();
			}
		});
	}

	/**
	 * Open the window.
	 */
	Display display;
	public void open() {
		bringToFront(shell);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private Table table;
	public Table getTable() {
		return table;
	}

	private Button btnAddNewLine;
	private Button btnRemoveLine;
	public Button getBtnAddNewLine() {
		return btnAddNewLine;
	}

	private Image currentImage;
	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	private void createContents(String title) {
		shell = new Shell(SWT.CLOSE & (~SWT.RESIZE) );
		shell.setSize(912, 304);
		shell.setText(title);
		shell.setImage(SWTResourceManager.getImage(DynamicWindow.class,"/resources/icons/x16/sitemap.png"));
		shell.setLayout(new FormLayout());
		shell.addListener(SWT.Traverse, new Listener(){
			@Override
			public void handleEvent(Event event) {
				switch (event.detail) {
				case SWT.TRAVERSE_ESCAPE:
					shell.close();
					event.detail = SWT.TRAVERSE_NONE;
					event.doit = false;
					break;
				}
			}
		});
		
		Composite composite = new Composite(shell, SWT.BORDER);
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0, 10);
		fd_composite.right = new FormAttachment(0, 895);
		fd_composite.left = new FormAttachment(0, 494);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		

		Label imgLabel = new Label(composite, SWT.HORIZONTAL | SWT.CENTER);
		imgLabel.setAlignment(SWT.CENTER);
		imgLabel.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		
		//Calculating the new Image size holding the aspect ratio
		Double w = (401.0/currentImage.getBounds().width);
		Double h = (220.0/currentImage.getBounds().height);

		double scale = Math.min(w, h);

		w = currentImage.getBounds().width * scale;
		h = currentImage.getBounds().height * scale;

		imgLabel.setImage(new Image(Display.getCurrent(),currentImage.getImageData().scaledTo(w.intValue(),h.intValue())));
		imgLabel.pack();
		composite.pack();

		this.table = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(0, 230);
		fd_table.right = new FormAttachment(0, 477);
		fd_table.top = new FormAttachment(0, 10);
		fd_table.left = new FormAttachment(0, 10);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		Button btnPressMe = new Button (shell, SWT.NONE);
		FormData fd_btnPressMe = new FormData();
		fd_btnPressMe.top = new FormAttachment(table, 6);
		fd_btnPressMe.right = new FormAttachment(table, 0, SWT.RIGHT);
		btnPressMe.setLayoutData(fd_btnPressMe);
		btnPressMe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				hashTable = new HashMap<String, ArrayList<Object[]>>();
				TableItem[] tis = table.getItems();
				Object ob;

				for(TableItem ti : tis){
					for(String field : dataFields){
						ob = ti.getData("text_"+field);
						if(ob != null){
							boolean reuse = ((Button)ti.getData("reuse_"+field)).getSelection(); 
							String text = ((Text)ti.getData("text_"+field)).getText();
							String stereotype = ((CCombo)ti.getData("stereotype_"+field)).getItem(((CCombo)ti.getData("stereotype_"+field)).getSelectionIndex());
							boolean active = ((Button)ti.getData("active_"+field)).getSelection(); 
							if(!hashTable.containsKey(field))
								hashTable.put(field,new ArrayList<Object[]>());
							hashTable.get(field).add(new Object[]{reuse,text,stereotype,active});
						}						
					}
				}
				shell.dispose();
			}
		});
		btnPressMe.setText("Create classes");
		btnPressMe.pack ();

		btnAddNewLine = new Button(shell, SWT.NONE);
		btnAddNewLine.setVisible(false);
		FormData fd_btnAddNewLine = new FormData();
		fd_btnAddNewLine.top = new FormAttachment(table, 6);
		fd_btnAddNewLine.left = new FormAttachment(0, 133);
		btnAddNewLine.setLayoutData(fd_btnAddNewLine);
		btnAddNewLine.setText("Add new line");

		btnRemoveLine = new Button(shell, SWT.NONE);
		btnRemoveLine.setVisible(false);
		fd_btnAddNewLine.right = new FormAttachment(btnRemoveLine, -6);
		FormData fd_btnRemoveLine = new FormData();
		fd_btnRemoveLine.right = new FormAttachment(btnPressMe, -65);
		fd_btnRemoveLine.left = new FormAttachment(0, 233);
		fd_btnRemoveLine.top = new FormAttachment(table, 6);
		btnRemoveLine.setLayoutData(fd_btnRemoveLine);
		btnRemoveLine.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				if(table.getItemCount() > initialItemCount){
					int i = table.getItemCount()-1;
					TableItem ti = table.getItem(i);
					for(TableEditor te : tableEditorHash.get(ti)){
						te.getEditor().dispose();
					}
					table.remove(i);
					if(currentImage != null)
						currentImage.dispose();
				}else{
					MessageBox dialog = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK );
					dialog.setText("Invalid action");
					dialog.setMessage("Insert some line before delete.");
					dialog.open(); 					
				}
			}
		});
		btnRemoveLine.setText("Remove Line");

		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setTouchEnabled(true);
		fd_composite.bottom = new FormAttachment(btnNewButton, -6);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.left = new FormAttachment(btnPressMe, 341);
		fd_btnNewButton.right = new FormAttachment(100, -11);
		fd_btnNewButton.top = new FormAttachment(0, 236);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ImagePreview.open(currentImage);
			}
		});
		btnNewButton.setText("Show Image");
		
		Button btnHelp = new Button(shell, SWT.CENTER);
		FormData fd_btnHelp = new FormData();
		fd_btnHelp.top = new FormAttachment(0, 236);
		fd_btnHelp.left = new FormAttachment(0, 10);
		btnHelp.setLayoutData(fd_btnHelp);
		btnHelp.setImage(SWTResourceManager.getImage(DynamicWindow.class,"/resources/icons/x16/help.png"));

		btnHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				Helper h = new Helper(usedStereotypes);
				h.open();
			}
		});
	}

	
	private int initialItemCount = 0;
	public void setInitialItemCount(int initialValue) {
		initialItemCount = initialValue;
	}

	public Button getBtnDeleteLine() {
		return btnRemoveLine;
	}

	private HashMap<TableItem, ArrayList<TableEditor>> tableEditorHash = new HashMap<>();
	public void addTableEditor(TableItem tableItem, ArrayList<TableEditor> editors){
		tableEditorHash.put(tableItem, editors);
	}

	private ArrayList<String> dataFields = new ArrayList<>();
	public void addDataField(String field){
		if(!dataFields.contains(field))
			dataFields.add(field);
	}
	
	private ArrayList<String> usedStereotypes = new ArrayList<>();
	public void addUsedStereotypes(String[] stereotypes) {
		for (String stereotype : stereotypes) {
			if(!usedStereotypes.contains(stereotype))
				usedStereotypes.add(stereotype);
		}
	}
}