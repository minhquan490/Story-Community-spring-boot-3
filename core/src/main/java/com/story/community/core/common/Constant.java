package com.story.community.core.common;

public final class Constant {
    private Constant() {
    }

    public static final String MONGO_DATABASE_NAME = "Story_Community";

    public static final String MONGO_ENTITIES_PACKAGE_STRING = "com.story.community.core.resource.entities";

    public static final String RESOURCE_SERVER_URL = "https://localhost:8080";
    public static final String GATE_WAY_URL = "https://localhost:8081";
    public static final String AUTHENTICATION_SERVER_URL = "https://localhost:8082";
    public static final String COMMUNITY_SYSTEM_URL = "https://localhost:8083";

    public static final String AUTHORIZE_HEADER = "Authorize";
    public static final String REFRESH_TOKEN_HEADER = "Refresh";
    public static final String IGNORE_AUTHORIZE_HEADER = "Ignore_Authorize";

    public static final String KEY_GET_ALL_VALUE_CACHE = "all";
}
