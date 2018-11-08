package de.htw.ai.kbe.beleg1;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface RunMe {
}

//Interface welches die Annotation @RunMe definiert
//@Target definiert, dass nur Methoden diese Annotationen haben dürfen
//@Retention definiert, wann die Annotation genutzt wird, in diesem Fall zur Laufzeit der Programms
