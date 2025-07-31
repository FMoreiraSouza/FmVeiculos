package com.example.fmveiculos.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class Masks(private val editText: EditText) : TextWatcher {

    private var isUpdating = false
    private val mask = "###.###.###-##"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isUpdating) return

        isUpdating = true
        val unmasked = s.toString().replace("[^0-9]".toRegex(), "").take(11) // Limita a 11 dígitos
        val masked = StringBuilder()

        var i = 0
        for (m in mask) {
            if (i >= unmasked.length) break
            if (m == '#') {
                masked.append(unmasked[i])
                i++
            } else {
                masked.append(m)
            }
        }

        editText.setText(masked.toString())
        editText.setSelection(masked.length)
        isUpdating = false
    }
}