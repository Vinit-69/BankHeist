package Custom;

import java.util.*;

public class CustomHashMap<K, V> {

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private int capacity = 16;
    private int size = 0;

    private Entry<K, V>[] table;

    @SuppressWarnings("unchecked")
    public CustomHashMap() {
        table = new Entry[capacity];
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    public void put(K key, V value) {
        int index = hash(key);
        Entry<K, V> head = table[index];

        while (head != null) {
            if (Objects.equals(head.key, key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        Entry<K, V> newEntry = new Entry<>(key, value, table[index]);
        table[index] = newEntry;
        size++;

        float loadFactor = 0.75f;
        if ((1.0 * size) / capacity >= loadFactor) {
            resize();
        }
    }

    public V get(K key) {
        int index = hash(key);
        Entry<K, V> head = table[index];

        while (head != null) {
            if (Objects.equals(head.key, key)) {
                return head.value;
            }
            head = head.next;
        }

        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V remove(K key) {
        int index = hash(key);
        Entry<K, V> head = table[index];
        Entry<K, V> prev = null;

        while (head != null) {
            if (Objects.equals(head.key, key)) {
                if (prev != null) {
                    prev.next = head.next;
                } else {
                    table[index] = head.next;
                }
                size--;
                return head.value;
            }
            prev = head;
            head = head.next;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity = capacity * 2;
        Entry<K, V>[] oldTable = table;
        table = new Entry[capacity];
        size = 0;

        for (Entry<K, V> headNode : oldTable) {
            while (headNode != null) {
                put(headNode.key, headNode.value);
                headNode = headNode.next;
            }
        }
    }

    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (Entry<K, V> head : table) {
            while (head != null) {
                keys.add(head.key);
                head = head.next;
            }
        }
        return keys;
    }

    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        for (Entry<K, V> head : table) {
            while (head != null) {
                values.add(head.value);
                head = head.next;
            }
        }
        return values;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entries = new HashSet<>();
        for (Entry<K, V> head : table) {
            while (head != null) {
                entries.add(new AbstractMap.SimpleEntry<>(head.key, head.value));
                head = head.next;
            }
        }
        return entries;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    @Override
    public String toString() {
        return entrySet().toString();
    }
}
