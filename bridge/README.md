# Bounce Bridge

Minestom extension that teaches CloudNet's bridge module how Bounce resolves permissions —
it registers a `MinestomPermissionChecker` backed by Adventure's `PermissionChecker.POINTER`,
the same pointer `PermissionAwarePlayer` installs and LuckPerms answers through.

## Why this exists

CloudNet's bridge ships a default permission checker that only inspects
`player.getPermissionLevel()`, which is always `0` on a LuckPerms-managed server. Without this
extension, CloudNet's maintenance-mode bypass and any task-level `requiredPermission` check
reject every player — staff included.

## Deployment

This module is never bundled into the game or setup fat jars. Build it and drop the resulting
jar into the CloudNet service's `extensions/` folder, next to the `CloudNet_Bridge` extension
it depends on (declared via `dependencies = "CloudNet_Bridge"` on
`BounceBridgePermissionExtension`, so it will refuse to load before the bridge is present):

```
./gradlew :bridge:build
cp bridge/build/libs/bridge-*.jar <cloudnet-service>/extensions/
```

If this jar is missing from a service's `extensions/` folder, the server boots and runs
normally — CloudNet's bridge silently falls back to its `getPermissionLevel()`-based checker,
reproducing the exact maintenance-mode/permission-gate bug this extension exists to fix. There
is currently no log line anywhere that flags a missing extension; treat this file as the
authoritative reminder until that gap is closed.
