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
  private var onErrors: OnError<V>? = null

  /** Adds an assertion to the field to be used during validation. */
  fun assert(assertion: Assertion<V>) {
    assertions.add(assertion.apply { condition(currentCondition) })
  }

  /** Gets all assertions added to the field. */
  @CheckResult fun assertions(): List<Assertion<V>> = assertions

  /** Wraps assertions in a common condition. */
  fun conditional(
    condition: Condition,
    builder: F.() -> Unit
  ) {
    currentCondition = condition
    @Suppress("UNCHECKED_CAST")
    builder(this as F)
    currentCondition = null
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
      if (!assertion.conditionsMet()) {
        // If conditions aren't met, the assertion is ignored
        continue
      } else if (!assertion.isValid(v)) {
        val error = FieldError(
            id = id,
            name = name,
            description = assertion.description()
        )
        result.addError(error)
      }
    }

    propagateErrors(result.errors())
    return result
  }

  private fun propagateErrors(errors: List<FieldError>) {
    view?.let {
      onErrors?.invoke(it, errors)
    }
  }
}
