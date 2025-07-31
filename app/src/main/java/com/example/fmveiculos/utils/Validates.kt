package com.example.fmveiculos.utils

fun isValidCPF(cpf: String): Boolean {
    val cleanCpf = cpf.replace("[^0-9]".toRegex(), "")
    if (cleanCpf.isEmpty() || cleanCpf.length != 11 || cleanCpf.all { it == cleanCpf[0] }) {
        return false
    }

    fun calculateDigit(cpf: String, weights: IntArray): Int {
        val sum = weights.indices.sumOf { i ->
            cpf[i].digitToInt() * weights[i]
        }
        val remainder = sum % 11
        return if (remainder < 2) 0 else 11 - remainder
    }

    val weight1 = intArrayOf(10, 9, 8, 7, 6, 5, 4, 3, 2)
    val weight2 = intArrayOf(11, 10, 9, 8, 7, 6, 5, 4, 3, 2)

    val digit1 = calculateDigit(cleanCpf, weight1)
    val digit2 = calculateDigit(cleanCpf, weight2)

    return cleanCpf[9].digitToInt() == digit1 && cleanCpf[10].digitToInt() == digit2
}