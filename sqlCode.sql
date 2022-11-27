/* DDL(Data Definition Language)
    : Í¥?Í≥ÑÌòï ?ç∞?ù¥?Ñ∞Î≤†Ïù¥?ä§ Í∞ùÏ≤¥Î•? ?Éù?Ñ±, ?àò?†ï, ?Ç≠?†ú
    : CREATE > ALTER, DROP
    Syntex
    [CREATE | ALTER | DROP] [OBJRCT-TYPE] [OBJECT-NAME]
*/
/* ACCOUNT 
    Syntex
    CREATE USER [ID] IDENTIFIED BY "[PW]"//?Ç¨?ö©
    (DEFAULT TABLESPACE [TABLESPACE-NAME]); --usersÎ°? Î∞∞Ï†ï//?Ç¨?ö©
    (TEMPORARY TABLESPACE [TABLESPACE-NAME]);
    (QUOTA [INT][Kb | Mb | Gb | UNLIBITED] ON [TABLESPACE-NAME]);//?Ç¨?ö©
    (ACCOUNT [UNLOCK | LOCK])
*/

/* Í≥ÑÏ†ï ?ö¥?òÅ ?îÑÎ°úÏÑ∏?ä§
    TEAM : DBA, DEV * 4
    DBA : changyong <-- SYS
        : CREATE SESSION : CONNECT ROLE : 
        : DBA ROLE
    DEV : mk, ej, gh, cy <-- DBA
        : CREATE SESSION : CONNECT ROLE :
        : RESOURCE ROLE
*/
select sr_name
from (
select stores.sr_name, stores.sr_locate, owners.ow_name as ownersName
from stores, owners
where stores.sr_owcode = owners.ow_code and owners.ow_name = '?ûÑÏ∞ΩÏö©')
;

select *
from stores, owners
where stores.sr_owcode = owners.ow_code and owners.ow_name = '?ûÑÏ∞ΩÏö©';

-- Í≥ÑÏ†ï?Éù?Ñ±
create user tododba
IDENTIFIED BY "12345"
default tablespace users
quota UNLIMITED on users;

-- Í∂åÌïúÏ£ºÍ∏∞
GRANT CREATE SESSION, DBA TO tododba;

commit;

-- Í≥ÑÏ†ï?Éù?Ñ±2
create user changyongdev
IDENTIFIED BY "1234"
default tablespace users
quota 20M on users;

-- Í∂åÌïúÏ£ºÍ∏∞2
GRANT CREATE SESSION, resource TO changyongdev;
grant alter any table to tododba;

ALTER USER changyong IDENTIFIED BY "12345";

grant create any table to keonho;
revoke create view from changyong;
drop table keonho.members;
grant select, insert, update on changyong.todo to tododba;
/* Constraint ?†ú?ïΩÏ°∞Í±¥ : DML Íµ¨Î¨∏?óê ?†Å?ö©
    1. Not null
    2. Unique
    3. PRIMARY KEY : ?ïò?Çò?ùò ?Öå?ù¥Î∏îÏóê 1Í∞úÏùò PKÎß? Ï°¥Ïû¨
                    ?†àÏΩîÎìú?ùò ?ú†?ùº?Ñ±?ùÑ ?ôïÎ≥¥ÌïòÎ©? Null?ùÑ ?óà?ö©X
    4. FK
    5. DF
    6. 

*/

select * from privs to keonho;

update mr
set password = '4321'
where member_id = 'KEONHO';

commit;

alter table changyong.members
ADD constraint MR_ID_PK primary key(member_id)
add constraint MR_PHONE_UK UNIQUE(phone_number);

grant select, insert, update on dba1.stores to mingyu;
grant select, insert, update on dba1.stores to changyongdev;
grant select, insert, update on dba1.stores to keonho;
grant select, insert, update on dba1.stores to eunjin18;

select * from SESSION_PRIVS;
select * from user_tab_privs;

delete
from products;

ALTER TABLE products DROP CONSTRAINT pr_cgcode_fk;
ALTER TABLE products DROP CONSTRAINT pr_status_fk;

commit;

/* ACCESSHISTORY :: AH */
--TABLE
create table ACCESSHISTORY(
AH_MRID   NVARCHAR(20),
AH_DATE    DATE,
AH_STATE   NUMBER(1,0)
)tablespace users;

create public synonym AH for changyong.ACCESSHISTORY;

create table categories(
AH_MRID   NVARCHAR(20),
AH_DATE    DATE,
AH_STATE   NUMBER(1,0)
)tablespace users;

create public synonym AH for changyong.ACCESSHISTORY;

--CONSTRAINT(add constraint)
add constraint foreign key () references ();

--GRANT :: alter any table
grant alter any table to MINGYU;
--CONSTRAINT(modify)
alter table changyong.ACCESSHISTORY
modify AH_DATE DEFAULT sysdate
modify AH_STATE not null;

alter table changyong.TODO
modify TD_FEEDBACK DEFAULT 'NONE'
modify TD_STATE DEFAULT 'B'
modify TD_ACTIVATION DEFAULT 'A';
commit;

select sysdate from dual;
--insert
insert into dba1.categories(cg_code, cg_name) values ('SN','?ä§?ÇµÎ•?');
insert into dba1.categories(cg_code, cg_name) values ('BV','?ùåÎ£?');
insert into dba1.categories(cg_code, cg_name) values ('BK','Î≤†Ïù¥Ïª§Î¶¨');
insert into dba1.categories(cg_code, cg_name) values ('DN','?Éù?ïÑ?íà');
insert into dba1.categories(cg_code, cg_name) values ('AC','Ï£ºÎ•ò');
insert into dba1.categories(cg_code, cg_name) values ('EX','?ã®Ï¢?');
insert into dba1.categories(cg_code, cg_name) values ('SO','?íà?†à');
insert into dba1.categories(cg_code, cg_name) values ('AS','?åêÎß§Í??ä•');
insert into dba1.categories(cg_code, cg_name) values ('BS','?åêÎß§Ïòà?†ï');

select * from dba1.categories;

alter table dba1.products
modify pr_name NVARCHAR2(20);

insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('LPA01','004','500','1200','AS','SN','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('LPB01','004','500','1200','AS','SN','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('LPN01','004','500','1200','AS','SN','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('LPM01','004','500','1200','AS','SN','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('LPC01','004','500','1200','AS','SN','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('SBP01','004','400','1000','AS','BK','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('SBP02','004','500','1400','AS','BK','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('SBP03','004','600','1250','AS','BK','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('SBP04','004','450','1300','AS','BK','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('SBP05','004','700','1500','AS','BK','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('MMC01','004','400','1000','AS','BV','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('MMS01','004','500','1400','AS','BV','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('MMB01','004','600','1250','AS','BV','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('MMM01','004','450','1300','AS','BV','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('MMC02','004','700','1500','AS','BV','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('AC001','004','400','1000','AS','AC','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('AC002','004','500','1400','AS','AC','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('AC003','004','600','1250','AS','AC','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('AC004','004','450','1300','AS','AC','30');
insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values ('AC005','004','700','1500','AS','AC','30');
commit;



-----------------------
insert into dba1.products(pr_code, pr_name) values ('LPA01','Î°??ç∞ ?ïÑÎ™¨Îìú ÎπºÎπºÎ°?');
insert into dba1.products(pr_code, pr_name) values ('LPB01','Î°??ç∞ ÎπºÎπºÎ°?');
insert into dba1.products(pr_code, pr_name) values ('LPN01','Î°??ç∞ ?àÑ?ìú ÎπºÎπºÎ°?');
insert into dba1.products(pr_code, pr_name) values ('LPM01','Î°??ç∞ ÎØºÌä∏ ÎπºÎπºÎ°?');
insert into dba1.products(pr_code, pr_name) values ('LPC01','Î°??ç∞ Ïø†Ïï§?Å¨ ÎπºÎπºÎ°?');
insert into dba1.products(pr_code, pr_name) values ('SBP01','?è¨ÏºìÎ™¨ ?îºÏπ¥Ï∏ÑÎπ?');
insert into dba1.products(pr_code, pr_name) values ('SBP02','?è¨ÏºìÎ™¨ ?ùº?ù¥Ï∏ÑÎπµ');
insert into dba1.products(pr_code, pr_name) values ('SBP03','?è¨ÏºìÎ™¨ ?åå?ù¥Î¶¨Îπµ');
insert into dba1.products(pr_code, pr_name) values ('SBP04','?è¨ÏºìÎ™¨ Íº¨Î?Í∏∞Îπµ');
insert into dba1.products(pr_code, pr_name) values ('SBP05','?è¨ÏºìÎ™¨ Î≤ÑÌÑ∞?îåÎπ?');
insert into dba1.products(pr_code, pr_name) values ('MMC01','Îß§Ïùº Ï¥àÏΩî?ö∞?ú†');
insert into dba1.products(pr_code, pr_name) values ('MMS01','Îß§Ïùº ?î∏Í∏∞Ïö∞?ú†');
insert into dba1.products(pr_code, pr_name) values ('MMB01','Îß§Ïùº Î∞îÎÇò?Çò?ö∞?ú†');
insert into dba1.products(pr_code, pr_name) values ('MMM01','Îß§Ïùº ÎØºÌä∏Ï¥àÏΩî?ö∞?ú†');
insert into dba1.products(pr_code, pr_name) values ('MMC02','Îß§Ïùº Ï¥àÏΩîÎ∞îÎÇò?Çò?ö∞?ú†');

insert into dba1.products(pr_code, pr_name) values ('AC001','Ï∞∏Ïù¥?ä¨');
insert into dba1.products(pr_code, pr_name) values ('AC002','Ï≤òÏùåÏ≤òÎüº');
insert into dba1.products(pr_code, pr_name) values ('AC003','ÏßÑÎ°ú?ù¥Ï¶àÎ∞±');
insert into dba1.products(pr_code, pr_name) values ('AC004','Ï≤??ïò');
insert into dba1.products(pr_code, pr_name) values ('AC005','Î∞±Ìôî?àòÎ≥?');

insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values();

commit;
insert into dba1.owners(ow_code,ow_name) values('001','Íπ?ÎØºÍ∑ú');
insert into dba1.owners(ow_code,ow_name) values('002','?ûÑÏ∞ΩÏö©');
insert into dba1.owners(ow_code,ow_name) values('003','?óº??Ïß?');
insert into dba1.owners(ow_code,ow_name) values('004','Î∞ïÍ±¥?ò∏');

insert into dba1.customers(cm_code,cm_name,cm_phone) values('0000','ÎπÑÌöå?õê','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0001','?†ï?òÑ?ö∞','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0002','?Ü°???òú','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0003','Íπ??ò∏?õê','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0004','?ô©?òÅ?ò∏','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0005','?ù¥?†ï?ïú','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0006','?ôçÏ§??Éù','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0007','Íπ??ïò?äò','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0008','?ù¥?òàÎ¶?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0009','?õàÏß?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0010','?ú§Ïß??àò','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0011','Íπ?Ïß??õÖ','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0012','Î∞ïÏ¥àÎ°?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0013','Í≥ΩÎ??õà','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0014','?†ï?òÅÏ§?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0015','?ù¥Í∑úÌôò','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0016','Ï£ºÏÑ±?òÑ','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0017','Íπ?Ï§??Ñù','01000000000');

insert into dba1.member(me_cmcode,me_owcode) values('0000','001');
insert into dba1.member(me_cmcode,me_owcode) values('0000','002');
insert into dba1.member(me_cmcode,me_owcode) values('0000','003');
insert into dba1.member(me_cmcode,me_owcode) values('0000','004');
insert into dba1.member(me_cmcode,me_owcode) values('0001','001');
insert into dba1.member(me_cmcode,me_owcode) values('0002','001');
insert into dba1.member(me_cmcode,me_owcode) values('0003','001');
insert into dba1.member(me_cmcode,me_owcode) values('0004','001');
insert into dba1.member(me_cmcode,me_owcode) values('0005','002');
insert into dba1.member(me_cmcode,me_owcode) values('0006','002');
insert into dba1.member(me_cmcode,me_owcode) values('0007','002');
insert into dba1.member(me_cmcode,me_owcode) values('0008','002');
insert into dba1.member(me_cmcode,me_owcode) values('0009','003');
insert into dba1.member(me_cmcode,me_owcode) values('0010','003');
insert into dba1.member(me_cmcode,me_owcode) values('0011','003');
insert into dba1.member(me_cmcode,me_owcode) values('0012','003');
insert into dba1.member(me_cmcode,me_owcode) values('0013','004');
insert into dba1.member(me_cmcode,me_owcode) values('0014','004');
insert into dba1.member(me_cmcode,me_owcode) values('0015','004');
insert into dba1.member(me_cmcode,me_owcode) values('0016','004');

commit;
/*?ÉÅ?íàÎ∂ÑÎ•òÏΩîÎìú
- Í≥ºÏûê(SN)
- ?ùåÎ£?(BV)
- Î≤†Ïù¥Ïª§Î¶¨(BK)
- ?Éù?ïÑ?íà(DN)
- Ï£ºÎ•ò(AC)

?ÉÅ?íà?ÉÅ?ÉúÏΩîÎìú
- ?ã®Ï¢?(EX)
- ?íà?†à(SO)
- ?åêÎß§Í??ä•(AS)
- ?åêÎß§Ïòà?†ï(BS)
*/

update AH
set AH_state = -1
where ah_mrid = 'KEONHO';
commit;