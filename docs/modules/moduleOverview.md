---
layout: default
title: Overview
parent: ACC Modules
nav_order: 1
---

# ACC-Modules
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

---

### AdvancedChatCore
{:toc}
- The base mod and API

***General Tab***  
- Time Format - SimpleDateFormat, 
- Complete Time Format - How the text surrounding the time is formatted, 
- Time Color - Color of the time using HEX+Alpha (You can use https://www.hexcolortool.com/ to make your life easy.) 
- Show time - Whether or not to show time next to chat messages.  
- Clear Chat Messages on Disconnect - Whether or not to chat messages are kept when quitting servers or worlds. All messages are cleared regardless of this setting if you quit the game.  
- Stack Duplicate Messages - How many previous lines are checked for stacking duplicate messages (set to zero to disable.)  
- Update Stacked Messages - Whether or not stacked messages are shown again as new messages.  
- Message Owner Regex - The regex used to detect the head used for Chat Heads (No touchy unless you know your jazz.)  
- Filter Profanity - Whether or not to filter out swear words and replace them with astrixs (you can use your own swear word list in the swear_words.txt in the advancedchat config folder.)  

***Chat Screen Tab***
- Presistent Text - Whether or not messages that are typed but not sent are kept in the chat box.  
- Chat Color - Changes the color of the chat box and buttons nearby using HEX+Alpha values.  
- Extended Text Limit - Whether or not messages that are over the 256 limit are broken into multiple chat messages.

---
### AdvancedChatLog
{:toc}
- View and sort through mass amounts of previous messages.

***Chat Filters Tab***
- Stored Lines - The maximum amount of lines stored in the Chat Log. (Excessively large values may cause memory errors)  
- Saved Lines - Stores a specific amount of lines into a file and loads it when the game starts.  
- Clean Saved Lines - Removes some styling information for the saved text such as click events and hover events reducing file size.

---
### AdvancedChatFilters
{:toc}
- React to keywords in messages and act on them.

***Chat Filters Tab***  
By default has 3 options; Import Filters, Advanced Filters and New Filter.  
- Import Filters - Allows you to import already made filters either by yourself or others.
- Advanced Filters - Before activating and clicking the button you will be greeted by a message that gives fair warning on being careful with what you do and also instructing you on how to turn on Advanced Filters. 
- New Filter - Adds a filter that can either be turned on or off, Deleted or configured.

When a new filter is configured you are given the following options;
- Filter Name - What the filter shows up as in the config
- Ignore Colors - Whether or not this filter will acount for the color of the matched string.
- String to Find - The string that will be searched and filtered
- Filter Type - How matches will be found. Avaliable options;
	- **Literal**, exact match. 
	- **Upper-Lower**, literal but not case-sensitive.
	- **RegEx**, Parses the strings as Regular Expression.
	- **All**, Passes all text through the filter regardless of the string to find.
- Text Color - The color of the replaced text when the filter is triggered, again using HEX+Alpha channels. (This setting gets overridden by legacy formatting codes)
- Replace Text Color? - Whether or not the cutom text color should be used when the filter is triggered.
- Replace to - The string that replaces what is set for replace type %Match% is replaced by whatever triggered the filter.
- Replace Type - How the filter should replace matched messages. Currently avaliable are 
	- **None** - Does nothing, 		
	- **Children** - Sends the message to be modified by child friendly filters.
	- **Full Message** - Replaces the whole message with the replace to string.
	- **Only Match** - Replaces only what triggered the filter.
	- **OwO** - Sends the whole message to be converted to furry speak.
	- **Rainbow** - Converts the entire message to rainbow.
	- **Roman Numeral** - Parses any numbers and converts them to roman numerals.
	- **Reverse** - Reverses the entire message.  
	
As well as 3 additional buttons; Back, takes you to the previous page. Export, used to wrap all your settings into a neat file that can be given to others to import or just as a backup for yourself. The last button is Message Processor Settings which when clicked gives the following options that can either be turned on or off;
- Forward to Chat, whether or not the messages caught byt the filter are sent to the Chat Box.
- Action Bar, whether or not the messages caught by the filter are sent to the action bar.
- Sound, whether or not the messages caught by the filter play a sound. This also has a configure option that you can change the sound volume, sound pitch, and the notifying sound used.
- Narrator, whether or not the narrotor say the messages caught by the filter. This also has a configure button that can be used to give the option to alter what is said by the narrator when the filter is triggered.

---
### AdvancedChatBox
{:toc}
- Complex writing of messages and formatting in the text box.</b>

***Chat Suggestor Tab***  
Has two avaliable buttons, Configure Suggestors, and Configure Formatters, as well as the following;
- Highlight Color - What color selected suggestions should be using Hex+Alpha values.
- Non-Highlighted Color - What color non-selected suggestions should be using Hex+Alpha values.
- Background Color - What color the background of the Chat Suggestor should be using Hex+Alpha values.
- Number of Suggestions - The maximum number of suggestions that the Chat Suggestor should display at once.
- Remove Namespace - Whether or not the namespace should be omitted from the Chat Suggestor. (The namespace is the part of the ID that preceeds the colon and includes the colon, such as 'minecraft:' in minecraft:stone)
- Prune Player Suggestions - Whether or not non-real players are removed from Chat Suggestor. (Such as the fake players used in BungeeTabListPlus)
- Available Suggestion Color - What color custom chat suggestors will show up as (Hex+Alpha values.)

For the two buttons, the options are as follows;

***Configure Suggestors Button***
- Suggest Players - Whether or not player names are suggested in chat. Can be set to on or off.
- In-Chat Calculator - Whether or not the chat calculator is active. Can be set to on or off. (To use the chat calculator, put a math expression inside of brackets, and press tab in the brackets. Expressions follow the mXParser format.)
- Suggest Shortcuts - Whether or not shortcuts are suggested as you type. Can be set to on or off as well as configured to add or remove shortcuts. (Shortcuts are a way to bind a memorable phrase to a string of text, such as for emoticons. To use shortcuts, put a colon and the shortcut name, and press tab.)
- Spell Checker - Whether or not the spell-checker is active. 

***Configure Formatters Button***
- Custom Command Highlighting - Whether or not custom colors are used to highlight part of a command in Chat HUD. This may also be configured with; 
	- **Command Color** - What color a command is by default using Hex+Alpha values. 
	- **Error Color** - The color that highlights and incorrect command using Hex+Alpha values.
	- **Default Palette** - The palette the command colorer uses for parts of commands. Custom ones may be declared in the ~/minecraft/config/advancedchat/colors.toml file.
- Highlight JSON in Commands - whether or not JSON is parsed and highlighted in the Chat HUD.
- Color Code Formatting - Whether or not color codes with & are parsed and highlighted in their respected color.

***Spell Checker Tab***  
- Spell Checker Hover Format - The format of the text that appears when you hover your mouse over a suggested spelling change.

---
### AdvancedChatHUD
{:toc}
- Create chat tabs and a good looking HUD.

***Chat HUD Tab***  
Enable Vanilla Chat HUD - Enables the Vanilla Chat HUD that acts mostly like a Chat Window. (Vanilla Chat HUD can use Chat Tabs if you click on it and then change the tab)  
- Display Chat Heads - Whether or not the head of the message sender displays besides their message.
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
-  Empty Text Color - The color of text without formatting using HEX+Alpha values.
- Background Line Style - How the background of the unfocused Chat HUD is fitted to the messages. 
	- **Full** - Fits the background width of the Chat HUD. 
	- **Compact** - Fits the background to the length of each respective line.
- Striped Messages - Whether or not the backgrounds of every other message is colored differently to create a striped effect.
- Stored Lines - The maximum amount of lines stored in Chat HUD (Excessively large values may cause memory errors)

***Chat Tabs Tab***  
By default there is already the Main Chat Tab listed. Other than that there are two buttons avaliable. Import Tab, and New Chat Tab.
The import tab just like with the other modules is used to import an already made Chat Tab. The New Chat Tab provides a new chat tabe to configure or delete.
Chat Tabs have the following options to configure as well as the option to export;  
- Tab Name - What the tab shows up as in the config.
- Starting Message - What text shows up when the tab is focused.
- Tab HUD Abbreviation - What the tab shows up as on the side of the Chat HUD if Show Chat Tabs is enabled under Chat HUD settings.
- Accent Color - The accent color of the chat tab using HEX+Alpha values.
- Border Color - The color of the outline of the chat tab using HEX+Alpha values.
- Background Color - The color of the background of the chat tab using HEX+Alpha values.
- Show Unread Messages - Whether or not the number of unread messages are displayed for the chat tab.

---
### AdvancedChatMacros (**Planned**)

- Be able to execute commands based on filters and keybinds.
