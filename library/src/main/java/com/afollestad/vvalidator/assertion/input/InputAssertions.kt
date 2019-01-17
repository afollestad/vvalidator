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
  class UrlAssertion : Assertion<EditText, UrlAssertion>() {
    private val regex = Patterns.WEB_URL

    override fun isValid(view: EditText) = regex.matcher(view.text()).matches()

    override fun defaultDescription() = "must be a valid URL"
  }

  /** @author Aidan Follestad (@afollestad) */
  class UriAssertion internal constructor() : Assertion<EditText, UriAssertion>() {
    private var schemes: List<String> = emptyList()
    private var schemesDescription: String? = null
    private var that: ((Uri) -> Boolean)? = null
    private var thatDescription: String? = null
    private var descriptionToUse: String? = null

    /** Asserts that the URI has a scheme within the given values. */
    fun hasScheme(
      description: String,
      schemes: List<String>
    ): UriAssertion {
      this.schemesDescription = description
      this.schemes = schemes
      return this
    }

    /** Makes a custom assertion on the Uri. */
    fun that(
      description: String,
      that: (Uri) -> Boolean
    ): UriAssertion {
      this.thatDescription = description
      this.that = that
      return this
    }

    override fun isValid(view: EditText): Boolean {
      try {
        val uri = Uri.parse(view.text())
        val uriScheme = uri.scheme ?: ""
        if (schemes.isNotEmpty() && uriScheme !in schemes) {
          descriptionToUse = schemesDescription
              ?: "scheme '$uriScheme' not in ${schemes.displayString()}"
          return false
        }
        val thatResult = that?.invoke(uri) ?: true
        if (!thatResult) {
          descriptionToUse = thatDescription ?: "didn't pass custom validation"
          return false
        }
      } catch (_: Exception) {
        return false
      }
      return true
    }

    override fun defaultDescription() = descriptionToUse ?: "must be a valid Uri"

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
    private var exactly: Int? = null
    private var lessThan: Int? = null
    private var atMost: Int? = null
    private var atLeast: Int? = null
    private var greaterThan: Int? = null

    /** Asserts the number is an exact (=) value. */
    fun exactly(length: Int): NumberAssertion {
      exactly = length
      return this
    }

    /** Asserts the number is less than (<) a value. */
    fun lessThan(length: Int): NumberAssertion {
      lessThan = length
      return this
    }

    /** Asserts the number is at most (<=) a value. */
    fun atMost(length: Int): NumberAssertion {
      atMost = length
      return this
    }

    /** Asserts the number is at least (>=) a value. */
    fun atLeast(length: Int): NumberAssertion {
      atLeast = length
      return this
    }

    /** Asserts the number is greater (>) than a value. */
    fun greaterThan(length: Int): NumberAssertion {
      greaterThan = length
      return this
    }

    override fun isValid(view: EditText): Boolean {
      val intValue = view.text().toIntOrNull() ?: return false
      return when {
        exactly != null && intValue != exactly!! -> false
        lessThan != null && intValue >= lessThan!! -> false
        atMost != null && intValue > atMost!! -> false
        atLeast != null && intValue < atLeast!! -> false
        greaterThan != null && intValue <= greaterThan!! -> false
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

private fun EditText.text(): String {
  return text.toString()
}
