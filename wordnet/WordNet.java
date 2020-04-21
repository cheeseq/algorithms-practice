import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordNet {
    private List<String> synsetList = new ArrayList<>();
    private Map<String, Set<Integer>> nounToSynsetsMap = new HashMap<>();
    private SAP sap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        int synsetsCount = buildSynsets(new In(synsets));
        Digraph digraph = new Digraph(synsetsCount);
        buildHypernymsGraph(new In(hypernyms), digraph);

        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if(directedCycle.hasCycle()) {
            throw new IllegalArgumentException("The input graph has cycles");
        }
        sap = new SAP(digraph);
    }

    private int buildSynsets(In synsetsIn) {
        int synsetCount = 0;
        while (synsetsIn.hasNextLine()) {
            String nextLine = synsetsIn.readLine();
            String[] chunks = nextLine.split(",");
            int synsetId = Integer.parseInt(chunks[0]);
            synsetList.add(synsetId, chunks[1]);
            String[] nouns = chunks[1].split(" ");
            for(String noun : nouns) {
                Set<Integer> synsetsOfNoun = nounToSynsetsMap.get(noun);
                if(synsetsOfNoun == null) {
                    synsetsOfNoun = new HashSet<>();
                }
                synsetsOfNoun.add(synsetId);
                nounToSynsetsMap.put(noun, synsetsOfNoun);
            }
            synsetCount++;
        }
        return synsetCount;
    }

    private void buildHypernymsGraph(In hypernymsIn, Digraph digraph) {
        Set<Integer> uniqConnections = new HashSet<>();
        while (hypernymsIn.hasNextLine()) {
            String nextLine = hypernymsIn.readLine();
            String[] chunks = nextLine.split(",");
            int parsedSynsetId = Integer.parseInt(chunks[0]);
            for (int i = 1; i < chunks.length; i++) {
                digraph.addEdge(parsedSynsetId, Integer.parseInt(chunks[i]));
            }
            uniqConnections.add(parsedSynsetId);
        }

        if((synsetList.size() - uniqConnections.size()) > 1) {
            throw new IllegalArgumentException("The input have multiple roots!");
        }
    }

    public Iterable<String> nouns() {
        return nounToSynsetsMap.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nounToSynsetsMap.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        Set<Integer> synsetsOfA = nounToSynsetsMap.get(nounA);
        Set<Integer> synsetsOfB = nounToSynsetsMap.get(nounB);

        return sap.length(synsetsOfA, synsetsOfB);
    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        Set<Integer> synsetsOfA = nounToSynsetsMap.get(nounA);
        Set<Integer> synsetsOfB = nounToSynsetsMap.get(nounB);

        int ancestor = sap.ancestor(synsetsOfA, synsetsOfB);
        return synsetList.get(ancestor);
    }

    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        String a = "a";
        String b = "c";
        System.out.println(wordNet.sap(a,b));
        System.out.println(wordNet.distance(a,b));
        Set<String> nouns = (Set<String>)wordNet.nouns();
        System.out.println(nouns.size());
    }
}