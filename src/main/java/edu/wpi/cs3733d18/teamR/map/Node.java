package edu.wpi.cs3733d18.teamR.map;

import java.util.HashMap;

public class Node {
    String id;
    HashMap<Edge, Edge> edges;
    double heuristic;
    Node parent;
    private int xCoord, yCoord, xCoord3D, yCoord3D;
    private String shortName;
    private String longName;
    private String floor;
    private NodeType nodeType;
    private boolean muted;

    public Node(String id, int xCoord, int yCoord, int xCoord3D, int yCoord3D, String floor, String longName, String shortName, NodeType nodeType) {
        this.id = id;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.xCoord3D = xCoord3D;
        this.yCoord3D = yCoord3D;
        this.floor = floor;
        this.shortName = shortName;
        this.longName = longName;
        this.nodeType = nodeType;
        muted = false;
        edges = new HashMap<>();
    }

    /**
     * @param node Node to calculate distance to
     * @return distance between the two nodes
     */
    public String getId() {
        return this.id;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int newVal) {
        this.xCoord = newVal;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int newVal) {
        this.yCoord = newVal;
    }

    public int getxCoord3D() {
        return xCoord3D;
    }

    public void setxCoord3D(int newVal) {
        this.xCoord3D = newVal;
    }

    public int getyCoord3D() {
        return yCoord3D;
    }

    public void setyCoord3D(int newVal) {
        this.yCoord3D = newVal;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String newVal) {
        this.longName = newVal;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String newVal) {
        this.shortName = newVal;
    }

    public NodeType getNodeType() {
        return this.nodeType;
    }

    public void setNodeType(String newVal) {
        this.nodeType = NodeType.valueOf(newVal);
    }

    public void setNodeType(NodeType newVal) {
        this.nodeType = newVal;
    }

    public int getFloor() throws IllegalStateException {
        switch (this.floor) {
            case "L2":
                return 0;
            case "L1":
                return 1;
            case "1":
            case "01":
                return 2;
            case "2":
            case "02":
                return 3;
            case "3":
            case "03":
                return 4;
            default:
                throw new IllegalStateException(this.floor);
        }
    }

    /**
     * @param node Node to calculate distance to
     * @return distance between the two nodes
     */
    public double distanceTo(Node node) {
        double dx = this.xCoord - node.xCoord;
        double dy = this.yCoord - node.yCoord;
        double dz = 300 * (this.getFloor() - node.getFloor());
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * @param end Goal node of path
     */
    void setHeuristic(Node end) {
        this.heuristic = distanceTo(end);
        if (this.nodeType == NodeType.ELEV || this.nodeType == NodeType.STAI)
            this.heuristic += 2000;
    }

    /**
     * @param withNode Adjacent node that creates an edge with this node
     * @return Edge with this node and withNode as its two vertices
     */
    public Edge getEdge(Node withNode) {
        return edges.get((new Edge(this, withNode)));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Node))
            return false;

        Node n = (Node) o;

        return n.id.equals(this.id);
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public void setNodeID(String newValue) {
        this.id = newValue;
    }
}
