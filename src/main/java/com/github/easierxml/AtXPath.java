package com.github.easierxml;

import javaslang.control.Try;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.util.stream.Stream;

public interface AtXPath {
    XPath XPATH = XPathFactory.newInstance().newXPath();

    Try<String> getValue();

    Try<Stream<String>> getValues();

    Try<Document> setValue(String value);

    Try<Document> addValue(String value);
}
