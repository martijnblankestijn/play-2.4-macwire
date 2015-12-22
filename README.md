# Compile Time Dependency Injection with MacWire

A lot of examples that has to do with Compile Time Dependency Injection using the Play! Framework 2.4 just deal with
the simple cases. How to get it running or how to inject a service into a controller.

But I wanted the full picture. What if I want to use Play 2.4 in combination with [MacWire](https://github.com/adamw/macwire).
And configure a [PostgreSQL](http://www.postgresql.org/) database 
with [Slick 3.1](http://slick.typesafe.com/doc/3.1.0/) 
and Play [managing the database evolutions](https://www.playframework.com/documentation/2.4.x/Evolutions).

## Run it
Running the whole setup is easy (I hope ;-)).
Prerequisites for running this project is that you have [sbt](http://www.scala-sbt.org/) 
and ['Docker Machine'](https://docs.docker.com/machine/) installed.

In the root of the project there is a `runlocal.sh` script that will build a docker container, start a docker container
and will run the Play applcation through `sbt run`.

If docker and sbt have downloaded the internet ;-), you can access the application through [http://localhost:9000](http://localhost:9000/locations).

## Structure

### Postgres, Slick and Evolutions
The dependencies in the build.sbt needed to use Slick, (Database) Evolutions for a PostgreSQL database are as follows:

```
libraryDependencies ++= Seq(
  evolutions,
  "com.typesafe.play" %% "play-slick" % "1.1.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  "com.typesafe.slick" %% "slick" % "3.1.0",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
```

'evolutions' is the configured Play import of the module 'play-jdbc-evolutions'.
This is followed by the integration library for Slick within Play and the use of Evolutions in combination with Slick.
Last but not least, Slick itself and the JDBC-driver.

The configuration in the `src/main/resources/application.conf` configures the use of evolutions, Slick for PostgreSQL.

```
play.evolutions.db.default.autoApply=true

slick.dbs.default.driver= "slick.driver.PostgresDriver$"
slick.dbs.default.db.driver="org.postgresql.Driver"
slick.dbs.default.db.url=${?JDBC_DATABASE_URL}
```

`play.evolutions.db.default.autoApply=true` enables automatically applying database-changes.
The other three properties are needed to access the database with Slick. 
More than one database is possible, in this case we use the default database name 'default'.


## Links
See the following documentation, links that I used

* [Generic documentation on Play 2.4 Compile Time dependency injection]
  (https://www.playframework.com/documentation/2.4.x/ScalaCompileTimeDependencyInjection)
* A reply from [Nicolae Namolovan](https://groups.google.com/forum/#!topic/play-framework/02WRgu_mruc) had a 
  link to [Gist](https://gist.github.com/iref/d7493f9b8b390efaa26e)
  which brought the solution for my problem with automatically applying the database evolutions.
  
  
## Problems

### My problem with applying database evolutions
I tried for an hour to get the database evolutions to be applied. 
I thought everything was ok. 
Even compared my setup with another project. 
In the end I realized I had left one, VERY important, element out in the SQL-file. 
If you leave that comment out, you can have a beautifully crafted SQL-file, but Play will do nothing with it ;-(.

```# --- !Ups```

...

```# --- !Downs```

...

I know, it is stated perfectly in the [Play Documentation](https://www.playframework.com/documentation/2.4.x/Evolutions#Evolutions-scripts).
But hey, are we always looking at the documentation ;-).

### Slugsize problems

I got errors on the slug size:

```
-----> Compressing... 
 !   Compiled slug size: 304.7MB is too large (max is 300MB).
```

I was using the buildpack for [scala](http://github.com/heroku/heroku-buildpack-scala.git) 
and should have been using the buildpack for [play](http://github.com/heroku/heroku-buildpack-play.git).
This reduce the size of the slug from over 300 MB to (be tested) MB.


