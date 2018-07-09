package Panel;

import Diagram.ClassDiagram;
import Uml.ClassDiagram.EnumMultiplicity;
import Uml.SimpleUmlObject;
import Uml.UmlObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class ClassPanel extends DiagramPanel {

    public ClassPanel(){
        super();
        diagram = new ClassDiagram();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void mousePressed(MouseEvent e){
        UmlObject current;
        super.mousePressed(e);
        fromObject = null;
        if (lastCommand != null && (lastCommand == EnumCommand.CONNECTION || lastCommand == EnumCommand.INHERITANCE || lastCommand == EnumCommand.AGGREGATION)){
            current = diagram.getUmlObjectAtPos(e.getPoint());
            if (current != null){
                String objectName = current.getClass().getName();
                if (objectName.equalsIgnoreCase("Uml.ClassDiagram.Class")){
                    fromObject = current;
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e){
        UmlObject toObject;
        super.mouseReleased(e);
        dragged = false;
        moved = false;
        if (lastCommand != null && (lastCommand == EnumCommand.CONNECTION || lastCommand == EnumCommand.INHERITANCE || lastCommand == EnumCommand.AGGREGATION) && fromObject != null){
            toObject = diagram.getUmlObjectAtPos(e.getPoint());
            if (toObject != null){
                String objectName = toObject.getClass().getName();
                if (objectName.equalsIgnoreCase("Uml.ClassDiagram.Class")){
                    save();
                    switch (lastCommand){
                        case CONNECTION:
                            String multiplicity = JOptionPane.showInputDialog(null, "Enter Multiplicity", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                            if (multiplicity.equalsIgnoreCase("onetoone")){
                                ((ClassDiagram) diagram).addAssociation((Uml.ClassDiagram.Class)fromObject, (Uml.ClassDiagram.Class)toObject, EnumMultiplicity.ONE_TO_ONE);
                            } else {
                                if (multiplicity.equalsIgnoreCase("onetomany")){
                                    ((ClassDiagram) diagram).addAssociation((Uml.ClassDiagram.Class)fromObject, (Uml.ClassDiagram.Class)toObject, EnumMultiplicity.ONE_TO_MANY);
                                } else {
                                    if (multiplicity.equalsIgnoreCase("manytomany")){
                                        ((ClassDiagram) diagram).addAssociation((Uml.ClassDiagram.Class)fromObject, (Uml.ClassDiagram.Class)toObject, EnumMultiplicity.MANY_TO_MANY);
                                    }                                    
                                }
                            }
                            break;
                        case INHERITANCE:
                            ((ClassDiagram) diagram).addInheritance((Uml.ClassDiagram.Class)fromObject, (Uml.ClassDiagram.Class)toObject);
                            break;
                        case AGGREGATION:
                            ((ClassDiagram) diagram).addAggregation((Uml.ClassDiagram.Class)fromObject, (Uml.ClassDiagram.Class)toObject);
                            break;
                    }
                    this.repaint();
                }
            }
        }
    }

    public void mouseClicked(MouseEvent e){
        UmlObject current;
        super.mouseClicked(e);
        if (lastCommand == EnumCommand.EMPTY && e.getClickCount() == 2){
            current = diagram.getUmlObjectAtPos(e.getPoint());
            if (current != null){
                String objectName = current.getClass().getName();
                if (objectName.equalsIgnoreCase("Uml.ClassDiagram.Class")){
                    String className = JOptionPane.showInputDialog(null, "Enter Class Name", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                    if (className != null){
                        save();
                        ((SimpleUmlObject)current).setName(className);
                        this.repaint();
                    }
                }
            }
        }
        if (lastCommand != null && lastCommand != EnumCommand.EMPTY){
            switch (lastCommand){
                case CLASS:
                    String className = JOptionPane.showInputDialog(null, "Enter Class Name", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                    if (className != null){
                        save();
                        ((ClassDiagram) diagram).addClass(className, e.getPoint());
                        this.repaint();
                    }
                    break;
                case ATTRIBUTE:
                    current = diagram.getUmlObjectAtPos(e.getPoint());
                    if (current != null){
                        String objectName = current.getClass().getName();
                        if (objectName.equalsIgnoreCase("Uml.ClassDiagram.Class")){
                            String attributeName = JOptionPane.showInputDialog(null, "Enter Attribute Name", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                            String attributeType = JOptionPane.showInputDialog(null, "Enter Attribute Type", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                            if (attributeName != null && attributeType != null){
                                save();
                                ((Uml.ClassDiagram.Class)current).addAttribute(attributeName, attributeType);
                                this.repaint();
                            }
                        }
                    }
                    break;
                case METHOD:
                    current = diagram.getUmlObjectAtPos(e.getPoint());
                    if (current != null){
                        String objectName = current.getClass().getName();
                        if (objectName.equalsIgnoreCase("Uml.ClassDiagram.Class")){
                            String methodName = JOptionPane.showInputDialog(null, "Enter Method Name", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                            String methodAttributes = JOptionPane.showInputDialog(null, "Enter Method Attribute List", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                            String methodReturnType = JOptionPane.showInputDialog(null, "Enter Method Return Type", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                            if (methodName != null && methodAttributes != null && methodReturnType != null){
                                save();
                                ((Uml.ClassDiagram.Class)current).addMethod(methodName, methodAttributes, methodReturnType);
                                this.repaint();
                            }
                        }
                    }
                    break;
            }
        }
    }

    public void save(){
        undoList.add(diagram.clone());
    }

    public void undo(){
        if (undoList.size() != 0){
            diagram = undoList.remove(undoList.size() - 1);
        }
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        ((ClassDiagram)diagram).paint(g);
    }
    
}
