package com.immi;

import java.io.FileNotFoundException;
import java.io.FileReader;
//import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.opencsv.CSVReader;
//import com.opencsv.exceptions.CsvValidationException;

public class BackgroundController {
	private String fileDirectory;
	private EmployeeDAO employeeDAO;
	private Graph graph;
	
	public BackgroundController(String fileDirectory) {
		super();
		this.fileDirectory = fileDirectory;
		this.employeeDAO = new EmployeeDAO();
	}
	
	public void initAddEmployees() throws FileNotFoundException {
		FileReader filereader = new FileReader(fileDirectory); 
		CSVReader csvReader = new CSVReader(filereader); 
        String[] nextRecord; 
        Map<String,Employee> employeesMap = new HashMap<>();
        Map<String,Boolean> reportingToMap =  new HashMap<>();
        try {
			while ((nextRecord = csvReader.readNext()) != null) { 
				if(nextRecord.length >= 3) {
					employeesMap.putIfAbsent(nextRecord[1],new Employee(nextRecord[0], nextRecord[1],nextRecord[2]));
					reportingToMap.put(nextRecord[2], false);
				}
			}
			csvReader.close();
			setIsValidReportingTo(reportingToMap,employeesMap);
			Set<Boolean> isValid = new HashSet<>(reportingToMap.values());
			
			if(isValid.size() == 1 && isValid.iterator().next() != true)
				throw new Exception("Invalid Reporting To Exist In CSV");
			
			Set<String> allEmployees = new HashSet<>();
			allEmployees.addAll(employeesMap.keySet());
			allEmployees.addAll(reportingToMap.keySet());
			graph = getGraphConstructed(allEmployees,employeesMap);
			//graph.print();
			if (graph.hasCycle()) {
				throw new Exception("Cycle exist in CSV");
			}
			employeeDAO.batchInsert(employeesMap.values());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void setIsValidReportingTo(Map<String,Boolean> reportingMap, Map<String,Employee> employeesMap) {
		for(String reportingToEmail: reportingMap.keySet()) {
			if (employeesMap.get(reportingToEmail) != null) 
				reportingMap.put(reportingToEmail,true);
			else 
				reportingMap.put(reportingToEmail,employeeDAO.checkIfEmployeeExist(reportingToEmail));
		}
	}

	private Graph getGraphConstructed(Set<String> allEmployees, Map<String,Employee> employeesMap) {
		Graph newGraph = new Graph();
		for (String employee : allEmployees) {
			newGraph.addNode(employee);
		}
		for (Employee employee : employeesMap.values()) {
			newGraph.addEdge(employee.getEmail(),employee.getReportTo());
		}
		return newGraph;
	}
}
