package com.story.community.core.resource.generator;

import java.util.concurrent.ThreadLocalRandom;

import com.story.community.core.generation.IdGenerator;

public class LightnovelIdGenerator implements IdGenerator {

    @Override
    public String generate() {
        StringBuilder idBuilder = new StringBuilder("LN-");
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        idBuilder.append(String.format("%06d", threadLocalRandom.nextInt(999999)));
        return idBuilder.toString();
    }

}
