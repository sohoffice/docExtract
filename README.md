A really simple sbt 1.0 plugin to extract document to an output file. At the moment it works for scala source files and will generate a file 
in java resource bundle format.

The output file can be used as input of other build tool, which I plan to use it along with 
[play-swagger](https://github.com/iheartradio/play-swagger) to supply the description of swagger definitions.

Output example:

```
example.CaseClass = This is a CaseClass for testing\
This is the second line.
example.CaseClass.name = Name of CaseClass
example.CaseClass.age = Age of CaseClass
example.FooClass = Foo class
example.FooClass.someMethod() = some method
example.FooClass.someMethod(String) = some method with argument
example.FooClass.someMethod(String)#name = The first argument of someMethod is name.
```

Please note this plugin only support sbt 1.0, sbt 0.13 or lower is not supported.

Play-Swagger
------------

The plugin is designed to be used with [play-swagger](https://github.com/iheartradio/play-swagger). The integration require the following steps.

1. Install this plugin as described in the [Standalone installation](#installation) section.
2. In build.sbt, add (or modify) the following project settings

```
lazy val root = (project in file("."))
  .enablePlugins(DocExtractPlugin, SwaggerPlugin)
  .settings(
    swagger := swagger.dependsOn(docExtract).value,
    swaggerDescriptionFile := docExtractTargetFile.value.right.toOption
  )
```

  The above will instruct sbt to run docExtract before swagger, and supply docExtractTargetFile as swaggerDescriptionFile so it can be picked up by swagger.
  
  Execute `swagger` to re-generate swagger.json

Standalone installation
-----------------------

Use the plugin with the following setup:

In the `project/plugins.sbt` file

```
addSbtPlugin("com.sohoffice" % "sbt-doc-extract" % "0.0.3")
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

Running
-------

In sbt console, execute 'docExtract' to run.
