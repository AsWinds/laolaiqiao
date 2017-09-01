package com.codi.laolaiqiao.common.util;

import java.util.HashMap;
import java.util.Map;

public class DebugableHashMap<K, V> extends HashMap<K, V> {

    private static final long serialVersionUID = 1L;
    
    public DebugableHashMap(int initialCapacity, float loadFactor) {
    	super(initialCapacity, loadFactor);
    }

    public DebugableHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs an empty <tt>HashMap</tt> with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public DebugableHashMap() {
        super();
    }

    /**
     * Constructs a new <tt>HashMap</tt> with the same mappings as the
     * specified <tt>Map</tt>.  The <tt>HashMap</tt> is created with
     * default load factor (0.75) and an initial capacity sufficient to
     * hold the mappings in the specified <tt>Map</tt>.
     *
     * @param   m the map whose mappings are to be placed in this map
     * @throws  NullPointerException if the specified map is null
     */
    public DebugableHashMap(Map<? extends K, ? extends V> m) {
    	super(m);
    }
    
    @Override
    public V get(Object key) {
        System.out.println(key);
        return super.get(key);
    }
    
    @Override
    public V getOrDefault(Object key, V defaultValue) {
    	System.out.println(key + " : " + defaultValue);
        return super.getOrDefault(key, defaultValue);
    }
}
