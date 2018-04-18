package edu.wpi.cs3733d18.teamR.database;

import edu.wpi.cs3733d18.teamR.administration.Employee;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class EmployeeDatabase extends Database {
    public EmployeeDatabase(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void initialize() {
        createEmployeeTables();
        ArrayList<String> filePaths = new ArrayList<String>();
        filePaths.add("EmployeesAPI.csv");
        fromEmployeeCSV(filePaths);
    }

    private void createEmployeeTables() {
        String sqlEmp = "CREATE TABLE Employees(" +
                "  name varchar(20)," +
                " empID varchar(30)," +
                "  PRIMARY KEY(empID))";
        runSQL(sqlEmp);
    }


    private void fromEmployeeCSV(ArrayList<String> filepathList) {
        BufferedReader br = null;
        for (String filepath : filepathList) {
            String[] words = null;
            try {
                InputStream in = new FileInputStream(filepath);
                br = new BufferedReader(new InputStreamReader(in));

                String sCurrentLine;

                br.readLine(); //skip first line with row/column names

                while ((sCurrentLine = br.readLine()) != null) {
                    words = sCurrentLine.split(",");
                    addEmployee(words[0], words[1]);
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

    public void addEmployee(String name, String empID) {
        try {
            PreparedStatement stmt1 = connection.prepareStatement("INSERT into Employees values (?, ?)");
            stmt1.setString(1, name);
            stmt1.setString(2, empID);

            stmt1.execute();
            stmt1.close();
        } catch (SQLException e) {
            System.err.println("Could not update apiPackage.database");
            e.printStackTrace();
        }
    }

    private void dropEmployeeTables() {
        String sqlEmp = "DROP TABLE Employees";
        runSQL(sqlEmp);
    }

    private void toEmployeeCSV() {
        FileWriter fileWriter = null;
        String fileName = "EmployeesAPI.csv";
        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.append("name,empID\n");
            try {
                Statement stmt1 = connection.createStatement();
                ResultSet rset1 = stmt1.executeQuery("SELECT * FROM Employees");
                while (rset1.next()) {
                    fileWriter.append(rset1.getString("name"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("empID"));
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
            if(fileWriter != null) {
                closeFW(fileWriter);
            }
        }

    }

    public ArrayList<Employee> getEmployees(){
        ArrayList<Employee> returnList = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Employees"); //" WHERE TeamAssigned = ?");
            ResultSet rset1 = stmt.executeQuery();

            while(rset1.next()){
                String name = rset1.getString("name");
                String empID = rset1.getString("empID");
                Employee e = new Employee(name, empID);
                returnList.add(e);
                //System.out.println(e.getName());
            }
            rset1.close();
            stmt.close();
        }
        catch (SQLException e) {
            System.err.println("Could not find data");
            e.printStackTrace();
        }
        return returnList;
    }

    public void removeEmployee(Employee emp) {
        try {
            PreparedStatement stmt1 = connection.prepareStatement("DELETE FROM Employees WHERE Employees.empID = ?");

            stmt1.setString(1, emp.getEmpID());
            stmt1.execute();
            stmt1.close();
        } catch (SQLException e) {
            System.err.println("Could not update apiPackage.database");
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        toEmployeeCSV();
        dropEmployeeTables();
    }
}
