package Panel;

import Diagram.SequenceDiagram;
import Uml.SimpleUmlObject;
import Uml.UmlObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SequencePanel extends DiagramPanel {

    public SequencePanel(){
        super();        
        diagram = new SequenceDiagram();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void mousePressed(MouseEvent e){
        UmlObject current;
        super.mousePressed(e);
        fromObject = null;
        if (lastCommand != null && lastCommand == EnumCommand.MESSAGE){
            current = diagram.getUmlObjectAtPos(e.getPoint());
            if (current != null){
                String objectName = current.getClass().getName();
                if (objectName.equalsIgnoreCase("Uml.SequenceDiagram.Actor") || objectName.equalsIgnoreCase("Uml.SequenceDiagram.Object")){
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
        if (lastCommand != null && lastCommand == EnumCommand.MESSAGE && fromObject != null){
            toObject = diagram.getUmlObjectAtPos(e.getPoint());
            if (toObject != null){
                String objectName = toObject.getClass().getName();
                if (objectName.equalsIgnoreCase("Uml.SequenceDiagram.Actor") || objectName.equalsIgnoreCase("Uml.SequenceDiagram.Object")){
                    String message = JOptionPane.showInputDialog(null, "Enter Message", "Uml Editor", JOptionPane.QUESTION_MESSAGE);
                    if (message != null){
                        save();
                        ((SequenceDiagram) diagram).addMessage(fromObject, toObject, message);
                        this.repaint();                        
                    }
                }
            }
        }
    }

    public void mouseClicked(MouseEvent e){
        super.mouseClicked(e);
        if (lastCommand == EnumCommand.EMPTY && e.getClickCount() == 2){
            UmlObject current = diagram.getUmlObjectAtPos(e.getPoint());
            if (current != null){
                String objectName = current.getClass().getName();
                if (objectName.equalsIgnoreCase("Uml.SequenceDiagram.Actor")){
                    String actorName = JOptionPane.showInputDialog(null, "Enter Actor Name", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                    if (actorName != null){
                        save();
                        ((SimpleUmlObject)current).setName(actorName);
                        this.repaint();
                    }
                } else {
                    if (objectName.equalsIgnoreCase("Uml.SequenceDiagram.Object")){
                        String className = JOptionPane.showInputDialog(null, "Enter Class Name", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                        if (className != null){
                            save();
                            ((SimpleUmlObject)current).setName(className);
                            this.repaint();
                        }
                    }
                }
            }
        }
        if (lastCommand != null && lastCommand != EnumCommand.EMPTY){
            switch (lastCommand){
                case ACTOR:
                    String actorName = JOptionPane.showInputDialog(null, "Enter Actor Name", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                    if (actorName != null){
                        save();
                        ((SequenceDiagram) diagram).addActor(actorName, e.getPoint());
                        this.repaint();
                    }
                    break;
                case CLASS:
                    String className = JOptionPane.showInputDialog(null, "Enter Class Name", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                    if (className != null){
                        save();
                        ((SequenceDiagram) diagram).addObject(className, e.getPoint());
                        this.repaint();
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
        ((SequenceDiagram)diagram).paint(g);
    }

}
