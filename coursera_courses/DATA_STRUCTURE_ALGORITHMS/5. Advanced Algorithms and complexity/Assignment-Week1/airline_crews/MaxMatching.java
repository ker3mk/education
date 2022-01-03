/**
 * @author Kerem
 * 4/4/2019
 */
import java.io.*;
import java.util.*;

public class MaxMatching {
    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new MaxMatching().solve();
    }

    //Used bfs in it.
    private static void maxFindFlows(FlowGraph graph) {
        for (; ; ) {
            boolean foundPath = false;
            Queue<Integer> queue = new LinkedList<Integer>();
            int[] EdgePointList = new int[graph.size()];
            Arrays.fill(EdgePointList, -1);
            queue.add(0);

            while (!queue.isEmpty() && !foundPath) {
                int point = queue.poll();
                List<Integer> graphGainedId = graph.getIds(point);
                for (int i = 0; i < graphGainedId.size(); i++) {
                    Edge edge = graph.getEdge(graphGainedId.get(i));
                    if (edge.flow < edge.capacity && EdgePointList[edge.to] == -1) {
                        if (edge.to == edge.from) {
                            continue;
                        }
                        EdgePointList[edge.to] = graphGainedId.get(i);
                        if (edge.to == graph.size() - 1) {
                            foundPath = true;
                            break;
                        }
                        queue.add(edge.to);
                    }
                }
            }
            if (!foundPath) {
                break;
            }

            int output = graph.size() - 1;
            int capSminmum = -1;
            while (output != 0) {
                int id = EdgePointList[output];
                Edge edge = graph.getEdge(id);
                if (capSminmum == -1 || (edge.capacity - edge.flow) < capSminmum) {
                    capSminmum = edge.capacity - edge.flow;
                }
                output = edge.from;
            }
            output = graph.size() - 1;
            while (output != 0) {
                int id = EdgePointList[output];
                Edge edge = graph.getEdge(id);
                graph.addFlow(id, capSminmum);
                output = edge.from;
            }
        }
    }

    static FlowGraph readGraph(boolean[][] graphpart) {
        int n = graphpart.length;
        int m = graphpart[0].length;
        int lineNumber = n + m + 2;
        FlowGraph graph = new FlowGraph(lineNumber);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!graphpart[i][j]) {
                    continue;
                }
                graph.addEdge(i + 1, n + 1 + j, 1);
            }
        }
        for (int i = 0; i < m; i++) {
            graph.addEdge(n + 1 + i, n + m + 1, 1);
        }
        for (int i = 0; i < n; i++) {
            graph.addEdge(0, i + 1, 1);
        }
        return graph;
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        boolean[][] bipartiteGraph = readData();
        int[] matching = findMatching(bipartiteGraph);
        writeResponse(matching);
        out.close();
    }

    boolean[][] readData() throws IOException {
        int numLeft = in.nextInt();
        int numRight = in.nextInt();
        boolean[][] adjMatrix = new boolean[numLeft][numRight];
        for (int i = 0; i < numLeft; ++i)
            for (int j = 0; j < numRight; ++j)
                adjMatrix[i][j] = (in.nextInt() == 1);
        return adjMatrix;
    }

    private int[] findMatching(boolean[][] graphPartitie) {

        int numLeft = graphPartitie.length;
        int[] matching = new int[numLeft];
        FlowGraph mapFlow = readGraph(graphPartitie);
        maxFindFlows(mapFlow);
        for (int i = 0; i < numLeft; i++) {
            for (int j : mapFlow.getIds(i + 1)) {
                Edge edge = mapFlow.getEdge(j);
                if (edge.flow == 1) {
                    matching[i] = edge.to - numLeft;
                    break;
                }
                matching[i] = -1;
            }
        }

        return matching;
    }

    private void writeResponse(int[] matching) {
        for (int i = 0; i < matching.length; ++i) {
            if (i > 0) {
                out.print(" ");
            }
            if (matching[i] == -1) {
                out.print("-1");
            } else {
                out.print(matching[i]);
            }
        }
        out.println();
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
            for (int i = 0; i < n; ++i) {
                this.graph[i] = new ArrayList<>();
            }
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