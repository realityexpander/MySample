package other

fun encode(stringToEncode: String): String {

    println(stringToEncode)

    val chars = stringToEncode.toLowerCase().toCharArray()

    var c: Char
    var i = 0
    var collectedNumReversed = ""
    var collectingNumbers = false
    var res = ""

    while( i < chars.size) {
        c = chars[i]

        if ( c.isDigit() ) {
            collectingNumbers = true
            collectedNumReversed = c + collectedNumReversed
        } else {

            if ( !c.isDigit() ) {
                if( collectingNumbers ) {
                    res += collectedNumReversed
                    collectedNumReversed = ""
                    collectingNumbers = false
                }
            }

            res += when (c) {
                'a' -> '1'
                'e' -> '2'
                'i' -> '3'
                'o' -> '4'
                'u' -> '5'
                'y' -> ' '
                ' ' -> 'y'
                in 'a'..'z'-> (c.toInt() - 1).toChar()
                else -> c
            }
        }

        i++
    }
    // finish the reversed number if still collecting numbers
    if( collectingNumbers ) {
        res += collectedNumReversed
    }

    return res
}


fun asynchronyLabs() {
//    print( encode("Hello World!"))
//    print( encode("abcde1234fghijklmnopqrstuvwxyz"))
    print( encode("The one from the village, FN2187"))
}
