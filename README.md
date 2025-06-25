## Bounce

**Bounce** is a small Minecraft minigame written in Java that uses the [Minestom](https://github.com/Minestom/Minestom)
framework.

The gameplay is simple and fast-paced: once the game starts, players are teleported into a small arena where they
compete by eliminating each other to earn points. When time runs out, the player with the highest score wins.

To add a fun twist, players bounce around the arena, making it challenging to land hits. The amount of bouncing depends
on the type of block the player is standing on, adding a strategic element to movement and positioning.

## Limitations

The current version of the game has a few limitations:

- When players have the same score at the end of the game, the winner is determined by who reached that score earlier.
  This is tracked using a timestamp stored when a player’s score changes.
- The playing area must be surrounded by lava blocks at the edges. If a player falls out of the arena, they are
  teleported back to the center. The lava block type is currently hardcoded in the game.

> [!NOTE]
> Some of the used dependencies are not available in a public repository like Maven Central or JitPack.
> So it's not possible to build the project event when you create a fork of it.
> We appreciate any feedback or issues reports regarding the code.
