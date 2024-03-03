package io.github.ausf_software.smbp_editor.core.tool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация указывающая, что для данного поля класса будет установленно
 * значение из конфигурационного файла инструмента. Поле не должно быть <code>final</code>.
 * <p>Для каждого такого поля в классе должен содержаться метод сеттер. Его имя должно иметь
 * следующий формат: <code>getИмяполя</code> для поля <code>имяполя</code> или для
 * <code>Имяполя</code>
 *
 * <p>В качестве конфигурируемых значений поддерживаются следующие типы: <code>int</code>,
 * <code>short</code>, <code>byte</code>, <code>long</code>, <code>String</code>,
 * <code>Color</code>, <code>String[]</code>.
 *
 * <p>Примечание: для типа данных Color в файле указывается значение int являющееся кодом цвета в
 * формате rgb. (Подробнее в документации к классу)
 *
 * <p>Примечание: массив строк не должен содержать дубликатов (дубликаты будут игнорироваться) и
 * записывается в конфигурационном файле в следующем виде для поля с
 * именем <code>array</code>:
 * <pre>
 *     array_0=первое_значение
 *     array_1=второе_значение
 *     array_2=третье_значение
 * </pre>
 * @see java.awt.Color
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurableField {
}
