package it.sagelab.specpro.atg.utils;

import it.sagelab.specpro.atg.TestSequence;
import it.sagelab.specpro.models.ba.BAExplorer;
import it.sagelab.specpro.models.ba.BuchiAutomaton;
import it.sagelab.specpro.models.ba.Edge;
import it.sagelab.specpro.models.ba.ac.EndsWithAcceptanceStateCondition;
import it.sagelab.specpro.models.ltl.assign.Assignment;

import java.util.*;


public class RandomOutputSelector implements OutputSelector {

    private final BAExplorer explorer;

    public RandomOutputSelector() {
        explorer = new BAExplorer();
        explorer.addAcceptanceCondition(new EndsWithAcceptanceStateCondition());
    }

    @Override
    public TestSequence selectOutput(BuchiAutomaton automaton, List<Assignment> inputs) {

        explorer.setLength(inputs.size());

        Set<List<Edge>> paths = explorer.findInducedPaths(automaton, inputs).toSet();

        List<Edge> output = select(paths);

        TestSequence oututSequence = new TestSequence();
        for(int i = 0; i < output.size(); ++i) {
            Edge edge = output.get(i);
            Assignment inputAssigment = inputs.get(i);
            Optional<Assignment> optional = edge.getAssigments().stream().filter(a -> inputAssigment.isCompatible(a)).map(a -> a.combine(inputAssigment)).findAny();
            if(optional.isPresent() && optional.get() != null) {
                oututSequence.add(edge, optional.get());
            } else {
                throw new RuntimeException("Selected an incompatible edge!");
            }

        }


        return oututSequence;
    }

    protected List<Edge> select(Set<List<Edge>> paths) {
        Random random = new Random();
        int n = random.nextInt(paths.size());
        Iterator<List<Edge>> itr = paths.iterator();
        int i = 0;
        while (i++ < n)
            itr.next();
        return itr.next();
    }
}
