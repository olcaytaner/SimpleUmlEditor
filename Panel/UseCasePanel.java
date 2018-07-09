package Panel;

import Diagram.UseCaseDiagram;
import Uml.SimpleUmlObject;
import Uml.UmlObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class UseCasePanel extends DiagramPanel{

    public UseCasePanel(){
        super();        
        diagram = new UseCaseDiagram();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void mousePressed(MouseEvent e){
        UmlObject current;
        super.mousePressed(e);
        fromObject = null;
        if (lastCommand != null && lastCommand == EnumCommand.CONNECTION){
            current = diagram.getUmlObjectAtPos(e.getPoint());
            if (current != null){
                String objectName = current.getClass().getName();
                if (objectName.equalsIgnoreCase("Uml.UseCase.Actor") || objectName.equalsIgnoreCase("Uml.UseCase.UseCase")){
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
        if (lastCommand != null && lastCommand == EnumCommand.CONNECTION && fromObject != null){
            toObject = diagram.getUmlObjectAtPos(e.getPoint());
            if (toObject != null){
                String objectName = toObject.getClass().getName();
                if (objectName.equalsIgnoreCase("Uml.UseCase.Actor") || objectName.equalsIgnoreCase("Uml.UseCase.UseCase")){
                    String relationshipType = JOptionPane.showInputDialog(null, "Enter Relationship Type", "Uml Editor", JOptionPane.QUESTION_MESSAGE);
                    if (relationshipType != null){
                        save();
                        ((UseCaseDiagram) diagram).addConnection(fromObject, toObject, relationshipType);
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
                if (objectName.equalsIgnoreCase("Uml.UseCase.Actor")){
                    String actorName = JOptionPane.showInputDialog(null, "Enter Actor Name", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                    if (actorName != null){
                        save();
                        ((SimpleUmlObject)current).setName(actorName);
                        this.repaint();
                    }
                } else {
                    if (objectName.equalsIgnoreCase("Uml.UseCase.UseCase")){
                        String useCaseName = JOptionPane.showInputDialog(null, "Enter Use Case Name", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                        if (useCaseName != null){
                            save();
                            ((SimpleUmlObject)current).setName(useCaseName);
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
                        ((UseCaseDiagram) diagram).addActor(actorName, e.getPoint());
                        this.repaint();
                    }
                    break;
                case USE_CASE:
                    String useCaseName = JOptionPane.showInputDialog(null, "Enter Use Case Name", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                    if (useCaseName != null){
                        save();
                        ((UseCaseDiagram) diagram).addUseCase(useCaseName, e.getPoint());
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
        ((UseCaseDiagram)diagram).paint(g);
    }

}
