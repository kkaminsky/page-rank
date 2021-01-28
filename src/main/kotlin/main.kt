fun main(args: Array<String>) {

    val readAddr = "data.txt"
    val writeAddr = "data_rank_result.txt"
    val iterations = 10
    val dumpingFactor = 0.85
    val topK = 100
    val pagerank = PageRank()
    pagerank.initializeMap(readAddr)
    pagerank.rank(iterations, dumpingFactor)
    pagerank.showResults(topK)
    pagerank.saveRankedResults(writeAddr)

}

