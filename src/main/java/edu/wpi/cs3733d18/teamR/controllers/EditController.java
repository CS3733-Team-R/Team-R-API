package edu.wpi.cs3733d18.teamR.controllers;

import edu.wpi.cs3733d18.teamR.administration.Employee;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class EditController extends Controller implements Initializable {

    @FXML
    JFXButton back;
    @FXML
    JFXButton add;
    @FXML
    JFXButton edit;
    @FXML
    JFXButton delete;
    @FXML
    TableColumn name;
    @FXML
    TableColumn empID;
    @FXML
    TableView<Employee> t;
    @FXML
    Label current;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        t.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Employee r = t.getSelectionModel().getSelectedItem();
                current.setText(r.getName());
            } });
        ArrayList<Employee> employees = KioskDatabase.getInstance().getEmployees();
        name.setCellValueFactory(new PropertyValueFactory<Employee,String>("name"));
        empID.setCellValueFactory(new PropertyValueFactory<Employee,String>("empID"));
        ObservableList<Employee> data = FXCollections.observableArrayList(employees);
        t.setItems(data);
    }

    public void back() throws IOException {
        goToController("/fxml/HubUI.fxml");

    }

    public void add() throws IOException {
        int numberOfEmployees = KioskDatabase.getInstance().getEmpCounter();
        numberOfEmployees++;
        KioskDatabase.getInstance().setEmpCounter(numberOfEmployees);
        String emp = ("Dr#" + Integer.toString(numberOfEmployees));
        TextInputDialog dialog = new TextInputDialog("name");
        dialog.setTitle("Add new Employee");
        dialog.setHeaderText("Add new Employee");
        dialog.setContentText("Please enter Employee name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(output ->
                KioskDatabase.getInstance().addNewEmployee(output, emp)
        );
        goToController("/fxml/EditUI.fxml");

    }

    public void edit() throws IOException {
        Employee r = t.getSelectionModel().getSelectedItem();
        if(r != null) {
            String emp = r.getEmpID();
            KioskDatabase.getInstance().removeEmployee(r);
            TextInputDialog dialog = new TextInputDialog("edit");
            dialog.setTitle("Edit current Employee");
            dialog.setHeaderText("Edit Employee");
            dialog.setContentText("Please enter new Employee name:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(output ->
                    KioskDatabase.getInstance().addNewEmployee(output, emp)
            );
            result.ifPresent(output ->
                    current.setText(output)
            );
            goToController("/fxml/EditUI.fxml");


        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Edit error:");
            alert.setContentText("No Doctor selected to edit.");
            // alert.initOwner(empName.getScene().getWindow());
            alert.showAndWait();
        }

    }

    public void delete(){
        Employee cancel = t.getSelectionModel().getSelectedItem();
        if(cancel != null) {
            KioskDatabase.getInstance().removeEmployee(cancel);
            t.getItems().remove(cancel);
            current.setText("");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Deletion error:");
            alert.setContentText("No Doctor selected to remove.");
            // alert.initOwner(empName.getScene().getWindow());
            alert.showAndWait();
        }
    }

    }
