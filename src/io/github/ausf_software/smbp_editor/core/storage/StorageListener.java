package io.github.ausf_software.smbp_editor.core.storage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация <code>StorageListener</code> используется для обозначения метода как
 * реакцию на изменение указанных в массиве элементов хранилища. В случае если
 * данный элемент в хранилище отсутствует метод будет игнорироваться.
 * <p>Запрошенные значения для прослушивания должны указываться в том же порядке в
 * качестве аргументов, что и в перечислении в аннотации.
 * <p>Пример использования:
 *
 *     <pre>
 *     &#64;StorageListener(names = { "one1", "one3", "one2" })
 *     public void test(Long one1, Long one3, Long one2) {
 *         System.out.println("one1: " + one1);
 *         System.out.println("one3: " + one3);
 *         System.out.println("one2: " + one2);
 *     }
 *     </pre>
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StorageListener {
    /**
     * Возвращает массив строк с именами значений в хранилище.
     * @return массив строк с именами значений в хранилище
     */
    String[] names();
}
