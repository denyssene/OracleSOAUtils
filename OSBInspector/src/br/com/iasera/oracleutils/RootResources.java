package br.com.iasera.oracleutils;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso(ProxyServiceResource.class)
@XmlRootElement(name="RootResource")
public class RootResources {

	@XmlAnyElement
	private List<ProxyServiceResource> proxyReferences;
	
	public RootResources() {
		proxyReferences = new ArrayList<ProxyServiceResource>();
	}
	
	public void addProxyReference(ProxyServiceResource ps) {
		this.proxyReferences.add(ps);
	}
	
	public void setProxyReferences(List<ProxyServiceResource> proxyReferences) {
		this.proxyReferences = proxyReferences;
	}
	
}
