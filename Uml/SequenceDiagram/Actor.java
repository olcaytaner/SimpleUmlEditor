package Uml.SequenceDiagram;

import Uml.SimpleUmlObject;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class Actor extends SimpleUmlObject {

    static final private int HEAD_LENGTH = 20;

    public Actor(String name, Point position){
        super(name, position);
        setBoundingBoxSize(HEAD_LENGTH, 7 * HEAD_LENGTH / 2);
    }

    public Actor clone(){
        return new Actor(name, new Point(position));
    }

    public void save(FileWriter outfile){
        try{
            outfile.write("<Actor name=\""+ this.name + "\" positionX=\"" + this.position.x + "\" positionY=\"" + this.position.y + "\"/>\n");
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void paint(Graphics g){
        super.paint(g);
        int stringSize = g.getFontMetrics().stringWidth(name);
        g.drawOval(position.x, position.y, HEAD_LENGTH, HEAD_LENGTH);
        g.drawLine(position.x + HEAD_LENGTH / 2, position.y + HEAD_LENGTH, position.x + HEAD_LENGTH / 2, position.y + 3 * HEAD_LENGTH);
        g.drawLine(position.x + HEAD_LENGTH / 2, position.y + HEAD_LENGTH, position.x, position.y + 3 * HEAD_LENGTH / 2);
        g.drawLine(position.x + HEAD_LENGTH / 2, position.y + HEAD_LENGTH, position.x + HEAD_LENGTH, position.y + 3 * HEAD_LENGTH / 2);
        g.drawLine(position.x + HEAD_LENGTH / 2, position.y + 3 * HEAD_LENGTH, position.x, position.y + 7 * HEAD_LENGTH / 2);
        g.drawLine(position.x + HEAD_LENGTH / 2, position.y + 3 * HEAD_LENGTH, position.x + HEAD_LENGTH, position.y + 7 * HEAD_LENGTH / 2);
        g.drawString(name, position.x + HEAD_LENGTH / 2 - stringSize / 2, position.y + 9 * HEAD_LENGTH / 2);
        g.drawRect(getCenter().x - 2, position.y + 5 * HEAD_LENGTH, 5, 400);
    }

}
