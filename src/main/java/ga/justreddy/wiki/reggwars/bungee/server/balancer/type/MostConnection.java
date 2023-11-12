package ga.justreddy.wiki.reggwars.bungee.server.balancer.type;

import ga.justreddy.wiki.reggwars.bungee.server.balancer.BaseBalancer;
import ga.justreddy.wiki.reggwars.bungee.server.balancer.elements.LoadBalancerObject;
import ga.justreddy.wiki.reggwars.bungee.server.balancer.elements.NumberConnection;

/**
 * @author JustReddy
 */
public class MostConnection<T extends LoadBalancerObject & NumberConnection>
        extends BaseBalancer<T> {
    @Override
    public int getTotalNumber() {
        int number = 0;
        for (T item : nextObj) {
            number += item.getActualNumber();
        }
        return number;
    }

    @Override
    public T next() {
        T obj = null;
        if (nextObj != null) {
            if (!nextObj.isEmpty()) {
                for (T item : nextObj) {
                    if (!item.canBeSelected()) {
                        continue;
                    }

                    if (obj == null) {
                        obj = item;
                        continue;
                    }

                    if (obj.getActualNumber() < item.getActualNumber()) {
                        obj = item;
                    }
                }
            }
        }

        return obj;
    }
}
