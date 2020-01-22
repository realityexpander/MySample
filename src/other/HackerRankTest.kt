package other;

import java.util.Deque;

fun hackerRankTest() {

    // Complete the droppedRequests function below.
    fun droppedRequests(requestTime: Array<Int>): Int {

        var curTime = requestTime[0]
        var curTransactions = 0
        var totalDrop = 0
        var item = 0


        for(t in requestTime) {

            curTransactions += 1

            fun calcTimeWindow( timeWindow: Int, threshold: Int): Boolean {
                var numWindowTransactions = 0
                for( i in 0..item) {
                    var tItem = requestTime[i]

                    if (tItem >= t-(timeWindow-1) && tItem <= t) {
                        numWindowTransactions++

                    }
                    if (numWindowTransactions > threshold) {
                        return true
                    }
                }

                return false
            }

            when {
                calcTimeWindow(10, 20) -> totalDrop += 1
                calcTimeWindow(60, 60) -> totalDrop += 1
                t != curTime -> {
                    if(curTransactions > 3) {
                        totalDrop += 1
                    }

                    curTime = t
                    curTransactions = 0
                }
            }

            item += 1

        }

        return totalDrop

    }



//    val requestTime = arrayOf(1,1,1,1,2)  // answer 1
//    val requestTime = arrayOf(1,1,1,1,2,2,2,3,3,3,4,4,4,5,5,5,6,6,6,7,7)  // answer 2
    val requestTime = arrayOf(1,1,1,1,2,2,2,3,3,3,3,4,5,5,5,6,6,6,6,7,7,7,8,8,8,8,9,9,9,9,9,10,10,11,11,11,11,11,11,12,12,12,12,12,12,12,13,13,13,13,14,14,14,14,14,16,16,16,16,16
            ,16,17,17,17,18,18,18,18,18,18,18,18,19,19,19,19,19,19,19,20,20,20,20,20) // answer 67
    val res = droppedRequests(requestTime)

    println(res)


}
