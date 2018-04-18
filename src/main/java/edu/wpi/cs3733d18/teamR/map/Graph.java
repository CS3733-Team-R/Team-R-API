package edu.wpi.cs3733d18.teamR.map;


import edu.wpi.cs3733d18.teamR.database.KioskDatabase;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    // Going to be the kiosk location
    HashMap<String, Node> nodes;
    HashMap<String, Node> longNames;
    PathFinder pathFinder;

    public Graph(HashMap<String, Node> nodes) {
        this.nodes = nodes;
        this.longNames = new HashMap<>();
       // pathFinder = new AStar(nodes);
        initLongNames();
    }

    public Graph() {
        this.nodes = new HashMap<>();
        this.longNames = new HashMap<>();
    }


    /**
     * @param id ID of node object to get
     * @return Node in list of nodes with given ID
     */

    public Node getNode(String id) {
        return nodes.get(id);
    }
    public Node getNodeByLongName(String longname) {
        return longNames.get(longname);
    }


    public List<Node> getNodes(){
        return new ArrayList<>(nodes.values());
    }

    public List<Edge> getEdges(){
        List<Edge> edges = new ArrayList<>();
        for (Node n: nodes.values()){
            for(Edge edge: n.edges.values()){
                if (edges.contains(edge))
                        continue;
                edges.add(edge);
            }
        }
        return edges;
    }
    public List<String> getNodesAsStrings(){
        return nodes.values().stream().map(Node::getLongName).collect(Collectors.toList());
    }
    public List<String> getLongNames() { return new ArrayList<>(longNames.keySet()); }

    public void initLongNames() {
        for(Node n : nodes.values()) {
            longNames.put(n.getLongName(), n);
        }
    }

    /**
     * @param node Node that will be added to graph
     */
    public void addNode(Node node) {
        this.nodes.put(node.id, node);
        this.longNames.put(node.getLongName(), node);
    }

    /**
     * Removes node from graph
     * @param id ID of node to remove
     */
    public void removeNode(String id) {
        Node node = getNode(id);
        removeNode(node);

    }

    public void removeNode(Node node){
        for (Map.Entry<Edge, Edge> entry : node.edges.entrySet()) {
            entry.getValue().getNeighbor(node).edges.remove(entry.getValue());
        }
        KioskDatabase.getInstance().removeNode(node);
        nodes.remove(node.id);
        longNames.remove(node.getLongName());
    }

    /**
     * Remove edge from graph
     *
     * @param edge Edge to remove
     */
    void removeEdge(Edge edge) {
        edge.delete();
    }

    public LinkedList<Node> getShortestPath(Node start, Node end, boolean ifHandicap) {
        LinkedList<Node> destination = pathFinder.findShortestPath(start, end, ifHandicap);
        return destination;
    }

}
