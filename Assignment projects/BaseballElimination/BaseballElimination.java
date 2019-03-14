import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Set;

public class BaseballElimination {
    private final int N;
    private HashMap<String, Integer> teamMap;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;

    public BaseballElimination(String filename) {
        In input = new In(filename);
        N = Integer.parseInt(input.readLine());
        teamMap = new HashMap<>();
        wins = new int[N];
        losses = new int[N];
        remaining = new int[N];
        against = new int[N][N];

        for (int i = 0; input.hasNextLine(); ++i) {
            String[] tokens = input.readLine().split(" +");
            teamMap.put(tokens[0], i);
            wins[i] = Integer.parseInt(tokens[1]);
            losses[i] = Integer.parseInt(tokens[2]);
            remaining[i] = Integer.parseInt(tokens[3]);
            for(int j = 0; j < N; ++j) {
                against[i][j] = Integer.parseInt(tokens[4 + j]);
            }
        }
    }

    public int numberOfTeams() {
        return N;
    }

    public Iterable<String> teams() {
        Set<String> strings = teamMap.keySet();
        return strings;
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

        String[] indices = constructIndices(team);
        FlowNetwork flowNet = constructFlowNetwork(indices, team);
        System.out.println(flowNet);
        return true;
    }

    private String[] constructIndices(String team) {
        String[] indices = new String[N - 1];
        int i = 0;
        for(String t : teams()) {
            if (!t.equals(team)) indices[i++] = t;
        }
        return indices;
    }

    private FlowNetwork constructFlowNetwork(String[] indices, String team) {
        int V = 2;
        for (int i = 1; i < N; ++i) {
            V += i;
        }
        FlowNetwork flowNet = new FlowNetwork(V);
        for (int i = 0; i < N - 1; ++i) {
            String other = indices[i];
            flowNet.addEdge(new FlowEdge(V - N + i, V - 1, maxWins(team, other)));
        }
        for (int i = 0; i < N - 2; ++i) {
            for (int j = i + 1; j < N - 1; ++j) {
                String team1 = indices[i];
                String team2 = indices[j];
                int v = 0;
                for (int k = N - 2; k > N - 2 - i; --k) {
                    v += k;
                }
                v += j - i;

                flowNet.addEdge(new FlowEdge(0, v, against(team1, team2)));
                flowNet.addEdge(new FlowEdge(v, V - N + i, Double.MAX_VALUE));
                flowNet.addEdge(new FlowEdge(v, V - N + j - i, Double.MAX_VALUE));
            }
        }
        return flowNet;
    }

    private int maxWins(String team1, String team2) {
        return wins(team1) + remaining(team1) - wins(team2);
    }

    public Iterable<String> certificateOfElimination(String team) {
        return null;
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
