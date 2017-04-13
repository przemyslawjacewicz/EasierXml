package com.github.easierxml;

import org.w3c.dom.Document;

public class UsingDocument {
    private Document document;

    public UsingDocument(Document document) {
        this.document = document;
    }

    public AtXPath at(String xPath) {
        String[] splitted = xPath.split("/");
        if (splitted[splitted.length - 1].startsWith("@")) {
            return new AtAttributeXPath(document, xPath);
        }

        return new AtElementXPath(document, xPath);
    }
}
