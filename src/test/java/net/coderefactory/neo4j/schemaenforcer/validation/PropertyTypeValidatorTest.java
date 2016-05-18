package net.coderefactory.neo4j.schemaenforcer.validation;

import net.coderefactory.neo4j.schemaenforcer.validation.PropertyTypeValidator;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests for {@link PropertyTypeValidator}.
 */
public class PropertyTypeValidatorTest {

    private final boolean boolValue = false;
    private final int intValue = 42;
    private final long longValue = 12_345_567_890L;
    private final float floatValue = 2.71828f;
    private final double doubleVlaue = 3.14159 ;
    private final String stringValue = "the string";

    private final boolean[] boolArray = new boolean[]{true, false};
    private final int[] intArray = new int[]{1, 2, 3};
    private final long[] longArray = new long[]{123L, 321L};
    private final float[] floatArray = new float[]{3.333f};
    private final double[] doubleArray = new double[]{0.00001, 0.00002};
    private final String[] stringArray = new String[]{"even", "more", "strings"};
    private final Object[] emptyArray = new Object[0];

    private PropertyTypeValidator validator;

    @Test
    public void testBool() throws Exception {
        validator = PropertyTypeValidator.get(PropertyTypeValidator.BOOL);

        assertEquals(true, validator.isValid(boolValue));
        assertEquals(false, validator.isValid(intValue));
        assertEquals(false, validator.isValid(longValue));
        assertEquals(false, validator.isValid(floatValue));
        assertEquals(false, validator.isValid(doubleVlaue));
        assertEquals(false, validator.isValid(stringValue));

        assertEquals(false, validator.isValid(boolArray));
        assertEquals(false, validator.isValid(intArray));
        assertEquals(false, validator.isValid(longArray));
        assertEquals(false, validator.isValid(floatArray));
        assertEquals(false, validator.isValid(doubleArray));
        assertEquals(false, validator.isValid(stringArray));
        assertEquals(false, validator.isValid(emptyArray));
    }

    @Test
    public void testInt() throws Exception {
        validator = PropertyTypeValidator.get(PropertyTypeValidator.INT);

        assertEquals(false, validator.isValid(boolValue));
        assertEquals(true, validator.isValid(intValue));
        assertEquals(true, validator.isValid(longValue));
        assertEquals(false, validator.isValid(floatValue));
        assertEquals(false, validator.isValid(doubleVlaue));
        assertEquals(false, validator.isValid(stringValue));

        assertEquals(false, validator.isValid(boolArray));
        assertEquals(false, validator.isValid(intArray));
        assertEquals(false, validator.isValid(longArray));
        assertEquals(false, validator.isValid(floatArray));
        assertEquals(false, validator.isValid(doubleArray));
        assertEquals(false, validator.isValid(stringArray));
        assertEquals(false, validator.isValid(emptyArray));
    }

    @Test
    public void testNumber() throws Exception {
        validator = PropertyTypeValidator.get(PropertyTypeValidator.NUMBER);

        assertEquals(false, validator.isValid(boolValue));
        assertEquals(true, validator.isValid(intValue));
        assertEquals(true, validator.isValid(longValue));
        assertEquals(true, validator.isValid(floatValue));
        assertEquals(true, validator.isValid(doubleVlaue));
        assertEquals(false, validator.isValid(stringValue));

        assertEquals(false, validator.isValid(boolArray));
        assertEquals(false, validator.isValid(intArray));
        assertEquals(false, validator.isValid(longArray));
        assertEquals(false, validator.isValid(floatArray));
        assertEquals(false, validator.isValid(doubleArray));
        assertEquals(false, validator.isValid(stringArray));
        assertEquals(false, validator.isValid(emptyArray));
    }

    @Test
    public void testString() throws Exception {
        validator = PropertyTypeValidator.get(PropertyTypeValidator.STRING);

        assertEquals(false, validator.isValid(boolValue));
        assertEquals(false, validator.isValid(intValue));
        assertEquals(false, validator.isValid(longValue));
        assertEquals(false, validator.isValid(floatValue));
        assertEquals(false, validator.isValid(doubleVlaue));
        assertEquals(true, validator.isValid(stringValue));

        assertEquals(false, validator.isValid(boolArray));
        assertEquals(false, validator.isValid(intArray));
        assertEquals(false, validator.isValid(longArray));
        assertEquals(false, validator.isValid(floatArray));
        assertEquals(false, validator.isValid(doubleArray));
        assertEquals(false, validator.isValid(stringArray));
        assertEquals(false, validator.isValid(emptyArray));
    }

    @Test
    public void testBoolArray() throws Exception {
        validator = PropertyTypeValidator.get(PropertyTypeValidator.ARRAY_BOOL);

        assertEquals(false, validator.isValid(boolValue));
        assertEquals(false, validator.isValid(intValue));
        assertEquals(false, validator.isValid(longValue));
        assertEquals(false, validator.isValid(floatValue));
        assertEquals(false, validator.isValid(doubleVlaue));
        assertEquals(false, validator.isValid(stringValue));

        assertEquals(true, validator.isValid(boolArray));
        assertEquals(false, validator.isValid(intArray));
        assertEquals(false, validator.isValid(longArray));
        assertEquals(false, validator.isValid(floatArray));
        assertEquals(false, validator.isValid(doubleArray));
        assertEquals(false, validator.isValid(stringArray));
        assertEquals(true, validator.isValid(emptyArray));
    }

    @Test
    public void testIntArray() throws Exception {
        validator = PropertyTypeValidator.get(PropertyTypeValidator.ARRAY_INT);

        assertEquals(false, validator.isValid(boolValue));
        assertEquals(false, validator.isValid(intValue));
        assertEquals(false, validator.isValid(longValue));
        assertEquals(false, validator.isValid(floatValue));
        assertEquals(false, validator.isValid(doubleVlaue));
        assertEquals(false, validator.isValid(stringValue));

        assertEquals(false, validator.isValid(boolArray));
        assertEquals(true, validator.isValid(intArray));
        assertEquals(true, validator.isValid(longArray));
        assertEquals(false, validator.isValid(floatArray));
        assertEquals(false, validator.isValid(doubleArray));
        assertEquals(false, validator.isValid(stringArray));
        assertEquals(true, validator.isValid(emptyArray));
    }

    @Test
    public void testNumberArray() throws Exception {
        validator = PropertyTypeValidator.get(PropertyTypeValidator.ARRAY_NUMBER);

        assertEquals(false, validator.isValid(boolValue));
        assertEquals(false, validator.isValid(intValue));
        assertEquals(false, validator.isValid(longValue));
        assertEquals(false, validator.isValid(floatValue));
        assertEquals(false, validator.isValid(doubleVlaue));
        assertEquals(false, validator.isValid(stringValue));

        assertEquals(false, validator.isValid(boolArray));
        assertEquals(true, validator.isValid(intArray));
        assertEquals(true, validator.isValid(longArray));
        assertEquals(true, validator.isValid(floatArray));
        assertEquals(true, validator.isValid(doubleArray));
        assertEquals(false, validator.isValid(stringArray));
        assertEquals(true, validator.isValid(emptyArray));
    }

    @Test
    public void testStringArray() throws Exception {
        validator = PropertyTypeValidator.get(PropertyTypeValidator.ARRAY_STRING);

        assertEquals(false, validator.isValid(boolValue));
        assertEquals(false, validator.isValid(intValue));
        assertEquals(false, validator.isValid(longValue));
        assertEquals(false, validator.isValid(floatValue));
        assertEquals(false, validator.isValid(doubleVlaue));
        assertEquals(false, validator.isValid(stringValue));

        assertEquals(false, validator.isValid(boolArray));
        assertEquals(false, validator.isValid(intArray));
        assertEquals(false, validator.isValid(longArray));
        assertEquals(false, validator.isValid(floatArray));
        assertEquals(false, validator.isValid(doubleArray));
        assertEquals(true, validator.isValid(stringArray));
        assertEquals(true, validator.isValid(emptyArray));
    }
}