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
CREATE TABLE FP_DOC_STATUS_T (
        FDOC_STATUS_CD                 VARCHAR2(2) CONSTRAINT FP_DOC_STATUS_TN1 NOT NULL,
        OBJ_ID                         VARCHAR2(36) DEFAULT SYS_GUID() CONSTRAINT FP_DOC_STATUS_TN2 NOT NULL,
        VER_NBR                        NUMBER(8) DEFAULT 1 CONSTRAINT FP_DOC_STATUS_TN3 NOT NULL,
        FDOC_STATUS_NM                 VARCHAR2(10),
     CONSTRAINT FP_DOC_STATUS_TP1 PRIMARY KEY (
        FDOC_STATUS_CD) ,
     CONSTRAINT FP_DOC_STATUS_TC0 UNIQUE (OBJ_ID) 
);
