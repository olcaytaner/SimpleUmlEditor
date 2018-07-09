package Uml.ClassDiagram;

public class Attribute{

    private String name;
    private String type;
    private EnumVisibility visibility;

    public Attribute(String name, String type, EnumVisibility visibility){
        this.name = name;
        this.type = type;
        this.visibility = visibility;
    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public String toString(){
        if (visibility == EnumVisibility.PROTECTED){
            return  "protected " + type + " " + name;
        } else {
            if (visibility == EnumVisibility.PUBLIC){
                return  "public " + type + " " + name;
            } else {
                return  "private " + type + " " + name;                                            
            }
        }
    }
}
