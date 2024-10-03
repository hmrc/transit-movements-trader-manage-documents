
# transit-movements-trader-manage-documents

This is a service for generating PDFs for the following documents:
* TAD (Transit Accompanying Document)
* TSAD (Transit and Safety and Security Declaration)
* Unloading permission

### Download
In order to download these documents, submit the following messages through postman then download the document through the UI from the relevant view departures / view arrivals page:
* TAD
    * IE015
    * IE029
* Unloading permission
    * IE007
    * IE043

### Development
Here is a list of logs that have been encountered during development and the likely causes:

| Error                                                                                                                                                                                | Cause                                                                                                                                                                                                            | Fix                                                 |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------|
| `javax.xml.transform.TransformerException: org.xml.sax.SAXParseException; lineNumber: 2; columnNumber: 6; The processing instruction target matching "[xX][mM][lL]" is not allowed.` | There is likely a blank line above the `@template` in the `xxxDocument.scala.xml` file. This blank line seemingly introduces a BOM into the `XmlFormat.Appendable` which the PlayFop library is unable to parse. | Remove the blank line.                              |
| `Cannot find LM to handle given FO for LengthBase. (fo:table-row, location: 93:22)`                                                                                                  | LM corresponds to the Layout Manager. This log likely indicates a dimension (e.g. `width` and/or `height`) has been applied to a `table-row` as a percentage of something that PlayFop cannot determine.         | Remove the redundant dimension and regression test. |
| `Cannot find LM to handle given FO for LengthBase. (fo:table-cell, location: 764:30)`                                                                                                | LM corresponds to the Layout Manager. This log likely indicates a dimension (e.g. `width` and/or `height`) has been applied to a `table-cell` as a percentage of something that PlayFop cannot determine.        | Remove the redundant dimension and regression test. |
| `getContentAreaBPD called on unknown BPD`                                                                                                                                            | ???                                                                                                                                                                                                              | ???                                                 |

It can be tricky to interpret where in the code the location/line number/column number refers to but generally speaking if you `println` the XML that gets generated in the PDF generator class and copy it to a text interpreter the line numbers should roughly align (expect an offset of one or two lines in some cases).

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
