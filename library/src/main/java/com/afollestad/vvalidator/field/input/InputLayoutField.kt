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

import androidx.annotation.IdRes
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.assertion.CustomAssertion
import com.afollestad.vvalidator.assertion.InputLayoutContainsAssertion
import com.afollestad.vvalidator.assertion.InputLayoutEmailAssertion
import com.afollestad.vvalidator.assertion.InputLayoutLengthAtLeastAssertion
import com.afollestad.vvalidator.assertion.InputLayoutLengthAtMostAssertion
import com.afollestad.vvalidator.assertion.InputLayoutLengthExactlyAssertion
import com.afollestad.vvalidator.assertion.InputLayoutLengthGreaterThanAssertion
import com.afollestad.vvalidator.assertion.InputLayoutLengthLessThanAssertion
import com.afollestad.vvalidator.assertion.InputLayoutNotEmptyAssertion
import com.afollestad.vvalidator.assertion.InputLayoutNumberAssertion
import com.afollestad.vvalidator.assertion.InputLayoutRegexAssertion
import com.afollestad.vvalidator.assertion.InputLayoutUriAssertion
import com.afollestad.vvalidator.assertion.InputLayoutUrlAssertion
import com.afollestad.vvalidator.field.FormField
import com.afollestad.vvalidator.util.resName
import com.google.android.material.textfield.TextInputLayout

/**
 * Represents an edit text field.
 *
 * @author Aidan Follestad (@afollestad)
 */
class InputLayoutField internal constructor(
  container: ValidationContainer,
  @IdRes override val id: Int,
  override val name: String
) : FormField<InputLayoutField, TextInputLayout>() {

  init {
    onErrors { view, errors ->
      view.error = errors.firstOrNull()
          ?.toString()
    }
  }

  override val view = container.findViewById<TextInputLayout>(id) ?: throw IllegalArgumentException(
      "Didn't find view by ID ${id.resName(container.context())} in $container"
  )
  val editText = view.editText ?: throw IllegalStateException(
      "TextInputLayout ${id.resName(container.context())} should have a child EditText."
  )

  /** Asserts that the input text is not empty. */
  fun isNotEmpty() = assert(InputLayoutNotEmptyAssertion())

  /** Asserts that the input text is a valid URL. */
  fun isUrl() = assert(InputLayoutUrlAssertion())

  /** Asserts that the input text is a valid URI, optionally with a specific scheme. */
  fun isUri(vararg schemes: String = emptyArray()) = assert(InputLayoutUriAssertion(schemes))

  /** Asserts that the input text is a valid email address. */
  fun isEmail() = assert(InputLayoutEmailAssertion())

  /** Asserts that the input text is a valid number. */
  fun isNumber() = assert(InputLayoutNumberAssertion())

  /** Asserts that the input text is shorter (<) than a specified length. */
  fun lengthLessThan(length: Int) = assert(InputLayoutLengthLessThanAssertion(length))

  /** Asserts that the input text is at most (<=) a specified length. */
  fun lengthAtMost(length: Int) = assert(InputLayoutLengthAtMostAssertion(length))

  /** Asserts that the input text is an exact (==) length. */
  fun lengthExactly(length: Int) = assert(InputLayoutLengthExactlyAssertion(length))

  /** Asserts that the input text is at least (>=) a specified length. */
  fun lengthAtLeast(length: Int) = assert(InputLayoutLengthAtLeastAssertion(length))

  /** Asserts that the input text is greater (>) than a specified length. */
  fun lengthGreaterThan(length: Int) = assert(InputLayoutLengthGreaterThanAssertion(length))

  /** Asserts that the input text contains a string. */
  fun contains(text: String) = assert(InputLayoutContainsAssertion(text))

  /** Asserts that the input text matches a regular expression. */
  fun matches(
    description: String,
    regex: String
  ) = assert(
      InputLayoutRegexAssertion(
          regexString = regex,
          description = description
      )
  )

  /** Adds a custom inline assertion for the input layout field. */
  fun assert(
    description: String,
    matcher: (TextInputLayout) -> Boolean
  ) = assert(CustomAssertion(description, matcher))
}
