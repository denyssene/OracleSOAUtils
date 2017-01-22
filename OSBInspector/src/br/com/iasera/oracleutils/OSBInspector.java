package br.com.iasera.oracleutils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.net.MalformedURLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import javax.naming.Context;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.bea.wli.config.Ref;
import com.bea.wli.config.component.NotFoundException;
import com.bea.wli.config.resource.DependencyQuery;
import com.bea.wli.sb.management.configuration.ALSBConfigurationMBean;
import com.bea.wli.sb.management.query.ProxyServiceQuery;
import com.bea.wli.sb.transports.URITableElementType;
import com.bea.wli.sb.transports.URITableType;
import com.bea.wli.sb.util.EnvValueTypes;

import weblogic.management.jmx.MBeanServerInvocationHandler;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;

public class OSBInspector {

	private MBeanServerConnection mbeanServerConnection;
	private ALSBConfigurationMBean alsbCore;
	
	public OSBInspector(String host, int port, String username, String passwd) throws IOException, MalformedURLException {
		initRemoteConnection(host, port, username, passwd);
	}

	
	private void initRemoteConnection(String hostname, int port, String username, String password) throws IOException, MalformedURLException {
		JMXServiceURL serviceURL = new JMXServiceURL(
				"t3", 
				hostname, 
				port,
				"/jndi/" + DomainRuntimeServiceMBean.MBEANSERVER_JNDI_NAME);
		
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.SECURITY_PRINCIPAL, username);
		h.put(Context.SECURITY_CREDENTIALS, password);
		h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
		
		JMXConnector jmxConnection = JMXConnectorFactory.connect(serviceURL, h);
		this.mbeanServerConnection = jmxConnection.getMBeanServerConnection();
	}
	
	private ProxyServiceResource createProxyServiceResource(Ref ref) throws NotFoundException {
		ProxyServiceResource ps = new ProxyServiceResource(ref);
		String uri = (String)alsbCore.getEnvValue(ref,EnvValueTypes.SERVICE_URI, null);
		if (uri == null)
			uri = "Local";
		ps.setEndpointUri( uri );
		return ps;
	}

	private BusinessServiceResource createBusinessServiceResource(Ref ref) throws NotFoundException {
		BusinessServiceResource bs = new BusinessServiceResource(ref);
		URITableType uriTable = (URITableType)alsbCore.getEnvValue(ref, EnvValueTypes.SERVICE_URI_TABLE, null);
		if (uriTable!=null) {
			for(URITableElementType uriElement : uriTable.getTableElementArray()) {
				bs.addEndpoint(uriElement.getURI());
			}
		}
		return bs;
	}
	
	
	public ArrayList<ProxyServiceResource> readProxyServicesAndReferences() throws Exception {
		
		ArrayList<ProxyServiceResource> proxyReferences = new ArrayList<ProxyServiceResource>();
				
		DomainRuntimeServiceMBean domainService = (DomainRuntimeServiceMBean) MBeanServerInvocationHandler.
                newProxyInstance(this.mbeanServerConnection, new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME));

		alsbCore = (ALSBConfigurationMBean) domainService
				.findService( ALSBConfigurationMBean.NAME, ALSBConfigurationMBean.TYPE, null);
		
		ProxyServiceQuery psQuery = new ProxyServiceQuery();
		
		for(Ref ref : alsbCore.getRefs(psQuery) ) {
			
			ProxyServiceResource ps = createProxyServiceResource(ref);
			
			DependencyQuery depQuery = new DependencyQuery(Collections.singleton(ref),false);
			for (Ref depref: alsbCore.getRefs(depQuery)) {
				
				
				if ( depref.getTypeId().equals("ProxyService") ) {
					ps.addProxyReference( createProxyServiceResource(depref) );
				} else if ( depref.getTypeId().equals("BusinessService") ) {
					ps.addBusinessReference(createBusinessServiceResource(depref));
				}
			}
			proxyReferences.add(ps);
		}
		return proxyReferences;
	}

	
	public static void main(String[] args) {
		
		Options options = new Options();
		
		Option option = new Option("h","host",true,"Server Host");
		option.setRequired(true);
		options.addOption(option);

		option = new Option("p","port",true,"Server Port");
		option.setRequired(true);
		options.addOption(option);

		option = new Option("u","user",true,"Username");
		option.setRequired(true);
		options.addOption(option);

		option = new Option("P","passwd",true,"Password");
		option.setRequired(true);
		options.addOption(option);

		option = new Option("help","Help");
		option.setRequired(false);
		options.addOption(option);
		
		option = new Option("o","output",true,"Output File");
		option.setRequired(true);
		options.addOption(option);
		
		CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("OSBInspector", options);
            System.exit(1);
            return;
        }

        if (cmd.hasOption("help")) {
            formatter.printHelp("OSBInspector", options);
            System.exit(1);
            return;
        }
        
        String host = cmd.getOptionValue("host");
        String user = cmd.getOptionValue("user");
        String passwd = cmd.getOptionValue("passwd");
        FileWriter xmlFile = null;
        
        try {
        	xmlFile = new FileWriter(new File(cmd.getOptionValue("output")));
        } catch (IOException e) {
        	System.out.println(e.getMessage());
        	System.exit(-1);
        }
        
        int port = 0;
        try {
        	port = Integer.parseInt(cmd.getOptionValue("port"));
        		
        } catch (NumberFormatException e) {
            formatter.printHelp("OSBInspector", options);
            System.exit(-1);       	
        }

        RootResources root = new RootResources();       
        try {
	        OSBInspector osbInspector = new OSBInspector(host, port, user, passwd);
	        root.setProxyReferences(osbInspector.readProxyServicesAndReferences());
        } catch (Exception e) {
        	e.printStackTrace();
        }

        try {
        	xmlFile.write(XmlUtil.convertToXml(root, RootResources.class));
        } catch (IOException e) {
        	System.out.println(e.getMessage());
        } finally {
			try {
				xmlFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
        
	}
}
