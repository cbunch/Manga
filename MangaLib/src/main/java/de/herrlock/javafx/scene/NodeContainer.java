package de.herrlock.javafx.scene;

import javafx.scene.Node;

/**
 * @author HerrLock
 */
public abstract class NodeContainer {
    private final Node node = createNode();

    protected abstract Node createNode();

    public Node getNode() {
        return this.node;
    }
}