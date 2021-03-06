/*
 * Copyright 2005-2013 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.protocol.actions.correspondence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kuali.kra.protocol.correspondence.ProtocolCorrespondenceTemplateBase;
import org.kuali.rice.krad.service.BusinessObjectService;

public abstract class ProtocolActionTypeToCorrespondenceTemplateServiceImplBase implements ProtocolActionTypeToCorrespondenceTemplateService {
    
    protected abstract Map<String, List<String>> getActionTypesToCorrespondenceTypeMap();
    
    protected abstract List<ProtocolCorrespondenceTemplateBase> getCorrespondenceTemplatesForTypeId(String correspondenceTypeId);
    
    private BusinessObjectService businessObjectService;

    /**
     * 
     * @see org.kuali.kra.irb.actions.correspondence.ProtocolActionTypeToCorrespondenceTemplateService#getTemplatesByProtocolAction(java.lang.String)
     */
    public List<ProtocolCorrespondenceTemplateBase> getTemplatesByProtocolAction(String protocolActionType) {
        List<ProtocolCorrespondenceTemplateBase> templates = new ArrayList<ProtocolCorrespondenceTemplateBase>();
        if (getActionTypesToCorrespondenceTypeMap().containsKey(protocolActionType)) {
            for (String correspondenceTypeId : getActionTypesToCorrespondenceTypeMap().get(protocolActionType)) {
                templates.addAll(getCorrespondenceTemplatesForTypeId(correspondenceTypeId));
            }
        }
        return templates;
    }
    
    public void setBusinessObjectService(BusinessObjectService businessObjectService){
        this.businessObjectService = businessObjectService;
    }
    
    protected BusinessObjectService getBusinessObjectService(){
        return this.businessObjectService;
    }
}
