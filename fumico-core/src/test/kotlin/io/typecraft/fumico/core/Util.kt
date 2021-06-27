package io.typecraft.fumico.core

import io.typecraft.fumico.core.tokenizer.Span
import io.typecraft.fumico.core.tokenizer.Token

operator fun Token.Kind.invoke(text: String): Token = Token(this, text, Span.Unknown)