package me.obsilabor.laboratory.internal

import io.ktor.client.request.*
import io.ktor.client.statement.*
import me.obsilabor.laboratory.VERSION
import me.obsilabor.laboratory.config.Config
import me.obsilabor.laboratory.httpClient
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.SpinnerAnimation
import me.obsilabor.laboratory.utils.copyFolder
import me.obsilabor.laboratory.utils.downloadFileV2
import net.lingala.zip4j.ZipFile
import java.io.File

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
        spinner.update("Copying files")
        copyFolder(tempDir.resolve("laboratory-cli-jvm\\bin").toPath(), laboratoryDir.resolve("bin").toPath())
        copyFolder(tempDir.resolve("laboratory-cli-jvm\\lib").toPath(), laboratoryDir.resolve("lib").toPath())
        spinner.update("Removing temporary files")
        tempDir.deleteRecursively()
        spinner.stop("done")
    }

    suspend fun updateOnLinux(sudoPassword: String) { // Only for manual installations, installation via package manager is preferred.
        val tempDir = File(System.getProperty("user.home"), "laboratory-update-temp")
        if (!tempDir.exists()) tempDir.mkdir()
        val archiveFile = File(tempDir, "laboratory-cli-jvm.zip")
        downloadFileV2(getDownloadURL(getLatestVersion()), archiveFile.toPath())
        val zipArchive = ZipFile(archiveFile)
        val destination = File(tempDir, "laboratory-cli-jvm")
        if (!destination.exists()) destination.createNewFile()
        zipArchive.extractAll(destination.absolutePath)
        zipArchive.close()
        archiveFile.delete()
        terminal.println("Launching bash session...")
        val bash = ProcessBuilder("bash").start()
        bash.outputStream.write("echo $sudoPassword\\ | sudo -S cp -r laboratory-cli-jvm /usr/share/laboratory/\n".toByteArray())
        bash.outputStream.write("echo $sudoPassword\\ | sudo -S chmod +x /usr/share/laboratory/laboratory-cli-jvm/bin/laboratory-cli\n".toByteArray())
        bash.outputStream.flush()
        bash.waitFor()
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