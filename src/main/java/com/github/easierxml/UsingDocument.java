package com.github.easierxml;

import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class UsingDocument {
    private static final XPath XPATH = XPathFactory.newInstance().newXPath();

    private Document document;

    public UsingDocument(Document document) {
        this.document = document;
    }

    public AtXPath at(String xPath) {
        try {
            XPATH.compile(xPath);
        } catch (XPathExpressionException e) {
            return new AtInvalidXPath(document, xPath);
        }

        String[] parts = xPath.split("/");
        if (parts[parts.length - 1].startsWith("@")) {
            return new AtAttributeXPath(document, xPath);
        }

        return new AtElementXPath(document, xPath);
    }
}
