package com.grooble.mathvoice.speech

class LookupTable {
    val multiplication : Map<String, List<String>> = mapOf(
                            Pair("9", listOf("く", "きゅう","九")),
                            Pair("9x9=81", listOf("くくはちじゅいち","くく八十一")),
                            Pair("2x9=18", listOf("にくじゅうはち")),
                            Pair("3x9=27", listOf("さんくはにじゅうしち")),
                            Pair("4x9=36", listOf("しくさんじゅうろく")),
                            Pair("5x9=45", listOf("ごくしじゅうご")),
                            Pair("6x9=54", listOf("ろくくごじゅうし")),
                            Pair("7x9=63", listOf("しちくろくじゅうさん")),
                            Pair("8x9=72", listOf("はくしちじゅうに"))
    )
}