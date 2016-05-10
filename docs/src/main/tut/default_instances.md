---
layout: tutorial
title: "Default instances"
section: tutorial
sort: 7
---


## `GroupDecoder`

### Basic types

The following types have [`GroupDecoder`] instances available out of the box:

* [`BigDecimal`]
* [`BigInt`]
* [`Byte`]
* [`Char`]
* [`Boolean`]
* [`Double`]
* [`Float`]
* [`Int`]
* [`Long`]
* [`Short`]
* [`String`]
* [`UUID`]
* [`URI`]
* [`URL`]

### `java.util.Date`

There also is a default [`GroupDecoder`] instance available for [`Date`], but this one is slightly more complicated. 
There are so many different ways of writing dates that there is no reasonable default behaviour - one might argue that 
defaulting to ISO 8601 might make sense, but there doesn't appear to be a sane way of implementing that in Java’s crusty
date / time API.

Instead of providing a default implementation that is likely going to be incorrect for most people, kantan.regex expects 
an implicit [`DateFormat`] instance in scope, and will decode using that format.

We could for example declare a formatter for something ISO 8601-like:

```tut:silent
import kantan.regex.ops._
import java.util.{Locale, Date}

implicit val formatter = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
```

And we're now capable of decoding XML content as dates:

```tut
"2000-01-00T00:00:00.000".evalRegex[Date]("\\d\\d\\d\\d-\\d\\d-\\d\\d").foreach(println _)
```

Note that kantan.regex has a joda-time module, a very well thought out alternative to [`Date`]. 

### `Either`

For any two types `A` and `B` that each have a [`GroupDecoder`], there exists a
[`GroupDecoder[Either[A, B]]`][`GroupDecoder`].


This is useful for dodgy string data where the type of a value is not well defined - it might sometimes be an int, 
sometimes a boolean, for example:

```tut
"[123] [true]".evalRegex[Either[Int, Boolean]]("\\[(\\d+|true|false)\\]", 1).foreach(println _)
```

### `Option`

For any type `A` that has a [`GroupDecoder`], there exists a [`GroupDecoder[Option[A]]`][`GroupDecoder`].

This is particularly useful for optional groups. For example:

```tut
"[123], []".evalRegex[Option[Int]]("\\[(\\d+)?\\]", 1).foreach(println _)
```


## `MatchDecoder`

### Basic types

Any type `A` such that `A` has a [`GroupDecoder`] also has a [`MatchDecoder`].

Note that this can sometimes be a bit tricky, as it involves selecting the group that's the most likely to be the
correct one. Kantan.regex will consider that, unless explicitly instructed otherwise, turning a [`GroupDecoder`] into
a [`MatchDecoder`] is done by looking at the *entire match* (that is, group 0).

That is in fact what was happening in most examples above: [`evalRegex`] expects a [`MatchDecoder`], not a
[`GroupDecoder`].


### Tuples

Tuples composed of types that each have a [`GroupDecoder`] automatically have a [`MatchDecoder`]. This is done by
assuming that the value of group 1 corresponds to the first field in the tuple, group 2 to the second, ...

```tut
"[1, true] and then [3, false]".evalRegex[(Int, Boolean)]("\\[(\\d+), ([a-z]+)\\]").foreach(println _)
```


### `Either`

For any two types `A` and `B` that each have a [`MatchDecoder`], there exists a
[`MatchDecoder[Either[A, B]]`][`MatchDecoder`].

This works essentially the same way as [`GroupDecoder`] for [`Either`]:

```tut
"[123, true] [456, foo]".evalRegex[Either[(Int, Boolean), (Int, String)]]("\\[(\\d+), ([a-z]+)\\]").foreach(println _)
```

### `Option`

For any type `A` that has a [`MatchDecoder`], there exists a [`MatchDecoder[Option[A]]`][`MatchDecoder`].

Now, I know for a fact this works - I have tests for it. I just can't really think of an actual use case for it -
the notion of an optional match is... odd.


[`BigInt`]:http://www.scala-lang.org/api/current/index.html#scala.math.BigInt
[`BigDecimal`]:http://www.scala-lang.org/api/current/index.html#scala.math.BigDecimal
[`Byte`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Byte.html
[`Char`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Character.html
[`Boolean`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Boolean.html
[`Double`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Double.html
[`Float`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Float.html
[`Int`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html
[`Long`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Long.html
[`Short`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Short.html
[`String`]:https://docs.oracle.com/javase/7/docs/api/java/lang/String.html
[`UUID`]:https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html
[`URL`]:https://docs.oracle.com/javase/7/docs/api/java/net/URL.html
[`URI`]:https://docs.oracle.com/javase/7/docs/api/java/net/URI.html
[`Reader`]:https://docs.oracle.com/javase/7/docs/api/java/io/Reader.html
[`InputStream`]:https://docs.oracle.com/javase/7/docs/api/java/io/InputStream.html
[`File`]:https://docs.oracle.com/javase/7/docs/api/java/io/File.html
[`Path`]:https://docs.oracle.com/javase/7/docs/api/java/nio/file/Path.html
[`XmlSource`]:{{ site.baseUrl }}/api/#kantan.xpath.XmlSource
[`GroupDecoder`]:{{ site.baseUrl }}/api/index.html#kantan.regex.package@GroupDecoder[A]=kantan.codecs.Decoder[Option[String],A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`Date`]:https://docs.oracle.com/javase/7/docs/api/java/util/Date.html
[`DateFormat`]:https://docs.oracle.com/javase/7/docs/api/java/text/DateFormat.html
[`MatchDecoder`]:{{ site.baseUrl }}/api/index.html#kantan.regex.package@MatchDecoder[A]=kantan.codecs.Decoder[kantan.regex.Match,A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`Either`]:http://www.scala-lang.org/api/current/index.html#scala.util.Either
[`evalRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops$$StringOps@evalRegex[A](expr:String,group:Int)(implicitevidence$2:kantan.regex.GroupDecoder[A]):Iterator[kantan.regex.RegexResult[A]]