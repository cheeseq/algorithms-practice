import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SAP {
    private Digraph graph;

    private Map<Set<Integer>, Integer[]> cache = new HashMap<>();

    private static final int ANCESTOR_INDEX = 0;
    private static final int LENGTH_INDEX = 1;

    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        graph = new Digraph(G);
    }

    public int length(int v, int w) {
        List<Integer> iterableV = Arrays.asList(v);
        List<Integer> iterableW = Arrays.asList(w);
        Set<Integer> cacheKey = createCacheKey(iterableV, iterableW);
        bfs(iterableV, iterableW);
        if (cache.get(cacheKey) == null) {
            return -1;
        }
        return cache.get(cacheKey)[LENGTH_INDEX];
    }

    public int ancestor(int v, int w) {
        List<Integer> iterableV = Arrays.asList(v);
        List<Integer> iterableW = Arrays.asList(w);
        Set<Integer> cacheKey = createCacheKey(iterableV, iterableW);
        bfs(iterableV, iterableW);
        if (cache.get(cacheKey) == null) {
            return -1;
        }
        return cache.get(cacheKey)[ANCESTOR_INDEX];
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateIterable(v);
        validateIterable(w);
        Set<Integer> cacheKey = createCacheKey(v, w);
        bfs(v, w);
        if (cache.get(cacheKey) == null) {
            return -1;
        }
        return cache.get(cacheKey)[LENGTH_INDEX];
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateIterable(v);
        validateIterable(w);
        Set<Integer> cacheKey = createCacheKey(v, w);
        bfs(v, w);
        if (cache.get(cacheKey) == null) {
            return -1;
        }
        return cache.get(cacheKey)[ANCESTOR_INDEX];
    }

    private void bfs(Iterable<Integer> v, Iterable<Integer> w) {
        Set<Integer> cacheKey = createCacheKey(v, w);
        if (cache.get(cacheKey) != null) {
            return;
        }
        BreadthFirstDirectedPaths pathsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths pathsW = new BreadthFirstDirectedPaths(graph, w);
        for (int vertex = 0; vertex < graph.V(); vertex++) {
            if (!pathsV.hasPathTo(vertex) || !pathsW.hasPathTo(vertex)) {
                continue;
            }
            int distanceSum = pathsV.distTo(vertex) + pathsW.distTo(vertex);
            if (cache.get(cacheKey) == null || distanceSum < cache.get(cacheKey)[LENGTH_INDEX]) {
                cache.put(cacheKey, new Integer[] { vertex, distanceSum });
            }
        }
        if (cache.get(cacheKey) == null) {
            for (Integer i : w) {
                if (pathsV.hasPathTo(i)) {
                    cache.put(cacheKey, new Integer[] { i, 1 });
                    break;
                }
            }
            for (Integer i : v) {
                if (pathsW.hasPathTo(i)) {
                    cache.put(cacheKey, new Integer[] { i, 1 });
                    break;
                }
            }
        }
    }

    private Set<Integer> createCacheKey(Iterable<Integer> v, Iterable<Integer> w) {
        Set<Integer> cacheKey = new HashSet<>();
        for (Integer vertex : v) {
            cacheKey.add(vertex);
        }
        for (Integer vertex : w) {
            cacheKey.add(vertex);
        }
        return cacheKey;
    }

    private void validateIterable(Iterable<Integer> input) {
        if (input == null) throw new IllegalArgumentException();
        for (Object item : input) {
            if (item == null) throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        Digraph g = new Digraph(new In(args[0]));
        System.out.println(g);
        SAP sap = new SAP(g);
        SET<Integer> v = new SET<>();
        v.add(81679);
        v.add(81680);
        v.add(81681);
        v.add(81682);
        SET<Integer> w = new SET<>();
        w.add(24306);
        w.add(24307);
        w.add(25293);
        w.add(33764);
        w.add(70067);
        int vint = 1;
        int wint = 2;
        System.out.println(String.format("ancestor is %d", sap.ancestor(vint, wint)));
        System.out.println(String.format("length is %d", sap.length(vint, wint)));
        System.out.println(String.format("ancestor iterable is %d", sap.ancestor(v,w)));
        System.out.println(String.format("length iterable is %d", sap.length(v,w)));
    }
}
