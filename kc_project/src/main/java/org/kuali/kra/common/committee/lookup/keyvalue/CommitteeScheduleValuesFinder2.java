/*
 * Copyright 2005-2013 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.common.committee.lookup.keyvalue;

import java.util.List;

import org.kuali.kra.common.committee.service.CommitteeServiceBase;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.protocol.ProtocolFormBase;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kns.util.KNSGlobalVariables;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

/**
 * Finds the available set of dates where a protocol can be scheduled
 * for a review by a committee.  This values finder is almost exactly 
 * the same as CommitteeScheduleValuesFinderBase, except that the committee
 * information comes from the AssignCmtSchedBean.
 * 
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public class CommitteeScheduleValuesFinder2 extends KeyValuesBase {
    
    /**
     * @return the list of &lt;key, value&gt; pairs of committees.  The first entry
     * is always &lt;"", "select:"&gt;.
     * @see org.kuali.core.lookup.keyvalues.KeyValuesFinder#getKeyValues()
     */
    public List<KeyValue> getKeyValues() {
        return getCommitteeService().getAvailableCommitteeDates(getCommitteeId());
    }
    
    /**
     * Get the CommitteeBase Service.
     * @return the CommitteeBase Service
     */
    private CommitteeServiceBase getCommitteeService() {
        return KraServiceLocator.getService(CommitteeServiceBase.class);
    }

    /**
     * Get the committee id.  Currently we are only concerned with
     * scheduling protocols.  The committee id is found in via the ProtocolFormBase.
     * Keep in mind that the user selects the committee via a drop-down and
     * thus the selected committee id is placed into the form.
     * @return
     */
    private String getCommitteeId() {
        String committeeId = "";
        KualiForm form = KNSGlobalVariables.getKualiForm();
        if (form instanceof ProtocolFormBase) {
            ProtocolFormBase protocolForm = (ProtocolFormBase) form;
            // this needs to be different for irb and iacuc. iacuc does not have a assignCmtSchedBean
            //committeeId = ()protocolForm.getActionHelper().getAssignCmtSchedBean().getCommitteeId();
        }
        return committeeId;
    }
}
