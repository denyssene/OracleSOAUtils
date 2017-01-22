package br.com.iasera.oracleutils;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bea.wli.config.Ref;

@XmlRootElement(name="BusinessServiceResource")
public class BusinessServiceResource extends ServiceResource {

	@XmlElementWrapper(name="EndpointsTable")
	@XmlElement(name="Endpoint")
	private List<String> endpointUriTable;
	
	public BusinessServiceResource() {
	}

	public BusinessServiceResource(Ref ref) {
		super(ref);
		endpointUriTable = new ArrayList<String>();
	}
	
	@Override
	@XmlElement(name="type")
	public String getType() {
		return "BusinessService";
	}
	
	public void addEndpoint(String endpoint) {
		this.endpointUriTable.add(endpoint);
	}
	
}
