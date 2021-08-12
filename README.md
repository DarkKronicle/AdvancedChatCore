# AdvancedChatCore

This is the base mod of all AdvancedChat modules and features. This mod provides the necessary foundation and framework for AdvancedChat mod's to work.

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