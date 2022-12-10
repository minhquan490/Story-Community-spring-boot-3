package com.story.community.core.common.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Resource<T> {
    private final T value;
}
