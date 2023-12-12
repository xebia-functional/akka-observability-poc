resolvers := Resolver.combineDefaultResolvers(
    Vector(
        // "Akka library repository".at("https://repo.akka.io/maven"),
        "NCL Nexxus" at "https://nexus.nclmiami.ncl.com/content/groups/public/",
        Resolver.url(
            "NCL Nexxus (Ivy Style Pattern)",
            new java.net.URL("https://nexus.nclmiami.ncl.com/content/groups/public/")
        )(Resolver.ivyStylePatterns),
    ),
    mavenCentral = true
)

libraryDependencies += "com.typesafe" % "config" % "1.3.1"

libraryDependencies += "com.ncl.scalastyle.scalariform" %%  "ncl-scalastyle-rules" % "1.0.4"
