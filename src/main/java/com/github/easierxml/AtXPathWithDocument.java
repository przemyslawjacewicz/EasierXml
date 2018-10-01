package com.github.easierxml;

import javaslang.control.Try;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class AtXPathWithDocument implements AtXPath {
    private Document document;
    private String xPath;
    private Map<String, String> elements;

    protected AtXPathWithDocument(Document document, String xPath, List<String> parts) {
        this.document = document;
        this.xPath = xPath;
        this.elements = IntStream
                .range(0, parts.size())
                .mapToObj(position -> new AbstractMap.SimpleImmutableEntry<>(parts.get(position),
                        parts.subList(0, position + 1)
                                .stream()
                                .collect(Collectors.joining("/", "/", ""))))
                .collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue));
    }

    protected Document getDocument() {
        return document;
    }

    protected String getXPath() {
        return xPath;
    }

    protected Map<String, String> getElements() {
        return elements;
    }

    @Override
    public Try<String> getValue() {
        return getValues()
                .mapTry(stream -> stream.collect(Collectors.toList()))
                .mapTry(values -> {
                    if (values.size() != 1) {
                        throw new Exception("Node number does not equal one");
                    }
                    return values;
                })
                .mapTry(values -> values.iterator().next())
                .recoverWith(ex -> Try.failure(new XmlContentException(ex)));
    }

    @Override
    public Try<Stream<String>> getValues() {
        return Try
                .of(() -> (NodeList) XPATH.evaluate(xPath, document.getDocumentElement(), XPathConstants.NODESET))
                .mapTry(nodeList -> {
                    if (nodeList == null || nodeList.getLength() == 0) {
                        throw new Exception("Node not present");
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
                .mapTry(nodes -> nodes.stream().map(Node::getTextContent))
                .recoverWith(ex -> Try.failure(new XmlContentException(ex)));
    }

    protected Try<Document> initDocument() {
        return Try
                .of(() -> DocumentBuilderFactory.newInstance().newDocumentBuilder())
                .mapTry(documentBuilder -> {
                    Document newDocument = documentBuilder.newDocument();

                    if (document.getDocumentElement() != null) {
                        Node root = document.getDocumentElement();
                        Node newRoot = newDocument.importNode(root, true);
                        newDocument.appendChild(newRoot);
                    } else {
                        Element root = newDocument.createElement(elements
                                .entrySet()
                                .stream()
                                .sorted(Comparator.comparingInt(x -> x.getValue().length()))
                                .map(Map.Entry::getKey)
                                .iterator()
                                .next());
                        newDocument.appendChild(root);
                    }


                    return newDocument;
                });
    }

}
