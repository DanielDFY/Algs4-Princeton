import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;


import java.util.HashMap;

public class WordNet {
    private final HashMap<Integer, Bag<String>> idMap;
    private final HashMap<String, Bag<Integer>> wordMap;
    private final Digraph g;
    private final SAP sap;
    private int v;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Input format : synsets, hypernyms");

        idMap = new HashMap<Integer, Bag<String>>();
        wordMap = new HashMap<String, Bag<Integer>>();
        readSynsets(new In(synsets));
        g = new Digraph(v);
        readHypernyms(new In(hypernyms));
        sap = new SAP(g);

        checkDAG();
        checkRoot();
    }

    public Iterable<String> nouns() {
        return wordMap.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return wordMap.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        Iterable<Integer> setA = wordMap.get(nounA);
        Iterable<Integer> setB = wordMap.get(nounB);

        return sap.length(setA, setB);
    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        Iterable<Integer> setA = wordMap.get(nounA);
        Iterable<Integer> setB = wordMap.get(nounB);

        int ancestor = sap.ancestor(setA, setB);
        Bag<String> nouns = idMap.get(ancestor);
        StringBuilder result = new StringBuilder();
        for (String noun : nouns) {
            result.append(noun);
            result.append(" ");
        }

        return result.toString();
    }

    private void readSynsets(In synsetsIn) {
        v = 0;

        while (synsetsIn.hasNextLine()) {
            String line = synsetsIn.readLine();
            String[] tokens = line.split(",");
            int id = Integer.parseInt(tokens[0]);
            String[] nouns = tokens[1].split(" ");
            Bag<String> words = new Bag<String>();

            for (String noun : nouns) {
                words.add(noun);
                Bag<Integer> ids = new Bag<Integer>();
                if (this.wordMap.containsKey(noun))
                    ids = this.wordMap.get(noun);
                ids.add(id);
                this.wordMap.put(noun, ids);
            }

            idMap.put(id, words);
            if (id > this.v) this.v = id;
        }
        ++this.v;
    }

    private void readHypernyms(In hypernymsIn) {
        while (hypernymsIn.hasNextLine()) {
            String line = hypernymsIn.readLine();
            String[] tokens = line.split(",");
            int x = Integer.parseInt(tokens[0]);
            for (int i = 1; i < tokens.length; ++i) {
                int y = Integer.parseInt(tokens[i]);
                this.g.addEdge(x, y);
            }
        }
    }

    private void checkDAG() {
        DirectedCycle cycle = new DirectedCycle(this.g);
        if (cycle.hasCycle())
            throw new IllegalArgumentException("Digraph has cycle!");
    }

    private void checkRoot() {
        int count = 0;
        for (int i = 0; i < v; ++i) {
            if (g.outdegree(i) == 0)
                count++;
        }
        if (count != 1)
            throw new IllegalArgumentException();
    }
}
