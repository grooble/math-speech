package com.grooble.mathvoice


import java.util.Arrays

internal class NumberParser {

    private var isValidInput = true
    private var result: Long = 0
    private var finalResult: Long = 0
    private val allowedStrings = Arrays.asList(
        "zero", "one", "two", "three", "four", "five", "six", "seven",
        "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen",
        "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty",
        "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety",
        "hundred", "thousand", "million", "billion", "trillion"
    )

    fun parseInput(`in`: String): Long {
        var input: String? = "One hundred two thousand and thirty four"

        if (input != null && input.length > 0) {
            input = input.replace("-".toRegex(), " ")
            input = input.toLowerCase().replace(" and".toRegex(), " ")
            val splittedParts = input.trim { it <= ' ' }.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            for (str in splittedParts) {
                if (!allowedStrings.contains(str)) {
                    isValidInput = false
                    println("Invalid word found : $str")
                    break
                }
            }
            if (isValidInput) {
                for (str in splittedParts) {
                    if (str.equals("zero", ignoreCase = true)) {
                        result += 0
                    } else if (str.equals("one", ignoreCase = true)) {
                        result += 1
                    } else if (str.equals("two", ignoreCase = true)) {
                        result += 2
                    } else if (str.equals("three", ignoreCase = true)) {
                        result += 3
                    } else if (str.equals("four", ignoreCase = true)) {
                        result += 4
                    } else if (str.equals("five", ignoreCase = true)) {
                        result += 5
                    } else if (str.equals("six", ignoreCase = true)) {
                        result += 6
                    } else if (str.equals("seven", ignoreCase = true)) {
                        result += 7
                    } else if (str.equals("eight", ignoreCase = true)) {
                        result += 8
                    } else if (str.equals("nine", ignoreCase = true)) {
                        result += 9
                    } else if (str.equals("ten", ignoreCase = true)) {
                        result += 10
                    } else if (str.equals("eleven", ignoreCase = true)) {
                        result += 11
                    } else if (str.equals("twelve", ignoreCase = true)) {
                        result += 12
                    } else if (str.equals("thirteen", ignoreCase = true)) {
                        result += 13
                    } else if (str.equals("fourteen", ignoreCase = true)) {
                        result += 14
                    } else if (str.equals("fifteen", ignoreCase = true)) {
                        result += 15
                    } else if (str.equals("sixteen", ignoreCase = true)) {
                        result += 16
                    } else if (str.equals("seventeen", ignoreCase = true)) {
                        result += 17
                    } else if (str.equals("eighteen", ignoreCase = true)) {
                        result += 18
                    } else if (str.equals("nineteen", ignoreCase = true)) {
                        result += 19
                    } else if (str.equals("twenty", ignoreCase = true)) {
                        result += 20
                    } else if (str.equals("thirty", ignoreCase = true)) {
                        result += 30
                    } else if (str.equals("forty", ignoreCase = true)) {
                        result += 40
                    } else if (str.equals("fifty", ignoreCase = true)) {
                        result += 50
                    } else if (str.equals("sixty", ignoreCase = true)) {
                        result += 60
                    } else if (str.equals("seventy", ignoreCase = true)) {
                        result += 70
                    } else if (str.equals("eighty", ignoreCase = true)) {
                        result += 80
                    } else if (str.equals("ninety", ignoreCase = true)) {
                        result += 90
                    } else if (str.equals("hundred", ignoreCase = true)) {
                        result *= 100
                    } else if (str.equals("thousand", ignoreCase = true)) {
                        result *= 1000
                        finalResult += result
                        result = 0
                    } else if (str.equals("million", ignoreCase = true)) {
                        result *= 1000000
                        finalResult += result
                        result = 0
                    } else if (str.equals("billion", ignoreCase = true)) {
                        result *= 1000000000
                        finalResult += result
                        result = 0
                    } else if (str.equals("trillion", ignoreCase = true)) {
                        result *= 1000000000000L
                        finalResult += result
                        result = 0
                    }
                }

                finalResult += result
                result = 0
                println(finalResult)
            }
        }
        return finalResult
    }

}