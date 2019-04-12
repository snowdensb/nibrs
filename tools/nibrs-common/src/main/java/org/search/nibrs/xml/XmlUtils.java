/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.xml.NibrsNamespaceContext.Namespace;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Utilities class for handling common XML tasks.
 *
 */
public class XmlUtils {
	
	private static final NamespaceContext NAMESPACE_CONTEXT = new NibrsNamespaceContext();
	private static final Log log = LogFactory.getLog(XmlUtils.class);;

	/**
	 * Create a new element as a child of the specified parent and append it.  Use the specified namespace context to set the element's namespace prefix
	 * explicitly.
	 * @param parent the new element's parent
	 * @param namespace the namespace URI of the new element
	 * @param name the name of the new element
	 * @return the new element
	 */
	public static final Element appendChildElement(Node parent, Namespace namespace, String name) {
		
		Document doc = parent instanceof Document ? (Document) parent : parent.getOwnerDocument();
		Element ret = doc.createElementNS(namespace.uri, name);
		parent.appendChild(ret);
		ret.setPrefix(namespace.prefix);
		return ret;
	}
	
	/**
	 * Add an attribute to the specified element
	 * @param e the element
	 * @param namespace the namespace of the attribute
	 * @param attributeName the name of the attribute
	 * @param attributeValue the value of the attribute
	 * @return the element
	 */
	public static final Element addAttribute(Element e, Namespace namespace, String attributeName, String attributeValue) {
		e.setAttributeNS(namespace.uri, namespace.prefix + ":" + attributeName, attributeValue);
		return e;
	}

	/**
	 * Create a new DOM document that is namespace-aware.
	 * @return the new document
	 * @throws ParserConfigurationException
	 */
	public static final Document createNewDocument() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		return factory.newDocumentBuilder().newDocument();
	}
	
	/**
     * Print the specified XML DOM node to the specified output stream
     * 
     * @param n the node to print
     * @param os the output stream to print to
     * @throws Exception
     */
    public static void printNode(Node n, OutputStream os) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = setupTransformerOptions(tf.newTransformer());
        t.transform(new DOMSource(n), new StreamResult(os));
    }
    
    /**
     * Print the specified XML DOM node to System.out
     * 
     * @param n
     *            the node to print
     * @throws Exception
     */
    public static final void printNode(Node n) throws Exception {
        printNode(n, System.out);
    }    
    
    /**
     * Read the contents of the specified file into a DOM document
     * @param f the input XML file
     * @return the document
     * @throws Exception
     */
    public static final Document toDocument(File f) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        return dbf.newDocumentBuilder().parse(f);
    }    
    
    /**
     * Read the contents of the specified String into a DOM document
     * @param xml the String containing the XML
     * @return the document
     * @throws Exception
     */
    public static final Document toDocument(String xml) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        return dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }	
    
    /**
     * Write the specified DOM node into a String.
     * @param node the node to write
     * @return the xml string
     * @throws Exception
     */
    public static final String nodeToString(Node node) throws Exception {
    	StringWriter writer = new StringWriter();
    	Transformer transformer = TransformerFactory.newInstance().newTransformer();
    	setupTransformerOptions(transformer);
    	transformer.transform(new DOMSource(node), new StreamResult(writer));
    	return writer.toString();
    }
    
    /**
     * Search the context node for a node that matches the specified xpath
     * 
     * @param context the node that's the context for the xpath
     * @param xPath the xpath query
     * @return the matching object, or null if no match
     * @throws Exception
     */
    public static final Node xPathNodeSearch(Node context, String xPath){
        if (xPath == null)
        {
            return null;
        }
        
        try{
	        XPath xpath = XPathFactory.newInstance().newXPath();
	        xpath.setNamespaceContext(NAMESPACE_CONTEXT);
	        XPathExpression expression = xpath.compile(xPath);
	        return (Node) expression.evaluate(context, XPathConstants.NODE);
        }
        catch (Exception e){
        	log.warn(e);
        	return null; 
        }
    }

    /**
     * Search the context node for a String that matches the specified xpath
     * @param context the node that's the context for the xpath
     * @param xPath the xpath query
     * @return the matching string, or null if no match
     * @throws Exception
     */
    public static final String xPathStringSearch(Node context, String xPath){
    	
        if (xPath == null) {
            return null;
        }
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(NAMESPACE_CONTEXT);
        XPathExpression expression;
        String value = null;
		try {
			expression = xpath.compile(xPath);
			value = (String) expression.evaluate(context, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			log.warn(e);
		}
        return org.apache.commons.lang3.StringUtils.trimToNull(value);
        
    }    
    
    /**
     * Search the context node for a node set that matches the specified xpath
     * @param context the node that's the context for the xpath
     * @param xPath the xpath query
     * @return the matching node, or null if no match
     * @throws Exception
     */
    public static final NodeList xPathNodeListSearch(Node context, String xPath){
    	
        if (xPath == null) {
            return null;
        }
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(NAMESPACE_CONTEXT);
        XPathExpression expression;
        NodeList result = null;
		try {
			expression = xpath.compile(xPath);
			result = (NodeList) expression.evaluate(context, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			log.warn(e);
		}
        		
        return result;

    }
    
    private static final Transformer setupTransformerOptions(Transformer t) {
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		return t;
    }

    /**
     * Returns true if the node specified by the xpath exists in the context, and false otherwise
     * 
     * @param context
     *            the context node from which to apply the xpath
     * @param xPath
     *            the query
     * @return true if the specified node exists, false otherwise
     * @throws Exception
     */
    public static final boolean nodeExists(Node context, String xPath) throws Exception {
        return xPathNodeSearch(context, xPath) != null;
    }

    /**
     * Create a new element with the specified namespace and name, append it to the specified parent, and return it
     * 
     * @param parent
     *            the parent to contain the new element
     * @param ns
     *            the namespace URI of the new element
     * @param elementName
     *            the name of the new element
     * @return the new element
     */
    public static final Element appendElement(Element parent, String ns, String elementName) {
        Document doc = parent.getOwnerDocument();
        Element ret = doc.createElementNS(ns, elementName);
        parent.appendChild(ret);
        ret.setPrefix(NAMESPACE_CONTEXT.getPrefix(ns));
        return ret;
    }

	public static void appendElementAndValue(Element parentElement, Namespace elementNamespace, String elementName, String value) {
		if ( StringUtils.isNotBlank(value)) {
			XmlUtils.appendChildElement(parentElement, elementNamespace, elementName).setTextContent(value);
		}
	}

    /**
     * Add an attribute to the specified element
     * @param parent the element to which we add the attribute
     * @param ns the namespace of the attribute
     * @param attributeName the name of the attribute
     * @param value the value of the attribute
     * @return the attribute
     */
    public static final Attr addAttribute(Element parent, String ns, String attributeName, String value) {
        Document doc = parent.getOwnerDocument();
        Attr ret = doc.createAttributeNS(ns, attributeName);
        ret.setTextContent(value);
        ret.setPrefix(NAMESPACE_CONTEXT.getPrefix(ns));
        parent.setAttributeNode(ret);
        return ret;
    }
    
    /**
     * if textValue is not blank, create a new element under the parent element with the namespace and 
     * elementName, then set the textContent of the element to the textValue. 
     * 
     * if textValue is blank,  do nothing. 
     * 
     * @param parent
     * @param namespace
     * @param elementName
     * @param textValue
     */
    public static final void appendTextElement(Element parent, String namespace,
			String elementName, String textValue) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(textValue)){
			Element personEthnicityText = 
					XmlUtils.appendElement(parent, namespace, elementName);
			personEthnicityText.setTextContent(textValue);
		}
	}

   /**
     * Create a new element with the specified namespace and name, insert it under the specified parent but before the specified sibling, and return it
     * 
     * @param parent
     *            the parent
     * @param sibling
     *            the parent's current child, in front of which the new element is to be inserted
     * @param ns
     *            the namespace URI of the new element
     * @param elementName
     *            the name of the new element
     * @return the new element
     */
    public static final Element insertElementBefore(Element parent, Element sibling, String ns, String elementName) {
        Document doc = parent.getOwnerDocument();
        Element ret = doc.createElementNS(ns, elementName);
        ret.setPrefix(NAMESPACE_CONTEXT.getPrefix(ns));
        parent.insertBefore(ret, sibling);
        return ret;
    }

    /**
     * Read the contents of the specified file into a DOM document
     * @param f the input XML file
     * @return the document
     * @throws Exception
     */
    public static final Document parseFileToDocument(File f) throws Exception {
        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
        Document document = docBuilder.parse(f);
        return document;
    }
    
    public static Source getDomSourceIgnoringDtd(String xmlContents) throws ParserConfigurationException, SAXException, IOException {
		
		InputStream inputStream = new ByteArrayInputStream(xmlContents.getBytes());
		
		Source source = getDomSourceIgnoringDtd(inputStream);

		return source;
	}	
	
	public static Source getDomSourceIgnoringDtd(File file) throws FileNotFoundException, SAXException, 
		IOException, ParserConfigurationException{		
		
		InputStream inputStream = new FileInputStream(file);
		
		Source source = getDomSourceIgnoringDtd(inputStream);
		
		return source;
	}
		
	public static Source getDomSourceIgnoringDtd(InputStream inputStream) throws ParserConfigurationException, 
		SAXException, IOException {		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		// stop the loading of DTD files
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		factory.setFeature("http://xml.org/sax/features/validation", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		
		DocumentBuilder docbuilder = factory.newDocumentBuilder();
						
		Document doc = docbuilder.parse(inputStream);
		
		Source domSource = new DOMSource(doc.getDocumentElement());
		
		return domSource;
	}
    
    /**
     * Original intent was to represent root node as a string without 
     * any xml comments at the top 
     */
    public static String getRootNodeAsString(String fileClasspath) throws Exception{
    	
        File file = new File(fileClasspath);    	
    	Document doc = XmlUtils.parseFileToDocument(file);    	
    	Element rootElement = doc.getDocumentElement();    	
    	String sRoot = XmlUtils.getStringFromNode(rootElement);
    	
    	return sRoot;
    }
    
    public static String getStringFromNode(Node node) throws Exception{
    	
    	StringWriter writer = new StringWriter();
    	Transformer transformer = TransformerFactory.newInstance().newTransformer();
    	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    	transformer.transform(new DOMSource(node), new StreamResult(writer));
    	String xml = writer.toString();
    	
    	return xml;
    }
}

