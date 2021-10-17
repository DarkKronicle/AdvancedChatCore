# AdvancedChatCore

This is the base mod of all AdvancedChat modules and features. This mod provides the necessary foundation and framework for AdvancedChat mod's to work.

This mod primarily adds internal features used by other modules, as well as the ability to display the time that a message was sent. 

## Dependencies
[MaLiLib](https://www.curseforge.com/minecraft/mc-mods/malilib) and [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api/) are **required** for this mod to run

[Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu) is strongly recommended, as it allows you to easily edit the config


## Configuration

You can either manually edit the config file, located in `~.minecraft/config/advancedchat/advancedchatcore.json`, or you can open the configuration screen using mod menu (see **Dependencies**)


## About AdvancedChat Modules

AdvancedChat Modules splits the features of [AdvancedChat](https://www.curseforge.com/minecraft/mc-mods/advancedchat/) into several different mods, all of which depend on AdvancedChatCore. This simplifies development, and also allows for the user to pick-and-choose what features of AdvancedChat they want or don't want. The main AdvancedChat mod will eventually serve as a bundle of all AdvancedChat modules. 

## Developers

To use AdvancedChatCore within your own mod you can use [jitpack](https://jitpack.io/) with maven to download and implement it.
 
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```
dependencies {
	modImplementation 'com.github.DarkKronicle:AdvancedChatCore:VERSION'
}
```

To have the core reference the mod as a module, int `fabric.mod.json` in `custom` put `"acmodule: true"`

```JSON
{
  ...
  "custom": {
    "acmodule": true
  }
}
```
 
 Reference the [example mod](https://github.com/DarkKronicle/AdvancedChatModuleTemplate) for individual use cases.

## Credits n' more

Code & Mastermind: DarkKronicle

Update to 1.16.3: lmichaelis

Logo & Proofreading: Chronos22

For more help join the [Discord](https://discord.gg/WnaE3uZxDA)