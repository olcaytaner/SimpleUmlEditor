package Uml.UseCase;

import Uml.SimpleUmlObject;
import Uml.UmlObject;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class Connection extends UmlObject {

    private SimpleUmlObject from;
    private SimpleUmlObject to;
    private EnumRelationship type;

    public Connection(SimpleUmlObject from, SimpleUmlObject to, EnumRelationship type){
        this.from = from;
        this.to = to;
        this.type = type;
        this.boundingBox = new Rectangle(from.getCenter());
        this.setBoundingBoxSize(Math.abs(from.getCenter().x - to.getCenter().x), Math.abs(from.getCenter().y - to.getCenter().y));
    }

    public Connection clone(){
        return new Connection(from, to, type);
    }

    public void save(FileWriter outfile){
        try{
            outfile.write("<Connection from=\""+ this.from.getName() + "\" to=\"" + this.to.getName() + "\" type=\"" + this.type + "\"/>\n");
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
        int centerx = (from.getCenter().x + to.getCenter().x) / 2, centery = (from.getCenter().y + to.getCenter().y) / 2;
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        Stroke strokeOriginal = g2.getStroke();
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, null, 0.0f));
        switch (type){
            case NO:break;
            case INITIATE:
                g2.drawString("Initiate", centerx, centery - 10);
                break;
            case PARTICIPATE:
                g2.drawString("Participate", centerx, centery - 10);
                break;
            case INCLUDE:
                g2.setStroke (new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] { 8.0f, 3.0f}, 0.0f));
                g2.drawString("Include", centerx, centery - 10);
                break;
            case EXTEND:
                g2.setStroke (new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] { 8.0f, 3.0f}, 0.0f));
                g2.drawString("Extend", centerx, centery - 10);
                break;
            case INHERITANCE:
                g2.drawString("Inheritance", centerx, centery - 10);
                break;
        }
        g2.drawLine(from.getCenter().x, from.getCenter().y, to.getCenter().x, to.getCenter().y);
        g2.setStroke(strokeOriginal);
    }


    public String toString(){
        return from.getName() + " to " + to.getName();
    }
}
