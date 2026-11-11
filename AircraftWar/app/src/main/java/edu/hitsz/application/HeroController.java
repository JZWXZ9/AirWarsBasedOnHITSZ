package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/** Moves the soul with the mouse, within the current play area. */
public final class HeroController {
    private final HeroAircraft heroAircraft;
    private final Node inputNode;
    private double minX;
    private double minY;
    private double maxX = Main.WINDOW_WIDTH;
    private double maxY = Main.WINDOW_HEIGHT;

    public HeroController(Node inputNode, HeroAircraft heroAircraft) {
        this.inputNode = inputNode;
        this.heroAircraft = heroAircraft;
        inputNode.addEventHandler(MouseEvent.MOUSE_MOVED, this::handleMouse);
        inputNode.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouse);
    }

    public void setMovementBounds(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        moveTo(heroAircraft.getLocationX(), heroAircraft.getLocationY());
    }

    private void handleMouse(MouseEvent event) {
        Point2D position = inputNode.sceneToLocal(event.getSceneX(), event.getSceneY());
        moveTo(position.getX(), position.getY());
    }

    private void moveTo(double x, double y) {
        double halfWidth = heroAircraft.getWidth() / 2.0;
        double halfHeight = heroAircraft.getHeight() / 2.0;
        heroAircraft.setLocation(clamp(x, minX + halfWidth, maxX - halfWidth),
                clamp(y, minY + halfHeight, maxY - halfHeight));
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
