package org.condast.commons.jpa.persistence;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PersistencePropertiesHandler {

	private static final String S_ERR_XML_MISSING =
		    "Persistence descriptor not found";

		private static final String S_ERR_UNIT_MISSING =
		    "Persistence unit not found: ";

		private static final String S_INFO_PROPERTIES =
		    "Persistence properties loaded for: ";

	private static Logger logger = Logger.getLogger(PersistencePropertiesHandler.class.getName());

	public static Properties readPersistenceProperties(
	        URL descriptor,
	        String unitName) throws Exception {

	    if (descriptor == null) {
	        throw new IllegalStateException(S_ERR_XML_MISSING);
	    }

	    DocumentBuilderFactory factory =
	        DocumentBuilderFactory.newInstance();

	    factory.setNamespaceAware(true);
	    factory.setFeature(
	        XMLConstants.FEATURE_SECURE_PROCESSING, true);
	    factory.setFeature(
	        "http://apache.org/xml/features/disallow-doctype-decl",
	        true);
	    factory.setXIncludeAware(false);
	    factory.setExpandEntityReferences(false);

	    try (InputStream input = descriptor.openStream()) {

	        Document document =
	            factory.newDocumentBuilder().parse(input);

	        NodeList units = document.getElementsByTagNameNS(
	            "*", "persistence-unit");

	        for (int i = 0; i < units.getLength(); i++) {

	            Element unit = (Element) units.item(i);

	            if (!unitName.equals(unit.getAttribute("name"))) {
	                continue;
	            }

	            Properties properties = new Properties();

	            NodeList entries =
	                unit.getElementsByTagNameNS("*", "property");

	            for (int j = 0; j < entries.getLength(); j++) {

	                Element property = (Element) entries.item(j);

	                properties.setProperty(
	                    property.getAttribute("name"),
	                    property.getAttribute("value")
	                );
	            }

	            logger.info(S_INFO_PROPERTIES + unitName);
	            return properties;
	        }
	    }

	    throw new IllegalStateException(
	        S_ERR_UNIT_MISSING + unitName);
	}

}
