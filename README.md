# Mint

![build](https://github.com/octalide/mint/workflows/build/badge.svg?branch=master)
[![GitHub license](https://img.shields.io/github/license/octalide/mint)](https://github.com/octalide/mint/blob/master/LICENSE)
![Mod loader: Fabric](https://img.shields.io/badge/modloader-Fabric-1976d2)
<img src="https://i.imgur.com/Ol1Tcf8.png" alt="Requires Fabric API" width="96" height="32">

Mint is a mod for Minecraft that adds a few **Min**or **T**weaks.

The goal of mint is to improve QOL and automation capabilities while maintaining the vanilla minecraft "feel" we all hold so dear. This is achieved by keeping things wrapped into existing core gameplay mechanics. For example, most of the crafting recipes for new blocks are composed of vanilla items (no endless "item upgrade" chains found in larger mods)
and ALL recipes have minimal levels of crafting complexity. This means you won't be crafting item A for item B for item
C for item D for item... you get it.

> Mint is currently IN DEVELOPMENT. Not all features may be working or implemented.

Currently implemented and working:

- [x] Pipes
  - [x] Pipe
  - [x] Extractor Pipe
  - [x] Filtered Extractor Pipe
  - [x] Splitter Pipe
- [x] Destroyer
- [ ] Telescopic Piston
- [ ] Mining Drill

## Items

### Telescopic Beam

The Telescopic Beam is an item used to craft a few of the blocks in mint, namely the Telescopic Piston and Mining Drill.

## Blocks

### Pipes

#### Pipe

A pipe is similar to a vanilla Minecraft Hopper, but it:

- is slightly cheaper!
- has a single-tile inventory size
- can be placed in any orientation
- cannot pick up items from the surrounding area or pull from inventories

In effect, this turns the vanilla Hopper into an item *collection* utility, while Pipes can be used for item *
transportation*.

#### Extractor Pipe

An Extractor Pipe pulls from the inventory it's connected to (similar to a hopper, but not restricted to direction).

#### Filtered Extractor Pipe

A Filter Pipe allows for whitelisting a single item type. Simple enough!

#### Splitter Pipe

A Splitter Pipe can branch out to multiple destinations, cycling output evenly between them. No more janky hopper configurations!

### Destroyer

When given a redstone signal, a Destroyer will break the block in the direction it's
facing. Note that the Destroyer does NOT have an internal inventory and WILL leave the resulting item drop where it
spawns. Use a hopper!

### Telescopic Piston

The Telescopic Piston behaves like a normal minecraft piston, but extends to the level of redstone signal it receives, up to 15 blocks. This is an INCREDIBLY useful tool for a very large number of reasons.

The Telescopic Piston also has a sticky variant.

### Mining Drill

The Mining Drill is exactly what it sounds like. The Mining Drill is a block that, when placed and powered with a redstone signal, will extend in the direction it's facing until it encounters a block, which it will break and collect.

Items mined by the drill can be extracted from its single tile inventory with a hopper, pipe, or by hand.

## Plans

I have many plans for Mint's future. This is a brand new mod at the time of writing, so stay tuned for future changes!
