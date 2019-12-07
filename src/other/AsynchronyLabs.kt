package other

fun encode(stringToEncode: String): String {

    var chars = stringToEncode.toCharArray()

    // change number order
    var s: Char
    var i: Int = 0
    var curNum: String = ""
    var collectingNumber = false
    var res = ""

    while( i < chars.size) {
        s = chars[i]

        if ( s.isDigit() ) {
          collectingNumber = true

          curNum += s
        }
        if (!s.isDigit() && collectingNumber) {
            res += curNum.reversed()

            curNum = ""
            collectingNumber = false
        } else if (!s.isDigit()){
            res += s
        }

        i++
    }

    // replace vowels with numbers (a->1, e->2, i->3, o->4, u->5)
    chars = res.toCharArray()
    i=0
    while( i < chars.size) {

    }

    return res
}


fun asynchronyLabs() {
    print( encode("abc1234def7890azx"))
}
