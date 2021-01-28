data class PageRankNode(
    val identifier: String? = null,
    val rank: Double
): Comparable<PageRankNode> {
    override fun compareTo(other: PageRankNode): Int {
        return if (rank > other.rank) -1 else if (rank < other.rank) 1 else 0
    }
}