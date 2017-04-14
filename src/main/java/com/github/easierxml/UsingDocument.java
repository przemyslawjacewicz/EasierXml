package com.github.easierxml;

import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class UsingDocument {
    private Document document;

    public UsingDocument(Document document) {
        this.document = document;
    }

    public AtXPath at(String xPath) {
        XPath theXPath = XPathFactory.newInstance().newXPath();

        try {
            theXPath.compile(xPath);
        } catch (XPathExpressionException e) {
            return new AtInvalidXPath(document, xPath);
        }

        String[] splitted = xPath.split("/");
        if (splitted[splitted.length - 1].startsWith("@")) {
            return new AtAttributeXPath(document, xPath);
        }

        return new AtElementXPath(document, xPath);
    }
}
