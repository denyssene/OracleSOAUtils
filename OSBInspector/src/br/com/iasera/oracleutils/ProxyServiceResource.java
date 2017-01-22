package br.com.iasera.oracleutils;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.bea.wli.config.Ref;

@XmlSeeAlso(BusinessServiceResource.class)
@XmlRootElement(name="ProxyServiceResource")
@XmlType(propOrder = {"type", "endpointUri", "proxyReferences", "businessReferences"})
public class ProxyServiceResource extends ServiceResource {

	@XmlElementWrapper(name="ProxyReferences")
	@XmlElement(name="ProxyReference", required=false)
	private List<ProxyServiceResource> proxyReferences;
	
	@XmlElementWrapper(name="BusinessReferences")
	@XmlElement(name="BusinessReference", required=false)
	private List<BusinessServiceResource> businessReferences;

	private String endpointUri;
	
	public ProxyServiceResource() {
	}
	
	public ProxyServiceResource(Ref ref) {
		super(ref);
		this.proxyReferences=new ArrayList<ProxyServiceResource>();
		this.businessReferences=new ArrayList<BusinessServiceResource>();
	}
	
	public void addProxyReference(ProxyServiceResource ps) {
		this.proxyReferences.add(ps);
	}

	public void addBusinessReference(BusinessServiceResource bs) {
		this.businessReferences.add(bs);
	}

	@Override
	@XmlElement(name="type")
	public String getType() {
		return "ProxyService";
	}

	public String getEndpointUri() {
		return endpointUri;
	}

	public void setEndpointUri(String endpointUri) {
		this.endpointUri = endpointUri;
	}

	
}
