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

public class AtElementXPath extends AtXPath {

    public AtElementXPath(Document document, String xPath) {
        super(document, xPath);
    }

    @Override
    public Try<Document> setValue(String value) {
        List<String> parts = Arrays
                .stream(super.getXPath().split("/"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        Map<String, String> elements = IntStream
                .range(0, parts.size())
                .mapToObj(position -> new AbstractMap.SimpleImmutableEntry<>(parts.get(position),
                        parts.subList(0, position + 1)
                                .stream()
                                .collect(Collectors.joining("/", "/", ""))))
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        return Try
                .of(() -> DocumentBuilderFactory.newInstance().newDocumentBuilder())
                .map(documentBuilder -> {
                    Document newDocument = documentBuilder.newDocument();

                    if (super.getDocument().getDocumentElement() != null) {
                        Node root = super.getDocument().getDocumentElement();
                        Node newRoot = newDocument.importNode(root, true);
                        newDocument.appendChild(newRoot);
                    } else {
                        Element root = newDocument.createElement(elements
                                .entrySet()
                                .stream()
                                .sorted((x, y) -> x.getValue().length() - y.getValue().length())
                                .map(entry -> entry.getKey())
                                .iterator()
                                .next());
                        newDocument.appendChild(root);
                    }

                    return newDocument;
                })
                .mapTry(newDocument -> {
                            Map<String, String> elementsWithoutRoot = elements
                                    .entrySet()
                                    .stream()
                                    .sorted((x, y) -> x.getValue().length() - y.getValue().length())
                                    .skip(1)
                                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

                            Node parent = newDocument.getDocumentElement();
                            for (Map.Entry<String, String> entry : elementsWithoutRoot
                                    .entrySet()
                                    .stream()
                                    .sorted((x, y) -> x.getValue().length() - y.getValue().length())
                                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (v1, v2) -> v2, () -> new LinkedHashMap<>()))
                                    .entrySet()) {
                                String name = entry.getKey();
                                String subXPath = entry.getValue();

                                NodeList result = (NodeList) XPATH.evaluate(subXPath, newDocument, XPathConstants.NODESET);
                                if (result == null || result.getLength() == 0) {
                                    Element element = newDocument.createElement(name);
                                    parent.appendChild(element);
                                    parent = element;
                                } else if (result.getLength() == 1) {
                                    parent = result.item(0);
                                } else {
                                    throw new XmlContentException(String.format("XPath ambiguous: %s", subXPath));
                                }
                            }
                            parent.setTextContent(value);

                            return newDocument;
                        }
                );
    }

    @Override
    public Try<Document> addValue(String value) {
        List<String> parts = Arrays
                .stream(super.getXPath().split("/"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        Map<String, String> elements = IntStream
                .range(0, parts.size())
                .mapToObj(position -> new AbstractMap.SimpleImmutableEntry<>(parts.get(position),
                        parts.subList(0, position + 1)
                                .stream()
                                .collect(Collectors.joining("/", "/", ""))))
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        return Try
                .of(() -> DocumentBuilderFactory.newInstance().newDocumentBuilder())
                .map(documentBuilder -> {
                    Document newDocument = documentBuilder.newDocument();

                    if (super.getDocument().getDocumentElement() != null) {
                        Node root = super.getDocument().getDocumentElement();
                        Node newRoot = newDocument.importNode(root, true);
                        newDocument.appendChild(newRoot);
                    } else {
                        Element root = newDocument.createElement(elements
                                .entrySet()
                                .stream()
                                .sorted((x, y) -> x.getValue().length() - y.getValue().length())
                                .map(entry -> entry.getKey())
                                .iterator()
                                .next());
                        newDocument.appendChild(root);
                    }

                    return newDocument;
                })
                .mapTry(newDocument -> {
                            Node parent = newDocument.getDocumentElement();
                            for (Map.Entry<String, String> entry : elements
                                    .entrySet()
                                    .stream()
                                    .sorted((x, y) -> x.getValue().length() - y.getValue().length())
                                    .skip(1)
                                    .limit(elements.size() - 2 < 0 ? 0 : elements.size() - 2)
                                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (v1, v2) -> v2, () -> new LinkedHashMap<>()))
                                    .entrySet()) {
                                String name = entry.getKey();
                                String subXPath = entry.getValue();

                                NodeList result = (NodeList) XPATH.evaluate(subXPath, newDocument, XPathConstants.NODESET);
                                if (result == null || result.getLength() == 0) {
                                    Element element = newDocument.createElement(name);
                                    parent.appendChild(element);
                                    parent = element;
                                } else {
                                    parent = result.item(0);
                                }
                            }

                            if (elements.size() == 1) {
                                parent.setTextContent(value);
                            } else {
                                String name = elements
                                        .entrySet()
                                        .stream()
                                        .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                                        .map(entry -> entry.getKey())
                                        .iterator()
                                        .next();

                                Element element = newDocument.createElement(name);
                                element.setTextContent(value);
                                parent.appendChild(element);
                            }

                            return newDocument;
                        }
                );
    }


}
