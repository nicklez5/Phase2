import java.util.*;
public class Symbol_Table{
    public Map<String,String> id_type_map;
    public Stack<Scope> scope_stack;

    public Symbol_Table(){
        id_type_map = new HashMap<String,String>();
        scope_stack = new Stack<Scope>();

    }
    public void add_map_value(String _id, String _value){
        id_type_map.put(_id,_value);
    }
    public void add_scope(Scope temp_scope){
        scope_stack.push(temp_scope);
    }
    public void print_me(){
      System.out.println("Printing Map");
      for(Map.Entry<String,String> entry: id_type_map.entrySet()){
          String k = entry.getKey();
          String v = entry.getValue();
          System.out.println(k + ":" + v);
      }
   }
   public void print_scope(){
      System.out.println("Printing Scope");
      while(!scope_stack.empty()){
         Scope temp_scope = scope_stack.pop();
         temp_scope.print_scope();
      }
   }
}
