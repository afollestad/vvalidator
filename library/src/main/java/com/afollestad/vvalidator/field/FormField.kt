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
import android.view.View.NO_ID
import androidx.annotation.CheckResult
import androidx.annotation.IdRes
import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PRIVATE
import com.afollestad.vvalidator.assertion.Assertion

typealias FieldBuilder<T> = T.() -> Unit

typealias Condition = () -> Boolean

typealias OnError<V> = (view: V, errors: List<FieldError>) -> Unit

/** @author Aidan Follestad (@afollestad) */
abstract class FormField<F, V> where F : FormField<F, V>, V : View {

  /** The view ID of the field. */
  @IdRes open val id: Int = NO_ID
  /** The name of the field, defaults to the resource ID entry name. */
  open val name: String = ""
  open val view: V? = null

  private val assertions = mutableListOf<Assertion<V>>()
  private var currentCondition: Condition? = null
  @VisibleForTesting(otherwise = PRIVATE) var onErrors: OnError<V>? = null

  /** Adds an assertion to the field to be used during validation. */
  @CheckResult fun <T : Assertion<V>> assert(assertion: T): T {
    assertions.add(assertion.apply { condition = currentCondition })
    return assertion
  }

  /** Gets all assertions added to the field. */
  @CheckResult fun assertions(): List<Assertion<V>> = assertions

  /** Makes inner assertions optional based on a condition. */
  fun conditional(
    condition: Condition,
    builder: F.() -> Unit
  ) {
    val previousCondition = this.currentCondition
    currentCondition = condition
    @Suppress("UNCHECKED_CAST")
    builder(this as F)
    // We restore the previous condition to correctly support nesting.
    currentCondition = previousCondition
  }

  /** Sets custom logic for displaying errors for the field. */
  fun onErrors(errors: OnError<V>?) {
    this.onErrors = errors
  }

  /** Validates the field, returning the result. */
  @CheckResult fun validate(): FieldResult {
    val v = view ?: throw IllegalStateException("View is null.")
    val result = FieldResult()

    for (assertion in assertions()) {
      if (!assertion.isConditionMet()) {
        // If conditions aren't met, the assertion is ignored
        continue
      } else if (!assertion.isValid(v)) {
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

  @VisibleForTesting(otherwise = PRIVATE)
  fun propagateErrors(errors: List<FieldError>) {
    view?.let {
      onErrors?.invoke(it, errors)
    }
  }
}
