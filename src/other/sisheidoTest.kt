package other

fun shisheidoTest() {

    class Line (
            var start: Int = 0,
            var end: Int = 0
    )

    class Segment (
            var filled: Boolean = false,
            var connected: Boolean = false
    )

    var lines = listOf( Line(2,4), Line(7,9), Line(5,8), Line(1,3))
    var out = arrayOf( Segment(false,false),
            Segment(false,false), Segment(false,false),
            Segment(false,false), Segment(false,false), Segment(false,false),
            Segment(false,false), Segment(false,false), Segment(false,false),
            Segment(false,false) )

    for(l in lines) {
        print("${l.start}, ${l.end}\n")

        for(i in l.start..l.end) {
            out[i].filled = true
            if (i != l.end) {
                out[i].connected = true
            }
        }
    }

    var curStart = 11
    var curEnd = 0
    for(i in 0..9) {
        println("${i}=${out[i].filled} - ${out[i].connected}")
        if(out[i].filled ) {
            if (i > curStart) {
                curStart = i
                println("Start: $i")
            }
            out[i].filled = false

            if (!out[i].connected) {
                curEnd=i
                println("segment: ${curStart}-${curEnd}")
                curStart=i
            }
        }

    }

}