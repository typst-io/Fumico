package io.typecraft.parsecom

data class ParseInput<Meta, Token>(
    private val data: List<Token>,
    val meta: Meta,
    val debug: Boolean = false,
    val start: Int = 0,
    private val endExclusive: Int = data.size,
) {
    val size: Int = endExclusive - start

    fun splitAt(index: Int): Pair<ParseInput<Meta, Token>, ParseInput<Meta, Token>> =
        Pair(
            ParseInput(data, meta, debug, start, start + index),
            ParseInput(data, meta, debug, start + index, endExclusive)
        )

    fun withMeta(update: (Meta) -> Meta): ParseInput<Meta, Token> =
        this.copy(meta = update(meta))

    fun asSequence(): Sequence<Token> =
        data.asSequence().drop(start).take(endExclusive - start)

    fun peek(): Token? =
        data.asSequence().drop(start).firstOrNull()

    override fun toString(): String = asSequence().toList().toString()
}
