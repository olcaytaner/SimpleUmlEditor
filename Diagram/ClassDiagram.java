package Diagram;

import Uml.ClassDiagram.Association;
import Uml.ClassDiagram.EnumMultiplicity;
import Uml.ClassDiagram.Inheritance;
import Uml.ClassDiagram.Aggregation;
import Uml.SimpleUmlObject;
import Uml.UmlObject;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class ClassDiagram extends Diagram{

    public ClassDiagram(){
        super();
    }
    
    public Diagram clone(){
        int i;
        ClassDiagram newDiagram = new ClassDiagram();
        for (i = 0; i < objects.size(); i++){
            newDiagram.objects.add(objects.get(i).clone());
        }
        return newDiagram;
    }

    public Uml.ClassDiagram.Class addClass(String name, Point position){
        Uml.ClassDiagram.Class added = new Uml.ClassDiagram.Class(name, position);
        objects.add(added);
        return added;
    }

    public void addAssociation(Uml.ClassDiagram.Class from, Uml.ClassDiagram.Class to, EnumMultiplicity multiplicity){
        objects.add(new Association(from, to, multiplicity));
    }

    public void addInheritance(Uml.ClassDiagram.Class from, Uml.ClassDiagram.Class to){
        objects.add(new Inheritance(from, to));
    }

    public void addAggregation(Uml.ClassDiagram.Class from, Uml.ClassDiagram.Class to){
        objects.add(new Aggregation(from, to));
    }

    public UmlObject getObject(String name){
        int i;
        for (i = 0; i < objects.size(); i++){
            if (objects.get(i).getClass().getName().equalsIgnoreCase("Uml.ClassDiagram.Class")){
                if (((SimpleUmlObject)objects.get(i)).getName().equalsIgnoreCase(name)){
                    return objects.get(i);
                }
            }
        }
        return null;
    }

    public void generateCode(String directory){
        int i;
        String filename;
        Uml.ClassDiagram.Class current;
        for (i = 0; i < objects.size(); i++){
            if (objects.get(i).getClass().getName().equalsIgnoreCase("Uml.ClassDiagram.Class")){
                current = ((Uml.ClassDiagram.Class)objects.get(i));
                filename = directory + '/' + current.getName() + ".java";
                current.generateCode(filename);
            }
        }
    }

    public void loadFromXml(Node rootNode){
        Node objectNode, propertyNode;
        NamedNodeMap attributes;
        String objectName, propertyName, multiplicity;
        UmlObject from, to;
        objectNode = rootNode.getFirstChild();
        while (objectNode != null){
            if (objectNode.hasAttributes()){
                objectName = objectNode.getNodeName();
                attributes = objectNode.getAttributes();
                if (objectName.equalsIgnoreCase("Class")){
                    Uml.ClassDiagram.Class current = addClass(attributes.getNamedItem("name").getNodeValue(), new Point(Integer.parseInt(attributes.getNamedItem("positionX").getNodeValue()), Integer.parseInt(attributes.getNamedItem("positionY").getNodeValue())));
                    propertyNode = objectNode.getFirstChild();
                    while (propertyNode != null){
                        if (propertyNode.hasAttributes()){
                            propertyName = propertyNode.getNodeName();
                            attributes = propertyNode.getAttributes();
                            if (propertyName.equalsIgnoreCase("Attribute")){
                                current.addAttribute(attributes.getNamedItem("name").getNodeValue(), attributes.getNamedItem("type").getNodeValue());
                            } else {
                                if (propertyName.equalsIgnoreCase("Method")){
                                    current.addMethod(attributes.getNamedItem("name").getNodeValue(), attributes.getNamedItem("parameters").getNodeValue(), attributes.getNamedItem("returntype").getNodeValue());
                                }
                            }
                        }
                        propertyNode = propertyNode.getNextSibling(); 
                    }
                } else {
                    if (objectName.equalsIgnoreCase("Association")){
                        from = getObject(attributes.getNamedItem("from").getNodeValue());
                        to = getObject(attributes.getNamedItem("to").getNodeValue());
                        multiplicity = attributes.getNamedItem("multiplicity").getNodeValue();
                        if (multiplicity.equalsIgnoreCase("ONE_TO_ONE")){
                            addAssociation((Uml.ClassDiagram.Class)from, (Uml.ClassDiagram.Class)to, EnumMultiplicity.ONE_TO_ONE);
                        } else {
                            if (multiplicity.equalsIgnoreCase("ONE_TO_MANY")){
                                addAssociation((Uml.ClassDiagram.Class)from, (Uml.ClassDiagram.Class)to, EnumMultiplicity.ONE_TO_MANY);
                            } else {
                                if (multiplicity.equalsIgnoreCase("MANY_TO_MANY")){
                                    addAssociation((Uml.ClassDiagram.Class)from, (Uml.ClassDiagram.Class)to, EnumMultiplicity.MANY_TO_MANY);
                                }
                            }
                        }
                    } else {
                        if (objectName.equalsIgnoreCase("Aggregation")){
                            from = getObject(attributes.getNamedItem("from").getNodeValue());
                            to = getObject(attributes.getNamedItem("to").getNodeValue());
                            addAggregation((Uml.ClassDiagram.Class)from, (Uml.ClassDiagram.Class)to);
                        } else {
                            if (objectName.equalsIgnoreCase("Inheritance")){
                                from = getObject(attributes.getNamedItem("from").getNodeValue());
                                to = getObject(attributes.getNamedItem("to").getNodeValue());
                                addInheritance((Uml.ClassDiagram.Class)from, (Uml.ClassDiagram.Class)to);
                            }
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
            outfile.write("<Diagram type=\"Class\">\n");
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

    public void paint(Graphics graphics){
        int i;
        for (i = 0; i < objects.size(); i++){
            objects.get(i).paint(graphics);
        }
    }

}
