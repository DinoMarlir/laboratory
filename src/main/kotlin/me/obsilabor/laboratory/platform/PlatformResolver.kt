package me.obsilabor.laboratory.platform

import me.obsilabor.laboratory.platform.impl.*

object PlatformResolver {
    fun resolvePlatform(input: String): IPlatform {
        return when(input) {
            "papermc" -> PaperPlatform
            "quiltmc" -> QuiltPlatform
            "vanilla" -> VanillaPlatform
            "velocity" -> VelocityPlatform
            "waterfall" -> WaterfallPlatform
            else -> throw RuntimeException("Unknown platform $input")
        }
    }
}