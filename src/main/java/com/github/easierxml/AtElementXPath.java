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
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AtElementXPath extends AtXPath {

    public AtElementXPath(Document document, String xPath) {
        super(document, xPath);
    }

    @Override
    public Try<Document> setValue(String value) {
        List<String> splitted = Arrays
                .stream(super.getXPath().split("/"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

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
                                .iterator().next());
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
                            for (Map.Entry<String, String> entry : elementsWithoutRoot.entrySet()) {
                                String name = entry.getKey();
                                String subXPath = entry.getValue();
                                System.out.println("name:" + name + ", subXPath: " + subXPath);

                                NodeList result = (NodeList) theXPath.evaluate(subXPath, newDocument, XPathConstants.NODESET);
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
        return null;
    }


}
