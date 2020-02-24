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
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.afollestad.vvalidator.field

import androidx.annotation.CheckResult
import androidx.annotation.IdRes
import com.afollestad.vvalidator.assertion.Assertion
import kotlin.reflect.KClass

/** @author Aidan Follestad (@afollestad) */
data class FieldError(
  /** The view ID that the error is for. */
  @IdRes val id: Int,
  /** The name of the field that the error is for. */
  val name: String,
  /** The defaultDescription of the error. */
  val description: String,
  /** The type of assertion that created this error. */
  val assertionType: KClass<out Assertion<*, *>>
) {
  /** Returns the [description]. */
  override fun toString() = description
}

/**
 * Holds the validation result of a single field.
 *
 * @author Aidan Follestad (@afollestad)
 */
class FieldResult<T : Any>(
  /** The name of the field. */
  val name: String,
  /** The snapshot value of the field when validation executed. */
  val value: FieldValue<T>?
) {
  private val errors = mutableListOf<FieldError>()

  /** Returns true if validation passed with no errors. */
  @CheckResult
  fun success() = errors.isEmpty()

  /** Returns true if validation failed with errors. */
  @CheckResult
  fun hasErrors() = errors.isNotEmpty()

  /** Returns errors that occurred during validation. */
  @CheckResult
  fun errors(): List<FieldError> = errors

  /** Appends an error to the validation result. */
  @CheckResult
  internal fun addError(error: FieldError) = errors.add(error)

  override fun toString(): String {
    return if (success()) {
      "Success"
    } else {
      "${errors.size} errors"
    }
  }
}
