package testprojectcore.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class XmlUtil {


    public Map<String, String> getParamsAndTheirValuesFromXmlDocument(String xmlDocumentUri) throws Exception {
        String nodeName;
        Map<String, String> paramsMap = new HashMap<>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(xmlDocumentUri);
        String firstTag = document.getFirstChild().getNodeName();
        Element node = (Element) xpath.evaluate("//" + firstTag,
                document, XPathConstants.NODE);

        NodeList nodeList = node.getChildNodes();

        for (int i = 1; i < nodeList.getLength(); i++) {
            nodeName = nodeList.item(i).getNodeName();
            if (!nodeName.contains("#text")) {
                String textContent = nodeList.item(i).getTextContent();
                paramsMap.put(nodeName, textContent);
            }
        }

        System.out.println(paramsMap.toString());

        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(new DOMSource(document), new StreamResult(new File("target/NewFile.xml")));

        return paramsMap;
    }


    public void printElementsAtLineNumber(int lineNumber, String resourceDir) throws Exception {
        URL resource = XmlUtil.class.getClassLoader().getResource(resourceDir);    //resource dir starts after src/test/resources..
        FileReader reader = new FileReader(new File(resource.toURI()));
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader xmlStreamReader = factory.createXMLStreamReader(reader);
        XMLStreamReader xmlFilteredStreamReader = factory.createFilteredReader(xmlStreamReader, filter);
        while (xmlFilteredStreamReader.hasNext()) {
            if (xmlFilteredStreamReader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                if (lineNumber == xmlFilteredStreamReader.getLocation().getLineNumber()) {
                    System.out.println("Character offset: " + xmlFilteredStreamReader.getLocation().getCharacterOffset());
                    System.out.println("Column number: " + xmlFilteredStreamReader.getLocation().getColumnNumber());
                    System.out.println("Element name: " + xmlFilteredStreamReader.getName().getLocalPart());
                    System.out.println("Line number: " + xmlStreamReader.getLocation().getLineNumber());
                    System.out.println("Element text: " + xmlStreamReader.getElementText());
                }
            }
            xmlFilteredStreamReader.next();
        }
    }

    private static QName[] exclude = new QName[]{
            new QName("a"), new QName("b")};

    private static StreamFilter filter = new StreamFilter() {
        int depth = -1;
        int match = -1;
        boolean process = true;
        int currentPos = -1;

        public boolean accept(XMLStreamReader reader) {
            Location loc = reader.getLocation();
            int pos = loc.getCharacterOffset();
            if (pos != currentPos) {
                currentPos = pos;
                switch (reader.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        if (++depth < exclude.length && match == depth - 1) {
                            if (reader.getName().equals(exclude[depth]))
                                match = depth;
                        }
                        process = match < exclude.length - 1;
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        process = match < exclude.length - 1;
                        if (--depth < match)
                            match = depth;
                        break;
                }
            }
            return process;
        }
    };
}
