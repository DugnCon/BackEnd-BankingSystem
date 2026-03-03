package com.damdung.banking.utils.QrUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Map;

public class EmvNode {

    private String id;
    private String value;
    private Map<String, EmvNode> children = new LinkedHashMap<>();

    public EmvNode(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public void addChild(EmvNode node) {
        children.put(node.id, node);
    }

    public EmvNode get(String id) {
        return children.get(id);
    }

    public String getValue() {
        return value;
    }

    public Map<String, EmvNode> getChildren() {
        return children;
    }

    public String getId() {
        return id;
    }
}