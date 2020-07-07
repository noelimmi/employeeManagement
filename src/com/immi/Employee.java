package com.immi;

public class Employee {
	private String name;
	private String email;
	private String reportTo;
	
	public Employee(String name, String email, String reportTo) {
		super();
		this.name = name;
		this.email = email;
		this.reportTo = reportTo;
	}
	
	public String getReportTo() {
		return reportTo;
	}

	public void setReportTo(String reportTo) {
		this.reportTo = reportTo;
	}
	
	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Employee [name=" + name + ", email=" + email + ", reportTo=" + reportTo + "]";
	}

}
