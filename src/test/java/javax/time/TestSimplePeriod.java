/*
 * Copyright (c) 2007-2012, Stephen Colebourne & Michael Nascimento Santos
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
package javax.time;

import static javax.time.calendrical.LocalPeriodUnit.MILLIS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.time.calendrical.LocalPeriodUnit;
import javax.time.calendrical.PeriodUnit;

import org.testng.annotations.Test;

/**
 * Test.
 */
@Test
public class TestSimplePeriod {

    private static final PeriodUnit FOREVER = LocalPeriodUnit.FOREVER;
    private static final PeriodUnit MONTHS = LocalPeriodUnit.MONTHS;
    private static final PeriodUnit DAYS = LocalPeriodUnit.DAYS;
    private static final PeriodUnit HOURS = LocalPeriodUnit.HOURS;
    private static final PeriodUnit MINUTES = LocalPeriodUnit.MINUTES;
    private static final PeriodUnit SECONDS = LocalPeriodUnit.SECONDS;

    //-----------------------------------------------------------------------
    @Test(groups={"implementation"})
    public void test_interfaces() {
        assertTrue(Comparable.class.isAssignableFrom(SimplePeriod.class));
        assertTrue(Serializable.class.isAssignableFrom(SimplePeriod.class));
    }

    //-----------------------------------------------------------------------
    // constants
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_constant_zero_days() {
        assertEquals(SimplePeriod.ZERO_DAYS.getAmount(), 0);
        assertEquals(SimplePeriod.ZERO_DAYS.getUnit(), DAYS);
    }

    @Test(groups={"tck"})
    public void test_constant_zero_seconds() {
        assertEquals(SimplePeriod.ZERO_SECONDS.getAmount(), 0);
        assertEquals(SimplePeriod.ZERO_SECONDS.getUnit(), SECONDS);
    }

    //-----------------------------------------------------------------------
    // factories
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_factory_of() {
        assertEquals(SimplePeriod.of(1, DAYS).getAmount(), 1);
        assertEquals(SimplePeriod.of(2, DAYS).getAmount(), 2);
        assertEquals(SimplePeriod.of(Long.MAX_VALUE, DAYS).getAmount(), Long.MAX_VALUE);
        assertEquals(SimplePeriod.of(-1, DAYS).getAmount(), -1);
        assertEquals(SimplePeriod.of(-2, DAYS).getAmount(), -2);
        assertEquals(SimplePeriod.of(Long.MIN_VALUE, DAYS).getAmount(), Long.MIN_VALUE);
    }

    @Test(expectedExceptions=DateTimeException.class, groups={"tck"})
    public void test_factory_of_Forever() {
        SimplePeriod.of(1, FOREVER);
    }

    @Test(expectedExceptions=NullPointerException.class, groups={"tck"})
    public void test_factory_of_null() {
        SimplePeriod.of(1, null);
    }

    //-----------------------------------------------------------------------
    // serialization
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_serialization() throws Exception {
        SimplePeriod orginal = SimplePeriod.of(3, DAYS);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(orginal);
        out.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bais);
        SimplePeriod ser = (SimplePeriod) in.readObject();
        assertEquals(SimplePeriod.of(3, DAYS), ser);
    }

    //-----------------------------------------------------------------------
    // getAmount()
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_getAmount() {
        assertEquals(SimplePeriod.of(0, DAYS).getAmount(), 0L);
        assertEquals(SimplePeriod.of(1, DAYS).getAmount(), 1L);
        assertEquals(SimplePeriod.of(-1, DAYS).getAmount(), -1L);
        assertEquals(SimplePeriod.of(Long.MAX_VALUE, DAYS).getAmount(), Long.MAX_VALUE);
        assertEquals(SimplePeriod.of(Long.MIN_VALUE, DAYS).getAmount(), Long.MIN_VALUE);
    }

    //-----------------------------------------------------------------------
    // getAmountInt()
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_getAmountInt() {
        assertEquals(SimplePeriod.of(0, DAYS).getAmountInt(), 0);
        assertEquals(SimplePeriod.of(1, DAYS).getAmountInt(), 1);
        assertEquals(SimplePeriod.of(-1, DAYS).getAmountInt(), -1);
        assertEquals(SimplePeriod.of(Integer.MAX_VALUE, DAYS).getAmountInt(), Integer.MAX_VALUE);
        assertEquals(SimplePeriod.of(Integer.MIN_VALUE, DAYS).getAmountInt(), Integer.MIN_VALUE);
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_getAmountInt_tooBig() {
        SimplePeriod.of(Integer.MAX_VALUE + 1L, DAYS).getAmountInt();
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_getAmountInt_tooSmall() {
        SimplePeriod.of(Integer.MIN_VALUE - 1L, DAYS).getAmountInt();
    }

    //-----------------------------------------------------------------------
    // getUnit()
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_getUnit() {
        assertEquals(SimplePeriod.of(0, DAYS).getUnit(), DAYS);
        assertEquals(SimplePeriod.of(1, DAYS).getUnit(), DAYS);
        assertEquals(SimplePeriod.of(-1, DAYS).getUnit(), DAYS);
    }

    //-----------------------------------------------------------------------
    // withAmount()
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_withAmount() {
        assertEquals(SimplePeriod.of(0, DAYS).withAmount(23), SimplePeriod.of(23, DAYS));
        assertEquals(SimplePeriod.of(1, DAYS).withAmount(23), SimplePeriod.of(23, DAYS));
        assertEquals(SimplePeriod.of(-1, DAYS).withAmount(23), SimplePeriod.of(23, DAYS));
    }

    @Test(groups={"implementation"})
    public void test_withAmount_same() {
        SimplePeriod base = SimplePeriod.of(1, DAYS);
        assertSame(base.withAmount(1), base);
    }
    
    @Test(groups={"tck"})
    public void test_withAmount_equal() {
        SimplePeriod base = SimplePeriod.of(1, DAYS);
        assertEquals(base.withAmount(1), base);
    }

    //-----------------------------------------------------------------------
    // withUnit()
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_withUnit() {
        assertEquals(SimplePeriod.of(0, DAYS).withUnit(MONTHS), SimplePeriod.of(0, MONTHS));
        assertEquals(SimplePeriod.of(1, DAYS).withUnit(MONTHS), SimplePeriod.of(1, MONTHS));
        assertEquals(SimplePeriod.of(-1, DAYS).withUnit(MONTHS), SimplePeriod.of(-1, MONTHS));
    }

    @Test(groups={"implementation"})
    public void test_withUnit_same() {
        SimplePeriod base = SimplePeriod.of(1, DAYS);
        assertSame(base.withUnit(DAYS), base);
    }
    
    @Test(groups={"tck"})
    public void test_withUnit_equal() {
        SimplePeriod base = SimplePeriod.of(1, DAYS);
        assertEquals(base.withUnit(DAYS), base);
    }

    @Test(expectedExceptions=DateTimeException.class, groups={"tck"})
    public void test_withUnit_forever() {
        SimplePeriod.of(1, DAYS).withUnit(FOREVER);
    }

    @Test(expectedExceptions=NullPointerException.class, groups={"tck"})
    public void test_withUnit_null() {
        SimplePeriod.of(1, DAYS).withUnit(null);
    }

    //-----------------------------------------------------------------------
    // plus(Period)
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_plus_Period() {
        SimplePeriod test5 = SimplePeriod.of(5, DAYS);
        assertEquals(test5.plus(SimplePeriod.of(0, DAYS)), SimplePeriod.of(5, DAYS));
        assertEquals(test5.plus(SimplePeriod.of(2, DAYS)), SimplePeriod.of(7, DAYS));
        assertEquals(test5.plus(SimplePeriod.of(-1, DAYS)), SimplePeriod.of(4, DAYS));
        assertEquals(SimplePeriod.of(Long.MAX_VALUE - 1, DAYS).plus(SimplePeriod.of(1, DAYS)), SimplePeriod.of(Long.MAX_VALUE, DAYS));
        assertEquals(SimplePeriod.of(Long.MIN_VALUE + 1, DAYS).plus(SimplePeriod.of(-1, DAYS)), SimplePeriod.of(Long.MIN_VALUE, DAYS));
    }

    @Test(expectedExceptions=DateTimeException.class, groups={"tck"})
    public void test_plus_Period_wrongRule() {
        SimplePeriod.of(1, DAYS).plus(SimplePeriod.of(-2, MONTHS));
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_plus_Period_overflowTooBig() {
        SimplePeriod.of(Long.MAX_VALUE - 1, DAYS).plus(SimplePeriod.of(2, DAYS));
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_plus_Period_overflowTooSmall() {
        SimplePeriod.of(Long.MIN_VALUE + 1, DAYS).plus(SimplePeriod.of(-2, DAYS));
    }

    @Test(expectedExceptions=NullPointerException.class, groups={"tck"})
    public void test_plus_Period_null() {
        SimplePeriod.of(1, DAYS).plus(null);
    }

    //-----------------------------------------------------------------------
    // plus(long)
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_plus() {
        SimplePeriod test5 = SimplePeriod.of(5, DAYS);
        assertEquals(test5.plus(0), SimplePeriod.of(5, DAYS));
        assertEquals(test5.plus(2), SimplePeriod.of(7, DAYS));
        assertEquals(test5.plus(-1), SimplePeriod.of(4, DAYS));
        assertEquals(SimplePeriod.of(Long.MAX_VALUE - 1, DAYS).plus(1), SimplePeriod.of(Long.MAX_VALUE, DAYS));
        assertEquals(SimplePeriod.of(Long.MIN_VALUE + 1, DAYS).plus(-1), SimplePeriod.of(Long.MIN_VALUE, DAYS));
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_plus_overflowTooBig() {
        SimplePeriod.of(Long.MAX_VALUE - 1, DAYS).plus(2);
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_plus_overflowTooSmall() {
        SimplePeriod.of(Long.MIN_VALUE + 1, DAYS).plus(-2);
    }

    //-----------------------------------------------------------------------
    // minus(Period)
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_minus_Period() {
        SimplePeriod test5 = SimplePeriod.of(5, DAYS);
        assertEquals(test5.minus(SimplePeriod.of(0, DAYS)), SimplePeriod.of(5, DAYS));
        assertEquals(test5.minus(SimplePeriod.of(2, DAYS)), SimplePeriod.of(3, DAYS));
        assertEquals(test5.minus(SimplePeriod.of(-1, DAYS)), SimplePeriod.of(6, DAYS));
        assertEquals(SimplePeriod.of(Long.MIN_VALUE + 1, DAYS).minus(SimplePeriod.of(1, DAYS)), SimplePeriod.of(Long.MIN_VALUE, DAYS));
        assertEquals(SimplePeriod.of(Long.MAX_VALUE - 1, DAYS).minus(SimplePeriod.of(-1, DAYS)), SimplePeriod.of(Long.MAX_VALUE, DAYS));
    }

    @Test(expectedExceptions=DateTimeException.class, groups={"tck"})
    public void test_minus_Period_wrongRule() {
        SimplePeriod.of(1, DAYS).minus(SimplePeriod.of(-2, MONTHS));
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_minus_Period_overflowTooBig() {
        SimplePeriod.of(Long.MIN_VALUE + 1, DAYS).minus(SimplePeriod.of(2, DAYS));
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_minus_Period_overflowTooSmall() {
        SimplePeriod.of(Long.MAX_VALUE - 1, DAYS).minus(SimplePeriod.of(-2, DAYS));
    }

    @Test(expectedExceptions=NullPointerException.class, groups={"tck"})
    public void test_minus_Period_null() {
        SimplePeriod.of(1, DAYS).minus(null);
    }

    //-----------------------------------------------------------------------
    // minus(long)
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_minus() {
        SimplePeriod test5 = SimplePeriod.of(5, DAYS);
        assertEquals(test5.minus(0), SimplePeriod.of(5, DAYS));
        assertEquals(test5.minus(2), SimplePeriod.of(3, DAYS));
        assertEquals(test5.minus(-1), SimplePeriod.of(6, DAYS));
        assertEquals(SimplePeriod.of(Long.MIN_VALUE + 1, DAYS).minus(1), SimplePeriod.of(Long.MIN_VALUE, DAYS));
        assertEquals(SimplePeriod.of(Long.MAX_VALUE - 1, DAYS).minus(-1), SimplePeriod.of(Long.MAX_VALUE, DAYS));
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_minus_overflowTooBig() {
        SimplePeriod.of(Long.MIN_VALUE + 1, DAYS).minus(2);
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_minus_overflowTooSmall() {
        SimplePeriod.of(Long.MAX_VALUE - 1, DAYS).minus(-2);
    }

    //-----------------------------------------------------------------------
    // multipliedBy(long)
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_multipliedBy() {
        SimplePeriod test5 = SimplePeriod.of(5, DAYS);
        assertEquals(test5.multipliedBy(0), SimplePeriod.of(0, DAYS));
        assertEquals(test5.multipliedBy(1), SimplePeriod.of(5, DAYS));
        assertEquals(test5.multipliedBy(2), SimplePeriod.of(10, DAYS));
        assertEquals(test5.multipliedBy(3), SimplePeriod.of(15, DAYS));
        assertEquals(test5.multipliedBy(-3), SimplePeriod.of(-15, DAYS));
    }

    @Test(groups={"implementation"})
    public void test_multipliedBy_same() {
        SimplePeriod base = SimplePeriod.of(12, DAYS);
        assertSame(base.multipliedBy(1), base);
    }
    
    @Test(groups={"tck"})
    public void test_multipliedBy_equal() {
        SimplePeriod base = SimplePeriod.of(12, DAYS);
        assertEquals(base.multipliedBy(1), base);
    }

    @Test(expectedExceptions = {ArithmeticException.class}, groups={"tck"})
    public void test_multipliedBy_overflowTooBig() {
        SimplePeriod.of(Long.MAX_VALUE / 2 + 1, DAYS).multipliedBy(2);
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_multipliedBy_overflowTooSmall() {
        SimplePeriod.of(Long.MIN_VALUE / 2 - 1, DAYS).multipliedBy(2);
    }

    //-----------------------------------------------------------------------
    // dividedBy(long)
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_dividedBy() {
        SimplePeriod test12 = SimplePeriod.of(12, DAYS);
        assertEquals(test12.dividedBy(1), SimplePeriod.of(12, DAYS));
        assertEquals(test12.dividedBy(2), SimplePeriod.of(6, DAYS));
        assertEquals(test12.dividedBy(3), SimplePeriod.of(4, DAYS));
        assertEquals(test12.dividedBy(4), SimplePeriod.of(3, DAYS));
        assertEquals(test12.dividedBy(5), SimplePeriod.of(2, DAYS));
        assertEquals(test12.dividedBy(6), SimplePeriod.of(2, DAYS));
        assertEquals(test12.dividedBy(-3), SimplePeriod.of(-4, DAYS));
    }

    @Test(groups={"implementation"})
    public void test_dividedBy_same() {
        SimplePeriod base = SimplePeriod.of(12, DAYS);
        assertSame(base.dividedBy(1), base);
    }
    
    @Test(groups={"tck"})
    public void test_dividedBy_equal() {
        SimplePeriod base = SimplePeriod.of(12, DAYS);
        assertEquals(base.dividedBy(1), base);
    }

    @Test(groups={"tck"})
    public void test_dividedBy_negate() {
        SimplePeriod test12 = SimplePeriod.of(12, DAYS);
        assertEquals(SimplePeriod.of(-4, DAYS), test12.dividedBy(-3));
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_dividedBy_divideByZero() {
        SimplePeriod.of(1, DAYS).dividedBy(0);
    }

    //-----------------------------------------------------------------------
    // remainder(long)
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_remainder() {
        SimplePeriod test12 = SimplePeriod.of(13, DAYS);
        assertEquals(test12.remainder(1), SimplePeriod.of(0, DAYS));
        assertEquals(test12.remainder(2), SimplePeriod.of(1, DAYS));
        assertEquals(test12.remainder(3), SimplePeriod.of(1, DAYS));
        assertEquals(test12.remainder(4), SimplePeriod.of(1, DAYS));
        assertEquals(test12.remainder(5), SimplePeriod.of(3, DAYS));
        assertEquals(test12.remainder(6), SimplePeriod.of(1, DAYS));
        assertEquals(test12.remainder(-3), SimplePeriod.of(1, DAYS));
    }

    @Test(groups={"tck"})
    public void test_remainder_negate() {
        SimplePeriod test12 = SimplePeriod.of(-14, DAYS);
        assertEquals(test12.remainder(-5), SimplePeriod.of(-4, DAYS));
    }

    @Test(groups={"implementation"})
    public void test_remainder_same() {
        SimplePeriod base = SimplePeriod.of(12, DAYS);
        assertSame(base.remainder(15), base);
    }
    
    @Test(groups={"tck"})
    public void test_remainder_equal() {
        SimplePeriod base = SimplePeriod.of(12, DAYS);
        assertEquals(base.remainder(15), base);
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_remainder_divideByZero() {
        SimplePeriod.of(1, DAYS).remainder(0);
    }

    //-----------------------------------------------------------------------
    // negated()
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_negated() {
        assertEquals(SimplePeriod.of(0, DAYS).negated(), SimplePeriod.of(0, DAYS));
        assertEquals(SimplePeriod.of(12, DAYS).negated(), SimplePeriod.of(-12, DAYS));
        assertEquals(SimplePeriod.of(-12, DAYS).negated(), SimplePeriod.of(12, DAYS));
        assertEquals(SimplePeriod.of(Long.MAX_VALUE, DAYS).negated(), SimplePeriod.of(-Long.MAX_VALUE, DAYS));
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_negated_overflow() {
        SimplePeriod.of(Long.MIN_VALUE, DAYS).negated();
    }

    //-----------------------------------------------------------------------
    // abs()
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_abs() {
        assertEquals(SimplePeriod.of(0, DAYS).abs(), SimplePeriod.of(0, DAYS));
        assertEquals(SimplePeriod.of(12, DAYS).abs(), SimplePeriod.of(12, DAYS));
        assertEquals(SimplePeriod.of(-12, DAYS).abs(), SimplePeriod.of(12, DAYS));
        assertEquals(SimplePeriod.of(Long.MAX_VALUE, DAYS).abs(), SimplePeriod.of(Long.MAX_VALUE, DAYS));
    }

    @Test(groups={"implementation"})
    public void test_abs_same() {
        SimplePeriod base = SimplePeriod.of(12, DAYS);
        assertSame(base.abs(), base);
    }
    
    @Test(groups={"tck"})
    public void test_abs_equal() {
        SimplePeriod base = SimplePeriod.of(12, DAYS);
        assertEquals(base.abs(), base);
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_abs_overflow() {
        SimplePeriod.of(Long.MIN_VALUE, DAYS).abs();
    }

    //-----------------------------------------------------------------------
    // toDuration()
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_toDuration_hours() {
        Duration test = SimplePeriod.of(5, HOURS).toDuration();
        Duration fiveHours = Duration.ofHours(5);
        assertEquals(test, fiveHours);
    }

    @Test(groups={"tck"})
    public void test_toDuration_millis() {
        Duration test = SimplePeriod.of(5, MILLIS).toDuration();
        Duration fiveMillis = Duration.ofMillis(5);
        assertEquals(test, fiveMillis);
    }

    @Test(groups={"tck"})
    public void test_toDuration_days() {
        Duration test = SimplePeriod.of(5, DAYS).toDuration();
        Duration fiveDays = DAYS.getDuration().multipliedBy(5);
        assertEquals(test, fiveDays);
    }

    @Test(groups={"tck"})
    public void test_toDuration_months() {
        Duration test = SimplePeriod.of(5, MONTHS).toDuration();
        Duration fiveMonths = MONTHS.getDuration().multipliedBy(5);
        assertEquals(test, fiveMonths);
    }

    @Test(expectedExceptions=ArithmeticException.class, groups={"tck"})
    public void test_toDuration_tooBig() {
        SimplePeriod.of(Long.MAX_VALUE, MINUTES).toDuration();
    }

    //-----------------------------------------------------------------------
    // compareTo()
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_compareTo() {
        SimplePeriod a = SimplePeriod.of(5, DAYS);
        SimplePeriod b = SimplePeriod.of(6, DAYS);
        assertEquals(a.compareTo(a), 0);
        assertEquals(a.compareTo(b) < 0, true);
        assertEquals(b.compareTo(a) > 0, true);
    }

    @Test(expectedExceptions=IllegalArgumentException.class,groups={"tck"})
    public void test_compareTo_differentUnits() {
        SimplePeriod a = SimplePeriod.of(6 * 60, MINUTES);
        SimplePeriod b = SimplePeriod.of(5, HOURS);
        a.compareTo(b);
    }

    @Test(expectedExceptions = {NullPointerException.class}, groups={"tck"})
    public void test_compareTo_null() {
        SimplePeriod test5 = SimplePeriod.of(5, DAYS);
        test5.compareTo(null);
    }

    //-----------------------------------------------------------------------
    // equals()
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_equals() {
        SimplePeriod a = SimplePeriod.of(5, DAYS);
        SimplePeriod b = SimplePeriod.of(6, DAYS);
        assertEquals(a.equals(a), true);
        assertEquals(a.equals(b), false);
        assertEquals(b.equals(a), false);
    }

    @Test(groups={"tck"})
    public void test_equals_null() {
        SimplePeriod test = SimplePeriod.of(5, DAYS);
        assertEquals(test.equals(null), false);
    }

    @Test(groups={"tck"})
    public void test_equals_otherClass() {
        SimplePeriod test = SimplePeriod.of(5, DAYS);
        assertEquals(test.equals(""), false);
    }

    //-----------------------------------------------------------------------
    // hashCode()
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_hashCode() {
        SimplePeriod a = SimplePeriod.of(5, DAYS);
        SimplePeriod b = SimplePeriod.of(6, DAYS);
        SimplePeriod c = SimplePeriod.of(5, HOURS);
        assertEquals(a.hashCode() == a.hashCode(), true);
        assertEquals(a.hashCode() == b.hashCode(), false);
        assertEquals(a.hashCode() == c.hashCode(), false);
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    @Test(groups={"tck"})
    public void test_toString() {
        SimplePeriod test5 = SimplePeriod.of(5, DAYS);
        assertEquals(test5.toString(), "5 Days");
        SimplePeriod testM1 = SimplePeriod.of(-1, MONTHS);
        assertEquals(testM1.toString(), "-1 Months");
    }

}
