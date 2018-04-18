package edu.wpi.cs3733d18.teamR.database;


import edu.wpi.cs3733d18.teamR.administration.Employee;
import edu.wpi.cs3733d18.teamR.administration.Request;
import edu.wpi.cs3733d18.teamR.map.Graph;
import edu.wpi.cs3733d18.teamR.map.Node;
import edu.wpi.cs3733d18.teamR.map.NodeType;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class KioskDatabase {
    // Something to hold all the apiPackage.database stuffs
    private static KioskDatabase instance;

    Employee currEmployee;

    private GraphDatabase graphDatabase;
    private RequestDatabase requestDatabase;
    private EmployeeDatabase employeeDatabase;

    private static Connection c;
    int reqCounter = 0;
    int empCounter = 0;

    String floor = "1";
    String building = "Tower";
    String teamAssigned = "Team F";

    public static boolean loggedIn = false;
    public static void setLoggedIn(boolean ifLoggedIn) { loggedIn = ifLoggedIn; }

    private KioskDatabase() {
        initialize();
        reqCounter = getRequests().size();
        empCounter = getEmployees().size();
    }

    private static class KioskDatabaseHelper {
        private static final KioskDatabase instance = new KioskDatabase();
    }

    public static KioskDatabase getInstance() {
        return KioskDatabaseHelper.instance;
    }

    public void initialize() {
        System.out.println("-------Embedded Java DB Connection Testing --------");
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Java DB Driver not found. Add the classpath to your module.");
            System.err.println("For IntelliJ do the following:");
            System.err.println("File | Project Structure, Modules, Dependency tab");
            System.err.println("Add by clicking on the green plus icon on the right of the window");
            System.err.println("Select JARs or directories. Go to the folder where the Java JDK is installed");
            System.err.println("Select the folder java/jdk1.8.xxx/db/lib where xxx is the version.");
            System.err.println("Click OK, compile the code and run it.");
            e.printStackTrace();
            return;
        }

        System.out.println("Java DB driver registered!");
        Connection connection = null;

        try {
            // substitute your apiPackage.database name for myDB
            connection = DriverManager.getConnection("jdbc:derby:APIDB;create=true");  //create=true means that everything is deleted and has to be repopulated (don't do this repeatedly on big datasets)
        } catch (SQLException e) {
            System.err.println("Connection failed. Check output console.");
            e.printStackTrace();
            return;
        }
        System.out.println("Java DB connection established!");
        c = connection;

        requestDatabase = new RequestDatabase(c);
        graphDatabase = new GraphDatabase(c);
        employeeDatabase = new EmployeeDatabase(c);

        graphDatabase.initialize();
        requestDatabase.initialize();
        employeeDatabase.initialize();
    }

    public void close() {
        requestDatabase.close();
        graphDatabase.close();
        employeeDatabase.close();
    }

    public Graph makeKioskGraph() {
        return graphDatabase.makeKioskGraph();
    }

    public static NodeType getNodeType(String id) throws IllegalArgumentException {
        return GraphDatabase.getNodeType(id);
    }

    public int getTypeFrequency(String type) {
        return graphDatabase.getTypeFrequency(type);
    }

    public void addNewNode(Node newNode, String toNode) {
        graphDatabase.addNewNodeOLDVERSION(newNode, toNode, floor, building, teamAssigned);
    }

    public void removeNode(Node node) {
        graphDatabase.removeNode(node);
    }

    public List<Node> retrieveNodes() {
        return null;  //what is this function supposed to do???
    }

    public boolean isFloor(String floor, String id){
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT floor FROM Nodes WHERE NodeID = ?");
            stmt.setString(1, id);
            ResultSet rset = stmt.executeQuery();

            while(rset.next()){
                if(rset.getString("floor").equals(floor)){
                    return true;
                }
                else{
                    return false;
                }
            }

        } catch (SQLException e) {
            System.err.println("Could not access apiPackage.database: " + id);
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> getDisplayNames() {
        ArrayList<String> nodeNames = new ArrayList<>();
        try{
            PreparedStatement stmt = c.prepareStatement("SELECT LONGNAME FROM NODES");
            ResultSet rset = stmt.executeQuery();
            while(rset.next()){
                nodeNames.add(rset.getString("LongName"));
            }

        } catch (SQLException e) {
            System.err.println("Could not access apiPackage.database");
            e.printStackTrace();
        }
        return nodeNames;
    }

    public ArrayList<Request> getRequests(){
        return requestDatabase.getRequests();
    }

    public ArrayList<Employee> getEmployees(){
        return employeeDatabase.getEmployees();
    }

    public LinkedList<String> getReqElements(String element) { return requestDatabase.getRequestElements(element); }

    public void removeEmployee(Employee e){
        employeeDatabase.removeEmployee(e);
    }

    public void removeRequest(String r){
        requestDatabase.removeRequest(r);
    }

    public void addNewRequest(String name, String DOB, String medication, String address, String location, String allergies, String conditions, String date, String time, String phone, String signature, String reqID, int SSN) {
        requestDatabase.addRequest(name, DOB, medication, address, location, allergies, conditions, date, time, phone, signature, reqID, SSN);
    }

    public void addNewEmployee(String name, String empID) {
        employeeDatabase.addEmployee(name,empID);
    }

    public Employee getCurrEmployee() {
        return currEmployee;
    }

    public void setCurrEmployee(Employee currEmployee) {
        this.currEmployee = currEmployee;
    }

    public int getReqCounter() {
        return reqCounter;
    }

    public void setReqCounter(int reqCounter) {
        this.reqCounter = reqCounter;
    }

    public int getEmpCounter() {
        return empCounter;
    }

    public void setEmpCounter(int empCounter) {
        this.empCounter = empCounter;
    }
}

