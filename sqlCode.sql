/* DDL(Data Definition Language)
    : κ΄?κ³ν ?°?΄?°λ² μ΄?€ κ°μ²΄λ₯? ??±, ?? , ?­? 
    : CREATE > ALTER, DROP
    Syntex
    [CREATE | ALTER | DROP] [OBJRCT-TYPE] [OBJECT-NAME]
*/
/* ACCOUNT 
    Syntex
    CREATE USER [ID] IDENTIFIED BY "[PW]"//?¬?©
    (DEFAULT TABLESPACE [TABLESPACE-NAME]); --usersλ‘? λ°°μ //?¬?©
    (TEMPORARY TABLESPACE [TABLESPACE-NAME]);
    (QUOTA [INT][Kb | Mb | Gb | UNLIBITED] ON [TABLESPACE-NAME]);//?¬?©
    (ACCOUNT [UNLOCK | LOCK])
*/

/* κ³μ  ?΄? ?λ‘μΈ?€
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
where stores.sr_owcode = owners.ow_code and owners.ow_name = '?μ°½μ©')
;

select *
from stores, owners
where stores.sr_owcode = owners.ow_code and owners.ow_name = '?μ°½μ©';

-- κ³μ ??±
create user tododba
IDENTIFIED BY "12345"
default tablespace users
quota UNLIMITED on users;

-- κΆνμ£ΌκΈ°
GRANT CREATE SESSION, DBA TO tododba;

commit;

-- κ³μ ??±2
create user changyongdev
IDENTIFIED BY "1234"
default tablespace users
quota 20M on users;

-- κΆνμ£ΌκΈ°2
GRANT CREATE SESSION, resource TO changyongdev;
grant alter any table to tododba;

ALTER USER changyong IDENTIFIED BY "12345";

grant create any table to keonho;
revoke create view from changyong;
drop table keonho.members;
grant select, insert, update on changyong.todo to tododba;
/* Constraint ? ?½μ‘°κ±΄ : DML κ΅¬λ¬Έ? ? ?©
    1. Not null
    2. Unique
    3. PRIMARY KEY : ??? ??΄λΈμ 1κ°μ PKλ§? μ‘΄μ¬
                    ? μ½λ? ? ?Ό?±? ?λ³΄νλ©? Null? ??©X
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
insert into dba1.categories(cg_code, cg_name) values ('SN','?€?΅λ₯?');
insert into dba1.categories(cg_code, cg_name) values ('BV','?λ£?');
insert into dba1.categories(cg_code, cg_name) values ('BK','λ² μ΄μ»€λ¦¬');
insert into dba1.categories(cg_code, cg_name) values ('DN','???');
insert into dba1.categories(cg_code, cg_name) values ('AC','μ£Όλ₯');
insert into dba1.categories(cg_code, cg_name) values ('EX','?¨μ’?');
insert into dba1.categories(cg_code, cg_name) values ('SO','?? ');
insert into dba1.categories(cg_code, cg_name) values ('AS','?λ§€κ??₯');
insert into dba1.categories(cg_code, cg_name) values ('BS','?λ§€μ? ');

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
insert into dba1.products(pr_code, pr_name) values ('LPA01','λ‘??° ?λͺ¬λ λΉΌλΉΌλ‘?');
insert into dba1.products(pr_code, pr_name) values ('LPB01','λ‘??° λΉΌλΉΌλ‘?');
insert into dba1.products(pr_code, pr_name) values ('LPN01','λ‘??° ?? λΉΌλΉΌλ‘?');
insert into dba1.products(pr_code, pr_name) values ('LPM01','λ‘??° λ―ΌνΈ λΉΌλΉΌλ‘?');
insert into dba1.products(pr_code, pr_name) values ('LPC01','λ‘??° μΏ μ€?¬ λΉΌλΉΌλ‘?');
insert into dba1.products(pr_code, pr_name) values ('SBP01','?¬μΌλͺ¬ ?ΌμΉ΄μΈλΉ?');
insert into dba1.products(pr_code, pr_name) values ('SBP02','?¬μΌλͺ¬ ?Ό?΄μΈλΉ΅');
insert into dba1.products(pr_code, pr_name) values ('SBP03','?¬μΌλͺ¬ ??΄λ¦¬λΉ΅');
insert into dba1.products(pr_code, pr_name) values ('SBP04','?¬μΌλͺ¬ κΌ¬λ?κΈ°λΉ΅');
insert into dba1.products(pr_code, pr_name) values ('SBP05','?¬μΌλͺ¬ λ²ν°?λΉ?');
insert into dba1.products(pr_code, pr_name) values ('MMC01','λ§€μΌ μ΄μ½?°? ');
insert into dba1.products(pr_code, pr_name) values ('MMS01','λ§€μΌ ?ΈκΈ°μ°? ');
insert into dba1.products(pr_code, pr_name) values ('MMB01','λ§€μΌ λ°λ??°? ');
insert into dba1.products(pr_code, pr_name) values ('MMM01','λ§€μΌ λ―ΌνΈμ΄μ½?°? ');
insert into dba1.products(pr_code, pr_name) values ('MMC02','λ§€μΌ μ΄μ½λ°λ??°? ');

insert into dba1.products(pr_code, pr_name) values ('AC001','μ°Έμ΄?¬');
insert into dba1.products(pr_code, pr_name) values ('AC002','μ²μμ²λΌ');
insert into dba1.products(pr_code, pr_name) values ('AC003','μ§λ‘?΄μ¦λ°±');
insert into dba1.products(pr_code, pr_name) values ('AC004','μ²??');
insert into dba1.products(pr_code, pr_name) values ('AC005','λ°±ν?λ³?');

insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values();

commit;
insert into dba1.owners(ow_code,ow_name) values('001','κΉ?λ―Όκ·');
insert into dba1.owners(ow_code,ow_name) values('002','?μ°½μ©');
insert into dba1.owners(ow_code,ow_name) values('003','?Ό??μ§?');
insert into dba1.owners(ow_code,ow_name) values('004','λ°κ±΄?Έ');

insert into dba1.customers(cm_code,cm_name,cm_phone) values('0000','λΉν?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0001','? ??°','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0002','?‘???','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0003','κΉ??Έ?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0004','?©??Έ','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0005','?΄? ?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0006','?μ€??','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0007','κΉ???','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0008','?΄?λ¦?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0009','?μ§?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0010','?€μ§??','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0011','κΉ?μ§??','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0012','λ°μ΄λ‘?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0013','κ³½λ??','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0014','? ?μ€?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0015','?΄κ·ν','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0016','μ£Όμ±?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0017','κΉ?μ€??','01000000000');

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
/*??λΆλ₯μ½λ
- κ³Όμ(SN)
- ?λ£?(BV)
- λ² μ΄μ»€λ¦¬(BK)
- ???(DN)
- μ£Όλ₯(AC)

????μ½λ
- ?¨μ’?(EX)
- ?? (SO)
- ?λ§€κ??₯(AS)
- ?λ§€μ? (BS)
*/

update AH
set AH_state = -1
where ah_mrid = 'KEONHO';
commit;