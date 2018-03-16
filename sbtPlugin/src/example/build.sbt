// resolvers += Resolver.defaultLocal
resolvers += Resolver.bintrayIvyRepo("sohoffice", "sbt-plugins")

lazy val root = (project in file("."))
  .enablePlugins(DocExtractPlugin)
  .settings(
    (docExtractTarget in docExtract) := "STDOUT"
  )
