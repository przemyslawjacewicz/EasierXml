package com.github.easierxml;

import javaslang.control.Try;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AtXPath {
    private Document document;
    private String xPath;

    protected AtXPath(Document document, String xPath) {
        this.document = document;
        this.xPath = xPath;
    }

    public Document getDocument() {
        return document;
    }

    public String getXPath() {
        return xPath;
    }

    public Try<Stream<String>> getValues() {
        XPath theXPath = XPathFactory.newInstance().newXPath();

        return Try
                .of(() -> (NodeList) theXPath.evaluate(xPath, document.getDocumentElement(), XPathConstants.NODESET))
                .map(nodeList -> {
                    List<Node> nodes = new LinkedList<>();
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        nodes.add(nodeList.item(i));
                    }

                    return nodes.stream().map(node -> node.getTextContent());
                })
                .recoverWith(ex -> Try.failure(new XmlContentException(ex)));
    }

    public abstract Try<Document> setValue(String value);

    public abstract Try<Document> addValue(String value);
}
