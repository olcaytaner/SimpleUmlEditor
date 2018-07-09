package Panel;

import Diagram.Diagram;
import Uml.UmlObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class DiagramPanel extends JPanel  implements MouseListener, MouseMotionListener {

    protected Diagram diagram;
    protected ArrayList<Diagram> undoList;
    protected UmlObject fromObject = null;
    protected boolean dragged = false;
    protected boolean moved = false;

    protected EnumCommand lastCommand;
    private UmlObject previousColored = null;
    private Point fromPoint;
    private Rectangle selectedArea = null;
    private String filename = null;

    public DiagramPanel(){
        undoList = new ArrayList<Diagram>();
    }

    public void mousePressed(MouseEvent e){
        if (lastCommand != null && lastCommand == EnumCommand.EMPTY){
            fromPoint = e.getPoint();
        }
    }

    public void mouseReleased(MouseEvent e){
        selectedArea = null;
        this.repaint();
    }

    public Diagram getDiagram(){
        return diagram;
    }

    public String getFileName(){
        return filename;
    }

    public void setFileName(String filename){
        this.filename = filename;
    }

    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e){}

    public void mouseDragged(MouseEvent e){
        dragged = true;
        if (lastCommand != null && lastCommand == EnumCommand.EMPTY){
            Point toPoint = e.getPoint();
            if (diagram.getUmlObjectAtPos(fromPoint) == null){
                selectedArea = new Rectangle(fromPoint.x, fromPoint.y, Math.abs(toPoint.x - fromPoint.x), Math.abs(toPoint.y - fromPoint.y));
                diagram.selectArea(selectedArea);
            } else {
                if (!moved){
                    save();
                }
                moved = true;
                diagram.moveSelected(toPoint.x - fromPoint.x, toPoint.y - fromPoint.y);
                fromPoint = toPoint;
            }
            this.repaint();
        }
    }
    
    public void mouseClicked(MouseEvent e){
        if (lastCommand != null && lastCommand == EnumCommand.EMPTY){
            UmlObject current;
            current = diagram.getUmlObjectAtPos(e.getPoint());
            if (current != null){
                current.select(!current.isSelected());
            } else {
                diagram.deselectAll();
            }
            this.repaint();            
        }
    }

    public void mouseMoved(MouseEvent e){
        UmlObject current;
        if (lastCommand != null && lastCommand == EnumCommand.EMPTY){
            current = diagram.getUmlObjectAtPos(e.getPoint());
            if (current == null){
                if (previousColored != null){
                    previousColored.colored = false;
                }
                previousColored = null;
            } else {
                if (current != previousColored){
                    current.colored = true;
                    if (previousColored != null){
                        previousColored.colored = false;
                    }
                    previousColored = current;
                }
            }
            this.repaint();
        }
    }

    public boolean canUndo(){
        return (undoList.size() != 0);
    }

    public void save(){
    }

    public void undo(){
    }

    public void setCommand(EnumCommand lastCommand){
        this.lastCommand = lastCommand;
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if (selectedArea != null){
            g.drawRect(selectedArea.x, selectedArea.y, selectedArea.width, selectedArea.height);
        }
    }

}
