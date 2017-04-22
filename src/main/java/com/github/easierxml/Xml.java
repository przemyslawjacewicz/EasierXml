package com.github.easierxml;

import javaslang.control.Try;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Xml {

    public static Using using(Document document) {
        return new UsingDocument(document);
    }

    public static Using using(String xml) throws XmlContentException {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(xml)));
            return using(document);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            return new Using() {

                @Override
                public AtXPath at(String xPath) {
                    return new AtXPath() {

                        @Override
                        public Try<String> getValue() {
                            return Try.failure(new XmlContentException(ex));
                        }

                        @Override
                        public Try<Stream<String>> getValues() {
                            return Try.failure(new XmlContentException(ex));
                        }

                        @Override
                        public Try<Document> setValue(String value) {
                            return Try.failure(new XmlContentException(ex));
                        }

                        @Override
                        public Try<Document> addValue(String value) {
                            return Try.failure(new XmlContentException(ex));
                        }
                    };
                }
            };
        }
    }

    public static Using using(Path file) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(file.toFile());
            return using(document);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            return new Using() {

                @Override
                public AtXPath at(String xPath) {
                    return new AtXPath() {

                        @Override
                        public Try<String> getValue() {
                            return Try.failure(new XmlContentException(ex));
                        }

                        @Override
                        public Try<Stream<String>> getValues() {
                            return Try.failure(new XmlContentException(ex));
                        }

                        @Override
                        public Try<Document> setValue(String value) {
                            return Try.failure(new XmlContentException(ex));
                        }

                        @Override
                        public Try<Document> addValue(String value) {
                            return Try.failure(new XmlContentException(ex));
                        }
                    };
                }
            };
        }
    }
}
