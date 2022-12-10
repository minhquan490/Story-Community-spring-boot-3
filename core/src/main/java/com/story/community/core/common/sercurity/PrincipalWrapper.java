package com.story.community.core.common.sercurity;

/**
 * The holder hold customer information use in secutrity
 * 
 * @author hoangquan
 */
public class PrincipalWrapper<T> {
    private T principal;

    /**
     * Check the {@code principal} is present in {@code PrincipalWrapper}
     * 
     * @return true if {@code principal} is present otherwise false
     */
    public boolean isPresent() {
        return principal != null;
    }

    /**
     * Wrap the pricipal into this wrapper
     * 
     * @param principal for wrapping
     */
    public void wrap(T principal) {
        this.principal = principal;
    }

    /**
     * Return the principal in this wrapper. Must be called after call
     * {@code isPresent}
     * 
     * @throws NullPointerException if the wapper not wrap any principal
     * @return the principal
     */
    public T getPrincipal() {
        if (!isPresent()) {
            throw new NullPointerException("Call isPresent before this method");
        }
        return principal;
    }
}
