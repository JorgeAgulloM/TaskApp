package com.softyorch.taskapp.utils.extensions

import java.security.SecureRandom

fun Int.random(): Int {
    val random = SecureRandom()
    random.setSeed(random.generateSeed(this))
    return random.nextInt(this - 1)
}