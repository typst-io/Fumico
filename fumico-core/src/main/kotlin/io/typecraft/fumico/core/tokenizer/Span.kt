package io.typecraft.fumico.core.tokenizer

data class Span(val begin: LineColumn, val end: LineColumn) : Comparable<Span> {
    companion object {
        val Unknown = Span(LineColumn.Unknown, LineColumn.Unknown)
    }
    data class LineColumn(val pos: Int, val line: Int, val column: Int) : Comparable<LineColumn> {
        companion object {
            val Unknown = LineColumn(-1, -1, -1)
        }

        override fun compareTo(other: LineColumn): Int =
            pos.compareTo(other.pos)
    }

    override fun compareTo(other: Span): Int =
        begin.compareTo(other.begin)
}
