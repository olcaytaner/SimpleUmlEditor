package Uml;

import java.awt.*;
import java.io.FileWriter;

public class UmlObject {

    protected boolean selected = false;
    public boolean colored = false;
    protected Rectangle boundingBox;

    public boolean isSelected(){
        return selected;
    }

    public UmlObject clone(){
        UmlObject clone = new UmlObject();
        clone.boundingBox = new Rectangle(boundingBox);
        return clone;
    }

    public void move(int deltaX, int deltaY){}

    public void save(FileWriter outfile){}

    public void select(boolean selected){
        this.selected = selected;
    }

    public void setBoundingBoxSize(int width, int height){
        boundingBox.setSize(width, height);
    }

    public boolean contains(Point p){
        return boundingBox.contains(p);
    }

    public Point getCenter(){
        return new Point(boundingBox.x + boundingBox.width / 2, boundingBox.y + boundingBox.height / 2);
    }

    public Point getTop(){
        return new Point(boundingBox.x + boundingBox.width / 2, boundingBox.y);
    }

    public Point getBottom(){
        return new Point(boundingBox.x + boundingBox.width / 2, boundingBox.y + boundingBox.height);
    }

    public void paint(Graphics g){
        if (colored){
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }        
        if (isSelected()){
            g.drawOval(boundingBox.x - 2, boundingBox.y - 2, 4, 4);
            g.drawOval(boundingBox.x - 2, boundingBox.y + boundingBox.height - 2, 4, 4);
            g.drawOval(boundingBox.x + boundingBox.width - 2, boundingBox.y - 2, 4, 4);
            g.drawOval(boundingBox.x + boundingBox.width - 2, boundingBox.y + boundingBox.height - 2, 4, 4);
        }
    }

}
