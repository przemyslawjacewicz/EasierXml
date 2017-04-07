package com.github.easierxml;

import org.w3c.dom.Document;

public class UsingDocument {
    private Document document;

    public UsingDocument(Document document) {
        this.document = document;
    }

    public AtXPath at(String xPath) {
        return new AtXPath(document, xPath);
    }
}
