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
@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.afollestad.vvalidator.form

import android.view.Menu
import android.widget.Button
import androidx.annotation.IdRes
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.field.FieldBuilder
import com.afollestad.vvalidator.field.FormField
import com.afollestad.vvalidator.field.checkable.CheckableField
import com.afollestad.vvalidator.field.input.InputField
import com.afollestad.vvalidator.field.input.InputLayoutField
import com.afollestad.vvalidator.field.seeker.SeekField
import com.afollestad.vvalidator.field.spinner.SpinnerField
import com.afollestad.vvalidator.util.resName

typealias FormBuilder = Form.() -> Unit

/** @author Aidan Follestad (@afollestad) */
class Form internal constructor(
  val container: ValidationContainer
) {
  private val fields = mutableListOf<FormField<*, *>>()

  /** Adds a field to the form. */
  fun appendField(field: FormField<*, *>) {
    fields.add(field)
  }

  /** Adds an input field, which must be a [android.widget.EditText]. */
  fun input(
    @IdRes id: Int,
    name: String? = null,
    builder: FieldBuilder<InputField>
  ) {
    val newField = InputField(
        container = container,
        id = id,
        name = container.getFieldName(id, name)
    )
    builder(newField)
    appendField(newField)
  }

  /**
   * Adds an input layout field, which must be a
   * [com.google.android.material.textfield.TextInputLayout]
   */
  fun inputLayout(
    @IdRes id: Int,
    name: String? = null,
    builder: FieldBuilder<InputLayoutField>
  ) {
    val newField = InputLayoutField(
        container = container,
        id = id,
        name = container.getFieldName(id, name)
    )
    builder(newField)
    appendField(newField)
  }

  /** Adds a dropdown field, which must be a [android.widget.Spinner]. */
  fun spinner(
    @IdRes id: Int,
    name: String? = null,
    builder: FieldBuilder<SpinnerField>
  ) {
    val newField = SpinnerField(
        container = container,
        id = id,
        name = container.getFieldName(id, name)
    )
    builder(newField)
    appendField(newField)
  }

  /**
   * Adds a checkable field, like a [android.widget.CheckBox], [android.widget.Switch], or
   * [android.widget.RadioButton].
   */
  fun checkable(
    @IdRes id: Int,
    name: String? = null,
    builder: FieldBuilder<CheckableField>
  ) {
    val newField = CheckableField(
        container = container,
        id = id,
        name = container.getFieldName(id, name)
    )
    builder(newField)
    appendField(newField)
  }

  /** Adds a AbsSeekBar field, like a [android.widget.SeekBar] or [android.widget.RatingBar]. */
  fun seeker(
    @IdRes id: Int,
    name: String? = null,
    builder: FieldBuilder<SeekField>
  ) {
    val newField = SeekField(
        container = container,
        id = id,
        name = container.getFieldName(id, name)
    )
    builder(newField)
    appendField(newField)
  }

  /** Validates all fields in the form. */
  fun validate(): FormResult {
    val finalResult = FormResult()
    for (field in fields) {
      val fieldResult = field.validate()
      finalResult += fieldResult
    }
    return finalResult
  }

  /**
   * Attaches the form to a button. When the button is clicked, validation is performed. If
   * validation passes, the given callback is invoked.
   */
  fun submitWith(
    @IdRes id: Int,
    onSubmit: (FormResult) -> Unit
  ) {
    val button = container.findViewById<Button>(id) ?: throw IllegalArgumentException(
        "View ${id.resName(container.context)} must be a Button."
    )
    button.setOnClickListener {
      val result = validate()
      if (result.success()) {
        onSubmit(result)
      }
    }
  }

  /**
   * Attaches the form to a menu item. When the item is clicked, validation is performed. If
   * validation passes, the given callback is invoked.
   */
  fun submitWith(
    menu: Menu,
    @IdRes itemId: Int,
    onSubmit: (FormResult) -> Unit
  ) {
    val item = menu.findItem(itemId) ?: throw IllegalArgumentException(
        "Didn't find item ${itemId.resName(container.context)} in the given Menu."
    )
    item.setOnMenuItemClickListener {
      val result = validate()
      if (result.success()) {
        onSubmit(result)
      }
      true
    }
  }
}
