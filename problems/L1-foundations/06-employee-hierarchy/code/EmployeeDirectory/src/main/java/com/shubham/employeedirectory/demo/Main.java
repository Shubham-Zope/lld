package com.shubham.employeedirectory.demo;

import com.shubham.employeedirectory.exception.CycleException;
import com.shubham.employeedirectory.exception.DuplicateEmployeeException;
import com.shubham.employeedirectory.exception.InvalidOperationException;
import com.shubham.employeedirectory.model.Employee;
import com.shubham.employeedirectory.service.EmployeeDirectoryService;

public final class Main {

  public static void main(String[] args) {
    EmployeeDirectoryService directory = new EmployeeDirectoryService();

    directory.addEmployee("E1", "Avery Chen", null, null, "CEO", "NYC");
    directory.addEmployee("E2", "Baz Iyer", "E1", "Engineering", "VP Engineering", "NYC");
    directory.addEmployee("E3", "Cleo Fox", "E1", "Sales", "VP Sales", "SF");
    directory.addEmployee("E4", "Dana Park", "E2", "Engineering", "Eng Manager", "NYC");
    directory.addEmployee("E5", "Emil Ruiz", "E2", "Engineering", "Eng Manager", "SF");
    directory.addEmployee("E6", "Faye Bloom", "E4", "Engineering", "SDE2", "NYC");
    directory.addEmployee("E7", "Gus Nolan", "E4", "Engineering", "SDE1", "NYC");
    directory.addEmployee("E8", "Hana Seo", "E5", "Engineering", "SDE2", "SF");
    directory.addEmployee("E9", "Ivo Marsh", "E3", "Sales", "Account Exec", "SF");
    directory.addEmployee("E10", "Jia Wren", "E3", null, "Contractor", "SF");

    System.out.println("=== Org chart from the CEO ===");
    System.out.print(directory.printOrgChart("E1"));

    System.out.println("\n=== FR4: direct reports of E2 (VP Engineering) ===");
    directory.getDirectReports("E2").forEach(System.out::println);

    System.out.println("\n=== FR4: full subtree of E2 ===");
    directory.getSubtree("E2").forEach(System.out::println);

    System.out.println("\n=== FR4: chain from E6 up to the CEO ===");
    directory.getChainToCeo("E6").forEach(e -> System.out.print(e.getId() + " -> "));
    System.out.println("(root)");

    System.out.println("\n=== FR5: lowest common manager ===");
    System.out.println(
        "LCM(E6, E7) = "
            + directory.lowestCommonManager("E6", "E7").getId()
            + " (same manager, E4)");
    System.out.println(
        "LCM(E6, E8) = "
            + directory.lowestCommonManager("E6", "E8").getId()
            + " (different managers, same VP org, E2)");
    System.out.println(
        "LCM(E6, E9) = "
            + directory.lowestCommonManager("E6", "E9").getId()
            + " (different orgs, the CEO)");
    System.out.println(
        "LCM(E2, E6) = " + directory.lowestCommonManager("E2", "E6").getId() + " (E2 manages E6)");

    System.out.println("\n=== FR6: headcount by department / location ===");
    System.out.println("By department: " + directory.headcountByDepartment());
    System.out.println("By location:   " + directory.headcountByLocation());
    System.out.println(
        "Engineering org (E2) by department: " + directory.subtreeHeadcountByDepartment("E2"));

    System.out.println("\n=== FR2: move E5's whole team (E5 + E8) under E3 ===");
    directory.moveEmployee("E5", "E3", true);
    System.out.print(directory.printOrgChart("E1"));

    System.out.println(
        "=== FR2: move E9 alone (without subtree) under E2; E9 has no reports here, so nothing to reassign ===");
    directory.moveEmployee("E9", "E2", false);
    System.out.print(directory.printOrgChart("E1"));

    System.out.println("=== FR3: remove E4; E6 and E7 are promoted to report to E2 ===");
    directory.removeEmployee("E4");
    System.out.print(directory.printOrgChart("E1"));

    System.out.println("=== EC1: moving a manager under their own report is rejected ===");
    try {
      directory.moveEmployee("E2", "E6", true);
    } catch (CycleException e) {
      System.out.println("Rejected: " + e.getMessage());
    }

    System.out.println("\n=== EC2: removing the CEO is rejected ===");
    try {
      directory.removeEmployee("E1");
    } catch (InvalidOperationException e) {
      System.out.println("Rejected: " + e.getMessage());
    }

    System.out.println("\n=== EC3: employee with no department (E10, a contractor) ===");
    System.out.println("E10 department = " + directory.getEmployee("E10").getDepartment());
    System.out.println(
        "Headcount by department still reflects only assigned departments: "
            + directory.headcountByDepartment());

    System.out.println("\n=== EC4: adding a duplicate employee id is rejected ===");
    try {
      directory.addEmployee("E6", "Someone Else", "E2", "Engineering", "SDE3", "NYC");
    } catch (DuplicateEmployeeException e) {
      System.out.println("Rejected: " + e.getMessage());
    }
  }
}
