# ImportMoreXtension
Adds custom operations with a focus on more interaction with external XML docs to the Oxygen XML Editor

This software package provides seven Java operations developed at the Digital Academy of the Academy of Sciences and Literature | Mainz that may be used to create custom actions within the Oxygen XML Editor that simplify importing XML fragments from resources other than the one to be edited. A documentation of all operations can be found in the [wiki of this repository](https://github.com/digicademy/ImportMoreXtension/wiki).


# Requirements
The ImportMoreXtension was developed and tested to be used with the versions 19.1 to 27.1 of the [Oxygen XML Editor](https://www.oxygenxml.com/) and depends on the the Saxon version included.

Since it extends functionality of the [AskMoreXtension](https://github.com/digicademy/AskMoreXtension) you will also need to add a released JAR with a version greater than or equal to 1.3.0.

If you plan on using the DisplayImportChartOperation you will also need to add JFreeChart (version 1.5.0).


# Download and Installation
All releases can be downloaded as a .jar file from the [release page of this repository](https://github.com/digicademy/ImportMoreXtension/releases).

You can include the package into your own Oxygen framework by adding the necessary JARs (AskMoreXtension, (JFreeChart,) ImportMoreXtension) to the classpath. Please find notes on how to do this in the [official documentation of the ClassPath Tab](https://www.oxygenxml.com/doc/versions/27.1/ug-editor/topics/document-type-classpath-tab.html).


# License
The software is published under the terms of the MIT license.


# Research Software Engineering and Development

Copyright 2021 <a href="https://orcid.org/0000-0002-5843-7577">Patrick Daniel Brookshire</a>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
