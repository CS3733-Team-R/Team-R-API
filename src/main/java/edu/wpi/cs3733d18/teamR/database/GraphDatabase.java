package edu.wpi.cs3733d18.teamR.database;


import edu.wpi.cs3733d18.teamR.map.Edge;
import edu.wpi.cs3733d18.teamR.map.Graph;
import edu.wpi.cs3733d18.teamR.map.Node;
import edu.wpi.cs3733d18.teamR.map.NodeType;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphDatabase extends Database {
    public GraphDatabase(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void initialize() {
        createMapTables();
        parseNodeCSV();
        parseEdgeCSV();
    }

    public Graph makeKioskGraph() {
        HashMap<String, Node> nodeList = new HashMap<>();
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Nodes"); //" WHERE TeamAssigned = ?");
            ResultSet rset1 = stmt.executeQuery();

            //int counter = 0;
            while (rset1.next()) {
                String id = rset1.getString("nodeID");
                int xcoord = rset1.getInt("xcoord");
                int ycoord = rset1.getInt("ycoord");
                String floor = rset1.getString("floor");
                //building = rset1.getString("building");
                String nodeType = rset1.getString("nodeType");
                String longName = rset1.getString("longName");
                String shortName = rset1.getString("shortName");
                //teamAssigned = rset1.getString("teamAssigned");
                int x3D = rset1.getInt("xcoord3D");
                int y3D = rset1.getInt("ycoord3D");

                Node curNode = new Node(id, xcoord, ycoord, x3D, y3D, floor, longName, shortName, NodeType.valueOf(nodeType));

                nodeList.put(id, curNode);
                nodeArrayList.add(curNode);
            }

            PreparedStatement stmt3 = connection.prepareStatement("SELECT * FROM Edges WHERE startNode = ?");
            for (Node n : nodeArrayList) {
                stmt3.setString(1,n.getId());
                rset1 = stmt3.executeQuery();
                while (rset1.next()) {
                    String startNodeID = rset1.getString("startNode");
                    String endNodeID = rset1.getString("endNode");

                    Edge curEdge = new Edge(nodeList.get(startNodeID), nodeList.get(endNodeID));
                    curEdge.addToNodes();
                }
            }
            rset1.close();
        } catch (SQLException e) {
            System.err.println("Could not find data");
            e.printStackTrace();
        }

        return new Graph(nodeList);
    }

    public static NodeType getNodeType(String id) throws IllegalArgumentException {
        String type = parseNodeType(id);
        return NodeType.valueOf(type);
    }

    private static String parseNodeType(String id) {
        String test;
        try {
            test = id.substring(1,5);
        }
        catch(StringIndexOutOfBoundsException e){
            test = "unknown";
        }

        return test;
    }

    private void createMapTables() {
        String sqlnode = "CREATE TABLE Nodes(" +
                "  nodeID varchar(20)," +
                "  xcoord int," +
                "  ycoord int," +
                "  floor varchar(2)," +
                "  building varchar(20)," +
                "  nodeType varchar(10)," +
                "  longName varchar(80)," +
                "  shortName varchar(50)," +
                "  teamAssigned varchar(20)," +
                "  xCoord3D int," +
                "  yCoord3D int," +
                "  PRIMARY KEY (nodeID))";

        String sqledge = "CREATE TABLE Edges(" +
                "  edgeID varchar(50)," +
                "  startNode varchar(20)," +
                "  endNode varchar(20)," +
                "  PRIMARY KEY (edgeID)," +
                "  FOREIGN KEY (startNode) REFERENCES Nodes(nodeID)," +  //I'd like to get this working, requires all the edges to have nodes
                "  FOREIGN KEY (endNode) REFERENCES Nodes(nodeID))";
        runSQL(sqlnode);
        runSQL(sqledge);
    }

    public int getTypeFrequency(String type) {
        int nodeCount = 0;
        try {
            PreparedStatement stmt2 = connection.prepareStatement("SELECT COUNT(NODEID) AS nodeCount FROM Nodes WHERE NODETYPE = ? GROUP BY NODETYPE");
            stmt2.setString(1, type);
            ResultSet rset1 = stmt2.executeQuery();
            while (rset1.next()) {
                nodeCount = rset1.getInt("nodeCount");
            }
            rset1.close();
        } catch (SQLException e) {
            System.err.println("Could not find data");
            e.printStackTrace();
        }
        return nodeCount;
    }

    public void addNewNodeOLDVERSION(Node newNode, String toNode, String floor, String building, String teamAssigned) {
        addNode(newNode.getId(), newNode.getxCoord(), newNode.getyCoord(), floor,
                building, newNode.getNodeType().name(), newNode.getLongName(), newNode.getShortName(),
                teamAssigned, newNode.getxCoord3D(), newNode.getyCoord3D());

        String edgeID = newNode.getId() + "_" + toNode;
        addEdge(edgeID, newNode.getId(), toNode);
    }

    public void addNewNode(Node newNode, String floor, String building, String teamAssigned) {
        addNode(newNode.getId(), newNode.getxCoord(), newNode.getyCoord(), floor,
                building, newNode.getNodeType().name(), newNode.getLongName(), newNode.getShortName(),
                teamAssigned, newNode.getxCoord3D(), newNode.getyCoord3D());
    }

    public void addNewEdge(Node newNode, Node toNode) {
        addEdge(newNode.getId() + "_" + toNode.getId(), newNode.getId(), toNode.getId());
    }

    private void addNode(String nodeID, int x, int y, String floor, String bldng, String type, String lname, String sname, String team, int x3d, int y3d){
        try {
            PreparedStatement stmt1 = connection.prepareStatement("INSERT into Nodes values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            stmt1.setString(1, nodeID);
            stmt1.setInt(2, x);
            stmt1.setInt(3, y);
            stmt1.setString(4, floor);
            stmt1.setString(5, bldng);
            stmt1.setString(6, type);
            stmt1.setString(7, lname);
            stmt1.setString(8, sname);
            stmt1.setString(9, team);
            stmt1.setInt(10, x3d);
            stmt1.setInt(11, y3d);

            stmt1.execute();
            stmt1.close();
        } catch (SQLException e) {
            System.err.println("Could not update apiPackage.database: " + nodeID);
            e.printStackTrace();
        }
    }

    private void addEdge(String id, String start, String end){
        try {
            PreparedStatement stmt1 = connection.prepareStatement("INSERT into Edges values (?, ?, ?)");
            stmt1.setString(1, id);
            stmt1.setString(2, start);
            stmt1.setString(3, end);

            stmt1.execute();
            stmt1.close();
        } catch (SQLException e) {
            System.err.println("Could not update apiPackage.database");
            e.printStackTrace();
        }
    }

    public void removeNode(Node node) {
        try {
            PreparedStatement stmt1 = connection.prepareStatement("DELETE FROM Nodes WHERE Nodes.NODEID = ?");
            PreparedStatement stmt2 = connection.prepareStatement("DELETE FROM Edges WHERE Edges.STARTNODE = ?");
            PreparedStatement stmt3 = connection.prepareStatement("DELETE FROM Edges WHERE EDGES.ENDNODE = ?");

            stmt1.setString(1, node.getId());
            stmt2.setString(1, node.getId());
            stmt3.setString(1, node.getId());

            stmt2.execute();
            stmt3.execute();
            stmt1.execute();

            stmt1.close();
            stmt2.close();
            stmt3.close();

        } catch (SQLException e) {
            System.err.println("Could not update apiPackage.database");
            e.printStackTrace();
        }
    }

    private void parseNodeCSV(){
        ArrayList<String> nodeFiles = new ArrayList<>();
        nodeFiles.add("nodes/MapAnodes.csv");
        nodeFiles.add("nodes/MapBnodes.csv");
        nodeFiles.add("nodes/MapCnodes.csv");
        nodeFiles.add("nodes/MapDnodes.csv");
        nodeFiles.add("nodes/MapEnodes.csv");
        nodeFiles.add("nodes/MapFnodes.csv");
        nodeFiles.add("nodes/MapGnodes.csv");
        nodeFiles.add("nodes/MapHnodes.csv");
        nodeFiles.add("nodes/MapInodes.csv");
        nodeFiles.add("nodes/MapWnodes.csv");

        fromNodeCSV(nodeFiles);
    }

    private void parseEdgeCSV() {
        ArrayList<String> edgeFiles = new ArrayList<>();
        edgeFiles.add("edges/MapAedges.csv");
        edgeFiles.add("edges/MapBedges.csv");
        edgeFiles.add("edges/MapCedges.csv");
        edgeFiles.add("edges/MapDedges.csv");
        edgeFiles.add("edges/MapEedges.csv");
        edgeFiles.add("edges/MapFedges.csv");
        edgeFiles.add("edges/MapGedges.csv");
        edgeFiles.add("edges/MapHedges.csv");
        edgeFiles.add("edges/MapIedges.csv");
        edgeFiles.add("edges/MapWedges.csv");

        fromEdgeCSV(edgeFiles);
    }

    private void fromNodeCSV(ArrayList<String> filepathList) {
        BufferedReader br = null;
        for (String filepath : filepathList) {
            String[] words = null;
            try {
                InputStream in = KioskDatabase.class.getResourceAsStream("/" + filepath);
                br = new BufferedReader(new InputStreamReader(in));

                String sCurrentLine;

                br.readLine(); //skip first line with row/column names

                while ((sCurrentLine = br.readLine()) != null) {
                    words = sCurrentLine.split(",");
                    addNode(words[0], Integer.parseInt(words[1]), Integer.parseInt(words[2]),
                            words[3], words[4], words[5], words[6], words[7], words[8],
                            Integer.parseInt(words[9]), Integer.parseInt(words[10]));
                }
                br.close();
                in.close();
            } catch (IOException e) {
                System.err.println("Connection failed. Could not find CSV files");
                e.printStackTrace();
                // return 0;
            }
        }
    }

    private void toNodeCSV() {
        FileWriter fileWriter = null;
        String fileName = "edit_nodes.csv";// + this.nodeFilepaths.get(0);
        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.append("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName,teamAssigned,xcoord3D,ycoord3D\n");
            try {
                PreparedStatement stmt1 = connection.prepareStatement("SELECT * FROM Nodes");
                ResultSet rset1 = stmt1.executeQuery();
                while (rset1.next()) {
                    fileWriter.append(rset1.getString("nodeID"));
                    fileWriter.append(",");
                    fileWriter.append(Integer.toString(rset1.getInt("xcoord")));
                    fileWriter.append(",");
                    fileWriter.append(Integer.toString(rset1.getInt("ycoord")));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("floor"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("building"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("nodeType"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("longName"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("shortName"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("teamAssigned"));
                    fileWriter.append(",");
                    fileWriter.append(Integer.toString(rset1.getInt("xcoord")));
                    fileWriter.append(",");
                    fileWriter.append(Integer.toString(rset1.getInt("ycoord")));
                    fileWriter.append("\n");
                }
                rset1.close();
                stmt1.close();
            } catch (SQLException e) {
                System.err.println("Could not update apiPackage.database");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Connection failed. Could not create CSV files");
            e.printStackTrace();
        } finally {
            closeFW(fileWriter);
        }
    }

    private void toEdgeCSV() {
        FileWriter fileWriter = null;
        String fileName = "edit_edges.csv";
        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.append("edgeID,startNode,endNode\n");
            try {
                PreparedStatement stmt1 = connection.prepareStatement("SELECT * FROM Edges");
                ResultSet rset1 = stmt1.executeQuery();
                while (rset1.next()) {
                    fileWriter.append(rset1.getString("edgeID"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("startNode"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("endNode"));
                    fileWriter.append("\n");
                }
                rset1.close();
                stmt1.close();
            } catch (SQLException e) {
                System.err.println("Could not update apiPackage.database");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Connection failed. Could not create CSV files");
            e.printStackTrace();
        } finally {
            closeFW(fileWriter);
        }
    }

    private void fromEdgeCSV(ArrayList<String> filepathList) {
        BufferedReader br = null;
        for (String filepath : filepathList) {
            try {
                InputStream in = KioskDatabase.class.getResourceAsStream("/" + filepath);
                br = new BufferedReader(new InputStreamReader(in));

                String sCurrentLine;

                br.readLine(); //skip first line with row/column names

                while ((sCurrentLine = br.readLine()) != null) {
                    String[] words = sCurrentLine.split(",");
                    addEdge(words[0], words[1], words[2]);
                }
                br.close();
                in.close();
            } catch (IOException e) {
                System.err.println("Connection failed. Could not find CSV files");
                e.printStackTrace();
            }
        }
    }

    private void dropMapTables() {
        String sqlnode = "DROP TABLE Nodes";
        String sqledge = "DROP TABLE Edges";

        runSQL(sqledge);
        runSQL(sqlnode);
    }

    @Override
    public void close() {
        toNodeCSV();
        toEdgeCSV();
        dropMapTables();
    }
}

