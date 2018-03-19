//
// Generated by JTB 1.3.2
//

package parser.ast;

import parser.visitor.*;

/**
 * Grammar production:
 * f0 -> AssignmentInstruction()
 *       | BranchStatement()
 *       | RepeatStatement()
 *       | HeatStatement()
 *       | DrainStatement()
 *       | FunctionInvoke()
 */
public class Statements implements Node {
   public NodeChoice f0;

   public Statements(NodeChoice n0) {
      f0 = n0;
   }

   public void accept(Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}

