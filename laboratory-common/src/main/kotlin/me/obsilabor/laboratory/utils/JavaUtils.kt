package me.obsilabor.laboratory.utils

import java.io.File

fun searchForJavaInstallations(): List<File> {
    val list = arrayListOf<File>()
    val dotJdksFolder = File(System.getProperty("user.home"), ".jdks")
    if (dotJdksFolder.exists()) {
        list.addAll(dotJdksFolder.listFiles().filter { it.isDirectory })
    }
    when(OperatingSystem.current) {
        OperatingSystem.WINDOWS -> {
            val eclipseAdoptiumFolder = File("C:\\Program Files\\Eclipse Adoptium")
            if (eclipseAdoptiumFolder.exists()) {
                list.addAll(eclipseAdoptiumFolder.listFiles().filter { it.isDirectory })
            }
            val eclipseFoundationFolder = File("C:\\Program Files\\Eclipse Foundation")
            if (eclipseFoundationFolder.exists()) {
                list.addAll(eclipseFoundationFolder.listFiles().filter { it.isDirectory && it.name.contains("jdk") })
            }
            val scoopFolder = File(System.getProperty("user.home"), "scoop/apps")
            if (scoopFolder.exists()) {
                scoopFolder.listFiles().filter { it.isDirectory && it.name.lowercase().contains("jdk") }.forEach { file ->
                    list.addAll(file.listFiles().filter { dir -> dir.isDirectory })
                }
            }
        }
        OperatingSystem.LINUX -> {
            val usrLibJvmFolder = File("/usr/lib/jvm")
            if (usrLibJvmFolder.exists()) {
                list.addAll(usrLibJvmFolder.listFiles().filter { it.isDirectory })
            }
            val homebrewFolder = File("/home/linuxbrew/.linuxbrew/Cellar/")
            if (homebrewFolder.exists()) {
                list.addAll(homebrewFolder.listFiles().filter { it.isDirectory && it.name.lowercase().contains("jdk") })
            }
        }
        OperatingSystem.MACOS -> {
            val macosJavaFolder = File("/Library/Java/JavaVirtualMachines")
            if (macosJavaFolder.exists()) {
                list.addAll(macosJavaFolder.listFiles().filter { it.isDirectory })
            }
            val homebrewFolder = File("/usr/local/Cellar/")
            if (homebrewFolder.exists()) {
                list.addAll(homebrewFolder.listFiles().filter { it.isDirectory && it.name.lowercase().contains("jdk") })
            }
        }
        else -> {}
    }
    return list
}