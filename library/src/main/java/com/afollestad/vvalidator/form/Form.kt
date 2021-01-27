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
import android.view.View
import android.widget.AbsSeekBar
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.CheckResult
import androidx.annotation.IdRes
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.checkAttached
import com.afollestad.vvalidator.field.FieldBuilder
import com.afollestad.vvalidator.field.FormField
import com.afollestad.vvalidator.field.checkable.CheckableField
import com.afollestad.vvalidator.field.input.InputField
import com.afollestad.vvalidator.field.input.InputLayoutField
import com.afollestad.vvalidator.field.seeker.SeekField
import com.afollestad.vvalidator.field.spinner.SpinnerField
import com.afollestad.vvalidator.getViewOrThrow
import com.google.android.material.textfield.TextInputLayout

typealias FormBuilder = Form.() -> Unit
typealias GenericFormField = FormField<*, *, *>

@DslMarker
annotation class FormMarker

/** @author Aidan Follestad (@afollestad) */
@FormMarker
class Form constructor(validationContainer: ValidationContainer) {
  var container: ValidationContainer? = validationContainer

  internal var useRealTimeValidation: Boolean = false
  internal var realTimeValidationDebounce: Int = -1
  internal var realTimeDisableSubmit: Boolean = false

  internal var realTimeValidMap: MutableMap<GenericFormField, Boolean>? = null
  internal var submitWith: SubmitWrapper? = null

  private val fields = mutableListOf<GenericFormField>()

  /**
   * Used by [FormField] in real time validation mode to tell the parent [Form] whether or not
   * validation is passing. Aids in decision making when [realTimeDisableSubmit] is true.
   */
  internal fun setFieldIsValid(
    field: GenericFormField,
    valid: Boolean
  ) {
    realTimeValidMap?.put(field, valid)
    submitWith?.isEnabled = !(realTimeValidMap?.any { !it.value } ?: false)
  }

  /** Adds a field to the form. */
  @CheckResult fun appendField(field: GenericFormField): GenericFormField {
    fields.add(field.apply {
      this.form = this@Form
    })
    return field
  }

  /** Retrieves fields that have been added to the form. */
  fun getFields(): List<GenericFormField> = fields

  /**
   * Enables real-time validation. Views will be observed in real time rather than
   * waiting for Form submission.
   *
   * @param debounce Must be >= 0. Sets a custom delay between a user pausing
   *  interaction with a view and the view being validated. Defaults to a half second (500).
   *  NOTE that certain fields can choose to IGNORE this, like a checkbox.
   * @param disableSubmit When true, the submitWith view is disabled when real time validation
   *  fails. It's re-enabled when it passes.
   */
  fun useRealTimeValidation(
    debounce: Int = 500,
    disableSubmit: Boolean = false
  ): Form {
    require(debounce >= 0) { "Debounce must be >= 0." }
    useRealTimeValidation = true
    realTimeValidationDebounce = debounce
    if (disableSubmit) {
      realTimeDisableSubmit = true
      realTimeValidMap = mutableMapOf()
    }
    return this
  }

  /** Adds an input field, which must be a [android.widget.EditText]. */
  fun input(
    view: EditText,
    name: String? = null,
    optional: Boolean = false,
    builder: FieldBuilder<InputField>
  ): GenericFormField {
    val newField = InputField(
        container = container.checkAttached(),
        view = view,
        name = name
    )
    if (optional) {
      newField.isEmptyOr(builder)
    } else {
      newField.builder()
    }
    return appendField(newField)
  }

  /** Adds an input field, which must be a [android.widget.EditText]. */
  fun input(
    @IdRes id: Int,
    name: String? = null,
    optional: Boolean = false,
    builder: FieldBuilder<InputField>
  ) = input(
      view = container.getViewOrThrow(id),
      name = name,
      optional = optional,
      builder = builder
  )

  /**
   * Adds an input layout field, which must be a
   * [com.google.android.material.textfield.TextInputLayout]
   */
  fun inputLayout(
    view: TextInputLayout,
    name: String? = null,
    optional: Boolean = false,
    builder: FieldBuilder<InputLayoutField>
  ): GenericFormField {
    val newField = InputLayoutField(
        container = container.checkAttached(),
        view = view,
        name = name
    )
    if (optional) {
      newField.isEmptyOr(builder)
    } else {
      newField.builder()
    }
    return appendField(newField)
  }

  /**
   * Adds an input layout field, which must be a
   * [com.google.android.material.textfield.TextInputLayout]
   */
  fun inputLayout(
    @IdRes id: Int,
    name: String? = null,
    optional: Boolean = false,
    builder: FieldBuilder<InputLayoutField>
  ) = inputLayout(
      view = container.getViewOrThrow(id),
      name = name,
      optional = optional,
      builder = builder
  )

  /** Adds a dropdown field, which must be a [android.widget.Spinner]. */
  fun spinner(
    view: Spinner,
    name: String? = null,
    builder: FieldBuilder<SpinnerField>
  ): GenericFormField {
    val newField = SpinnerField(
        container = container.checkAttached(),
        view = view,
        name = name
    )
    newField.builder()
    return appendField(newField)
  }

  /** Adds a dropdown field, which must be a [android.widget.Spinner]. */
  fun spinner(
    @IdRes id: Int,
    name: String? = null,
    builder: FieldBuilder<SpinnerField>
  ) = spinner(
      view = container.getViewOrThrow(id),
      name = name,
      builder = builder
  )

  /**
   * Adds a checkable field, like a [android.widget.CheckBox], [android.widget.Switch], or
   * [android.widget.RadioButton].
   */
  fun checkable(
    view: CompoundButton,
    name: String? = null,
    builder: FieldBuilder<CheckableField>
  ): GenericFormField {
    val newField = CheckableField(
        container = container.checkAttached(),
        view = view,
        name = name
    )
    newField.builder()
    return appendField(newField)
  }

  /**
   * Adds a checkable field, like a [android.widget.CheckBox], [android.widget.Switch], or
   * [android.widget.RadioButton].
   */
  fun checkable(
    @IdRes id: Int,
    name: String? = null,
    builder: FieldBuilder<CheckableField>
  ) = checkable(
      view = container.getViewOrThrow(id),
      name = name,
      builder = builder
  )

  /** Adds a AbsSeekBar field, like a [android.widget.SeekBar] or [android.widget.RatingBar]. */
  fun seeker(
    view: AbsSeekBar,
    name: String? = null,
    builder: FieldBuilder<SeekField>
  ): GenericFormField {
    val newField = SeekField(
        container = container.checkAttached(),
        view = view,
        name = name
    )
    newField.builder()
    return appendField(newField)
  }

  /** Adds a AbsSeekBar field, like a [android.widget.SeekBar] or [android.widget.RatingBar]. */
  fun seeker(
    @IdRes id: Int,
    name: String? = null,
    builder: FieldBuilder<SeekField>
  ) = seeker(
      view = container.getViewOrThrow(id),
      name = name,
      builder = builder
  )

  /** Validates all fields in the form. */
  fun validate(silent: Boolean = false): FormResult {
    val finalResult = FormResult()
    for (field in fields) {
      val fieldResult = field.validate(silent = silent)
      finalResult += fieldResult
    }
    return finalResult
  }

  /**
   * Attaches the form to a view. When the view is clicked, validation is performed. If
   * validation passes, the given callback is invoked.
   */
  fun submitWith(
    view: View,
    onSubmit: (FormResult) -> Unit
  ) {
    view.setOnClickListener {
      val result = validate()
      if (result.success()) {
        onSubmit(result)
      }
    }
    this.submitWith = SubmitWrapper(view = view)
  }

  /**
   * Attaches the form to a view. When the view is clicked, validation is performed. If
   * validation passes, the given callback is invoked.
   */
  fun submitWith(
    @IdRes id: Int,
    onSubmit: (FormResult) -> Unit
  ) {
    val currentContainer = container.checkAttached()
    val view = currentContainer.findViewById<View>(id) ?: error(
        "Unable to find view ${currentContainer.getFieldName(id)} in your container."
    )
    submitWith(view, onSubmit)
    this.submitWith = SubmitWrapper(view = view)
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
    val currentContainer = container.checkAttached()
    val item = menu.findItem(itemId) ?: error(
        "Didn't find item ${currentContainer.getFieldName(itemId)} in the given Menu."
    )
    item.setOnMenuItemClickListener {
      val result = validate()
      if (result.success()) {
        onSubmit(result)
      }
      true
    }
    this.submitWith = SubmitWrapper(menuItem = item)
  }

  /** Signals that the form is finished being built. */
  @CheckResult fun start(): Form {
    if (useRealTimeValidation) {
      // Notify all fields that we are using real time validation and they should start observing.
      fields.forEach { it.startRealTimeValidation(realTimeValidationDebounce) }
      // Immediately do a validation to see if the submit button should initially be disabled.
      if (realTimeDisableSubmit && submitWith?.isEnabled == true) {
        // Silent so that we do not show errors visually.
        validate(silent = true)
      }
    }
    return this
  }

  /** Destroys the form by removing all fields and freeing up references. */
  internal fun destroy() {
    fields.clear()
    container = null
  }
}
