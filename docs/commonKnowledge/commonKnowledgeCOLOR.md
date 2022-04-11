---
layout: default
title: Color
nav_order: 2
parent: Common Knowledge
---

# Common Knowledge - Color
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}  
</details>

---
## Color in AdvancedChat  
{:toc}    
AdvancedChat Project, the name given to encompass all of the AdvancedChat mods(Core, HUD, Box, Log, and Filters), has the option to change the color of certain things. Anytime you encounter the option to change the color of a setting it will use HEX values. Some will only use the 6 character format and others may use the 8 character format. The difference between these two is the alpha channel aka the opacity of your color.  

A normal HEX code looks something like this #FF0000, which is bright red. The FF indicates the hue of the the RGB channels which are represented by the four zeros. But when using an 8 character HEX suddenly you unlock an additional two characters at the front of the string that represent opacity also called transparency.   

The proper format is #AARRGGBB where the A is alpha channels, R is red, G is green and B is blue. Which is why HEX+Alpha is sometimes referred to ARGB but has fallen out of style a bit due to ARGB referring to Addressable RGB lights.  

So knowing all that; #CCFF0000 would be an 80% opacity to our bright red mentioned earlier making it a slightly lighter red. As 100% opacity would be our default value of bright red.  

---
## Resources  
{:toc}  
Here is a dumb of useful links to help get you on your way;  
- Learning
	- Incase you would like to learn more [here](https://learn.sparkfun.com/tutorials/hexadecimal/all) is a link to an article on HEX itself 
	- As well as [this](https://www.codeconquest.com/hex-color-codes/) one for HEX color specifically.
- Coloring 
	- Color pallet [picker](https://coolors.co/c6c5b9-62929e-4a6d7c-393a10-475657) randomizer that has a fair few customization options. Just load the page and if you don't like the choices hit SPACE.
	- Have a specific color you like in an image? [This](https://imagecolorpicker.com/en) site allows you to figure out what it is.  
	- Looking for something popular? We got you covered with [this](https://www.color-hex.com/popular-colors.php) site.  
	- What's that you want a palette and to be popular? Well alright [here](https://www.color-hex.com/color-palettes/popular.php) you go.  
	- If you don't want to google around for a decent HEX color picker [here](https://htmlcolorcodes.com/color-picker/) is my go to that offers HSL, CYMYK, RGB, and our needed HEX. 
It also has some information on things like color theory and shading. Pick your color and then just use the section below to add opacity if you'd like.  

---
## HEX Opacity Table  
{:toc}   
Here is a complete table from 0% opacity all the way up to 100% opacity at single percent increments...It should be noted that you wont see much of a difference from say 1% to 3%. You more than likely will see the best result when working at 5% increments. I included 1% increments incase you want to dial it in after getting in the ball park of where youd like it. <u>Remember</u>! The **closer** to 100% the value is the **less** transparent it is.

<details>
<summary> Click here to show the Opacity Table </summary>

|          |	 |     |     |	   |	 |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |	   |     |     |	 |	   |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |	 |     |     |	   |	 |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |	 |     |     |	   |	 |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |      |
| ---      | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |  --- |
|Opacity%  |  0% |  1% |  2% |	3% |  4% |  5% |  6% |  7% |  8% |  9% | 10% | 11% | 12% | 13% | 14% | 15% | 16% | 17% | 18% | 19% | 20% | 21% | 22% | 23% | 24% | 25% | 26% | 27% | 28% | 29% | 30% | 31% | 32% | 33% | 34% | 35% | 36% | 37% | 38% | 39% | 40% | 41% | 42% | 43% | 44% | 45% | 46% | 47% | 48% | 49% | 50% | 51% | 52% | 53% | 54% | 55% | 56% | 57% | 58% | 59% | 60% | 61% | 62% | 63% | 64% | 65% | 66% | 67% | 68% | 69% | 70% | 71% | 72% | 73% | 74% | 75% | 76% | 77% | 78% | 79% | 80% | 81% | 82% | 83% | 84% | 85% | 86% | 87% | 88% | 89% | 90% | 91% | 92% | 93% | 94% | 95% | 96% | 97% | 98% | 99% | 100% |
|Hex#      |  00 |  02 |  05 |  07 |  0C |  0C |  0F |  11 |  14 |  16 |  19 |  1C |  1E |  21 |  23 |  26 |  28 |  2B |  2D |  30 |  33 |  35 |  38 |  3A |  3D |  3F |  42 |  44 |  47 |  49 |  4C |  4F |  51 |  54 |  56 |  59 |  5B |  5E |  60 |  63 |  66 |  68 |  6B |  6D |  70 |  72 |  75 |  77 |  7A |  7C |  7F |  82 |  84 |  87 |  89 |  8C |  8E |  91 |  93 |  96 |  99 |  9B |  9E |  A0 |  A3 |  A5 |  A8 |  AA |  AD |  AF |  B2 |  B5 |  B7 |  BA |  BC |  8F |  C1 |  C4 |  C6 |  C9 |  CC |  CE |  D1 |  D3 |  D6 |  D8 |  DB |  DD |  E0 |  E2 |  E5 |  E8 |  EA |  ED |  EF |  F2 |  F4 |  F7 |  F9 |  FC |   FF |

</details>

<!--This documentation was written by Nomad on February 8th 2022-->
