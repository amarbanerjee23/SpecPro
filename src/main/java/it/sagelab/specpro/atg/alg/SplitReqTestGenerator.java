package it.sagelab.specpro.atg.alg;

import it.sagelab.specpro.collections.ListUtils;
import it.sagelab.specpro.fe.snl2fl.Snl2FlParser;
import it.sagelab.specpro.models.ba.BuchiAutomata;
import it.sagelab.specpro.models.psp.Requirement;
import it.sagelab.specpro.reasoners.translators.spot.LTL2BA;
import it.sagelab.specpro.reasoners.translators.spot.SpotTranslator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SplitReqTestGenerator extends PathsMultiplierTestGenerator {

    private final int nSplits;

    public SplitReqTestGenerator(String filePath, int nSplits) throws IOException {
        this.nSplits =nSplits;
        parseRequirements(filePath);
    }

    @Override
    protected void parseRequirements(String filePath) throws IOException {
        Snl2FlParser parser = new Snl2FlParser();
        parser.parseFile(filePath);

        LTL2BA ltl2ba = new LTL2BA();

        List<Requirement> requirements = new ArrayList<>(parser.getRequirements());

        List<Requirement>[] reqSplit = ListUtils.randomSplit(requirements, nSplits);

        for(List<Requirement> r : reqSplit)
            System.out.println(r.size());

        for(List<Requirement> r: reqSplit) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (PrintStream ps = new PrintStream(baos, true, "UTF-8")) {
                parser.getRequirements().clear();
                parser.getRequirements().addAll(r);
                parser.translate(new SpotTranslator(), ps);

                String ltlFormula = new String(baos.toByteArray(), StandardCharsets.UTF_8);
                System.out.println(ltlFormula);


                BuchiAutomata ba = ltl2ba.translate(ltlFormula);
                graphs.add(ba);
            }

        }
    }

}
