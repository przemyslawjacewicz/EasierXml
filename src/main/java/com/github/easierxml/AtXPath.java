package com.github.easierxml;

import javaslang.control.Try;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AtXPath {
    private Document document;
    private String xPath;

    public AtXPath(Document document, String xPath) {
        this.document = document;
        this.xPath = xPath;
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

    public Try<Document> setValue(String value) {
        List<String> splitted = Arrays
                .stream(xPath.split("/"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        List<String> nodeXPaths = IntStream
                .rangeClosed(1, splitted.size())
                .mapToObj(position -> splitted.subList(0, position)
                        .stream()
                        .collect(Collectors.joining("/", "/", "")))
                .collect(Collectors.toList());

        XPath theXPath = XPathFactory.newInstance().newXPath();

        nodeXPaths
                .forEach(subPath -> {
                    Try
                            .of(() -> (Node) theXPath.evaluate(subPath, document.getDocumentElement(), XPathConstants.NODE))
                            .onFailure(ex -> {


                            });

                });


        return Try
                .of(() -> {
                    Node node = (Node) theXPath.evaluate(xPath, document.getDocumentElement(), XPathConstants.NODE);

//                    if (nodeList.getLength() != 1) {
//                        throw new Exception("Node list contains more than one node");
//                    }

                    System.out.println("node: " + node);
                    return node;
                })
                .map(node -> {
                    node.setTextContent(value);
                    return document;
                })
                .recoverWith(ex -> Try.failure(new XmlContentException(ex)));
    }

    public Try<Document> addValue(String value) {
        return setValue(value);
    }
}
