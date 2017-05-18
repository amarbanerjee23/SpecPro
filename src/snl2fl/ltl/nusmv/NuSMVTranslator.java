package snl2fl.ltl.nusmv;

import java.io.PrintStream;
import java.util.List;
import java.util.TreeMap;

import snl2fl.fl.elements.Atom;
import snl2fl.fl.elements.Formula;
import snl2fl.ltl.LTLContext;
import snl2fl.ltl.LTLTranslator;
import snl2fl.req.expressions.BooleanVariableExpression;
import snl2fl.req.expressions.VariableExpression;

/**
 * @author Simone Vuotto
 */
public class NuSMVTranslator {
    private final LTLTranslator translator;

    public NuSMVTranslator(LTLTranslator translator) {
        this.translator = translator;
    }

    public void translate(PrintStream stream) {
        stream.println("MODULE main");
        this.printVariables(stream);
        stream.println();
        LTLNuSMVVisitor visitor = new LTLNuSMVVisitor(stream);
        List<Formula> invariants = translator.getInvariants();
        for(Formula inv : invariants) {
            stream.print("INVAR ");
            inv.accept(visitor);
            stream.print(";\n");
        }

        stream.println();
        List<Formula> ltlFormulae = translator.translate();
        stream.print("LTLSPEC !(");
        for(int i=0; i < ltlFormulae.size(); ++i) {
            Formula formula = ltlFormulae.get(i);
            stream.print("(");
            formula.accept(visitor);
            stream.print(")");
            if(i < ltlFormulae.size() - 1)
                stream.print(" & ");
        }
        stream.print(");");

    }

    public void printVariables(PrintStream stream) {
        stream.println("VAR\n");
        LTLContext context = translator.getContext();
        // Print boolean variables
        for(VariableExpression ve : context.getSymbolTable().values())
            if(ve instanceof BooleanVariableExpression)
                stream.println("\t"+ve.getName()+" : boolean;");
        // Print range variables encoded with atoms
        for(TreeMap<Float, Atom[]> t : context.getRangeMap().values())
            for(Atom[] a : t.values()) {
                stream.println("\t"+a[0].getName()+" : boolean;");
                stream.println("\t"+a[1].getName()+" : boolean;");

            }
    }


}
