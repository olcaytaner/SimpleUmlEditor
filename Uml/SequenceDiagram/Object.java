package Uml.SequenceDiagram;

import Uml.SimpleUmlObject;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class Object extends SimpleUmlObject {

    static final int RECTANGLE_HEIGHT = 40;

    public Object(String name, Point position){
        super(name, position);
    }

    public Object clone(){
        return new Object(name, new Point(position));
    }

    public void save(FileWriter outfile){
        try{
            outfile.write("<Object name=\""+ this.name + "\" positionX=\"" + this.position.x + "\" positionY=\"" + this.position.y + "\"/>\n");
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void paint(Graphics g){
        super.paint(g);
        int stringSize = g.getFontMetrics().stringWidth(name);
        g.drawRect(position.x, position.y, stringSize + 10, RECTANGLE_HEIGHT);
        g.drawString(name, position.x + 5, position.y + RECTANGLE_HEIGHT / 2 + 5);
        setBoundingBoxSize(stringSize + 10, RECTANGLE_HEIGHT);
        g.drawRect(getCenter().x - 2, position.y + RECTANGLE_HEIGHT, 5, 400);
    }

}
