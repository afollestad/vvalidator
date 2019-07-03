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
import com.afollestad.vvalidator.assertion.Assertion
import com.google.android.material.textfield.TextInputLayout

/** @author Aidan Follestad (@afollestad) */
sealed class InputLayoutAssertions {

  /** @author Aidan Follestad (@afollestad) */
  class NotEmptyAssertion internal constructor() : Assertion<TextInputLayout, NotEmptyAssertion>() {

    override fun isValid(view: TextInputLayout) = view.editText?.text?.isNotEmpty() ?: false

    override fun defaultDescription() = "cannot be empty"
  }

  /** @author Aidan Follestad (@afollestad) */
  class UriAssertion internal constructor() : Assertion<TextInputLayout, UriAssertion>() {
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

    override fun isValid(view: TextInputLayout): Boolean {
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
  class EmailAssertion internal constructor() : Assertion<TextInputLayout, EmailAssertion>() {
    private val regex = Patterns.EMAIL_ADDRESS

    override fun isValid(view: TextInputLayout) = regex.matcher(view.text()).matches()

    override fun defaultDescription() = "must be a valid email address"
  }

  /** @author Aidan Follestad (@afollestad) */
  class NumberAssertion internal constructor() : Assertion<TextInputLayout, NumberAssertion>() {
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

    override fun isValid(view: TextInputLayout): Boolean {
      val longValue = view.text().toLongOrNull() ?: return false
      return when {
        exactly != null && longValue != exactly!! -> false
        lessThan != null && longValue >= lessThan!! -> false
        atMost != null && longValue > atMost!! -> false
        atLeast != null && longValue < atLeast!! -> false
        greaterThan != null && longValue <= greaterThan!! -> false
        else -> true
      }
    }

    override fun defaultDescription() = when {
      exactly != null -> "must equal $exactly"
      lessThan != null -> "must be less than $lessThan"
      atMost != null -> "must be at most $atMost"
      atLeast != null -> "must be at least $atLeast"
      greaterThan != null -> "must be greater than $greaterThan"
      else -> "must be a number"
    }
  }

  /** @author Chen Lei (@cooppor) */
  class NumberDecimalAssertion internal constructor() : Assertion<TextInputLayout, NumberDecimalAssertion>() {
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

    override fun isValid(view: TextInputLayout): Boolean {
      val doubleValue = view.text().toDoubleOrNull() ?: return false
      return when {
        exactly != null && doubleValue != exactly!! -> false
        lessThan != null && doubleValue >= lessThan!! -> false
        atMost != null && doubleValue > atMost!! -> false
        atLeast != null && doubleValue < atLeast!! -> false
        greaterThan != null && doubleValue <= greaterThan!! -> false
        else -> true
      }
    }

    override fun defaultDescription() = when {
      exactly != null -> "must equal $exactly"
      lessThan != null -> "must be less than $lessThan"
      atMost != null -> "must be at most $atMost"
      atLeast != null -> "must be at least $atLeast"
      greaterThan != null -> "must be greater than $greaterThan"
      else -> "must be a number"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class LengthAssertion internal constructor() : Assertion<TextInputLayout, LengthAssertion>() {
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

    override fun isValid(view: TextInputLayout): Boolean {
      val length = view.text()
          .length
      return when {
        exactly != null -> length == exactly!!
        lessThan != null -> length < lessThan!!
        atMost != null -> length <= atMost!!
        atLeast != null -> length >= atLeast!!
        greaterThan != null -> length > greaterThan!!
        else -> false
      }
    }

    override fun defaultDescription() = when {
      exactly != null -> "length must be exactly $exactly"
      lessThan != null -> "length must be less than $lessThan"
      atMost != null -> "length must be at most $atMost"
      atLeast != null -> "length must be at least $atLeast"
      greaterThan != null -> "length must be greater than $greaterThan"
      else -> "no length bound set"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class ContainsAssertion internal constructor(
    private val text: String
  ) : Assertion<TextInputLayout, ContainsAssertion>() {
    private var ignoreCase: Boolean = false

    /** Case is ignored when checking if the input contains the string. */
    fun ignoreCase(): ContainsAssertion {
      ignoreCase = true
      return this
    }

    override fun isValid(view: TextInputLayout) = view.text().contains(text, ignoreCase)

    override fun defaultDescription() = "must contain \"$text\""
  }

  /** @author Aidan Follestad (@afollestad) */
  class RegexAssertion(
    private val regexString: String
  ) : Assertion<TextInputLayout, RegexAssertion>() {
    private val regex = Regex(regexString)

    override fun isValid(view: TextInputLayout) = view.text().matches(regex)

    override fun defaultDescription() = "must match regex \"$regexString\""
  }
}

internal fun TextInputLayout.text(): String {
  return editText?.text?.toString() ?: ""
}
