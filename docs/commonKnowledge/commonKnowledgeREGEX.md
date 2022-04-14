---
layout: default
title: RegEx
nav_order: 3
parent: Common Knowledge
---

# RegEx

AdvancedChat provides a lot of support for Regular Expressions (RegEx) to allow for more complex matching, filtering, and searching.

## Overview

Regular Expressions are a tool to find matches within a string. They can come off as very complex, confusing, and hard to learn. 9/10 times a simple regular expression will be easy to create and easy to understand.

- RegEx parser (HIGHLY RECOMMEND) - https://regex101.com/
- Interactive Tutorial - https://regexone.com/
- Visualizer - https://extendsclass.com/regex-tester.html#java
- Cheat Sheet - https://cheatography.com/davechild/cheat-sheets/regular-expressions/
- Basics Video - https://www.youtube.com/watch?v=sXQxhojSdZM

## AdvancedChat Specific RegEx

AdvancedChat (in specific situations) allows for matching for colors/links/other text properties within RegEx. This is supported in filters and is rolling out to other modules.

How do you do it? RegEx group names.

You can create a RegEx group name by using the syntax `(?<name>)`. An example is `(?<eachCharacter>.)`. It creates a group named `eachCharacter that is applied to every character that it matches.

AdvancedChat takes these names and checks to see if any start with `adv`. If they do you can apply filtering conditions making it so that AdvancedChat won't match unless the condition is met. This is a second layer, so the RegEx won't be reprocessed so `or` conditions can be a bit confusing.

## Color Example

You can use any built in Minecraft color `0-9`, `a-f` to compare to as well as `o`, `l`, `m`, `n`. Let's say I only want to match to dark blue `DarkKronicle`. I would use the regex `(?<adv09>DarkKronicle)`. The `adv0` specifies function 0 for AdvancedChat. Any character specified after that functions as an `or` condition. This one is only `9` which is the color code for dark blue. For example, if I did `9al` it would match dark blue, green, or bold. If an `and` condition is wanted there can be nested groups.

Custom types:

- `z` - A clickable link
- `x` - Copy to clipboard
- `y` - Open file
- `w` - Run command
- `v` - Suggest command
- `h` - Hover text
