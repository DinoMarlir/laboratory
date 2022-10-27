---
sidebar_position: 1
---

# Installation

Let's install **laboratory** in **less than 3 minutes**.

## Linux & macOS

Run this command `curl -sS https://raw.githubusercontent.com/mooziii/laboratory/dev/chemicae/packages/autoinstall.sh | bash`

You will be prompted for your sudo password (installation methods that don't require root access will come soon).

#### Distribution specific installation methods:
<details>
    <summary>Arch Linux</summary>

*soon*
</details>

<details>
    <summary>Homebrew</summary>

*soon*
</details>

<details>
    <summary>Flatpak</summary>

*soon*
</details>


### Tab Completion

Tab completion is currently only available for bash and zsh (I didn't test zsh yet so 50/50 chance that it will work).
Just restart your shell after the installation and it works just fine.

## Windows

Open a PowerShell terminal and run:
```powershell
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser # Required to run remote scripts
irm https://raw.githubusercontent.com/mooziii/laboratory/dev/chemicae/packages/install-windows.ps1 | iex
```
