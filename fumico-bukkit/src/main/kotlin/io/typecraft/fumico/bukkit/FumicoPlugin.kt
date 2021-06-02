package io.typecraft.fumico.bukkit

import kr.entree.spigradle.annotations.SpigotPlugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File

// This class automatically loaded by Bukkit
@Suppress("unused")
@SpigotPlugin
class MwmPlugin : JavaPlugin {
    companion object {
        val INSTANCE by lazy {
            getPlugin(MwmPlugin::class.java)
        }
    }

    // For the Bukkit's System
    @Suppress("unused")
    constructor() : super()

    // For the MockBukkit
    @Suppress("unused", "ProtectedInFinal")
    protected constructor(
        loader: JavaPluginLoader,
        description: PluginDescriptionFile,
        dataFolder: File,
        file: File
    ) : super(loader, description, dataFolder, file)
}