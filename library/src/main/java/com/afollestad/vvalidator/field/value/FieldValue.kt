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
package com.afollestad.vvalidator.field.value

import androidx.annotation.IdRes

/** @author Bernat Borras (@alorma) */
open class FieldValue<T>(
  /** The view ID that the value is for. */
  @IdRes open val id: Int,
  /** The name of the field that the value is for. */
  open val name: String,
  /** The value of that field */
  open val value: T
) {
    /** Returns the [value]. */
    override fun toString() = value.toString()
}

data class NumericFieldValue<T : Number>(
  @IdRes override val id: Int,
  override val name: String,
  override val value: T
) : FieldValue<T>(id, name, value)

data class TextFieldValue(
  @IdRes override val id: Int,
  override val name: String,
  override val value: CharSequence
) : FieldValue<CharSequence>(id, name, value)

data class BooleanFieldValue(
  @IdRes override val id: Int,
  override val name: String,
  override val value: Boolean
) : FieldValue<Boolean>(id, name, value)
