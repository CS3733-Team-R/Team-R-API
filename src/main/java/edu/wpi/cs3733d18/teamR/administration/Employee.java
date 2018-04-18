package edu.wpi.cs3733d18.teamR.administration;

public class Employee {
    String name;
    String empID;

    public Employee(String name, String empID) {
        this.name = name;
        this.empID = empID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }
}


