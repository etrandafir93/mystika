package com.mystika.tarot.drawings;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
class DrawingRepository {

    private final Map<String, Drawing> drawings = new HashMap<>();

    Drawing save(Drawing drawing) {
        drawings.put(drawing.id(), drawing);
        return drawing;
    }

    Drawing byId(String id) {
        return drawings.get(id);
    }

}
