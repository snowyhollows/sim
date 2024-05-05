## general

Move to github and use jitpack.io for dependencies.
Remove "visitor" from everywhere
Make just any release, to stabilize things for some games.

## tiled:
- (?) add inheritance of layer properties to layer objects
- change `data.put("width", Integer.toString(width - 1))` to `putIfAbsent` in `TiledSingleMap`
- remove special casing of tile.name and tile.type - replace with full support for properties (except for object references)
- remove trimming object sizes (-1), make widths, heights and positions floats (instead of integers)

