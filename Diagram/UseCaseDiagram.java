package Diagram;

import Uml.SimpleUmlObject;
import Uml.UmlObject;
import Uml.UseCase.Actor;
import Uml.UseCase.Connection;
import Uml.UseCase.EnumRelationship;
import Uml.UseCase.UseCase;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class UseCaseDiagram extends Diagram{

    public UseCaseDiagram(){
        super();
    }

    public UseCaseDiagram clone(){
        int i;
        UseCaseDiagram newDiagram = new UseCaseDiagram();
        for (i = 0; i < objects.size(); i++){
            newDiagram.objects.add(objects.get(i).clone());
        }
        return newDiagram;
    }

    public void addActor(String name, Point position){
        objects.add(new Actor(name, position));
    }

    public void addUseCase(String name, Point position){
        objects.add(new UseCase(name, position));
    }

    public void addConnection(UmlObject from, UmlObject to, String type){
        if (type.equalsIgnoreCase("INHERITANCE")){
            objects.add(new Connection((SimpleUmlObject)from, (SimpleUmlObject)to, EnumRelationship.INHERITANCE));
        } else {
            if (type.equalsIgnoreCase("INITIATE")){
                objects.add(new Connection((SimpleUmlObject)from, (SimpleUmlObject)to, EnumRelationship.INITIATE));
            } else {
                if (type.equalsIgnoreCase("PARTICIPATE")){
                    objects.add(new Connection((SimpleUmlObject)from, (SimpleUmlObject)to, EnumRelationship.PARTICIPATE));
                } else {
                    if (type.equalsIgnoreCase("INCLUDE")){
                        objects.add(new Connection((SimpleUmlObject)from, (SimpleUmlObject)to, EnumRelationship.INCLUDE));
                    } else {
                        if (type.equalsIgnoreCase("EXTEND")){
                            objects.add(new Connection((SimpleUmlObject)from, (SimpleUmlObject)to, EnumRelationship.EXTEND));
                        } else {
                            objects.add(new Connection((SimpleUmlObject)from, (SimpleUmlObject)to, EnumRelationship.NO));
                        }
                    }
                }
            }
        }
    }

    public void save(String filename){
        int i;
        FileWriter outfile;
        try{
            outfile = new FileWriter(filename);
            outfile.write("<Diagram type=\"UseCase\">\n");
            for (i = 0; i < objects.size(); i++){
                objects.get(i).save(outfile);
            }
            outfile.write("</Diagram>\n");
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public UmlObject getObject(String name){
        int i;
        for (i = 0; i < objects.size(); i++){
            if (objects.get(i).getClass().getName().equalsIgnoreCase("Uml.UseCase.Actor") || objects.get(i).getClass().getName().equalsIgnoreCase("Uml.UseCase.UseCase")){
                if (((SimpleUmlObject)objects.get(i)).getName().equalsIgnoreCase(name)){
                    return objects.get(i);
                }                
            }
        }
        return null;
    }

    public void loadFromXml(Node rootNode){
        Node objectNode;
        NamedNodeMap attributes;
        String objectName;
        UmlObject from, to;
        objectNode = rootNode.getFirstChild();
        while (objectNode != null){
            if (objectNode.hasAttributes()){
                objectName = objectNode.getNodeName();
                attributes = objectNode.getAttributes();
                if (objectName.equalsIgnoreCase("Actor")){
                    addActor(attributes.getNamedItem("name").getNodeValue(), new Point(Integer.parseInt(attributes.getNamedItem("positionX").getNodeValue()), Integer.parseInt(attributes.getNamedItem("positionY").getNodeValue())));
                } else {
                    if (objectName.equalsIgnoreCase("UseCase")){
                        addUseCase(attributes.getNamedItem("name").getNodeValue(), new Point(Integer.parseInt(attributes.getNamedItem("positionX").getNodeValue()), Integer.parseInt(attributes.getNamedItem("positionY").getNodeValue())));
                    } else {
                        if (objectName.equalsIgnoreCase("Connection")){
                            from = getObject(attributes.getNamedItem("from").getNodeValue());
                            to = getObject(attributes.getNamedItem("to").getNodeValue());
                            addConnection(from, to, attributes.getNamedItem("type").getNodeValue());
                        }
                    }
                }
            }
            objectNode = objectNode.getNextSibling();
        }
    }

    public void paint(Graphics graphics){
        int i;
        for (i = 0; i < objects.size(); i++){
            objects.get(i).paint(graphics);
        }
    }

}
