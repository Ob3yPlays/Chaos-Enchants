# Chaos Enchants Forge

Minecraft Forge mod for **1.20.1**.

## What it does
Every item that enters a player's inventory gets random enchantments with random levels.

This includes items from:
- mining blocks
- chopping trees
- mob drops
- fishing
- crops
- chest loot when taken
- villages/dungeons/trial chamber loot when taken
- villager trades
- furnace smelting
- crafting
- barter/trading outputs
- basically anything that ends up in your inventory

## Main features
- Every item can receive every enchantment.
- Item compatibility is ignored.
- Impossible combinations are allowed.
- Random amount of enchantments: usually 1-5, rare chaos items can get 15-30.
- God Tier chance: **1% per enchant roll**.
- God Display levels can show huge values up to 999,999,999.
- Actual Minecraft 1.20.1 enchant levels are safely capped at 32767 because 1.20.1 stores vanilla enchant levels as shorts.
- Chaos Crystal reroll item.
- Commands included.

## Commands
- `/chaosenchants info`
- `/chaosenchants reroll hand`
- `/chaosenchants reroll offhand`
- `/chaosenchants reroll inventory` - OP only
- `/chaosenchants givecrystal <players> <amount>` - OP only

Alias:
- `/ce`

## Chaos Crystal
Crafting recipe:

```text
Diamond   Amethyst   Diamond
Amethyst Nether Star Amethyst
Diamond   Amethyst   Diamond
```

Use:
- Put the item you want to reroll in your offhand.
- Right-click with Chaos Crystal in your main hand.

## IntelliJ setup
1. Extract the zip.
2. Open the folder in IntelliJ IDEA Community Edition.
3. Let Gradle import/load.
4. Run:
   - `gradlew genIntellijRuns`
5. Refresh Gradle.
6. Use the generated Minecraft Client run config, or run:
   - `gradlew runClient`

## Build jar
Run:

```bash
gradlew build
```

The mod jar will be in:

```text
build/libs/
```
