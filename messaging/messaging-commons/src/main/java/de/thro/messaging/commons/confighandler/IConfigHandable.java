package de.thro.messaging.commons.confighandler;

/**
 * Wir brauchen die Klasse weil Java Generics es nicht einfach hergeben, den Klassennamen herauszurücken.
 * *freude*
 */
interface IConfigHandable {
    /**
     * gibt mir den Name der Klasse
     * @return gibt Name der Klasse aus
     */
    String getClassName();
}
