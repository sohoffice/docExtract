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

- Clone [my modified version of play-swagger](https://github.com/sohoffice/docExtract), which will accept a java properties file to provision the descriptions.

  > The modified version is currently only in my repo, I'll see if the community has the needs to decide whether to submit a pull request.

- In the cloned play-swagger directory, switch to play2.6 branch

```
git checkout play2.6
```

- In the cloned play-swagger directory, publish the artifact to local

```sbtshell
sbt
> publishLocal
```

- In the `project/plugins.sbt` file

```sbtshell
addSbtPlugin("com.iheart" %% "sbt-play-swagger" % "0.7.5-SNAPSHOT")
addSbtPlugin("com.sohoffice" % "sbt-doc-extract" % "0.0.3")
```

- In the `build.sbt` file

```sbtshell
resolvers += Resolver.bintrayIvyRepo("sohoffice", "sbt-plugins")

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, DocExtractPlugin, SwaggerPlugin)
  .settings(
    // Make sure you set the swaggerDomainNameSpaces according to your package structure.
    // swaggerDomainNameSpaces := Seq("io")
    swagger := swagger.dependsOn(docExtract).value,
    swaggerDescriptionFile := docExtractTargetFile.value.right.toOption
  )
```

  The above will instruct sbt to run docExtract before swagger, and supply docExtractTargetFile as swaggerDescriptionFile so it can be picked up by swagger.
  
- Execute `swagger` to re-generate swagger.json

- Or check the [seed project](https://github.com/sohoffice/play-doc-gen-seed-projects) for reference.


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
