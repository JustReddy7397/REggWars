package ga.justreddy.wiki.reggwars.exceptions;

/**
 * @author JustReddy
 */
public class DependencyNotInstalledException extends RuntimeException {

    public DependencyNotInstalledException(String dependency) {
        super("Dependency " + dependency + " is not found, but you have it enabled");
    }

}
