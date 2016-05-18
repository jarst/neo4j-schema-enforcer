package net.coderefactory.neo4j.schemaenforcer.validation;

public class SchemaViolationException extends Exception {

    SchemaViolationException(final String message) {
        super(message);
    }
}
