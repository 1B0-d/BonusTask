package mst_edge_removal;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;

public class Main {


    public static class Edge implements Comparable<Edge> {
        public int u;
        public int v;
        public int w;

        public Edge() {}

        public Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        @Override
        public int compareTo(Edge o) {
            return Integer.compare(this.w, o.w);
        }
    }


    public static class InputGraph {
        public int id;
        public int n;
        public List<Edge> edges;

        public InputGraph() {}
    }


    public static class InputWrapper {
        public List<InputGraph> graphs;

        public InputWrapper() {}
    }

    public static class OutputData {
        public int graphId;

        public List<Edge> initialMstEdges;
        public int initialMstWeight;

        public Edge removedEdge;
        public List<List<Integer>> components;

        public Edge replacementEdge;
        public List<Edge> newMstEdges;
        public int newMstWeight;

        public OutputData() {}
    }

    public static class DSU {
        int[] parent, rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        boolean union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return false;
            if (rank[ra] < rank[rb]) {
                int t = ra;
                ra = rb;
                rb = t;
            }
            parent[rb] = ra;
            if (rank[ra] == rank[rb]) rank[ra]++;
            return true;
        }
    }

    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            InputWrapper wrapper = mapper.readValue(new File("inputs.json"), InputWrapper.class);

            if (wrapper.graphs == null || wrapper.graphs.isEmpty()) {
                System.out.println("No graphs in inputs.json");
                return;
            }

            InputGraph input = wrapper.graphs.get(0);
            int n = input.n;
            List<Edge> allEdges = new ArrayList<>(input.edges);

            Collections.sort(allEdges);
            DSU dsu = new DSU(n);
            List<Edge> mst = new ArrayList<>();
            int mstWeight = 0;

            for (Edge e : allEdges) {
                int u = e.u - 1;
                int v = e.v - 1;
                if (dsu.union(u, v)) {
                    mst.add(new Edge(e.u, e.v, e.w));
                    mstWeight += e.w;
                    if (mst.size() == n - 1) break;
                }
            }

            if (mst.size() != n - 1) {
                System.out.println("Graph is not connected, MST does not exist.");
                return;
            }


            List<Edge> initialMstEdges = new ArrayList<>(mst);

            Edge removed = Collections.max(mst, Comparator.comparingInt(e -> e.w));
            mst.remove(removed);

            List<Integer>[] adj = new ArrayList[n];
            for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
            for (Edge e : mst) {
                int u = e.u - 1;
                int v = e.v - 1;
                adj[u].add(v);
                adj[v].add(u);
            }

            int[] comp = new int[n];
            Arrays.fill(comp, -1);
            int compId = 0;
            for (int i = 0; i < n; i++) {
                if (comp[i] == -1) {
                    bfsComponent(i, compId, comp, adj);
                    compId++;
                }
            }

            List<List<Integer>> components = new ArrayList<>();
            for (int c = 0; c < compId; c++) components.add(new ArrayList<>());
            for (int v = 0; v < n; v++) {
                components.get(comp[v]).add(v + 1);
            }

            Edge best = null;
            for (Edge e : allEdges) {
                int u = e.u - 1;
                int v = e.v - 1;
                if (comp[u] != comp[v]) {
                    if (best == null || e.w < best.w) {
                        best = new Edge(e.u, e.v, e.w);
                    }
                }
            }

            if (best != null) {
                mst.add(best);
            }

            int newWeight = 0;
            for (Edge e : mst) newWeight += e.w;

            OutputData out = new OutputData();
            out.graphId = input.id;
            out.initialMstEdges = initialMstEdges;
            out.initialMstWeight = mstWeight;
            out.removedEdge = removed;
            out.components = components;
            out.replacementEdge = best;
            out.newMstEdges = new ArrayList<>(mst);
            out.newMstWeight = newWeight;

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File("output.json"), out);

            System.out.println("Done. See output.json");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void bfsComponent(int start, int id, int[] comp, List<Integer>[] adj) {
        Queue<Integer> q = new ArrayDeque<>();
        comp[start] = id;
        q.add(start);
        while (!q.isEmpty()) {
            int v = q.poll();
            for (int to : adj[v]) {
                if (comp[to] == -1) {
                    comp[to] = id;
                    q.add(to);
                }
            }
        }
    }
}
