package com.github.easierxml;

import javaslang.control.Try;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.*;
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
                .filter(s -> !s.startsWith("@"))
                .collect(Collectors.toList());
        String attributeName = xPath.substring(xPath.lastIndexOf("@") + 1);

        Map<String, String> elements = IntStream
                .range(0, splitted.size())
                .mapToObj(position -> new AbstractMap.SimpleImmutableEntry<>(splitted.get(position),
                        splitted.subList(0, position + 1)
                                .stream()
                                .collect(Collectors.joining("/", "/", ""))))
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        XPath theXPath = XPathFactory.newInstance().newXPath();

        return Try
                .of(() -> DocumentBuilderFactory.newInstance().newDocumentBuilder())
                .map(documentBuilder -> {
                    Document newDocument = documentBuilder.newDocument();

                    if (document.getDocumentElement() != null) {
                        Node root = document.getDocumentElement();
                        Node newRoot = newDocument.importNode(root, true);
                        newDocument.appendChild(newRoot);
                    }

                    return newDocument;
                })
                .map(newDocument -> {
                    final Node[] parent = {newDocument};
                    elements
                            .entrySet()
                            .stream()
                            .sorted((x, y) -> x.getValue().length() - y.getValue().length())
                            .forEach(entry -> {
                                String name = entry.getKey();
                                String subXPath = entry.getValue();
                                Try
                                        .of(() -> {
                                            System.out.println(String.format("name: %s, subXPath: %s", name, subXPath));
                                            Element result = (Element) theXPath.evaluate(subXPath, newDocument.getDocumentElement(), XPathConstants.NODE);
                                            if (result == null) {
                                                throw new Exception("Node not present: " + name);
                                            }

                                            return result;
                                        })
                                        .onFailure(ex -> {
                                            System.out.println("onFailure: " + ex);
                                            Element element = newDocument.createElement(name);
                                            parent[0].appendChild(element);
                                            parent[0] = element;
                                        })
                                        .onSuccess(element -> {
                                            System.out.println("onSuccess: " + element.getNodeName());
                                            parent[0] = element;
                                        });
                            });
                    ((Element) parent[0]).setAttribute(attributeName, value);

                    return newDocument;
                });
    }

    public Try<Document> addValue(String value) {
        return setValue(value);
    }
}
