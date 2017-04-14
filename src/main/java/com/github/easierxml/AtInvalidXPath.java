package com.github.easierxml;

import javaslang.control.Try;
import org.w3c.dom.Document;

public class AtInvalidXPath extends AtXPath {

    protected AtInvalidXPath(Document document, String xPath) {
        super(document, xPath);
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
