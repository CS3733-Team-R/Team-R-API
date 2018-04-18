package edu.wpi.cs3733d18.teamR.controllers;


import edu.wpi.cs3733d18.teamR.administration.Employee;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733d18.teamR.database.KioskDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.textfield.TextFields;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import email.com.gmail.ttsai0509.escpos.EscPosBuilder;
import email.com.gmail.ttsai0509.escpos.command.Align;
import email.com.gmail.ttsai0509.escpos.command.Font;


public class FormController extends Controller implements Initializable {
    @FXML
    JFXButton back;
    @FXML
    JFXTextField name;
    @FXML
    JFXTextField SSN;
    @FXML
    JFXDatePicker DOB;
    @FXML
    JFXTextField address;
    @FXML
    JFXTextField phone;
    @FXML
    JFXDatePicker date;
    @FXML
    CheckComboBox<String> allergies;
    @FXML
    CheckComboBox conditions;
    @FXML
    JFXTextField medication;
    @FXML
    JFXTextField signature;
    @FXML
    TextField locations;
    @FXML
    Label alle;
    @FXML
    Label condd;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> ln = KioskDatabase.getInstance().getDisplayNames();
        TextFields.bindAutoCompletion(locations, ln);
        allergies.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {
               alle.setText("");
            }
        });
        conditions.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                condd.setText("");
            }
        });


        ObservableList<String> aller = FXCollections.observableArrayList("None", "Ampicillin" ,"Aspirin", "Cephalosporins"
                , "Codeine", "Erythromycin", "Morphine","SAIDS" , "Penicillin", "Sulfa","Tetracycline" );
        allergies.getItems().addAll(aller);
        ObservableList<String> cond = FXCollections.observableArrayList("None", "Arthritis" ,"Asthma", "COPD"
                , "Depression", "Diabetes", "Glaucoma","High Cholesterol" , "Hypertension", "Kidney Disease","Liver Disease", "Seasonal Allergies",  "Seizures/Epilepsy", "Thyroid", "Ulcers");
        conditions.getItems().addAll(cond);
    }

    public void back() throws IOException {
        goToController("/fxml/HubUI.fxml");
    }

    public void submit() throws IOException {
        try {
            int numberOfRequests = KioskDatabase.getInstance().getReqCounter();
            numberOfRequests++;
            KioskDatabase.getInstance().setReqCounter(numberOfRequests);

            String birth = "0";
            String currDay = "0";
            String nodeID = "N/A";
            int social = 0;
            if (DOB.getValue() != null) {
                birth = DOB.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            if (date.getValue() != null) {
                currDay = date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            if (!(SSN.getText().equals(""))) {
                social = Integer.parseInt(SSN.getText());
            }

            if (!(locations.getText().equals(""))) {
                nodeID = KioskDatabase.getInstance().makeKioskGraph().getNodeByLongName(locations.getText()).getId();
            }

            String req = ("Rx" + Integer.toString(numberOfRequests));
            System.out.println(req);

            KioskDatabase.getInstance().addNewRequest(
                    name.getText(),
                    birth,
                    medication.getText(),
                    address.getText(),
                    nodeID,
                    encodeAllergies(),
                    encodeConditions(),
                    currDay,
                    java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")),
                    phone.getText(),
                    checkSignature(),
                    req,
                    social
            );
            goToController("/fxml/FormUI.fxml");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Submission:");
            alert.setContentText("Rx Submitted to database.");
            // alert.initOwner(empName.getScene().getWindow());
            alert.showAndWait();
        }
        catch (Exception e) {
            if(e.getClass().equals(NumberFormatException.class)){
                goToController("/fxml/FormUI.fxml");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Entry Error:");
                alert.setContentText("This is not a valid SSN.");
                // alert.initOwner(empName.getScene().getWindow());
                alert.showAndWait();
            }
            else {
                goToController("/fxml/FormUI.fxml");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Lookup Error:");
                alert.setContentText("This is not a valid doctor name.");
                // alert.initOwner(empName.getScene().getWindow());
                alert.showAndWait();
            }
        }

    }

    public String encodeAllergies(){
        String alcode = "";
        ArrayList<Integer> arr = new ArrayList<Integer> (allergies.getCheckModel().getCheckedIndices());
        if(arr.contains(0)){
            alcode += "0";
        }
        if(arr.contains(1)){
            alcode += "1";
        }
        if(arr.contains(2)){
            alcode += "2";
        }
        if(arr.contains(3)){
            alcode += "3";
        }
        if(arr.contains(4)){
            alcode += "4";
        }
        if(arr.contains(5)){
            alcode += "5";
        }
        if(arr.contains(6)){
            alcode += "6";
        }
        if(arr.contains(7)){
            alcode += "7";
        }
        if(arr.contains(8)){
            alcode += "8";
        }
        if(arr.contains(9)){
            alcode += "9";
        }
        if(arr.contains(10)){
            alcode += "a";
        }
        //System.out.println(alcode);
        return alcode;
    }

    public String encodeConditions(){
        String condcode = "";
        ArrayList<Integer> arr = new ArrayList<Integer> (conditions.getCheckModel().getCheckedIndices());
        if(arr.contains(0)){
            condcode += "0";
        }
        if(arr.contains(1)){
            condcode += "1";
        }
        if(arr.contains(2)){
            condcode += "2";
        }
        if(arr.contains(3)){
            condcode += "3";
        }
        if(arr.contains(4)){
            condcode += "4";
        }
        if(arr.contains(5)){
            condcode += "5";
        }
        if(arr.contains(6)){
            condcode += "6";
        }
        if(arr.contains(7)){
            condcode += "7";
        }
        if(arr.contains(8)){
            condcode += "8";
        }
        if(arr.contains(9)){
            condcode += "9";
        }
        if(arr.contains(10)){
            condcode += "a";
        }
        if(arr.contains(11)){
            condcode += "b";
        }
        if(arr.contains(12)){
            condcode += "c";
        }
        if(arr.contains(13)){
            condcode += "d";
        }
        if(arr.contains(14)){
            condcode += "e";
        }
        //System.out.println(alcode);
        return condcode;
    }

    public String checkSignature() throws Exception {
       ArrayList<Employee> e = KioskDatabase.getInstance().getEmployees();
       for(Employee emp : e){
           if(signature.getText().equals(emp.getName())){
               return emp.getName();
           }
       }
       throw new Exception();
    }

    public String decodeAllergies(String code){
        String out = "";
        if(code.contains("0")){
            out += "None";
        }
        else{
            if(code.contains("1")){
                out += "Ampicillin, ";
            }
            if(code.contains("2")){
                out += "Aspirin, ";
            }
            if(code.contains("3")){
                out += "Cephalosporins, ";
            }
            if(code.contains("4")){
                out += "Codeine, ";
            }
            if(code.contains("5")){
                out += "Erythromycin, ";
            }
            if(code.contains("6")){
                out += "Morphine, ";
            }
            if(code.contains("7")){
                out += "SAIDS, ";
            }
            if(code.contains("8")){
                out += "Penicillin, ";
            }
            if(code.contains("9")){
                out += "Sulfa, ";
            }
            if(code.contains("a")){
                out += "Tetracycline, ";
            }

        }
        return out;
    }
    public String decodeConditions(String code){
        String out = "";
        if(code.contains("0")){
            out += "None";
        }
        else{
            if(code.contains("1")){
                out += "Arthritis, ";
            }
            if(code.contains("2")){
                out += "Asthma, ";
            }
            if(code.contains("3")){
                out += "COPD, ";
            }
            if(code.contains("4")){
                out += "Depression, ";
            }
            if(code.contains("5")){
                out += "Diabetes, ";
            }
            if(code.contains("6")){
                out += "Glaucoma, ";
            }
            if(code.contains("7")){
                out += "High Cholesterol, ";
            }
            if(code.contains("8")){
                out += "Hypertension, ";
            }
            if(code.contains("9")){
                out += "Kidney Disease, ";
            }
            if(code.contains("a")){
                out += "Liver Disease, ";
            }
            if(code.contains("b")){
                out += "Seasonal Allergies, ";
            }
            if(code.contains("c")){
                out += "Seizures/Epilepsy, ";
            }
            if(code.contains("d")){
                out += "Thyroid, ";
            }
            if(code.contains("e")){
                out += "Ulcers, ";
            }
        }

        return out;
    }

    public String generateRx() throws IOException {
        String birth = "0";
        String currDay = "0";
        String nodeID = "N/A";
        int social = 0;
        if (DOB.getValue() != null) {
            birth = DOB.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        if (date.getValue() != null) {
            currDay = date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        if (!(SSN.getText().equals(""))) {
            social = Integer.parseInt(SSN.getText());
        }

        if (!(locations.getText().equals(""))) {
            nodeID = KioskDatabase.getInstance().makeKioskGraph().getNodeByLongName(locations.getText()).getId();
        }

        String Rx = "";
        Rx +=  "Patient Name: " + name.getText() + "\n";
        Rx +=  "Patient DOB: " + birth + "\n";
        Rx +=  "Patient SSN: " + social + "\n";
        Rx +=  "Medication: " + medication.getText() + "\n";
        Rx +=  "Allergies: " + decodeAllergies(encodeAllergies()) + "\n";
        Rx +=  "Conditions: " + decodeConditions(encodeConditions()) + "\n";
        Rx +=  "Location: " + nodeID + "\n";
        Rx +=  "Time Placed: " + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) + "\n";
        Rx +=  "Date Placed: " + currDay + "\n";
        Rx +=  "Phone Number: " + phone.getText() + "\n";
        try {
            Rx +=  "Doctor E-Signature: " + checkSignature() + "\n";
        } catch (Exception e) {
            goToController("/fxml/FormUI.fxml");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Lookup Error:");
            alert.setContentText("This is not a valid doctor name.");
            alert.showAndWait();
            return "";
        }
        return Rx;

    }

    public void print() throws IOException {
        sendToPrint(generateRx());
        submit();
    }

    public void sendToPrint(String Rx){
        EscPosBuilder escPos = new EscPosBuilder();
        byte[] data = escPos.initialize()
                .font(Font.EMPHASIZED)
                .align(Align.LEFT)
                .text(Rx)
                .feed(1)
                .getBytes();
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService service = null;
        for (PrintService printService : printServices) {
            System.out.println(printService.getName());
            if (printService.getName().equals("POS-58-Series")) {
                service = printService;
                break;
            }
        }
        if (service == null)
            System.err.println("NOT FOUND");
        DocPrintJob job = service.createPrintJob();
        Doc doc = new SimpleDoc(data, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
        try {
            job.print(doc, new HashPrintRequestAttributeSet());
        } catch (PrintException e) {
            e.printStackTrace();
        }
    }

    public void cancel() throws IOException {
        goToController("/fxml/HubUI.fxml");
    }


}
