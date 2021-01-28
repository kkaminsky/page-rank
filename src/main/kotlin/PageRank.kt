import java.io.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.system.exitProcess

class PageRank {
    private var map: MutableMap<String?, ArrayList<MapEntry>>? = HashMap()
    private var rankedList: List<PageRankNode>? = null

    //Инициализация графа переходов из файла
    fun initializeMap(addr: String) {
        var inputStream: BufferedReader? = null
        var line: String? = null

        inputStream = BufferedReader(FileReader(addr))
        line = inputStream.readLine()
        var node1: String? = null
        var node2: String? = null
        var edgeWeight = 0.0
        while (line != null) {
            line = line.trim { it <= ' ' }
            val entries = line.split("\t".toRegex()).toTypedArray()
            node1 = entries[0]
            node2 = entries[1]
            edgeWeight = entries[2].toDouble()
            addEntry(node1, node2, edgeWeight)
            addEntry(node2, node1, edgeWeight)
            line = inputStream.readLine()
        }
        inputStream.close()

    }


    private fun addEntry(node1: String?, node2: String?, edgeWeight: Double) {
        val mapEntry = MapEntry(node2, edgeWeight)
        if (map!!.containsKey(node1)) {
            if (!map!![node1]!!.contains(mapEntry)) {
                map!![node1]!!.add(mapEntry)
            }
        } else {
            val list = ArrayList<MapEntry>()
            list.add(mapEntry)
            map!![node1] = list
        }
    }

    fun rank(iterations: Int, dampingFactor: Double) {
        var lastRanking = HashMap<String?, Double>()
        val nextRanking = HashMap<String?, Double>()
        val startRank = 1.0 / map!!.size
        for (key in map!!.keys) {
            lastRanking[key] = startRank
        }
        val dampingFactorComplement = 1.0 - dampingFactor
        for (times in 0 until iterations) {
            for (key in map!!.keys) {
                var totalWeight = 0.0
                for (entry in map!![key]!!) totalWeight += entry.edgeWeight * lastRanking[entry.identifier]!! / map!![entry.identifier]!!.size
                val nextRank = (dampingFactorComplement
                        + dampingFactor * totalWeight)
                nextRanking[key] = nextRank
            }
            lastRanking = nextRanking
        }
        rankedList = pageRankVector(lastRanking)
    }

    fun saveRankedResults(writeAddr: String) {
        val file = File(writeAddr)
        var writer: PrintWriter? = null
        try {
            writer = PrintWriter(file)
            for ((identifier, rank) in rankedList!!) {
                writer.println(identifier + "\t" + rank)
            }
            writer.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    fun showResults(topK: Int) {
        println("--------------------------------------")
        println("     node     |          rank         ")
        println("--------------------------------------")
        var startIndex = 0
        for (i in 0 until topK) {
            var key = rankedList!![startIndex].identifier
            while (key!!.startsWith("N")) //this is a pattern node
            {
                startIndex++
                if (startIndex > rankedList!!.size) {
                    println("number of T nodes < top-K")
                    exitProcess(0)
                }
                key = rankedList!![startIndex].identifier
            }


            val rank = rankedList!![startIndex].rank
            println("     $key    |     $rank     ")
            startIndex++
        }
    }

    private fun pageRankVector(LastRanking: HashMap<String?, Double>): List<PageRankNode> {
        val nodeList: MutableList<PageRankNode> = LinkedList()
        for (identifier in LastRanking.keys) {
            val node = PageRankNode(identifier, LastRanking[identifier]!!)
            nodeList.add(node)
        }
        nodeList.sort()
        return nodeList
    }


}