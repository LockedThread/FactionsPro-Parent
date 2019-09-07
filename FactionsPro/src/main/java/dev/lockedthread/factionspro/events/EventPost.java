package dev.lockedthread.factionspro.events;

import dev.lockedthread.factionspro.modules.Module;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventPost<T extends Event> {

    private final Class<T> eventClass;
    protected LinkedList<Predicate<T>> filters;
    private EventPriority eventPriority;
    private Consumer<T> eventConsumer;

    private EventPost(Class<T> eventClass, EventPriority eventPriority) {
        this.eventClass = eventClass;
        this.eventPriority = eventPriority;
    }

    @NotNull
    public static <T extends Event> EventPost<T> of(Class<T> eventClass, EventPriority eventPriority) {
        return new EventPost<>(eventClass, eventPriority);
    }

    @NotNull
    public static <T extends Event> EventPost<T> of(Class<T> eventClass) {
        return new EventPost<>(eventClass, EventPriority.NORMAL);
    }

    @NotNull
    public EventPost<T> filter(Predicate<T> event) {
        getFilters().add(event);
        return this;
    }

    @NotNull
    LinkedList<Predicate<T>> getFilters() {
        return filters == null ? filters = new LinkedList<>() : filters;
    }

    @NotNull
    Class<? extends Event> getEventClass() {
        return eventClass;
    }

    @NotNull
    public EventPoster handle(Consumer<T> event) {
        this.eventConsumer = event;
        return plugin -> {
            if (plugin instanceof Module) {
                Module module = (Module) plugin;
                module.getEventPosts().add(EventPost.this);
            }
            new EventPostExecutor<>(EventPost.this, plugin);
        };
    }

    @NotNull
    EventPriority getEventPriority() {
        return eventPriority;
    }

    public void setEventPriority(EventPriority eventPriority) {
        this.eventPriority = eventPriority;
    }

    @NotNull
    Consumer<T> getEventConsumer() {
        return eventConsumer;
    }

}
