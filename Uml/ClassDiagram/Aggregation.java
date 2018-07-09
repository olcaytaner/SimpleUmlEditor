package Uml.ClassDiagram;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class Aggregation extends Association{

    public Aggregation(Class composition, Class contents){
        super(composition, contents, EnumMultiplicity.ONE_TO_MANY);
    }

    public void save(FileWriter outfile){
        try{
            outfile.write("<Aggregation from=\""+ this.from.getName() + "\" to=\"" + this.to.getName() + "\" multiplicity=\"" + this.multiplicity + "\"/>\n");
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void paint(Graphics g){
        g.drawLine(from.getCenter().x, from.getBottom().y, from.getCenter().x - 5, from.getBottom().y + 10);
        g.drawLine(from.getCenter().x, from.getBottom().y, from.getCenter().x + 5, from.getBottom().y + 10);
        g.drawLine(from.getCenter().x - 5, from.getBottom().y + 10, from.getCenter().x, from.getBottom().y + 20);
        g.drawLine(from.getCenter().x + 5, from.getBottom().y + 10, from.getCenter().x, from.getBottom().y + 20);
        g.drawLine(from.getCenter().x, from.getBottom().y + 20, to.getCenter().x, to.getTop().y);
    }

}
