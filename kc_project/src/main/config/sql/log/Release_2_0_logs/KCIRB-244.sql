
ALTER table COMM_RESEARCH_AREAS DROP CONSTRAINT FK_COMM_RESEARCH_AREAS1;

ALTER table COMM_RESEARCH_AREAS rename column COMMITTEE_ID to COMMITTEE_ID_FK;
ALTER table COMM_RESEARCH_AREAS add (COMMITTEE_ID VARCHAR2(15) DEFAULT '' NOT NULL);

ALTER TABLE COMM_RESEARCH_AREAS 
ADD CONSTRAINT FK_COMM_RESEARCH_AREAS1
FOREIGN KEY (COMMITTEE_ID_FK) 
REFERENCES COMMITTEE (ID);

CREATE OR REPLACE VIEW OSP$COMM_RESEARCH_AREAS AS SELECT 
  COMMITTEE_ID,
  RESEARCH_AREA_CODE,
  UPDATE_TIMESTAMP, 
  UPDATE_USER
FROM COMM_RESEARCH_AREAS;

COMMIT;

