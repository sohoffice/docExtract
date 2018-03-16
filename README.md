A really simple sbt plugin to extract document to an output file. At the moment it works for scala source files and will generate a file 
in java resource bundle format.

The output file can be used as input of other build tool, which I plan to use it along with 
[play-swagger](https://github.com/iheartradio/play-swagger) to supply the description of swagger definitions.

Output example:

```
com.sohoffice.doc.extract.CaseClass = This is a CaseClass for testing\
This is the second line.
com.sohoffice.doc.extract.CaseClass.name = Name of CaseClass
com.sohoffice.doc.extract.CaseClass.age = Age of CaseClass
```

Installation
------------

Use the plugin with the following setup:

In the `project/plugins.sbt` file

```
resolvers += Resolver.bintrayIvyRepo("sohoffice", "sbt-plugins")

addSbtPlugin("com.sohoffice" % "sbt-doc-extract" % "0.1-SNAPSHOT")
```

In the `build.sbt` file

```
resolvers += Resolver.bintrayIvyRepo("sohoffice", "sbt-plugins")

lazy val root = (project in file("."))
  .enablePlugins(DocExtractPlugin)
  .settings(
    (docExtractTarget in docExtract) := "STDOUT"
  )
```

You may specify a filename to `(docExtractTarget in docExtract)`, or use `STDOUT` or `STDERR` to output to console. 
The default of docExtractTarget is `docExtract.properties`, which means the plugin will output to the file `target/docExtract.properties`.
