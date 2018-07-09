package Uml;

import java.awt.*;
import java.io.FileWriter;

public class SimpleUmlObject extends UmlObject{

    protected String name;
    protected Point position;

    public SimpleUmlObject(String name, Point position){
        this.name = name;
        this.position = position;
        this.boundingBox = new Rectangle(position);
    }

    public SimpleUmlObject clone(){
        return new SimpleUmlObject(name, new Point(position));
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void save(FileWriter outfile){}

    public void move(int deltaX, int deltaY){
        position.x += deltaX;
        position.y += deltaY;
        this.boundingBox.setLocation(position);
    }

    public String toString(){
        return name;
    }

}
