/*
 * Copyright 2017 Nicolas Rinaudo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kantan.regex

import java.util.regex.Matcher

/** Type class for types that can be compiled to instances of [[Regex]].
  *
  * While regular expression [[literals]] are usually the preferred way of creating instances of [[Regex]], they don't
  * fit all possible situations - one might imagine, for example, a scenario where a regular expression is created
  * dynamically before being compiled.
  *
  * [[Compiler]] is provided for these cases where literals are not an option. The preferred way of using it is to
  * make sure the corresponding [[kantan.regex.ops.CompilerOps syntax]] is in scope and use the `asRegex` method:
  * {{{
  * // Obtain a regular expression as a string somehow.
  * val regex: String = ???
  *
  * // Promote it to a Regex[Int]
  * regex.asRegex[Int]
  * }}}
  */
trait Compiler[E] {
  /** Compiles the specified expression into a [[Regex]]. */
  def compile[A](expr: E)(implicit db: MatchDecoder[A]): CompileResult[Regex[DecodeResult[A]]]

  /** Compiles the specified expression into a [[Regex]].
    *
    * This is useful when you're only interested in a specific group of each match, and that group is of a type that
    * already has a [[GroupDecoder]] - typically, a primitive type. For example:
    * {{{
    *   // An example match will be [123], with the first equal to 123. That's the bit we want to extract
    *   // as an int.
    *   compiler.compile[Int]("""\[(\d+)\]""", 1)
    * }}}
    *
    *
    * A [[[MatchDecoder MatchDecoder[A]]]] will be generated by calling
    * [[MatchDecoder$.fromGroup[A](index:Int)* MatchDecoder.fromGroup]] with the specified `group` value.
    */
  def compile[A: GroupDecoder](expr: E, group: Int): CompileResult[Regex[DecodeResult[A]]] =
    compile(expr)(MatchDecoder.fromGroup[A](group))

  /** Unsafe version of `compile`. */
  def unsafeCompile[A: MatchDecoder](expr: E): Regex[DecodeResult[A]] = compile(expr).get

  /** Unsafe version of `compile`. */
  def unsafeCompile[A: GroupDecoder](expr: E, group: Int): Regex[DecodeResult[A]] = compile(expr, group).get
}

/** Provides default instances, instance creation and instance summoning methods. */
object Compiler {
  /** Summons an implicit instance of [[[Compiler Compiler[A]]] if one can be found.
    *
    * This is a convenience method and equivalent to calling `implicitly[Compiler[A]]`
    */
  def apply[A](implicit ev: Compiler[A]): Compiler[A] = macro imp.summon[Compiler[A]]

  /** Creates a new [[Compiler]] instance from a function that turns `A` into a `Pattern`. */
  def fromPattern[A](f: A ⇒ CompileResult[Pattern]): Compiler[A] = new Compiler[A] {
    override def compile[B: MatchDecoder](expr: A): CompileResult[Regex[DecodeResult[B]]] =
      f(expr).map(p ⇒ Regex(p))
  }

  /** Provides compilation for Scala Regexes. */
  implicit val scalaRegex: Compiler[scala.util.matching.Regex] = fromPattern(r ⇒ CompileResult.success(r.pattern))
  /** Provides compilation for Java Patterns. */
  implicit val pattern: Compiler[Pattern] = fromPattern(p ⇒ CompileResult.success(p))
  /** Provides compilation for Strings. */
  implicit val string: Compiler[String] = fromPattern(s ⇒ CompileResult(java.util.regex.Pattern.compile(s)))
}

@SuppressWarnings(Array("org.wartremover.warts.Var"))
private class MatchIterator(val matcher: Matcher) extends Iterator[Match] {
  var nextSeen = false
  val m = new Match(matcher)

  override def hasNext = {
    if(!nextSeen) nextSeen = matcher.find()
    nextSeen
  }

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  override def next(): Match = {
    if(!hasNext) throw new NoSuchElementException
    nextSeen = false
    m
  }
}
