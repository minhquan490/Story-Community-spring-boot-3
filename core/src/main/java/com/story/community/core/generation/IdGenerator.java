package com.story.community.core.generation;

/**
 * Define how id to be generated, use for entity
 * 
 * @author hoangquan
 */
public interface IdGenerator {

    /**
     * Generate id for entity, must be a string
     * 
     * @return generated id
     */
    String generate();
}
