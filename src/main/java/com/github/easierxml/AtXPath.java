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
import java.util.stream.Collectors;
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

    public Try<String> getValue() {
        return getValues()
                .map(stream -> stream
                        .collect(Collectors.toList())
                        .iterator()
                        .next())
                .recoverWith(ex -> Try.failure(new XmlContentException(ex)));
    }

    public Try<Stream<String>> getValues() {
        XPath theXPath = XPathFactory.newInstance().newXPath();

        return Try
                .of(() -> (NodeList) theXPath.evaluate(xPath, document.getDocumentElement(), XPathConstants.NODESET))
                .mapTry(nodeList -> {
                    if (nodeList == null || nodeList.getLength() == 0) {
                        throw new Exception("Attribute not present");
                    }
                    return nodeList;
                })
                .mapTry(nodeList -> {
                    List<Node> nodes = new LinkedList<>();
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        nodes.add(nodeList.item(i));
                    }

                    return nodes;
                })
                .mapTry(nodes -> nodes.stream().map(node -> node.getTextContent()))
                .recoverWith(ex -> Try.failure(new XmlContentException(ex)));
    }

    public abstract Try<Document> setValue(String value);

    public abstract Try<Document> addValue(String value);
}
