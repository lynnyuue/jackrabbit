/*
 * Copyright 2004-2005 The Apache Software Foundation or its licensors,
 *                     as applicable.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.core.value;

import org.apache.jackrabbit.core.util.ISO8601;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import java.util.Calendar;

/**
 * A <code>DateValue</code> provides an implementation
 * of the <code>Value</code> interface representing a date value.
 */
public class DateValue extends BaseValue {

    public static final int TYPE = PropertyType.DATE;

    private final Calendar date;

    /**
     * Constructs a <code>DateValue</code> object representing a date.
     *
     * @param date the date this <code>DateValue</code> should represent
     */
    public DateValue(Calendar date) {
        super(TYPE);
        this.date = date;
    }

    /**
     * Returns a new <code>DateValue</code> initialized to the value
     * represented by the specified <code>String</code>.
     * <p/>
     * The specified <code>String</code> must be a ISO8601-compliant date/time
     * string.
     *
     * @param s the string to be parsed.
     * @return a newly constructed <code>DateValue</code> representing the
     *         the specified value.
     * @throws ValueFormatException If the <code>String</code> is not a valid
     *                              ISO8601-compliant date/time string.
     * @see ISO8601
     */
    public static DateValue valueOf(String s) throws ValueFormatException {
        Calendar cal = ISO8601.parse(s);
        if (cal != null) {
            return new DateValue(cal);
        } else {
            throw new ValueFormatException("not a valid date format");
        }
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p/>
     * The result is <code>true</code> if and only if the argument is not
     * <code>null</code> and is a <code>DateValue</code> object that
     * represents the same value as this object.
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DateValue) {
            DateValue other = (DateValue) obj;
            if (date == other.date) {
                return true;
            } else if (date != null && other.date != null) {
                return date.equals(other.date);
            }
        }
        return false;
    }


    //------------------------------------------------------------< BaseValue >
    /**
     * {@inheritDoc}
     */
    protected String getInternalString() throws ValueFormatException {
        if (date != null) {
            return ISO8601.format(date);
        } else {
            throw new ValueFormatException("empty value");
        }
    }

    //----------------------------------------------------------------< Value >
    /**
     * {@inheritDoc}
     */
    public Calendar getDate()
            throws ValueFormatException, IllegalStateException,
            RepositoryException {
        setValueConsumed();

        if (date != null) {
            return date;
        } else {
            throw new ValueFormatException("empty value");
        }
    }

    /**
     * {@inheritDoc}
     */
    public long getLong()
            throws ValueFormatException, IllegalStateException,
            RepositoryException {
        setValueConsumed();

        if (date != null) {
            return date.getTimeInMillis();
        } else {
            throw new ValueFormatException("empty value");
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean getBoolean()
            throws ValueFormatException, IllegalStateException,
            RepositoryException {
        setValueConsumed();

        if (date != null) {
            throw new ValueFormatException("cannot convert date to boolean");
        } else {
            throw new ValueFormatException("empty value");
        }
    }

    /**
     * {@inheritDoc}
     */
    public double getDouble()
            throws ValueFormatException, IllegalStateException,
            RepositoryException {
        setValueConsumed();

        if (date != null) {
            long ms = date.getTimeInMillis();
            if (ms <= Double.MAX_VALUE) {
                return ms;
            }
            throw new ValueFormatException("conversion from date to double failed: inconvertible types");
        } else {
            throw new ValueFormatException("empty value");
        }
    }
}
