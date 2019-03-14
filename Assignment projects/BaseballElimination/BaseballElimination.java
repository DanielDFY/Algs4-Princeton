import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

import java.util.HashMap;

public class BaseballElimination {
    private final int teamNum;
    private final HashMap<String, Integer> teamMap;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] against;

    public BaseballElimination(String filename) {
        In input = new In(filename);
        teamNum = Integer.parseInt(input.readLine());
        teamMap = new HashMap<>();
        wins = new int[teamNum];
        losses = new int[teamNum];
        remaining = new int[teamNum];
        against = new int[teamNum][teamNum];
        for (int i = 0; input.hasNextLine(); ++i) {
            String line = input.readLine();
            if (line == null)
                break;
            String[] tokens = line.trim().split(" +");
            teamMap.put(tokens[0], i);
            wins[i] = Integer.parseInt(tokens[1]);
            losses[i] = Integer.parseInt(tokens[2]);
            remaining[i] = Integer.parseInt(tokens[3]);
            for (int j = 0; j < teamNum; ++j) {
                against[i][j] = Integer.parseInt(tokens[4 + j]);
            }
        }
    }

    public int numberOfTeams() {
        return teamNum;
    }

    public Iterable<String> teams() {
        return teamMap.keySet();
    }

    public int wins(String team) {
        if (!teamMap.containsKey(team))
            throw new IllegalArgumentException();
        return wins[teamMap.get(team)];
    }

    public int losses(String team) {
        if (!teamMap.containsKey(team))
            throw new IllegalArgumentException();
        return losses[teamMap.get(team)];
    }

    public int remaining(String team) {
        if (!teamMap.containsKey(team))
            throw new IllegalArgumentException();
        return remaining[teamMap.get(team)];
    }

    public int against(String team1, String team2) {
        if (!teamMap.containsKey(team1) || !teamMap.containsKey(team2))
            throw new IllegalArgumentException();
        return against[teamMap.get(team1)][teamMap.get(team2)];
    }

    public boolean isEliminated(String team) {
        if (!teamMap.containsKey(team))
            throw new IllegalArgumentException();
        if (teamNum < 2)
            return false;
        Bag<String> subSet = triElimination(team);
        if (!subSet.isEmpty())
            return true;
        subSet = nonTriElimination(team);
        return !(subSet == null);
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!teamMap.containsKey(team))
            throw new IllegalArgumentException();
        Bag<String> subSet = triElimination(team);
        if (!subSet.isEmpty())
            return subSet;
        subSet = nonTriElimination(team);
        return subSet;
    }

    private int maxWins(String team1, String team2) {
        return wins(team1) + remaining(team1) - wins(team2);
    }

    private Bag<String> triElimination(String team) {
        Bag<String> subSet = new Bag<>();
        for (String t : teams()) {
            if (!t.equals(team) && maxWins(team, t) < 0) {
                subSet.add(t);
            }
        }
        return subSet;
    }

    private Bag<String> nonTriElimination(String team) {
        Bag<String> subSet = new Bag<>();
        String[] indices = constructIndices(team);
        FlowNetwork flowNet = constructFlowNetwork(indices, team);
        /*
         * Use FordFulkerson data type to check whether team vertices are in the min-cut
         * If so, put them into subSet
         */

        int vertexNum = flowNet.V();
        FordFulkerson fordFulkerson = new FordFulkerson(flowNet, 0, vertexNum - 1);
        for (int i = 0; i < teamNum - 1; ++i) {
            boolean inMincut = fordFulkerson.inCut(vertexNum - teamNum + i);
            if (inMincut) {
                subSet.add(indices[i]);
            }
        }
        return subSet.isEmpty() ? null : subSet;
    }

    private String[] constructIndices(String team) {
        String[] indices = new String[teamNum - 1];
        int i = 0;
        for (String t : teams()) {
            if (!t.equals(team)) indices[i++] = t;
        }
        return indices;
    }

    private FlowNetwork constructFlowNetwork(String[] indices, String team) {
        // create vertices
        int vertexNum = 2;
        for (int i = 1; i < teamNum; ++i) {
            vertexNum += i;
        }
        FlowNetwork flowNet = new FlowNetwork(vertexNum);
        // initialize team vertices
        for (int i = 0; i < teamNum - 1; ++i) {
            String other = indices[i];
            flowNet.addEdge(new FlowEdge(vertexNum - teamNum + i, vertexNum - 1, maxWins(team, other)));
        }
        // initialize game vertices
        for (int i = 0; i < teamNum - 2; ++i) {
            for (int j = i + 1; j < teamNum - 1; ++j) {
                String team1 = indices[i];
                String team2 = indices[j];
                int v = 0;
                for (int k = teamNum - 2; k > teamNum - 2 - i; --k) {
                    v += k;
                }
                v += j - i;

                flowNet.addEdge(new FlowEdge(0, v, against(team1, team2)));
                flowNet.addEdge(new FlowEdge(v, vertexNum - teamNum + i, Double.POSITIVE_INFINITY));
                flowNet.addEdge(new FlowEdge(v, vertexNum - teamNum + j, Double.POSITIVE_INFINITY));
            }
        }
        return flowNet;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
