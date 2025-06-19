---

<center>
<p align="center"><img src=https://cdn.modrinth.com/data/cached_images/c8b162bb8cc4eaa95133ffd91190a651689d2133.png /></p>
</center>

---

Yet another chat ~~plugin~~ **mod** aimed to add more chat customization.

All the features below are toggleable, so if you want to have only pings and PMs, you can turn off all the other features in the config.

**Find the Project Page on [Modrinth](https://modrinth.com/mod/classic-chat)**

## Features
- **Custom Channels** which can be customized individually. You can set up message format, chat range, cooldown and fast access prefix for each channel.
![Custom channels showcase](https://cdn.modrinth.com/data/cached_images/9f1a23a90ff6e07731e6035eb428a3067429d322_0.webp)
- **Private messages** with custom formatting and /reply command for quick replies.
![Private messages showcase](https://cdn.modrinth.com/data/cached_images/ed6a4cf5d746ebc5529abcfaf75ca7855c83d7db.png)
- **@pings in chat** with customazible color and sound.
![Ping showcase](https://cdn.modrinth.com/data/cached_images/0e716bdce8c7cfcef31436353b97369bcd6e7aaf.png)
- **Roleplay comands** for true lovers of immersive interactions.
![Roleplay showcase](https://cdn.modrinth.com/data/cached_images/229eb8ee740e51d58db0ce41168308085c229840.png)
- **Custom color codes** so you don't need to search for that '§' symbol to apply colors in chat. Also it adds advanced color codes, read about them below.
![Color codes showcase](https://cdn.modrinth.com/data/cached_images/e3bfbd424b62a274cd3fe282d47b3a39b8ea9f1e.png)
- **Non-italic /nickname** so you can be a true spy.
![Nick showcase](https://cdn.modrinth.com/data/cached_images/277c3f7b177f04042222b48ca79b803b22495c7e.png)

### Advanced color codes
They are currently used for gradients, but maybe i'll add more features to them in future updates. 

To get gradients, use following syntax: `&{colors, period}`. Colors represents one or more colors, alternating in the gradient. Period affects how frequently (in letters) color will change. It's 1 letter per color by default, so you can just type `&{colors}` if you don't want to change period.

![Replace this with a description](https://cdn.modrinth.com/data/cached_images/e29fad03fd9f8c9fbdcf664c1846d94049ee4507_0.webp)

Also you can use alias `&{rainbow}` to get rainbow text. (periods work here too)

![Rainbow color code showcase](https://cdn.modrinth.com/data/cached_images/02572d575252610e59798482f8455bb3c54d3af4_0.webp)

Note that "&" is a default custom color code symbol, so changing it in config affects advanced color codes syntax.

## Commands
- **/channel (/ch)** — used to select default writing channel and display info about current channel.
- **/message (/msg, /tell, /whisper)** — PM other players.
- **/reply (/r)** — reply to the latest PM.
- **/roll** — roll a dice with 6 (or you can select how much) sides.
- **/try** — describe some action and see if it successeed or not.
- **/me** — just a roleplay action.

## How to install
1. Make sure you are hosting a b1.7.3 BTA! Babric server.
2. Don't forget to install [ServerLibe](https://github.com/UselessSolutions/ServerLibe/releases), the only one dependency of this mod.
3. Drop the latest mod version to the `/mods` folder.
4. Customize chat in `config/cchat.cfg`.
5. Restart your server.
6. Enjoy!

---
_This mod uses a modified version of Fabric (Babric) and is designed only for Better than Adventure, a heavily modified version of Minecraft b1.7.3!_

