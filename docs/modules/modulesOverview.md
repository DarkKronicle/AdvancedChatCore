---
layout: default
title: Modules Overview
nav_order: 1
has_children: false
---

# AdvancedChat Modules Overview
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

---
![AdvancedChatCoreIcon](./assets/images/AdvancedChatCore_Icon.png)
### AdvancedChatCore
- The base mod and API

Once the mod is installed a **<u>Core</u>** tab as well as a **<u>Hot Keys</u>** tab will be added to the AdvancedChat Config Menu. Located in that tab are the following;  

***General Tab***  
- Time Format - SimpleDateFormat, 
- Complete Time Format - How the text surrounding the time is formatted, 
- Time Color - Color of the time. (*See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeCOLOR.html) page for color formatting.*)   
- Show time - Whether or not to show time next to chat messages.  
- Clear Chat Messages on Disconnect - Whether or not to chat messages are kept when quitting servers or worlds. All messages are cleared regardless of this setting if you quit the game.  
- Stack Duplicate Messages - How many previous lines are checked for stacking duplicate messages (set to zero to disable.)  
- Update Stacked Messages - Whether or not stacked messages are shown again as new messages.  
- Message Owner Regex - The regex used to detect the head used for Chat Heads (No touchy unless you know your jazz.)  
- Filter Profanity - Whether or not to filter out swear words and replace them with asterixis (you can use your own swear word list in the swear_words.txt in the advancedchat config folder.)  
- Profanity Threshold - The minimal level of severity before AdvancedChat filters a word. Ranges from 0 to 3. 1 includes common swear words, and progressively gets more explicit.
- Disable Partial Matches - Whether or not the profanity filter does not match words that contain swears, such as grass.

***Chat Screen Tab***
- Persistent Text - Whether or not messages that are typed but not sent are kept in the chat box.  
- Chat Color - Changes the color of the chat box and buttons nearby. (*See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeCOLOR.html) page for color formatting.*)    
- Extended Text Limit - Whether or not messages that are over the 256 limit are broken into multiple chat messages.

***Hotkeys Tab***
- Open Settings GUI - Opens the settings GUI with the available options of;
	- Assigning a hotkey and then modifying how this reacts with the game in the following ways;
	- Activate On - Does the keybind activate on press or release of the key combination.  
	- Context - Cant the keybind be activated in-game or inside a GUI.  
	- Allow Empty Keybind - Is an empty keybind valid (will always be considered to be active)  
	- Allow Extra Keys - Are extra keys allowed to be held to still activate the keybind.  
	- Order Sensitive - Should the keybind keys be pressed in the specific order they were defined in.  
	- Exclusive - If true, then no other keybinds can have been activated before the keybind in question. This check resets when all keys are released.  
	- Cancel Further Processing - Cancel further (vanilla) processing of the last pressed key, when the keybind activates.  

---
![ACC-ChatLogIcon](./assets/images/ACC-ChatLog_Icon.png)
### AdvancedChatLog
{:toc}
- View, and sort through mass amounts of previous messages. Plus store previous messages to be loaded next play session.

Once the mod is installed a **<u>Chat Log</u>** tab will be added to the AdvancedChat Config Menu. As well as a button in the chat GUI that looks like 3 stacked bars. Located in that tab and button are the following;

**In Game Buttons**  
- The Chatlog module adds an additional button to the right of the UI that looks like 3 stacked bars once the chat is opened. That is used to view and search chat. The search options use the **Filter Type** button that cycles the options;
	- **Literal**, exact match. 
	- **Upper-Lower**, literal but not case-sensitive.
	- **RegEx**, Parses the strings as Regular Expression.
	- **Custom**, Allows for custom search parameters.  

***Chat Log Tab***  
- Stored Lines - The maximum amount of lines stored in the Chat Log. (Excessively large values may cause memory errors)  
- Saved Lines - Stores a specific amount of lines into a file and loads it when the game starts.  
- Clean Saved Lines - Removes some styling information for the saved text such as click events and hover events reducing file size.

**Output File**
- The location of the log file is in your .minecraft folder, either in the main directory (*~/.minecraft/chatlogs/*) or profile directory (*~/.minecraft/MC_Profile_Name/chatlogs/*) in the 'chatlogs' folder. 

---
![ACC-ChatFiltersIcon](./assets/images/ACC-ChatFilters_Icon.png)
### AdvancedChatFilters
{:toc}
- React to keywords in messages and act on them.

Once the mod is installed a **<u>Chat Filters</u>** tab will be added to the AdvancedChat Config Menu. Located in that tab are the following;  

***Chat Filter Buttons***  
By default has 3 options; Import Filters, Advanced Filters and New Filter.  
- Import Filters - Allows you to import already made filters either by yourself or others.
- Advanced Filters - Before activating and clicking the button you will be greeted by a message that gives fair warning on being careful with what you do and also instructing you on how to turn on Advanced Filters. 
Once you enable it when clicking the button you will be given two buttons, Back and open directory. Opening the directory will bring you to the advancechat filters folder in your .Minecraft directory.
- New Filter - Adds a filter that can either be turned on or off, Deleted or configured.

When a new filter is configured you are given the following options;
- Filter Name - What the filter shows up as in the config
- Ignore Colors - Whether or not this filter will account for the color of the matched string.
- String to Find - The string that will be searched and filtered
- Filter Type - How matches will be found. Available options;
	- **Literal**, exact match. 
	- **Upper-Lower**, literal but not case-sensitive.
	- **RegEx**, Parses the strings as Regular Expression.
	- **All**, Passes all text through the filter regardless of the string to find.
- Text Color - The color of the replaced text when the filter is triggered. (*This setting gets overridden by legacy formatting codes. | See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeCOLOR.html) page for color formatting.*)    
- Replace Text Color? - Whether or not the custom text color should be used when the filter is triggered.
- Replace Background Color? - Whether or not the background should be replaced when the filter is triggered.
- Replace to - The string that replaces what is set for replace type %Match% is replaced by whatever triggered the filter.
- Replace Type - How the filter should replace matched messages. Currently available are 
	- **None** - Does nothing, 		
	- **Children** - Sends the message to be modified by child friendly filters.
	- **Full Message** - Replaces the whole message with the replace to string.
	- **Only Match** - Replaces only what triggered the filter.
	- **OwO** - Sends the whole message to be converted to furry speak.
	- **Rainbow** - Converts the entire message to rainbow.
	- **Roman Numeral** - Parses any numbers and converts them to roman numerals.
	- **Reverse** - Reverses the entire message.  
- Test Input - Is a text entry box where you can test your created filter with the included listed information of the following outputs;   
**Input Message:** the message input to test.   
**Matched:** True/False on the message matching the filter.  
**Output Message:** the input message with the filter applied.

Once the filter has been made you have the option to configure it again, turn it off/on or delete it from the **Chat Filters Tab**  

As well as 3 additional buttons; Back, takes you to the previous page. Export, used to wrap all your settings into a neat file that can be given to others to import or just as a backup for yourself. The last button is Message Processor Settings which when clicked gives the following options that can either be turned on, off, or configured;
- Forward to Chat, whether or not the messages caught by the filter are sent to the Chat Box.
- Action Bar, whether or not the messages caught by the filter are sent to the action bar.
- Sound, whether or not the messages caught by the filter play a sound. This also has a configure option that you can change the sound volume, sound pitch, and the notifying sound used.
- Narrator, whether or not the narrator say the messages caught by the filter. This also has a configure button that can be used to give the option to alter what is said by the narrator when the filter is triggered.
- Konstruct, Runs a construct script. More info on the wiki.

---
![ACC-ChatBoxIcon](./assets/images/ACC-ChatBox_Icon.png)
### AdvancedChatBox
{:toc}
- Complex writing of messages and formatting in the text box.</b>

Once the mod is installed a **<u>Box</u>** tab will be added to the AdvancedChat Config Menu. Located in that tab are the following;  

***General Tab***  
Has two available buttons, Configure Suggestors, and Configure Formatters, as well as the following;
- Highlight Color - What color selected suggestions should be. (*See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeCOLOR.html) page for color formatting.*)   
- Non-Highlighted Color - What color non-selected suggestions should be. (*See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeCOLOR.html) page for color formatting.*)   
- Background Color - What color the background of the Chat Suggestor should be. (*See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeCOLOR.html) page for color formatting.*)   
- Number of Suggestions - The maximum number of suggestions that the Chat Suggestor should display at once.
- Remove Namespace - Whether or not the namespace should be omitted from the Chat Suggestor. (The namespace is the part of the ID that precedes the colon and includes the colon, such as 'minecraft:' in minecraft:stone)
- Prune Player Suggestions - Whether or not non-real players are removed from Chat Suggestor. (Such as the fake players used in BungeeTabListPlus)
- Available Suggestion Color - What color custom chat suggestors will show up as. (*See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeCOLOR.html) page for color formatting.*)   

For the two buttons, the options are as follows;

***Configure Suggestors Button***
- Suggest Players - Whether or not player names are suggested in chat. Can be set to on or off.
- In-Chat Calculator - Whether or not the chat calculator is active. Can be set to on or off. (*To use the chat calculator, put a math expression inside of brackets, and press tab in the brackets. | See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeMATH.html) page for Maths formatting*) 
- Suggest Shortcuts - Whether or not shortcuts are suggested as you type. Can be set to on or off as well as configured to add or remove shortcuts. (Shortcuts are a way to bind a memorable phrase to a string of text, such as for emoticons. To use shortcuts, put a colon and the shortcut name, and press tab.)
- Spell Checker - Whether or not the spell-checker is active. 

***Configure Formatters Button***
- Custom Command Highlighting - Whether or not custom colors are used to highlight part of a command in Chat HUD. This may also be configured with; 
	- **Command Color** - What color a command is by default. (*See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeCOLOR.html) page for color format*)  
	- **Error Color** - The color that highlights and incorrect command. (*See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeCOLOR.html) page for color format*)  
	- **Default Palette** - The palette the command colorer uses for parts of commands. (*Custom palettes may be declared in the ~/minecraft/config/advancedchat/colors.toml*)  
- Highlight JSON in Commands - whether or not JSON is parsed and highlighted in the Chat HUD.
- Color Code Formatting - Whether or not color codes with & are parsed and highlighted in their respected color.

***Spell Checker Tab***  
- Spell Checker Hover Format - The format of the text that appears when you hover your mouse over a suggested spelling change.

---
![ACC-ChatHUDIcon](./assets/images/ACC-ChatHUD_Icon.png)
### AdvancedChatHUD
{:toc}
- Create chat tabs and a good looking Chat.

Once the mod is installed a **<u>HUD</u>** tab will be added to the AdvancedChat Config Menu. Located in that tab are the following tabs as well buttons outside of the AdvancedChat config menu when opening chat;  

**In Game Buttons** 
- Exclamation Mark (Location: Chat window) - This symbol when clicked has 3 options that modify the windows render of new messages and are as follows;
	- Exclamation Mark - Vanilla default.  
	- Thumbtack - Always focused. 
	- Square - Never focus.
- Nested 90° Lines (Location: Chat window) - For resizing the current chatbox.  
- X (Location: Chat window)- This will remove the respective AdvancedChat Window. 
- Plus Sign (Location: Relative to listed tabs above player text entry)- Will create a new chat window for the currently selected Tab.
- Trash Can (Location: Relative to listed tabs above player text entry)- This will remove all currently open AdvancedChat boxes.    
- Cog Wheel (Location: Bottom right)- Opens the configuration menu for the AdvancedChatHUD Chat Tabs.  

There is also an added right click context menu when your chat is open that has the following options; 
- Remove All Windows - Removes all windows currently displayed.  
- Clear All Messages - Clears all messages from the chatbox.  
- Duplicate Window - Duplicates the current window.  

***Chat HUD Tab***  
- Enable Vanilla Chat HUD - Enables the Vanilla Chat HUD that acts mostly like a Chat Window. (Vanilla Chat HUD can use Chat Tabs if you click on it and then change the tab)  
- Display Chat Heads - Whether or not the head of the message sender displays besides their message.
- Always Show Tab Buttons - Whether or not to constantly show the tab buttons. 
- Right Tab Buttons - Whether or not to push the tab buttons to the right side of the screen.  
- Default Chat Width - The width of the Chat HUD (Overrides vanilla chat width)
- Default Chat Height - The height of the Chat HUD (Overrides vanilla chat height)
- Default Chat X - The x-coordinate of the chat box, starting at the bottom-left side of the screen.
- Default chat Y - The y-coordinate of the chat box, starting at the bottom-left side of the screen.
- Padding Between Messages - How much space should be between individual messages.
- Spacing Between Lines - The space in pixels between lines of chat.
- Left-Side Chat Padding - The amount of space between the left side of the chat box to where the text starts.
- Right-Side Chat Padding - The amount of space between the right side of the chat box to where the text breaks.
- Bottom-Side Chat Padding - The amount of space between the bottom of the chat box to the newest message.
- Top-Side Chat Padding - The amount of space between the top of the chat box to the oldest message.
- Chat Visibility - What behavior of the visibility the Chat HUD should be. 
	- **Vanilla** - Chat HUD renders as it does in Vanilla.
	- **Always** - Renders the Chat HUD at all times.
	- **Focus Only** - Renders the Chat HUD only if the Chat Box is open.
- Chat Scale - How big or small the text inside of the chat box is. (Does not affect the size of the Chat HUD.)
- Fade Speed - How quickly a message takes to fade-out.
- Time Until Fade - The number of ticks before newly received messages begin to disappear.
- Fade Style - The easing function used for the fade animation. (Vanilla uses linear easing)
- Empty Text Color - The color of text without formatting. (*See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeCOLOR.html) page for color formatting.*)   
- Background Line Style - How the background of the unfocused Chat HUD is fitted to the messages. 
	- **Full** - Fits the background width of the Chat HUD. 
	- **Compact** - Fits the background to the length of each respective line.
- Striped Messages - Whether or not the backgrounds of every other message is colored differently to create a striped effect.
- Stored Lines - The maximum amount of lines stored in Chat HUD (Excessively large values may cause memory errors)

***Chat Tabs Tab***  
By default there is already the Main Chat Tab listed. Other than that there are two buttons available. Import Tab, and New Chat Tab.
The import tab just like with the other modules is used to import an already made Chat Tab. The New Chat Tab provides a new chat tab to configure or delete.
Chat Tabs have the following options to configure as well as the option to export;  
- Tab Name - What the tab shows up as in the config.
- Starting Message - What text shows up when the tab is focused.
- Tab HUD Abbreviation - What the tab shows up as on the side of the Chat HUD if Show Chat Tabs is enabled under Chat HUD settings.
- Accent Color - The accent color of the chat tab using. (*See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeCOLOR.html) page for color formatting.*)   
- Border Color - The color of the outline of the chat tab. (*See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeCOLOR.html) page for color formatting.*)   
- Background Color - The color of the background of the chat tab. (*See [this](https://darkkronicle.github.io/AdvancedChatCore/commonKnowledge/commonKnowledgeCOLOR.html) page for color formatting.*)   
- Show Unread Messages - Whether or not the number of unread messages are displayed for the chat tab.

---
![ACC-ChatMacrosIcon](./assets/images/ACC-ChatMacros_Icon.png)  
*Icon is writers rendition that is subject to change once a real icon is made*
<!--Why is it that my art can never please you father Kron? Is it because its made of dried macaroni noodles and crayons and not some fancy art program like Xylo :sob: -->
### AdvancedChatMacros (**Planned**)
{:toc}
- Be able to execute commands based on filters and keybinds.

<!--This documentation was written by Nomad on January 30th 2022-->
<!--Thanks to Fury for fixing the file pathing issues when pushing to the wiki page-->
