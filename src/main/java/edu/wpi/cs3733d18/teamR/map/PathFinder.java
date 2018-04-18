package edu.wpi.cs3733d18.teamR.map;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

abstract class PathFinder {

    HashMap<String, Node> nodes;
    boolean ifStairsAccessible = false;

    /**
     * Generates shortest path between two nodes
     *
     * @param start Starting node of the path
     * @param end   Ending node of the path
     * @return List of nodes containing the
     */
    abstract LinkedList<Node> findShortestPath(Node start, Node end, boolean ifHandicap);

    protected final void setHandicap(boolean ifNotStairs) {
        ifStairsAccessible = !ifNotStairs;
        Collection<Node> listOfNodes = nodes.values();
        for (Node n : listOfNodes) {
            if (n.getNodeType() == NodeType.STAI) {
                for (Edge e : n.edges.values()) {
                    if (e.getNeighbor(n).getNodeType() == NodeType.STAI) {
                        // Then mute it
                        e.setMuted(ifNotStairs);
                    }
                }
            }
        }
    }

    /**
     * Given an end node, constructs path using the parent parameter
     *
     * @param end Node marking the end of path
     * @return List of nodes in the shortest path in order from start to end
     */
    final LinkedList<Node> reconstruct_path(Node end) {
        Node current = end;
        LinkedList<Node> totalPath = new LinkedList<>();
        totalPath.add(current);
        while (current.parent != null) {
            Node tmp = current;
            current = current.parent;
            tmp.parent = null;
            totalPath.add(current);
        }
        Collections.reverse(totalPath);
        return totalPath;
    }

    /**
     * @return Hashmap with all values of positive infinity
     */
    final HashMap<Node, Double> initializeAllToInfinity() {
        HashMap<Node, Double> distances = new HashMap<>();
        nodes.forEach((k, v) -> distances.put(v, Double.POSITIVE_INFINITY));
        return distances;
    }


}
