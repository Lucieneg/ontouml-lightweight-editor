/**
 * Copyright 2011 NEMO (http://nemo.inf.ufes.br/en)
 *
 * This file is part of OLED (OntoUML Lightweight BaseEditor).
 * OLED is based on TinyUML and so is distributed under the same
 * licence terms.
 *
 * OLED is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * OLED is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OLED; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package br.ufes.inf.nemo.oled.ui.diagram;

import java.awt.geom.Point2D;

import RefOntoUML.Association;
import RefOntoUML.Classifier;
import RefOntoUML.Generalization;
import br.ufes.inf.nemo.oled.draw.DiagramElement;
import br.ufes.inf.nemo.oled.draw.DrawingContext;
import br.ufes.inf.nemo.oled.draw.LineConnectMethod;
import br.ufes.inf.nemo.oled.draw.NullElement;
import br.ufes.inf.nemo.oled.model.RelationType;
import br.ufes.inf.nemo.oled.ui.diagram.commands.AddConnectionCommand;
import br.ufes.inf.nemo.oled.umldraw.shared.UmlConnection;
import br.ufes.inf.nemo.oled.umldraw.shared.UmlNode;
import br.ufes.inf.nemo.oled.umldraw.structure.AssociationElement;
import br.ufes.inf.nemo.oled.umldraw.structure.ClassElement;
import br.ufes.inf.nemo.oled.util.ModelHelper;

/**
 * This class is a handler for line shaped allElements.
 *
 * @author Wei-ju Wu, Antognoni Albuquerque
 * @version 1.1
 */
public class LineHandler implements EditorMode {

  private DiagramEditor editor;
  private Point2D anchor = new Point2D.Double();
  private Point2D tmpPos = new Point2D.Double();
  private DiagramElement source;
  private boolean isDragging;
  private RelationType relationType;
  private LineConnectMethod connectMethod;

  /**
   * Constructor.
   * @param anEditor a DiagramEditor
   */
  public LineHandler(DiagramEditor anEditor) {
    editor = anEditor;
  }

  /**
   * Sets the association type.
   * @param anAssociationType the association type
   * @param aConnectMethod the connect method
   */
  public void setRelationType(RelationType anAssociationType,
    LineConnectMethod aConnectMethod) {
    connectMethod = aConnectMethod;
    relationType = anAssociationType;   
  }

  /**
   * Returns the isDragging property for testing.
   * @return the status for the isDragging property
   */
  public boolean isDragging() { return isDragging; }

  /**
   * {@inheritDoc}
   */
  public void stateChanged() { }

  /**
   * {@inheritDoc}
   */
  public void cancel() { isDragging = false; }
  
  /**
   * {@inheritDoc}
   */
  public void mousePressed(EditorMouseEvent event) {
    double mx = event.getX(), my = event.getY();
    DiagramElement elem = editor.getDiagram().getChildAt(mx, my);
    if (elem!=null && ! (elem instanceof NullElement)) {
      anchor.setLocation(mx, my); //TODO Change the anchor to the edge of the Diagram Element
      tmpPos.setLocation(mx, my);
      isDragging = true;
      if (elem instanceof UmlNode)
    	  source = (UmlNode) elem;
      else
    	  source = (UmlConnection) elem;
    }
  }

  /**
   * {@inheritDoc}
   * FIXME Allow Self-Relationships !
   *
   */
  public void mouseReleased(EditorMouseEvent event) {
    double mx = event.getX(), my = event.getY();
    DiagramElement target = editor.getDiagram().getChildAt(mx, my);    
    tmpPos.setLocation(mx, my);
    if(source !=null && target !=null){
    	UmlConnection conn = createConnection(editor, connectMethod, relationType, source, target, anchor, tmpPos); 
    	addConnection(editor, conn, source, target);
    }
    isDragging = false;
    editor.redraw();
  }
  
  /**
   * Create a connection and add it to the Diagram.
   */
  public UmlConnection connect(DiagramEditor editor, RelationType relationType, DiagramElement source, DiagramElement target)
  {
	  Point2D sourcePoint = new Point2D.Double();
	  Point2D targetPoint = new Point2D.Double();
	  
	  if(source instanceof ClassElement) sourcePoint.setLocation(((ClassElement)source).getAbsCenterX(),((ClassElement)source).getAbsCenterY());
	  if(target instanceof ClassElement) targetPoint.setLocation(((ClassElement)target).getAbsCenterX(),((ClassElement)target).getAbsCenterY());
	  if(source instanceof AssociationElement) sourcePoint.setLocation(((AssociationElement)source).getAbsCenterX(),((AssociationElement)source).getAbsCenterY());
	  if(target instanceof AssociationElement) targetPoint.setLocation(((AssociationElement)target).getAbsCenterX(),((AssociationElement)target).getAbsCenterY());	  
	  
	  LineConnectMethod connectMethod = editor.getDiagram().getElementFactory().getConnectMethod(relationType);
	  
	  UmlConnection conn = createConnection(editor, connectMethod, relationType, source, target, sourcePoint, targetPoint);    
	  
	  addConnection(editor, conn, source, target);  
	  
	  editor.redraw();
	  
	  return conn;
  }

  /**
   * Add Connection
   */
  public void addConnection(DiagramEditor editor, UmlConnection conn, DiagramElement source, DiagramElement target)
  {	    
	    RefOntoUML.Classifier aSource = null;
	    RefOntoUML.Classifier aTarget = null;
	    
		//UmlConnection ->(connectedTo) -> UmlNode
		if (source instanceof UmlConnection && target instanceof UmlNode)
		{
			aSource =  (Classifier) ((AssociationElement)source).getRelationship(); 
	    	aTarget =  (Classifier) ((UmlNode)target).getClassifier();
		}
	    // UmlNode ->(connectedTo) -> UmlConnection
		if (source instanceof UmlNode && target instanceof UmlConnection)
		{
			//invert sides if derivation is pushed from the UmlNode (relator), it should be from the UmlConnection (material)
			if (relationType == RelationType.DERIVATION) { 
				aSource =  (Classifier) ((AssociationElement)target).getRelationship(); 
		    	aTarget =  (Classifier) ((UmlNode)source).getClassifier();
			}
		}
	    // UmlNode ->(connectedTo) -> UmlNode
	    if (source instanceof UmlNode && target instanceof UmlNode)
	    {
		    //invert sides if characterization is pushed from a UmlNode that is not a Mode. It should be from a Mode.
		    if ((relationType == RelationType.CHARACTERIZATION && ! (((UmlNode)source).getClassifier() instanceof RefOntoUML.Mode) && (((UmlNode)target).getClassifier() instanceof RefOntoUML.Mode)) ||  
		       (relationType == RelationType.MEDIATION && ! (((UmlNode)source).getClassifier() instanceof RefOntoUML.Relator) && (((UmlNode)target).getClassifier() instanceof RefOntoUML.Relator)) )
		    {				  
		    	aSource =  (Classifier) ((UmlNode)target).getClassifier(); 
		    	aTarget =  (Classifier) ((UmlNode)source).getClassifier();
		    }else{
		    	aSource =  (Classifier) ((UmlNode)source).getClassifier(); 
		    	aTarget =  (Classifier) ((UmlNode)target).getClassifier();
		    }		    	
		}
			  
	    if(aSource !=null && aTarget != null){
	    	AddConnectionCommand command = new AddConnectionCommand(editor, editor.getDiagram(), conn.getRelationship(), aSource, aTarget, editor.getDiagram().getProject(),null);
	    	editor.execute(command);
	    }
  }
    
  /**
   * Create connection cloning
   */
  public UmlConnection createCloning(RelationType relationType, RefOntoUML.Relationship toBeCloned)
  {
	  RefOntoUML.Type sourceType = null;
	  RefOntoUML.Type targetType = null;
	  
	  if(toBeCloned instanceof Generalization){
		  sourceType = ((Generalization)toBeCloned).getSpecific();
		  targetType = ((Generalization)toBeCloned).getGeneral();
	  }else if (toBeCloned instanceof Association){
		  sourceType = ((Association)toBeCloned).getMemberEnd().get(0).getType();
		  targetType = ((Association)toBeCloned).getMemberEnd().get(1).getType();
	  }	  
	  
	  if(sourceType!=null && targetType!=null){
		  
		  DiagramElement source = ModelHelper.getDiagramElement(sourceType);
		  DiagramElement target = ModelHelper.getDiagramElement(targetType);
	    
		  if(source!=null && target !=null)
		  {
			  Point2D sourcePoint = new Point2D.Double();
			  Point2D targetPoint = new Point2D.Double();
			  
			  if(source instanceof ClassElement) sourcePoint.setLocation(((ClassElement)source).getAbsCenterX(),((ClassElement)source).getAbsCenterY());
			  if(target instanceof ClassElement) targetPoint.setLocation(((ClassElement)target).getAbsCenterX(),((ClassElement)target).getAbsCenterY());
			  if(source instanceof AssociationElement) sourcePoint.setLocation(((AssociationElement)source).getAbsCenterX(),((AssociationElement)source).getAbsCenterY());
			  if(target instanceof AssociationElement) targetPoint.setLocation(((AssociationElement)target).getAbsCenterX(),((AssociationElement)target).getAbsCenterY());	  
			  
			  LineConnectMethod connectMethod = editor.getDiagram().getElementFactory().getConnectMethod(relationType);
			  
			  UmlConnection conn = createConnection(editor, connectMethod, relationType, source, target, sourcePoint, targetPoint); 
			  
			  return conn;
		  }	  
	  }
	  
	  return null;
  }
  
  /**
   * Create connection.
   */
  public UmlConnection createConnection (DiagramEditor editor, LineConnectMethod connectMethod, RelationType relationType, DiagramElement source, DiagramElement target, Point2D anchor, Point2D tmpPos)
  {
	  UmlConnection conn = null;
	  
	  //UmlConnection ->(connectedTo) -> UmlNode
	  if (source instanceof UmlConnection && target instanceof UmlNode)
	  {
		  conn = editor.getDiagram().getElementFactory().createConnectionFromCon(relationType, (UmlConnection) source, (UmlNode) target);		
	  	  connectMethod.generateAndSetPointsToConnection(conn, (UmlConnection)source,  (UmlNode)target, anchor, tmpPos);	
	  }
	  // UmlNode ->(connectedTo) -> UmlConnection
	  if (source instanceof UmlNode && target instanceof UmlConnection)
	  {
		  //invert sides if derivation is pushed from the UmlNode (relator), it should be from the UmlConnection (material)
		  if (relationType == RelationType.DERIVATION) { 
			  conn = editor.getDiagram().getElementFactory().createConnectionFromCon(relationType, (UmlConnection) target, (UmlNode) source);   
              connectMethod.generateAndSetPointsToConnection(conn, (UmlConnection) target, (UmlNode)source, anchor, tmpPos); 
		  }
	  }
	  // UmlNode ->(connectedTo) -> UmlNode
	  if (source instanceof UmlNode && target instanceof UmlNode)
	  {
		  //invert sides if characterization is pushed from a UmlNode that is not a Mode. It should be from a Mode.
		  if ((relationType == RelationType.CHARACTERIZATION && ! (((UmlNode)source).getClassifier() instanceof RefOntoUML.Mode) && (((UmlNode)target).getClassifier() instanceof RefOntoUML.Mode)) ||  
		     (relationType == RelationType.MEDIATION && ! (((UmlNode)source).getClassifier() instanceof RefOntoUML.Relator) && (((UmlNode)target).getClassifier() instanceof RefOntoUML.Relator)) )
		  {
			  conn = editor.getDiagram().getElementFactory().createConnection(relationType, (UmlNode) target, (UmlNode) source);
		      connectMethod.generateAndSetPointsToConnection(conn, (UmlNode) target, (UmlNode)source, anchor, tmpPos);
		  }else{
			  conn = editor.getDiagram().getElementFactory().createConnection(relationType, (UmlNode) source, (UmlNode) target);
		      connectMethod.generateAndSetPointsToConnection(conn, (UmlNode) source, (UmlNode)target, anchor, tmpPos);
		    }	
	  }
	  if (conn!=null){
		  //Add mapping from the refontouml element to the diagram element		  
		  ModelHelper.addMapping(((UmlConnection)conn).getRelationship(), conn);
	  }
	  return conn;
  }
  
  /**
   * {@inheritDoc}
   */
  public void mouseClicked(EditorMouseEvent event) { }


  /**
   * {@inheritDoc}
   */
  public void mouseDragged(EditorMouseEvent event) {
    double mx = event.getX(), my = event.getY();
    tmpPos.setLocation(mx, my);
    editor.redraw();
  }

  /**
   * {@inheritDoc}
   */
  public void mouseMoved(EditorMouseEvent event) { }

  /**
   * {@inheritDoc}
   */
  public void draw(DrawingContext drawingContext) {
    if (isDragging) {
      connectMethod.drawLineSegments(drawingContext, anchor, tmpPos);
    }
  }
}
