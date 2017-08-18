/*
 * This file is part of the Disco Deterministic Network Calculator v2.4.0beta3 "Chimera".
 *
 * Copyright (C) 2017 Steffen Bondorf
 * Copyright (C) 2017 The DiscoDNC contributors
 *
 * Distributed Computer Systems (DISCO) Lab
 * University of Kaiserslautern, Germany
 *
 * http://disco.cs.uni-kl.de/index.php/projects/disco-dnc
 *
 *
 * The Disco Deterministic Network Calculator (DiscoDNC) is free software;
 * you can redistribute it and/or modify it under the terms of the 
 * GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */

package de.uni_kl.cs.disco.numbers.rational;

import de.uni_kl.cs.disco.numbers.Num;
import de.uni_kl.cs.disco.numbers.NumUtils;

public class IntUtils implements NumUtils {
	private static IntUtils instance = new IntUtils();

    protected IntUtils() {} 
	
	public static IntUtils getInstance() {
		return instance;
	}
	
    public Num add(Num num1, Num num2) {
        if (num1 instanceof NaN || num2 instanceof NaN
                || (num1 instanceof PositiveInfinity && num2 instanceof NegativeInfinity)
                || (num1 instanceof NegativeInfinity && num2 instanceof PositiveInfinity)) {
            return new NaN();
        }
        if (num1 instanceof PositiveInfinity || num2 instanceof PositiveInfinity) { // other num is not negative infinity
            return new PositiveInfinity();
        }
        if (num1 instanceof NegativeInfinity || num2 instanceof NegativeInfinity) { // other num is not positive infinity
            return new NegativeInfinity();
        }

        return Int.add((Int) num1, (Int) num2);
    }

    public Num sub(Num num1, Num num2) {
        if (num1 instanceof NaN || num2 instanceof NaN) {
            return new NaN();
        }

        if (num1 instanceof NaN || num2 instanceof NaN
                || (num1 instanceof PositiveInfinity && num2 instanceof PositiveInfinity)
                || (num1 instanceof NegativeInfinity && num2 instanceof NegativeInfinity)) {
            return new NaN();
        }
        if (num1 instanceof PositiveInfinity                // num2 is not positive infinity
                || num2 instanceof NegativeInfinity) {    // num1 is not negative infinity
            return new PositiveInfinity();
        }
        if (num1 instanceof NegativeInfinity                // num2 is not negative infinity
                || num2 instanceof PositiveInfinity) {    // num1 is not positive infinity
            return new NegativeInfinity();
        }

        return Int.sub((Int) num1, (Int) num2);
    }

    public Num mult(Num num1, Num num2) {
        if (num1 instanceof NaN || num2 instanceof NaN) {
            return new NaN();
        }
        if (num1 instanceof PositiveInfinity) {
            if (num2.ltZero() || num2 instanceof NegativeInfinity) {
                return new NegativeInfinity();
            } else {
                return new PositiveInfinity();
            }
        }
        if (num2 instanceof PositiveInfinity) {
            if (num1.ltZero() || num1 instanceof NegativeInfinity) {
                return new NegativeInfinity();
            } else {
                return new PositiveInfinity();
            }
        }
        if (num1 instanceof NegativeInfinity) {
            if (num2.ltZero() || num2 instanceof NegativeInfinity) {
                return new PositiveInfinity();
            } else {
                return new NegativeInfinity();
            }
        }
        if (num2 instanceof NegativeInfinity) {
            if (num1.ltZero() || num1 instanceof NegativeInfinity) {
                return new PositiveInfinity();
            } else {
                return new NegativeInfinity();
            }
        }

        return Int.mult((Int) num1, (Int) num2);
    }

    public Num div(Num num1, Num num2) {
        if (num1 instanceof NaN || num2 instanceof NaN
                || ((num1 instanceof PositiveInfinity || num1 instanceof NegativeInfinity)
                && (num2 instanceof PositiveInfinity || num2 instanceof NegativeInfinity))) { // two infinities in the division
            return new NaN();
        }
        if (num1 instanceof PositiveInfinity) { // positive infinity divided by some finite value
            if (num2.ltZero()) {
                return new NegativeInfinity();
            } else {
                return new PositiveInfinity();
            }
        }
        if (num1 instanceof NegativeInfinity) { // negative infinity divided by some finite value
            if (num2.ltZero()) {
                return new PositiveInfinity();
            } else {
                return new NegativeInfinity();
            }
        }
        if (num2 instanceof PositiveInfinity || num2 instanceof NegativeInfinity) { // finite value divided by infinity
            return new Int(0);
        }

        if (((Int) num2).eqZero()) {
            return new PositiveInfinity();
        } else {
            return Int.div((Int) num1, (Int) num2);
        }
    }

    public Num abs(Num num) {
        if (num instanceof NaN) {
            return new NaN();
        }
        if (num instanceof PositiveInfinity || num instanceof NegativeInfinity) {
            return new PositiveInfinity();
        }

        return Int.abs((Int) num);
    }

    public Num diff(Num num1, Num num2) {
        if (num1 instanceof NaN || num2 instanceof NaN) {
            return new NaN();
        }
        if (num1 instanceof PositiveInfinity || num2 instanceof PositiveInfinity
                || num1 instanceof NegativeInfinity || num2 instanceof NegativeInfinity) {
            return new PositiveInfinity();
        }

        return Int.diff((Int) num1, (Int) num2);
    }

    public Num max(Num num1, Num num2) {
        if (num1 instanceof NaN || num2 instanceof NaN) {
            return new NaN();
        }
        if (num1 instanceof PositiveInfinity || num2 instanceof PositiveInfinity) {
            return new PositiveInfinity();
        }
        if (num1 instanceof NegativeInfinity) {
            return num2.copy();
        }
        if (num2 instanceof NegativeInfinity) {
            return num1.copy();
        }

        return Int.max((Int) num1, (Int) num2);
    }

    public Num min(Num num1, Num num2) {
        if (num1 instanceof NaN || num2 instanceof NaN) {
            return new NaN();
        }
        if (num1 instanceof NegativeInfinity || num2 instanceof NegativeInfinity) {
            return new NegativeInfinity();
        }
        if (num1 instanceof PositiveInfinity) {
            return num2.copy();
        }
        if (num2 instanceof PositiveInfinity) {
            return num1.copy();
        }

        return Int.min((Int) num1, (Int) num2);
    }

    public Num negate(Num num) {
        if (num instanceof NaN) {
            return new NaN();
        }
        if (num instanceof PositiveInfinity) {
            return new NegativeInfinity();
        }
        if (num instanceof NegativeInfinity) {
            return new PositiveInfinity();
        }

        return Int.negate((Int) num);
    }

    public boolean isFinite(Num num) {
        if (num instanceof Int) { // Only stores finite values
            return true;
        } else {
            return false; // NaN is neither finite nor infinite
        }
    }

    public boolean isInfinite(Num num) {
        if ((num instanceof PositiveInfinity) || (num instanceof NegativeInfinity)) {
            return true;
        } else {
            return false; // NaN is neither finite nor infinite
        }
    }

    public boolean isNaN(Num num) {
        if (num instanceof NaN) {
            return true;
        } else {
            return false;
        }
    }
}