package edu.wpi.cs3733d18.teamR.controllers;

import edu.wpi.cs3733d18.teamR.administration.Request;
import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733d18.teamR.database.KioskDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HubController extends Controller implements Initializable {

    @FXML
    JFXButton create;
    @FXML
    JFXButton edit;
    @FXML
    JFXButton cancel;
    @FXML
    Label details;
    @FXML
    TableColumn name;
    @FXML
    TableColumn time;
    @FXML
    TableColumn med;
    @FXML
    TableColumn sig;
    @FXML
    TableColumn date;
    @FXML
    TableView<Request> t;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        t.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Request r = t.getSelectionModel().getSelectedItem();
                details.setText(
                        "DOB: " + r.getDOB() + "\n" +
                        "Address: " + r.getAddress() + "\n" +
                        "Location: " + r.getLocation() + "\n" +
                        "Allergies: " + decodeAllergies(r.getAllergies()) + "\n" +
                        "Conditions: " + decodeConditions(r.getConditions()) + "\n" +
                        "Phone: " + r.getPhone() + "\n"
                                        );
            }
        });
        ArrayList<Request> prescriptions = KioskDatabase.getInstance().getRequests();
        name.setCellValueFactory(new PropertyValueFactory<Request,String>("name"));
        time.setCellValueFactory(new PropertyValueFactory<Request,String>("time"));
        date.setCellValueFactory(new PropertyValueFactory<Request,String>("date"));
        med.setCellValueFactory(new PropertyValueFactory<Request,String>("medication"));
        sig.setCellValueFactory(new PropertyValueFactory<Request,String>("signature"));
        ObservableList<Request> data = FXCollections.observableArrayList(prescriptions);
        t.setItems(data);

        }

    public void edit() throws IOException{
            goToController("/fxml/EditUI.fxml");

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

    public void cancel(){
        Request cancel = t.getSelectionModel().getSelectedItem();
        if(cancel != null) {
            KioskDatabase.getInstance().removeRequest(cancel.getReqID());
            t.getItems().remove(cancel);
            details.setText("");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Cancel error:");
            alert.setContentText("No Rx selected to cancel.");
           // alert.initOwner(empName.getScene().getWindow());
            alert.showAndWait();
        }
    }

    public void form() throws IOException {
            goToController("/fxml/FormUI.fxml");
    }
}
