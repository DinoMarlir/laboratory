package me.obsilabor.laboratory.internal

import me.obsilabor.laboratory.arch.Server

enum class ServerEditAction(val actionString: String, val perform: (Server) -> Unit) {
    RENAME("Rename the server", perform = {

    }),
    STATIC("Make the server static/dynamic", perform = {

    }),
    ADD_TEMPLATE("Add a template", perform = {

    }),
    REMOVE_TEMPLATE("Remove a template", perform = {

    }),
    PLATFORM("Migrate the servers platform", perform = {

    }),
    PLATFORM_BUILD("Change the platform build", perform = {

    }),
    MC_VERSION("Migrate the servers mc version", perform = {

    }),
    AUTOMATIC_UPDATES("Toggle automatic updates", perform = {

    }),
    RAM("Modify the servers heap memory", perform = {

    }),
    ADD_JVM_ARG("Add a jvm argument", perform = {

    }),
    REMOVE_JVM_ARG("Remove a jvm argument", perform = {

    }),
    ADD_PROCESS_ARG("Add a process argument", perform = {

    }),
    REMOVE_PROCESS_ARG("Remove a process argument", perform = {

    }),
}