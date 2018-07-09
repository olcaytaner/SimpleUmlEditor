package Diagram;

import Uml.SequenceDiagram.Actor;
import Uml.SequenceDiagram.Message;
import Uml.SimpleUmlObject;
import Uml.UmlObject;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class SequenceDiagram extends Diagram{

    public SequenceDiagram(){
        super();
    }

    public SequenceDiagram clone(){
        int i;
        SequenceDiagram newDiagram = new SequenceDiagram();
        for (i = 0; i < objects.size(); i++){
            newDiagram.objects.add(objects.get(i).clone());
        }
        return newDiagram;
    }

    public void addActor(String name, Point position){
        objects.add(new Actor(name, position));
    }

    public void addObject(String name, Point position){
        objects.add(new Uml.SequenceDiagram.Object(name, position));
    }

    public void addMessage(UmlObject from, UmlObject to, String message){
        objects.add(new Message((SimpleUmlObject)from, (SimpleUmlObject)to, message, messageCount() + 1));
    }

    public void addMessage(UmlObject from, UmlObject to, String message, int index){
        objects.add(new Message((SimpleUmlObject)from, (SimpleUmlObject)to, message, index));
    }

    public UmlObject getObject(String name){
        int i;
        for (i = 0; i < objects.size(); i++){
            if (objects.get(i).getClass().getName().equalsIgnoreCase("Uml.SequenceDiagram.Actor") || objects.get(i).getClass().getName().equalsIgnoreCase("Uml.SequenceDiagram.Object")){
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
                    if (objectName.equalsIgnoreCase("Object")){
                        addObject(attributes.getNamedItem("name").getNodeValue(), new Point(Integer.parseInt(attributes.getNamedItem("positionX").getNodeValue()), Integer.parseInt(attributes.getNamedItem("positionY").getNodeValue())));
                    } else {
                        if (objectName.equalsIgnoreCase("Message")){
                            from = getObject(attributes.getNamedItem("from").getNodeValue());
                            to = getObject(attributes.getNamedItem("to").getNodeValue());
                            addMessage(from, to, attributes.getNamedItem("text").getNodeValue(), Integer.parseInt(attributes.getNamedItem("index").getNodeValue()));
                        }
                    }
                }
            }
            objectNode = objectNode.getNextSibling();
        }
    }


    public void save(String filename){
        int i;
        FileWriter outfile;
        try{
            outfile = new FileWriter(filename);
            outfile.write("<Diagram type=\"Sequence\">\n");
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

    private int messageCount(){
        int i, count = 0;
        for (i = 0; i < objects.size(); i++){
            if (objects.get(i).getClass().getName().equalsIgnoreCase("Uml.SequenceDiagram.Message")){
                count++;
            }
        }
        return count;
    }

    public void paint(Graphics graphics){
        int i;
        for (i = 0; i < objects.size(); i++){
            objects.get(i).paint(graphics);
        }
    }

}
