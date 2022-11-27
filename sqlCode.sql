/* DDL(Data Definition Language)
    : �?계형 ?��?��?��베이?�� 객체�? ?��?��, ?��?��, ?��?��
    : CREATE > ALTER, DROP
    Syntex
    [CREATE | ALTER | DROP] [OBJRCT-TYPE] [OBJECT-NAME]
*/
/* ACCOUNT 
    Syntex
    CREATE USER [ID] IDENTIFIED BY "[PW]"//?��?��
    (DEFAULT TABLESPACE [TABLESPACE-NAME]); --users�? 배정//?��?��
    (TEMPORARY TABLESPACE [TABLESPACE-NAME]);
    (QUOTA [INT][Kb | Mb | Gb | UNLIBITED] ON [TABLESPACE-NAME]);//?��?��
    (ACCOUNT [UNLOCK | LOCK])
*/

/* 계정 ?��?�� ?��로세?��
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
where stores.sr_owcode = owners.ow_code and owners.ow_name = '?��창용')
;

select *
from stores, owners
where stores.sr_owcode = owners.ow_code and owners.ow_name = '?��창용';

-- 계정?��?��
create user tododba
IDENTIFIED BY "12345"
default tablespace users
quota UNLIMITED on users;

-- 권한주기
GRANT CREATE SESSION, DBA TO tododba;

commit;

-- 계정?��?��2
create user changyongdev
IDENTIFIED BY "1234"
default tablespace users
quota 20M on users;

-- 권한주기2
GRANT CREATE SESSION, resource TO changyongdev;
grant alter any table to tododba;

ALTER USER changyong IDENTIFIED BY "12345";

grant create any table to keonho;
revoke create view from changyong;
drop table keonho.members;
grant select, insert, update on changyong.todo to tododba;
/* Constraint ?��?��조건 : DML 구문?�� ?��?��
    1. Not null
    2. Unique
    3. PRIMARY KEY : ?��?��?�� ?��?��블에 1개의 PK�? 존재
                    ?��코드?�� ?��?��?��?�� ?��보하�? Null?�� ?��?��X
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
insert into dba1.categories(cg_code, cg_name) values ('SN','?��?���?');
insert into dba1.categories(cg_code, cg_name) values ('BV','?���?');
insert into dba1.categories(cg_code, cg_name) values ('BK','베이커리');
insert into dba1.categories(cg_code, cg_name) values ('DN','?��?��?��');
insert into dba1.categories(cg_code, cg_name) values ('AC','주류');
insert into dba1.categories(cg_code, cg_name) values ('EX','?���?');
insert into dba1.categories(cg_code, cg_name) values ('SO','?��?��');
insert into dba1.categories(cg_code, cg_name) values ('AS','?��매�??��');
insert into dba1.categories(cg_code, cg_name) values ('BS','?��매예?��');

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
insert into dba1.products(pr_code, pr_name) values ('LPA01','�??�� ?��몬드 빼빼�?');
insert into dba1.products(pr_code, pr_name) values ('LPB01','�??�� 빼빼�?');
insert into dba1.products(pr_code, pr_name) values ('LPN01','�??�� ?��?�� 빼빼�?');
insert into dba1.products(pr_code, pr_name) values ('LPM01','�??�� 민트 빼빼�?');
insert into dba1.products(pr_code, pr_name) values ('LPC01','�??�� 쿠앤?�� 빼빼�?');
insert into dba1.products(pr_code, pr_name) values ('SBP01','?��켓몬 ?��카츄�?');
insert into dba1.products(pr_code, pr_name) values ('SBP02','?��켓몬 ?��?��츄빵');
insert into dba1.products(pr_code, pr_name) values ('SBP03','?��켓몬 ?��?��리빵');
insert into dba1.products(pr_code, pr_name) values ('SBP04','?��켓몬 꼬�?기빵');
insert into dba1.products(pr_code, pr_name) values ('SBP05','?��켓몬 버터?���?');
insert into dba1.products(pr_code, pr_name) values ('MMC01','매일 초코?��?��');
insert into dba1.products(pr_code, pr_name) values ('MMS01','매일 ?��기우?��');
insert into dba1.products(pr_code, pr_name) values ('MMB01','매일 바나?��?��?��');
insert into dba1.products(pr_code, pr_name) values ('MMM01','매일 민트초코?��?��');
insert into dba1.products(pr_code, pr_name) values ('MMC02','매일 초코바나?��?��?��');

insert into dba1.products(pr_code, pr_name) values ('AC001','참이?��');
insert into dba1.products(pr_code, pr_name) values ('AC002','처음처럼');
insert into dba1.products(pr_code, pr_name) values ('AC003','진로?��즈백');
insert into dba1.products(pr_code, pr_name) values ('AC004','�??��');
insert into dba1.products(pr_code, pr_name) values ('AC005','백화?���?');

insert into dba1.storeproducts(sp_prcode,sp_owcode,sp_purchase_price,sp_sales_price,sp_status,sp_cgcode,sp_stock) values();

commit;
insert into dba1.owners(ow_code,ow_name) values('001','�?민규');
insert into dba1.owners(ow_code,ow_name) values('002','?��창용');
insert into dba1.owners(ow_code,ow_name) values('003','?��??�?');
insert into dba1.owners(ow_code,ow_name) values('004','박건?��');

insert into dba1.customers(cm_code,cm_name,cm_phone) values('0000','비회?��','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0001','?��?��?��','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0002','?��???��','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0003','�??��?��','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0004','?��?��?��','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0005','?��?��?��','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0006','?���??��','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0007','�??��?��','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0008','?��?���?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0009','?���?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0010','?���??��','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0011','�?�??��','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0012','박초�?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0013','곽�??��','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0014','?��?���?','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0015','?��규환','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0016','주성?��','01000000000');
insert into dba1.customers(cm_code,cm_name,cm_phone) values('0017','�?�??��','01000000000');

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
/*?��?��분류코드
- 과자(SN)
- ?���?(BV)
- 베이커리(BK)
- ?��?��?��(DN)
- 주류(AC)

?��?��?��?��코드
- ?���?(EX)
- ?��?��(SO)
- ?��매�??��(AS)
- ?��매예?��(BS)
*/

update AH
set AH_state = -1
where ah_mrid = 'KEONHO';
commit;