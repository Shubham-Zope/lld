package com.parkinglot.model;

import com.parkinglot.enums.GateType;

public class Gate {
    private final String id;
    private final GateType type;

    public Gate(String id, GateType type) {
        if (id == null || id.isBlank() || type == null)
            throw new IllegalArgumentException("Invalid gate");
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public GateType getType() {
        return type;
    }
}
