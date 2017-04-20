package de.uniorg.ui5helper.cache;

import com.intellij.ide.caches.CachesInvalidator;

public class CacheInvalidator extends CachesInvalidator {
    @Override
    public void invalidateCaches() {
        System.out.println("invalidate caches");
        CacheStorage.getInstance().invalidate();
    }
}
