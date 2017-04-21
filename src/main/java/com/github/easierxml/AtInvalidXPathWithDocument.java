package com.github.easierxml;

import javaslang.control.Try;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class AtInvalidXPathWithDocument extends AtXPathWithDocument {

    protected AtInvalidXPathWithDocument(Document document, String xPath) {
        super(document, xPath, new ArrayList<>());
    }

    @Override
    public Try<Document> setValue(String value) {
        return Try.failure(new XmlContentException(String.format("Invalid xPath: %s", super.getXPath())));
    }

    @Override
    public Try<Document> addValue(String value) {
        return Try.failure(new XmlContentException(String.format("Invalid xPath: %s", super.getXPath())));
    }
}
