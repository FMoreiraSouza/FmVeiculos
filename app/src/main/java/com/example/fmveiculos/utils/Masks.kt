package com.example.fmveiculos.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlin.text.iterator

class Masks(private val editText: EditText) : TextWatcher {

    private var isUpdating = false
    private val mask = "###.###.###-##"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isUpdating) return

        isUpdating = true
        val unmasked = s.toString().replace("[^0-9]".toRegex(), "")
        val masked = StringBuilder()

        var i = 0
        for (m in mask) {
            if (m != '#' || i >= unmasked.length) break
            masked.append(unmasked[i])
            if (i < unmasked.length - 1 && (i == 2 || i == 5 || i == 8)) {
                masked.append(if (i == 8) '-' else '.')
            }
            i++
        }

        editText.setText(masked.toString())
        editText.setSelection(masked.length)
        isUpdating = false
    }
}