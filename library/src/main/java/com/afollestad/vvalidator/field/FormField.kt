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

package com.afollestad.vvalidator.field

import android.view.View
import androidx.annotation.CheckResult
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PRIVATE
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.assertion.Assertion
import com.afollestad.vvalidator.form.Condition
import com.afollestad.vvalidator.form.ConditionStack
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.form.FormMarker

typealias FieldBuilder<T> = T.() -> Unit
typealias OnError<V> = (view: V, errors: List<FieldError>) -> Unit
typealias OnValue<V, T> = (view: V, value: FieldValue<T>) -> Unit

/** @author Aidan Follestad (@afollestad) */
@FormMarker
abstract class FormField<F, V : View, T : Any>(
  /** The container which provides Context, view lookup, etc. */
  val container: ValidationContainer,
  /** The view that the field acts on. */
  val view: V,
  /** The name of the field, defaults to the resource ID entry name. */
  name: String? = null,
  /** Same as [name] but a string resource way of setting it. */
  @StringRes nameRes: Int? = null
) where F : FormField<F, V, T> {

  /** The name of the field. The name of the resource ID if not overridden by the user. */
  val name = getFormFieldName(container, name, nameRes, view.id)
  /** The parent [Form] that the field belongs to. */
  lateinit var form: Form

  private val assertions = mutableListOf<Assertion<V, *>>()
  private val conditionStack = ConditionStack()

  internal var onErrors: OnError<V>? = null
  internal var onValue: OnValue<V, T>? = null

  /** Adds an assertion to the field to be used during validation. */
  fun <T : Assertion<V, *>> assert(assertion: T): T {
    assertions.add(assertion.apply {
      this.container = this@FormField.container
      this.conditions = conditionStack.asList()
    })
    return assertion
  }

  /** Gets all assertions added to the field. */
  @CheckResult
  fun assertions(): List<Assertion<V, *>> = assertions

  /** Makes inner assertions optional based on a condition. */
  fun conditional(
    condition: Condition,
    builder: F.() -> Unit
  ): FormField<F, V, T> = apply {
    conditionStack.push(condition)
    @Suppress("UNCHECKED_CAST")
    (this as F).builder()
    conditionStack.pop()
  }

  /** Sets custom logic for displaying errors for the field. */
  fun onErrors(errors: OnError<V>?): FormField<F, V, T> = apply {
    this.onErrors = errors
  }

  /** Sets a callback that is invoked when this field becomes validated successfully and has a value. */
  fun onValue(onValue: OnValue<V, T>?): FormField<F, V, T> = apply {
    this.onValue = onValue
  }

  /** Validates the field, returning the result. */
  @CheckResult
  fun validate(silent: Boolean = false): FieldResult<T> {
    val result = FieldResult(
        name = name,
        value = obtainValue(view.id, name)
    )

    for (assertion in assertions()) {
      if (!assertion.isConditionMet()) {
        // If conditions aren't met, the assertion is ignored
        continue
      } else if (!assertion.isValid(view)) {
        val error = FieldError(
            id = view.id,
            name = name,
            description = assertion.description(),
            assertionType = assertion::class
        )
        result.addError(error)
      }
    }

    propagateErrors(
        silent = silent,
        errors = result.errors()
    )
    if (!silent) {
      propagateValue(result.value)
    }
    return result
  }

  /**
   * Generates a [FieldValue] instance of the field, should contain a snapshot of the field's
   * current value.
   */
  @CheckResult
  protected abstract fun obtainValue(
    id: Int,
    name: String
  ): FieldValue<T>?

  /**
   * The containing [Form] is built, and real time validation was requested. Begin observing
   * the view and running real-time validation.
   *
   * @param debounce The wanted delay between value changes and validation.
   */
  abstract fun startRealTimeValidation(debounce: Int)

  /** Sends errors through the field's error callback if there is one. */
  @VisibleForTesting(otherwise = PRIVATE)
  fun propagateErrors(
    silent: Boolean,
    errors: List<FieldError>
  ) {
    form.setFieldIsValid(this, errors.isEmpty())
    if (!silent) {
      onErrors?.invoke(view, errors)
    }
  }

  /** Sends value through the field's value callback if there is one. */
  @VisibleForTesting(otherwise = PRIVATE)
  fun propagateValue(value: FieldValue<T>?) {
    value?.let { onValue?.invoke(view, it) }
  }
}

private fun getFormFieldName(
  container: ValidationContainer,
  name: String?,
  nameRes: Int?,
  @IdRes id: Int
) = container.getString(nameRes) ?: (name ?: container.getFieldName(id))
