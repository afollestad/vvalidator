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
package com.afollestad.vvalidator.assertion

import android.widget.AbsSeekBar

/** @author Aidan Follestad (@afollestad) */
internal class SeekProgressExactlyAssertion(
  private val progress: Int
) : Assertion<AbsSeekBar>() {
  override fun isValid(view: AbsSeekBar): Boolean {
    return view.progress == progress
  }

  override fun description(): String {
    return "selection should be exactly $progress"
  }
}

/** @author Aidan Follestad (@afollestad) */
internal class SeekProgressLessThanAssertion(
  private val ceil: Int
) : Assertion<AbsSeekBar>() {
  override fun isValid(view: AbsSeekBar): Boolean {
    return view.progress < ceil
  }

  override fun description(): String {
    return "selection should be less than $ceil"
  }
}

/** @author Aidan Follestad (@afollestad) */
internal class SeekProgressAtMostAssertion(
  private val ceil: Int
) : Assertion<AbsSeekBar>() {
  override fun isValid(view: AbsSeekBar): Boolean {
    return view.progress <= ceil
  }

  override fun description(): String {
    return "selection should be at most $ceil"
  }
}

/** @author Aidan Follestad (@afollestad) */
internal class SeekProgressAtLeastAssertion(
  private val floor: Int
) : Assertion<AbsSeekBar>() {
  override fun isValid(view: AbsSeekBar): Boolean {
    return view.progress >= floor
  }

  override fun description(): String {
    return "selection should be at least $floor"
  }
}

/** @author Aidan Follestad (@afollestad) */
internal class SeekProgressGreaterThanAssertion(
  private val floor: Int
) : Assertion<AbsSeekBar>() {
  override fun isValid(view: AbsSeekBar): Boolean {
    return view.progress > floor
  }

  override fun description(): String {
    return "selection should be greater than $floor"
  }
}
