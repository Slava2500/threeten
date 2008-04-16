/*
 * Copyright (c) 2008, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time.calendar.field;

import static org.testng.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.time.calendar.LocalDate;
import javax.time.calendar.MonthDay;
import javax.time.calendar.TimeFieldRule;
import javax.time.calendar.UnsupportedCalendarFieldException;
import javax.time.calendar.format.FlexiDateTime;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test DayOfMonth.RULE.
 *
 * @author Stephen Colebourne
 */
@Test
public class TestDayOfMonthRule {

    private static final int STANDARD_YEAR_LENGTH = 365;
    private static final int LEAP_YEAR_LENGTH = 366;
    private static final int MAX_LENGTH = 31;

    @BeforeMethod
    public void setUp() {
    }

    //-----------------------------------------------------------------------
    public void test_interfaces() {
        assertTrue(TimeFieldRule.class.isAssignableFrom(DayOfMonth.RULE.getClass()));
    }

    public void test_immutable() {
        Class<DayOfMonth> cls = DayOfMonth.class;
        assertTrue(Modifier.isPublic(cls.getModifiers()));
        assertTrue(Modifier.isFinal(cls.getModifiers()));
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                assertTrue(Modifier.isFinal(field.getModifiers()), "Field:" + field.getName());
            } else {
                assertTrue(Modifier.isPrivate(field.getModifiers()), "Field:" + field.getName());
                assertTrue(Modifier.isFinal(field.getModifiers()), "Field:" + field.getName());
            }
        }
    }

    //-----------------------------------------------------------------------
    public void test_singleton() {
        assertSame(DayOfMonth.RULE, DayOfMonth.RULE);
    }

    //-----------------------------------------------------------------------
    // getValue(FlexiDateTime)
    //-----------------------------------------------------------------------
    public void test_getValue_FlexiDateTime_date() {
        LocalDate date = LocalDate.date(2007, 6, 20);
        FlexiDateTime dt = date.toFlexiDateTime();
        
        assertEquals(DayOfMonth.RULE.getValue(dt), 20);
    }

    @Test(expectedExceptions=UnsupportedCalendarFieldException.class)
    public void test_getValue_FlexiDateTime_noDate() {
        FlexiDateTime dt = new FlexiDateTime(null, null, null, null);
        DayOfMonth.RULE.getValue(dt);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_getValue_FlexiDateTime_null() {
        FlexiDateTime dt = null;
        DayOfMonth.RULE.getValue(dt);
    }

    public void test_getValue_FlexiDateTime_monthDay() {
        MonthDay date = MonthDay.monthDay(6, 20);
        FlexiDateTime dt = date.toFlexiDateTime();
        
        assertEquals(DayOfMonth.RULE.getValue(dt), 20);
    }

    public void test_getValue_FlexiDateTime_day() {
        FlexiDateTime dt = new FlexiDateTime(DayOfMonth.RULE, 20);
        
        assertEquals(DayOfMonth.RULE.getValue(dt), 20);
    }

}
