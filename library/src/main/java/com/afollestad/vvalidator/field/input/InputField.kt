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

package com.afollestad.vvalidator.field.input

import android.widget.EditText
import androidx.annotation.IdRes
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.assertion.CustomAssertion
import com.afollestad.vvalidator.assertion.EditTextContainsAssertion
import com.afollestad.vvalidator.assertion.EditTextEmailAssertion
import com.afollestad.vvalidator.assertion.EditTextLengthAtLeastAssertion
import com.afollestad.vvalidator.assertion.EditTextLengthAtMostAssertion
import com.afollestad.vvalidator.assertion.EditTextLengthExactlyAssertion
import com.afollestad.vvalidator.assertion.EditTextLengthGreaterThanAssertion
import com.afollestad.vvalidator.assertion.EditTextLengthLessThanAssertion
import com.afollestad.vvalidator.assertion.EditTextNotEmptyAssertion
import com.afollestad.vvalidator.assertion.EditTextNumberAssertion
import com.afollestad.vvalidator.assertion.EditTextRegexAssertion
import com.afollestad.vvalidator.assertion.EditTextUriAssertion
import com.afollestad.vvalidator.assertion.EditTextUrlAssertion
import com.afollestad.vvalidator.field.FormField
import com.afollestad.vvalidator.util.resName

/**
 * Represents an edit text field.
 *
 * @author Aidan Follestad (@afollestad)
 */
class InputField internal constructor(
  container: ValidationContainer,
  @IdRes override val id: Int,
  override val name: String
) : FormField<InputField, EditText>() {

  init {
    onErrors { view, errors ->
      view.error = errors.firstOrNull()
          ?.toString()
    }
  }

  override val view = container.findViewById<EditText>(id) ?: throw IllegalArgumentException(
      "Didn't find view by ID ${id.resName(container.context())} in $container"
  )

  /** Asserts that the input text is not empty. */
  fun isNotEmpty() = assert(EditTextNotEmptyAssertion())

  /**
   * A wrapper around [conditional] which applies inner assertions only if the
   * input text is not empty.
   */
  fun isEmptyOr(builder: InputField.() -> Unit) {
    conditional(
        condition = {
          view.text.trim()
              .isNotEmpty()
        },
        builder = builder
    )
  }

  /** Asserts that the input text is a valid URL. */
  fun isUrl() = assert(EditTextUrlAssertion())

  /** Asserts that the input text is a valid URI, optionally with a specific scheme. */
  fun isUri(vararg schemes: String = emptyArray()) = assert(EditTextUriAssertion(schemes))

  /** Asserts that the input text is a valid email address. */
  fun isEmail() = assert(EditTextEmailAssertion())

  /** Asserts that the input text is a valid number. */
  fun isNumber() = assert(EditTextNumberAssertion())

  /** Asserts that the input text is shorter (<) than a specified length. */
  fun lengthLessThan(length: Int) = assert(EditTextLengthLessThanAssertion(length))

  /** Asserts that the input text is at most (<=) a specified length. */
  fun lengthAtMost(length: Int) = assert(EditTextLengthAtMostAssertion(length))

  /** Asserts that the input text is an exact (==) length. */
  fun lengthExactly(length: Int) = assert(EditTextLengthExactlyAssertion(length))

  /** Asserts that the input text is at least (>=) a specified length. */
  fun lengthAtLeast(length: Int) = assert(EditTextLengthAtLeastAssertion(length))

  /** Asserts that the input text is greater (>) than a specified length. */
  fun lengthGreaterThan(length: Int) = assert(EditTextLengthGreaterThanAssertion(length))

  /** Asserts that the input text contains a string. */
  fun contains(text: String) = assert(EditTextContainsAssertion(text))

  /** Asserts that the input text matches a regular expression. */
  fun matches(
    description: String,
    regex: String
  ) = assert(
      EditTextRegexAssertion(
          regexString = regex,
          description = description
      )
  )

  /** Adds a custom inline assertion for the input field. */
  fun assert(
    description: String,
    matcher: (EditText) -> Boolean
  ) = assert(CustomAssertion(description, matcher))
}
