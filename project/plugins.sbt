addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.5.0")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.2.2")

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.13.0")

resolvers += Resolver.url("bintray-sbt-plugins", url("https://dl.bintray.com/sbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
