---
sidebar_position: 1
---

# Editing server environments

To edit things like the max heap memory or the name of servers, run `laboratory modify <server-name>` or the long command `laboratory server modify <server-name>`.
You will have to choose what you want to do.

![img.png](/img/docs/server-modify-prompt.png)

### Change java versions

If you're using a minecraft version below 1.17, you may have to use Java 8. For that enter `17` to change the java version. You will see a list with all java installations and can select the one you want to use.

![img.png](/img/docs/java-version-prompt.png)

:::info Important
If your java installation is not listed, please file a [bug-report](https://github.com/mooziii/laboratory/issues/new?assignees=&labels=bug&template=bug_report.yml) and provide the location of your java installation and the operating system you're using.
:::