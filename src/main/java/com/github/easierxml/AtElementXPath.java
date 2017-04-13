package com.github.easierxml;

import javaslang.control.Try;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
                    parent[0].setTextContent(value);

                    return newDocument;
                });
    }

    @Override
    public Try<Document> addValue(String value) {
        return null;
    }


}
