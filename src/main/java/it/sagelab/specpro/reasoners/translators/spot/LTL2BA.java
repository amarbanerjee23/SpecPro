package it.sagelab.specpro.reasoners.translators.spot;

import it.sagelab.specpro.models.ba.BuchiAutomata;
import it.sagelab.specpro.models.ba.DotBuilder;
import it.sagelab.specpro.models.ba.Edge;
import it.sagelab.specpro.models.ba.Vertex;
import org.apache.commons.io.IOUtils;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.io.DOTImporter;
import org.jgrapht.io.ImportException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

public class LTL2BA {

    DotBuilder db;
    int index = 0;

    DOTImporter<Vertex, Edge> importer;

    public LTL2BA() {
        db = new DotBuilder();
        importer = new DOTImporter<>(db, db, db);
    }

    public BuchiAutomata translate(String formula) {
        BuchiAutomata g = new BuchiAutomata(db);
        Runtime rt = Runtime.getRuntime();
        Process process = null;
        String[] command = {"ltl2tgba", "-B", formula, "-d"};
        try {
            process = rt.exec(command);
            int exitValue = process.waitFor();
            if(exitValue != 0)
                throw new RuntimeException("ltl2gba returned with value " + exitValue);
            if(process.getErrorStream().available() > 0) {
                String error = IOUtils.toString(process.getErrorStream());
                if (error.length() > 0)
                    System.err.println(error);
            }
            if(process.getInputStream().available() > 0) {
                String input = IOUtils.toString(process.getInputStream());
                IOUtils.write(input, new FileOutputStream("test" + index + ".dot"));
                ++index;
                input = input.replace("label=\"\\n[Büchi]\"", "");
                //System.out.println(input);
                importer.importGraph(g, new StringReader(input));
            } else {
                return null;
            }

        } catch (IOException | InterruptedException | ImportException e) {
            if(process != null)
                process.destroyForcibly();
            e.printStackTrace();
            g = null;
        }

        return g;
    }

}
