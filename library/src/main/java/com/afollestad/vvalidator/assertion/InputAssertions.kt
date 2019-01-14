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

package com.afollestad.vvalidator.assertion

import android.net.Uri
import android.util.Patterns
import android.widget.EditText

/** @author Aidan Follestad (@afollestad) */
sealed class InputAssertions {

  /** @author Aidan Follestad (@afollestad) */
  class NotEmptyAssertion : Assertion<EditText>() {
    override fun isValid(view: EditText): Boolean {
      return view.text()
          .isNotEmpty()
    }

    override fun description(): String {
      return "cannot be empty"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class UrlAssertion : Assertion<EditText>() {
    private val regex = Patterns.WEB_URL

    override fun isValid(view: EditText): Boolean {
      return regex.matcher(view.text())
          .matches()
    }

    override fun description(): String {
      return "must be a valid URL"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class UriAssertion : Assertion<EditText>() {

    private var schemes: Array<out String> = emptyArray()
    private var that: ((Uri) -> Boolean)? = null
    private var thatDescription: String? = null
    private var description: String? = null

    /** Asserts that the URI has a scheme within the given values. */
    fun hasScheme(vararg schemes: String): UriAssertion {
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
        if (schemes.isNotEmpty() && uri.scheme !in schemes) {
          description = "Scheme '${uri.scheme}' not in ${schemes.displayString()}"
          return false
        }
        val thatResult = that?.invoke(uri) ?: true
        if (!thatResult) {
          description = thatDescription
          return false
        }
      } catch (_: Exception) {
        return false
      }
      return true
    }

    override fun description() = description ?: "must be a valid Uri"

    private fun Array<out String>.displayString(): String {
      return joinToString(
          prefix = "[",
          postfix = "]",
          transform = { "'$it'" }
      )
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class EmailAssertion : Assertion<EditText>() {
    private val regex = Patterns.EMAIL_ADDRESS

    override fun isValid(view: EditText): Boolean {
      return regex.matcher(view.text())
          .matches()
    }

    override fun description(): String {
      return "must be a valid email address"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class NumberAssertion : Assertion<EditText>() {
    private var exactly: Int? = null
    private var lessThan: Int? = null
    private var atMost: Int? = null
    private var atLeast: Int? = null
    private var greaterThan: Int? = null

    /** Asserts the number is an exact (=) value. */
    fun exactly(length: Int) {
      exactly = length
    }

    /** Asserts the number is less than (<) a value. */
    fun lessThan(length: Int) {
      lessThan = length
    }

    /** Asserts the number is at most (<=) a value. */
    fun atMost(length: Int) {
      atMost = length
    }

    /** Asserts the number is at least (>=) a value. */
    fun atLeast(length: Int) {
      atLeast = length
    }

    /** Asserts the number is greater (>) than a value. */
    fun greaterThan(length: Int) {
      greaterThan = length
    }

    override fun isValid(view: EditText): Boolean {
      val intValue = view.text()
          .toIntOrNull() ?: return false
      if (exactly != null && intValue != exactly!!) {
        return false
      } else if (lessThan != null && intValue >= lessThan!!) {
        return false
      } else if (atMost != null && intValue > atMost!!) {
        return false
      } else if (atLeast != null && intValue < atLeast!!) {
        return false
      } else if (greaterThan != null && intValue <= greaterThan!!) {
        return false
      }
      return true
    }

    override fun description(): String {
      return when {
        exactly != null -> "must equal $exactly"
        lessThan != null -> "must be less than $lessThan"
        atMost != null -> "must be at most $atMost"
        atLeast != null -> "must be at least $atLeast"
        greaterThan != null -> "must be greater than $greaterThan"
        else -> "must be a number"
      }
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class LengthAssertion : Assertion<EditText>() {
    private var exactly: Int? = null
    private var lessThan: Int? = null
    private var atMost: Int? = null
    private var atLeast: Int? = null
    private var greaterThan: Int? = null

    /** Asserts the length is an exact (=) value. */
    fun exactly(length: Int) {
      exactly = length
    }

    /** Asserts the length is less than (<) a value. */
    fun lessThan(length: Int) {
      lessThan = length
    }

    /** Asserts the length is at most (<=) a value. */
    fun atMost(length: Int) {
      atMost = length
    }

    /** Asserts the length is at least (>=) a value. */
    fun atLeast(length: Int) {
      atLeast = length
    }

    /** Asserts the length is greater (>) than a value. */
    fun greaterThan(length: Int) {
      greaterThan = length
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

    override fun description(): String {
      return when {
        exactly != null -> "length must be exactly $exactly"
        lessThan != null -> "length must be less than $lessThan"
        atMost != null -> "length must be at most $atMost"
        atLeast != null -> "length must be at least $atLeast"
        greaterThan != null -> "length must be greater than $greaterThan"
        else -> "no length bound set"
      }
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class ContainsAssertion(private val text: String) : Assertion<EditText>() {
    private var ignoreCase: Boolean = false

    /** Case is ignored when checking if the input contains the string. */
    fun ignoreCase() {
      ignoreCase = true
    }

    override fun isValid(view: EditText): Boolean {
      return view.text()
          .contains(text, ignoreCase)
    }

    override fun description(): String {
      return "must contain \"$text\""
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class RegexAssertion(
    regexString: String,
    private val description: String
  ) : Assertion<EditText>() {
    private val regex = Regex(regexString)

    override fun isValid(view: EditText): Boolean {
      return view.text()
          .matches(regex)
    }

    override fun description(): String {
      return description
    }
  }
}

private fun EditText.text(): String {
  return text.toString()
}
