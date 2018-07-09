package Uml.ClassDiagram;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class Inheritance extends Association{

    public Inheritance(Class superClass, Class subClass){
        super(superClass, subClass, EnumMultiplicity.ONE_TO_ONE);
    }

    public void save(FileWriter outfile){
        try{
            outfile.write("<Inheritance from=\""+ this.from.getName() + "\" to=\"" + this.to.getName() + "\" multiplicity=\"" + this.multiplicity + "\"/>\n");
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void paint(Graphics g){
        g.drawLine(from.getCenter().x, from.getBottom().y, from.getCenter().x - 5, from.getBottom().y + 10);
        g.drawLine(from.getCenter().x, from.getBottom().y, from.getCenter().x + 5, from.getBottom().y + 10);
        g.drawLine(from.getCenter().x - 5, from.getBottom().y + 10, from.getCenter().x + 5, from.getBottom().y + 10);        
        g.drawLine(from.getCenter().x, from.getBottom().y + 10, to.getCenter().x, to.getTop().y);
    }

    public String toString(){
        return to.getName() + " inherited from " + from.getName();
    }
}
