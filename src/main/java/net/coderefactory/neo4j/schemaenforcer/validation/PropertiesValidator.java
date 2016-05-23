package net.coderefactory.neo4j.schemaenforcer.validation;

import net.coderefactory.neo4j.schemaenforcer.schema.Schema;
import net.coderefactory.neo4j.schemaenforcer.schema.SchemaProvider;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.event.PropertyEntry;
import org.neo4j.kernel.impl.logging.LogService;
import org.neo4j.logging.Log;

import java.util.Map;

public class PropertiesValidator {

    private final SchemaProvider schemaProvider;
    private final Log log;

    public PropertiesValidator(final SchemaProvider schemaProvider, final LogService logService) {
        this.schemaProvider = schemaProvider;
        this.log =  logService.getUserLog(PropertiesValidator.class);
    }

    public void validatePropertyEntry(PropertyEntry<? extends PropertyContainer> prop) throws SchemaViolationException {
        final Schema schema = schemaProvider.getSchema(prop.entity());

        final String key = prop.key();
        final String propType = schema.getType(key);
        if (propType != null) {
            validateProperty(key, propType, prop.value());
        }
    }

    private void validateProperty(final String key, final String propType, final Object value) throws SchemaViolationException {
        final PropertyTypeValidator propertyTypeValidator = PropertyTypeValidatorFactory.get(propType);
        if (propertyTypeValidator != null) {
            if (!propertyTypeValidator.isValid(value)) {
                rollback(key, propertyTypeValidator.getAllowedTypeName());
            }
        } else {
            log.warn("Unsupported property type:" + propType);
        }
    }

    private void rollback(final String property, final String type) throws SchemaViolationException {
        final String msg = "Constraint violation: property '" + property + "' was not of type: " + type;
        log.error(msg);
        throw new SchemaViolationException(msg);
    }
}
