package com.shubham.employeedirectory;

import com.shubham.employeedirectory.exception.CycleException;
import com.shubham.employeedirectory.exception.DuplicateEmployeeException;
import com.shubham.employeedirectory.exception.EmployeeNotFoundException;
import com.shubham.employeedirectory.exception.InvalidOperationException;
import com.shubham.employeedirectory.model.Employee;
import com.shubham.employeedirectory.service.EmployeeDirectoryService;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDirectoryServiceTest {

    // E1 (CEO)
    //  +- E2 (Eng)
    //  |   +- E4 (Eng)
    //  |       +- E6 (Eng)
    //  |       +- E7 (Eng)
    //  +- E3 (Sales)
    //      +- E5 (no department)
    private EmployeeDirectoryService directory;

    @BeforeEach
    void setUp() {
        directory = new EmployeeDirectoryService();
        directory.addEmployee("E1", "CEO", null, null, "Chief Executive", "NYC");
        directory.addEmployee("E2", "VP Eng", "E1", "Engineering", "VP", "NYC");
        directory.addEmployee("E3", "VP Sales", "E1", "Sales", "VP", "SF");
        directory.addEmployee("E4", "Eng Mgr", "E2", "Engineering", "Manager", "NYC");
        directory.addEmployee("E5", "Contractor", "E3", null, "Contractor", "SF");
        directory.addEmployee("E6", "SDE A", "E4", "Engineering", "SDE", "NYC");
        directory.addEmployee("E7", "SDE B", "E4", "Engineering", "SDE", "SF");
    }

    @Test // FR1
    void addEmployeeRegistersThemUnderTheirManager() {
        directory.addEmployee("E8", "New Hire", "E6", "Engineering", "Intern", "NYC");

        assertEquals("E6", directory.getEmployee("E8").getManager().getId());
        assertEquals(List.of("E8"), idsOf(directory.getDirectReports("E6")));
    }

    @Test // FR1: a second employee without a manager would mean a second CEO
    void addEmployeeWithoutManagerIsRejectedOnceACeoExists() {
        assertThrows(InvalidOperationException.class,
                () -> directory.addEmployee("E8", "Rival CEO", null, null, "CEO", "NYC"));
    }

    @Test // FR1: a manager that does not exist cannot be assigned
    void addEmployeeWithUnknownManagerIsRejected() {
        assertThrows(EmployeeNotFoundException.class,
                () -> directory.addEmployee("E8", "Orphan", "NOPE", "Engineering", "SDE", "NYC"));
    }

    @Test // FR2: moving with the subtree keeps E6/E7 under E4
    void moveEmployeeWithSubtreeTakesReportsAlong() {
        directory.moveEmployee("E4", "E3", true);

        assertEquals("E3", directory.getEmployee("E4").getManager().getId());
        assertEquals(List.of("E6", "E7"), idsOf(directory.getDirectReports("E4")));
        assertTrue(directory.getDirectReports("E2").isEmpty());
    }

    @Test // FR2: moving without the subtree promotes E6/E7 to E4's old manager
    void moveEmployeeWithoutSubtreeReassignsItsReportsToTheOldManager() {
        directory.moveEmployee("E4", "E3", false);

        assertEquals("E3", directory.getEmployee("E4").getManager().getId());
        assertTrue(directory.getDirectReports("E4").isEmpty());
        assertEquals(List.of("E6", "E7"), idsOf(directory.getDirectReports("E2")));
    }

    @Test // EC1
    void moveEmployeeUnderTheirOwnReportIsRejectedAsACycle() {
        assertThrows(CycleException.class, () -> directory.moveEmployee("E2", "E6", true));
    }

    @Test // EC1: moving an employee under itself is also a cycle
    void moveEmployeeUnderItselfIsRejected() {
        assertThrows(CycleException.class, () -> directory.moveEmployee("E4", "E4", true));
    }

    @Test // EC2 (via move): the CEO has no manager to move under
    void moveTheCeoIsRejected() {
        assertThrows(InvalidOperationException.class, () -> directory.moveEmployee("E1", "E3", true));
    }

    @Test // FR3
    void removeEmployeeReassignsDirectReportsToTheRemovedManagersManager() {
        directory.removeEmployee("E4");

        assertThrows(EmployeeNotFoundException.class, () -> directory.getEmployee("E4"));
        assertEquals(List.of("E6", "E7"), idsOf(directory.getDirectReports("E2")));
        assertEquals("E2", directory.getEmployee("E6").getManager().getId());
    }

    @Test // EC2
    void removeCeoIsRejected() {
        assertThrows(InvalidOperationException.class, () -> directory.removeEmployee("E1"));
    }

    @Test // FR4
    void getDirectReportsReturnsOnlyImmediateReports() {
        assertEquals(List.of("E4"), idsOf(directory.getDirectReports("E2")));
    }

    @Test // FR4
    void getSubtreeReturnsTheEmployeeAndEveryDescendant() {
        assertEquals(Set.of("E2", "E4", "E6", "E7"), new HashSet<>(idsOf(directory.getSubtree("E2"))));
    }

    @Test // FR4
    void getChainToCeoWalksUpToTheRoot() {
        assertEquals(List.of("E6", "E4", "E2", "E1"), idsOf(directory.getChainToCeo("E6")));
    }

    @Test // FR5
    void lowestCommonManagerOfSiblingsIsTheirDirectManager() {
        assertEquals("E4", directory.lowestCommonManager("E6", "E7").getId());
    }

    @Test // FR5
    void lowestCommonManagerOfDifferentOrgsIsTheCeo() {
        assertEquals("E1", directory.lowestCommonManager("E6", "E5").getId());
    }

    @Test // FR5: a manager is its own report's lowest common manager
    void lowestCommonManagerWhenOneManagesTheOther() {
        assertEquals("E2", directory.lowestCommonManager("E2", "E6").getId());
    }

    @Test // FR6
    void headcountByDepartmentCountsOnlyEmployeesWithThatDepartment() {
        Map<String, Integer> byDepartment = directory.headcountByDepartment();

        assertEquals(4, byDepartment.get("Engineering"));
        assertEquals(1, byDepartment.get("Sales"));
    }

    @Test // FR6
    void headcountByLocationCountsEveryEmployeeAtThatLocation() {
        assertEquals(4, directory.headcountByLocation().get("NYC"));
        assertEquals(3, directory.headcountByLocation().get("SF"));
    }

    @Test // FR6: headcount reflects removals immediately (NFR: queries reflect writes immediately)
    void headcountIsUpdatedAfterRemoval() {
        directory.removeEmployee("E6");

        assertEquals(3, directory.headcountByDepartment().get("Engineering"));
        assertEquals(3, directory.headcountByLocation().get("NYC"));
    }

    @Test // EC3
    void employeeWithNoDepartmentIsExcludedFromDepartmentHeadcountButStillCountedByLocation() {
        assertNull(directory.getEmployee("E5").getDepartment());
        // E1 and E5 have no department; everyone else (5 employees) does.
        int totalWithADepartment = directory.headcountByDepartment().values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(5, totalWithADepartment);
        assertEquals(3, directory.headcountByLocation().get("SF"));
    }

    @Test // EC4
    void addingADuplicateEmployeeIdIsRejected() {
        assertThrows(DuplicateEmployeeException.class,
                () -> directory.addEmployee("E6", "Someone Else", "E2", "Engineering", "SDE", "NYC"));
    }

    private static List<String> idsOf(List<Employee> employees) {
        return employees.stream().map(Employee::getId).toList();
    }
}
