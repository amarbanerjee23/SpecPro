package it.sagelab.specpro.models.psp.qualitative;

import java.util.List;

import it.sagelab.specpro.models.psp.Requirement;
import it.sagelab.specpro.models.psp.Scope;
import it.sagelab.specpro.models.psp.expressions.Expression;

/**
 * The Class QualitativeRequirement.
 *
 * @author Simone Vuotto
 */
public abstract class QualitativeRequirement extends Requirement {

    /**
     * Instantiates a new qualitative requirement.
     *
     * @param scope the scope
     * @param expressions the expressions
     */
    public QualitativeRequirement(Scope scope, List<Expression> expressions) {
        super(scope, expressions);
    }

    /**
     * Accept.
     *
     * @param visitor the visitor
     */
    public abstract void accept(QualitativeRequirementVisitor visitor);

}
