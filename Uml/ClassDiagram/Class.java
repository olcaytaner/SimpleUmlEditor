package Uml.ClassDiagram;

import Uml.SimpleUmlObject;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Class extends SimpleUmlObject{

    static final int RECTANGLE_HEIGHT = 40;

    private ArrayList<Attribute> attributes;
    private ArrayList<Method> methods;
    private ArrayList<Contract> invariants;

    public Class(String name, Point position){
        super(name, position);
        attributes = new ArrayList<Attribute>();
        methods = new ArrayList<Method>();
        invariants = new ArrayList<Contract>();
    }

    public Class clone(){
        int i;
        Class newClass = new Class(name, new Point(position));
        for (i = 0; i < attributes.size(); i++){
            newClass.addAttribute(attributes.get(i).getName(), attributes.get(i).getType());
        }
        for (i = 0; i < methods.size(); i++){
            newClass.addMethod(methods.get(i).getName(), methods.get(i).getParameters(), methods.get(i).getReturnType());
        }
        return newClass;
    }

    public void addAttribute(String name, String type){
        attributes.add(new Attribute(name, type, EnumVisibility.PUBLIC));
    }

    public void addMethod(String name, String attributes, String returnType){
        methods.add(new Method(name, attributes, returnType, EnumVisibility.PUBLIC));
    }

    public void generateCode(String filename){
        int i;
        FileWriter outfile;
        try{
            outfile = new FileWriter(filename);
            outfile.write("public class " + name + " {\n");
            outfile.write("\n");
            for (i = 0; i < attributes.size(); i++){
                outfile.write("\t" + attributes.get(i).toString() + ";\n");
            }
            outfile.write("\n");
            for (i = 0; i < methods.size(); i++){
                outfile.write("\t" + methods.get(i).toString() + "{\n");
                outfile.write("\t\n");
                outfile.write("\t}\n");
                outfile.write("\n");
            }
            outfile.write("}\n");
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file " + filename + " can not be opened");
        }
    }

    public void save(FileWriter outfile){
        int i;
        try{
            outfile.write("<Class name=\""+ this.name + "\" positionX=\"" + this.position.x + "\" positionY=\"" + this.position.y + "\">\n");
            for (i = 0; i < attributes.size(); i++){
                outfile.write("<Attribute name=\"" + attributes.get(i).getName() + "\" type=\"" + attributes.get(i).getType() + "\"/>\n");
            }
            for (i = 0; i < methods.size(); i++){
                outfile.write("<Method name=\"" + methods.get(i).getName() + "\" parameters=\"" + methods.get(i).getParameters() + "\" returntype=\"" + methods.get(i).getReturnType() + "\"/>\n");
            }
            outfile.write("</Class>");
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void paint(Graphics g){
        int i, count;
        super.paint(g);
        int stringSize = g.getFontMetrics().stringWidth(name);
        for (i = 0; i < attributes.size(); i++){
            if (g.getFontMetrics().stringWidth(attributes.get(i).toString()) > stringSize){
                stringSize = g.getFontMetrics().stringWidth(attributes.get(i).toString());
            }
        }
        for (i = 0; i < methods.size(); i++){
            if (g.getFontMetrics().stringWidth(methods.get(i).toString()) > stringSize){
                stringSize = g.getFontMetrics().stringWidth(methods.get(i).toString());
            }
        }
        g.drawRect(position.x, position.y, stringSize + 10, (attributes.size() + methods.size() + 1) * RECTANGLE_HEIGHT);
        g.drawString(name, position.x + 5, position.y + RECTANGLE_HEIGHT / 2 + 5);
        g.drawLine(position.x, position.y + RECTANGLE_HEIGHT, position.x + stringSize + 10, position.y + RECTANGLE_HEIGHT);
        count = 1;
        for (i = 0; i < attributes.size(); i++, count++){
            g.drawString(attributes.get(i).toString(), position.x + 5, position.y + (RECTANGLE_HEIGHT * count) + RECTANGLE_HEIGHT / 2 + 5);
        }
        g.drawLine(position.x, position.y + count * RECTANGLE_HEIGHT, position.x + stringSize + 10, position.y + count * RECTANGLE_HEIGHT);
        for (i = 0; i < methods.size(); i++, count++){
            g.drawString(methods.get(i).toString(), position.x + 5, position.y + (RECTANGLE_HEIGHT * count) + RECTANGLE_HEIGHT / 2 + 5);
        }
        setBoundingBoxSize(stringSize + 10, (attributes.size() + methods.size() + 1) * RECTANGLE_HEIGHT);
    }


}
