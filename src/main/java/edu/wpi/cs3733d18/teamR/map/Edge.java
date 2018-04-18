package edu.wpi.cs3733d18.teamR.map;


public class Edge {
    Node node1, node2;
    double weight;
    double distWeight;
    boolean muted;

    /**
     * @param node1 One of the nodes making up the edge
     * @param node2 The other node making up the edge
     */
    public Edge(Node node1, Node node2) throws IllegalArgumentException {
        if (node1.equals(node2)) {
            throw new IllegalArgumentException();
        }
        this.node1 = node1;
        this.node2 = node2;
        this.weight = node1.distanceTo(node2);
    }

    public double getWeight(){
        if (muted)
            return Double.POSITIVE_INFINITY;
        return this.weight;
    }
    /**
     * Adds this edge to the list of edges for each vertex
     */
    public void addToNodes() {
        node1.edges.put(this, this);
        node2.edges.put(this, this);
    }

    /**
     * @param node Node you're looking for the neighbor of
     * @return Node making up the other vertex of this edge
     */
    public Node getNeighbor(Node node) {
        return node.equals(node1) ? node2 : node1;
    }

    public Node getNode1(){return node1;}
    public Node getNode2(){return node2;}
    public boolean getMuted() {
        return muted;
    }

    /**
     * Removes this edge from each of its vertex's list of edges
     */
    public void delete() {
        node1.edges.remove(this);
        node2.edges.remove(this);
    }

    public void setMuted(boolean ifMuted) {
        muted = ifMuted;
        if (ifMuted) {
            weight = Double.POSITIVE_INFINITY;
        }
        else {
            weight = distWeight;
        }
    }

    public boolean ifBothOfType(NodeType nodeType) {
        // Determine if both of them are this specific type
        return (nodeType == node1.getNodeType() && nodeType == node2.getNodeType());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Edge))
            return false;

        Edge e = (Edge) o;

        // Check if the comparing edge contains the same node as this edge
        return (e.node1.equals(this.node1) && e.node2.equals(this.node2)) || e.node1.equals(this.node2) && e.node2.equals(this.node1);
    }

    @Override
    public int hashCode() {
        return node1.hashCode() ^ node2.hashCode();
    }

}
