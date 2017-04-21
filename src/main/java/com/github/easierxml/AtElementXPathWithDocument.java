package com.github.easierxml;

import javaslang.Tuple;
import javaslang.control.Try;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AtElementXPathWithDocument extends AtXPathWithDocument {

    public AtElementXPathWithDocument(Document document, String xPath, List<String> parts) {
        super(document, xPath, parts);
    }

    @Override
    public Try<Document> setValue(String value) {
        return initDocument()
                .mapTry(newDocument -> {
                            Element parent = newDocument.getDocumentElement();
                            for (Map.Entry<String, String> entry : super.getElements()
                                    .entrySet()
                                    .stream()
                                    .sorted((x, y) -> x.getValue().length() - y.getValue().length())
                                    .skip(1)
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
                                    parent = (Element) result.item(0);
                                } else {
                                    throw new XmlContentException(String.format("XPath ambiguous: %s", subXPath));
                                }
                            }

                            return Tuple.of(newDocument, parent);
                        }
                )
                .mapTry(pair -> {
                    Document newDocument = pair._1();
                    Element parent = pair._2();

                    parent.setTextContent(value);

                    return newDocument;
                });

    }

    @Override
    public Try<Document> addValue(String value) {
        return initDocument()
                .mapTry(newDocument -> {
                            Element parent = newDocument.getDocumentElement();
                            for (Map.Entry<String, String> entry : super.getElements()
                                    .entrySet()
                                    .stream()
                                    .sorted((x, y) -> x.getValue().length() - y.getValue().length())
                                    .skip(1)
                                    .limit(super.getElements().size() - 2 < 0 ? 0 : super.getElements().size() - 2)
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
                                    parent = (Element) result.item(0);
                                }
                            }

                            return Tuple.of(newDocument, parent);
                        }
                )
                .mapTry(pair -> {
                    Document newDocument = pair._1();
                    Element parent = pair._2();

                    if (super.getElements().size() == 1) {
                        parent.setTextContent(value);
                    } else {
                        String name = super.getElements()
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
                });
    }

}