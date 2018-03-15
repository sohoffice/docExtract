resolvers += Resolver.defaultLocal

lazy val root = (project in file("."))
  .enablePlugins(DocExtractPlugin)
  .settings(
    (docExtractTarget in docExtract) := "STDOUT"
  )
