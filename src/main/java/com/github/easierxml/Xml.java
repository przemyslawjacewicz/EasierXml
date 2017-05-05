package com.github.easierxml;

import javaslang.control.Try;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Xml {

    public static Using using(Document document) {
        return new UsingDocument(document);
    }

    public static Using using(Element element) {
        return Try
                .of(() -> {
                    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                    document.appendChild(element);
                    return using(document);
                })
                .getOrElseGet(ex -> new FailedUsing(ex));
    }

    public static Using using(String xml) throws XmlContentException {
        return Try
                .of(() -> {
                    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document document = documentBuilder.parse(new InputSource(new StringReader(xml)));
                    return using(document);
                })
                .getOrElseGet(ex -> new FailedUsing(ex));
    }

    public static Using using(Path file) {
        return Try
                .of(() -> {
                    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document document = documentBuilder.parse(file.toFile());
                    return using(document);
                })
                .getOrElseGet(ex -> new FailedUsing(ex));
    }

    private static class FailedUsing implements Using {
        private Throwable ex;

        private FailedUsing(Throwable ex) {
            this.ex = ex;
        }

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
    }
}
