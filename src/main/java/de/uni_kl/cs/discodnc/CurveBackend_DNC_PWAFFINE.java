package de.uni_kl.cs.discodnc;

import de.uni_kl.cs.discodnc.curves.Curve;
import de.uni_kl.cs.discodnc.curves.LinearSegment;
import de.uni_kl.cs.discodnc.curves.dnc.LinearSegment_DNC;
import de.uni_kl.cs.discodnc.curves.dnc.pwaffine.Curve_DNC;
import de.uni_kl.cs.discodnc.minplus.MinPlus;
import de.uni_kl.cs.discodnc.minplus.dnc.pwaffine.MinPlus_DNC;

public enum CurveBackend_DNC_PWAFFINE implements CurveBackend {
	DNC_PWAFFINE;

	@Override
	public MinPlus getMinPlus() {
		return MinPlus_DNC.MINPLUS_DNC;
	}

	@Override
	public Curve getCurveFactory() {
		return Curve_DNC.getFactory();
	}

	@Override
	public LinearSegment.Builder getLinearSegmentFactory() {
		return LinearSegment_DNC.getBuilder();
	}
}
