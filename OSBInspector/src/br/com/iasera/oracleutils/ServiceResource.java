package br.com.iasera.oracleutils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.bea.wli.config.Ref;

@XmlType(propOrder = {"name", "project", "path", "status"})
@XmlRootElement
public abstract class ServiceResource {

	private String name;
	private String project;
	private String path;
	@XmlElement(name="enabled")
	private boolean status;
		
	public ServiceResource() {
	}
	
	public ServiceResource(Ref ref) {
		this.name=ref.getLocalName();
		this.project=ref.getProjectName();
		this.path=ref.getFullName();		
	}
	
	public abstract String getType();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getProject() {
		return project;
	}
	
	public void setProject(String project) {
		this.project = project;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public boolean isEnabled() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
