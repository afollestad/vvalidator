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
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PRIVATE
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.assertion.Assertion
import com.afollestad.vvalidator.form.Condition
import com.afollestad.vvalidator.form.ConditionStack

typealias FieldBuilder<T> = T.() -> Unit

typealias OnError<V> = (view: V, errors: List<FieldError>) -> Unit

/** @author Aidan Follestad (@afollestad) */
abstract class FormField<F, V>(
  /** The container which provides Context, view lookup, etc. */
  val container: ValidationContainer,
  /** The view ID of the field. */
  val id: Int,
  /** The name of the field, defaults to the resource ID entry name. */
  name: String? = null,
  /** Same as [name] but a string resource way of setting it. */
  @StringRes nameRes: Int? = null
) where F : FormField<F, V>, V : View {

  /** The name of the field. The name of the resource ID if not overridden by the user. */
  val name = container.getString(nameRes) ?: (name ?: container.getFieldName(id))
  /** The view the field acts on. */
  val view = container.getViewOrThrow<V>(id)

  private val assertions = mutableListOf<Assertion<V, *>>()
  private var conditionStack = ConditionStack()

  internal var onErrors: OnError<V>? = null

  /** Adds an assertion to the field to be used during validation. */
  @CheckResult fun <T : Assertion<V, *>> assert(assertion: T): T {
    assertions.add(assertion.apply {
      this.container = this@FormField.container
      this.conditions = conditionStack.asList()
    })
    return assertion
  }

  /** Gets all assertions added to the field. */
  @CheckResult fun assertions(): List<Assertion<V, *>> = assertions

  /** Makes inner assertions optional based on a condition. */
  fun conditional(
    condition: Condition,
    builder: F.() -> Unit
  ) {
    conditionStack.push(condition)
    @Suppress("UNCHECKED_CAST")
    builder(this as F)
    conditionStack.pop()
  }

  /** Sets custom logic for displaying errors for the field. */
  fun onErrors(errors: OnError<V>?) {
    this.onErrors = errors
  }

  /** Validates the field, returning the result. */
  @CheckResult fun validate(): FieldResult {
    val result = FieldResult()

    for (assertion in assertions()) {
      if (!assertion.isConditionMet()) {
        // If conditions aren't met, the assertion is ignored
        continue
      } else if (!assertion.isValid(view)) {
        val error = FieldError(
            id = id,
            name = name,
            description = assertion.description(),
            assertionType = assertion::class
        )
        result.addError(error)
      }
    }

    propagateErrors(result.errors())
    return result
  }

  /** Sends errors through the field's error callback if there is one. */
  @VisibleForTesting(otherwise = PRIVATE)
  fun propagateErrors(errors: List<FieldError>) {
    onErrors?.invoke(view, errors)
  }
}
