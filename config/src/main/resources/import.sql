--
-- The MIT License
-- Copyright (c) 2011 Kuali Mobility Team
--
-- Permission is hereby granted, free of charge, to any person obtaining a copy
-- of this software and associated documentation files (the "Software"), to deal
-- in the Software without restriction, including without limitation the rights
-- to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
-- copies of the Software, and to permit persons to whom the Software is
-- furnished to do so, subject to the following conditions:
--
-- The above copyright notice and this permission notice shall be included in
-- all copies or substantial portions of the Software.
--
-- THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
-- IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
-- FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
-- AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
-- LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
-- OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
-- THE SOFTWARE.
--



-- INSERT DEVICES
INSERT INTO KME_DVCS_T (ID, DVCID, NM, PST_TS, REGID, TYP, USR, VER_NBR) VALUES (1,'A2Zq5TuWhvRgf2VLgQtz','Device A','2014-03-30 03:40:16','pyrBBFNEDnTE3zlxwr6k','iOS','john',0);
INSERT INTO KME_DVCS_T (ID, DVCID, NM, PST_TS, REGID, TYP, USR, VER_NBR) VALUES (2,'Mn5BfkPfXCHDYITe8Mk8','Device B iPad','2014-03-30 03:40:06','1DmGB8xeZLAXRZOss0HH','iOS','jane',0);
INSERT INTO KME_DVCS_T (ID, DVCID, NM, PST_TS, REGID, TYP, USR, VER_NBR) VALUES (4,'SRHTsQ6qHDDe1F22sR5w','Device D Galaxy','2014-01-28 07:40:06','h7KJgPSxiltWyL65Ydve','Android','jane',0);
INSERT INTO KME_DVCS_T (ID, DVCID, NM, PST_TS, REGID, TYP, USR, VER_NBR) VALUES (5,'64bj1iAbOhCWhB9bjN6h','Device E Z10','2013-10-29 01:40:16','sJi1IwvXDubkG6ldUVXc','BlackBerry','',0);
INSERT INTO KME_DVCS_T (ID, DVCID, NM, PST_TS, REGID, TYP, USR, VER_NBR) VALUES (6,'JtZNLN9EgXuD04gCgtXb','Device F Lumia','2013-04-29 13:40:16','wf6Ac2SZTdKaPlKC9sZU','WindowsMobile','wendy',0);
insert into HIBERNATE_SEQUENCES (SEQUENCE_NAME,SEQUENCE_NEXT_HI_VALUE) values ('KME_DVCS_T',7);


-- Insert push messages
INSERT INTO KME_PSH_MSG_T (ID, EMR, MSG, PST_TS, RCPT, SNDR, TTL, URL, VER_NBR) VALUES (1,0x01,'A Tornado warning has been issued for the area near campus and downtown. Please report to a shelter until all-clear has been sounded.','2014-04-29 13:40:26',1000,'mtwagner','Tornado Warning','http://protect.iu.edu',0);
INSERT INTO KME_PSH_MSG_T (ID, EMR, MSG, PST_TS, RCPT, SNDR, TTL, URL, VER_NBR) VALUES (2,0x00,'Once you log into a CAS protected tool (such as My Classes or Classifieds) the push notifications you receive will be tailored specifically to you.','2014-04-29 13:40:26',100,'barnetjm','Log into a protected tool','',0);
INSERT INTO KME_PSH_MSG_T (ID, EMR, MSG, PST_TS, RCPT, SNDR, TTL, URL, VER_NBR) VALUES (3,0x00,'Check out the new {BLANK} in KME.','2014-05-19 17:25:34',-1,'KME_PUSH','New Feature in KME','',1);
INSERT INTO HIBERNATE_SEQUENCES (SEQUENCE_NAME,SEQUENCE_NEXT_HI_VALUE) values ('KME_PSH_MSG_T',4);

-- Insert senders
INSERT INTO KME_PSH_SNDR_T (ID, DSCRP, HDN, NM, PST_TS, SENDER_KEY, SNM, USR, VER_NBR) VALUES (1,'General Push notifications from KME',0x01,'KME Notifications',NULL,'3AbHRDjirFn2hvii4Pq3','KME_PUSH',NULL,0);
INSERT INTO KME_PSH_SNDR_T (ID, DSCRP, HDN, NM, PST_TS, SENDER_KEY, SNM, USR, VER_NBR) VALUES (2,'Grade Notifications from courses.',0x00,'KME Grades',NULL,'RS5XcyVYoHSgnLVY2ZZw','KME_GRADES',NULL,0);
INSERT INTO KME_PSH_SNDR_T (ID, DSCRP, HDN, NM, PST_TS, SENDER_KEY, SNM, USR, VER_NBR) VALUES (3,'Emergency Alert Notifications.',0x01,'KME Emergency Alerts',NULL,'qjiDTDHoQprnavcE4YHK','KME_ALERTS',NULL,0);
INSERT INTO KME_PSH_SNDR_T (ID, DSCRP, HDN, NM, PST_TS, SENDER_KEY, SNM, USR, VER_NBR) VALUES (4,'Dining related notifications.',0x00,'KME Dining',NULL,'IEix3BlRtveRkteiZx5X','KME_DINING',NULL,0);
INSERT INTO KME_PSH_SNDR_T (ID, DSCRP, HDN, NM, PST_TS, SENDER_KEY, SNM, USR, VER_NBR) VALUES (5,'Writer instance default sender',0x00,'KME Writer sender',NULL,'qp9IiQNrA81gsNIBvtNE','WRITER_DEFAULT',NULL,0);
insert into HIBERNATE_SEQUENCES (SEQUENCE_NAME,SEQUENCE_NEXT_HI_VALUE) values ('KME_PSH_MSG_T',4);

-- Insert sample push messages
INSERT INTO KME_PUSHMESSAGE_T (ID, LANG, MSG, PST_TS, TTL, VER_NBR) VALUES (1,'en','Check out the new {BLANK} in KME.',NULL,'New Feature in KME',0);
INSERT INTO KME_PUSHMESSAGE_T (ID, LANG, MSG, PST_TS, TTL, VER_NBR) VALUES (2,'en','Once you log into a CAS protected tool (such as My Classes or Classifieds) the push notifications you receive will be tailored specifically to you.',NULL,'Log into a protected tool',0);
INSERT INTO KME_PUSHMESSAGE_T (ID, LANG, MSG, PST_TS, TTL, VER_NBR) VALUES (3,'en','A Tornado warning has been issued for the area near campus and downtown. Please report to a shelter until all-clear has been sounded.',NULL,'Tornado Warning',0);
INSERT INTO KME_PUSHMESSAGE_T (ID, LANG, MSG, PST_TS, TTL, VER_NBR) VALUES (4,'af','ŉ Tornado waarskuwing uitgereik is vir die gebied naby die kampus en die sentrum. Meld asseblief na ŉ skuiling totdat all-duidelik is geklink.',NULL,'Tornado waarskuwing',0);
INSERT INTO HIBERNATE_SEQUENCES (SEQUENCE_NAME,SEQUENCE_NEXT_HI_VALUE) values ('KME_PUSHMESSAGE_T',5);


-- Create tables for authentication
CREATE TABLE USERS(
	ID BIGINT(20) NOT NULL PRIMARY KEY,
	USERNAME varchar(64) DEFAULT NULL,
	PASSWORD varchar(64) DEFAULT NULL,
	ENABLED bit(1) DEFAULT 1,
	UNIQUE KEY UN_USERNAME (USERNAME)
);

-- password is admin
INSERT INTO USERS VALUES(1, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1);
INSERT INTO USERS VALUES(2, 'rest', '4e738ca5563c06cfd0018299933d58db1dd8bf97f6973dc99bf6cdc64b5550bd', 1);
INSERT INTO HIBERNATE_SEQUENCES (SEQUENCE_NAME,SEQUENCE_NEXT_HI_VALUE) values ('USERS',3);


-- Create table ROLES
CREATE TABLE ROLES(
	ID BIGINT(20) NOT NULL PRIMARY KEY,
	NM varchar(64) DEFAULT NULL,
	DESCRIPTION varchar(255) DEFAULT NULL,
	UNIQUE KEY UN_NAME (NM)
);

INSERT INTO ROLES VALUES (1, "ROLE_REST_API", "Role allowing access to rest services");
INSERT INTO ROLES VALUES (2, "ROLE_PUSH_USER", "Role allowing access to the push fron end");
INSERT INTO HIBERNATE_SEQUENCES (SEQUENCE_NAME,SEQUENCE_NEXT_HI_VALUE) values ('ROLES',3);

-- Create USER_ROLES Table
CREATE TABLE USER_ROLES(
	ID BIGINT(20) NOT NULL PRIMARY KEY,
	USER_ID BIGINT(255) NOT NULL,
	ROLE_ID BIGINT(255) NOT NULL,
	UNIQUE KEY UN_ROLE (USER_ID,ROLE_ID),
	CONSTRAINT FK_USER_ID FOREIGN KEY (USER_ID) REFERENCES USERS(ID),
	CONSTRAINT FK_ROLE_ID FOREIGN KEY (ROLE_ID) REFERENCES ROLES(ID)
);

INSERT INTO USER_ROLES VALUES (1, 1, 1);
INSERT INTO USER_ROLES VALUES (2, 1, 2);
INSERT INTO USER_ROLES VALUES (3, 2, 1);
INSERT INTO HIBERNATE_SEQUENCES (SEQUENCE_NAME,SEQUENCE_NEXT_HI_VALUE) values ('USER_ROLES',4);
