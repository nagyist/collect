INSERT INTO collect.ofc_user(id, username, password, enabled) 
	VALUES(1,'eko',md5('eko'),'Y')
GO

INSERT INTO collect.ofc_user_role(id, user_id, role) 
	VALUES(1, 1, 'ROLE_ADMIN')
GO

INSERT INTO collect.ofc_user(id, username, password, enabled) 
	VALUES(2,'user1',md5('user1'),'Y')
GO

INSERT INTO collect.ofc_user_role(id, user_id, role) 
	VALUES(2, 2, 'ROLE_ENTRY')
GO

INSERT INTO collect.ofc_user(id, username, password, enabled) 
	VALUES(3,'user2',md5('user2'),'Y')
GO

INSERT INTO collect.ofc_user_role(id, user_id, role) 
	VALUES(3, 3, 'ROLE_ENTRY')
GO


INSERT INTO collect.ofc_user(id, username, password, enabled) 
	VALUES(4,'user3',md5('user3'),'Y')
GO

INSERT INTO collect.ofc_user_role(id, user_id, role) 
	VALUES(4, 4, 'ROLE_ENTRY')
GO

INSERT INTO collect.ofc_user(id, username, password, enabled) 
	VALUES(5,'user4',md5('user4'),'Y')
GO

INSERT INTO collect.ofc_user_role(id, user_id, role) 
	VALUES(5, 5, 'ROLE_ENTRY')
GO

INSERT INTO collect.ofc_user(id, username, password, enabled) 
	VALUES(6,'user5',md5('user5'),'Y')
GO

INSERT INTO collect.ofc_user_role(id, user_id, role) 
	VALUES(6, 6, 'ROLE_ENTRY')
GO

