package com.example.fmveiculos.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CpfMask(private val editText: EditText) : TextWatcher {

    private var isUpdating: Boolean = false
    private val mask = "###.###.###-##"

    override fun afterTextChanged(s: Editable?) {
        if (isUpdating) {
            isUpdating = false
            return
        }

        isUpdating = true
        val str = s.toString().replace(Regex("[^\\d]"), "")
        val maskedStr = applyMask(mask, str)
        editText.setText(maskedStr)
        editText.setSelection(maskedStr.length)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    private fun applyMask(mask: String, str: String): String {
        val sb = StringBuilder()
        var i = 0
        for (m in mask.toCharArray()) {
            if (m != '#' && str.length > i) {
                sb.append(m)
                continue
            }
            try {
                sb.append(str[i])
            } catch (e: Exception) {
                break
            }
            i++
        }
        return sb.toString()
    }
}
