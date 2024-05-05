## world2d

- add cap (also vertical and horizontal cap separately) to `Speed`.
- change the name of `Speed` to `Velocity`.
- movement action is not an action - this is confusing.
- CollisionFilter should have a raw version that gets ids as the parameters, not whole entities.
- Think about collision which do not stop anyone (like - a player picking up a power-up)
- Make hasType with id -> public (and hasName).
- collider should have a "clearCollisions" method???
- collider should have a way to just go through collisions
- add entity remover
- namesAndTypesService: getName, getType
- move entity manipulator to world2d
- add resetAnimation to entityManipulator
- remove type from the standard components
- rename NameAndTypeService to NameService or something
- rewrite entitymanipulator
- CollisionService should have utility methods for bounding boxes - like "isIntersecting", but also simpler: getWidth(idx), getBoundingBoxCopy(idx)
- GlobalTimer should produce Timer instances with the given duration
- entitymanipulator copies transform wrong way (not from the displayable)
- collider should remember the dx, dy before the collision

## tiled
- handle horizontal and vertical flipping

# direction service
- add isN, isS, isE, isW methods to the Direction enum.
- add WASD
- fix bug with EntityManipulator setType

# display
- negative scale could work for scale relative to the bounding box (so scale of -1 stretches image over the bounding box).
- switch width/height in formulas in RepresentationAction (fixes a bug)
- add a way to reset animation to the first frame
- pack flags in graphicsTransform to a single byte (the flips)