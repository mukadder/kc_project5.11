ALTER TABLE PROTOCOL_SUBMISSION
DROP CONSTRAINT FK_PROTOCOL_SUBMISSION2;
ALTER TABLE comm_schedule_minutes  
DROP CONSTRAINT fk_csm_schedule_id_fk;
ALTER TABLE comm_schedule_attendance
DROP CONSTRAINT fk_csa_schedule_id_new;

DROP TABLE COMM_MEMBER_EXPERTISE;
DROP TABLE COMM_MEMBER_ROLES;
DROP TABLE COMM_MEMBERSHIPS;
DROP TABLE COMM_RESEARCH_AREAS;
DROP TABLE COMM_SCHEDULE;


-- Table Script
CREATE TABLE COMM_MEMBERSHIPS ( 
    COMM_MEMBERSHIP_ID NUMBER(12,0) NOT NULL,
    COMMITTEE_ID_FK NUMBER(12,0) NOT NULL,
    PERSON_ID VARCHAR2(9) NULL,
    ROLODEX_ID NUMBER(12,0) NULL, 
    PERSON_NAME VARCHAR2(90) NOT NULL, 
    MEMBERSHIP_ID VARCHAR2(10) NOT NULL,
    PAID_MEMBER_FLAG VARCHAR2(1) NOT NULL,
    TERM_START_DATE DATE NOT NULL,
    TERM_END_DATE DATE,
    MEMBERSHIP_TYPE_CODE VARCHAR2(3) NOT NULL,
    COMMENTS CLOB,
    CONTACT_NOTES CLOB,
    TRAINING_NOTES CLOB,
    UPDATE_TIMESTAMP DATE, 
    UPDATE_USER VARCHAR2(60), 
    VER_NBR NUMBER(8,0) DEFAULT 1 NOT NULL, 
    OBJ_ID VARCHAR2(36) DEFAULT SYS_GUID() NOT NULL);

-- Primary Key Constraint 
ALTER TABLE COMM_MEMBERSHIPS 
ADD CONSTRAINT PK_COMM_MEMBERSHIPS 
PRIMARY KEY (COMM_MEMBERSHIP_ID);
 
-- Foreign Key Constraint(s)  
ALTER TABLE COMM_MEMBERSHIPS 
ADD CONSTRAINT FK_COMM_MEMBERSHIPS 
FOREIGN KEY (COMMITTEE_ID_FK) 
REFERENCES COMMITTEE (ID);

ALTER TABLE COMM_MEMBERSHIPS 
ADD CONSTRAINT FK_COMM_MEMBERSHIPS_2 
FOREIGN KEY (MEMBERSHIP_TYPE_CODE) 
REFERENCES COMM_MEMBERSHIP_TYPE (MEMBERSHIP_TYPE_CODE);


-- Table Script
CREATE TABLE COMM_MEMBER_ROLES ( 
    COMM_MEMBER_ROLES_ID NUMBER(12,0) NOT NULL, 
    COMM_MEMBERSHIP_ID_FK NUMBER(12,0) NOT NULL, 
    MEMBERSHIP_ROLE_CODE VARCHAR2(3) NOT NULL, 
    START_DATE DATE NOT NULL, 
    END_DATE DATE NOT NULL, 
    UPDATE_TIMESTAMP DATE NOT NULL, 
    UPDATE_USER VARCHAR2(60) NOT NULL, 
    VER_NBR NUMBER(8,0) DEFAULT 1 NOT NULL,
    OBJ_ID VARCHAR2(36) DEFAULT SYS_GUID() NOT NULL);
    
-- Primary Key Constraint
ALTER TABLE COMM_MEMBER_ROLES 
ADD CONSTRAINT PK_COMM_MEMBER_ROLES 
PRIMARY KEY (COMM_MEMBER_ROLES_ID);

-- Foreign Key Constraint(s)
ALTER TABLE COMM_MEMBER_ROLES 
ADD CONSTRAINT FK_COMM_MEMBER_ROLES 
FOREIGN KEY (COMM_MEMBERSHIP_ID_FK) 
REFERENCES COMM_MEMBERSHIPS (COMM_MEMBERSHIP_ID); 

ALTER TABLE COMM_MEMBER_ROLES 
ADD CONSTRAINT FK_COMM_MEMBER_ROLES_2 
FOREIGN KEY (MEMBERSHIP_ROLE_CODE) 
REFERENCES MEMBERSHIP_ROLE (MEMBERSHIP_ROLE_CODE); 


-- Table Script
CREATE TABLE COMM_MEMBER_EXPERTISE ( 
    COMM_MEMBER_EXPERTISE_ID NUMBER(12,0) NOT NULL, 
    COMM_MEMBERSHIP_ID_FK NUMBER(12,0) NOT NULL, 
    RESEARCH_AREA_CODE VARCHAR2(8) NOT NULL, 
    UPDATE_TIMESTAMP DATE NOT NULL, 
    UPDATE_USER VARCHAR2(60) NOT NULL, 
    VER_NBR NUMBER(8,0) DEFAULT 1 NOT NULL, 
    OBJ_ID VARCHAR2(36) DEFAULT SYS_GUID() NOT NULL);

-- Primary Key Constraint 
ALTER TABLE COMM_MEMBER_EXPERTISE 
ADD CONSTRAINT PK_COMM_MEMBER_EXPERTISE 
PRIMARY KEY (COMM_MEMBER_EXPERTISE_ID);

-- Foreign Key Constraint(s)
ALTER TABLE COMM_MEMBER_EXPERTISE 
ADD CONSTRAINT FK_COMM_MEMBER_EXPERTISE 
FOREIGN KEY (COMM_MEMBERSHIP_ID_FK) 
REFERENCES COMM_MEMBERSHIPS (COMM_MEMBERSHIP_ID);

ALTER TABLE COMM_MEMBER_EXPERTISE 
ADD CONSTRAINT FK_COMM_MEMBER_EXPERTISE_2 
FOREIGN KEY (RESEARCH_AREA_CODE) 
REFERENCES RESEARCH_AREAS (RESEARCH_AREA_CODE); 


CREATE TABLE COMM_RESEARCH_AREAS (
    ID NUMBER(12, 0),
    COMMITTEE_ID_FK NUMBER(12,0) NOT NULL,
    RESEARCH_AREA_CODE VARCHAR2(8) NOT NULL,
    UPDATE_TIMESTAMP DATE NOT NULL,
    UPDATE_USER VARCHAR2(60) NOT NULL,
    VER_NBR NUMBER(8,0) DEFAULT 1 NOT NULL,
    OBJ_ID VARCHAR2(36) DEFAULT SYS_GUID() NOT NULL);

ALTER TABLE COMM_RESEARCH_AREAS
ADD CONSTRAINT PK_COMM_RESEARCH_AREAS
PRIMARY KEY (ID);

ALTER TABLE COMM_RESEARCH_AREAS
ADD CONSTRAINT FK_COMM_RESEARCH_AREAS1
FOREIGN KEY (COMMITTEE_ID_FK)
REFERENCES COMMITTEE (ID);

ALTER TABLE COMM_RESEARCH_AREAS
ADD CONSTRAINT FK_COMM_RESEARCH_AREAS2
FOREIGN KEY (RESEARCH_AREA_CODE)
REFERENCES RESEARCH_AREAS (RESEARCH_AREA_CODE);


-- Table Script 
CREATE TABLE COMM_SCHEDULE (
    ID NUMBER(12,0) NOT NULL,
    SCHEDULE_ID VARCHAR2(10) NOT NULL, 
    COMMITTEE_ID_FK NUMBER(12,0) NOT NULL,
    SCHEDULED_DATE DATE NOT NULL, 
    PLACE VARCHAR2(200), 
    TIME DATE, 
    PROTOCOL_SUB_DEADLINE DATE NOT NULL, 
    SCHEDULE_STATUS_CODE NUMBER(3,0) NOT NULL, 
    MEETING_DATE DATE, 
    START_TIME DATE, 
    END_TIME DATE, 
    AGENDA_PROD_REV_DATE DATE, 
    MAX_PROTOCOLS NUMBER(4,0), 
    COMMENTS VARCHAR2(2000), 
    UPDATE_TIMESTAMP DATE NOT NULL, 
    UPDATE_USER VARCHAR2(60) NOT NULL, 
    VER_NBR NUMBER(8,0) DEFAULT 1 NOT NULL, 
    OBJ_ID VARCHAR2(36) DEFAULT SYS_GUID() NOT NULL);


-- Primary Key Constraint 
ALTER TABLE COMM_SCHEDULE 
ADD CONSTRAINT PK_COMM_SCHEDULE 
PRIMARY KEY (ID);

-- Foreign Key Constraint(s)
ALTER TABLE COMM_SCHEDULE 
ADD CONSTRAINT FK_COMM_SCHEDULE_2 
FOREIGN KEY (COMMITTEE_ID_FK) 
REFERENCES COMMITTEE (ID);

ALTER TABLE COMM_SCHEDULE 
ADD CONSTRAINT FK_COMM_SCHEDULE 
FOREIGN KEY (SCHEDULE_STATUS_CODE) 
REFERENCES SCHEDULE_STATUS (SCHEDULE_STATUS_CODE);


ALTER TABLE PROTOCOL_SUBMISSION
ADD CONSTRAINT FK_PROTOCOL_SUBMISSION2
FOREIGN KEY (SCHEDULE_ID_FK)
REFERENCES COMM_SCHEDULE (ID);

ALTER TABLE comm_schedule_minutes  
ADD CONSTRAINT fk_csm_schedule_id_fk
FOREIGN KEY (schedule_id_fk) 
REFERENCES COMM_SCHEDULE (id);

ALTER TABLE comm_schedule_attendance
ADD CONSTRAINT fk_csa_schedule_id_new
FOREIGN KEY (schedule_id_new) 
REFERENCES comm_schedule (id);  



-- View for Coeus compatibility 
CREATE OR REPLACE VIEW OSP$COMM_MEMBERSHIPS AS
SELECT
    COMMITTEE.COMMITTEE_ID,
    DECODE (COMM_MEMBERSHIPS.PERSON_ID, NULL, TO_CHAR(COMM_MEMBERSHIPS.ROLODEX_ID),
                                                       COMM_MEMBERSHIPS.PERSON_ID) AS PERSON_ID,
    COMM_MEMBERSHIPS.MEMBERSHIP_ID,
    COMMITTEE.SEQUENCE_NUMBER,
    COMM_MEMBERSHIPS.PERSON_NAME,
    DECODE (COMM_MEMBERSHIPS.PERSON_ID, NULL, 'N',
                                              'Y') AS NON_EMPLOYEE_FLAG,
    COMM_MEMBERSHIPS.PAID_MEMBER_FLAG,
    COMM_MEMBERSHIPS.TERM_START_DATE,
    COMM_MEMBERSHIPS.TERM_END_DATE,
    COMM_MEMBERSHIPS.MEMBERSHIP_TYPE_CODE,
    COMM_MEMBERSHIPS.COMMENTS,
    COMM_MEMBERSHIPS.UPDATE_TIMESTAMP,
    COMM_MEMBERSHIPS.UPDATE_USER
FROM COMM_MEMBERSHIPS, COMMITTEE
WHERE COMM_MEMBERSHIPS.COMMITTEE_ID_FK = COMMITTEE.ID;


-- View for Coeus compatibility
CREATE OR REPLACE VIEW OSP$COMM_MEMBER_ROLES AS
SELECT 
    COMM_MEMBERSHIPS.MEMBERSHIP_ID, 
    COMMITTEE.SEQUENCE_NUMBER, 
    COMM_MEMBER_ROLES.MEMBERSHIP_ROLE_CODE, 
    COMM_MEMBER_ROLES.START_DATE, 
    COMM_MEMBER_ROLES.END_DATE, 
    COMM_MEMBER_ROLES.UPDATE_TIMESTAMP, 
    COMM_MEMBER_ROLES.UPDATE_USER
FROM COMM_MEMBER_ROLES, COMM_MEMBERSHIPS, COMMITTEE
WHERE COMM_MEMBER_ROLES.COMM_MEMBERSHIP_ID_FK = COMM_MEMBERSHIPS.COMM_MEMBERSHIP_ID
  AND COMM_MEMBERSHIPS.COMMITTEE_ID_FK = COMMITTEE.ID;
  
  
-- View for Coeus compatibility 
CREATE OR REPLACE VIEW OSP$COMM_MEMBER_EXPERTISE AS SELECT 
    COMM_MEMBERSHIPS.MEMBERSHIP_ID, 
    COMMITTEE.SEQUENCE_NUMBER, 
    COMM_MEMBER_EXPERTISE.RESEARCH_AREA_CODE, 
    COMM_MEMBER_EXPERTISE.UPDATE_TIMESTAMP, 
    COMM_MEMBER_EXPERTISE.UPDATE_USER
FROM COMM_MEMBER_EXPERTISE, COMM_MEMBERSHIPS, COMMITTEE
WHERE COMM_MEMBER_EXPERTISE.COMM_MEMBERSHIP_ID_FK = COMM_MEMBERSHIPS.COMM_MEMBERSHIP_ID
  AND COMM_MEMBERSHIPS.COMMITTEE_ID_FK = COMMITTEE.ID;

  
CREATE OR REPLACE VIEW OSP$COMM_RESEARCH_AREAS AS SELECT
  COMMITTEE.COMMITTEE_ID,
  COMM_RESEARCH_AREAS.RESEARCH_AREA_CODE,
  COMM_RESEARCH_AREAS.UPDATE_TIMESTAMP,
  COMM_RESEARCH_AREAS.UPDATE_USER
FROM COMM_RESEARCH_AREAS, COMMITTEE
WHERE COMM_RESEARCH_AREAS.COMMITTEE_ID_FK = COMMITTEE.ID;  


-- View for Coeus compatibility 
CREATE OR REPLACE VIEW OSP$COMM_SCHEDULE AS SELECT 
    COMM_SCHEDULE.SCHEDULE_ID, 
    COMMITTEE.COMMITTEE_ID, 
    COMM_SCHEDULE.SCHEDULED_DATE, 
    COMM_SCHEDULE.PLACE, 
    COMM_SCHEDULE.TIME, 
    COMM_SCHEDULE.PROTOCOL_SUB_DEADLINE, 
    COMM_SCHEDULE.SCHEDULE_STATUS_CODE, 
    COMM_SCHEDULE.MEETING_DATE, 
    COMM_SCHEDULE.START_TIME, 
    COMM_SCHEDULE.END_TIME, 
    COMM_SCHEDULE.AGENDA_PROD_REV_DATE, 
    COMM_SCHEDULE.MAX_PROTOCOLS, 
    COMM_SCHEDULE.COMMENTS, 
    COMM_SCHEDULE.UPDATE_TIMESTAMP, 
    COMM_SCHEDULE.UPDATE_USER
FROM COMM_SCHEDULE, COMMITTEE
WHERE COMM_SCHEDULE.COMMITTEE_ID_FK = COMMITTEE.ID;
