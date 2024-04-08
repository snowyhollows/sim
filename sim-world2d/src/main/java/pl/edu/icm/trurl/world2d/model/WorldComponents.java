package pl.edu.icm.trurl.world2d.model;

public final class WorldComponents {
    private WorldComponents() {}

    public static final Class<AnimationComponent> ANIMATION = AnimationComponent.class;
    public static final Class<AnimationFrame> ANIMATION_FRAME = AnimationFrame.class;
    public static final Class<BoundingBox> BOUNDING_BOX = BoundingBox.class;
    public static final Class<Collider> COLLIDER = Collider.class;
    public static final Class<Constraint> CONSTRAINT = Constraint.class;
    public static final Class<Displayable> DISPLAYABLE = Displayable.class;
    public static final Class<Focusable> FOCUSABLE = Focusable.class;
    public static final Class<GraphicsTransform> GRAPHICS_TRANSFORM = GraphicsTransform.class;
    public static final Class<Named> NAMED = Named.class;
    public static final Class<Speed> SPEED = Speed.class;
    public static final Class<TextureComponent> TEXTURE_COMPONENT = TextureComponent.class;
    public static final Class<TextureFragmentComponent> TEXTURE_FRAGMENT_COMPONENT = TextureFragmentComponent.class;
    public static final Class<Timer> TIMER = Timer.class;
    public static final Class<Typed> TYPED = Typed.class;
}
