---
sidebar_position: 1
---

# Editing server environments

To edit things like the max heap memory or the name of servers, run `laboratory modify <server-name>` or the long command `laboratory server modify <server-name>`.
    You will have to choose what you want to do. (`<server-name>` is the name of the server)

![img.png](/img/docs/server-modify-prompt.png)

### Rename the server

Just type in a new name for the server. The server needs to be turned off first.
![server-modify-rename.png](/img/docs/server-modify-rename.png)

### Templates (2, 3, 4)
To get more information about using templates in laboratory take a look at [`Using templates`](/docs/laboratory-storage/using-templates)

### Migrate servers platform
soon
### Change platform build
soon
### Toggle automatic updates
soon
### Modify thew servers heap memory
soon
### Add jvm argument
soon
### Remove jvm argument
soon
### Add a process argument
soon
### Remove a process argument
soon
### Change servers report
soon
### Toogle automatic backups
soon
### Change java versions

If you're using a minecraft version below 1.17, you may have to use Java 8. For that enter `17` to change the java version. You will see a list with all java installations and can select the one you want to use.

![img.png](/img/docs/java-version-prompt.png)

:::info Important
If your java installation is not listed, please file a [bug-report](https://github.com/mooziii/laboratory/issues/new?assignees=&labels=bug&template=bug_report.yml) and provide the location of your java installation and the operating system you're using.
:::