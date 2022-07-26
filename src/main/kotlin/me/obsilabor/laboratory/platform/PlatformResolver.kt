package me.obsilabor.laboratory.platform

import me.obsilabor.laboratory.platform.impl.PaperPlatform

object PlatformResolver {
    fun resolvePlatform(input: String): IPlatform {
        return when(input) {
            "papermc" -> PaperPlatform
            else -> throw RuntimeException("Unknown platform $input")
        }
    }
}