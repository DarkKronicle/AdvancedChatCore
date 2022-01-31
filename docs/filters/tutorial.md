---
layout: default
title: Filters Tutorial
parent: AdvancedChatFilters
nav_order: 0
---
# Basic Filters

AC Filters let you take control of your chat. Let's take a look at how to make your own filters.
Recall that the String to Find box contains the string to filter for. The treatment is specified by the Filter Type property.  

## Name Highlights

A common use for filters is to highlight your name in chat. The easiest way to do this is to edit the `Text Color` property. Change the color value to what you want make sure to turn on `Replace Text Color`. Keep the other values at default for now.
What if you wanted to make it trigger regardless of casing? You can simply change the `Filter Type` property to Upper-Lower. You can experiment with this and change different properties.

## Profanity

Let's try something a little harder. What about filtering profanity? You may think you need to check for *every* single profane word, but DarkKronicle makes this easy for us. Set the `Filter Type` to Custom, and set the search string to `profanity`. You can then set the Replace String to something more friendly.

We encourage you to mess around with these examples and try things on your own.

# Message Processor

ACF offers a lot more options with the matched chat message. The Message Processor section allows you to configure what happens to the chat message. You can control if it goes to the chat, it goes to the Action Bar, the Narrator says it, or if a sound is played. (psst... that would be great for the name highlights we mentioned before!). You can also add Konstruct filters, which are pretty advanced.

# Regular Expressions

Regular expressions are one of the best ways to implement find and replace searches. Fortunately, ACF contains RegEx support in `Filter Type`. We recommend a few tools to help you out.
1. [Regex Parser - Extremely useful](https://regex101.com)
2. [Interactive Tutorial](https://regexone.com/)
3. [Visualizer](https://extendsclass.com/regex-tester.html#java)
4. [Cheat Sheet](https://cheatography.com/davechild/cheat-sheets/regular-expressions/)
5. [Basics Video](https://www.youtube.com/watch?v=sXQxhojSdZM])

A few things to keep in mind - 
Let's go over some quick regex tips.

## Player Regex

Oftentimes with ACF we find the need for regex that matches valid players. `[A-Za-z0-9_§]{3,16}` matches valid playernames. You can use this for [message regex](FILE SOON) and more.  
Another thing we can do with regex is make more complicated name highlights. This will vary for everyone, but the regex for me is `(?i)furr?y(?:[_ ]?101)?`. 
