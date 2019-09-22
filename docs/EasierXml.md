# EasierXml

Replace JDOM with standard Java XML using `org.w3c.Document`.

## Extraction

```java
Xml.using(Document document).at(String xPath).getValue().asText(): Try<String>
Xml.using(Document document).at(String xPath).getValues().asText(): Try<Stream<String>>
Xml.using(Document document).at(String xPath).getValue().asInt(): Try<Integer>
Xml.using(Document document).at(String xPath).getValues().asInt(): Try<Stream<Integer>>
Xml.using(Document document).at(String xPath).getValue().asDouble(): Try<Double>
Xml.using(Document document).at(String xPath).getValues().asDouble(): Try<Stream<Double>>
```

`xPath` determines either attribute or element:
  - if `xPath` is an attribute path - `getValue` gets attribute value, `getValues` gets attribute value as a one element `Stream`
  - if `xPath` is an element path - `getValue` gets element value, `getValues` gets elements' value as a `Stream`

Return: a `Try.Success` with value, or `Try.Failure` with error detail on errors

Overloads:
  - `using(String xml)` - the same functionality, but using a string in place of `Document`
  - `using(Path file)` - the same functionality, but using characters read from a file in place of `Document`

## Insertion

```java
Xml.using(Document document).at(String xPath).setValue(String value).getDocument(): Try<Document>
Xml.using(Document document).at(String xPath).addValue(String value).getDocument(): Try<Document>
```

`xPath` determines either attribute or element:
  - if `xPath` is an attribute path - `setValue(value)` sets existing attribute to value, or creates an attribute with value if attribute does not exist
  - if `xPath` is an attribute path - `addValue(value)` sets existing attribute to value, or creates an attribute with value if attribute does not exist
  - if `xPath` is an element path - `setValue(value)` sets existing element to value, or creates an element with value if element does not exist
  - if `xPath` is an element path - `addValue(value)` adds an element with value if element exists, or creates an element with value if element does not exist

Return: a `Try.Success` with a new Document, or `Try.Failure` with error detail on errors

Overloads:
  - `getString(): Try<String>` - the same functionality, but returning a string in place of a `Document`
  - `getFile(Path file): Try<Path>` - the same functionality, but returning a Path to file with `Document` content written to it
