package Uml.ClassDiagram;

import Uml.UmlObject;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class Association extends UmlObject{

    protected Class from;
    protected Class to;
    protected EnumMultiplicity multiplicity;

    public Association(Class from, Class to, EnumMultiplicity multiplicity){
        this.from = from;
        this.to = to;
        this.multiplicity = multiplicity;
        this.boundingBox = new Rectangle(from.getCenter());
        this.setBoundingBoxSize(Math.abs(from.getCenter().x - to.getCenter().x), Math.abs(from.getCenter().y - to.getCenter().y));
    }

    public Association clone(){
        return new Association(from, to, multiplicity);
    }

    public void save(FileWriter outfile){
        try{
            outfile.write("<Association from=\""+ this.from.getName() + "\" to=\"" + this.to.getName() + "\" multiplicity=\"" + this.multiplicity + "\"/>\n");
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
        super.paint(g);
        if (from.getBottom().y < to.getTop().y){
            switch (multiplicity){
                case ONE_TO_ONE:
                    g.drawString("1", from.getCenter().x - 5, from.getBottom().y + 15);
                    g.drawString("1", to.getCenter().x - 5, to.getTop().y - 5);
                    break;
                case ONE_TO_MANY:
                    g.drawString("1", from.getCenter().x - 5, from.getBottom().y + 15);
                    g.drawString("*", to.getCenter().x - 5, to.getTop().y - 5);
                    break;
                case MANY_TO_MANY:
                    g.drawString("*", from.getCenter().x - 5, from.getBottom().y + 15);
                    g.drawString("*", to.getCenter().x - 5, to.getTop().y - 5);
                    break;
            }
            g.drawLine(from.getCenter().x, from.getBottom().y, to.getCenter().x, to.getTop().y);
        } else {
            switch (multiplicity){
                case ONE_TO_ONE:
                    g.drawString("1", from.getCenter().x - 5, from.getTop().y - 5);
                    g.drawString("1", to.getCenter().x - 5, to.getBottom().y + 15);
                    break;
                case ONE_TO_MANY:
                    g.drawString("1", from.getCenter().x - 5, from.getTop().y - 5);
                    g.drawString("*", to.getCenter().x - 5, to.getBottom().y + 15);
                    break;
                case MANY_TO_MANY:
                    g.drawString("*", from.getCenter().x - 5, from.getTop().y - 5);
                    g.drawString("*", to.getCenter().x - 5, to.getBottom().y + 15);
                    break;
            }
            g.drawLine(from.getCenter().x, from.getTop().y, to.getCenter().x, to.getBottom().y);
        }
    }

    public String toString(){
        return from.getName() + " to " + to.getName();
    }
}
