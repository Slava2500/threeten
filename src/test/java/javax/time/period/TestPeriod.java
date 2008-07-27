/*
 * Copyright (c) 2007, Stephen Colebourne & Michael Nascimento Santos
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
package javax.time.period;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import javax.time.calendar.DateTimeFields;
import javax.time.period.field.Days;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test Period.
 *
 * @author Michael Nascimento Santos
 * @author Stephen Colebourne
 */
@Test
public class TestPeriod {

    //-----------------------------------------------------------------------
    // basics
    //-----------------------------------------------------------------------
    public void test_interfaces() {
        assertTrue(PeriodProvider.class.isAssignableFrom(Period.class));
        assertTrue(Serializable.class.isAssignableFrom(Period.class));
    }

    @DataProvider(name="serialization")
    Object[][] data_serialization() {
        return new Object[][] {
            {Period.ZERO},
            {Period.days(0)},
            {Period.days(1)},
            {Period.period(1, 2, 3, 4, 5, 6)},
        };
    }

    @Test(dataProvider="serialization")
    public void test_serialization(Period period) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(period);
        oos.close();
        
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                baos.toByteArray()));
        if (period.isZero()) {
            assertSame(ois.readObject(), period);
        } else {
            assertEquals(ois.readObject(), period);
        }
    }

    public void test_immutable() {
        Class<DateTimeFields> cls = DateTimeFields.class;
        assertTrue(Modifier.isPublic(cls.getModifiers()));
        assertTrue(Modifier.isFinal(cls.getModifiers()));
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) == false) {
                assertTrue(Modifier.isPrivate(field.getModifiers()));
                assertTrue(Modifier.isFinal(field.getModifiers()));
            }
        }
        Constructor<?>[] cons = cls.getDeclaredConstructors();
        for (Constructor<?> con : cons) {
            assertTrue(Modifier.isPrivate(con.getModifiers()));
        }
    }

    //-----------------------------------------------------------------------
    public void factory_zeroSingleton() {
        assertSame(Period.ZERO, Period.ZERO);
        assertSame(Period.years(0), Period.ZERO);
        assertSame(Period.months(0), Period.ZERO);
        assertSame(Period.days(0), Period.ZERO);
        assertSame(Period.hours(0), Period.ZERO);
        assertSame(Period.minutes(0), Period.ZERO);
        assertSame(Period.seconds(0), Period.ZERO);
        assertSame(Period.yearsMonths(0, 0), Period.ZERO);
        assertSame(Period.yearsMonthsDays(0, 0, 0), Period.ZERO);
        assertSame(Period.hoursMinutesSeconds(0, 0, 0), Period.ZERO);
        assertSame(Period.period(0, 0, 0, 0, 0, 0), Period.ZERO);
        assertSame(Period.period(PeriodFields.period(new HashMap<PeriodUnit, Integer>())), Period.ZERO);
    }

    //-----------------------------------------------------------------------
    public void factory_years() {
        assertPeriod(Period.years(1), 1, 0, 0, 0, 0, 0);
        assertPeriod(Period.years(0), 0, 0, 0, 0, 0, 0);
        assertPeriod(Period.years(-1), -1, 0, 0, 0, 0, 0);
        assertPeriod(Period.years(Integer.MAX_VALUE), Integer.MAX_VALUE, 0, 0, 0, 0, 0);
        assertPeriod(Period.years(Integer.MIN_VALUE), Integer.MIN_VALUE, 0, 0, 0, 0, 0);
    }

    public void factory_months() {
        assertPeriod(Period.months(1), 0, 1, 0, 0, 0, 0);
        assertPeriod(Period.months(0), 0, 0, 0, 0, 0, 0);
        assertPeriod(Period.months(-1), 0, -1, 0, 0, 0, 0);
        assertPeriod(Period.months(Integer.MAX_VALUE), 0, Integer.MAX_VALUE, 0, 0, 0, 0);
        assertPeriod(Period.months(Integer.MIN_VALUE), 0, Integer.MIN_VALUE, 0, 0, 0, 0);
    }

    public void factory_days() {
        assertPeriod(Period.days(1), 0, 0, 1, 0, 0, 0);
        assertPeriod(Period.days(0), 0, 0, 0, 0, 0, 0);
        assertPeriod(Period.days(-1), 0, 0, -1, 0, 0, 0);
        assertPeriod(Period.days(Integer.MAX_VALUE), 0, 0, Integer.MAX_VALUE, 0, 0, 0);
        assertPeriod(Period.days(Integer.MIN_VALUE), 0, 0, Integer.MIN_VALUE, 0, 0, 0);
    }

    public void factory_hours() {
        assertPeriod(Period.hours(1), 0, 0, 0, 1, 0, 0);
        assertPeriod(Period.hours(0), 0, 0, 0, 0, 0, 0);
        assertPeriod(Period.hours(-1), 0, 0, 0, -1, 0, 0);
        assertPeriod(Period.hours(Integer.MAX_VALUE), 0, 0, 0, Integer.MAX_VALUE, 0, 0);
        assertPeriod(Period.hours(Integer.MIN_VALUE), 0, 0, 0, Integer.MIN_VALUE, 0, 0);
    }

    public void factory_minutes() {
        assertPeriod(Period.minutes(1), 0, 0, 0, 0, 1, 0);
        assertPeriod(Period.minutes(0), 0, 0, 0, 0, 0, 0);
        assertPeriod(Period.minutes(-1), 0, 0, 0, 0, -1, 0);
        assertPeriod(Period.minutes(Integer.MAX_VALUE), 0, 0, 0, 0, Integer.MAX_VALUE, 0);
        assertPeriod(Period.minutes(Integer.MIN_VALUE), 0, 0, 0, 0, Integer.MIN_VALUE, 0);
    }

    public void factory_seconds() {
        assertPeriod(Period.seconds(1), 0, 0, 0, 0, 0, 1);
        assertPeriod(Period.seconds(0), 0, 0, 0, 0, 0, 0);
        assertPeriod(Period.seconds(-1), 0, 0, 0, 0, 0, -1);
        assertPeriod(Period.seconds(Integer.MAX_VALUE), 0, 0, 0, 0, 0, Integer.MAX_VALUE);
        assertPeriod(Period.seconds(Integer.MIN_VALUE), 0, 0, 0, 0, 0, Integer.MIN_VALUE);
    }

    //-----------------------------------------------------------------------
    public void factory_yearsMonths() {
        assertPeriod(Period.yearsMonths(1, 2), 1, 2, 0, 0, 0, 0);
        assertPeriod(Period.yearsMonths(0, 2), 0, 2, 0, 0, 0, 0);
        assertPeriod(Period.yearsMonths(1, 0), 1, 0, 0, 0, 0, 0);
        assertPeriod(Period.yearsMonths(0, 0), 0, 0, 0, 0, 0, 0);
        assertPeriod(Period.yearsMonths(-1, -2), -1, -2, 0, 0, 0, 0);
    }

    //-----------------------------------------------------------------------
    public void factory_yearsMonthsDays() {
        assertPeriod(Period.yearsMonthsDays(1, 2, 3), 1, 2, 3, 0, 0, 0);
        assertPeriod(Period.yearsMonthsDays(0, 2, 3), 0, 2, 3, 0, 0, 0);
        assertPeriod(Period.yearsMonthsDays(1, 0, 0), 1, 0, 0, 0, 0, 0);
        assertPeriod(Period.yearsMonthsDays(0, 0, 0), 0, 0, 0, 0, 0, 0);
        assertPeriod(Period.yearsMonthsDays(-1, -2, -3), -1, -2, -3, 0, 0, 0);
    }

    //-----------------------------------------------------------------------
    public void factory_hoursMinutesSeconds() {
        assertPeriod(Period.hoursMinutesSeconds(1, 2, 3), 0, 0, 0, 1, 2, 3);
        assertPeriod(Period.hoursMinutesSeconds(0, 2, 3), 0, 0, 0, 0, 2, 3);
        assertPeriod(Period.hoursMinutesSeconds(1, 0, 0), 0, 0, 0, 1, 0, 0);
        assertPeriod(Period.hoursMinutesSeconds(0, 0, 0), 0, 0, 0, 0, 0, 0);
        assertPeriod(Period.hoursMinutesSeconds(-1, -2, -3), 0, 0, 0, -1, -2, -3);
    }

    //-----------------------------------------------------------------------
    public void factory_period_ints() {
        assertPeriod(Period.period(1, 2, 3, 4, 5, 6), 1, 2, 3, 4, 5, 6);
        assertPeriod(Period.period(0, 2, 3, 4, 5, 6), 0, 2, 3, 4, 5, 6);
        assertPeriod(Period.period(1, 0, 0, 0, 0, 0), 1, 0, 0, 0, 0, 0);
        assertPeriod(Period.period(0, 0, 0, 0, 0, 0), 0, 0, 0, 0, 0, 0);
        assertPeriod(Period.period(-1, -2, -3, -4, -5, -6), -1, -2, -3, -4, -5, -6);
    }

    //-----------------------------------------------------------------------
    public void factory_period_provider() {
        PeriodProvider provider = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(Period.period(provider), 1, 2, 3, 4, 5, 6);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void factory_period_provider_null() {
        PeriodProvider provider = null;
        Period.period(provider);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void factory_period_badProvider() {
        PeriodProvider provider = new MockPeriodProviderReturnsNull();
        Period.period(provider);
    }

    //-----------------------------------------------------------------------
    // isZero()
    //-----------------------------------------------------------------------
    public void test_isZero() {
        assertEquals(Period.period(1, 2, 3, 4, 5, 6).isZero(), false);
        assertEquals(Period.period(1, 2, 3, 0, 0, 0).isZero(), false);
        assertEquals(Period.period(0, 0, 0, 4, 5, 6).isZero(), false);
        assertEquals(Period.period(1, 0, 0, 0, 0, 0).isZero(), false);
        assertEquals(Period.period(0, 2, 0, 0, 0, 0).isZero(), false);
        assertEquals(Period.period(0, 0, 3, 0, 0, 0).isZero(), false);
        assertEquals(Period.period(0, 0, 0, 4, 0, 0).isZero(), false);
        assertEquals(Period.period(0, 0, 0, 0, 5, 0).isZero(), false);
        assertEquals(Period.period(0, 0, 0, 0, 0, 6).isZero(), false);
        assertEquals(Period.period(0, 0, 0, 0, 0, 0).isZero(), true);
    }

    //-----------------------------------------------------------------------
    // withYears()
    //-----------------------------------------------------------------------
    public void test_withYears() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.withYears(10), 10, 2, 3, 4, 5, 6);
    }

    public void test_withYears_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.withYears(1), test);
    }

    public void test_withYears_toZero() {
        Period test = Period.years(1);
        assertSame(test.withYears(0), Period.ZERO);
    }

    //-----------------------------------------------------------------------
    // withMonths()
    //-----------------------------------------------------------------------
    public void test_withMonths() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.withMonths(10), 1, 10, 3, 4, 5, 6);
    }

    public void test_withMonths_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.withMonths(2), test);
    }

    public void test_withMonths_toZero() {
        Period test = Period.months(1);
        assertSame(test.withMonths(0), Period.ZERO);
    }

    //-----------------------------------------------------------------------
    // withDays()
    //-----------------------------------------------------------------------
    public void test_withDays() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.withDays(10), 1, 2, 10, 4, 5, 6);
    }

    public void test_withDays_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.withDays(3), test);
    }

    public void test_withDays_toZero() {
        Period test = Period.days(1);
        assertSame(test.withDays(0), Period.ZERO);
    }

    //-----------------------------------------------------------------------
    // withHours()
    //-----------------------------------------------------------------------
    public void test_withHours() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.withHours(10), 1, 2, 3, 10, 5, 6);
    }

    public void test_withHours_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.withHours(4), test);
    }

    public void test_withHours_toZero() {
        Period test = Period.hours(1);
        assertSame(test.withHours(0), Period.ZERO);
    }

    //-----------------------------------------------------------------------
    // withMinutes()
    //-----------------------------------------------------------------------
    public void test_withMinutes() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.withMinutes(10), 1, 2, 3, 4, 10, 6);
    }

    public void test_withMinutes_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.withMinutes(5), test);
    }

    public void test_withMinutes_toZero() {
        Period test = Period.minutes(1);
        assertSame(test.withMinutes(0), Period.ZERO);
    }

    //-----------------------------------------------------------------------
    // withSeconds()
    //-----------------------------------------------------------------------
    public void test_withSeconds() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.withSeconds(10), 1, 2, 3, 4, 5, 10);
    }

    public void test_withSeconds_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.withSeconds(6), test);
    }

    public void test_withSeconds_toZero() {
        Period test = Period.seconds(1);
        assertSame(test.withSeconds(0), Period.ZERO);
    }

    //-----------------------------------------------------------------------
    // plus(PeriodProvider)
    //-----------------------------------------------------------------------
    public void test_plus_provider() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        PeriodProvider provider = Period.period(3, 3, 3, 3, 3, 3);
        assertPeriod(test.plus(provider), 4, 5, 6, 7, 8, 9);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_plus_provider_null() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        PeriodProvider provider = null;
        test.plus(provider);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_plus_badProvider() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        PeriodProvider provider = new MockPeriodProviderReturnsNull();
        test.plus(provider);
    }

    //-----------------------------------------------------------------------
    // plusYears()
    //-----------------------------------------------------------------------
    public void test_plusYears() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.plusYears(10), 11, 2, 3, 4, 5, 6);
    }

    public void test_plusYears_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.plusYears(0), test);
    }

    public void test_plusYears_toZero() {
        Period test = Period.years(-1);
        assertSame(test.plusYears(1), Period.ZERO);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_plusYears_overflowTooBig() {
        Period test = Period.years(Integer.MAX_VALUE);
        test.plusYears(1);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_plusYears_overflowTooSmall() {
        Period test = Period.years(Integer.MIN_VALUE);
        test.plusYears(-1);
    }

    //-----------------------------------------------------------------------
    // plusMonths()
    //-----------------------------------------------------------------------
    public void test_plusMonths() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.plusMonths(10), 1, 12, 3, 4, 5, 6);
    }

    public void test_plusMonths_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.plusMonths(0), test);
    }

    public void test_plusMonths_toZero() {
        Period test = Period.months(-1);
        assertSame(test.plusMonths(1), Period.ZERO);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_plusMonths_overflowTooBig() {
        Period test = Period.months(Integer.MAX_VALUE);
        test.plusMonths(1);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_plusMonths_overflowTooSmall() {
        Period test = Period.months(Integer.MIN_VALUE);
        test.plusMonths(-1);
    }

    //-----------------------------------------------------------------------
    // plusDays()
    //-----------------------------------------------------------------------
    public void test_plusDays() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.plusDays(10), 1, 2, 13, 4, 5, 6);
    }

    public void test_plusDays_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.plusDays(0), test);
    }

    public void test_plusDays_toZero() {
        Period test = Period.days(-1);
        assertSame(test.plusDays(1), Period.ZERO);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_plusDays_overflowTooBig() {
        Period test = Period.days(Integer.MAX_VALUE);
        test.plusDays(1);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_plusDays_overflowTooSmall() {
        Period test = Period.days(Integer.MIN_VALUE);
        test.plusDays(-1);
    }

    //-----------------------------------------------------------------------
    // plusHours()
    //-----------------------------------------------------------------------
    public void test_plusHours() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.plusHours(10), 1, 2, 3, 14, 5, 6);
    }

    public void test_plusHours_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.plusHours(0), test);
    }

    public void test_plusHours_toZero() {
        Period test = Period.hours(-1);
        assertSame(test.plusHours(1), Period.ZERO);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_plusHours_overflowTooBig() {
        Period test = Period.hours(Integer.MAX_VALUE);
        test.plusHours(1);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_plusHours_overflowTooSmall() {
        Period test = Period.hours(Integer.MIN_VALUE);
        test.plusHours(-1);
    }

    //-----------------------------------------------------------------------
    // plusMinutes()
    //-----------------------------------------------------------------------
    public void test_plusMinutes() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.plusMinutes(10), 1, 2, 3, 4, 15, 6);
    }

    public void test_plusMinutes_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.plusMinutes(0), test);
    }

    public void test_plusMinutes_toZero() {
        Period test = Period.minutes(-1);
        assertSame(test.plusMinutes(1), Period.ZERO);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_plusMinutes_overflowTooBig() {
        Period test = Period.minutes(Integer.MAX_VALUE);
        test.plusMinutes(1);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_plusMinutes_overflowTooSmall() {
        Period test = Period.minutes(Integer.MIN_VALUE);
        test.plusMinutes(-1);
    }

    //-----------------------------------------------------------------------
    // plusSeconds()
    //-----------------------------------------------------------------------
    public void test_plusSeconds() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.plusSeconds(10), 1, 2, 3, 4, 5, 16);
    }

    public void test_plusSeconds_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.plusSeconds(0), test);
    }

    public void test_plusSeconds_toZero() {
        Period test = Period.seconds(-1);
        assertSame(test.plusSeconds(1), Period.ZERO);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_plusSeconds_overflowTooBig() {
        Period test = Period.seconds(Integer.MAX_VALUE);
        test.plusSeconds(1);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_plusSeconds_overflowTooSmall() {
        Period test = Period.seconds(Integer.MIN_VALUE);
        test.plusSeconds(-1);
    }

    //-----------------------------------------------------------------------
    // minus(PeriodProvider)
    //-----------------------------------------------------------------------
    public void test_minus_provider() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        PeriodProvider provider = Period.period(3, 3, 3, 3, 3, 3);
        assertPeriod(test.minus(provider), -2, -1, 0, 1, 2, 3);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_minus_provider_null() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        PeriodProvider provider = null;
        test.minus(provider);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_minus_badProvider() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        PeriodProvider provider = new MockPeriodProviderReturnsNull();
        test.minus(provider);
    }

    //-----------------------------------------------------------------------
    // minusYears()
    //-----------------------------------------------------------------------
    public void test_minusYears() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.minusYears(10), -9, 2, 3, 4, 5, 6);
    }

    public void test_minusYears_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.minusYears(0), test);
    }

    public void test_minusYears_toZero() {
        Period test = Period.years(1);
        assertSame(test.minusYears(1), Period.ZERO);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_minusYears_overflowTooBig() {
        Period test = Period.years(Integer.MAX_VALUE);
        test.minusYears(-1);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_minusYears_overflowTooSmall() {
        Period test = Period.years(Integer.MIN_VALUE);
        test.minusYears(1);
    }

    //-----------------------------------------------------------------------
    // minusMonths()
    //-----------------------------------------------------------------------
    public void test_minusMonths() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.minusMonths(10), 1, -8, 3, 4, 5, 6);
    }

    public void test_minusMonths_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.minusMonths(0), test);
    }

    public void test_minusMonths_toZero() {
        Period test = Period.months(1);
        assertSame(test.minusMonths(1), Period.ZERO);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_minusMonths_overflowTooBig() {
        Period test = Period.months(Integer.MAX_VALUE);
        test.minusMonths(-1);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_minusMonths_overflowTooSmall() {
        Period test = Period.months(Integer.MIN_VALUE);
        test.minusMonths(1);
    }

    //-----------------------------------------------------------------------
    // minusDays()
    //-----------------------------------------------------------------------
    public void test_minusDays() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.minusDays(10), 1, 2, -7, 4, 5, 6);
    }

    public void test_minusDays_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.minusDays(0), test);
    }

    public void test_minusDays_toZero() {
        Period test = Period.days(1);
        assertSame(test.minusDays(1), Period.ZERO);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_minusDays_overflowTooBig() {
        Period test = Period.days(Integer.MAX_VALUE);
        test.minusDays(-1);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_minusDays_overflowTooSmall() {
        Period test = Period.days(Integer.MIN_VALUE);
        test.minusDays(1);
    }

    //-----------------------------------------------------------------------
    // minusHours()
    //-----------------------------------------------------------------------
    public void test_minusHours() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.minusHours(10), 1, 2, 3, -6, 5, 6);
    }

    public void test_minusHours_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.minusHours(0), test);
    }

    public void test_minusHours_toZero() {
        Period test = Period.hours(1);
        assertSame(test.minusHours(1), Period.ZERO);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_minusHours_overflowTooBig() {
        Period test = Period.hours(Integer.MAX_VALUE);
        test.minusHours(-1);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_minusHours_overflowTooSmall() {
        Period test = Period.hours(Integer.MIN_VALUE);
        test.minusHours(1);
    }

    //-----------------------------------------------------------------------
    // minusMinutes()
    //-----------------------------------------------------------------------
    public void test_minusMinutes() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.minusMinutes(10), 1, 2, 3, 4, -5, 6);
    }

    public void test_minusMinutes_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.minusMinutes(0), test);
    }

    public void test_minusMinutes_toZero() {
        Period test = Period.minutes(1);
        assertSame(test.minusMinutes(1), Period.ZERO);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_minusMinutes_overflowTooBig() {
        Period test = Period.minutes(Integer.MAX_VALUE);
        test.minusMinutes(-1);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_minusMinutes_overflowTooSmall() {
        Period test = Period.minutes(Integer.MIN_VALUE);
        test.minusMinutes(1);
    }

    //-----------------------------------------------------------------------
    // minusSeconds()
    //-----------------------------------------------------------------------
    public void test_minusSeconds() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.minusSeconds(10), 1, 2, 3, 4, 5, -4);
    }

    public void test_minusSeconds_noChange() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.minusSeconds(0), test);
    }

    public void test_minusSeconds_toZero() {
        Period test = Period.seconds(1);
        assertSame(test.minusSeconds(1), Period.ZERO);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_minusSeconds_overflowTooBig() {
        Period test = Period.seconds(Integer.MAX_VALUE);
        test.minusSeconds(-1);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_minusSeconds_overflowTooSmall() {
        Period test = Period.seconds(Integer.MIN_VALUE);
        test.minusSeconds(1);
    }

    //-----------------------------------------------------------------------
    // multipliedBy()
    //-----------------------------------------------------------------------
    public void test_multipliedBy() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(Period.ZERO.multipliedBy(2), Period.ZERO);
        assertSame(test.multipliedBy(0), Period.ZERO);
        assertSame(test.multipliedBy(1), test);
        assertPeriod(test.multipliedBy(2), 2, 4, 6, 8, 10, 12);
        assertPeriod(test.multipliedBy(-3), -3, -6, -9, -12, -15, -18);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_multipliedBy_overflowTooBig() {
        Period test = Period.years(Integer.MAX_VALUE / 2 + 1);
        test.multipliedBy(2);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_multipliedBy_overflowTooSmall() {
        Period test = Period.years(Integer.MIN_VALUE / 2 - 1);
        test.multipliedBy(2);
    }

    //-----------------------------------------------------------------------
    // dividedBy()
    //-----------------------------------------------------------------------
    public void test_dividedBy() {
        Period test = Period.period(12, 12, 12, 12, 12, 12);
        assertSame(Period.ZERO.dividedBy(2), Period.ZERO);
        assertSame(test.dividedBy(1), test);
        assertPeriod(test.dividedBy(2), 6, 6, 6, 6, 6, 6);
        assertPeriod(test.dividedBy(-3), -4, -4, -4, -4, -4, -4);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_dividedBy_divideByZero() {
        Period test = Period.period(12, 12, 12, 12, 12, 12);
        test.dividedBy(0);
    }

    //-----------------------------------------------------------------------
    // negated()
    //-----------------------------------------------------------------------
    public void test_negated() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertPeriod(test.negated(), -1, -2, -3, -4, -5, -6);
    }

    public void test_negated_zero() {
        assertSame(Period.ZERO.negated(), Period.ZERO);
    }

    public void test_negated_max() {
        assertPeriod(Period.years(Integer.MAX_VALUE).negated(), -Integer.MAX_VALUE, 0, 0, 0, 0, 0);
    }

    @Test(expectedExceptions=ArithmeticException.class)
    public void test_negated_overflow() {
        Days.days(Integer.MIN_VALUE).negated();
    }

    //-----------------------------------------------------------------------
    // normalized()
    //-----------------------------------------------------------------------
    public void test_normalized() {
        assertPeriod(Period.period(1, 2, 3, 4, 5, 6).normalized(), 1, 2, 3, 4, 5, 6);
    }

    public void test_normalized_months() {
        assertPeriod(Period.period(1, 11, 3, 4, 5, 6).normalized(), 1, 11, 3, 4, 5, 6);
        assertPeriod(Period.period(1, 12, 3, 4, 5, 6).normalized(), 2, 0, 3, 4, 5, 6);
        assertPeriod(Period.period(1, 23, 3, 4, 5, 6).normalized(), 2, 11, 3, 4, 5, 6);
        assertPeriod(Period.period(1, 24, 3, 4, 5, 6).normalized(), 3, 0, 3, 4, 5, 6);
    }

    public void test_normalized_seconds() {
        assertPeriod(Period.period(1, 2, 3, 4, 5, 59).normalized(), 1, 2, 3, 4, 5, 59);
        assertPeriod(Period.period(1, 2, 3, 4, 5, 60).normalized(), 1, 2, 3, 4, 6, 0);
        assertPeriod(Period.period(1, 2, 3, 4, 5, 119).normalized(), 1, 2, 3, 4, 6, 59);
        assertPeriod(Period.period(1, 2, 3, 4, 5, 120).normalized(), 1, 2, 3, 4, 7, 0);
        
        assertPeriod(Period.period(1, 2, 3, 4, 59, 59).normalized(), 1, 2, 3, 4, 59, 59);
        assertPeriod(Period.period(1, 2, 3, 4, 59, 60).normalized(), 1, 2, 3, 5, 0, 0);
        
        assertPeriod(Period.period(1, 2, 3, 23, 59, 59).normalized(), 1, 2, 3, 23, 59, 59);
        assertPeriod(Period.period(1, 2, 3, 23, 59, 60).normalized(), 1, 2, 4, 0, 0, 0);
    }

    public void test_normalized_minutes() {
        assertPeriod(Period.period(1, 2, 3, 4, 59, 6).normalized(), 1, 2, 3, 4, 59, 6);
        assertPeriod(Period.period(1, 2, 3, 4, 60, 6).normalized(), 1, 2, 3, 5, 0, 6);
        assertPeriod(Period.period(1, 2, 3, 4, 119, 6).normalized(), 1, 2, 3, 5, 59, 6);
        assertPeriod(Period.period(1, 2, 3, 4, 120, 6).normalized(), 1, 2, 3, 6, 0, 6);
        
        assertPeriod(Period.period(1, 2, 3, 23, 59, 6).normalized(), 1, 2, 3, 23, 59, 6);
        assertPeriod(Period.period(1, 2, 3, 23, 60, 6).normalized(), 1, 2, 4, 0, 0, 6);
    }

    public void test_normalized_hours() {
        assertPeriod(Period.period(1, 2, 3, 23, 5, 6).normalized(), 1, 2, 3, 23, 5, 6);
        assertPeriod(Period.period(1, 2, 3, 24, 5, 6).normalized(), 1, 2, 4, 0, 5, 6);
        assertPeriod(Period.period(1, 2, 3, 47, 5, 6).normalized(), 1, 2, 4, 23, 5, 6);
        assertPeriod(Period.period(1, 2, 3, 48, 5, 6).normalized(), 1, 2, 5, 0, 5, 6);
    }

    public void test_normalized_zero() {
        assertSame(Period.ZERO.normalized(), Period.ZERO);
    }

    //-----------------------------------------------------------------------
    // toPeriod()
    //-----------------------------------------------------------------------
    public void test_toPeriod() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertSame(test.toPeriod(), test);
    }

    public void test_toPeriod_zero() {
        assertSame(Period.ZERO.toPeriod(), Period.ZERO);
    }

    //-----------------------------------------------------------------------
    // toPeriodFields()
    //-----------------------------------------------------------------------
    public void test_toPeriodFields() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        PeriodFields fields = test.toPeriodFields();
        assertEquals(fields.size(), 6);
        assertEquals(fields.getAmount(Periods.YEARS), 1);
        assertEquals(fields.getAmount(Periods.MONTHS), 2);
        assertEquals(fields.getAmount(Periods.DAYS), 3);
        assertEquals(fields.getAmount(Periods.HOURS), 4);
        assertEquals(fields.getAmount(Periods.MINUTES), 5);
        assertEquals(fields.getAmount(Periods.SECONDS), 6);
    }

    public void test_toPeriodFields_zeroRemoved() {
        Period test = Period.period(1, 0, 3, 0, 5, 0);
        PeriodFields fields = test.toPeriodFields();
        assertEquals(fields.size(), 3);
        assertEquals(fields.getAmount(Periods.YEARS), 1);
        assertEquals(fields.getAmount(Periods.DAYS), 3);
        assertEquals(fields.getAmount(Periods.MINUTES), 5);
        assertEquals(fields.contains(Periods.MONTHS), false);
        assertEquals(fields.contains(Periods.HOURS), false);
        assertEquals(fields.contains(Periods.SECONDS), false);
    }

    public void test_toPeriodFields_zero() {
        assertSame(Period.ZERO.toPeriodFields(), PeriodFields.ZERO);
    }

    //-----------------------------------------------------------------------
    // equals() / hashCode()
    //-----------------------------------------------------------------------
    public void test_equals() {
        assertEquals(Period.period(1, 0, 0, 0, 0, 0).equals(Period.years(1)), true);
        assertEquals(Period.period(0, 1, 0, 0, 0, 0).equals(Period.months(1)), true);
        assertEquals(Period.period(0, 0, 1, 0, 0, 0).equals(Period.days(1)), true);
        assertEquals(Period.period(0, 0, 0, 1, 0, 0).equals(Period.hours(1)), true);
        assertEquals(Period.period(0, 0, 0, 0, 1, 0).equals(Period.minutes(1)), true);
        assertEquals(Period.period(0, 0, 0, 0, 0, 1).equals(Period.seconds(1)), true);
        assertEquals(Period.period(1, 2, 0, 0, 0, 0).equals(Period.yearsMonths(1, 2)), true);
        assertEquals(Period.period(1, 2, 3, 0, 0, 0).equals(Period.yearsMonthsDays(1, 2, 3)), true);
        assertEquals(Period.period(0, 0, 0, 1, 2, 3).equals(Period.hoursMinutesSeconds(1, 2, 3)), true);
        assertEquals(Period.period(1, 2, 3, 4, 5, 6).equals(Period.period(1, 2, 3, 4, 5, 6)), true);
        
        assertEquals(Period.years(1).equals(Period.years(1)), true);
        assertEquals(Period.years(1).equals(Period.years(2)), false);
        
        assertEquals(Period.months(1).equals(Period.months(1)), true);
        assertEquals(Period.months(1).equals(Period.months(2)), false);
        
        assertEquals(Period.days(1).equals(Period.days(1)), true);
        assertEquals(Period.days(1).equals(Period.days(2)), false);
        
        assertEquals(Period.hours(1).equals(Period.hours(1)), true);
        assertEquals(Period.hours(1).equals(Period.hours(2)), false);
        
        assertEquals(Period.minutes(1).equals(Period.minutes(1)), true);
        assertEquals(Period.minutes(1).equals(Period.minutes(2)), false);
        
        assertEquals(Period.seconds(1).equals(Period.seconds(1)), true);
        assertEquals(Period.seconds(1).equals(Period.seconds(2)), false);
        
        assertEquals(Period.yearsMonths(1, 2).equals(Period.yearsMonths(1, 2)), true);
        assertEquals(Period.yearsMonths(1, 2).equals(Period.yearsMonths(3, 2)), false);
        assertEquals(Period.yearsMonths(1, 2).equals(Period.yearsMonths(1, 3)), false);
        assertEquals(Period.yearsMonths(1, 2).equals(Period.yearsMonths(3, 3)), false);
        
        assertEquals(Period.yearsMonthsDays(1, 2, 3).equals(Period.yearsMonthsDays(1, 2, 3)), true);
        assertEquals(Period.yearsMonthsDays(1, 2, 3).equals(Period.yearsMonthsDays(0, 2, 3)), false);
        assertEquals(Period.yearsMonthsDays(1, 2, 3).equals(Period.yearsMonthsDays(1, 0, 3)), false);
        assertEquals(Period.yearsMonthsDays(1, 2, 3).equals(Period.yearsMonthsDays(1, 2, 0)), false);
        
        assertEquals(Period.hoursMinutesSeconds(1, 2, 3).equals(Period.hoursMinutesSeconds(1, 2, 3)), true);
        assertEquals(Period.hoursMinutesSeconds(1, 2, 3).equals(Period.hoursMinutesSeconds(0, 2, 3)), false);
        assertEquals(Period.hoursMinutesSeconds(1, 2, 3).equals(Period.hoursMinutesSeconds(1, 0, 3)), false);
        assertEquals(Period.hoursMinutesSeconds(1, 2, 3).equals(Period.hoursMinutesSeconds(1, 2, 0)), false);
    }

    public void test_equals_self() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertEquals(test.equals(test), true);
    }

    public void test_equals_null() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertEquals(test.equals(null), false);
    }

    public void test_equals_otherClass() {
        Period test = Period.period(1, 2, 3, 4, 5, 6);
        assertEquals(test.equals(""), false);
    }

    //-----------------------------------------------------------------------
    public void test_hashCode() {
        Period test5 = Period.days(5);
        Period test6 = Period.days(6);
        assertEquals(test5.hashCode() == test5.hashCode(), true);
        assertEquals(test5.hashCode() == test6.hashCode(), false);
    }

    // test too slow and runs out of memory
//    public void test_hashCode_unique() {
//        // spec requires unique hash codes
//        // years 0-31, months 0-11, days 0-31, hours 0-23, minutes 0-59, seconds 0-59
//        boolean[] pos = new boolean[Integer.MAX_VALUE / 128];  // added 128 to avoid out of memory
//        boolean[] neg = new boolean[Integer.MAX_VALUE / 128];
//        for (int y = 0; y <= 31; y++) {
//            for (int mo = 0; mo <= 11; mo++) {
//                System.out.print(".");
//                for (int d = 0; d <= 31; d++) {
//                    for (int h = 0; h <= 23; h++) {
//                        for (int mn = 0; mn <= 59; mn++) {
//                            for (int s = 0; s <= 50; s++) {
//                                Period test = Period.period(y, mo, d, h, mn, s);
//                                int hashCode = test.hashCode();
//                                if (hashCode >= 0) {
//                                    if (hashCode < pos.length) {
//                                        assertEquals(pos[hashCode], false);
//                                        pos[hashCode] = true;
//                                    }
//                                } else {
//                                    hashCode = -(hashCode + 1);
//                                    if (hashCode < neg.length) {
//                                        assertEquals(neg[hashCode], false);
//                                        neg[hashCode] = true;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    @DataProvider(name="toString")
    Object[][] data_toString() {
        return new Object[][] {
            {Period.ZERO, "PT0S"},
            {Period.days(0), "PT0S"},
            {Period.years(1), "P1Y"},
            {Period.months(1), "P1M"},
            {Period.days(1), "P1D"},
            {Period.hours(1), "PT1H"},
            {Period.minutes(1), "PT1M"},
            {Period.seconds(1), "PT1S"},
            {Period.period(1, 2, 3, 4, 5, 6), "P1Y2M3DT4H5M6S"},
        };
    }

    @Test(dataProvider="toString")
    public void test_toString(Period test, String expected) {
        assertEquals(test.toString(), expected);
        assertSame(test.toString(), test.toString());  // repeat to check caching
    }

    //-----------------------------------------------------------------------
    private void assertPeriod(Period test, int y, int mo, int d, int h, int mn, int s) {
        assertEquals(test.getYears(), y);
        assertEquals(test.getMonths(), mo);
        assertEquals(test.getDays(), d);
        assertEquals(test.getHours(), h);
        assertEquals(test.getMinutes(), mn);
        assertEquals(test.getSeconds(), s);
    }

}
