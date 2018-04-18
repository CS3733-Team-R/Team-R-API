package edu.wpi.cs3733d18.teamR.database;


import edu.wpi.cs3733d18.teamR.administration.Request;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class RequestDatabase extends Database {
    public RequestDatabase(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void initialize() {
        createRequestTables();
        ArrayList<String> filePaths = new ArrayList<String>();
        filePaths.add("RequestsAPI.csv");
        fromRequestCSV(filePaths);
    }

    private void createRequestTables() {

        String sqlrequest = "CREATE TABLE Requests(" +
                "  name varchar(30)," +
                "  DOB varchar(30)," +
                "  medication varchar(30)," +
                "  address varchar(40)," +
                "  location varchar(40)," +
                "  allergies varchar(30)," +
                "  conditions varchar(30)," +
                "  date varchar(40)," +
                "  time varchar(40)," +
                "  phone varchar(30)," +
                "  signature varchar(30)," +
                "  reqID varchar(20)," +
                "  SSN int," +
                "  PRIMARY KEY (reqID))";

        runSQL(sqlrequest);
    }

    public void removeRequest(String reqID) {
        try {
            PreparedStatement stmt1 = connection.prepareStatement("DELETE FROM Requests WHERE Requests.reqID = ?");

            stmt1.setString(1, reqID);
            stmt1.execute();
            stmt1.close();
        } catch (SQLException e) {
            System.err.println("Could not update apiPackage.database");
            e.printStackTrace();
        }
    }

    public void addRequest(String name, String DOB, String medication, String address, String location, String allergies, String conditions, String date, String time, String phone, String signature, String reqID, int SSN)  {
        try {
            PreparedStatement stmt1 = connection.prepareStatement("INSERT into Requests values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            stmt1.setString(1, name);
            stmt1.setString(2, DOB);
            stmt1.setString(3, medication);
            stmt1.setString(4, address);
            stmt1.setString(5, location);
            stmt1.setString(6, allergies);
            stmt1.setString(7, conditions);
            stmt1.setString(8, date);
            stmt1.setString(9, time);
            stmt1.setString(10, phone);
            stmt1.setString(11, signature);
            stmt1.setString(12, reqID);
            stmt1.setInt(13, SSN);

            stmt1.execute();
            stmt1.close();
        } catch (SQLException e) {
            System.err.println("Could not update apiPackage.database");
            e.printStackTrace();
        }
    }


    private void fromRequestCSV(ArrayList<String> filepathList) {
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
                    addRequest(words[0], words[1], words[2], words[3], words[4], words[5], words[6], words[7]
                            , words[8], words[9], words[10], words[11], Integer.parseInt(words[12]));
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

    public ArrayList<Request> getRequests(){
        ArrayList<Request> returnList = new ArrayList<Request>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Requests"); //" WHERE TeamAssigned = ?");
            ResultSet rset1 = stmt.executeQuery();

            while(rset1.next()){
                String name = rset1.getString("name");
                String DOB = rset1.getString("DOB");
                String medication = rset1.getString("medication");
                String address = rset1.getString("address");
                String location = rset1.getString("location");
                String allergies = rset1.getString("allergies");
                String conditions = rset1.getString("conditions");
                String date = rset1.getString("date");
                String time = rset1.getString("time");
                String phone = rset1.getString("phone");
                String signature = rset1.getString("signature");
                String reqID = rset1.getString("reqID");
                int SSN = rset1.getInt("SSN");

                Request r = new Request( name,  DOB,  medication,  address,  location,  allergies,  conditions,  date,  time,  phone,  signature,  reqID,  SSN) ;
                returnList.add(r);
            }
        }
        catch (SQLException e) {
            System.err.println("Could not find data");
            e.printStackTrace();
        }
        return returnList;
    }


    public LinkedList<String> getRequestElements(String element){
        LinkedList<String> returnList = new LinkedList<>();
        try {
            String s = "SELECT " + element + " FROM Requests ORDER BY reqID ASC";
            PreparedStatement stmt = connection.prepareStatement(s); //" WHERE TeamAssigned = ?");
            ResultSet rset1 = stmt.executeQuery();

            while(rset1.next()){
                returnList.add(rset1.getString(element));
            }
        }
        catch (SQLException e) {
            System.err.println("Could not find data");
            e.printStackTrace();
        }
        //System.out.println(returnList);
        return returnList;
    }

    private void dropRequestTables() {
        String sqlrequest = "DROP TABLE Requests";
        runSQL(sqlrequest);
    }

    private void toRequestCSV() {
        FileWriter fileWriter = null;
        String fileName = "RequestsAPI.csv";
        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.append("name, DOB, medication, address, location, allergies, conditions, date, time, phone, signature, reqID, SSN\n");
            try {
                Statement stmt1 = connection.createStatement();
                ResultSet rset1 = stmt1.executeQuery("SELECT * FROM Requests");
                while (rset1.next()) {
                    fileWriter.append(rset1.getString("name"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("DOB"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("medication"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("address"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("location"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("allergies"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("conditions"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("date"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("time"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("phone"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("signature"));
                    fileWriter.append(",");
                    fileWriter.append(rset1.getString("reqID"));
                    fileWriter.append(",");
                    fileWriter.append(Integer.toString(rset1.getInt("SSN")));
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

    @Override
    public void close() {
        toRequestCSV();
        dropRequestTables();
    }
}

