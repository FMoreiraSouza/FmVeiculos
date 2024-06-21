package com.example.fmveiculos.utils

fun isValidCPF(cpf: String): Boolean {
    // Limpar caracteres especiais e espaços
    val cleanedCPF = cpf.replace("[^\\d]".toRegex(), "")

    // Verificar se o CPF possui 11 dígitos
    if (cleanedCPF.length != 11)
        return false

    // Verificar se todos os dígitos são iguais (caso trivial de CPF inválido)
    if (cleanedCPF == "00000000000" || cleanedCPF == "11111111111" ||
        cleanedCPF == "22222222222" || cleanedCPF == "33333333333" ||
        cleanedCPF == "44444444444" || cleanedCPF == "55555555555" ||
        cleanedCPF == "66666666666" || cleanedCPF == "77777777777" ||
        cleanedCPF == "88888888888" || cleanedCPF == "99999999999")
        return false

    // Calcula o primeiro dígito verificador
    var sum = 0
    var weight = 10
    for (i in 0 until 9) {
        sum += Integer.parseInt(cleanedCPF[i].toString()) * weight
        weight--
    }
    var digit = 11 - sum % 11
    if (digit >= 10) digit = 0
    if (digit != Integer.parseInt(cleanedCPF[9].toString()))
        return false

    // Calcula o segundo dígito verificador
    sum = 0
    weight = 11
    for (i in 0 until 10) {
        sum += Integer.parseInt(cleanedCPF[i].toString()) * weight
        weight--
    }
    digit = 11 - sum % 11
    if (digit >= 10) digit = 0
    if (digit != Integer.parseInt(cleanedCPF[10].toString()))
        return false

    return true
}
