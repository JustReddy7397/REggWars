package ga.justreddy.wiki.reggwars.bungee.server.balancer;

import ga.justreddy.wiki.reggwars.bungee.server.balancer.elements.LoadBalancerObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public abstract class BaseBalancer<T extends LoadBalancerObject> implements LoadBalancer<T> {

    private Map<String, T> objects;
    protected List<T> nextObj;

    public BaseBalancer() {
        this.objects = new HashMap<>();
        this.nextObj = new ArrayList<>();
    }

    public BaseBalancer(Map<String, T> map) {
        addAll(map);
    }

    public void destroy() {
        this.objects.clear();
        this.nextObj.clear();
    }

    public void add(String id, T obj) {
        objects.put(id, obj);
        update();
    }

    public T get(String id) {
        return objects.get(id);
    }

    public void remove(String id) {
        objects.remove(id);
        update();
    }

    public void addAll(Map<String, T> map) {
        if (objects != null)
            objects.clear();
        objects = map;
        update();
    }

    public List<T> getList() {
        return nextObj;
    }

    public void update() {
        if (nextObj != null)
            nextObj.clear();
        nextObj = new ArrayList<>();
        nextObj.addAll(objects.values());
    }

    public abstract int getTotalNumber();

}
