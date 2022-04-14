---
layout: default
title: Common Filters
nav_order: 1
parent: AdvancedChatFilters
---

# Common Filters

To import these go to filters then click import and paste in the JSON data from this page.

## Underline Links

Underline links in chat

```
{"name":"Underline Links","active":true,"stripColors":true,"findString":"(?\u003cadv0z\u003e.)","findType":"regex","replaceType":"onlymatch","replaceTo":"\u0026n$1\u0026r","replaceTextColor":false,"textColor":"#FFFFFFFF","replaceBackgroundColor":false,"backgroundColor":"#FFFFFFFF","processors":{"forward":{"active":true},"actionbar":{"active":false},"sound":{"active":false,"notifySound":"none","soundPitch":1.0,"soundVolume":1.0},"narrator":{"active":false,"message":"$1"},"konstruct":{"active":false,"nodeData":""}},"order":0}
```

## Markdown Formatting

Provides support for discord-like formatting within chat. All 5 need to be imported.

1
```
{"name":"Markdown Underline","active":true,"stripColors":true,"findString":"__([^_]*?)__","findType":"regex","replaceType":"onlymatch","replaceTo":"\u0026n$1","replaceTextColor":false,"textColor":"#FFFFFFFF","replaceBackgroundColor":false,"backgroundColor":"#FFFFFFFF","processors":{"forward":{"active":true},"actionbar":{"active":false},"sound":{"active":false,"notifySound":"none","soundPitch":1.0,"soundVolume":1.0},"narrator":{"active":false,"message":"$1"},"konstruct":{"active":false,"nodeData":""}},"order":1}
```
2
```
{"name":"Markdown Strikethrough","active":true,"stripColors":true,"findString":"~~([^\\*]*?)~~","findType":"regex","replaceType":"onlymatch","replaceTo":"\u0026m$1","replaceTextColor":false,"textColor":"#FFFFFFFF","replaceBackgroundColor":false,"backgroundColor":"#FFFFFFFF","processors":{"forward":{"active":true},"actionbar":{"active":false},"sound":{"active":false,"notifySound":"none","soundPitch":1.0,"soundVolume":1.0},"narrator":{"active":false,"message":"$1"},"konstruct":{"active":false,"nodeData":""}},"order":2}
```
3
```
{"name":"Markdown Bold Italic","active":true,"stripColors":true,"findString":"\\*\\*\\*([^\\*]*?)\\*\\*\\*","findType":"regex","replaceType":"onlymatch","replaceTo":"\u0026o\u0026l$1","replaceTextColor":false,"textColor":"#FFFFFFFF","replaceBackgroundColor":false,"backgroundColor":"#FFFFFFFF","processors":{"forward":{"active":true},"actionbar":{"active":false},"sound":{"active":false,"notifySound":"none","soundPitch":1.0,"soundVolume":1.0},"narrator":{"active":false,"message":"$1"},"konstruct":{"active":false,"nodeData":""}},"order":3}
```
4
```
{"name":"Markdown Bold","active":true,"stripColors":true,"findString":"(?\u003c!\\*)\\*\\*([^\\*]*?)\\*\\*(?!\\*)","findType":"regex","replaceType":"onlymatch","replaceTo":"\u0026o$1","replaceTextColor":false,"textColor":"#FFFFFFFF","replaceBackgroundColor":false,"backgroundColor":"#FFFFFFFF","processors":{"forward":{"active":true},"actionbar":{"active":false},"sound":{"active":false,"notifySound":"none","soundPitch":1.0,"soundVolume":1.0},"narrator":{"active":false,"message":"$1"},"konstruct":{"active":false,"nodeData":""}},"order":4}
```
5
```
{"name":"Markdown Italic","active":true,"stripColors":true,"findString":"(?\u003c!\\*)\\*([^\\*]*?)\\*(?!\\*)","findType":"regex","replaceType":"onlymatch","replaceTo":"\u0026o$1","replaceTextColor":false,"textColor":"#FFFFFFFF","replaceBackgroundColor":false,"backgroundColor":"#FFFFFFFF","processors":{"forward":{"active":true},"actionbar":{"active":false},"sound":{"active":false,"notifySound":"none","soundPitch":1.0,"soundVolume":1.0},"narrator":{"active":false,"message":"$1"},"konstruct":{"active":false,"nodeData":""}},"order":5}
```

## Pig Latin

Enable Pig Latin. Not perfect, but really close.

```
{"name":"Pig Latin","active":true,"stripColors":true,"findString":"(?\u003c\u003d\\W?)([aeiouy]?[^aeiouy\\W]+?)([aeiouy]\\w*)","findType":"regex","replaceType":"onlymatch","replaceTo":"$2$1ay","replaceTextColor":false,"textColor":"#FFBBFFFF","replaceBackgroundColor":false,"backgroundColor":"#FFFFFFFF","processors":{"forward":{"active":true},"actionbar":{"active":false},"sound":{"active":false,"notifySound":"none","soundPitch":1.0,"soundVolume":1.0},"narrator":{"active":false,"message":"$1"},"konstruct":{"active":false,"nodeData":""},"macro":{"active":false,"command":"po|gers","delay":68,"parseAsKonstruct":false}},"order":0}
```
