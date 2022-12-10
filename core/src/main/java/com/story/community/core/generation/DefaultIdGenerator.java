package com.story.community.core.generation;

import java.util.concurrent.ThreadLocalRandom;

public class DefaultIdGenerator implements IdGenerator {

    @Override
    public String generate() {
        int id = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        return String.format("%09d", id);
    }
}