package com.github.easierxml;

import javaslang.control.Try;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
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
                .collect(Collectors.toList());
        String attributeName = xPath.substring(xPath.lastIndexOf("@")+1);

        Map<String, String> elements = IntStream
                .range(0, splitted.size())
                .mapToObj(position -> {
                    String rawElementName = splitted.get(position);
                    String elementName = rawElementName.contains("@") ?
                            rawElementName.substring(0, rawElementName.lastIndexOf("@")) : rawElementName;
                    String rawElementSubXPath = splitted.subList(0, position + 1)
                            .stream()
                            .collect(Collectors.joining("/", "/", ""));
                    String elementSubXPath = rawElementSubXPath.contains("@") ?
                            rawElementSubXPath.substring(0, rawElementSubXPath.lastIndexOf("@")) : rawElementSubXPath;
                    return new AbstractMap.SimpleImmutableEntry<>(elementName, elementSubXPath);
                })
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));


        XPath theXPath = XPathFactory.newInstance().newXPath();

        if (document.getDocumentElement() == null) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = Try
                    .of(dbf::newDocumentBuilder)
                    .get();
            document = builder.newDocument();
        }

        final Node[] parent = {document};

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
                                Element result = (Element) theXPath.evaluate(subXPath, document.getDocumentElement(), XPathConstants.NODE);
                                if (result == null) {
                                    throw new Exception("Node not present: " + name);
                                }

                                return result;
                            })
                            .onFailure(ex -> {
                                System.out.println("onFailure: " + ex);
                                Element element = document.createElement(name);
                                parent[0].appendChild(element);
                                parent[0] = element;
                            })
                            .onSuccess(element -> {
                                System.out.println("onSuccess: " + element.getNodeName());
                                parent[0] = element;
                            });
                });
        ((Element) parent[0]).setAttribute(attributeName, value);
//        document.appendChild(parent[0]);


        return Try
                .of(() -> document);
    }

    public Try<Document> addValue(String value) {
        return setValue(value);
    }
}
