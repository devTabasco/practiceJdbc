/* ������ ��¥ ���ù޾� ��ȸ�ϴ� ���� */
SELECT * 
FROM TODOLIST
WHERE MRID = 'changyong' AND (SUBSTR(STARTDATE, 1, 8) <= '20221224' AND SUBSTR(ENDDATE, 1, 8) >= '20221224');

/* TODO �÷� Ȯ�� �� update �׽�Ʈ */
select *
from TODO;

UPDATE TODO
SET TD_DATE = '20221103000000';

select *
from TODO
WHERE TD_MRID = 'changyong' AND TO_CHAR(TD_DATE,'YYYYMMDDHH24MISS') = '20221103000000' AND TO_CHAR(TD_EDATE, 'YYYYMMDDHH24MISS') = '20221117000000' AND TD_CONTENT = '��������';