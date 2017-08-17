/*
 * This file is part of the Disco Deterministic Network Calculator v2.4.0beta2 "Chimera".
 *
 * Copyright (C) 2005 - 2007 Frank A. Zdarsky
 * Copyright (C) 2011 - 2017 Steffen Bondorf
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

package de.uni_kl.cs.disco.nc.operations;

import de.uni_kl.cs.disco.curves.ArrivalCurve;
import de.uni_kl.cs.disco.curves.CurvePwAffineFactoryDispatch;
import de.uni_kl.cs.disco.curves.CurvePwAffineUtilsDispatch;
import de.uni_kl.cs.disco.curves.ServiceCurve;
import de.uni_kl.cs.disco.numbers.Num;
import de.uni_kl.cs.disco.numbers.NumFactoryDispatch;
import de.uni_kl.cs.disco.numbers.NumUtilsDispatch;

public class DelayBound {
    private DelayBound() {}
    
    private static Num deriveForSpecialCurves(ArrivalCurve arrival_curve, ServiceCurve service_curve) {
        if (arrival_curve.equals(CurvePwAffineFactoryDispatch.createZeroArrivals())) {
            return NumFactoryDispatch.createZero();
        }
        if (service_curve.getDelayedInfiniteBurst_Property()) {
            // Assumption: the arrival curve does not have an initial latency.
            //             Otherwise its sub-additive closure would be zero, i.e., the arrival curve would not be sensible.
            return service_curve.getLatency().copy();
        }
        if (service_curve.equals(CurvePwAffineFactoryDispatch.createZeroService())  // We know from above that the arrivals are not zero.
                || arrival_curve.getUltAffineRate().gt(service_curve.getUltAffineRate())) {
            return NumFactoryDispatch.createPositiveInfinity();
        }
        return null;
    }

    public static Num deriveARB(ArrivalCurve arrival_curve, ServiceCurve service_curve) {
        Num result = deriveForSpecialCurves(arrival_curve, service_curve);
        if (result != null) {
            return result;
        }

        return CurvePwAffineUtilsDispatch.getXIntersection(arrival_curve, service_curve);
    }

    // Single flow to be bound, i.e., fifo per micro flow holds
    public static Num deriveFIFO(ArrivalCurve arrival_curve, ServiceCurve service_curve) {

        Num result = deriveForSpecialCurves(arrival_curve, service_curve);
        if (result != null) {
            return result;
        }

        result = NumFactoryDispatch.createNegativeInfinity();
        for (int i = 0; i < arrival_curve.getSegmentCount(); i++) {
            Num ip_y = arrival_curve.getSegment(i).getY();

            Num delay = NumUtilsDispatch.sub(service_curve.f_inv(ip_y, true), arrival_curve.f_inv(ip_y, false));
            result = NumUtilsDispatch.max(result, delay);
        }
        for (int i = 0; i < service_curve.getSegmentCount(); i++) {
            Num ip_y = service_curve.getSegment(i).getY();

            Num delay = NumUtilsDispatch.sub(service_curve.f_inv(ip_y, true), arrival_curve.f_inv(ip_y, false));
            result = NumUtilsDispatch.max(result, delay);
        }

        return NumUtilsDispatch.max(NumFactoryDispatch.getZero(), result);
    }
}