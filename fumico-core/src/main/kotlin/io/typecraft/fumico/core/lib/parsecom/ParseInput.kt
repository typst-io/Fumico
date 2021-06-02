package io.typecraft.fumico.core.lib.parsecom

data class ParseInput(
    private val data: CharSequence,
    private val start: Int = 0,
    private val endExclusive: Int = data.length
) {
    val size: Int = endExclusive - start

    fun splitAt(index: Int): Pair<ParseInput, ParseInput> =
        Pair(
            ParseInput(data, start, start + index),
            ParseInput(data, start + index, endExclusive)
        )

    fun drop(count: Int): ParseInput =
        ParseInput(data, start + count, endExclusive)

    fun asSequence(): Sequence<Char> =
        data.asSequence().drop(start).take(endExclusive - start)
}
