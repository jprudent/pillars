import org.typelevel.scalacoptions.ScalacOptions
import org.typelevel.scalacoptions.ScalaVersion

ThisBuild / versionScheme          := Some("semver-spec")
ThisBuild / scalaVersion           := "3.5.2"
ThisBuild / organization           := "com.rlemaitre"
ThisBuild / homepage               := Some(url("https://pillars.dev/"))
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository     := "https://s01.oss.sonatype.org/service/local"
ThisBuild / pgpPublicRing          := file("/tmp/public.asc")
ThisBuild / pgpSecretRing          := file("/tmp/secret.asc")
ThisBuild / pgpPassphrase          := sys.env.get("PGP_PASSWORD").map(_.toArray)
ThisBuild / publishTo              := sonatypePublishToBundle.value
ThisBuild / startYear              := Some(2023)
ThisBuild / scmInfo                := Some(
  ScmInfo(url("https://github.com/rlemaitre/pillars/"), "scm:git:git@github.com:rlemaitre/pillars.git")
)
ThisBuild / developers             := List(
  Developer(
    "rlemaitre",
    "Raphaël Lemaitre",
    "raphael@rlemaitre.com",
    url("https://github.com/rlemaitre")
  )
)
ThisBuild / licenses += (
  "Apache-2.0",
  url(
    "https://www.apache.org/licenses/LICENSE-2.0"
  )
)
ThisBuild / scalacOptions ++= ScalacOptions.tokensForVersion(
  ScalaVersion.V3_5_0,
  Set(
    ScalacOptions.deprecation,
    ScalacOptions.feature,
    ScalacOptions.fatalWarnings,
    ScalacOptions.lint
  ) ++ ScalacOptions.privateWarnOptions ++ ScalacOptions.privateWarnUnusedOptions
) ++ Seq("-new-syntax")
//javaOptions += "-Dotel.java.global-autoconfigure.enabled=true"

Compile / scalacOptions ++= ScalacOptions.tokensForVersion(
  ScalaVersion.V3_5_0,
  Set(
    ScalacOptions.sourceFuture,
    ScalacOptions.deprecation,
    ScalacOptions.feature,
    ScalacOptions.fatalWarnings,
    ScalacOptions.lint
  ) ++ ScalacOptions.privateWarnOptions ++ ScalacOptions.privateWarnUnusedOptions
) ++ Seq("-new-syntax", "-Xmax-inlines=128")

enablePlugins(ScalaUnidocPlugin)

outputStrategy := Some(StdoutOutput)

//assumedVersionScheme := VersionScheme.Always
//libraryDependencySchemes ++= Seq(
//  "org.typelevel" %% "otel4s-core-trace" % VersionScheme.Always
//)
val libDependencySchemes = Seq(
  "io.circe"      %% "circe-yaml"        % VersionScheme.Always,
  "org.typelevel" %% "otel4s-core-trace" % VersionScheme.Always
)

lazy val core = Project("pillars-core", file("modules/core"))
    .enablePlugins(BuildInfoPlugin)
    .settings(
      name             := "pillars-core",
      description      := "pillars-core is a scala 3 library providing base services for writing backend applications",
      libraryDependencies ++= Dependencies.core,
      buildInfoKeys    := Seq[BuildInfoKey](name, version, description),
      buildInfoPackage := "pillars.build",
      libraryDependencySchemes ++= libDependencySchemes
    )

lazy val dbSkunk = Project("pillars-db-skunk", file("modules/db-skunk"))
    .enablePlugins(BuildInfoPlugin)
    .settings(
      name             := "pillars-db-skunk",
      description      := "pillars-db-skunk is a scala 3 library providing database services for writing backend applications using skunk",
      libraryDependencies ++= Dependencies.skunk,
      buildInfoKeys    := Seq[BuildInfoKey](name, version, description),
      buildInfoPackage := "pillars.db.build",
      libraryDependencySchemes ++= libDependencySchemes
    )
    .dependsOn(core)

lazy val dbDoobie = Project("pillars-db-doobie", file("modules/db-doobie"))
    .enablePlugins(BuildInfoPlugin)
    .settings(
      name             := "pillars-db-doobie",
      description      := "pillars-db-doobie is a scala 3 library providing database services for writing backend applications using doobie",
      libraryDependencies ++= Dependencies.doobie,
      buildInfoKeys    := Seq[BuildInfoKey](name, version, description),
      buildInfoPackage := "pillars.doobie.build",
      libraryDependencySchemes ++= libDependencySchemes
    )
    .dependsOn(core)

lazy val redisRediculous = Project("pillars-redis-rediculous", file("modules/redis-rediculous"))
    .enablePlugins(BuildInfoPlugin)
    .settings(
      name             := "pillars-redis-rediculous",
      description      := "pillars-redis-rediculous is a scala 3 library providing redis services for writing backend applications using rediculous",
      libraryDependencies ++= Dependencies.rediculous,
      buildInfoKeys    := Seq[BuildInfoKey](name, version, description),
      buildInfoPackage := "pillars.doobie.build",
      libraryDependencySchemes ++= libDependencySchemes
    )
    .dependsOn(core)

lazy val dbMigrations = Project("pillars-db-migration", file("modules/db-migration"))
    .enablePlugins(BuildInfoPlugin)
    .settings(
      name                                        := "pillars-db-migration",
      description                                 := "pillars-db is a scala 3 library providing database migrations",
      libraryDependencySchemes += "org.typelevel" %% "otel4s-core-trace" % VersionScheme.Always,
      libraryDependencies ++= Dependencies.migrations,
      buildInfoKeys                               := Seq[BuildInfoKey](name, version, description),
      buildInfoPackage                            := "pillars.db.migrations.build",
      libraryDependencySchemes ++= libDependencySchemes
    )
    .dependsOn(core, dbSkunk)

lazy val rabbitmqFs2 = Project("pillars-rabbitmq-fs2", file("modules/rabbitmq-fs2"))
    .enablePlugins(BuildInfoPlugin)
    .settings(
      name             := "pillars-rabbitmq-fs2",
      description      := "pillars-rabbitmq-fs2 is a scala 3 library providing RabbitMQ services for writing backend applications using fs2-rabbit",
      libraryDependencies ++= Dependencies.fs2Rabbit,
      buildInfoKeys    := Seq[BuildInfoKey](name, version, description),
      buildInfoPackage := "pillars.rabbitmq.fs2.build",
      libraryDependencySchemes ++= libDependencySchemes
    )
    .dependsOn(core)

lazy val flags = Project("pillars-flags", file("modules/flags"))
    .enablePlugins(BuildInfoPlugin)
    .settings(
      name             := "pillars-flags",
      description      := "pillars-flag is a scala 3 library providing feature flag services for writing backend applications",
      libraryDependencies ++= Dependencies.flags,
      buildInfoKeys    := Seq[BuildInfoKey](name, version, description),
      buildInfoPackage := "pillars.flags.build",
      libraryDependencySchemes ++= libDependencySchemes
    )
    .dependsOn(core)

lazy val httpClient = Project("pillars-http-client", file("modules/http-client"))
    .enablePlugins(BuildInfoPlugin)
    .settings(
      name             := "pillars-http-client",
      description      := "pillars-http-client is a scala 3 library providing http client services for writing backend applications",
      libraryDependencies ++= Dependencies.httpClient,
      buildInfoKeys    := Seq[BuildInfoKey](name, version, description),
      buildInfoPackage := "pillars.httpclient.build",
      libraryDependencySchemes ++= libDependencySchemes
    )
    .dependsOn(core)

// tag::example[]
lazy val example = Project("pillars-example", file("modules/example"))
    .enablePlugins(BuildInfoPlugin) // //<1>
    .settings(
      name             := "pillars-example",                                            // //<2>
      description      := "pillars-example is an example of application using pillars", // //<3>
      libraryDependencies ++= Dependencies.tests ++ Dependencies.migrationsRuntime,
      buildInfoKeys    := Seq[BuildInfoKey](name, version, description),                // //<4>
      buildInfoOptions := Seq(BuildInfoOption.Traits("pillars.BuildInfo")),             // //<5>
      buildInfoPackage := "example.build",                                              // //<6>
      publish / skip   := true,
      libraryDependencySchemes ++= libDependencySchemes
    )
    .dependsOn(core, dbSkunk, flags, httpClient, dbMigrations)
// end::example[]
lazy val docs = Project("pillars-docs", file("modules/docs"))
    .settings(
      name           := "pillars-docs",
      publish / skip := true,
      libraryDependencySchemes ++= libDependencySchemes
    )
    .dependsOn(core)

lazy val pillars = project
    .in(file("."))
    .aggregate(core, example, docs, dbSkunk, dbDoobie, dbMigrations, flags, httpClient, rabbitmqFs2, redisRediculous)
    .settings(
      name                                       := "pillars",
      publishArtifact                            := false,
      ScalaUnidoc / unidoc / unidocProjectFilter := inAnyProject -- inProjects(example, docs),
      ScalaUnidoc / unidoc / target              := file("target/microsite/output/api"),
      ScalaUnidoc / unidoc / scalacOptions ++= Seq(
        "-project",
        name.value,
        "-project-version",
        version.value,
        "-project-logo",
        "modules/docs/src/docs/images/logo.png",
        //    "-source-links:github://rlemaitre/pillars",
        "-social-links:github::https://rlemaitre.github.io/pillars"
      )
    )
