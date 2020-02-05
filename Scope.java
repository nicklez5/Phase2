import java.util.*;

public class Scope{
    public String name;
    public Vector<String> fields;
    public Vector<String> methods;
    public Class_Record class_record;
    public V_Table v_table;
    public Scope(){
        name = "";
        fields = new Vector<String>();
        methods = new Vector<String>();
        class_record = new Class_Record();
        v_table = new V_Table();
    }
    public void print_scope_name(){
        System.out.println("Scope Name: " + name);
    }
    public void add_name(String temp_name){
        name = temp_name;
    }
    public void add_fields(String temp_fields){
        fields.add(temp_fields);
    }
    public void add_methods(String temp_methods){
        methods.add(temp_methods);
    }
    public void print_scope(){
      System.out.println("Class Name: " + name);
      System.out.println("Field Name: " + fields);
      System.out.println("Method Name: " + methods);
   }

}
