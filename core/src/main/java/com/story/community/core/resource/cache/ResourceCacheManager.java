package com.story.community.core.resource.cache;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

import org.springframework.cache.Cache;
import org.springframework.cache.jcache.JCacheCache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.story.community.core.common.exception.CacheStorageResolveException;

import lombok.extern.log4j.Log4j2;

/**
 * The cache manager use in resource server. Support auto detect entity for
 * create cache storage
 * 
 * @author hoanquan
 */
@Log4j2
public class ResourceCacheManager extends AbstractTransactionSupportingCacheManager {

    private CacheObjectLoader cacheObjectLoader;
    private boolean isAllowNullValues;
    private String basePackage;
    private CachingProvider cachingProvider;

    /***
     * Create new {@code CacheManager} with cache location scanning package
     * 
     * @param basePackage base package contain cache storage specific
     */
    public ResourceCacheManager(String basePackage) {
		Assert.notNull(basePackage == null, "Please specify where package contain entity cache");
        this.basePackage = basePackage;
        cacheObjectLoader = new DefaultCacheObjectLoader(ClassUtils.getDefaultClassLoader());
        isAllowNullValues = false;
		cachingProvider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
		Collection<Cache> caches = new LinkedHashSet<>();
		try {
			Collection<CacheObject> cacheObjects = cacheObjectLoader.loadCacheObject(basePackage);
			for (CacheObject co : cacheObjects) {
				String cacheStorageName = co.getCacheStorageName();
				if (cacheStorageName == null) {
					continue;
				}
				Cache cache = new JCacheCache(
                        createCache(co.getCacheStorageName()),
                        isAllowNullValues);
                caches.add(cache);
            }
		} catch (CacheStorageResolveException e) {
			log.info("Skip");
        }
        return caches;
    }

    /***
     * Config cache storage allow null value
     * 
     * @param isAllowNullValues true if null value is allow
     */
    public void setAllowNullValues(boolean isAllowNullValues) {
        this.isAllowNullValues = isAllowNullValues;
    }

    public boolean isAllowNullValues() {
        return isAllowNullValues;
    }

    public CacheManager getCacheManager() {
        return cachingProvider.getCacheManager();
    }

    @Override
    public void setTransactionAware(boolean transactionAware) {
        // Doesn't allow change transaction aware option
    }

    /**
     * Create {@code javax.cache.Cache} with given cache name
     * 
     * @param cacheName name of cache storage
     * @return {@code javax.cache.Cache} object with key and value are
     *         {@code Object} type
     */
    private javax.cache.Cache<Object, Object> createCache(String cacheName) {
        MutableConfiguration<Object, Object> configuration = new MutableConfiguration<>();
        configuration.setTypes(Object.class, Object.class);
        configuration.setStoreByValue(false);
        configuration.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ETERNAL));
        return getCacheManager().createCache(cacheName, configuration);
    }
}
