package ga.justreddy.wiki.reggwars.bungee.server.balancer;

import ga.justreddy.wiki.reggwars.bungee.server.balancer.elements.LoadBalancerObject;

/**
 * @author JustReddy
 */
public interface LoadBalancer<T extends LoadBalancerObject> {

    T next();

}
