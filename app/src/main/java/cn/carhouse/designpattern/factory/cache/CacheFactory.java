package cn.carhouse.designpattern.factory.cache;

public class CacheFactory {
    public static <C extends Cache> C createCache(Class<C> clz) {
        C cache = null;
        try {
            cache = clz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cache;
    }

    public static XmlCache getXmlCache() {
        return createCache(XmlCache.class);
    }

    public static FileCache getFileCache() {
        return createCache(FileCache.class);
    }
}
