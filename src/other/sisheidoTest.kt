package other

fun shiseidoTest() {

    class Line (
        var start: Int = 0,
        var end: Int = 0
    )

    class Segment (
        var filled: Boolean = false,
        var connected: Boolean = false
    )

//    var lines = listOf( Line(2,4), Line(7,9), Line(5,8), Line(1,3))
    var lines = listOf( Line(8,9), Line(3,4), Line(1,4), Line(7,9), Line(2,3), Line(6,8))
    var segments: Array<Segment> = Array(10) { _ -> Segment(filled = false, connected = false)}


    // Build the segments
    for(l in lines) {
//        print("line from ${l.start}, ${l.end}\n")

        for(i in l.start..l.end) {
            segments[i].filled = true
            if (i != l.end) {
                segments[i].connected = true
            }
        }
    }

    // Output the segments
    var curStart = segments.size
    var curEnd = 0
    for(i in segments.indices) {
//        println("$i = ${segments[i].filled}, ${segments[i].connected}")
        if(segments[i].filled ) {
            if (i < curStart) {
                curStart = i
            }

            if (!segments[i].connected) {
                curEnd=i
                println("$curStart - $curEnd")
                curStart = segments.size
            }
        }

    }

}