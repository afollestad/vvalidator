/**
 * Designed and developed by Aidan Follestad (@afollestad)
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
@file:Suppress("unused")

package com.afollestad.vvalidator.assertion.input

import android.net.Uri
import android.util.Patterns
import android.widget.EditText
import com.afollestad.vvalidator.assertion.Assertion

/** @author Aidan Follestad (@afollestad) */
sealed class InputAssertions {

  /** @author Aidan Follestad (@afollestad) */
  class NotEmptyAssertion internal constructor() : Assertion<EditText, NotEmptyAssertion>() {
    override fun isValid(view: EditText) = view.text().isNotEmpty()

    override fun defaultDescription() = "cannot be empty"
  }

  /** @author Aidan Follestad (@afollestad) */
  class UriAssertion internal constructor() : Assertion<EditText, UriAssertion>() {
    private var schemes: List<String> = emptyList()
    private var that: ((Uri) -> Boolean)? = null
    private var defaultDescription: String? = null

    /** Asserts that the URI has a scheme within the given values. */
    fun hasScheme(vararg schemes: String): UriAssertion {
      this.schemes = schemes.toList()
      return this
    }

    /** Makes a custom assertion on the Uri. */
    fun that(that: (Uri) -> Boolean): UriAssertion {
      this.that = that
      return this
    }

    override fun isValid(view: EditText): Boolean {
      try {
        val uri = Uri.parse(view.text())
        val uriScheme = uri.scheme ?: ""
        if (schemes.isNotEmpty() && uriScheme !in schemes) {
          defaultDescription = "scheme '$uriScheme' not in ${schemes.displayString()}"
          return false
        }
        val thatResult = that?.invoke(uri) ?: true
        if (!thatResult) {
          defaultDescription = "didn't pass custom validation"
          return false
        }
      } catch (_: Exception) {
        return false
      }
      return true
    }

    override fun defaultDescription() = defaultDescription ?: "must be a valid Uri"

    private fun List<String>.displayString() = joinToString(
        prefix = "[",
        postfix = "]",
        transform = { "'$it'" }
    )
  }

  /** @author Aidan Follestad (@afollestad) */
  class EmailAssertion internal constructor() : Assertion<EditText, EmailAssertion>() {
    private val regex = Patterns.EMAIL_ADDRESS

    override fun isValid(view: EditText) = regex.matcher(view.text()).matches()

    override fun defaultDescription() = "must be a valid email address"
  }

  /** @author Aidan Follestad (@afollestad) */
  class NumberAssertion internal constructor() : Assertion<EditText, NumberAssertion>() {
    private var exactly: Long? = null
    private var lessThan: Long? = null
    private var atMost: Long? = null
    private var atLeast: Long? = null
    private var greaterThan: Long? = null

    /** Asserts the number is an exact (=) value. */
    fun exactly(length: Long): NumberAssertion {
      exactly = length
      return this
    }

    /** Asserts the number is less than (<) a value. */
    fun lessThan(length: Long): NumberAssertion {
      lessThan = length
      return this
    }

    /** Asserts the number is at most (<=) a value. */
    fun atMost(length: Long): NumberAssertion {
      atMost = length
      return this
    }

    /** Asserts the number is at least (>=) a value. */
    fun atLeast(length: Long): NumberAssertion {
      atLeast = length
      return this
    }

    /** Asserts the number is greater (>) than a value. */
    fun greaterThan(length: Long): NumberAssertion {
      greaterThan = length
      return this
    }

    override fun isValid(view: EditText): Boolean {
      val longValue = view.text().toLongOrNull() ?: return false
      if (exactly != null && longValue != exactly!!) return false
      if (lessThan != null && longValue >= lessThan!!) return false
      if (atMost != null && longValue > atMost!!) return false
      if (atLeast != null && longValue < atLeast!!) return false
      if (greaterThan != null && longValue <= greaterThan!!) return false
      return true
    }

    override fun defaultDescription(): String {
      val descriptionBuilder = StringBuilder().apply {
        appendIf(exactly != null, "exactly $exactly")
        appendIf(lessThan != null, "less than $lessThan")
        appendIf(atMost != null, "at most $atMost")
        appendIf(atLeast != null, "at least $atLeast")
        appendIf(greaterThan != null, "greater than $greaterThan")
      }
      if (descriptionBuilder.isEmpty()) {
        return "value must be a number"
      }
      return descriptionBuilder.insert(0, "value must be ")
          .toString()
    }
  }

  /** @author Chen Lei (@cooppor) */
  class NumberDecimalAssertion internal constructor() : Assertion<EditText, NumberDecimalAssertion>() {
    private var exactly: Double? = null
    private var lessThan: Double? = null
    private var atMost: Double? = null
    private var atLeast: Double? = null
    private var greaterThan: Double? = null

    /** Asserts the number is an exact (=) value. */
    fun exactly(length: Double): NumberDecimalAssertion {
      exactly = length
      return this
    }

    /** Asserts the number is less than (<) a value. */
    fun lessThan(length: Double): NumberDecimalAssertion {
      lessThan = length
      return this
    }

    /** Asserts the number is at most (<=) a value. */
    fun atMost(length: Double): NumberDecimalAssertion {
      atMost = length
      return this
    }

    /** Asserts the number is at least (>=) a value. */
    fun atLeast(length: Double): NumberDecimalAssertion {
      atLeast = length
      return this
    }

    /** Asserts the number is greater (>) than a value. */
    fun greaterThan(length: Double): NumberDecimalAssertion {
      greaterThan = length
      return this
    }

    override fun isValid(view: EditText): Boolean {
      val doubleValue = view.text().toDoubleOrNull() ?: return false
      if (exactly != null && doubleValue != exactly!!) return false
      if (lessThan != null && doubleValue >= lessThan!!) return false
      if (atMost != null && doubleValue > atMost!!) return false
      if (atLeast != null && doubleValue < atLeast!!) return false
      if (greaterThan != null && doubleValue <= greaterThan!!) return false
      return true
    }

    override fun defaultDescription(): String {
      val descriptionBuilder = StringBuilder().apply {
        appendIf(exactly != null, "exactly $exactly")
        appendIf(lessThan != null, "less than $lessThan")
        appendIf(atMost != null, "at most $atMost")
        appendIf(atLeast != null, "at least $atLeast")
        appendIf(greaterThan != null, "greater than $greaterThan")
      }
      if (descriptionBuilder.isEmpty()) {
        return "value must be a number"
      }
      return descriptionBuilder.insert(0, "value must be ")
        .toString()
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class LengthAssertion internal constructor() : Assertion<EditText, LengthAssertion>() {
    private var exactly: Int? = null
    private var lessThan: Int? = null
    private var atMost: Int? = null
    private var atLeast: Int? = null
    private var greaterThan: Int? = null

    /** Asserts the length is an exact (=) value. */
    fun exactly(length: Int): LengthAssertion {
      exactly = length
      return this
    }

    /** Asserts the length is less than (<) a value. */
    fun lessThan(length: Int): LengthAssertion {
      lessThan = length
      return this
    }

    /** Asserts the length is at most (<=) a value. */
    fun atMost(length: Int): LengthAssertion {
      atMost = length
      return this
    }

    /** Asserts the length is at least (>=) a value. */
    fun atLeast(length: Int): LengthAssertion {
      atLeast = length
      return this
    }

    /** Asserts the length is greater (>) than a value. */
    fun greaterThan(length: Int): LengthAssertion {
      greaterThan = length
      return this
    }

    override fun isValid(view: EditText): Boolean {
      val length = view.text()
          .length
      if (exactly != null) {
        return length == exactly!!
      }
      if (lessThan != null && length >= lessThan!!) return false
      if (atMost != null && length > atMost!!) return false
      if (atLeast != null && length < atLeast!!) return false
      if (greaterThan != null && length <= greaterThan!!) return false
      return true
    }

    override fun defaultDescription(): String {
      val descriptionBuilder = StringBuilder().apply {
        if (exactly != null) {
          append("exactly $exactly")
        } else {
          appendIf(atMost != null, "at most $atMost")
          appendIf(atLeast != null, "at least $atLeast")
          appendIf(greaterThan != null, "greater than $greaterThan")
          appendIf(lessThan != null, "less than $lessThan")
        }
      }
      if (descriptionBuilder.isEmpty()) {
        return "no length bound set"
      }
      return descriptionBuilder.insert(0, "length must be ")
          .toString()
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class ContainsAssertion internal constructor(
    private val text: String
  ) : Assertion<EditText, ContainsAssertion>() {
    private var ignoreCase: Boolean = false

    /** Case is ignored when checking if the input contains the string. */
    fun ignoreCase(): ContainsAssertion {
      ignoreCase = true
      return this
    }

    override fun isValid(view: EditText): Boolean {
      return view.text()
          .contains(text, ignoreCase)
    }

    override fun defaultDescription() = "must contain \"$text\""
  }

  /** @author Aidan Follestad (@afollestad) */
  class RegexAssertion(
    private val regexString: String
  ) : Assertion<EditText, RegexAssertion>() {
    private val regex = Regex(regexString)

    override fun isValid(view: EditText): Boolean {
      return view.text()
          .matches(regex)
    }

    override fun defaultDescription() = "must match regex \"$regexString\""
  }
}

private fun StringBuilder.appendIf(
  condition: Boolean,
  s: String,
  separator: String = ", "
) {
  if (condition) {
    if (isNotEmpty()) {
      append(separator)
    }
    append(s)
  }
}

private fun EditText.text(): String {
  return text.toString()
}
