---
layout: tutorial
title: "Scalaz module"
section: tutorial
sort_order: 11
---
Kantan.regex has a [scalaz](https://github.com/scalaz/scalaz) module that is, in its current incarnation, fairly bare
bones: it provides decoders for [`Maybe`] and [`\/`] as well as a few useful type class instances.

The `scalaz` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-scalaz" % "0.1.4"
```

You then need to import the corresponding package:

```scala
import kantan.regex.scalaz._
```

## `\/` decoder

The `scalaz` module provides a [`GroupDecoder`] instance for [`\/`]: for any type `A` and `B` that each have a
[`GroupDecoder`] instance, there exists a [`GroupDecoder`] instance for `A \/ B`.

First, a few imports:

```scala
import scalaz._
import kantan.regex.implicits._
```

We can then simply write the following:

```scala
scala> "[123] [true]".evalRegex[Int \/ Boolean](rx"\[(\d+|true|false)\]", 1).foreach(println _)
Success(-\/(123))
Success(\/-(true))
```

This also applies to [`MatchDecoder`] instances:

```scala
scala> "(1, true) and then (2, foo)".evalRegex[(Int, Boolean) \/ (Int, String)](rx"\((\d+), ([a-z]+)\)").foreach(println _)
Success(-\/((1,true)))
Success(\/-((2,foo)))
```

## `Maybe` decoder

The `scalaz` module provides a [`GroupDecoder`] instance for [`Maybe`]: for any type `A` that has a [`GroupDecoder`]
instance, there exists a [`GroupDecoder`] instance for `Maybe[A]`.

```scala
scala> "[123], []".evalRegex[Maybe[Int]](rx"\[(\d+)?\]", 1).foreach(println _)
Success(Just(123))
Success(Empty())
```

The same is true for [`MatchDecoder`], although I can't really think of an example for this odd concept.

## Scalaz instances

The following instance for cats type classes are provided:

* [`Functor`] for all decoders ([`GroupDecoder`] and [`MatchDecoder`]).
* [`Order`] for all result types ([`DecodeResult`], [`RegexResult`] and [`CompileResult`]).
* [`Monoid`] for all result types.
* [`Show`] for all result types.
* [`Traverse`] for all result types.
* [`Monad`] for all result types.
* [`BiFunctor`] for all result types.

[`Functor`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Functor
[`BiFunctor`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Bifunctor
[`Order`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Order
[`Show`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Show
[`Traverse`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Show
[`Monad`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Monad
[`Monoid`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Monoid
[`\/`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.$bslash$div
[`Maybe`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Maybe
[`GroupDecoder`]:{{ site.baseurl }}/api/index.html#kantan.regex.package@GroupDecoder[A]=kantan.codecs.Decoder[Option[String],A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`GroupDecoder`]:{{ site.baseurl }}/api/index.html#kantan.regex.package@GroupDecoder[A]=kantan.codecs.Decoder[Option[String],A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`MatchDecoder`]:{{ site.baseurl}}/api/index.html#kantan.regex.package@MatchDecoder[A]=kantan.codecs.Decoder[kantan.regex.Match,A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`RegexResult`]:{{ site.baseurl}}/api/index.html#kantan.regex.package@RegexResult[A]=kantan.codecs.Result[kantan.regex.RegexError,A]
[`DecodeResult`]:{{ site.baseurl }}/api/index.html#kantan.regex.package@DecodeResult[A]=kantan.codecs.Result[kantan.regex.DecodeError,A]
[`CompileResult`]:{{ site.baseurl }}/api/index.html#kantan.regex.package@CompileResult[A]=kantan.codecs.Result[kantan.regex.CompileError,A]
