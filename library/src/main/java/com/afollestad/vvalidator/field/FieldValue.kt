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
@file:Suppress("MemberVisibilityCanBePrivate")

package com.afollestad.vvalidator.field

import androidx.annotation.CheckResult
import androidx.annotation.IdRes
import kotlin.reflect.KClass

/**
 * Represents a snapshot of the value of a field (e.g. the text of an input filed).
 *
 * @author Bernat Borras (@alorma)
 */
open class FieldValue<T : Any>(
  /** The view ID that the value is for. */
  @IdRes open val id: Int,
  /** The name of the field that the value is for. */
  open val name: String,
  /** The value of that field */
  open val value: T,
  /** The class type of the value, e.g. a String, Int, etc. */
  open val valueType: KClass<T>
) {

  /** Coerces the value to a string. */
  @CheckResult
  fun asString(): String = value.toString()

  /** Coerces the value to a int. */
  @CheckResult
  fun asInt(): Int? = (value as? Int) ?: value.toString().toIntOrNull()

  /** Coerces the value to a long. */
  @CheckResult
  fun asLong(): Long? = (value as? Long) ?: value.toString().toLongOrNull()

  /** Coerces the value to a float. */
  @CheckResult
  fun asFloat(): Float? = (value as? Float) ?: value.toString().toFloatOrNull()

  /** Coerces the value to a double. */
  @CheckResult
  fun asDouble(): Double? = (value as? Double) ?: value.toString().toDoubleOrNull()

  /** Coerces the value to a boolean. */
  @CheckResult
  fun asBoolean(): Boolean = (value as? Boolean) ?: value.toString().toBoolean()

  /** Returns the [value] as a string. */
  override fun toString() = asString()
}

/** Represents the value of a numeric field such as a progress bar or seek bar. */
data class IntFieldValue(
  @IdRes override val id: Int,
  override val name: String,
  override val value: Int
) : FieldValue<Int>(id, name, value, Int::class)

/** Represents the value of a text field, like an EditText or TextInputLayout. */
data class TextFieldValue(
  @IdRes override val id: Int,
  override val name: String,
  override val value: CharSequence
) : FieldValue<CharSequence>(id, name, value, CharSequence::class)

/** Represents the value of a boolean field, like a Switch or Checkbox. */
data class BooleanFieldValue(
  @IdRes override val id: Int,
  override val name: String,
  override val value: Boolean
) : FieldValue<Boolean>(id, name, value, Boolean::class)
