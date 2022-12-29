package org.pipeman.dccapi.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventEmitter {
    private final Map<Class<?>, Method> methodMap;
    private final List<Listener> listeners = new ArrayList<>();

    public EventEmitter() {
        Method[] methods = Listener.class.getMethods();
        methodMap = new HashMap<>(methods.length);
        for (Method method : methods) {
            methodMap.put(method.getParameterTypes()[0], method);
        }
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public boolean removeListener(Listener listener) {
        return listeners.remove(listener);
    }

    public void emitEvent(DiscordEvent event) {
        for (Listener listener : listeners) {
            try {
                methodMap.get(event.getClass()).invoke(listener, event);
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        }
    }
}
