package br.com.iasera.oracleutils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class XmlUtil {
	public static String convertToXml(Object source, Class<?>... type) {
		String result;
		StringWriter sw = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance(type);
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(source, sw);
			result = sw.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

		return result;
	}
}