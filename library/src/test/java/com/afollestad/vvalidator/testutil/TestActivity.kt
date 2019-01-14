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
package com.afollestad.vvalidator.testutil

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.SeekBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

const val ID_INPUT = 1
const val ID_INPUT_LAYOUT = 2
const val ID_SPINNER = 3
const val ID_CHECKABLE = 4
const val ID_SEEKER = 5
const val ID_BUTTON = 6

/** @author Aidan Follestad (@afollestad) */
class TestActivity : AppCompatActivity() {

  lateinit var input: EditText
  lateinit var inputLayout: TextInputLayout
  lateinit var spinner: Spinner
  lateinit var checkable: CheckBox
  lateinit var seeker: SeekBar
  lateinit var button: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val root = LinearLayout(this).apply {
      orientation = VERTICAL
    }

    input = EditText(this).apply {
      id = ID_INPUT
    }
    root.addView(input)

    inputLayout = TextInputLayout(this).apply {
      id = ID_INPUT_LAYOUT
      addView(TextInputEditText(this@TestActivity))
    }
    root.addView(inputLayout)

    spinner = Spinner(this).apply {
      id = ID_SPINNER
      adapter = ArrayAdapter<String>(
          this@TestActivity,
          android.R.layout.simple_list_item_1,
          android.R.id.text1
      ).apply {
        addAll("One", "Two", "Three")
      }
    }
    root.addView(spinner)

    checkable = CheckBox(this).apply {
      id = ID_CHECKABLE
    }
    root.addView(checkable)

    seeker = SeekBar(this).apply {
      id = ID_SEEKER
      progress = 0
      max = 10
    }
    root.addView(seeker)

    button = Button(this).apply {
      id = ID_BUTTON
    }
    root.addView(button)

    setContentView(root)
  }
}
