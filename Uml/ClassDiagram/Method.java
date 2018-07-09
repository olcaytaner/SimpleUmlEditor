package Uml.ClassDiagram;

import java.util.ArrayList;

public class Method {

    private String name;
    private String parameterList;
    private String returnType;
    private EnumVisibility visibility;
    private ArrayList<Contract> preconditions;
    private ArrayList<Contract> postconditions;

    public Method(String name, String parameterList, String returnType, EnumVisibility visibility){
        this.name = name;
        this.parameterList = parameterList;
        this.returnType = returnType;
        this.visibility = visibility;
        preconditions = new ArrayList<Contract>();
        postconditions = new ArrayList<Contract>();
    }

    public String getName(){
        return name;
    }

    public String getParameters(){
        return parameterList;
    }

    public String getReturnType(){
        return returnType;
    }

    public String toString(){
        if (visibility == EnumVisibility.PROTECTED){
            return  "protected " + returnType + " " + name  + "(" + parameterList + ")";
        } else {
            if (visibility == EnumVisibility.PUBLIC){
                return  "public " + returnType + " " + name  + "(" + parameterList + ")";
            } else {
                return  "private " + returnType + " " + name  + "(" + parameterList + ")";
            }
        }
    }
}
