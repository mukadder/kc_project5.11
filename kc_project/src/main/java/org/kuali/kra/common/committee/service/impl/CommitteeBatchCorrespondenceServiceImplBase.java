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
package org.kuali.kra.common.committee.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.kra.common.committee.bo.CommitteeBatchCorrespondenceBase;
import org.kuali.kra.common.committee.bo.CommitteeBatchCorrespondenceDetailBase;
import org.kuali.kra.common.committee.print.CommitteeReportType;
import org.kuali.kra.common.committee.service.CommitteeBatchCorrespondenceServiceBase;
import org.kuali.kra.common.committee.print.service.CommitteePrintingServiceBase;
import org.kuali.kra.common.notification.service.KcNotificationService;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.printing.Printable;
import org.kuali.kra.printing.PrintingException;
import org.kuali.kra.printing.print.AbstractPrint;
import org.kuali.kra.protocol.ProtocolBase;
import org.kuali.kra.protocol.ProtocolDao;
import org.kuali.kra.protocol.actions.ProtocolActionBase;
import org.kuali.kra.protocol.actions.genericactions.ProtocolGenericActionService;
import org.kuali.kra.protocol.correspondence.BatchCorrespondenceBase;
import org.kuali.kra.protocol.correspondence.BatchCorrespondenceDetailBase;
import org.kuali.kra.protocol.correspondence.ProtocolCorrespondence;
import org.kuali.kra.protocol.correspondence.ProtocolCorrespondenceTemplateService;
import org.kuali.kra.protocol.correspondence.ProtocolCorrespondenceTypeBase;
import org.kuali.kra.service.KcEmailService;
import org.kuali.kra.service.KcPersonService;
import org.kuali.kra.util.DateUtils;
import org.kuali.kra.util.EmailAttachment;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * 
 * This class generates the batch correspondence of committees.
 */
public abstract class CommitteeBatchCorrespondenceServiceImplBase implements CommitteeBatchCorrespondenceServiceBase {

    protected static final Log LOG = LogFactory.getLog(CommitteeBatchCorrespondenceServiceImplBase.class);
    private static final String COMMITTEE_ID = "committeeId";
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String PROTO_CORRESP_TYPE_CODE = "protoCorrespTypeCode";
    private static final String BATCH_CORRESPONDENCE_TYPE_CODE = "batchCorrespondenceTypeCode";


    protected BusinessObjectService businessObjectService;
    protected ProtocolDao<? extends ProtocolBase> protocolDao;
    protected ProtocolGenericActionService protocolGenericActionService;
    protected ProtocolCorrespondenceTemplateService protocolCorrespondenceTemplateService;
    protected DocumentService documentService;
    protected KcPersonService kcPersonService;
    protected KcNotificationService kcNotificationService;
    protected KcEmailService kcEmailService;
    private DateTimeService dateTimeService;

    protected int finalActionCounter;

    
    public abstract CommitteeBatchCorrespondenceBase generateBatchCorrespondence(String batchCorrespondenceTypeCode, String committeeId, Date startDate, Date endDate) throws Exception;
    
// TODO *********commented the code below during IACUC refactoring*********     
//    /**
//     * This method generates the batch correspondence for a committee.
//     * @param batchCorrespondenceTypeCode
//     * @param startDate
//     * @param endDate
//     * @return CommitteeBatchCorrespondenceBase
//     * @throws Exception 
//     */
//    public CommitteeBatchCorrespondenceBase generateBatchCorrespondence(String batchCorrespondenceTypeCode, String committeeId, Date startDate, 
//            Date endDate) throws Exception {
//        BatchCorrespondenceBase batchCorrespondence = null;
//        List<? extends ProtocolBase> protocols = null;
//        finalActionCounter = 0;
//
//        CommitteeBatchCorrespondenceBase committeeBatchCorrespondence = new CommitteeBatchCorrespondenceBase(batchCorrespondenceTypeCode, 
//                committeeId, startDate, endDate);
//        
//        String protocolActionTypeCode;
//        
//        if (StringUtils.equals(batchCorrespondenceTypeCode, Constants.PROTOCOL_RENEWAL_REMINDERS)) {
//            protocols = protocolDao.getExpiringProtocols(committeeId, startDate, endDate);
//            protocolActionTypeCode = Constants.IACUC_PROTOCOL_ACTION_TYPE_CODE_RENEWAL_REMINDER_GENERATED;
//        } else if (StringUtils.equals(batchCorrespondenceTypeCode, Constants.REMINDER_TO_IACUC_NOTIFICATIONS)) {
//            protocols = protocolDao.getNotifiedProtocols(committeeId, startDate, endDate);
//            protocolActionTypeCode = Constants.IACUC_PROTOCOL_ACTION_TYPE_CODE_IACUC_REMINDER_GENERATED;
//        } else {
//            throw new IllegalArgumentException(batchCorrespondenceTypeCode);
//        }
//
//        batchCorrespondence = lookupBatchCorrespondence(batchCorrespondenceTypeCode);
//        
//        for (ProtocolBase protocol : protocols) {
//            ProtocolCorrespondenceTypeBase protocolCorrespondenceType = getProtocolCorrespondenceTypeToGenerate(protocol, batchCorrespondence);
//
//            if (protocolCorrespondenceType != null)  {
//                if (protocolCorrespondenceTemplateService.getProtocolCorrespondenceTemplate(committeeId, 
//                        protocolCorrespondenceType.getProtoCorrespTypeCode()) == null) {
//                    LOG.warn("Correspondence template \"" + protocolCorrespondenceType.getDescription() + "\" is missing.  Correspondence for protocol " 
//                            + protocol.getProtocolNumber() + " has not been generated.  Add the missing template and regenerate correspondence.");
//                } else {
//                    CommitteeBatchCorrespondenceDetailBase batchCorrespondenceDetail = createBatchCorrespondenceDetail(committeeId, protocol, 
//                            protocolCorrespondenceType, committeeBatchCorrespondence.getCommitteeBatchCorrespondenceId(), protocolActionTypeCode);
//                    committeeBatchCorrespondence.getCommitteeBatchCorrespondenceDetails().add(batchCorrespondenceDetail);
//                    
//                    Long detailId = batchCorrespondenceDetail.getCommitteeBatchCorrespondenceDetailId();
//                    String description = protocolCorrespondenceType.getDescription();
//                    //String userFullName = kcPersonService.getKcPersonByPersonId(GlobalVariables.getUserSession().getPrincipalId()).getFullName();
//                    String userFullName = Constants.EMPTY_STRING;
//                
//                    IacucBatchCorrespondenceNotificationRenderer renderer 
//                        = new IacucBatchCorrespondenceNotificationRenderer((IacucProtocol) protocol, detailId, description, userFullName);
//                    IacucProtocolNotificationContext context 
//                        = new IacucProtocolNotificationContext((IacucProtocol) protocol, IacucProtocolActionType.RENEWAL_REMINDER_GENERATED, "Renewal Reminder Generated", renderer);
//                    context.setEmailAttachments(getEmailAttachments(batchCorrespondenceDetail.getProtocolCorrespondence()));
//                    kcNotificationService.sendNotification(context);
//                    
//                    
//                }
//            }
//        }
//
//        businessObjectService.save(committeeBatchCorrespondence);
//        
//        committeeBatchCorrespondence.setFinalActionCounter(finalActionCounter);
//        
//        return committeeBatchCorrespondence;
//    }
    
    /**
     * This method determines if and for which ProtocolCorrespondenceTypeBase a batch correspondence needs to be generated.
     * The final action is being applied at this time as well.
     * 
     * @param protocol
     * @param batchCorrespondence
     * @return The ProtocolCorrespondeceType for which correspondence needs to be generated.  
     *         Null if no correspondence needs to be generated.
     * @throws Exception
     */
    protected ProtocolCorrespondenceTypeBase getProtocolCorrespondenceTypeToGenerate(ProtocolBase protocol, BatchCorrespondenceBase batchCorrespondence) throws Exception {
        ProtocolCorrespondenceTypeBase protocolCorrespondenceType = null;

        if (StringUtils.equals(batchCorrespondence.getSendCorrespondence(), BatchCorrespondenceBase.SEND_CORRESPONDENCE_BEFORE_EVENT)) {
            protocolCorrespondenceType = getBeforeProtocolCorrespondenceTypeToGenerate(protocol, batchCorrespondence);
        } else {
            protocolCorrespondenceType = getAfterProtocolCorrespondenceTypeToGenerate(protocol, batchCorrespondence);
        }
        
        if ((protocolCorrespondenceType != null) && (correspondencePreviouslyGenerated(protocol, protocolCorrespondenceType))) {
            return null;
        } else {
            return protocolCorrespondenceType;
        }
    }
    
    /**
     * This method assists with determining what ProtocolCorrespondenceTypeBase is applicable to be generated at this time for
     * correspondences that are to be send before the event.
     * 
     * @param protocol
     * @param batchCorrespondence
     * @return The ProtocolCorrespondenceTypeBase for which correspondence may be generated at this time.  
     *         Null if no correspondence needs to be generated.
     * @throws Exception
     */
    protected ProtocolCorrespondenceTypeBase getBeforeProtocolCorrespondenceTypeToGenerate(ProtocolBase protocol, 
            BatchCorrespondenceBase batchCorrespondence) throws Exception {
        ProtocolCorrespondenceTypeBase protocolCorrespondenceType = null;
        
        double diff = DateUtils.getDifferenceInDays(new Timestamp(System.currentTimeMillis()), new Timestamp(protocol.getExpirationDate().getTime()));
        
        for (BatchCorrespondenceDetailBase batchCorrespondenceDetail : batchCorrespondence.getBatchCorrespondenceDetails()) {
            if (batchCorrespondenceDetail.getDaysToEvent() >= diff) { 
                protocolCorrespondenceType = batchCorrespondenceDetail.getProtocolCorrespondenceType();
            }
        }
        
        if (batchCorrespondence.getFinalActionDay() >= diff) {
            protocolCorrespondenceType = batchCorrespondence.getProtocolCorrespondenceType();
            applyFinalAction(protocol, batchCorrespondence);
        }

        return protocolCorrespondenceType;
    }

    /**
     * This method assists with determining what ProtocolCorrespondenceTypeBase is applicable to be generated at this time for
     * correspondences that are to be send after the event.
     * 
     * @param protocol
     * @param batchCorrespondence
     * @return The ProtocolCorrespondenceTypeBase for which correspondence may be generated at this time.  
     *         Null if no correspondence needs to be generated.
     * @throws Exception
     */
    protected ProtocolCorrespondenceTypeBase getAfterProtocolCorrespondenceTypeToGenerate(ProtocolBase protocol, 
            BatchCorrespondenceBase batchCorrespondence) throws Exception {
        ProtocolCorrespondenceTypeBase protocolCorrespondenceType = null;

        //double diff = DateUtils.getDifferenceInDays(protocol.getLastProtocolAction().getUpdateTimestamp(), new Timestamp(System.currentTimeMillis()));
        double diff = DateUtils.getDifferenceInDays(protocol.getLastProtocolAction().getActionDate(), new Timestamp(System.currentTimeMillis()));

        for (BatchCorrespondenceDetailBase batchCorrespondenceDetail : batchCorrespondence.getBatchCorrespondenceDetails()) {
            if (batchCorrespondenceDetail.getDaysToEvent() <= diff) { 
                protocolCorrespondenceType = batchCorrespondenceDetail.getProtocolCorrespondenceType();
            }
        }

        if (batchCorrespondence.getFinalActionDay() <= diff) {
            protocolCorrespondenceType = batchCorrespondence.getProtocolCorrespondenceType();
            applyFinalAction(protocol, batchCorrespondence);
        }

        return protocolCorrespondenceType;
    }

    
    
    protected abstract void applyFinalAction(ProtocolBase protocol, BatchCorrespondenceBase batchCorrespondence) throws Exception;
    
// TODO *********commented the code below during IACUC refactoring*********    
//    /**
//     * This method applies the final action to the protocol.
//     * 
//     * @param protocol
//     * @param batchCorrespondence
//     * @throws Exception
//     */
//    protected void applyFinalAction(ProtocolBase protocol, BatchCorrespondenceBase batchCorrespondence) throws Exception {
//    
//        ProtocolGenericActionBean actionBean = new IacucProtocolGenericActionBean(null, Constants.EMPTY_STRING);
//        actionBean.setComments("Final action of batch Correspondence: " + batchCorrespondence.getDescription());
//        
//        if (StringUtils.equals(IacucProtocolActionType.SUSPENDED, batchCorrespondence.getFinalActionTypeCode())) {
//            try {
//                protocol.getProtocolDocument().getDocumentHeader().getWorkflowDocument();
//            }
//            catch (RuntimeException ex) {
//                protocol.setProtocolDocument((ProtocolDocument) documentService.getByDocumentHeaderId(protocol.getProtocolDocument().getDocumentNumber()));
//            }
//            protocolGenericActionService.suspend(protocol, actionBean);
//            finalActionCounter++;
//        }
//        
//        if (StringUtils.equals(IacucProtocolActionType.EXPIRED, batchCorrespondence.getFinalActionTypeCode())) {
//            try {
//                protocol.getProtocolDocument().getDocumentHeader().getWorkflowDocument();
//            }
//            catch (RuntimeException ex) {
//                protocol.setProtocolDocument((ProtocolDocument) documentService.getByDocumentHeaderId(protocol.getProtocolDocument().getDocumentNumber()));
//            }
//            protocolGenericActionService.expire(protocol, actionBean);
//            finalActionCounter++;
//        }
//        
//    }

    
    /**
     * 
     * This method determines if the notification for the protocol has already been generated.
     * @param protocol
     * @param protocolCorrespondenceType
     * @return true if the correspondence has already been generated, false otherwise
     */
    protected boolean correspondencePreviouslyGenerated(ProtocolBase protocol, ProtocolCorrespondenceTypeBase protocolCorrespondenceType) {
        Map<String, String> fieldValues = new HashMap<String, String>();
        fieldValues.put(PROTOCOL_NUMBER, protocol.getProtocolNumber());
        fieldValues.put(SEQUENCE_NUMBER, protocol.getSequenceNumber().toString());
        fieldValues.put(PROTO_CORRESP_TYPE_CODE, protocolCorrespondenceType.getProtoCorrespTypeCode());
        
// TODO *********commented the code below during IACUC refactoring*********         
//        return !businessObjectService.findMatching(IacucProtocolCorrespondence.class, fieldValues).isEmpty();
        
        return !businessObjectService.findMatching(getProtocolCorrespondenceBOClassHook(), fieldValues).isEmpty();
        
    }

    protected abstract Class<? extends ProtocolCorrespondence> getProtocolCorrespondenceBOClassHook();
    
    
    /**
     * 
     * This method creates the CommitteeBatchCorrespondenceDetailBase and all associated business objects.  
     * The associated business objects are persisted to the database on creation.
     * @param protocol
     * @param protocolCorrespondenceType
     * @param committeeBatchCorrespondenceId
     * @param protocolActionTypeCode
     * @return the populated CommitteeBatchCorrespondenceDetailBase
     * @throws PrintingException 
     */
    protected CommitteeBatchCorrespondenceDetailBase createBatchCorrespondenceDetail(String committeeId, ProtocolBase protocol, 
            ProtocolCorrespondenceTypeBase protocolCorrespondenceType, String committeeBatchCorrespondenceId, 
            String protocolActionTypeCode) throws PrintingException {
        
// TODO *********commented the code below during IACUC refactoring*********         
//        CommitteeBatchCorrespondenceDetailBase committeeBatchCorrespondenceDetail = new CommitteeBatchCorrespondenceDetailBase();
        
        CommitteeBatchCorrespondenceDetailBase committeeBatchCorrespondenceDetail = getNewCommitteeBatchCorrespondenceDetailInstanceHook();
        
        committeeBatchCorrespondenceDetail.setCommitteeBatchCorrespondenceId(committeeBatchCorrespondenceId);
        
        committeeBatchCorrespondenceDetail.setProtocolAction(createAndSaveProtocolAction(protocol, protocolCorrespondenceType, protocolActionTypeCode));
        committeeBatchCorrespondenceDetail.setProtocolActionId(committeeBatchCorrespondenceDetail.getProtocolAction().getProtocolActionId());

        committeeBatchCorrespondenceDetail.setProtocolCorrespondence(createAndSaveProtocolCorrespondence(committeeId,
                protocol, protocolCorrespondenceType, committeeBatchCorrespondenceDetail.getProtocolAction()));
        committeeBatchCorrespondenceDetail.setProtocolCorrespondenceId(committeeBatchCorrespondenceDetail.getProtocolCorrespondence().getId());
        committeeBatchCorrespondenceDetail.setCommitteeBatchCorrespondenceDetailId(KraServiceLocator.getService(SequenceAccessorService.class)
                .getNextAvailableSequenceNumber("SEQ_COMMITTEE_ID"));

        return committeeBatchCorrespondenceDetail;
    }

    protected abstract CommitteeBatchCorrespondenceDetailBase getNewCommitteeBatchCorrespondenceDetailInstanceHook();

    
    
    /**
     * 
     * This method creates the ProtocolActionBase business object and persists it to the database.
     * @param protocol
     * @param protocolCorrespondenceType
     * @param protocolActionTypeCode
     * @return the populated ProtocolActionBase
     */
    protected ProtocolActionBase createAndSaveProtocolAction(ProtocolBase protocol, ProtocolCorrespondenceTypeBase protocolCorrespondenceType, 
            String protocolActionTypeCode) {
// TODO *********commented the code below during IACUC refactoring*********         
//        ProtocolActionBase protocolAction = new IacucProtocolAction((IacucProtocol) protocol, null, protocolActionTypeCode);
        
        ProtocolActionBase protocolAction = getNewProtocolActionInstanceHook(protocol, null, protocolActionTypeCode);
        protocolAction.setComments(protocolCorrespondenceType.getDescription());
        
        businessObjectService.save(protocolAction);
        return protocolAction;
    }

    protected abstract ProtocolActionBase getNewProtocolActionInstanceHook(ProtocolBase protocol, Object object, String protocolActionTypeCode);

    /**
     * 
     * This method creates the ProtocolCorrespondence business object and persists it to the database.  
     * @param protocol
     * @param protocolCorrespondenceType
     * @param protocolAction
     * @return the populated ProtocolCorrespondence
     * @throws PrintingException 
     */
    protected ProtocolCorrespondence createAndSaveProtocolCorrespondence(String committeeId, ProtocolBase protocol, 
            ProtocolCorrespondenceTypeBase protocolCorrespondenceType, ProtocolActionBase protocolAction) throws PrintingException {
        
// TODO *********commented the code below during IACUC refactoring*********               
//        ProtocolCorrespondence protocolCorrespondence = new IacucProtocolCorrespondence();
        
        ProtocolCorrespondence protocolCorrespondence = getNewProtocolCorrespondenceInstanceHook();
        
        protocolCorrespondence.setProtocolId(protocol.getProtocolId());
        protocolCorrespondence.setProtocolNumber(protocol.getProtocolNumber());
        protocolCorrespondence.setSequenceNumber(protocol.getSequenceNumber());
        protocolCorrespondence.setActionIdFk(protocolAction.getProtocolActionId());
        protocolCorrespondence.setActionId(protocolAction.getActionId());
        protocolCorrespondence.setProtoCorrespTypeCode(protocolCorrespondenceType.getProtoCorrespTypeCode());
        
        AbstractPrint printable = getCommitteePrintingService().getCommitteePrintable(CommitteeReportType.PROTOCOL_BATCH_CORRESPONDENCE);
        printable.setPrintableBusinessObject(protocol);
        Map<String, Object> reportParameters = new HashMap<String, Object>();
        reportParameters.put(COMMITTEE_ID, committeeId);
        reportParameters.put("submissionNumber", protocolAction.getSubmissionNumber());
        reportParameters.put(PROTO_CORRESP_TYPE_CODE, protocolCorrespondenceType.getProtoCorrespTypeCode());
        printable.setReportParameters(reportParameters);
        List<Printable> printableArtifactList = new ArrayList<Printable>();
        printableArtifactList.add(printable);
        protocolCorrespondence.setCorrespondence(getCommitteePrintingService().print(printableArtifactList).getContent());

        protocolCorrespondence.setFinalFlag(false);
        protocolCorrespondence.setCreateUser(GlobalVariables.getUserSession().getPrincipalName());
        protocolCorrespondence.setCreateTimestamp(dateTimeService.getCurrentTimestamp());
       
        protocolCorrespondence.setProtocol(protocol);
        protocolCorrespondence.setProtocolCorrespondenceType(protocolCorrespondenceType);
        
        businessObjectService.save(protocolCorrespondence);
        return protocolCorrespondence;
    }
    
    protected abstract ProtocolCorrespondence getNewProtocolCorrespondenceInstanceHook();
    

    
    protected List<EmailAttachment> getEmailAttachments(ProtocolCorrespondence protocolCorrespondence) {
        List<EmailAttachment> attachments = null;
        
        try {
            byte[] attachmentContents = protocolCorrespondence.getCorrespondence();
            if (attachmentContents != null) {
                    attachments = new ArrayList<EmailAttachment>();
                    String attachmentName = "correspondence_" + protocolCorrespondence.getProtocolNumber() + Constants.PDF_FILE_EXTENSION;
                    
                    EmailAttachment attachment = new EmailAttachment();
                    attachment.setFileName(attachmentName);
                    attachment.setMimeType(Constants.PDF_REPORT_CONTENT_TYPE);
                    attachment.setContents(attachmentContents);
                    attachments.add(attachment);         
            }
        } catch (Exception e) {
            LOG.error("Failed to get email attachments for batch correspondence.", e);
        }
        
        return attachments;
    }
    
    /**
     * 
     * This method looks up the BatchCorrespondenceBase business object via the batchCorrespondenceTypeCode.
     * @param batchCorrespondenceTypeCode
     * @return the BatchCorrespondenceBase business object
     */
    protected BatchCorrespondenceBase lookupBatchCorrespondence(String batchCorrespondenceTypeCode) {
        Map<String, String> fieldValues = new HashMap<String, String>();
        fieldValues.put(BATCH_CORRESPONDENCE_TYPE_CODE, batchCorrespondenceTypeCode);

// TODO *********commented the code below during IACUC refactoring*********         
//        return (BatchCorrespondenceBase) businessObjectService.findByPrimaryKey(IacucBatchCorrespondence.class, fieldValues);

        return businessObjectService.findByPrimaryKey(getBatchCorrespondenceBOClassHook(), fieldValues);
    }
    
    protected abstract Class<? extends BatchCorrespondenceBase> getBatchCorrespondenceBOClassHook();

    
    protected abstract CommitteePrintingServiceBase getCommitteePrintingService();
    
// TODO *********commented the code below during IACUC refactoring*********     
//    protected CommonCommitteePrintingService getCommitteePrintingService() {
//        return KraServiceLocator.getService(CommonCommitteePrintingService.class);
//    }

    /**
     * Populated by Spring Beans.
     * @param businessObjectService
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * Populated by Spring Beans.
     * @param protocolGenericActionService
     */
    public void setProtocolGenericActionService(ProtocolGenericActionService protocolGenericActionService) {
        this.protocolGenericActionService = protocolGenericActionService;
    }

    /**
     * Populated by Spring Beans.
     * @param protocolCorrespondenceTemplateService
     */
    public void setProtocolCorrespondenceTemplateService(ProtocolCorrespondenceTemplateService protocolCorrespondenceTemplateService) {
        this.protocolCorrespondenceTemplateService = protocolCorrespondenceTemplateService;
    }

    /**
     * Populated by Spring Beans.
     * @param documentService
     */
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Populated by Spring Beans.
     * @param kcPersonService
     */
    public void setKcPersonService(KcPersonService kcPersonService) {
        this.kcPersonService = kcPersonService;
    }
    
    /**
     * Populated by Spring Beans.
     * @param kcNotificationService
     */
    public void setKcNotificationService(KcNotificationService kcNotificationService) {
        this.kcNotificationService = kcNotificationService;
    }

    public void setDateTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }
    
    public ProtocolDao<? extends ProtocolBase> getProtocolDao() {
        return protocolDao;
    }

    public void setProtocolDao(ProtocolDao<? extends ProtocolBase> protocolDao) {
        this.protocolDao = protocolDao;
    }


}
