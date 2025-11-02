package com.example.included.models

import androidx.annotation.DrawableRes

/**
 * Representa uma única carta no Jogo da Memória.
 *
 * @param id O ID do par de cartas. Duas cartas com o mesmo ID são um par.
 * @param imageResId O ID do recurso de imagem (Drawable) que será exibido.
 * @param cardIndex O índice único desta carta na lista total de cartas.
 * @param isFlipped Indica se a carta está virada para cima.
 * @param isMatched Indica se a carta já foi combinada e deve permanecer visível.
 */
data class MemoryCard(
    val id: Int,
    @DrawableRes val imageResId: Int,
    val cardIndex: Int,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)
