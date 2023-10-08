package db;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MySqlParameterSource extends BeanPropertySqlParameterSource {
    /**
     * Create a new BeanPropertySqlParameterSource for the given bean.
     *
     * @param object the bean instance to wrap
     */
    public MySqlParameterSource(Object object) {
        super(object);
    }

    /**
     * Override to support enum handling.
     *
     * @param paramName the name of the parameter
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Object getValue(String paramName) throws IllegalArgumentException {
        Object value = super.getValue(paramName);
        if (value instanceof Enum<?> e) {
            return e.name();
        }
        return value;
    }

    public static MySqlParameterSource of(Object object) {
        return new MySqlParameterSource(object);
    }

    public static MySqlParameterSource[] createBatch(Collection<?> objects) {
        var batch = new ArrayList<MySqlParameterSource>();

        for (Object object : objects) {
            batch.add(MySqlParameterSource.of(object));
        }

        return batch.toArray(new MySqlParameterSource[0]);
    }
}
