package com.eusebiu.calculator

class Expression(private val expression: CharSequence
) {
    fun isNumber(value: Char): Boolean {
        return value in '0'..'9' || value == '.' || value == ')' || value == '('
    }
    val lastIndexOfNaN =  expression.indexOfLast {
       !isNumber(it)
   }
    fun toggleNegative(): String {
        val lastNumber = getLastNumber()
        if (!expression.toString().endsWith(")")) {
            return "${expression.slice(0..lastIndexOfNaN)}(-${lastNumber})"
        }
        return "${expression.slice(0..lastIndexOfNaN - 2)}${lastNumber.removePrefix("(-").removeSuffix(")")}"
    }
    fun getLastNumber(): CharSequence {
        val lastNumber: CharSequence =  if (lastIndexOfNaN != -1 && lastIndexOfNaN != expression.lastIndex)
            expression.slice((lastIndexOfNaN + 1)..expression.lastIndex)
        else
            expression
        if (lastNumber.contains(')')) {
          return  "(-$lastNumber"
        }
        return lastNumber
    }

   }
