/**
 * @author Kerem
 * 3/4/2019
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Evacuation {
    private static FastScanner in;

    public static void main(String[] args) throws IOException {
        in = new FastScanner();

        FlowGraph graph = readGraph();
        System.out.println(maxFlow(graph, 0, graph.size() - 1));
    }


    private static Map<Integer, Integer> breadthFSAlgorithm(FlowGraph graph, int from, int to) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(from);
        boolean[] visitDecision = new boolean[graph.size()];
        Map<Integer, Integer> prev = new HashMap<>();

        while (!queue.isEmpty()) {
            Integer currentInteger = queue.poll();
            List<Integer> edgeList = graph.getIds(currentInteger);

            visitDecision[currentInteger] = true;

            for (Integer edNumber : edgeList) {
                Edge ed = graph.getEdge(edNumber);
                if (!visitDecision[ed.to] && !(ed.capacity <= ed.flow)) {
                    prev.put(ed.to, currentInteger);
                    queue.add(ed.to);
                    if (ed.to == to) break;
                }
            }
        }
        return prev;
    }


    private static boolean graphRecal(FlowGraph graph, int output, Map<Integer, Integer> prev) {
        int minCap = Integer.MAX_VALUE;

        int cr = output;
        List<Integer> path = new LinkedList<>();
        while (true) {
            int pr = prev.get(cr);
            List<Integer> edges = graph.getIds(pr);
            for (Integer edgeId : edges) {
                Edge edge = graph.getEdge(edgeId);
                if ((edge.to == cr) && (edge.from == pr) && (edge.capacity > edge.flow)) {
                    path.add(0, edgeId);
                    minCap = Math.min(minCap, edge.capacity - edge.flow);
                    break;
                }
            }
            cr = pr;
            if (pr == 0) {
                break;
            }
        }

        for (Integer edge : path) {
            graph.addFlow(edge, minCap);
        }
        return (minCap < Integer.MAX_VALUE && minCap > 0);
    }

    private static int maxFlow(FlowGraph graph, int from, int to) {
        int flow = 0;
        boolean existAugmentingPath = true;
        while (existAugmentingPath) {
            Map<Integer, Integer> prev = breadthFSAlgorithm(graph, from, to);
            if (prev.get(to) == null) break;
            existAugmentingPath = graphRecal(graph, to, prev);
        }

        for (Integer edge : graph.getIds(from)) {
            Edge current = graph.getEdge(edge);
            if (current.capacity > 0) {
                flow = flow + current.flow;
            }
        }
        return flow;
    }

    static FlowGraph readGraph() throws IOException {
        int vertex_count = in.nextInt();
        int edge_count = in.nextInt();
        FlowGraph graph = new FlowGraph(vertex_count);

        for (int i = 0; i < edge_count; ++i) {
            int from = in.nextInt() - 1, to = in.nextInt() - 1, capacity = in.nextInt();
            graph.addEdge(from, to, capacity);
        }
        return graph;
    }

    static class Edge {
        int from, to, capacity, flow;

        public Edge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }
    }

    /* This class implements a bit unusual scheme to store the graph edges, in order
     * to retrieve the backward edge for a given edge quickly. */
    static class FlowGraph {
        /* List of all - forward and backward - edges */
        private List<Edge> edges;

        /* These adjacency lists store only indices of edges from the edges list */
        private List<Integer>[] graph;

        public FlowGraph(int n) {
            this.graph = (ArrayList<Integer>[]) new ArrayList[n];
            for (int i = 0; i < n; ++i)
                this.graph[i] = new ArrayList<>();
            this.edges = new ArrayList<>();
        }

        public void addEdge(int from, int to, int capacity) {
            /* Note that we first append a forward edge and then a backward edge,
             * so all forward edges are stored at even indices (starting from 0),
             * whereas backward edges are stored at odd indices. */
            Edge forwardEdge = new Edge(from, to, capacity);
            Edge backwardEdge = new Edge(to, from, 0);
            graph[from].add(edges.size());
            edges.add(forwardEdge);
            graph[to].add(edges.size());
            edges.add(backwardEdge);
        }

        public int size() {
            return graph.length;
        }

        public List<Integer> getIds(int from) {
            return graph[from];
        }

        public Edge getEdge(int id) {
            return edges.get(id);
        }

        public void addFlow(int id, int flow) {
            /* To get a backward edge for a true forward edge (i.e id is even), we should get id + 1
             * due to the described above scheme. On the other hand, when we have to get a "backward"
             * edge for a backward edge (i.e. get a forward edge for backward - id is odd), id - 1
             * should be taken.
             *
             * It turns out that id ^ 1 works for both cases. Think this through! */
            edges.get(id).flow += flow;
            edges.get(id ^ 1).flow -= flow;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}