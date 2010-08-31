/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.clinicalsummary.rule;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.logic.LogicContext;
import org.openmrs.logic.LogicCriteria;
import org.openmrs.logic.LogicException;
import org.openmrs.logic.Rule;
import org.openmrs.logic.result.Result;
import org.openmrs.logic.rule.RuleParameterInfo;
import org.openmrs.module.clinicalsummary.SummaryService;
import org.openmrs.module.clinicalsummary.cache.SummaryDataSource;

/**
 * Abstract class to get the latest observations on a certain concept
 */
public abstract class BaseLatestSingleObsRule implements Rule {
	
	private static final Log log = LogFactory.getLog(BaseLatestSingleObsRule.class);
	
	/**
	 * @see org.openmrs.logic.Rule#eval(org.openmrs.logic.LogicContext, org.openmrs.Patient,
	 *      java.util.Map)
	 */
	@Override
	public Result eval(LogicContext context, Patient patient, Map<String, Object> parameters) throws LogicException {
		
		SummaryService service = Context.getService(SummaryService.class);
		
		String evaluatedConcept = String.valueOf(parameters.get(RuleConstants.EVALUATED_CONCEPT));
		
		Object o = parameters.get(RuleConstants.INCLUDED_ENCOUNTER_TYPES);
		
		LogicCriteria encounterCriteria = service.parseToken(SummaryDataSource.ENCOUNTER_TYPE).in((Collection<?>) o);
		LogicCriteria obsCriteria = service.parseToken(SummaryDataSource.CONCEPT).equalTo(evaluatedConcept);
		
		Result obsResults = context.read(patient, service.getLogicDataSource("summary"), obsCriteria.and(encounterCriteria));
		
		if (log.isDebugEnabled())
			log.debug("Patient: " + patient.getPatientId() + ", hiv test: " + obsResults);
		
		return obsResults.latest();
	}
	
	/**
	 * @see org.openmrs.logic.Rule#getDependencies()
	 */
	@Override
	public String[] getDependencies() {
		return null;
	}
	
	/**
	 * @see org.openmrs.logic.Rule#getParameterList()
	 */
	@Override
	public Set<RuleParameterInfo> getParameterList() {
		return null;
	}
	
	/**
	 * @see org.openmrs.logic.Rule#getTTL()
	 */
	@Override
	public int getTTL() {
		return 0;
	}
	
}