package Uml.SequenceDiagram;

import Uml.SimpleUmlObject;
import Uml.UmlObject;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class Message extends UmlObject {

    private SimpleUmlObject from;
    private SimpleUmlObject to;
    private String message;
    private int index;

    static final int MESSAGE_HEIGHT = 40;

    public Message(SimpleUmlObject from, SimpleUmlObject to, String message, int index){
        this.from = from;
        this.to = to;
        this.message = message;
        this.index = index;
        this.boundingBox = new Rectangle(from.getCenter());        
        this.setBoundingBoxSize(Math.abs(from.getCenter().x - to.getCenter().x), Math.abs(from.getCenter().y - to.getCenter().y));
    }

    public Message clone(){
        return new Message(from, to, message, index);
    }

    public void save(FileWriter outfile){
        try{
            outfile.write("<Message from=\""+ this.from.getName() + "\" to=\"" + this.to.getName() + "\" index=\"" + this.index + "\" text=\"" + this.message + "\"/>\n");
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void move(int deltaX, int deltaY){
        this.boundingBox = new Rectangle(from.getCenter());
        this.setBoundingBoxSize(Math.abs(from.getCenter().x - to.getCenter().x), Math.abs(from.getCenter().y - to.getCenter().y));
    }

    public void paint(Graphics g){
        int stringSize = g.getFontMetrics().stringWidth(message);        
        int centerx = (from.getCenter().x + to.getCenter().x) / 2, centery = (from.getBottom().y + to.getBottom().y) / 2 + index * MESSAGE_HEIGHT;
        super.paint(g);
        g.drawLine(from.getCenter().x, from.getBottom().y + index * MESSAGE_HEIGHT, to.getCenter().x, to.getBottom().y + index * MESSAGE_HEIGHT);
        g.fillRect(to.getCenter().x - 2, to.getBottom().y + index * MESSAGE_HEIGHT - 2, 5, 5);
        g.drawString(message, centerx - stringSize / 2, centery - 10);
    }

    public String toString(){
        return from.getName() + " to " + to.getName() + " with " + message;
    }

}
