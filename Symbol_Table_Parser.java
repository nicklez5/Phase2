import java.util.*;
import syntaxtree.*;
import visitor.*;

public class Symbol_Table_Parser extends DepthFirstVisitor{

   public Symbol_Table sym_table;
   public String current_id;
   public boolean field_value_on;
   public Scope current_scope;
   public static void main (String[] args){
      Goal holy_goal;
      try{
         MiniJavaParser xyz = new MiniJavaParser(System.in);
         Symbol_Table_Parser test_me = new Symbol_Table_Parser();
         holy_goal = xyz.Goal();
         holy_goal.accept(test_me);
         test_me.sym_table.print_me();
         test_me.sym_table.print_scope();
      } catch (ParseException e){
			System.out.println("Type error");
		}

   }
   public Symbol_Table_Parser(){
      sym_table = new Symbol_Table();
      current_id = "";
      field_value_on = false;


   }
   /**
   * f0 -> MainClass()
   * f1 -> ( TypeDeclaration() )*
   * f2 -> <EOF>
   */
   public void visit(Goal n) {
      visit(n.f0);
      Vector<Node> list_nodes = n.f1.nodes;
      Iterator _itr = list_nodes.iterator();
      while(_itr.hasNext()){
         TypeDeclaration temp_declare = (TypeDeclaration)_itr.next();
         visit(temp_declare);
      }
      n.f2.accept(this);
   }

   /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "{"
   * f3 -> "public"
   * f4 -> "static"
   * f5 -> "void"
   * f6 -> "main"
   * f7 -> "("
   * f8 -> "String"
   * f9 -> "["
   * f10 -> "]"
   * f11 -> Identifier()
   * f12 -> ")"
   * f13 -> "{"
   * f14 -> ( VarDeclaration() )*
   * f15 -> ( Statement() )*
   * f16 -> "}"
   * f17 -> "}"
   */
   public void visit(MainClass n) {
      Scope temp_scope = new Scope();
      current_scope = temp_scope;
      n.f0.accept(this);
      visit(n.f1);
      sym_table.add_map_value(current_id,Constants.CLASS_TYPE);
      current_scope.add_name(current_id);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      sym_table.add_map_value(n.f6.toString(),Constants.METHOD_TYPE);
      current_scope.add_methods(n.f6.toString());
      sym_table.add_scope(current_scope);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
      n.f13.accept(this);
      Vector<Node> list_nodes = n.f14.nodes;
      Iterator _itr = list_nodes.iterator();
      while(_itr.hasNext()){
         VarDeclaration temp_var = (VarDeclaration)_itr.next();
         visit(temp_var);
      }
      n.f16.accept(this);
      n.f17.accept(this);
   }

   /**
   * f0 -> ClassDeclaration()
   *       | ClassExtendsDeclaration()
   */
   public void visit(TypeDeclaration n) {
      if(n.f0.which == 0){
         ClassDeclaration temp_class = (ClassDeclaration)n.f0.choice;
         visit(temp_class);
      }else if(n.f0.which == 1){
         ClassExtendsDeclaration temp_ext_class = (ClassExtendsDeclaration)n.f0.choice;
         visit(temp_ext_class);
      }

   }

   /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "{"
   * f3 -> ( VarDeclaration() )*
   * f4 -> ( MethodDeclaration() )*
   * f5 -> "}"
   */
   public void visit(ClassDeclaration n) {
      Scope temp_scope = new Scope();
      current_scope = temp_scope;
      String class_name = n.f1.f0.toString();
      current_scope.add_name(class_name);
      sym_table.add_map_value(class_name,Constants.CLASS_TYPE);
      Vector<Node> list_nodes = n.f3.nodes;
      Iterator _itr = list_nodes.iterator();
      while(_itr.hasNext()){
         field_value_on = true;
         VarDeclaration temp_var = (VarDeclaration)_itr.next();
         visit(temp_var);
         current_scope.add_fields(current_id);
      }
      field_value_on = false;
      list_nodes = n.f4.nodes;
      _itr = list_nodes.iterator();
      while(_itr.hasNext()){
         MethodDeclaration temp_method = (MethodDeclaration)_itr.next();
         visit(temp_method);
      }
      sym_table.add_scope(current_scope);
   }

   /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "extends"
   * f3 -> Identifier()
   * f4 -> "{"
   * f5 -> ( VarDeclaration() )*
   * f6 -> ( MethodDeclaration() )*
   * f7 -> "}"
   */
   public void visit(ClassExtendsDeclaration n) {
      Scope temp_scope = new Scope();
      current_scope = temp_scope;
      n.f0.accept(this);
      visit(n.f1);
      current_scope.add_name(current_id);
      sym_table.add_map_value(current_id,Constants.CLASS_TYPE);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      Vector<Node> list_class = n.f5.nodes;
      Iterator _itr = list_class.iterator();
      while(_itr.hasNext()){
         field_value_on = true;
         VarDeclaration temp_var = (VarDeclaration)_itr.next();
         visit(temp_var);
         current_scope.add_fields(current_id);
      }
      field_value_on = false;
      Vector<Node> list_method = n.f6.nodes;
      _itr = list_method.iterator();
      while(_itr.hasNext()){
         MethodDeclaration temp_method = (MethodDeclaration)_itr.next();
         visit(temp_method);

      }
      sym_table.add_scope(current_scope);
   }

   /**
   * f0 -> Type()
   * f1 -> Identifier()
   * f2 -> ";"
   */
   public void visit(VarDeclaration n) {
      n.f0.accept(this);
      visit(n.f1);
      n.f2.accept(this);
      if(field_value_on){
         sym_table.add_map_value(current_id,Constants.FIELD_TYPE);
      }else{
         sym_table.add_map_value(current_id,Constants.LOCAL_TYPE);
      }
   }

   /**
   * f0 -> "public"
   * f1 -> Type()
   * f2 -> Identifier()
   * f3 -> "("
   * f4 -> ( FormalParameterList() )?
   * f5 -> ")"
   * f6 -> "{"
   * f7 -> ( VarDeclaration() )*
   * f8 -> ( Statement() )*
   * f9 -> "return"
   * f10 -> Expression()
   * f11 -> ";"
   * f12 -> "}"
   */
   public void visit(MethodDeclaration n) {
      n.f0.accept(this);
      n.f1.accept(this);
      visit(n.f2);
      sym_table.add_map_value(current_id,Constants.METHOD_TYPE);
      current_scope.add_methods(current_id);
      n.f3.accept(this);

      FormalParameterList temp_list = (FormalParameterList)n.f4.node;
      if(temp_list != null){
         visit(temp_list);
      }
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      Vector<Node> list_nodes = n.f7.nodes;
      Iterator _itr = list_nodes.iterator();
      while(_itr.hasNext()){
         VarDeclaration temp_var = (VarDeclaration)_itr.next();
         visit(temp_var);
      }
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
   }

   /**
   * f0 -> FormalParameter()
   * f1 -> ( FormalParameterRest() )*
   */
   public void visit(FormalParameterList n) {
      visit(n.f0);
      Vector<Node> list_nodes = n.f1.nodes;
      Iterator _itr = list_nodes.iterator();
      while(_itr.hasNext()){
         FormalParameterRest xyz = (FormalParameterRest)_itr.next();
         visit(xyz);
      }
   }

   /**
   * f0 -> Type()
   * f1 -> Identifier()
   */
   public void visit(FormalParameter n) {
      n.f0.accept(this);
      visit(n.f1);
      sym_table.add_map_value(current_id,Constants.LOCAL_TYPE);
   }

   /**
   * f0 -> ","
   * f1 -> FormalParameter()
   */
   public void visit(FormalParameterRest n) {
      n.f0.accept(this);
      visit(n.f1);
   }

   /**
   * f0 -> ArrayType()
   *       | BooleanType()
   *       | IntegerType()
   *       | Identifier()
   */
   public void visit(Type n) {
      n.f0.accept(this);
   }

   /**
   * f0 -> "int"
   * f1 -> "["
   * f2 -> "]"
   */
   public void visit(ArrayType n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
   * f0 -> "boolean"
   */
   public void visit(BooleanType n) {
      n.f0.accept(this);
   }

   /**
   * f0 -> "int"
   */
   public void visit(IntegerType n) {
      n.f0.accept(this);
   }

   /**
   * f0 -> Block()
   *       | AssignmentStatement()
   *       | ArrayAssignmentStatement()
   *       | IfStatement()
   *       | WhileStatement()
   *       | PrintStatement()
   */
   public void visit(Statement n) {
      n.f0.accept(this);
   }

   /**
   * f0 -> "{"
   * f1 -> ( Statement() )*
   * f2 -> "}"
   */
   public void visit(Block n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
   * f0 -> Identifier()
   * f1 -> "="
   * f2 -> Expression()
   * f3 -> ";"
   */
   public void visit(AssignmentStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
   }

   /**
   * f0 -> Identifier()
   * f1 -> "["
   * f2 -> Expression()
   * f3 -> "]"
   * f4 -> "="
   * f5 -> Expression()
   * f6 -> ";"
   */
   public void visit(ArrayAssignmentStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
   }

   /**
   * f0 -> "if"
   * f1 -> "("
   * f2 -> Expression()
   * f3 -> ")"
   * f4 -> Statement()
   * f5 -> "else"
   * f6 -> Statement()
   */
   public void visit(IfStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
   }

   /**
   * f0 -> "while"
   * f1 -> "("
   * f2 -> Expression()
   * f3 -> ")"
   * f4 -> Statement()
   */
   public void visit(WhileStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
   }

   /**
   * f0 -> "System.out.println"
   * f1 -> "("
   * f2 -> Expression()
   * f3 -> ")"
   * f4 -> ";"
   */
   public void visit(PrintStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
   }

   /**
   * f0 -> AndExpression()
   *       | CompareExpression()
   *       | PlusExpression()
   *       | MinusExpression()
   *       | TimesExpression()
   *       | ArrayLookup()
   *       | ArrayLength()
   *       | MessageSend()
   *       | PrimaryExpression()
   */
   public void visit(Expression n) {
      n.f0.accept(this);
   }

   /**
   * f0 -> PrimaryExpression()
   * f1 -> "&&"
   * f2 -> PrimaryExpression()
   */
   public void visit(AndExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
   * f0 -> PrimaryExpression()
   * f1 -> "<"
   * f2 -> PrimaryExpression()
   */
   public void visit(CompareExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
   * f0 -> PrimaryExpression()
   * f1 -> "+"
   * f2 -> PrimaryExpression()
   */
   public void visit(PlusExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
   * f0 -> PrimaryExpression()
   * f1 -> "-"
   * f2 -> PrimaryExpression()
   */
   public void visit(MinusExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
   * f0 -> PrimaryExpression()
   * f1 -> "*"
   * f2 -> PrimaryExpression()
   */
   public void visit(TimesExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
   * f0 -> PrimaryExpression()
   * f1 -> "["
   * f2 -> PrimaryExpression()
   * f3 -> "]"
   */
   public void visit(ArrayLookup n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
   }

   /**
   * f0 -> PrimaryExpression()
   * f1 -> "."
   * f2 -> "length"
   */
   public void visit(ArrayLength n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
   * f0 -> PrimaryExpression()
   * f1 -> "."
   * f2 -> Identifier()
   * f3 -> "("
   * f4 -> ( ExpressionList() )?
   * f5 -> ")"
   */
   public void visit(MessageSend n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
   }

   /**
   * f0 -> Expression()
   * f1 -> ( ExpressionRest() )*
   */
   public void visit(ExpressionList n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
   * f0 -> ","
   * f1 -> Expression()
   */
   public void visit(ExpressionRest n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
   * f0 -> IntegerLiteral()
   *       | TrueLiteral()
   *       | FalseLiteral()
   *       | Identifier()
   *       | ThisExpression()
   *       | ArrayAllocationExpression()
   *       | AllocationExpression()
   *       | NotExpression()
   *       | BracketExpression()
   */
   public void visit(PrimaryExpression n) {
      n.f0.accept(this);
   }

   /**
   * f0 -> <INTEGER_LITERAL>
   */
   public void visit(IntegerLiteral n) {
      n.f0.accept(this);
   }

   /**
   * f0 -> "true"
   */
   public void visit(TrueLiteral n) {
      n.f0.accept(this);
   }

   /**
   * f0 -> "false"
   */
   public void visit(FalseLiteral n) {
      n.f0.accept(this);
   }

   /**
   * f0 -> <IDENTIFIER>
   */
   public void visit(Identifier n) {
      current_id = n.f0.toString();

   }

   /**
   * f0 -> "this"
   */
   public void visit(ThisExpression n) {
      n.f0.accept(this);
   }

   /**
   * f0 -> "new"
   * f1 -> "int"
   * f2 -> "["
   * f3 -> Expression()
   * f4 -> "]"
   */
   public void visit(ArrayAllocationExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
   }

   /**
   * f0 -> "new"
   * f1 -> Identifier()
   * f2 -> "("
   * f3 -> ")"
   */
   public void visit(AllocationExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
   }

   /**
   * f0 -> "!"
   * f1 -> Expression()
   */
   public void visit(NotExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
   * f0 -> "("
   * f1 -> Expression()
   * f2 -> ")"
   */
   public void visit(BracketExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }
}
