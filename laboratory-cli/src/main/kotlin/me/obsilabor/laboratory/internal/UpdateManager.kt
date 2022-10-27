package me.obsilabor.laboratory.internal

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.rendering.TextStyles
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.obsilabor.laboratory.VERSION
import me.obsilabor.laboratory.config.Config
import me.obsilabor.laboratory.httpClient
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.SpinnerAnimation
import me.obsilabor.laboratory.utils.copyFolder
import me.obsilabor.laboratory.utils.downloadFileV2
import net.lingala.zip4j.ZipFile
import java.io.File
import kotlin.system.exitProcess

object UpdateManager {

    suspend fun updateOnWindows() {
        val laboratoryDir = File(System.getenv("LOCALAPPDATA"), "laboratory")
        val tempDir = File(laboratoryDir, "Temp")
        if (!tempDir.exists()) tempDir.mkdir()
        val archiveFile = File(tempDir, "laboratory-cli-jvm.zip")
        downloadFileV2(getDownloadURL(getLatestVersion()), archiveFile.toPath())
        val spinner = SpinnerAnimation("Extracting files")
        spinner.start()
        val zipArchive = ZipFile(archiveFile)
        zipArchive.extractAll(tempDir.absolutePath)
        zipArchive.close()
        archiveFile.delete()
        spinner.update("Tweaking files")
        val linuxExecutable = File(tempDir, "bin\\laboratory-cli-jvm")
        if (linuxExecutable.exists()) {
            linuxExecutable.delete()
        }
        File(tempDir, "laboratory-cli-jvm\\bin\\laboratory-cli.bat").renameTo(File(tempDir, "laboratory-cli-jvm\\bin\\laboratory.bat"))
        spinner.stop("Prepared for winupdater")
        downloadFileV2("https://raw.githubusercontent.com/mooziii/laboratory/dev/chemicae/.meta/winupdater.jar", laboratoryDir.resolve("winupdater.jar").toPath())
        ProcessBuilder("java", "-jar", "winupdater.jar").directory(laboratoryDir).inheritIO().start()
        exitProcess(0)
    }

    suspend fun updateOnLinux(sudoPassword: String) { // Only for manual installations, installation via package manager is preferred.
        val tempDir = File(System.getProperty("user.home"), "laboratory-update-temp")
        if (!tempDir.exists()) tempDir.mkdir()
        val archiveFile = File(tempDir, "laboratory-cli-jvm.zip")
        downloadFileV2(getDownloadURL(getLatestVersion()), archiveFile.toPath())
        val zipArchive = ZipFile(archiveFile)
        val destination = File(tempDir, "laboratory-cli-jvm")
        println(destination.absolutePath)
        if (!destination.exists()) destination.mkdir()
        zipArchive.extractAll(destination.absolutePath)
        zipArchive.close()
        archiveFile.delete()
        terminal.println("Launching bash session...")
        withContext(Dispatchers.IO) {
            val bash = ProcessBuilder("bash").start()
            terminal.println("> ${TextStyles.dim((TextColors.white on TextColors.black)("cd ${destination.absolutePath}"))}")
            bash.outputStream.write("cd ${destination.absolutePath}\n".toByteArray())
            terminal.println("> ${TextStyles.dim((TextColors.white on TextColors.black)("sudo -S cp -r laboratory-cli-jvm /usr/share/laboratory/"))}")
            bash.outputStream.write("echo $sudoPassword\\ | sudo -S cp -r laboratory-cli-jvm /usr/share/laboratory/\n".toByteArray())
            terminal.println("> ${TextStyles.dim((TextColors.white on TextColors.black)("sudo -S chmod +x /usr/share/laboratory/laboratory-cli-jvm/bin/laboratory-cli"))}")
            bash.outputStream.write("echo $sudoPassword\\ | sudo -S chmod +x /usr/share/laboratory/laboratory-cli-jvm/bin/laboratory-cli\n".toByteArray())
            terminal.println("> ${TextStyles.dim((TextColors.white on TextColors.black)("exit"))}")
            bash.outputStream.write("exit\n".toByteArray())
            bash.outputStream.flush()
            bash.waitFor()
            terminal.println(TextColors.brightGreen("Update completed!"))
        }
        tempDir.deleteRecursively()
    }

    suspend fun isUpdateAvailable(): Boolean {
        return getLatestVersion() != VERSION
    }

    suspend fun getLatestVersion(): String {
        return httpClient.get("https://raw.githubusercontent.com/mooziii/laboratory/${Config.userConfig.updateBranch}/.meta/version").bodyAsText()
    }

    suspend fun getDownloadURL(version: String): String {
        return httpClient.get("https://raw.githubusercontent.com/mooziii/laboratory/${Config.userConfig.updateBranch}/.meta/release-repo.txt").bodyAsText().replace("\${version}", version)
    }

}