package Diagram;

import Uml.UmlObject;
import org.w3c.dom.Node;

import java.awt.*;
import java.util.ArrayList;

public class Diagram {

    protected ArrayList<UmlObject> objects;

    public Diagram(){
        objects = new ArrayList<UmlObject>();
    }

    public Diagram clone(){
        return null;
    }

    public UmlObject getUmlObjectAtPos(Point p){
        int i;
        for (i = 0; i < objects.size(); i++){
            if (objects.get(i).contains(p)){
                return objects.get(i);
            }
        }
        return null;
    }

    public void selectAll(){
        int i;
        for (i = 0; i < objects.size(); i++){
            objects.get(i).select(true);
        }
    }

    public ArrayList<UmlObject> copyAll(){
        int i;
        ArrayList<UmlObject> result = new ArrayList<UmlObject>();
        for (i = 0; i < objects.size(); i++){
            if (objects.get(i).isSelected()){
                result.add(objects.get(i).clone());
            }
        }
        return result;
    }

    public void moveSelected(int deltaX, int deltaY){
        int i;
        for (i = 0; i < objects.size(); i++){
            if (objects.get(i).isSelected()){
                objects.get(i).move(deltaX, deltaY);
            }
        }
    }

    public void selectArea(Rectangle area){
        int i;
        for (i = 0; i < objects.size(); i++){
            if (area.contains(objects.get(i).getCenter())){
                objects.get(i).select(true);
            } else {
                objects.get(i).select(false);                
            }
        }
    }

    public void pasteObjects(ArrayList<UmlObject> copyList){
        int i;
        for (i = 0; i < copyList.size(); i++){
            objects.add(copyList.get(i));
        }
    }

    public void deselectAll(){
        int i;
        for (i = 0; i < objects.size(); i++){
            objects.get(i).select(false);
        }        
    }

    public void save(String filename){}

    public void loadFromXml(Node rootNode){}

    public void deleteSelected(){
        int i;
        for (i = 0; i < objects.size(); i++){
            if (objects.get(i).isSelected()){
                objects.remove(i);
                i--;
            }
        }
    }
}
